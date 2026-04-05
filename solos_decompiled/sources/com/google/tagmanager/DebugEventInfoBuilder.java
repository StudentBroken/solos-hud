package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.android.gms.common.util.VisibleForTesting;

/* JADX INFO: loaded from: classes49.dex */
class DebugEventInfoBuilder implements EventInfoBuilder {
    private DebugDataLayerEventEvaluationInfoBuilder dataLayerEventBuilder;

    @VisibleForTesting
    MutableDebug.EventInfo eventInfoBuilder = MutableDebug.EventInfo.newMessage();
    private DebugInformationHandler handler;
    private DebugMacroEvaluationInfoBuilder macroBuilder;

    public DebugEventInfoBuilder(MutableDebug.EventInfo.EventType eventType, String containerVersion, String containerId, String key, DebugInformationHandler handler) {
        this.eventInfoBuilder.setEventType(eventType);
        this.eventInfoBuilder.setContainerVersion(containerVersion);
        this.eventInfoBuilder.setContainerId(containerId);
        this.eventInfoBuilder.setKey(key);
        this.handler = handler;
        if (eventType.equals(MutableDebug.EventInfo.EventType.DATA_LAYER_EVENT)) {
            this.dataLayerEventBuilder = new DebugDataLayerEventEvaluationInfoBuilder(this.eventInfoBuilder.getMutableDataLayerEventResult());
        } else {
            this.macroBuilder = new DebugMacroEvaluationInfoBuilder(this.eventInfoBuilder.getMutableMacroResult());
        }
    }

    @Override // com.google.tagmanager.EventInfoBuilder
    public MacroEvaluationInfoBuilder createMacroEvaluationInfoBuilder() {
        return this.macroBuilder;
    }

    @Override // com.google.tagmanager.EventInfoBuilder
    public DataLayerEventEvaluationInfoBuilder createDataLayerEventEvaluationInfoBuilder() {
        return this.dataLayerEventBuilder;
    }

    @Override // com.google.tagmanager.EventInfoBuilder
    public void processEventInfo() {
        this.handler.receiveEventInfo(this.eventInfoBuilder);
    }
}
