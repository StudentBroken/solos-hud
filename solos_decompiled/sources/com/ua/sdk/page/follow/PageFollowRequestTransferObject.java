package com.ua.sdk.page.follow;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowRequestTransferObject extends ApiTransferObject {

    @SerializedName("page_follows")
    List<PageFollowPatchTransferObject> pageFollowTOs;

    void addPageFollow(PageFollow follow) {
        if (this.pageFollowTOs == null) {
            this.pageFollowTOs = new ArrayList();
        }
        this.pageFollowTOs.add(new PageFollowPatchTransferObject(follow));
    }

    public static PageFollowRequestTransferObject fromPageFollow(PageFollow pageFollow) {
        PageFollowRequestTransferObject pfrto = new PageFollowRequestTransferObject();
        List<PageFollow> follows = ((PageFollowImpl) pageFollow).getPageFollows();
        if (!follows.isEmpty()) {
            for (PageFollow page : follows) {
                pfrto.addPageFollow(page);
            }
        } else {
            if (pageFollow.getPageReference() != null) {
                pfrto.addLink("page", new Link(pageFollow.getPageReference().getHref(), pageFollow.getPageReference().getId()));
            }
            if (pageFollow.getUserReference() != null) {
                pfrto.addLink("user", new Link(String.format(UrlBuilderImpl.GET_USER_URL, pageFollow.getUserReference().getId()), pageFollow.getUserReference().getId()));
            }
        }
        return pfrto;
    }

    static class PageFollowPatchTransferObject extends ApiTransferObject {
        PageFollowPatchTransferObject(PageFollow pageFollow) {
            if (pageFollow.getPageReference() != null) {
                addLink("page", new Link(pageFollow.getPageReference().getHref(), pageFollow.getPageReference().getId()));
            }
            if (pageFollow.getUserReference() != null) {
                addLink("user", new Link(String.format(UrlBuilderImpl.GET_USER_URL, pageFollow.getUserReference().getId()), pageFollow.getUserReference().getId()));
            }
        }
    }
}
