package com.digits.sdk.android;

import android.content.Context;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterException;
import io.fabric.sdk.android.Fabric;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes18.dex */
public abstract class DigitsCallback<T> extends Callback<T> {
    private final WeakReference<Context> context;
    final DigitsController controller;

    DigitsCallback(Context context, DigitsController controller) {
        this.context = new WeakReference<>(context);
        this.controller = controller;
    }

    @Override // com.twitter.sdk.android.core.Callback
    public void failure(TwitterException exception) {
        DigitsException digitsException = DigitsException.create(this.controller.getErrors(), exception);
        Fabric.getLogger().e(Digits.TAG, "HTTP Error: " + exception.getMessage() + ", API Error: " + digitsException.getErrorCode() + ", User Message: " + digitsException.getMessage());
        this.controller.handleError(this.context.get(), digitsException);
    }
}
