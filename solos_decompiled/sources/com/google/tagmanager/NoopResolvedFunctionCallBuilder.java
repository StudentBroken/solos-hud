package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;

/* JADX INFO: loaded from: classes49.dex */
class NoopResolvedFunctionCallBuilder implements ResolvedFunctionCallBuilder {
    NoopResolvedFunctionCallBuilder() {
    }

    @Override // com.google.tagmanager.ResolvedFunctionCallBuilder
    public ResolvedPropertyBuilder createResolvedPropertyBuilder(String key) {
        return new NoopResolvedPropertyBuilder();
    }

    @Override // com.google.tagmanager.ResolvedFunctionCallBuilder
    public void setFunctionResult(TypeSystem.Value functionResult) {
    }
}
