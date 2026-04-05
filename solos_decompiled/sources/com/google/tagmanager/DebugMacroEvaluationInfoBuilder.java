package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;

/* JADX INFO: loaded from: classes49.dex */
class DebugMacroEvaluationInfoBuilder implements MacroEvaluationInfoBuilder {
    private MutableDebug.MacroEvaluationInfo macroEvaluationInfo;

    public DebugMacroEvaluationInfoBuilder(MutableDebug.MacroEvaluationInfo macroEvaluationInfo) {
        this.macroEvaluationInfo = macroEvaluationInfo;
    }

    @Override // com.google.tagmanager.MacroEvaluationInfoBuilder
    public ResolvedFunctionCallBuilder createResult() {
        return new DebugResolvedFunctionCallBuilder(this.macroEvaluationInfo.getMutableResult());
    }

    @Override // com.google.tagmanager.MacroEvaluationInfoBuilder
    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new DebugRuleEvaluationStepInfoBuilder(this.macroEvaluationInfo.getMutableRulesEvaluation());
    }
}
