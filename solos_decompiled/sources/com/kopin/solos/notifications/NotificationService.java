package com.kopin.solos.notifications;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/* JADX INFO: loaded from: classes37.dex */
public class NotificationService extends NotificationListenerService {
    static boolean isActive;
    static NewNotificationCallback mCallback;

    public interface NewNotificationCallback {
        void onNotification(String str, int i, String str2, String str3, String str4);
    }

    public static void setCallback(NewNotificationCallback cb) {
        mCallback = cb;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.d("Notifier", "Notifier listener starting");
        isActive = true;
    }

    @Override // android.service.notification.NotificationListenerService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
        Log.d("Notifier", "Notifier listener stopping");
        isActive = false;
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationPosted(StatusBarNotification statusBarNotif) {
        Log.d("Notifier", "Notification posted...");
        if (statusBarNotif != null) {
            String pkg = statusBarNotif.getPackageName();
            Log.d("Notifier", "  from " + pkg);
            String title = null;
            Notification note = statusBarNotif.getNotification();
            if (note != null) {
                String text = note.tickerText != null ? note.tickerText.toString() : null;
                int iconId = note.icon;
                if (Build.VERSION.SDK_INT >= 19 && note.extras != null && !note.extras.isEmpty()) {
                    title = note.extras.getString(NotificationCompat.EXTRA_TITLE);
                    if (note.extras.containsKey(NotificationCompat.EXTRA_TEXT)) {
                        text = note.extras.getString(NotificationCompat.EXTRA_TEXT);
                    }
                }
                PackageManager pm = getPackageManager();
                try {
                    ApplicationInfo appInfo = pm.getApplicationInfo(pkg, 0);
                    CharSequence label = pm.getApplicationLabel(appInfo);
                    Intent launch = pm.getLaunchIntentForPackage(pkg);
                    if (label != null && launch != null) {
                        Log.d("Notifier", "Notification from " + ((Object) label) + ": " + title + ": " + text);
                        if (mCallback != null) {
                            mCallback.onNotification(pkg, iconId, label.toString(), title, text);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}
