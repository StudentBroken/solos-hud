package com.google.tagmanager;

import com.google.tagmanager.ResourceUtil;
import java.util.Set;

/* JADX INFO: loaded from: classes49.dex */
class NoopRuleEvaluationStepInfoBuilder implements RuleEvaluationStepInfoBuilder {
    NoopRuleEvaluationStepInfoBuilder() {
    }

    @Override // com.google.tagmanager.RuleEvaluationStepInfoBuilder
    public void setEnabledFunctions(Set<ResourceUtil.ExpandedFunctionCall> enabledFunctions) {
    }

    @Override // com.google.tagmanager.RuleEvaluationStepInfoBuilder
    public ResolvedRuleBuilder createResolvedRuleBuilder() {
        return new NoopResolvedRuleBuilder();
    }
}
