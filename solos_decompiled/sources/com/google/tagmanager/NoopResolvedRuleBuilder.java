package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.tagmanager.ResourceUtil;
import java.util.List;

/* JADX INFO: loaded from: classes49.dex */
class NoopResolvedRuleBuilder implements ResolvedRuleBuilder {
    NoopResolvedRuleBuilder() {
    }

    @Override // com.google.tagmanager.ResolvedRuleBuilder
    public ResolvedFunctionCallBuilder createNegativePredicate() {
        return new NoopResolvedFunctionCallBuilder();
    }

    @Override // com.google.tagmanager.ResolvedRuleBuilder
    public ResolvedFunctionCallBuilder createPositivePredicate() {
        return new NoopResolvedFunctionCallBuilder();
    }

    @Override // com.google.tagmanager.ResolvedRuleBuilder
    public ResolvedFunctionCallTranslatorList getAddedMacroFunctions() {
        return new NoopResolvedFunctionCallTranslatorList();
    }

    @Override // com.google.tagmanager.ResolvedRuleBuilder
    public ResolvedFunctionCallTranslatorList getRemovedMacroFunctions() {
        return new NoopResolvedFunctionCallTranslatorList();
    }

    @Override // com.google.tagmanager.ResolvedRuleBuilder
    public ResolvedFunctionCallTranslatorList getAddedTagFunctions() {
        return new NoopResolvedFunctionCallTranslatorList();
    }

    @Override // com.google.tagmanager.ResolvedRuleBuilder
    public ResolvedFunctionCallTranslatorList getRemovedTagFunctions() {
        return new NoopResolvedFunctionCallTranslatorList();
    }

    @Override // com.google.tagmanager.ResolvedRuleBuilder
    public void setValue(TypeSystem.Value result) {
    }

    public class NoopResolvedFunctionCallTranslatorList implements ResolvedFunctionCallTranslatorList {
        public NoopResolvedFunctionCallTranslatorList() {
        }

        @Override // com.google.tagmanager.ResolvedFunctionCallTranslatorList
        public void translateAndAddAll(List<ResourceUtil.ExpandedFunctionCall> functions, List<String> ruleNames) {
        }
    }
}
