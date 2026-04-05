package com.kopin.solos.menu;

/* JADX INFO: loaded from: classes24.dex */
public abstract class CustomMenuItem {
    private int mDrawableId;
    private final int mId;
    private String mTitle;
    private boolean mEnabled = true;
    private boolean mDismissOnTap = false;

    public abstract void onDataChanged();

    public CustomMenuItem(String title, int drawableId, int id) {
        this.mTitle = title;
        this.mDrawableId = drawableId;
        this.mId = id;
    }

    public int getId() {
        return this.mId;
    }

    public int getDrawableId() {
        return this.mDrawableId;
    }

    public void setDrawableId(int drawableId) {
        if (this.mDrawableId != drawableId) {
            this.mDrawableId = drawableId;
            onDataChanged();
        }
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        if (!this.mTitle.equals(title)) {
            this.mTitle = title;
            onDataChanged();
        }
    }

    public boolean isDismissOnTap() {
        return this.mDismissOnTap;
    }

    public void setDismissOnTap(boolean dismissOnTap) {
        this.mDismissOnTap = dismissOnTap;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            onDataChanged();
        }
    }

    public String toString() {
        return getClass().getSimpleName() + " [ " + this.mTitle + " ]";
    }
}
