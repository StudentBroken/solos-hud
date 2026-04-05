package com.google.android.gms.common.data;

import android.os.Bundle;
import com.ua.sdk.role.RoleHelper;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes3.dex */
public final class DataBufferUtils {
    private DataBufferUtils() {
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freezeAndClose(DataBuffer<E> dataBuffer) {
        RoleHelper.AnonymousClass4 anonymousClass4 = (ArrayList<T>) new ArrayList(dataBuffer.getCount());
        try {
            Iterator<E> it = dataBuffer.iterator();
            while (it.hasNext()) {
                anonymousClass4.add(it.next().freeze());
            }
            return anonymousClass4;
        } finally {
            dataBuffer.close();
        }
    }

    public static boolean hasData(DataBuffer<?> dataBuffer) {
        return dataBuffer != null && dataBuffer.getCount() > 0;
    }

    public static boolean hasNextPage(DataBuffer<?> dataBuffer) {
        Bundle bundleZzqL = dataBuffer.zzqL();
        return (bundleZzqL == null || bundleZzqL.getString("next_page_token") == null) ? false : true;
    }

    public static boolean hasPrevPage(DataBuffer<?> dataBuffer) {
        Bundle bundleZzqL = dataBuffer.zzqL();
        return (bundleZzqL == null || bundleZzqL.getString("prev_page_token") == null) ? false : true;
    }
}
