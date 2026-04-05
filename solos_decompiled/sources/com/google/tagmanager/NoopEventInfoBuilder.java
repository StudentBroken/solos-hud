package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
class NoopEventInfoBuilder implements EventInfoBuilder {
    NoopEventInfoBuilder() {
    }

    @Override // com.google.tagmanager.EventInfoBuilder
    public MacroEvaluationInfoBuilder createMacroEvaluationInfoBuilder() {
        return new NoopMacroEvaluationInfoBuilder();
    }

    @Override // com.google.tagmanager.EventInfoBuilder
    public DataLayerEventEvaluationInfoBuilder createDataLayerEventEvaluationInfoBuilder() {
        return new NoopDataLayerEventEvaluationInfoBuilder();
    }

    @Override // com.google.tagmanager.EventInfoBuilder
    public void processEventInfo() {
    }
}
