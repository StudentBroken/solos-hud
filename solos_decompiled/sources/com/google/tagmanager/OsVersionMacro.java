package com.google.tagmanager;

import android.os.Build;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class OsVersionMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.OS_VERSION.toString();

    public static String getFunctionId() {
        return ID;
    }

    public OsVersionMacro() {
        super(ID, new String[0]);
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        return Types.objectToValue(Build.VERSION.RELEASE);
    }
}
