package com.digits.sdk.android;

import android.content.res.Resources;
import com.twitter.sdk.android.core.TwitterApiErrorConstants;

/* JADX INFO: loaded from: classes18.dex */
class PhoneNumberErrorCodes extends DigitsErrorCodes {
    PhoneNumberErrorCodes(Resources resources) {
        super(resources);
        this.codeIdMap.put(44, R.string.dgts__try_again_phone_number);
        this.codeIdMap.put(300, R.string.dgts__try_again_phone_number);
        this.codeIdMap.put(TwitterApiErrorConstants.REGISTRATION_PHONE_NORMALIZATION_FAILED, R.string.dgts__try_again_phone_number);
        this.codeIdMap.put(TwitterApiErrorConstants.DEVICE_ALREADY_REGISTERED, R.string.dgts__confirmation_error_alternative);
        this.codeIdMap.put(TwitterApiErrorConstants.OPERATOR_UNSUPPORTED, R.string.dgts__unsupported_operator_error);
    }
}
