package com.facebook.share.internal;

import com.facebook.internal.Validate;
import com.facebook.share.model.GameRequestContent;

/* JADX INFO: loaded from: classes41.dex */
public class GameRequestValidation {
    public static void validate(GameRequestContent content) {
        Validate.notNull(content.getMessage(), "message");
        if ((content.getObjectId() != null) ^ (content.getActionType() == GameRequestContent.ActionType.ASKFOR || content.getActionType() == GameRequestContent.ActionType.SEND)) {
            throw new IllegalArgumentException("Object id should be provided if and only if action type is send or askfor");
        }
        int mutex = 0;
        if (content.getRecipients() != null) {
            mutex = 0 + 1;
        }
        if (content.getSuggestions() != null) {
            mutex++;
        }
        if (content.getFilters() != null) {
            mutex++;
        }
        if (mutex > 1) {
            throw new IllegalArgumentException("Parameters to, filters and suggestions are mutually exclusive");
        }
    }
}
