package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.analytics.midtier.proto.containertag.MutableTypeSystem;
import com.google.analytics.midtier.proto.containertag.TypeSystem;

/* JADX INFO: loaded from: classes49.dex */
class DebugResolvedPropertyBuilder implements ResolvedPropertyBuilder {
    private MutableDebug.ResolvedProperty resolvedProperty;

    public DebugResolvedPropertyBuilder(MutableDebug.ResolvedProperty resolvedProperty) {
        this.resolvedProperty = resolvedProperty;
    }

    @Override // com.google.tagmanager.ResolvedPropertyBuilder
    public ValueBuilder createPropertyValueBuilder(TypeSystem.Value propertyValue) {
        MutableTypeSystem.Value val = DebugValueBuilder.copyImmutableValue(propertyValue);
        this.resolvedProperty.setValue(val);
        return new DebugValueBuilder(val);
    }
}
