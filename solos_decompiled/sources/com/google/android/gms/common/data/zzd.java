package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* JADX INFO: loaded from: classes3.dex */
public class zzd<T extends SafeParcelable> extends AbstractDataBuffer<T> {
    private static final String[] zzaFB = {ShareConstants.WEB_DIALOG_PARAM_DATA};
    private final Parcelable.Creator<T> zzaFC;

    public zzd(DataHolder dataHolder, Parcelable.Creator<T> creator) {
        super(dataHolder);
        this.zzaFC = creator;
    }

    public static <T extends SafeParcelable> void zza(DataHolder.zza zzaVar, T t) {
        Parcel parcelObtain = Parcel.obtain();
        t.writeToParcel(parcelObtain, 0);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ShareConstants.WEB_DIALOG_PARAM_DATA, parcelObtain.marshall());
        zzaVar.zza(contentValues);
        parcelObtain.recycle();
    }

    public static DataHolder.zza zzqO() {
        return DataHolder.zza(zzaFB);
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    /* JADX INFO: renamed from: zzas, reason: merged with bridge method [inline-methods] */
    public T get(int i) {
        byte[] bArrZzg = this.zzaCZ.zzg(ShareConstants.WEB_DIALOG_PARAM_DATA, i, this.zzaCZ.zzat(i));
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.unmarshall(bArrZzg, 0, bArrZzg.length);
        parcelObtain.setDataPosition(0);
        T tCreateFromParcel = this.zzaFC.createFromParcel(parcelObtain);
        parcelObtain.recycle();
        return tCreateFromParcel;
    }
}
