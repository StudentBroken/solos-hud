package com.google.firebase.iid;

import android.content.Intent;
import android.os.ConditionVariable;
import android.util.Log;
import java.io.IOException;

/* JADX INFO: loaded from: classes35.dex */
final class zzo implements zzp {
    private Intent intent;
    private final ConditionVariable zzcnE;
    private String zzcnF;

    private zzo() {
        this.zzcnE = new ConditionVariable();
    }

    /* synthetic */ zzo(zzm zzmVar) {
        this();
    }

    @Override // com.google.firebase.iid.zzp
    public final void onError(String str) {
        this.zzcnF = str;
        this.zzcnE.open();
    }

    public final Intent zzKl() throws IOException {
        if (!this.zzcnE.block(30000L)) {
            Log.w("InstanceID/Rpc", "No response");
            throw new IOException("TIMEOUT");
        }
        if (this.zzcnF != null) {
            throw new IOException(this.zzcnF);
        }
        return this.intent;
    }

    @Override // com.google.firebase.iid.zzp
    public final void zzq(Intent intent) {
        this.intent = intent;
        this.zzcnE.open();
    }
}
