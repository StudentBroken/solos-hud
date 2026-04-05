package com.kopin.solos.share.peloton;

import android.util.Log;
import com.kopin.solos.storage.ISyncable;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.Shared;
import java.util.Locale;

/* JADX INFO: loaded from: classes4.dex */
public class RunData implements ISyncable {
    private static final String TAG = "RunData";
    private long actualStartTime;
    private long mId;

    public RunData(SavedRun run) {
        this.mId = run.getId();
        this.actualStartTime = run.getActualStartTime();
    }

    @Override // com.kopin.solos.storage.ISyncable
    public Shared toShared(int providerKey, String userName, String externalId) {
        Log.d(TAG, String.format(Locale.US, "toShared id %d, externalId %s, %s", Long.valueOf(this.mId), externalId, Shared.ShareType.RUN_DATA.name()));
        return new Shared(this.mId, providerKey, userName, externalId, false, Shared.ShareType.RUN_DATA, this.actualStartTime);
    }
}
