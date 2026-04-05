package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
abstract class TrackingTag extends FunctionCallImplementation {
    public abstract void evaluateTrackingTag(Map<String, TypeSystem.Value> map);

    public TrackingTag(String functionId, String... requiredKeys) {
        super(functionId, requiredKeys);
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return false;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        evaluateTrackingTag(parameters);
        return Types.getDefaultValue();
    }
}
