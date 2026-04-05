package android.support.multidex;

import android.app.Application;
import android.content.Context;

/* JADX INFO: loaded from: classes12.dex */
public class MultiDexApplication extends Application {
    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
