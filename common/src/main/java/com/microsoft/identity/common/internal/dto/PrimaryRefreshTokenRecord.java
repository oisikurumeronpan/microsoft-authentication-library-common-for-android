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
package com.microsoft.identity.common.internal.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.microsoft.identity.common.internal.dto.PrimaryRefreshTokenRecord.SerializedNames.*;

public class PrimaryRefreshTokenRecord extends Credential {

    public static class SerializedNames extends Credential.SerializedNames {
        /**
         * String of family id.
         */
        public static final String FAMILY_ID = "family_id";
        /**
         * String of target.
         */
        public static final String TARGET = "target";

        /**
         * String of realm.
         */
        public static final String REALM = "realm";

        /**
         * String of session_key.
         */
        public static final String SESSION_KEY = "session_key";

        /**
         * String of session_key_rolling_date.
         */
        public static final String SESSION_KEY_ROLLING_DATE = "session_key_rolling_date";

        /**
         * String of prt_protocol_version.
         */
        public static final String PRT_PROTOCOL_VERSION = "prt_protocol_version";
    }

    /**
     * 1st Party Application Family ID.
     */
    @SerializedName(FAMILY_ID)
    private String mFamilyId;

    /**
     * Permissions that are included in the token. Formats for endpoints will be different. 
     * <p>
     * Mandatory, if credential is scoped down by some parameters or requirements (e.g. by
     * resource, scopes or permissions).
     */
    @SerializedName(TARGET)
    private String mTarget;

    /**
     * Full tenant or organizational identifier that account belongs to. Can be null.
     */
    @SerializedName(REALM)
    private String mRealm;

    /**
     * Token expiry time. This value should be calculated based on the current UTC time measured
     * locally and the value expires_in returned from the service. Measured in milliseconds from
     * epoch (1970).
     */
    @SerializedName(EXPIRES_ON)
    private String mExpiresOn;

    /**
     * Session key of PRT.
     */
    @SerializedName(SESSION_KEY)
    private String mSessionKey;

    /**
     * PRT protocol version, currently 3.0.
     */
    @SerializedName(PRT_PROTOCOL_VERSION)
    private String mPrtProtocolVersion;

    /**
     * Rolling date of session key.
     */
    @SerializedName(SESSION_KEY_ROLLING_DATE)
    private String mSessionKeyRollingDate;

    /**
     * Gets the target.
     *
     * @return The target to get.
     */
    public String getTarget() {
        return mTarget;
    }

    /**
     * Sets the target.
     *
     * @param target The target to set.
     */
    public void setTarget(final String target) {
        mTarget = target;
    }

    /**
     * Gets the family_id.
     *
     * @return The family_id to get.
     */
    public String getFamilyId() {
        return mFamilyId;
    }

    /**
     * Sets the family_id.
     *
     * @param familyId The family_id to set.
     */
    public void setFamilyId(final String familyId) {
        mFamilyId = familyId;
    }

    public boolean isExpired(final String expires) {
        // Init a Calendar for the current time/date
        final Calendar calendar = Calendar.getInstance();
        final Date validity = calendar.getTime();
        // Init a Date for the accessToken's expiry
        long epoch = Long.valueOf(expires);
        final Date expiresOn = new Date(
                TimeUnit.SECONDS.toMillis(epoch)
        );
        return expiresOn.before(validity);
    }

    @Override
    public boolean isExpired() {
        return isExpired(getExpiresOn());
    }

    /**
     * Gets the realm
     *
     * @return The realm to get.
     */
    public String getRealm() {
        return mRealm;
    }

    /**
     * Sets the realm.
     * @param realm The realm to set.
     */
    public void setRealm(String realm) {
        mRealm = realm;
    }

    /**
     * Gets the expires_on.
     * @return The expires_on to get.
     */
    public String getExpiresOn() {
        return mExpiresOn;
    }

    /**
     * Sets the expires_on.
     * @param expiresOn The expires_on to set.
     */
    public void setExpiresOn(String expiresOn) {
        mExpiresOn = expiresOn;
    }

    /**
     * Gets the session_key.
     * @return The session_key to get.
     */
    public String getSessionKey() {
        return mSessionKey;
    }

    /**
     * Sets the session_key.
     *
     * @param sessionKey The session_key to set.
     */
    public void setSessionKey(String sessionKey) {
        mSessionKey = sessionKey;
    }

