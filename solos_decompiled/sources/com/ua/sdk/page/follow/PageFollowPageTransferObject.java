package com.ua.sdk.page.follow;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowPageTransferObject extends ApiTransferObject {
    public static final String KEY_PAGE_FOLLOWS = "page_follows";

    @SerializedName("_embedded")
    public Map<String, ArrayList<PageFollowTransferObject>> pageFollows;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    public Integer totalPageFollowsCount;

    private ArrayList<PageFollowTransferObject> getPageFollowList() {
        if (this.pageFollows == null) {
            return null;
        }
        return this.pageFollows.get("page_follows");
    }

    public static PageFollowPageTransferObject fromPage(PageFollowsListImpl pageFollowPage) {
        if (pageFollowPage == null) {
            return null;
        }
        PageFollowPageTransferObject pageFollowPageTransferObject = new PageFollowPageTransferObject();
        for (PageFollow pageFollow : pageFollowPage.getAll()) {
            pageFollowPageTransferObject.pageFollows.get("page_follows").add(PageFollowTransferObject.fromPageFollow(pageFollow));
        }
        if (pageFollowPage instanceof PageFollowsListImpl) {
            pageFollowPageTransferObject.setLinkMap(pageFollowPage.getLinkMap());
        }
        pageFollowPageTransferObject.totalPageFollowsCount = Integer.valueOf(pageFollowPage.getTotalCount());
        return pageFollowPageTransferObject;
    }

    public static PageFollowsListImpl toPage(PageFollowPageTransferObject to) {
        PageFollowsListImpl page = new PageFollowsListImpl();
        ArrayList<PageFollowTransferObject> pageFollowTransferObjects = to.getPageFollowList();
        for (PageFollowTransferObject pageFollowTransferObject : pageFollowTransferObjects) {
            try {
                PageFollowImpl pageFollow = PageFollowTransferObject.toPageFollowImpl(pageFollowTransferObject);
                page.add(pageFollow);
            } catch (UaException e) {
                UaLog.error("Error converting PageFollowTransferObject to PageFollowImpl.", (Throwable) e);
            }
        }
        page.setLinkMap(to.getLinkMap());
        page.setTotalCount(to.totalPageFollowsCount.intValue());
        return page;
    }
}
