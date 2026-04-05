package com.squareup.picasso;

import android.graphics.Bitmap;
import com.squareup.picasso.Picasso;

/* JADX INFO: loaded from: classes11.dex */
class FetchAction extends Action<Object> {
    private Callback callback;
    private final Object target;

    FetchAction(Picasso picasso, Request data, int memoryPolicy, int networkPolicy, Object tag, String key, Callback callback) {
        super(picasso, null, data, memoryPolicy, networkPolicy, 0, null, key, tag, false);
        this.target = new Object();
        this.callback = callback;
    }

    @Override // com.squareup.picasso.Action
    void complete(Bitmap result, Picasso.LoadedFrom from) {
        if (this.callback != null) {
            this.callback.onSuccess();
        }
    }

    @Override // com.squareup.picasso.Action
    void error() {
        if (this.callback != null) {
            this.callback.onError();
        }
    }

    @Override // com.squareup.picasso.Action
    void cancel() {
        super.cancel();
        this.callback = null;
    }

    @Override // com.squareup.picasso.Action
    Object getTarget() {
        return this.target;
    }
}
