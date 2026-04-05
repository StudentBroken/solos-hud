package com.squareup.picasso;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes11.dex */
public interface Transformation {
    String key();

    Bitmap transform(Bitmap bitmap);
}
