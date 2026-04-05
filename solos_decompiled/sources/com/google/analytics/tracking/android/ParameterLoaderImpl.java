package com.google.analytics.tracking.android;

import android.content.Context;
import android.text.TextUtils;
import com.facebook.internal.ServerProtocol;

/* JADX INFO: loaded from: classes49.dex */
class ParameterLoaderImpl implements ParameterLoader {
    private final Context mContext;
    private String mOverrideResourcePackageName;

    public ParameterLoaderImpl(Context context) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        }
        this.mContext = context.getApplicationContext();
    }

    private int getResourceIdForType(String key, String type) {
        if (this.mContext == null) {
            return 0;
        }
        String resourcePackageName = this.mOverrideResourcePackageName != null ? this.mOverrideResourcePackageName : this.mContext.getPackageName();
        return this.mContext.getResources().getIdentifier(key, type, resourcePackageName);
    }

    @Override // com.google.analytics.tracking.android.ParameterLoader
    public String getString(String key) {
        int id = getResourceIdForType(key, "string");
        if (id == 0) {
            return null;
        }
        return this.mContext.getString(id);
    }

    @Override // com.google.analytics.tracking.android.ParameterLoader
    public boolean getBoolean(String key) {
        int id = getResourceIdForType(key, "bool");
        if (id == 0) {
            return false;
        }
        return ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equalsIgnoreCase(this.mContext.getString(id));
    }

    @Override // com.google.analytics.tracking.android.ParameterLoader
    public int getInt(String key, int defaultValue) {
        int id = getResourceIdForType(key, "integer");
        if (id != 0) {
            try {
                return Integer.parseInt(this.mContext.getString(id));
            } catch (NumberFormatException e) {
                Log.w("NumberFormatException parsing " + this.mContext.getString(id));
                return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override // com.google.analytics.tracking.android.ParameterLoader
    public boolean isBooleanKeyPresent(String key) {
        return getResourceIdForType(key, "bool") != 0;
    }

    @Override // com.google.analytics.tracking.android.ParameterLoader
    public Double getDoubleFromString(String key) {
        String value = getString(key);
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        try {
            return Double.valueOf(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            Log.w("NumberFormatException parsing " + value);
            return null;
        }
    }

    @Override // com.google.analytics.tracking.android.ParameterLoader
    public void setResourcePackageName(String resourcePackageName) {
        this.mOverrideResourcePackageName = resourcePackageName;
    }
}
