package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes67.dex */
public final class zzs {
    private AtomicInteger zzW;
    private final Map<String, Queue<zzp<?>>> zzX;
    private final Set<zzp<?>> zzY;
    private final PriorityBlockingQueue<zzp<?>> zzZ;
    private final PriorityBlockingQueue<zzp<?>> zzaa;
    private zzl[] zzab;
    private zzd zzac;
    private List<Object> zzad;
    private final zzb zzi;
    private final zzw zzj;
    private final zzk zzx;

    public zzs(zzb zzbVar, zzk zzkVar) {
        this(zzbVar, zzkVar, 4);
    }

    private zzs(zzb zzbVar, zzk zzkVar, int i) {
        this(zzbVar, zzkVar, 4, new zzh(new Handler(Looper.getMainLooper())));
    }

    private zzs(zzb zzbVar, zzk zzkVar, int i, zzw zzwVar) {
        this.zzW = new AtomicInteger();
        this.zzX = new HashMap();
        this.zzY = new HashSet();
        this.zzZ = new PriorityBlockingQueue<>();
        this.zzaa = new PriorityBlockingQueue<>();
        this.zzad = new ArrayList();
        this.zzi = zzbVar;
        this.zzx = zzkVar;
        this.zzab = new zzl[4];
        this.zzj = zzwVar;
    }

    public final void start() {
        if (this.zzac != null) {
            this.zzac.quit();
        }
        for (int i = 0; i < this.zzab.length; i++) {
            if (this.zzab[i] != null) {
                this.zzab[i].quit();
            }
        }
        this.zzac = new zzd(this.zzZ, this.zzaa, this.zzi, this.zzj);
        this.zzac.start();
        for (int i2 = 0; i2 < this.zzab.length; i2++) {
            zzl zzlVar = new zzl(this.zzaa, this.zzx, this.zzi, this.zzj);
            this.zzab[i2] = zzlVar;
            zzlVar.start();
        }
    }

    public final <T> zzp<T> zzc(zzp<T> zzpVar) {
        zzpVar.zza(this);
        synchronized (this.zzY) {
            this.zzY.add(zzpVar);
        }
        zzpVar.zza(this.zzW.incrementAndGet());
        zzpVar.zzb("add-to-queue");
        if (zzpVar.zzh()) {
            synchronized (this.zzX) {
                String strZzd = zzpVar.zzd();
                if (this.zzX.containsKey(strZzd)) {
                    Queue<zzp<?>> linkedList = this.zzX.get(strZzd);
                    if (linkedList == null) {
                        linkedList = new LinkedList<>();
                    }
                    linkedList.add(zzpVar);
                    this.zzX.put(strZzd, linkedList);
                    if (zzab.DEBUG) {
                        zzab.zza("Request for cacheKey=%s is in flight, putting on hold.", strZzd);
                    }
                } else {
                    this.zzX.put(strZzd, null);
                    this.zzZ.add(zzpVar);
                }
            }
        } else {
            this.zzaa.add(zzpVar);
        }
        return zzpVar;
    }

    final <T> void zzd(zzp<T> zzpVar) {
        synchronized (this.zzY) {
            this.zzY.remove(zzpVar);
        }
        synchronized (this.zzad) {
            Iterator<Object> it = this.zzad.iterator();
            while (it.hasNext()) {
                it.next();
            }
        }
        if (zzpVar.zzh()) {
            synchronized (this.zzX) {
                String strZzd = zzpVar.zzd();
                Queue<zzp<?>> queueRemove = this.zzX.remove(strZzd);
                if (queueRemove != null) {
                    if (zzab.DEBUG) {
                        zzab.zza("Releasing %d waiting requests for cacheKey=%s.", Integer.valueOf(queueRemove.size()), strZzd);
                    }
                    this.zzZ.addAll(queueRemove);
                }
            }
        }
    }
}
