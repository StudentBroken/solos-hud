package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
class NoopEventInfoDistributor implements EventInfoDistributor {
    NoopEventInfoDistributor() {
    }

    @Override // com.google.tagmanager.EventInfoDistributor
    public EventInfoBuilder createMacroEvalutionEventInfo(String key) {
        return new NoopEventInfoBuilder();
    }

    @Override // com.google.tagmanager.EventInfoDistributor
    public EventInfoBuilder createDataLayerEventEvaluationEventInfo(String event) {
        return new NoopEventInfoBuilder();
    }

    @Override // com.google.tagmanager.EventInfoDistributor
    public boolean debugMode() {
        return false;
    }
}
