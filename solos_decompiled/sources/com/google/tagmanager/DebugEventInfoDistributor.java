package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;

/* JADX INFO: loaded from: classes49.dex */
class DebugEventInfoDistributor implements EventInfoDistributor {
    private String containerId;
    private String containerVersion;
    private DebugInformationHandler handler;

    public DebugEventInfoDistributor(DebugInformationHandler handler, String containerVersion, String containerId) {
        this.handler = handler;
        this.containerVersion = containerVersion;
        this.containerId = containerId;
    }

    @Override // com.google.tagmanager.EventInfoDistributor
    public EventInfoBuilder createMacroEvalutionEventInfo(String key) {
        return new DebugEventInfoBuilder(MutableDebug.EventInfo.EventType.MACRO_REFERENCE, this.containerVersion, this.containerId, key, this.handler);
    }

    @Override // com.google.tagmanager.EventInfoDistributor
    public EventInfoBuilder createDataLayerEventEvaluationEventInfo(String event) {
        return new DebugEventInfoBuilder(MutableDebug.EventInfo.EventType.DATA_LAYER_EVENT, this.containerVersion, this.containerId, event, this.handler);
    }

    @Override // com.google.tagmanager.EventInfoDistributor
    public boolean debugMode() {
        return true;
    }
}
