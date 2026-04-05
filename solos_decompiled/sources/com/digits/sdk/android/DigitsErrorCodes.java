package com.digits.sdk.android;

import android.content.res.Resources;
import android.util.SparseIntArray;
import com.twitter.sdk.android.core.TwitterApiErrorConstants;

/* JADX INFO: loaded from: classes18.dex */
class DigitsErrorCodes implements ErrorCodes {
    private static final int INITIAL_CAPACITY = 10;
    protected final SparseIntArray codeIdMap = new SparseIntArray(10);
    private final Resources resources;

    DigitsErrorCodes(Resources resources) {
        this.codeIdMap.put(88, R.string.dgts__confirmation_error_alternative);
        this.codeIdMap.put(TwitterApiErrorConstants.REGISTRATION_GENERAL_ERROR, R.string.dgts__network_error);
        this.codeIdMap.put(TwitterApiErrorConstants.REGISTRATION_OPERATION_FAILED, R.string.dgts__network_error);
        this.codeIdMap.put(240, R.string.dgts__network_error);
        this.codeIdMap.put(87, R.string.dgts__network_error);
        this.resources = resources;
    }

    @Override // com.digits.sdk.android.ErrorCodes
    public String getMessage(int code) {
        int idx = this.codeIdMap.indexOfKey(code);
        return idx < 0 ? getDefaultMessage() : this.resources.getString(this.codeIdMap.valueAt(idx));
    }

    @Override // com.digits.sdk.android.ErrorCodes
    public String getDefaultMessage() {
        return this.resources.getString(R.string.dgts__try_again);
    }

    @Override // com.digits.sdk.android.ErrorCodes
    public String getNetworkError() {
        return this.resources.getString(R.string.dgts__network_error);
    }
}
