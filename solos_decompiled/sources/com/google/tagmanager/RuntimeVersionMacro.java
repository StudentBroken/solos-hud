package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class RuntimeVersionMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.RUNTIME_VERSION.toString();
    public static final long VERSION_NUMBER = 50788154;

    public static String getFunctionId() {
        return ID;
    }

    public RuntimeVersionMacro() {
        super(ID, new String[0]);
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        return Types.objectToValue(Long.valueOf(VERSION_NUMBER));
    }
}
