package io.fabric.sdk.android.services.concurrency;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes66.dex */
@Retention(RetentionPolicy.RUNTIME)
public @interface DependsOn {
    Class<?>[] value();
}
