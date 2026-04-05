package com.ua.sdk.actigraphysettings;

import android.os.Parcelable;
import com.ua.sdk.Resource;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface ActigraphySettings extends Parcelable, Resource {
    List<String> getActivityRecorderPriorities();

    List<String> getSleepRecorderPriorities();
}
