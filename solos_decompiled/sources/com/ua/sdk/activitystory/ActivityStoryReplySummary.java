package com.ua.sdk.activitystory;

import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryReplySummary extends Parcelable {
    List<ActivityStory> getItems();

    EntityRef<ActivityStory> getReplyRef();

    int getTotalCount();

    boolean isReplied();
}
