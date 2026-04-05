package com.google.android.gms.common.images;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.internal.zzbh;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/* JADX INFO: loaded from: classes3.dex */
public final class zzd extends zza {
    private WeakReference<ImageManager.OnImageLoadedListener> zzaGp;

    public zzd(ImageManager.OnImageLoadedListener onImageLoadedListener, Uri uri) {
        super(uri, 0);
        com.google.android.gms.common.internal.zzc.zzr(onImageLoadedListener);
        this.zzaGp = new WeakReference<>(onImageLoadedListener);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzd)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        zzd zzdVar = (zzd) obj;
        ImageManager.OnImageLoadedListener onImageLoadedListener = this.zzaGp.get();
        ImageManager.OnImageLoadedListener onImageLoadedListener2 = zzdVar.zzaGp.get();
        return onImageLoadedListener2 != null && onImageLoadedListener != null && zzbh.equal(onImageLoadedListener2, onImageLoadedListener) && zzbh.equal(zzdVar.zzaGh, this.zzaGh);
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzaGh});
    }

    @Override // com.google.android.gms.common.images.zza
    protected final void zza(Drawable drawable, boolean z, boolean z2, boolean z3) {
        ImageManager.OnImageLoadedListener onImageLoadedListener;
        if (z2 || (onImageLoadedListener = this.zzaGp.get()) == null) {
            return;
        }
        onImageLoadedListener.onImageLoaded(this.zzaGh.uri, drawable, z3);
    }
}
