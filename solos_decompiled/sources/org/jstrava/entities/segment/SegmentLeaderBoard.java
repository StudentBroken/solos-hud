package org.jstrava.entities.segment;

import java.util.List;

/* JADX INFO: loaded from: classes68.dex */
public class SegmentLeaderBoard {
    private int effort_count;
    private List<LeaderBoardEntry> entries;
    private int entry_count;

    public int getEffort_count() {
        return this.effort_count;
    }

    public void setEffort_count(int effort_count) {
        this.effort_count = effort_count;
    }

    public int getEntry_count() {
        return this.entry_count;
    }

    public void setEntry_count(int entry_count) {
        this.entry_count = entry_count;
    }

    public List<LeaderBoardEntry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<LeaderBoardEntry> entries) {
        this.entries = entries;
    }
}
