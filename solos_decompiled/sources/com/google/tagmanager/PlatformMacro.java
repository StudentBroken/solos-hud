package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class PlatformMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.PLATFORM.toString();
    private static final TypeSystem.Value PLATFORM = Types.objectToValue("Android");

    public static String getFunctionId() {
        return ID;
    }

    public PlatformMacro() {
        super(ID, new String[0]);
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        return PLATFORM;
    }
}
