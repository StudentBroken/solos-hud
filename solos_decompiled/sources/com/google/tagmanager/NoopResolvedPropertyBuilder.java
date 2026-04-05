package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;

/* JADX INFO: loaded from: classes49.dex */
class NoopResolvedPropertyBuilder implements ResolvedPropertyBuilder {
    NoopResolvedPropertyBuilder() {
    }

    @Override // com.google.tagmanager.ResolvedPropertyBuilder
    public ValueBuilder createPropertyValueBuilder(TypeSystem.Value propertyValue) {
        return new NoopValueBuilder();
    }
}
