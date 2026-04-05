package com.ua.sdk.page;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class PagePageTO extends ApiTransferObject {
    public static final String KEY_PAGE = "pages";

    @SerializedName("_embedded")
    public Map<String, ArrayList<PageTO>> pages;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    public Integer totalPagesCount;

    private ArrayList<PageTO> getPageList() {
        if (this.pages == null) {
            return null;
        }
        return this.pages.get("pages");
    }

    public static PageListImpl toPage(PagePageTO to) {
        PageListImpl page = new PageListImpl();
        ArrayList<PageTO> pageTransferObjects = to.getPageList();
        for (PageTO pageTransferObject : pageTransferObjects) {
            try {
                PageImpl pageImpl = PageTO.toPageImpl(pageTransferObject);
                page.add(pageImpl);
            } catch (UaException e) {
                UaLog.error("Error converting PageTO to PageImpl.", (Throwable) e);
            }
        }
        page.setLinkMap(to.getLinkMap());
        page.setTotalCount(to.totalPagesCount.intValue());
        return page;
    }
}
