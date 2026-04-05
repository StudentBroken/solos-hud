package com.digits.sdk.android;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

/* JADX INFO: loaded from: classes18.dex */
public abstract class ContactsCallback<T> extends Callback<T> {
    @Override // com.twitter.sdk.android.core.Callback
    public abstract void failure(TwitterException twitterException);

    @Override // com.twitter.sdk.android.core.Callback
    public abstract void success(Result<T> result);
}
