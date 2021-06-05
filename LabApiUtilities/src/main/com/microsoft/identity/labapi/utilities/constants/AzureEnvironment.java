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
package com.microsoft.identity.labapi.utilities.constants;

public enum AzureEnvironment {
    AZURE_B2C_CLOUD(LabConstants.AzureEnvironment.AZURE_B2C_CLOUD),
    AZURE_CHINA_CLOUD(LabConstants.AzureEnvironment.AZURE_CHINA_CLOUD),
    AZURE_CLOUD(LabConstants.AzureEnvironment.AZURE_CLOUD),
    AZURE_GERMANY_CLOUD(LabConstants.AzureEnvironment.AZURE_GERMANY_CLOUD),
    AZURE_PPE(LabConstants.AzureEnvironment.AZURE_PPE),
    AZURE_US_GOVERNMENT(LabConstants.AzureEnvironment.AZURE_US_GOVERNMENT);

    final String value;

    AzureEnvironment(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
