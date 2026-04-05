package com.google.android.gms.common.data;

import com.ua.sdk.role.RoleHelper;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes3.dex */
public final class FreezableUtils {
    public static <T, E extends Freezable<T>> ArrayList<T> freeze(ArrayList<E> arrayList) {
        RoleHelper.AnonymousClass4 anonymousClass4 = (ArrayList<T>) new ArrayList(arrayList.size());
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            anonymousClass4.add(arrayList.get(i).freeze());
        }
        return anonymousClass4;
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freeze(E[] eArr) {
        RoleHelper.AnonymousClass4 anonymousClass4 = (ArrayList<T>) new ArrayList(eArr.length);
        for (E e : eArr) {
            anonymousClass4.add(e.freeze());
        }
        return anonymousClass4;
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freezeIterable(Iterable<E> iterable) {
        RoleHelper.AnonymousClass4 anonymousClass4 = (ArrayList<T>) new ArrayList();
        Iterator<E> it = iterable.iterator();
        while (it.hasNext()) {
            anonymousClass4.add(it.next().freeze());
        }
        return anonymousClass4;
    }
}
