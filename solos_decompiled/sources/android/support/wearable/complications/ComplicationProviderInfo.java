package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class ComplicationProviderInfo implements Parcelable {
    public static final Parcelable.Creator<ComplicationProviderInfo> CREATOR = new Parcelable.Creator<ComplicationProviderInfo>() { // from class: android.support.wearable.complications.ComplicationProviderInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ComplicationProviderInfo createFromParcel(Parcel source) {
            return new ComplicationProviderInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ComplicationProviderInfo[] newArray(int size) {
            return new ComplicationProviderInfo[size];
        }
    };
    private static final String KEY_APP_NAME = "app_name";
    private static final String KEY_PROVIDER_ICON = "provider_icon";
    private static final String KEY_PROVIDER_NAME = "provider_name";
    private static final String KEY_PROVIDER_TYPE = "complication_type";
    public final String appName;
    public final int complicationType;
    public final Icon providerIcon;
    public final String providerName;

    public ComplicationProviderInfo(String appName, String providerName, Icon providerIcon, int complicationType) {
        this.appName = appName;
        this.providerName = providerName;
        this.providerIcon = providerIcon;
        this.complicationType = complicationType;
    }

    public ComplicationProviderInfo(Parcel in) {
        Bundle bundle = in.readBundle(getClass().getClassLoader());
        this.appName = bundle.getString("app_name");
        this.providerName = bundle.getString(KEY_PROVIDER_NAME);
        this.providerIcon = (Icon) bundle.getParcelable(KEY_PROVIDER_ICON);
        this.complicationType = bundle.getInt(KEY_PROVIDER_TYPE);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("app_name", this.appName);
        bundle.putString(KEY_PROVIDER_NAME, this.providerName);
        bundle.putParcelable(KEY_PROVIDER_ICON, this.providerIcon);
        bundle.putInt(KEY_PROVIDER_TYPE, this.complicationType);
        dest.writeBundle(bundle);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        String str = this.appName;
        String str2 = this.providerName;
        String strValueOf = String.valueOf(this.providerIcon);
        return new StringBuilder(String.valueOf(str).length() + 98 + String.valueOf(str2).length() + String.valueOf(strValueOf).length()).append("ComplicationProviderInfo{appName='").append(str).append("'").append(", providerName='").append(str2).append("'").append(", providerIcon=").append(strValueOf).append(", complicationType=").append(this.complicationType).append("}").toString();
    }
}
