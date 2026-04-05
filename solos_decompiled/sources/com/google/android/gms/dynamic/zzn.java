package com.google.android.gms.dynamic;

import android.os.IBinder;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes67.dex */
public final class zzn<T> extends IObjectWrapper.zza {
    private final T mWrappedObject;

    private zzn(T t) {
        this.mWrappedObject = t;
    }

    public static <T> T zzE(IObjectWrapper iObjectWrapper) {
        int i = 0;
        if (iObjectWrapper instanceof zzn) {
            return ((zzn) iObjectWrapper).mWrappedObject;
        }
        IBinder iBinderAsBinder = iObjectWrapper.asBinder();
        Field[] declaredFields = iBinderAsBinder.getClass().getDeclaredFields();
        Field field = null;
        int length = declaredFields.length;
        int i2 = 0;
        while (i2 < length) {
            Field field2 = declaredFields[i2];
            if (field2.isSynthetic()) {
                field2 = field;
            } else {
                i++;
            }
            i2++;
            field = field2;
        }
        if (i != 1) {
            throw new IllegalArgumentException(new StringBuilder(64).append("Unexpected number of IObjectWrapper declared fields: ").append(declaredFields.length).toString());
        }
        if (field.isAccessible()) {
            throw new IllegalArgumentException("IObjectWrapper declared field not private!");
        }
        field.setAccessible(true);
        try {
            return (T) field.get(iBinderAsBinder);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Could not access the field in remoteBinder.", e);
        } catch (NullPointerException e2) {
            throw new IllegalArgumentException("Binder object is null.", e2);
        }
    }

    public static <T> IObjectWrapper zzw(T t) {
        return new zzn(t);
    }
}
