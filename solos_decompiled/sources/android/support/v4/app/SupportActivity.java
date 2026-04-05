package android.support.v4.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ReportFragment;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.SimpleArrayMap;

/* JADX INFO: loaded from: classes27.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SupportActivity extends Activity implements LifecycleOwner {
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap = new SimpleArrayMap<>();
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static class ExtraData {
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void putExtraData(ExtraData extraData) {
        this.mExtraDataMap.put((Class<? extends ExtraData>) extraData.getClass(), extraData);
    }

    @Override // android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReportFragment.injectIfNeededIn(this);
    }

    @Override // android.app.Activity
    @CallSuper
    protected void onSaveInstanceState(Bundle outState) {
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(outState);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public <T extends ExtraData> T getExtraData(Class<T> extraDataClass) {
        return (T) this.mExtraDataMap.get(extraDataClass);
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }
}
