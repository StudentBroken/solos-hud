package com.kopin.pupil.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import com.kopin.pupil.util.Action;
import com.kopin.pupil.util.MathHelper;
import com.kopin.pupil.util.Utility;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: loaded from: classes25.dex */
public class VCNotification implements Comparable<VCNotification> {
    public static final int TOAST_ICON_HEIGHT = 21;
    public static final int TOAST_ICON_LEFT_PADDING = 17;
    public static final int TOAST_ICON_RIGHT_PADDING = 14;
    public static final int TOAST_ICON_WIDTH = 21;
    private String mAcceptText;
    private Action mAction;
    private Bundle mBundle;
    private Action mDismissAction;
    private String mDismissText;
    private long mDuration;
    private String mHeadline;
    private Bitmap mIcon;
    private Priority mPriority;
    private Runnable mRunnableAction;
    private String mSubtitle;
    private ArrayList<TextPart> mTextParts;
    private long mTimestamp;
    private VCNotificationType mType;

    public enum Priority {
        LOW,
        NORMAL,
        MESSAGE,
        MESSAGE_FIRST_VIEW,
        CALL
    }

    public enum VCNotificationType {
        ACTIONABLE,
        POPUP,
        TOAST,
        DROP_DOWN
    }

    public VCNotification(VCNotificationType type, Bitmap icon, TextPart... text) {
        this.mHeadline = "";
        this.mType = VCNotificationType.ACTIONABLE;
        this.mSubtitle = "";
        this.mBundle = null;
        this.mAction = null;
        this.mDismissAction = null;
        this.mTextParts = new ArrayList<>();
        this.mDuration = -1L;
        this.mPriority = Priority.LOW;
        this.mTimestamp = System.nanoTime();
        this.mType = type;
        this.mTextParts.addAll(Arrays.asList(text));
        this.mIcon = icon;
    }

    public VCNotification(String headline, String subtitle, VCNotificationType type) {
        this.mHeadline = "";
        this.mType = VCNotificationType.ACTIONABLE;
        this.mSubtitle = "";
        this.mBundle = null;
        this.mAction = null;
        this.mDismissAction = null;
        this.mTextParts = new ArrayList<>();
        this.mDuration = -1L;
        this.mPriority = Priority.LOW;
        this.mTimestamp = System.nanoTime();
        this.mHeadline = headline;
        this.mSubtitle = subtitle;
        this.mType = type;
    }

    public String getHeadline() {
        return this.mHeadline;
    }

    public void setHeadline(String headline) {
        this.mHeadline = headline;
    }

    public String getSubtitle() {
        return this.mSubtitle;
    }

    public void setSubtitle(String subtitle) {
        this.mSubtitle = subtitle;
    }

    public VCNotificationType getNotificationType() {
        return this.mType;
    }

    public Action getAction() {
        return this.mAction;
    }

    public void setAction(Action action) {
        this.mAction = action;
    }

    public Action getDismissAction() {
        return this.mDismissAction;
    }

    public void setDismissAction(Action dismissAction) {
        this.mDismissAction = dismissAction;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public void setBundle(Bundle bundle) {
        this.mBundle = bundle;
    }

    public String getAcceptText() {
        return this.mAcceptText != null ? this.mAcceptText : "";
    }

    public void setAcceptText(String acceptText) {
        this.mAcceptText = acceptText;
    }

    public String getDismissText() {
        return this.mDismissText != null ? this.mDismissText : "";
    }

    public void setDismissText(String dismissText) {
        this.mDismissText = dismissText;
    }

    public Bitmap getIcon() {
        return this.mIcon;
    }

    public void setmIcon(Bitmap icon) {
        this.mIcon = icon;
    }

    public ArrayList<TextPart> getTextParts() {
        return this.mTextParts;
    }

    public void setTextParts(ArrayList<TextPart> textParts) {
        this.mTextParts = textParts;
    }

    public Runnable getRunnableAction() {
        return this.mRunnableAction;
    }

    public void setRunnableAction(Runnable runnableAction) {
        this.mRunnableAction = runnableAction;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public Priority getPriority() {
        return this.mPriority;
    }

    public void setPriority(Priority priority) {
        this.mPriority = priority;
    }

    @Override // java.lang.Comparable
    public int compareTo(VCNotification another) {
        int priority = this.mPriority.compareTo(another.mPriority);
        if (priority == 0) {
            return (int) MathHelper.clamp(-1L, 1L, another.mTimestamp - this.mTimestamp);
        }
        return priority;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("VCNotification[");
        sb.append(this.mType);
        if (!Utility.isNullOrEmpty(this.mHeadline)) {
            sb.append(" ; headline: ").append(this.mHeadline);
        }
        if (!Utility.isNullOrEmpty(this.mSubtitle)) {
            sb.append(" ; subtitle: ").append(this.mSubtitle);
        }
        if (!this.mTextParts.isEmpty()) {
            sb.append(" ; text parts: ").append(this.mTextParts);
        }
        sb.append(" ; duration: ").append(this.mDuration);
        sb.append(" ; priority: ").append(this.mPriority);
        return sb.append("]").toString();
    }

    public static class TextPart {
        private int color;
        private int size;
        private String text;
        private int textStyle;

        public TextPart(String text) {
            this(text, 20);
        }

        public TextPart(String text, int size) {
            this(text, size, 1);
        }

        public TextPart(String text, int size, int textStyle) {
            this.color = -1;
            this.text = text;
            this.size = size;
            this.textStyle = textStyle;
        }

        public TextPart(TextPart other, String text) {
            this.color = -1;
            this.text = text;
            this.size = other.size;
            this.color = other.color;
            this.textStyle = other.textStyle;
        }

        public TextPart color(int color) {
            this.color = color;
            return this;
        }

        public int getSize() {
            return this.size;
        }

        public String getText() {
            return this.text;
        }

        public int getTextStyle() {
            return this.textStyle;
        }

        public int getColor() {
            return this.color;
        }

        public String toString() {
            return "{" + this.text + "; " + this.size + "}";
        }
    }
}
