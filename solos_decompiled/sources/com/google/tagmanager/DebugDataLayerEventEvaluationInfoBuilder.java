package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;

/* JADX INFO: loaded from: classes49.dex */
class DebugDataLayerEventEvaluationInfoBuilder implements DataLayerEventEvaluationInfoBuilder {
    private MutableDebug.DataLayerEventEvaluationInfo dataLayerEvent;

    public DebugDataLayerEventEvaluationInfoBuilder(MutableDebug.DataLayerEventEvaluationInfo dataLayerEvent) {
        this.dataLayerEvent = dataLayerEvent;
    }

    @Override // com.google.tagmanager.DataLayerEventEvaluationInfoBuilder
    public ResolvedFunctionCallBuilder createAndAddResult() {
        return new DebugResolvedFunctionCallBuilder(this.dataLayerEvent.addResults());
    }

    @Override // com.google.tagmanager.DataLayerEventEvaluationInfoBuilder
    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new DebugRuleEvaluationStepInfoBuilder(this.dataLayerEvent.getMutableRulesEvaluation());
    }
}
