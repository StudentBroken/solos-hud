package com.ua.sdk.page.association;

import com.ua.sdk.internal.ApiTransferObject;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationTransferObject extends ApiTransferObject {
    public static PageAssociationTransferObject fromPageAssocaition(PageAssociation pageAssociation) {
        PageAssociationTransferObject to = new PageAssociationTransferObject();
        if (pageAssociation instanceof PageAssociationImpl) {
            to.setLinkMap(((PageAssociationImpl) pageAssociation).getLinkMap());
        }
        return to;
    }

    public static PageAssociationImpl toPageAssocaitionImpl(PageAssociationTransferObject tranferObject) {
        if (tranferObject == null) {
            return null;
        }
        PageAssociationImpl association = new PageAssociationImpl();
        association.setLinkMap(tranferObject.getLinkMap());
        return association;
    }
}
