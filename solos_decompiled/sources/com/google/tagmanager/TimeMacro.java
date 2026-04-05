package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class TimeMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.TIME.toString();

    public static String getFunctionId() {
        return ID;
    }

    public TimeMacro() {
        super(ID, new String[0]);
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return false;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        return Types.objectToValue(Long.valueOf(System.currentTimeMillis()));
    }
}
