package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
class NoopDataLayerEventEvaluationInfoBuilder implements DataLayerEventEvaluationInfoBuilder {
    NoopDataLayerEventEvaluationInfoBuilder() {
    }

    @Override // com.google.tagmanager.DataLayerEventEvaluationInfoBuilder
    public ResolvedFunctionCallBuilder createAndAddResult() {
        return new NoopResolvedFunctionCallBuilder();
    }

    @Override // com.google.tagmanager.DataLayerEventEvaluationInfoBuilder
    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new NoopRuleEvaluationStepInfoBuilder();
    }
}
