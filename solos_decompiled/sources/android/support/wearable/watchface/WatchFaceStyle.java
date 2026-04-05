package android.support.wearable.watchface;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.kopin.pupil.pagerenderer.Theme;
import java.util.Locale;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public class WatchFaceStyle implements Parcelable {

    @Deprecated
    public static final int AMBIENT_PEEK_MODE_HIDDEN = 1;

    @Deprecated
    public static final int AMBIENT_PEEK_MODE_VISIBLE = 0;
    public static final int BACKGROUND_VISIBILITY_INTERRUPTIVE = 0;
    public static final int BACKGROUND_VISIBILITY_PERSISTENT = 1;
    public static final Parcelable.Creator<WatchFaceStyle> CREATOR = new Parcelable.Creator<WatchFaceStyle>() { // from class: android.support.wearable.watchface.WatchFaceStyle.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WatchFaceStyle createFromParcel(Parcel p) {
            return new WatchFaceStyle(p.readBundle());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WatchFaceStyle[] newArray(int size) {
            return new WatchFaceStyle[size];
        }
    };
    public static final String KEY_ACCEPTS_TAPS = "acceptsTapEvents";
    public static final String KEY_AMBIENT_PEEK_MODE = "ambientPeekMode";
    public static final String KEY_BACKGROUND_VISIBILITY = "backgroundVisibility";
    public static final String KEY_CARD_PEEK_MODE = "cardPeekMode";
    public static final String KEY_CARD_PROGRESS_MODE = "cardProgressMode";
    public static final String KEY_COMPONENT = "component";
    public static final String KEY_HIDE_HOTWORD_INDICATOR = "hideHotwordIndicator";
    public static final String KEY_HIDE_STATUS_BAR = "hideStatusBar";
    public static final String KEY_HOTWORD_INDICATOR_GRAVITY = "hotwordIndicatorGravity";
    public static final String KEY_PEEK_CARD_OPACITY = "peekOpacityMode";
    public static final String KEY_SHOW_SYSTEM_UI_TIME = "showSystemUiTime";
    public static final String KEY_SHOW_UNREAD_INDICATOR = "showUnreadIndicator";
    public static final String KEY_STATUS_BAR_GRAVITY = "statusBarGravity";
    public static final String KEY_VIEW_PROTECTION_MODE = "viewProtectionMode";

    @Deprecated
    public static final int PEEK_MODE_NONE = 2;

    @Deprecated
    public static final int PEEK_MODE_SHORT = 1;

    @Deprecated
    public static final int PEEK_MODE_VARIABLE = 0;

    @Deprecated
    public static final int PEEK_OPACITY_MODE_OPAQUE = 0;

    @Deprecated
    public static final int PEEK_OPACITY_MODE_TRANSLUCENT = 1;
    public static final int PROGRESS_MODE_DISPLAY = 1;
    public static final int PROGRESS_MODE_NONE = 0;
    public static final int PROTECT_HOTWORD_INDICATOR = 2;
    public static final int PROTECT_STATUS_BAR = 1;
    public static final int PROTECT_WHOLE_SCREEN = 4;
    private final boolean acceptsTapEvents;
    private final int ambientPeekMode;
    private final int backgroundVisibility;
    private final int cardPeekMode;
    private final int cardProgressMode;
    private final ComponentName component;
    private final boolean hideHotwordIndicator;
    private final boolean hideStatusBar;
    private final int hotwordIndicatorGravity;
    private final int peekOpacityMode;
    private final boolean showSystemUiTime;
    private final boolean showUnreadCountIndicator;
    private final int statusBarGravity;
    private final int viewProtectionMode;

    private WatchFaceStyle(ComponentName component, int cardPeekMode, int cardProgressMode, int backgroundVisibility, boolean showSystemUiTime, int ambientPeekMode, int peekOpacityMode, int viewProtectionMode, int statusBarGravity, int hotwordIndicatorGravity, boolean showUnreadCountIndicator, boolean acceptsTapEvents, boolean hideHotwordIndicator, boolean hideStatusBar) {
        this.component = component;
        this.ambientPeekMode = ambientPeekMode;
        this.backgroundVisibility = backgroundVisibility;
        this.cardPeekMode = cardPeekMode;
        this.cardProgressMode = cardProgressMode;
        this.hotwordIndicatorGravity = hotwordIndicatorGravity;
        this.peekOpacityMode = peekOpacityMode;
        this.showSystemUiTime = showSystemUiTime;
        this.showUnreadCountIndicator = showUnreadCountIndicator;
        this.statusBarGravity = statusBarGravity;
        this.viewProtectionMode = viewProtectionMode;
        this.acceptsTapEvents = acceptsTapEvents;
        this.hideHotwordIndicator = hideHotwordIndicator;
        this.hideStatusBar = hideStatusBar;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeBundle(toBundle());
    }

    public WatchFaceStyle(Bundle bundle) {
        this.component = (ComponentName) bundle.getParcelable(KEY_COMPONENT);
        this.ambientPeekMode = bundle.getInt(KEY_AMBIENT_PEEK_MODE, 0);
        this.backgroundVisibility = bundle.getInt(KEY_BACKGROUND_VISIBILITY, 0);
        this.cardPeekMode = bundle.getInt(KEY_CARD_PEEK_MODE, 0);
        this.cardProgressMode = bundle.getInt(KEY_CARD_PROGRESS_MODE, 0);
        this.hotwordIndicatorGravity = bundle.getInt(KEY_HOTWORD_INDICATOR_GRAVITY);
        this.peekOpacityMode = bundle.getInt(KEY_PEEK_CARD_OPACITY, 0);
        this.showSystemUiTime = bundle.getBoolean(KEY_SHOW_SYSTEM_UI_TIME);
        this.showUnreadCountIndicator = bundle.getBoolean(KEY_SHOW_UNREAD_INDICATOR);
        this.statusBarGravity = bundle.getInt(KEY_STATUS_BAR_GRAVITY);
        this.viewProtectionMode = bundle.getInt(KEY_VIEW_PROTECTION_MODE);
        this.acceptsTapEvents = bundle.getBoolean(KEY_ACCEPTS_TAPS);
        this.hideHotwordIndicator = bundle.getBoolean(KEY_HIDE_HOTWORD_INDICATOR);
        this.hideStatusBar = bundle.getBoolean(KEY_HIDE_STATUS_BAR);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_COMPONENT, this.component);
        bundle.putInt(KEY_AMBIENT_PEEK_MODE, this.ambientPeekMode);
        bundle.putInt(KEY_BACKGROUND_VISIBILITY, this.backgroundVisibility);
        bundle.putInt(KEY_CARD_PEEK_MODE, this.cardPeekMode);
        bundle.putInt(KEY_CARD_PROGRESS_MODE, this.cardProgressMode);
        bundle.putInt(KEY_HOTWORD_INDICATOR_GRAVITY, this.hotwordIndicatorGravity);
        bundle.putInt(KEY_PEEK_CARD_OPACITY, this.peekOpacityMode);
        bundle.putBoolean(KEY_SHOW_SYSTEM_UI_TIME, this.showSystemUiTime);
        bundle.putBoolean(KEY_SHOW_UNREAD_INDICATOR, this.showUnreadCountIndicator);
        bundle.putInt(KEY_STATUS_BAR_GRAVITY, this.statusBarGravity);
        bundle.putInt(KEY_VIEW_PROTECTION_MODE, this.viewProtectionMode);
        bundle.putBoolean(KEY_ACCEPTS_TAPS, this.acceptsTapEvents);
        bundle.putBoolean(KEY_HIDE_HOTWORD_INDICATOR, this.hideHotwordIndicator);
        bundle.putBoolean(KEY_HIDE_STATUS_BAR, this.hideStatusBar);
        return bundle;
    }

    public boolean equals(Object otherObj) {
        if (!(otherObj instanceof WatchFaceStyle)) {
            return false;
        }
        WatchFaceStyle other = (WatchFaceStyle) otherObj;
        return this.component.equals(other.component) && this.cardPeekMode == other.cardPeekMode && this.cardProgressMode == other.cardProgressMode && this.backgroundVisibility == other.backgroundVisibility && this.showSystemUiTime == other.showSystemUiTime && this.ambientPeekMode == other.ambientPeekMode && this.peekOpacityMode == other.peekOpacityMode && this.viewProtectionMode == other.viewProtectionMode && this.statusBarGravity == other.statusBarGravity && this.hotwordIndicatorGravity == other.hotwordIndicatorGravity && this.showUnreadCountIndicator == other.showUnreadCountIndicator && this.acceptsTapEvents == other.acceptsTapEvents && this.hideHotwordIndicator == other.hideHotwordIndicator && this.hideStatusBar == other.hideStatusBar;
    }

    public int hashCode() {
        int hashCode = this.component.hashCode() + 31;
        return (((((((((((((((((((((((((hashCode * 31) + this.cardPeekMode) * 31) + this.cardProgressMode) * 31) + this.backgroundVisibility) * 31) + (this.showSystemUiTime ? 1 : 0)) * 31) + this.ambientPeekMode) * 31) + this.peekOpacityMode) * 31) + this.viewProtectionMode) * 31) + this.statusBarGravity) * 31) + this.hotwordIndicatorGravity) * 31) + (this.showUnreadCountIndicator ? 1 : 0)) * 31) + (this.acceptsTapEvents ? 1 : 0)) * 31) + (this.hideHotwordIndicator ? 1 : 0)) * 31) + (this.hideStatusBar ? 1 : 0);
    }

    public String toString() {
        Locale locale = Locale.US;
        Object[] objArr = new Object[14];
        objArr[0] = this.component == null ? Theme.TEXT_SIZE_DEFAULT : this.component.getShortClassName();
        objArr[1] = Integer.valueOf(this.cardPeekMode);
        objArr[2] = Integer.valueOf(this.cardProgressMode);
        objArr[3] = Integer.valueOf(this.backgroundVisibility);
        objArr[4] = Boolean.valueOf(this.showSystemUiTime);
        objArr[5] = Integer.valueOf(this.ambientPeekMode);
        objArr[6] = Integer.valueOf(this.peekOpacityMode);
        objArr[7] = Integer.valueOf(this.viewProtectionMode);
        objArr[8] = Integer.valueOf(this.statusBarGravity);
        objArr[9] = Integer.valueOf(this.hotwordIndicatorGravity);
        objArr[10] = Boolean.valueOf(this.showUnreadCountIndicator);
        objArr[11] = Boolean.valueOf(this.acceptsTapEvents);
        objArr[12] = Boolean.valueOf(this.hideHotwordIndicator);
        objArr[13] = Boolean.valueOf(this.hideStatusBar);
        return String.format(locale, "watch face %s (card %d/%d bg %d time %s ambientPeek %d peekOpacityMode %d viewProtectionMode %d statusBarGravity %d hotwordIndicatorGravity %d showUnreadCountIndicator %s acceptsTapEvents %s hideHotwordIndicator %s hideStatusBar %s)", objArr);
    }

    public ComponentName getComponent() {
        return this.component;
    }

    @Deprecated
    public int getCardPeekMode() {
        return this.cardPeekMode;
    }

    public int getCardProgressMode() {
        return this.cardProgressMode;
    }

    @Deprecated
    public int getPeekOpacityMode() {
        return this.peekOpacityMode;
    }

    public int getViewProtectionMode() {
        return this.viewProtectionMode;
    }

    public int getStatusBarGravity() {
        return this.statusBarGravity;
    }

    @Deprecated
    public int getHotwordIndicatorGravity() {
        return this.hotwordIndicatorGravity;
    }

    @Deprecated
    public int getBackgroundVisibility() {
        return this.backgroundVisibility;
    }

    public boolean getShowSystemUiTime() {
        return this.showSystemUiTime;
    }

    @Deprecated
    public int getAmbientPeekMode() {
        return this.ambientPeekMode;
    }

    public boolean getShowUnreadCountIndicator() {
        return this.showUnreadCountIndicator;
    }

    public boolean getAcceptsTapEvents() {
        return this.acceptsTapEvents;
    }

    @Deprecated
    public boolean getHideHotwordIndicator() {
        return this.hideHotwordIndicator;
    }

    public boolean getHideStatusBar() {
        return this.hideStatusBar;
    }

    public static class Builder {
        private boolean mAcceptsTapEvents;
        private int mAmbientPeekMode;
        private int mBackgroundVisibility;
        private int mCardPeekMode;
        private int mCardProgressMode;
        private final ComponentName mComponent;
        private boolean mHideHotwordIndicator;
        private boolean mHideStatusBar;
        private int mHotwordIndicatorGravity;
        private int mPeekOpacityMode;
        private boolean mShowSystemUiTime;
        private boolean mShowUnreadCountIndicator;
        private int mStatusBarGravity;
        private int mViewProtectionMode;

        public static Builder forComponentName(ComponentName component) {
            if (component == null) {
                throw new IllegalArgumentException("component must not be null.");
            }
            return new Builder(component);
        }

        public static Builder forActivity(Activity activity) {
            if (activity == null) {
                throw new IllegalArgumentException("activity must not be null.");
            }
            return new Builder(new ComponentName(activity, activity.getClass()));
        }

        public Builder(Service service) {
            this(new ComponentName(service, service.getClass()));
        }

        public static Builder forDefault() {
            return new Builder((ComponentName) null);
        }

        private Builder(ComponentName component) {
            this.mCardPeekMode = 0;
            this.mCardProgressMode = 0;
            this.mBackgroundVisibility = 0;
            this.mShowSystemUiTime = false;
            this.mAmbientPeekMode = 0;
            this.mPeekOpacityMode = 0;
            this.mViewProtectionMode = 0;
            this.mStatusBarGravity = 0;
            this.mHotwordIndicatorGravity = 0;
            this.mShowUnreadCountIndicator = false;
            this.mAcceptsTapEvents = false;
            this.mHideHotwordIndicator = false;
            this.mHideStatusBar = false;
            this.mComponent = component;
        }

        @Deprecated
        public Builder setCardPeekMode(int peekMode) {
            switch (peekMode) {
                case 0:
                case 1:
                case 2:
                    this.mCardPeekMode = peekMode;
                    return this;
                default:
                    throw new IllegalArgumentException("peekMode must be PEEK_MODE_VARIABLE or PEEK_MODE_SHORT");
            }
        }

        public Builder setCardProgressMode(int progressMode) {
            switch (progressMode) {
                case 0:
                case 1:
                    this.mCardProgressMode = progressMode;
                    return this;
                default:
                    throw new IllegalArgumentException("progressMode must be PROGRESS_MODE_NONE or PROGRESS_MODE_DISPLAY");
            }
        }

        @Deprecated
        public Builder setBackgroundVisibility(int backgroundVisibility) {
            switch (backgroundVisibility) {
                case 0:
                case 1:
                    this.mBackgroundVisibility = backgroundVisibility;
                    return this;
                default:
                    throw new IllegalArgumentException("backgroundVisibility must be BACKGROUND_VISIBILITY_INTERRUPTIVE or BACKGROUND_VISIBILITY_PERSISTENT");
            }
        }

        @Deprecated
        public Builder setShowSystemUiTime(boolean showSystemUiTime) {
            this.mShowSystemUiTime = showSystemUiTime;
            return this;
        }

        @Deprecated
        public Builder setAmbientPeekMode(int ambientPeekMode) {
            switch (ambientPeekMode) {
                case 0:
                case 1:
                    this.mAmbientPeekMode = ambientPeekMode;
                    return this;
                default:
                    throw new IllegalArgumentException("Ambient peek mode must be AMBIENT_PEEK_MODE_VISIBLE or AMBIENT_PEEK_MODE_HIDDEN");
            }
        }

        @Deprecated
        public Builder setPeekOpacityMode(int peekOpacityMode) {
            switch (peekOpacityMode) {
                case 0:
                case 1:
                    this.mPeekOpacityMode = peekOpacityMode;
                    return this;
                default:
                    throw new IllegalArgumentException("Peek card opacity must be PEEK_OPACITY_MODE_OPAQUE or PEEK_OPACITY_MODE_TRANSLUCENT");
            }
        }

        @Deprecated
        public Builder setViewProtection(int viewProtection) {
            return setViewProtectionMode(viewProtection);
        }

        public Builder setViewProtectionMode(int viewProtectionMode) {
            if (viewProtectionMode < 0 || viewProtectionMode > 7) {
                throw new IllegalArgumentException("View protection must be combination PROTECT_STATUS_BAR, PROTECT_HOTWORD_INDICATOR or PROTECT_WHOLE_SCREEN");
            }
            this.mViewProtectionMode = viewProtectionMode;
            return this;
        }

        public Builder setStatusBarGravity(int statusBarGravity) {
            this.mStatusBarGravity = statusBarGravity;
            return this;
        }

        @Deprecated
        public Builder setHotwordIndicatorGravity(int hotwordIndicatorGravity) {
            this.mHotwordIndicatorGravity = hotwordIndicatorGravity;
            return this;
        }

        public Builder setShowUnreadCountIndicator(boolean show) {
            this.mShowUnreadCountIndicator = show;
            return this;
        }

        public Builder setAcceptsTapEvents(boolean acceptsTapEvents) {
            this.mAcceptsTapEvents = acceptsTapEvents;
            return this;
        }

        @Deprecated
        public Builder setHideHotwordIndicator(boolean hideHotwordIndicator) {
            this.mHideHotwordIndicator = hideHotwordIndicator;
            return this;
        }

        public Builder setHideStatusBar(boolean hideStatusBar) {
            this.mHideStatusBar = hideStatusBar;
            return this;
        }

        public WatchFaceStyle build() {
            return new WatchFaceStyle(this.mComponent, this.mCardPeekMode, this.mCardProgressMode, this.mBackgroundVisibility, this.mShowSystemUiTime, this.mAmbientPeekMode, this.mPeekOpacityMode, this.mViewProtectionMode, this.mStatusBarGravity, this.mHotwordIndicatorGravity, this.mShowUnreadCountIndicator, this.mAcceptsTapEvents, this.mHideHotwordIndicator, this.mHideStatusBar);
        }
    }
}
