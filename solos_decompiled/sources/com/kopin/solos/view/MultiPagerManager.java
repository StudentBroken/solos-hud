package com.kopin.solos.view;

import com.kopin.solos.view.Pager;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes48.dex */
public class MultiPagerManager implements Pager.OnPagerListener {
    private final boolean ENFORCE_PAGER_SELECTION_EXLCUSIVE;
    private final boolean ENFORCE_SINGLE_PAGER_ENABLED;
    private List<Pager> pagerList = new ArrayList();

    public MultiPagerManager(boolean enforceSinglePagerEnabled, boolean enforcePagerSelectionExclusive) {
        this.ENFORCE_SINGLE_PAGER_ENABLED = enforceSinglePagerEnabled;
        this.ENFORCE_PAGER_SELECTION_EXLCUSIVE = enforcePagerSelectionExclusive;
    }

    public void addPager(Pager pager) {
        this.pagerList.add(pager);
    }

    @Override // com.kopin.solos.view.Pager.OnPagerListener
    public void onEnabled(Pager pager) {
        List<Integer> selectedIds = new ArrayList<>();
        for (Pager p : this.pagerList) {
            if (p != pager) {
                selectedIds.add(Integer.valueOf(p.getCurrentPageId()));
                if (this.ENFORCE_SINGLE_PAGER_ENABLED) {
                    p.setEnabled(false);
                }
            }
        }
        if (this.ENFORCE_PAGER_SELECTION_EXLCUSIVE) {
            pager.hidePages(selectedIds);
        }
    }

    public void setEnabledAll(boolean enabled) {
        for (Pager p : this.pagerList) {
            p.setEnabled(enabled);
        }
    }

    @Override // com.kopin.solos.view.Pager.OnPagerListener
    public void onPageChange(Pager pager, int pageId) {
        Integer lastPage = (Integer) pager.getTag();
        if (lastPage == null || lastPage.intValue() != pageId) {
            pager.setTag(Integer.valueOf(pageId));
        }
    }

    public void moveNext(int pagerIndex) {
        this.pagerList.get(pagerIndex).moveNext();
    }

    public void movePrevious(int pagerIndex) {
        this.pagerList.get(pagerIndex).movePrevious();
    }
}
