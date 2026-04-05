package com.ua.sdk.activitystory;

import android.os.Parcelable;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.MeasurementSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryTemplate extends Parcelable {
    ImageUrl getIconUrl();

    String getMessage(MeasurementSystem measurementSystem);

    Map<String, Object> getMessageArgs();

    String getMessageTemplate();

    String getSubtitle(MeasurementSystem measurementSystem);

    Map<String, Object> getSubtitleArgs();

    String getSubtitleTemplate();

    String getTitle(MeasurementSystem measurementSystem);

    Map<String, Object> getTitleArgs();

    String getTitleTemplate();
}
