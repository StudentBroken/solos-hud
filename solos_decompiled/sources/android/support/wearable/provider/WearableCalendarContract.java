package android.support.wearable.provider;

import android.net.Uri;

/* JADX INFO: loaded from: classes33.dex */
public class WearableCalendarContract {
    public static final String AUTHORITY = "com.google.android.wearable.provider.calendar";
    public static final Uri CONTENT_URI = Uri.parse("content://com.google.android.wearable.provider.calendar");

    public static final class Instances {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(WearableCalendarContract.CONTENT_URI, "instances/when");

        private Instances() {
        }
    }

    public static final class Attendees {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(WearableCalendarContract.CONTENT_URI, "attendees");

        private Attendees() {
        }
    }

    public static final class Reminders {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(WearableCalendarContract.CONTENT_URI, "reminders");

        private Reminders() {
        }
    }
}
