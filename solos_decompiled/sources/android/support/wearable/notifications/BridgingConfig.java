package android.support.wearable.notifications;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class BridgingConfig {
    private static final String EXTRA_BRIDGING_ENABLED = "android.support.wearable.notifications.extra.bridgingEnabled";
    private static final String EXTRA_EXCLUDED_TAGS = "android.support.wearable.notifications.extra.excludedTags";
    private static final String EXTRA_ORIGINAL_PACKAGE = "android.support.wearable.notifications.extra.originalPackage";
    private static final String TAG = "BridgingConfig";
    private final boolean mBridgingEnabled;
    private final Set<String> mExcludedTags;
    private final String mPackageName;

    @VisibleForTesting
    public BridgingConfig(String packageName, boolean bridgingEnabled, Set<String> excludedTags) {
        this.mPackageName = packageName;
        this.mBridgingEnabled = bridgingEnabled;
        this.mExcludedTags = excludedTags;
    }

    public boolean isBridgingEnabled() {
        return this.mBridgingEnabled;
    }

    public Set<String> getExcludedTags() {
        return this.mExcludedTags;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public Bundle toBundle(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORIGINAL_PACKAGE, context.getPackageName());
        bundle.putBoolean(EXTRA_BRIDGING_ENABLED, isBridgingEnabled());
        bundle.putStringArrayList(EXTRA_EXCLUDED_TAGS, new ArrayList<>(getExcludedTags()));
        return bundle;
    }

    public static BridgingConfig fromBundle(Bundle bundle) {
        return new BridgingConfig(bundle.getString(EXTRA_ORIGINAL_PACKAGE), bundle.getBoolean(EXTRA_BRIDGING_ENABLED), new HashSet(bundle.getStringArrayList(EXTRA_EXCLUDED_TAGS)));
    }

    public boolean equals(Object o) {
        boolean zEquals = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BridgingConfig that = (BridgingConfig) o;
        if (this.mBridgingEnabled != that.mBridgingEnabled) {
            return false;
        }
        if (this.mPackageName != null) {
            if (!this.mPackageName.equals(that.mPackageName)) {
                return false;
            }
        } else if (that.mPackageName != null) {
            return false;
        }
        if (this.mExcludedTags != null) {
            zEquals = this.mExcludedTags.equals(that.mExcludedTags);
        } else if (that.mExcludedTags != null) {
            zEquals = false;
        }
        return zEquals;
    }

    public int hashCode() {
        int result = this.mPackageName != null ? this.mPackageName.hashCode() : 0;
        return (((result * 31) + (this.mBridgingEnabled ? 1 : 0)) * 31) + (this.mExcludedTags != null ? this.mExcludedTags.hashCode() : 0);
    }

    public String toString() {
        String str = this.mPackageName;
        boolean z = this.mBridgingEnabled;
        String strValueOf = String.valueOf(this.mExcludedTags);
        return new StringBuilder(String.valueOf(str).length() + 71 + String.valueOf(strValueOf).length()).append("BridgingConfig{mPackageName='").append(str).append("'").append(", mBridgingEnabled=").append(z).append(", mExcludedTags=").append(strValueOf).append("}").toString();
    }

    public static class Builder {
        private final boolean mBridgingEnabled;
        private final Set<String> mExcludedTags = new HashSet();
        private final String mPackageName;

        public Builder(Context context, boolean bridgingEnabled) {
            this.mPackageName = context.getPackageName();
            this.mBridgingEnabled = bridgingEnabled;
        }

        public Builder addExcludedTag(String tag) {
            this.mExcludedTags.add(tag);
            return this;
        }

        public Builder addExcludedTags(Collection<String> tags) {
            this.mExcludedTags.addAll(tags);
            return this;
        }

        public BridgingConfig build() {
            return new BridgingConfig(this.mPackageName, this.mBridgingEnabled, this.mExcludedTags);
        }
    }
}
