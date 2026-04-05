package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class EventMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.EVENT.toString();
    private final Runtime mRuntime;

    public static String getFunctionId() {
        return ID;
    }

    public EventMacro(Runtime runtime) {
        super(ID, new String[0]);
        this.mRuntime = runtime;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return false;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        String currentEventName = this.mRuntime.getCurrentEventName();
        return currentEventName == null ? Types.getDefaultValue() : Types.objectToValue(currentEventName);
    }
}
