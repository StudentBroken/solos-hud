package com.ua.sdk.page.association;

import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationRequestTransferObject extends ApiTransferObject {
    public static PageAssociationRequestTransferObject fromPageAssociation(PageAssociation pageAssociation) {
        PageAssociationRequestTransferObject pageAssociationRequestTransferObject = new PageAssociationRequestTransferObject();
        pageAssociationRequestTransferObject.addLink("to_page", new Link(((LinkEntityRef) pageAssociation.getToPage()).getHref(), pageAssociation.getToPage().getId()));
        pageAssociationRequestTransferObject.addLink("from_page", new Link(((LinkEntityRef) pageAssociation.getFromPage()).getHref(), pageAssociation.getFromPage().getId()));
        return pageAssociationRequestTransferObject;
    }
}
