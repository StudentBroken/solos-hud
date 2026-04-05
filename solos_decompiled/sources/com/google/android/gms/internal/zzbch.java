package com.google.android.gms.internal;

import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbch {
    private int zzaBJ;
    private final TaskCompletionSource<Void> zzaBI = new TaskCompletionSource<>();
    private boolean zzaBK = false;
    private final ArrayMap<zzbcf<?>, ConnectionResult> zzaAD = new ArrayMap<>();

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public zzbch(Iterable<? extends GoogleApi<?>> iterable) {
        Iterator<? extends GoogleApi<?>> it = iterable.iterator();
        while (it.hasNext()) {
            this.zzaAD.put(it.next().zzpf(), null);
        }
        this.zzaBJ = this.zzaAD.keySet().size();
    }

    public final Task<Void> getTask() {
        return this.zzaBI.getTask();
    }

    public final void zza(zzbcf<?> zzbcfVar, ConnectionResult connectionResult) {
        this.zzaAD.put(zzbcfVar, connectionResult);
        this.zzaBJ--;
        if (!connectionResult.isSuccess()) {
            this.zzaBK = true;
        }
        if (this.zzaBJ == 0) {
            if (!this.zzaBK) {
                this.zzaBI.setResult(null);
            } else {
                this.zzaBI.setException(new com.google.android.gms.common.api.zza(this.zzaAD));
            }
        }
    }

    public final Set<zzbcf<?>> zzpr() {
        return this.zzaAD.keySet();
    }

    public final void zzps() {
        this.zzaBI.setResult(null);
    }
}
