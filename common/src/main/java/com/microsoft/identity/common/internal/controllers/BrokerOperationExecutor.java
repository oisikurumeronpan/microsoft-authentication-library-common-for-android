//  Copyright (c) Microsoft Corporation.
//  All rights reserved.
//
//  This code is licensed under the MIT License.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files(the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions :
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.

package com.microsoft.identity.common.internal.controllers;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.microsoft.identity.common.exception.BaseException;
import com.microsoft.identity.common.exception.BrokerCommunicationException;
import com.microsoft.identity.common.exception.ClientException;
import com.microsoft.identity.common.exception.ErrorStrings;
import com.microsoft.identity.common.internal.broker.ipc.IIpcStrategy;
import com.microsoft.identity.common.internal.broker.ipc.BrokerOperationBundle;
import com.microsoft.identity.common.internal.commands.parameters.CommandParameters;
import com.microsoft.identity.common.internal.logging.Logger;
import com.microsoft.identity.common.internal.telemetry.Telemetry;
import com.microsoft.identity.common.internal.telemetry.events.ApiEndEvent;
import com.microsoft.identity.common.internal.telemetry.events.ApiStartEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Classes for executing IPC service operations.
 * It takes in a list of IIpcStrategy, and will try to connect to the broker with each strategy. one by one.
 * - If the current strategy succeeds, it will return the result right away.
 * - If the service returns an error, it will return the result right away.
 * - If the current strategy fails to connect to the targeted service, it will try the next one until the list is exhausted.
 */
public class BrokerOperationExecutor {

    private static final String TAG = BrokerOperationExecutor.class.getSimpleName();

    /**
     * Info of a service operation to be performed with available strategies.
     */
    public interface BrokerOperation<T> {

        /**
         * Trigger any prerequisite works before making the actual request.
         * This was added because the existing MSAL-Broker logic has a separate hello() call.
         */
        void performPrerequisites(final @NonNull IIpcStrategy strategy) throws BaseException;

        /**
         * Gets a BrokerOperationBundle bundle to pass to each IpcStrategies.
         */
        @NonNull BrokerOperationBundle getBundle();

        /**
         * Extracts the result object from a bundle returned by an IpcStrategy.
         * If the broker returns an error, this will throw an exception.
         */
        @NonNull T extractResultBundle(final @Nullable Bundle resultBundle) throws BaseException;

        /**
         * Returns method name (for logging/telemetry purpose).
         */
        @NonNull String getMethodName();

        /**
         * ID of the telemetry API event associated to this strategy task.
         * If this value returns null, no telemetry event will be emitted.
         */
        @Nullable String getTelemetryApiId();

        /**
         * A method that will be invoked before the success event is emitted.
         * If the calling operation wants to put any value in the success event, put it here.
         */
        void putValueInSuccessEvent(final @NonNull ApiEndEvent event,final @NonNull T result);
    }

    private final List<IIpcStrategy> mStrategies;

    /**
     * @param strategies list of IIpcStrategy to be invoked.
     */
    public BrokerOperationExecutor(final @NonNull List<IIpcStrategy> strategies) {
        mStrategies = strategies;
    }

    /**
     * A generic method that would initialize and iterate through available strategies.
     * It will return a result immediately if any of the strategy succeeds, or throw an exception if all of the strategies fails.
     */
    public <T extends CommandParameters, U> U execute(@Nullable final T parameters,
                                                      @NonNull final BrokerOperation<U> operation)
            throws BaseException {
        final String methodName = ":execute";

        emitOperationStartEvent(parameters, operation);

        if (mStrategies.size() == 0) {
            final ClientException exception = new ClientException(
                    ErrorStrings.BROKER_BIND_SERVICE_FAILED,
                    "No strategies can be used to connect to the broker.");
            emitOperationFailureEvent(operation, exception);
            throw exception;
        }

        final List<BrokerCommunicationException> communicationExceptionStack = new ArrayList<>();
        for (final IIpcStrategy strategy : mStrategies) {
            try {
                final U result = performStrategy(strategy, operation);
                emitOperationSuccessEvent(operation, result);
                return result;
            } catch (final BrokerCommunicationException communicationException) {
                // Fails to communicate to the . Try next strategy.
                communicationExceptionStack.add(communicationException);
            } catch (final BaseException exception) {
                emitOperationFailureEvent((BrokerOperation<U>) operation, exception);
                throw exception;
            }
        }

        final ClientException exception = new ClientException(
                ErrorStrings.BROKER_BIND_SERVICE_FAILED,
                "Unable to connect to the broker. Please refer to MSAL/Broker logs " +
                        "or suppressed exception (API 19+) for more details.");

        // This means that we've tried every strategies... log everything...
        for (final BrokerCommunicationException e : communicationExceptionStack) {
            Logger.error(TAG + methodName, e.getMessage(), e);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                exception.addSuppressed(e);
            }
        }

        emitOperationFailureEvent(operation, exception);
        throw exception;
    }

    private <T extends CommandParameters, U> void emitOperationStartEvent(@Nullable final T parameters,
                                                                          @NonNull final BrokerOperation<U> operation) {
        if (operation.getTelemetryApiId() != null) {
            Telemetry.emit(
                    new ApiStartEvent()
                            .putProperties(parameters)
                            .putApiId(operation.getTelemetryApiId())
            );
        }
    }

    private <U> void emitOperationSuccessEvent(@NonNull final BrokerOperation<U> operation,
                                               final U result) {
        if (operation.getTelemetryApiId() != null) {
            final ApiEndEvent successEvent = new ApiEndEvent()
                    .putApiId(operation.getTelemetryApiId())
                    .isApiCallSuccessful(Boolean.TRUE);
            operation.putValueInSuccessEvent(successEvent, result);
            Telemetry.emit(successEvent);
        }
    }

    private <U> void emitOperationFailureEvent(@NonNull final BrokerOperation<U> operation,
                                               final BaseException exception) {
        if (operation.getTelemetryApiId() != null) {
            Telemetry.emit(
                    new ApiEndEvent()
                            .putException(exception)
                            .putApiId(operation.getTelemetryApiId())
            );
        }
    }

    /**
     * Execute the given operation with a given IIpcStrategy.
     */
    private <T> T performStrategy(@NonNull final IIpcStrategy strategy,
                                  @NonNull final BrokerOperation<T> operation) throws BaseException {

        com.microsoft.identity.common.internal.logging.Logger.info(
                TAG + operation.getMethodName(),
                "Executing with IIpcStrategy: "
                        + strategy.getClass().getSimpleName()
        );

        operation.performPrerequisites(strategy);
        final BrokerOperationBundle brokerOperationBundle = operation.getBundle();
        final Bundle resultBundle = strategy.communicateToBroker(brokerOperationBundle);
        return operation.extractResultBundle(resultBundle);

        // TODO: Emit success rate and performance of each strategy to eSTS in a finally block.
    }
}