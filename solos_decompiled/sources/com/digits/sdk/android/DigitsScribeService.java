package com.digits.sdk.android;

import com.digits.sdk.android.DigitsScribeConstants;

/* JADX INFO: loaded from: classes18.dex */
interface DigitsScribeService {
    void click(DigitsScribeConstants.Element element);

    void error(DigitsException digitsException);

    void failure();

    void impression();

    void success();
}
