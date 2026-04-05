package com.google.android.gms.common.util;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: loaded from: classes67.dex */
@Deprecated
public final class zzb<E> extends AbstractSet<E> {
    private final ArrayMap<E, E> zzaJE;

    public zzb() {
        this.zzaJE = new ArrayMap<>();
    }

    public zzb(int i) {
        this.zzaJE = new ArrayMap<>(i);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean add(E e) {
        if (this.zzaJE.containsKey(e)) {
            return false;
        }
        this.zzaJE.put(e, e);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean addAll(Collection<? extends E> collection) {
        if (!(collection instanceof zzb)) {
            return super.addAll(collection);
        }
        int size = size();
        this.zzaJE.putAll((SimpleArrayMap<? extends E, ? extends E>) ((zzb) collection).zzaJE);
        return size() > size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        this.zzaJE.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        return this.zzaJE.containsKey(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator<E> iterator() {
        return this.zzaJE.keySet().iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        if (!this.zzaJE.containsKey(obj)) {
            return false;
        }
        this.zzaJE.remove(obj);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.zzaJE.size();
    }
}
