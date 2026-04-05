package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
interface EventInfoBuilder {
    DataLayerEventEvaluationInfoBuilder createDataLayerEventEvaluationInfoBuilder();

    MacroEvaluationInfoBuilder createMacroEvaluationInfoBuilder();

    void processEventInfo();
}
