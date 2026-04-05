package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class DigitsAuthConfig {
    protected final AuthCallback authCallback;
    protected final boolean isEmailRequired;
    protected final String phoneNumber;
    protected final int themeResId;

    protected DigitsAuthConfig(boolean isEmailRequired, String phoneNumber, AuthCallback authCallback, int themeResId) {
        this.isEmailRequired = isEmailRequired;
        this.themeResId = themeResId;
        this.phoneNumber = phoneNumber;
        this.authCallback = authCallback;
    }

    public static class Builder {
        AuthCallback authCallback;
        boolean isEmailRequired;
        String phoneNumber;
        int themeResId;

        public Builder() {
            this.isEmailRequired = false;
            this.themeResId = 0;
        }

        public Builder(Builder that) {
            this.isEmailRequired = that.isEmailRequired;
            this.phoneNumber = that.phoneNumber;
            this.authCallback = that.authCallback;
            this.themeResId = that.themeResId;
        }

        public Builder withEmailCollection() {
            this.isEmailRequired = true;
            return this;
        }

        public Builder withEmailCollection(boolean collectEmail) {
            this.isEmailRequired = collectEmail;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withAuthCallBack(AuthCallback authCallback) {
            this.authCallback = authCallback;
            return this;
        }

        public Builder withThemeResId(int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        public DigitsAuthConfig build() {
            if (this.authCallback == null) {
                throw new IllegalArgumentException("AuthCallback must not be null");
            }
            return new DigitsAuthConfig(this.isEmailRequired, this.phoneNumber == null ? "" : this.phoneNumber, this.authCallback, this.themeResId);
        }
    }
}
