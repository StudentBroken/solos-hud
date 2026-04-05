package com.google.android.gms.common.images;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.internal.zzbgs;
import com.google.android.gms.internal.zzbgx;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes3.dex */
public final class zzc extends zza {
    private WeakReference<ImageView> zzaGo;

    public zzc(ImageView imageView, int i) {
        super(null, i);
        com.google.android.gms.common.internal.zzc.zzr(imageView);
        this.zzaGo = new WeakReference<>(imageView);
    }

    public zzc(ImageView imageView, Uri uri) {
        super(uri, 0);
        com.google.android.gms.common.internal.zzc.zzr(imageView);
        this.zzaGo = new WeakReference<>(imageView);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzc)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ImageView imageView = this.zzaGo.get();
        ImageView imageView2 = ((zzc) obj).zzaGo.get();
        return (imageView2 == null || imageView == null || !zzbh.equal(imageView2, imageView)) ? false : true;
    }

    public final int hashCode() {
        return 0;
    }

    @Override // com.google.android.gms.common.images.zza
    protected final void zza(Drawable drawable, boolean z, boolean z2, boolean z3) {
        Drawable zzbgsVar;
        ImageView imageView = this.zzaGo.get();
        if (imageView != null) {
            boolean z4 = (z2 || z3) ? false : true;
            if (z4 && (imageView instanceof zzbgx)) {
                int iZzqW = ((zzbgx) imageView).zzqW();
                if (this.zzaGj != 0 && iZzqW == this.zzaGj) {
                    return;
                }
            }
            boolean zZzc = zzc(z, z2);
            if (zZzc) {
                Drawable drawable2 = imageView.getDrawable();
                if (drawable2 == null) {
                    drawable2 = null;
                } else if (drawable2 instanceof zzbgs) {
                    drawable2 = ((zzbgs) drawable2).zzqU();
                }
                zzbgsVar = new zzbgs(drawable2, drawable);
            } else {
                zzbgsVar = drawable;
            }
            imageView.setImageDrawable(zzbgsVar);
            if (imageView instanceof zzbgx) {
                zzbgx zzbgxVar = (zzbgx) imageView;
                zzbgxVar.zzo(z3 ? this.zzaGh.uri : null);
                zzbgxVar.zzax(z4 ? this.zzaGj : 0);
            }
            if (zZzc) {
                ((zzbgs) zzbgsVar).startTransition(ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
            }
        }
    }
}
