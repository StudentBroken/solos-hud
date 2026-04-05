package com.digits.sdk.android;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes18.dex */
public class Email {

    @SerializedName("address")
    final String address;

    @SerializedName("is_verified")
    final boolean verified;

    Email(String address, boolean verified) {
        this.address = address;
        this.verified = verified;
    }

    public String getAddress() {
        return this.address;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return this.verified == email.verified && this.address.equals(email.address);
    }

    public int hashCode() {
        int result = this.address.hashCode();
        return (result * 31) + (this.verified ? 1 : 0);
    }
}
