package com.kopin.solos.navigate.apimodels.sharedmodel;

import com.facebook.GraphResponse;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/* JADX INFO: loaded from: classes47.dex */
public abstract class AbstractModel implements Serializable {

    @SerializedName(GraphResponse.SUCCESS_KEY)
    private boolean success = false;

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String toString() {
        return super.toString() + ", success: " + this.success;
    }
}
