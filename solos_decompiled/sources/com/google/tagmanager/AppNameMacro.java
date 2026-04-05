package com.google.tagmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class AppNameMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.APP_NAME.toString();
    private final Context mContext;

    public static String getFunctionId() {
        return ID;
    }

    public AppNameMacro(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.mContext.getPackageName(), 0);
            return Types.objectToValue(packageManager.getApplicationLabel(applicationInfo).toString());
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("App name is not found.", e);
            return Types.getDefaultValue();
        }
    }
}
