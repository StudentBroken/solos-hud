package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

/* JADX INFO: loaded from: classes11.dex */
class ImageViewAction extends Action<ImageView> {
    Callback callback;

    ImageViewAction(Picasso picasso, ImageView imageView, Request data, int memoryPolicy, int networkPolicy, int errorResId, Drawable errorDrawable, String key, Object tag, Callback callback, boolean noFade) {
        super(picasso, imageView, data, memoryPolicy, networkPolicy, errorResId, errorDrawable, key, tag, noFade);
        this.callback = callback;
    }

    @Override // com.squareup.picasso.Action
    public void complete(Bitmap result, Picasso.LoadedFrom from) {
        if (result == null) {
            throw new AssertionError(String.format("Attempted to complete action with no result!\n%s", this));
        }
        ImageView target = (ImageView) this.target.get();
        if (target != null) {
            Context context = this.picasso.context;
            boolean indicatorsEnabled = this.picasso.indicatorsEnabled;
            PicassoDrawable.setBitmap(target, context, result, from, this.noFade, indicatorsEnabled);
            if (this.callback != null) {
                this.callback.onSuccess();
            }
        }
    }

    @Override // com.squareup.picasso.Action
    public void error() {
        ImageView target = (ImageView) this.target.get();
        if (target != null) {
            if (this.errorResId != 0) {
                target.setImageResource(this.errorResId);
            } else if (this.errorDrawable != null) {
                target.setImageDrawable(this.errorDrawable);
            }
            if (this.callback != null) {
                this.callback.onError();
            }
        }
    }

    @Override // com.squareup.picasso.Action
    void cancel() {
        super.cancel();
        if (this.callback != null) {
            this.callback = null;
        }
    }
}
