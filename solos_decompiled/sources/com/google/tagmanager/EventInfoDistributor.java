package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
interface EventInfoDistributor {
    EventInfoBuilder createDataLayerEventEvaluationEventInfo(String str);

    EventInfoBuilder createMacroEvalutionEventInfo(String str);

    boolean debugMode();
}
