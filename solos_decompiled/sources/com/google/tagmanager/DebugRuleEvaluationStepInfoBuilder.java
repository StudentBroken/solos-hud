package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.tagmanager.ResourceUtil;
import java.util.Set;

/* JADX INFO: loaded from: classes49.dex */
class DebugRuleEvaluationStepInfoBuilder implements RuleEvaluationStepInfoBuilder {
    private MutableDebug.RuleEvaluationStepInfo ruleEvaluationStepInfo;

    public DebugRuleEvaluationStepInfoBuilder(MutableDebug.RuleEvaluationStepInfo ruleEvaluationStepInfo) {
        this.ruleEvaluationStepInfo = ruleEvaluationStepInfo;
    }

    @Override // com.google.tagmanager.RuleEvaluationStepInfoBuilder
    public void setEnabledFunctions(Set<ResourceUtil.ExpandedFunctionCall> enabledFunctions) {
        for (ResourceUtil.ExpandedFunctionCall enabledFunction : enabledFunctions) {
            this.ruleEvaluationStepInfo.addEnabledFunctions(DebugResolvedRuleBuilder.translateExpandedFunctionCall(enabledFunction));
        }
    }

    @Override // com.google.tagmanager.RuleEvaluationStepInfoBuilder
    public ResolvedRuleBuilder createResolvedRuleBuilder() {
        return new DebugResolvedRuleBuilder(this.ruleEvaluationStepInfo.addRules());
    }
}
