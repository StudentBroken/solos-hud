package com.facebook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.FragmentWrapper;

/* JADX INFO: loaded from: classes41.dex */
public abstract class FacebookButtonBase extends Button {
    private String analyticsButtonCreatedEventName;
    private String analyticsButtonTappedEventName;
    private View.OnClickListener externalOnClickListener;
    private View.OnClickListener internalOnClickListener;
    private boolean overrideCompoundPadding;
    private int overrideCompoundPaddingLeft;
    private int overrideCompoundPaddingRight;
    private FragmentWrapper parentFragment;

    protected abstract int getDefaultRequestCode();

    protected FacebookButtonBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, String analyticsButtonCreatedEventName, String analyticsButtonTappedEventName) {
        super(context, attrs, 0);
        defStyleRes = defStyleRes == 0 ? getDefaultStyleResource() : defStyleRes;
        configureButton(context, attrs, defStyleAttr, defStyleRes == 0 ? R.style.com_facebook_button : defStyleRes);
        this.analyticsButtonCreatedEventName = analyticsButtonCreatedEventName;
        this.analyticsButtonTappedEventName = analyticsButtonTappedEventName;
        setClickable(true);
        setFocusable(true);
    }

    public void setFragment(Fragment fragment) {
        this.parentFragment = new FragmentWrapper(fragment);
    }

    public void setFragment(android.app.Fragment fragment) {
        this.parentFragment = new FragmentWrapper(fragment);
    }

    public Fragment getFragment() {
        if (this.parentFragment != null) {
            return this.parentFragment.getSupportFragment();
        }
        return null;
    }

    public android.app.Fragment getNativeFragment() {
        if (this.parentFragment != null) {
            return this.parentFragment.getNativeFragment();
        }
        return null;
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener l) {
        this.externalOnClickListener = l;
    }

    public int getRequestCode() {
        return getDefaultRequestCode();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            logButtonCreated(getContext());
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        boolean centered = (getGravity() & 1) != 0;
        if (centered) {
            int compoundPaddingLeft = getCompoundPaddingLeft();
            int compoundPaddingRight = getCompoundPaddingRight();
            int compoundDrawablePadding = getCompoundDrawablePadding();
            int textX = compoundPaddingLeft + compoundDrawablePadding;
            int textContentWidth = (getWidth() - textX) - compoundPaddingRight;
            int textWidth = measureTextWidth(getText().toString());
            int textPaddingWidth = (textContentWidth - textWidth) / 2;
            int imagePaddingWidth = (compoundPaddingLeft - getPaddingLeft()) / 2;
            int inset = Math.min(textPaddingWidth, imagePaddingWidth);
            this.overrideCompoundPaddingLeft = compoundPaddingLeft - inset;
            this.overrideCompoundPaddingRight = compoundPaddingRight + inset;
            this.overrideCompoundPadding = true;
        }
        super.onDraw(canvas);
        this.overrideCompoundPadding = false;
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingLeft() {
        return this.overrideCompoundPadding ? this.overrideCompoundPaddingLeft : super.getCompoundPaddingLeft();
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingRight() {
        return this.overrideCompoundPadding ? this.overrideCompoundPaddingRight : super.getCompoundPaddingRight();
    }

    protected Activity getActivity() {
        Context context = getContext();
        while (!(context instanceof Activity) && (context instanceof ContextWrapper)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        throw new FacebookException("Unable to get Activity.");
    }

    protected int getDefaultStyleResource() {
        return 0;
    }

    protected int measureTextWidth(String text) {
        return (int) Math.ceil(getPaint().measureText(text));
    }

    protected void configureButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        parseBackgroundAttributes(context, attrs, defStyleAttr, defStyleRes);
        parseCompoundDrawableAttributes(context, attrs, defStyleAttr, defStyleRes);
        parseContentAttributes(context, attrs, defStyleAttr, defStyleRes);
        parseTextAttributes(context, attrs, defStyleAttr, defStyleRes);
        setupOnClickListener();
    }

    protected void callExternalOnClickListener(View v) {
        if (this.externalOnClickListener != null) {
            this.externalOnClickListener.onClick(v);
        }
    }

    protected void setInternalOnClickListener(View.OnClickListener l) {
        this.internalOnClickListener = l;
    }

    private void logButtonCreated(Context context) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logSdkEvent(this.analyticsButtonCreatedEventName, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logButtonTapped(Context context) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logSdkEvent(this.analyticsButtonTappedEventName, null, null);
    }

    private void parseBackgroundAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            int[] attrsResources = {android.R.attr.background};
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, attrsResources, defStyleAttr, defStyleRes);
            try {
                if (a.hasValue(0)) {
                    int backgroundResource = a.getResourceId(0, 0);
                    if (backgroundResource != 0) {
                        setBackgroundResource(backgroundResource);
                    } else {
                        setBackgroundColor(a.getColor(0, 0));
                    }
                } else {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.com_facebook_blue));
                }
            } finally {
                a.recycle();
            }
        }
    }

    @SuppressLint({"ResourceType"})
    private void parseCompoundDrawableAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int[] attrsResources = {android.R.attr.drawableLeft, android.R.attr.drawableTop, android.R.attr.drawableRight, android.R.attr.drawableBottom, android.R.attr.drawablePadding};
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, attrsResources, defStyleAttr, defStyleRes);
        try {
            setCompoundDrawablesWithIntrinsicBounds(a.getResourceId(0, 0), a.getResourceId(1, 0), a.getResourceId(2, 0), a.getResourceId(3, 0));
            setCompoundDrawablePadding(a.getDimensionPixelSize(4, 0));
        } finally {
            a.recycle();
        }
    }

    private void parseContentAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int[] attrsResources = {android.R.attr.paddingLeft, android.R.attr.paddingTop, android.R.attr.paddingRight, android.R.attr.paddingBottom};
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, attrsResources, defStyleAttr, defStyleRes);
        try {
            setPadding(a.getDimensionPixelSize(0, 0), a.getDimensionPixelSize(1, 0), a.getDimensionPixelSize(2, 0), a.getDimensionPixelSize(3, 0));
        } finally {
            a.recycle();
        }
    }

    private void parseTextAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int[] colorResources = {android.R.attr.textColor};
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, colorResources, defStyleAttr, defStyleRes);
        try {
            setTextColor(a.getColorStateList(0));
            a.recycle();
            int[] gravityResources = {android.R.attr.gravity};
            a = context.getTheme().obtainStyledAttributes(attrs, gravityResources, defStyleAttr, defStyleRes);
            try {
                setGravity(a.getInt(0, 17));
                a.recycle();
                int[] attrsResources = {android.R.attr.textSize, android.R.attr.textStyle, android.R.attr.text};
                a = context.getTheme().obtainStyledAttributes(attrs, attrsResources, defStyleAttr, defStyleRes);
                try {
                    setTextSize(0, a.getDimensionPixelSize(0, 0));
                    setTypeface(Typeface.defaultFromStyle(a.getInt(1, 1)));
                    setText(a.getString(2));
                } finally {
                }
            } finally {
            }
        } finally {
        }
    }

    private void setupOnClickListener() {
        super.setOnClickListener(new View.OnClickListener() { // from class: com.facebook.FacebookButtonBase.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FacebookButtonBase.this.logButtonTapped(FacebookButtonBase.this.getContext());
                if (FacebookButtonBase.this.internalOnClickListener != null) {
                    FacebookButtonBase.this.internalOnClickListener.onClick(v);
                } else if (FacebookButtonBase.this.externalOnClickListener != null) {
                    FacebookButtonBase.this.externalOnClickListener.onClick(v);
                }
            }
        });
    }
}
