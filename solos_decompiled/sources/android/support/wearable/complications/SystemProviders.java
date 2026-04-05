package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.content.ComponentName;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class SystemProviders {
    public static final int ANDROID_PAY = 8;
    private static final String APPS_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.LauncherProviderService";
    public static final int APP_SHORTCUT = 6;
    private static final String BATTERY_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.BatteryProviderService";
    private static final String CURRENT_TIME_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.CurrentTimeProvider";
    public static final int DATE = 2;
    private static final String DATE_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.DayOfMonthProviderService";
    private static final String HOME_PACKAGE_NAME = "com.google.android.wearable.app";
    public static final int NEXT_EVENT = 9;
    private static final String NEXT_EVENT_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.NextEventProviderService";
    private static final String PAY_CLASS_NAME = "com.google.commerce.tapandpay.android.wearable.complications.PayProviderService";
    private static final String PAY_PACKAGE_NAME = "com.google.android.apps.walletnfcrel";
    public static final int RETAIL_CHAT = 11;
    private static final String RETAIL_CHAT_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.RetailChatProviderService";
    private static final String RETAIL_STEPS_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.RetailStepsProviderService";
    public static final int RETAIL_STEP_COUNT = 10;
    private static final String STEPS_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.StepsProviderService";
    public static final int STEP_COUNT = 4;
    public static final int SUNRISE_SUNSET = 12;
    public static final int TIME_AND_DATE = 3;
    private static final String UNREAD_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.UnreadNotificationsProviderService";
    public static final int UNREAD_NOTIFICATION_COUNT = 7;
    public static final int WATCH_BATTERY = 1;
    public static final int WORLD_CLOCK = 5;
    private static final String WORLD_CLOCK_CLASS_NAME = "com.google.android.clockwork.home.complications.providers.WorldClockProviderService";

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProviderId {
    }

    @Deprecated
    public static ComponentName batteryProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, BATTERY_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName dateProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, DATE_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName currentTimeProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, CURRENT_TIME_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName worldClockProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, WORLD_CLOCK_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName appsProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, APPS_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName stepCountProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, STEPS_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName unreadCountProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, UNREAD_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName nextEventProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, NEXT_EVENT_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName retailStepCountProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, RETAIL_STEPS_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName retailChatProvider() {
        return new ComponentName(HOME_PACKAGE_NAME, RETAIL_CHAT_CLASS_NAME);
    }

    @Deprecated
    public static ComponentName androidPayProvider() {
        return new ComponentName(PAY_PACKAGE_NAME, PAY_CLASS_NAME);
    }
}
