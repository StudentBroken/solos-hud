package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.analytics.midtier.proto.containertag.TypeSystem;

/* JADX INFO: loaded from: classes49.dex */
class DebugResolvedFunctionCallBuilder implements ResolvedFunctionCallBuilder {
    private MutableDebug.ResolvedFunctionCall resolvedFunctionCall;

    public DebugResolvedFunctionCallBuilder(MutableDebug.ResolvedFunctionCall functionCall) {
        this.resolvedFunctionCall = functionCall;
    }

    @Override // com.google.tagmanager.ResolvedFunctionCallBuilder
    public ResolvedPropertyBuilder createResolvedPropertyBuilder(String key) {
        MutableDebug.ResolvedProperty newProperty = this.resolvedFunctionCall.addProperties();
        newProperty.setKey(key);
        return new DebugResolvedPropertyBuilder(newProperty);
    }

    @Override // com.google.tagmanager.ResolvedFunctionCallBuilder
    public void setFunctionResult(TypeSystem.Value functionResult) {
        this.resolvedFunctionCall.setResult(DebugValueBuilder.copyImmutableValue(functionResult));
    }
}
