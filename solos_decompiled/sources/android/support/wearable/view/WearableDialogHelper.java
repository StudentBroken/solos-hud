package android.support.wearable.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.Button;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class WearableDialogHelper {
    private static final String TAG = "WearableDialogHelper";
    private Drawable mNegativeIcon;
    private int mNegativeIconId;
    private Drawable mNeutralIcon;
    private int mNeutralIconId;
    private Drawable mPositiveIcon;
    private int mPositiveIconId;

    @VisibleForTesting
    Resources mResources;

    @VisibleForTesting
    Resources.Theme mTheme;

    public WearableDialogHelper(@NonNull Context context) {
        this(context.getResources(), context.getTheme());
    }

    public WearableDialogHelper(@NonNull Resources resources, @NonNull Resources.Theme theme) {
        this.mResources = resources;
        this.mTheme = theme;
    }

    @Nullable
    public Drawable getPositiveIcon() {
        return resolveDrawable(this.mPositiveIcon, this.mPositiveIconId);
    }

    @Nullable
    public Drawable getNegativeIcon() {
        return resolveDrawable(this.mNegativeIcon, this.mNegativeIconId);
    }

    @Nullable
    public Drawable getNeutralIcon() {
        return resolveDrawable(this.mNeutralIcon, this.mNeutralIconId);
    }

    @NonNull
    public WearableDialogHelper setPositiveIcon(@DrawableRes int resId) {
        this.mPositiveIconId = resId;
        this.mPositiveIcon = null;
        return this;
    }

    @NonNull
    public WearableDialogHelper setPositiveIcon(@Nullable Drawable icon) {
        this.mPositiveIcon = icon;
        this.mPositiveIconId = 0;
        return this;
    }

    @NonNull
    public WearableDialogHelper setNegativeIcon(@DrawableRes int resId) {
        this.mNegativeIconId = resId;
        this.mNegativeIcon = null;
        return this;
    }

    @NonNull
    public WearableDialogHelper setNegativeIcon(@Nullable Drawable icon) {
        this.mNegativeIcon = icon;
        this.mNegativeIconId = 0;
        return this;
    }

    @NonNull
    public WearableDialogHelper setNeutralIcon(@DrawableRes int resId) {
        this.mNeutralIconId = resId;
        this.mNeutralIcon = null;
        return this;
    }

    @NonNull
    public WearableDialogHelper setNeutralIcon(@Nullable Drawable icon) {
        this.mNeutralIcon = icon;
        this.mNeutralIconId = 0;
        return this;
    }

    public void apply(@NonNull AlertDialog dialog) {
        applyButton(dialog.getButton(-1), getPositiveIcon());
        applyButton(dialog.getButton(-2), getNegativeIcon());
        applyButton(dialog.getButton(-3), getNeutralIcon());
    }

    @VisibleForTesting
    void applyButton(@Nullable Button button, @Nullable Drawable drawable) {
        if (button != null) {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            button.setAllCaps(false);
        } else if (drawable != null) {
            Log.w(TAG, "non-null drawable used with missing button, did you call AlertDialog.create()?");
        }
    }

    @VisibleForTesting
    Drawable resolveDrawable(@Nullable Drawable drawable, @DrawableRes int resId) {
        return (drawable != null || resId == 0) ? drawable : this.mResources.getDrawable(resId, this.mTheme);
    }

    public static class DialogBuilder extends AlertDialog.Builder {
        private final WearableDialogHelper mHelper;

        public DialogBuilder(Context context) {
            super(context);
            this.mHelper = new WearableDialogHelper(context.getResources(), context.getTheme());
        }

        public DialogBuilder(Context context, int themeResId) {
            super(context, themeResId);
            this.mHelper = new WearableDialogHelper(context.getResources(), context.getTheme());
        }

        public WearableDialogHelper getHelper() {
            return this.mHelper;
        }

        public DialogBuilder setPositiveIcon(@DrawableRes int iconId) {
            this.mHelper.setPositiveIcon(iconId);
            return this;
        }

        public DialogBuilder setPositiveIcon(@Nullable Drawable icon) {
            this.mHelper.setPositiveIcon(icon);
            return this;
        }

        public DialogBuilder setNegativeIcon(@DrawableRes int iconId) {
            this.mHelper.setNegativeIcon(iconId);
            return this;
        }

        public DialogBuilder setNegativeIcon(@Nullable Drawable icon) {
            this.mHelper.setNegativeIcon(icon);
            return this;
        }

        public DialogBuilder setNeutralIcon(@DrawableRes int iconId) {
            this.mHelper.setNeutralIcon(iconId);
            return this;
        }

        public DialogBuilder setNeutralIcon(@Nullable Drawable icon) {
            this.mHelper.setNeutralIcon(icon);
            return this;
        }

        @Override // android.app.AlertDialog.Builder
        public AlertDialog create() {
            AlertDialog dialog = super.create();
            dialog.create();
            this.mHelper.apply(dialog);
            return dialog;
        }

        @Override // android.app.AlertDialog.Builder
        public AlertDialog show() {
            AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
