package com.digits.sdk.android;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import java.util.List;

/* JADX INFO: loaded from: classes18.dex */
public class UploadResponse {

    @SerializedName(TwitterApiConstants.Errors.ERRORS)
    final List<UploadError> errors;

    UploadResponse(List<UploadError> errors) {
        this.errors = errors;
    }
}
