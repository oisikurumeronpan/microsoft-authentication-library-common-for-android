// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package com.microsoft.identity.common.internal.telemetry.events;

import com.microsoft.identity.common.internal.net.HttpResponse;
import com.microsoft.identity.common.internal.telemetry.TelemetryEventStrings.*;

public class HttpEndEvent extends BaseEvent {
    public HttpEndEvent() {
        super();
        names(Event.HTTP_END_EVENT);
        types(EventType.HTTP_EVENT);
    }

    public HttpEndEvent putResponse(final HttpResponse httpResponse) {
        put(Key.HTTP_RESPONSE_CODE, String.valueOf(httpResponse.getStatusCode()));
        //TODO discuss what kind of telemetry need to be refined from response body and header.
        return this;
    }
}
