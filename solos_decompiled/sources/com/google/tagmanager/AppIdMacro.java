package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class AppIdMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.APP_ID.toString();
    private final Context mContext;

    public static String getFunctionId() {
        return ID;
    }

    public AppIdMacro(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        return Types.objectToValue(this.mContext.getPackageName());
    }
}
