package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
class NoopMacroEvaluationInfoBuilder implements MacroEvaluationInfoBuilder {
    NoopMacroEvaluationInfoBuilder() {
    }

    @Override // com.google.tagmanager.MacroEvaluationInfoBuilder
    public ResolvedFunctionCallBuilder createResult() {
        return new NoopResolvedFunctionCallBuilder();
    }

    @Override // com.google.tagmanager.MacroEvaluationInfoBuilder
    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new NoopRuleEvaluationStepInfoBuilder();
    }
}
