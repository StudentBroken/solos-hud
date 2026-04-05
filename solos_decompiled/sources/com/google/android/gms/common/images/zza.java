package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.internal.zzbgy;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zza {
    final zzb zzaGh;
    protected int zzaGj;
    private int zzaGi = 0;
    private boolean zzaGk = false;
    private boolean zzaGl = true;
    private boolean zzaGm = false;
    private boolean zzaGn = true;

    public zza(Uri uri, int i) {
        this.zzaGj = 0;
        this.zzaGh = new zzb(uri);
        this.zzaGj = i;
    }

    final void zza(Context context, Bitmap bitmap, boolean z) {
        com.google.android.gms.common.internal.zzc.zzr(bitmap);
        zza(new BitmapDrawable(context.getResources(), bitmap), z, false, true);
    }

    final void zza(Context context, zzbgy zzbgyVar) {
        if (this.zzaGn) {
            zza(null, false, true, false);
        }
    }

    final void zza(Context context, zzbgy zzbgyVar, boolean z) {
        Drawable drawable = null;
        if (this.zzaGj != 0) {
            drawable = context.getResources().getDrawable(this.zzaGj);
        }
        zza(drawable, z, false, false);
    }

    protected abstract void zza(Drawable drawable, boolean z, boolean z2, boolean z3);

    protected final boolean zzc(boolean z, boolean z2) {
        return (!this.zzaGl || z2 || z) ? false : true;
    }
}