    /**
     * Gets the prt_protocol_version.
     *
     * @return The prt_protocol_version to get.
     */
    public String getPrtProtocolVersion() {
        return mPrtProtocolVersion;
    }

    /**
     * Sets the prt_protocol_version.
     *
     * @param prtProtocolVersion The prt_protocol_version to set.
     */
    public void setPrtProtocolVersion(String prtProtocolVersion) {
        mPrtProtocolVersion = prtProtocolVersion;
    }

    /**
     * Gets the session_key_rolling_date.
     *
     * @return The session_key_rolling_date to get.
     */
    public String getSessionKeyRollingDate() {
        return mSessionKeyRollingDate;
    }

    /**
     * Sets the session_key_rolling_date.
     * @param sessionKeyRollingDate The session_key_rolling_date to set.
     */
    public void setSessionKeyRollingDate(String sessionKeyRollingDate) {
        mSessionKeyRollingDate = sessionKeyRollingDate;
    }

    //CHECKSTYLE:OFF
    // This method is generated. Checkstyle and/or PMD has been disabled.
    // This method *must* be regenerated if the class' structural definition changes through the
    // addition/subtraction of fields.
    @SuppressWarnings("PMD")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrimaryRefreshTokenRecord)) return false;
        if (!super.equals(o)) return false;

        PrimaryRefreshTokenRecord that = (PrimaryRefreshTokenRecord) o;

        if (mFamilyId != null ? !mFamilyId.equals(that.mFamilyId) : that.mFamilyId != null)
            return false;
        if (mTarget != null ? !mTarget.equals(that.mTarget) : that.mTarget != null) return false;
        if (mRealm != null ? !mRealm.equals(that.mRealm) : that.mRealm != null) return false;
        if (mExpiresOn != null ? !mExpiresOn.equals(that.mExpiresOn) : that.mExpiresOn != null)
            return false;
        if (mSessionKey != null ? !mSessionKey.equals(that.mSessionKey) : that.mSessionKey != null)
            return false;
        if (mPrtProtocolVersion != null ? !mPrtProtocolVersion.equals(that.mPrtProtocolVersion) : that.mPrtProtocolVersion != null)
            return false;
        return mSessionKeyRollingDate != null ? mSessionKeyRollingDate.equals(that.mSessionKeyRollingDate) : that.mSessionKeyRollingDate == null;
    }
    //CHECKSTYLE:ON

    //CHECKSTYLE:OFF
    // This method is generated. Checkstyle and/or PMD has been disabled.
    // This method *must* be regenerated if the class' structural definition changes through the
    // addition/subtraction of fields.
    @SuppressWarnings("PMD")
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (mFamilyId != null ? mFamilyId.hashCode() : 0);
        result = 31 * result + (mTarget != null ? mTarget.hashCode() : 0);
        result = 31 * result + (mRealm != null ? mRealm.hashCode() : 0);
        result = 31 * result + (mExpiresOn != null ? mExpiresOn.hashCode() : 0);
        result = 31 * result + (mSessionKey != null ? mSessionKey.hashCode() : 0);
        result = 31 * result + (mPrtProtocolVersion != null ? mPrtProtocolVersion.hashCode() : 0);
        result = 31 * result + (mSessionKeyRollingDate != null ? mSessionKeyRollingDate.hashCode() : 0);
        return result;
    }
    //CHECKSTYLE:ON

    //CHECKSTYLE:OFF
    // This method is generated. Checkstyle and/or PMD has been disabled.
    // This method *must* be regenerated if the class' structural definition changes through the
    // addition/subtraction of fields.
    @Override
    public String toString() {
        return "PrimaryRefreshTokenRecord{" +
                "mFamilyId='" + mFamilyId + '\'' +
                ", mTarget='" + mTarget + '\'' +
                ", mRealm='" + mRealm + '\'' +
                ", mExpiresOn='" + mExpiresOn + '\'' +
                ", mSessionKey='" + mSessionKey + '\'' +
                ", mPrtProtocolVersion='" + mPrtProtocolVersion + '\'' +
                ", mSessionKeyRollingDate='" + mSessionKeyRollingDate + '\'' +
                '}';
    }
    //CHECKSTYLE:ON
}
