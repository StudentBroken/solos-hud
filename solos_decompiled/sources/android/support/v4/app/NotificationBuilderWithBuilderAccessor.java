package android.support.v4.app;

import android.app.Notification;
import android.support.annotation.RestrictTo;

/* JADX INFO: loaded from: classes27.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public interface NotificationBuilderWithBuilderAccessor {
    Notification build();

    Notification.Builder getBuilder();
}
