package com.google.tagmanager;

import com.google.tagmanager.ResourceUtil;
import java.util.Set;

/* JADX INFO: loaded from: classes49.dex */
interface RuleEvaluationStepInfoBuilder {
    ResolvedRuleBuilder createResolvedRuleBuilder();

    void setEnabledFunctions(Set<ResourceUtil.ExpandedFunctionCall> set);
}
