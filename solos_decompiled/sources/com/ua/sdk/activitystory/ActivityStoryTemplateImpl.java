package com.ua.sdk.activitystory;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.Convert;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.MeasurementSystem;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.internal.ImageUrlImpl;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryTemplateImpl implements ActivityStoryTemplate, Parcelable {
    public static Parcelable.Creator<ActivityStoryTemplateImpl> CREATOR = new Parcelable.Creator<ActivityStoryTemplateImpl>() { // from class: com.ua.sdk.activitystory.ActivityStoryTemplateImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryTemplateImpl createFromParcel(Parcel source) {
            return new ActivityStoryTemplateImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryTemplateImpl[] newArray(int size) {
            return new ActivityStoryTemplateImpl[size];
        }
    };

    @SerializedName(SettingsJsonConstants.APP_ICON_KEY)
    String mIcon;
    transient Map<String, Object> mMessageArgs;

    @SerializedName("message")
    String mMessageTemplate;
    transient Map<String, Object> mSubtitleArgs;

    @SerializedName("subtitle")
    String mSubtitleTemplate;
    transient Map<String, Object> mTitleArgs;

    @SerializedName("title")
    String mTitleTemplate;

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public ImageUrl getIconUrl() {
        if (this.mIcon == null) {
            return null;
        }
        return ImageUrlImpl.getBuilder().setUri(this.mIcon).build();
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public String getTitle(MeasurementSystem ms) {
        return createString(this.mTitleTemplate, this.mTitleArgs, ms);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public String getTitleTemplate() {
        return this.mTitleTemplate;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public Map<String, Object> getTitleArgs() {
        return this.mTitleArgs;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public String getSubtitle(MeasurementSystem ms) {
        return createString(this.mSubtitleTemplate, this.mSubtitleArgs, ms);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public String getSubtitleTemplate() {
        return this.mSubtitleTemplate;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public Map<String, Object> getSubtitleArgs() {
        return this.mSubtitleArgs;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public String getMessage(MeasurementSystem ms) {
        return createString(this.mMessageTemplate, this.mMessageArgs, ms);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public String getMessageTemplate() {
        return this.mMessageTemplate;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTemplate
    public Map<String, Object> getMessageArgs() {
        return this.mMessageArgs;
    }

    public void fillTemplateArgs(JsonObject json) {
        this.mTitleArgs = getArgs(this.mTitleTemplate, json);
        this.mSubtitleArgs = getArgs(this.mSubtitleTemplate, json);
        this.mMessageArgs = getArgs(this.mMessageTemplate, json);
    }

    Map<String, Object> getArgs(String template, JsonObject json) {
        Object asString;
        String subkey;
        if (json == null || template == null || template.isEmpty()) {
            return Collections.emptyMap();
        }
        HashMap<String, Object> args = null;
        boolean escaped = false;
        int openBrackets = 0;
        int closeBrackets = 0;
        int open = -1;
        int length = template.length();
        for (int i = 0; i < length; i++) {
            char c = template.charAt(i);
            if (escaped) {
                escaped = false;
            } else {
                switch (c) {
                    case '\\':
                        escaped = true;
                        break;
                    case '{':
                        openBrackets++;
                        if (openBrackets == 1) {
                            open = i;
                        }
                        break;
                    case '}':
                        if (openBrackets > 0 && openBrackets == (closeBrackets = closeBrackets + 1)) {
                            JsonElement e = json;
                            String key = template.substring(open + openBrackets, (i + 1) - closeBrackets);
                            while (key.length() > 0) {
                                int periodIndex = key.indexOf(46);
                                if (periodIndex > 0) {
                                    subkey = key.substring(0, periodIndex);
                                    key = key.substring(periodIndex + 1);
                                } else {
                                    subkey = key;
                                    key = "";
                                }
                                if (e.isJsonObject()) {
                                    e = e.getAsJsonObject().get(subkey);
                                } else {
                                    e = null;
                                }
                            }
                            if (e != null && !e.isJsonNull()) {
                                String fullKey = template.substring(open, i + 1);
                                if (e.isJsonPrimitive()) {
                                    JsonPrimitive primitive = (JsonPrimitive) e;
                                    if (primitive.isBoolean()) {
                                        asString = Boolean.valueOf(primitive.getAsBoolean());
                                    } else if (primitive.isNumber()) {
                                        asString = primitive.getAsNumber();
                                    } else {
                                        asString = primitive.getAsString();
                                    }
                                } else {
                                    asString = e.getAsString();
                                }
                                if (args == null) {
                                    args = new HashMap<>(2);
                                }
                                args.put(fullKey, asString);
                            }
                            open = -1;
                            openBrackets = 0;
                            closeBrackets = 0;
                        }
                        break;
                }
            }
        }
        return args == null ? Collections.emptyMap() : args;
    }

    private String createString(String template, Map<String, Object> args, MeasurementSystem ms) {
        if (template == null || template.isEmpty()) {
            return "";
        }
        if (ms == null) {
            ms = MeasurementSystem.IMPERIAL;
        }
        StringBuilder out = new StringBuilder(template.length());
        boolean escaped = false;
        int openBrackets = 0;
        int closeBrackets = 0;
        int open = -1;
        int length = template.length();
        for (int i = 0; i < length; i++) {
            char c = template.charAt(i);
            if (escaped) {
                escaped = false;
                out.append(c);
            } else {
                switch (c) {
                    case '\\':
                        escaped = true;
                        out.append(c);
                        break;
                    case '{':
                        openBrackets++;
                        if (openBrackets == 1) {
                            open = i;
                        }
                        break;
                    case '}':
                        if (openBrackets > 0 && openBrackets == (closeBrackets = closeBrackets + 1)) {
                            String key = template.substring(open, i + 1);
                            Object value = args.get(key);
                            if (value == null) {
                                out.append(key);
                            } else if (key.contains(BaseDataTypes.ID_DISTANCE)) {
                                writeDistance(out, value, ms);
                            } else {
                                out.append(value);
                            }
                            open = -1;
                            openBrackets = 0;
                            closeBrackets = 0;
                        }
                        break;
                    default:
                        if (open < 0) {
                            out.append(c);
                        }
                        break;
                }
            }
        }
        return out.toString();
    }

    private void writeDistance(StringBuilder out, Object distance, MeasurementSystem ms) {
        double meters;
        if (distance instanceof Number) {
            meters = ((Number) distance).doubleValue();
        } else {
            try {
                meters = Double.parseDouble(distance.toString());
            } catch (NumberFormatException e) {
                out.append(distance.toString());
                return;
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        switch (ms) {
            case IMPERIAL:
                double mile = Convert.meterToMile(Double.valueOf(meters)).doubleValue();
                out.append(df.format(mile));
                out.append("mi");
                break;
            case METRIC:
                double km = Convert.meterToKilometer(Double.valueOf(meters)).doubleValue();
                out.append(df.format(km));
                out.append("km");
                break;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private void writeArgsToParcel(Map<String, Object> args, Parcel dest) {
        if (args == null || args.isEmpty()) {
            dest.writeBundle(null);
            return;
        }
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            bundle.putSerializable(entry.getKey(), (Serializable) entry.getValue());
        }
        dest.writeBundle(bundle);
    }

    private Map<String, Object> readArgsFromParcel(Parcel in) {
        Bundle bundle = in.readBundle();
        if (bundle == null) {
            return Collections.emptyMap();
        }
        HashMap<String, Object> args = new HashMap<>(bundle.size());
        for (String key : bundle.keySet()) {
            args.put(key, bundle.getSerializable(key));
        }
        return args;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mIcon);
        writeArgsToParcel(this.mTitleArgs, dest);
        writeArgsToParcel(this.mSubtitleArgs, dest);
        writeArgsToParcel(this.mMessageArgs, dest);
        dest.writeString(this.mTitleTemplate);
        dest.writeString(this.mSubtitleTemplate);
        dest.writeString(this.mMessageTemplate);
    }

    public ActivityStoryTemplateImpl() {
    }

    private ActivityStoryTemplateImpl(Parcel in) {
        this.mIcon = in.readString();
        this.mTitleArgs = readArgsFromParcel(in);
        this.mSubtitleArgs = readArgsFromParcel(in);
        this.mMessageArgs = readArgsFromParcel(in);
        this.mTitleTemplate = in.readString();
        this.mSubtitleTemplate = in.readString();
        this.mMessageTemplate = in.readString();
    }

    public String toString() {
        return toString(null);
    }

    public String toString(MeasurementSystem ms) {
        StringBuilder out = new StringBuilder();
        if (this.mTitleTemplate != null) {
            out.append(getTitle(ms));
        }
        if (this.mSubtitleTemplate != null) {
            out.append('\n');
            out.append(getSubtitle(ms));
        }
        if (this.mMessageTemplate != null) {
            out.append('\n');
            out.append(getMessage(ms));
        }
        return out.toString();
    }
}
