package android.support.wearable.view;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.wearable.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class CardFragment extends Fragment {
    private static final String CONTENT_SAVED_STATE = "CardScrollView_content";
    public static final int EXPAND_DOWN = 1;
    public static final int EXPAND_UP = -1;
    public static final String KEY_ICON_RESOURCE = "CardFragment_icon";
    public static final String KEY_TEXT = "CardFragment_text";
    public static final String KEY_TITLE = "CardFragment_title";
    private boolean mActivityCreated;
    private CardFrame mCard;
    private Rect mCardPadding;
    private CardScrollView mCardScroll;
    private boolean mScrollToBottom;
    private boolean mScrollToTop;
    private int mCardGravity = 80;
    private boolean mExpansionEnabled = true;
    private float mExpansionFactor = 10.0f;
    private int mExpansionDirection = 1;
    private final Rect mCardMargins = new Rect(-1, -1, -1, -1);

    public static CardFragment create(CharSequence title, CharSequence description) {
        return create(title, description, 0);
    }

    public static CardFragment create(CharSequence title, CharSequence text, int iconRes) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        if (title != null) {
            args.putCharSequence(KEY_TITLE, title);
        }
        if (text != null) {
            args.putCharSequence(KEY_TEXT, text);
        }
        if (iconRes != 0) {
            args.putInt(KEY_ICON_RESOURCE, iconRes);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public void setExpansionEnabled(boolean enabled) {
        this.mExpansionEnabled = enabled;
        if (this.mCard != null) {
            this.mCard.setExpansionEnabled(this.mExpansionEnabled);
        }
    }

    public void setExpansionDirection(int direction) {
        this.mExpansionDirection = direction;
        if (this.mCard != null) {
            this.mCard.setExpansionDirection(this.mExpansionDirection);
        }
    }

    public void setCardGravity(int gravity) {
        this.mCardGravity = gravity & 112;
        if (this.mActivityCreated) {
            applyCardGravity();
        }
    }

    private void applyCardGravity() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mCard.getLayoutParams();
        lp.gravity = this.mCardGravity;
        this.mCard.setLayoutParams(lp);
    }

    public void setCardMargins(int left, int top, int right, int bottom) {
        this.mCardMargins.set(left, top, right, bottom);
        if (this.mActivityCreated) {
            applyCardMargins();
        }
    }

    public void setCardMarginTop(int top) {
        this.mCardMargins.top = top;
        if (this.mActivityCreated) {
            applyCardMargins();
        }
    }

    public void setCardMarginLeft(int left) {
        this.mCardMargins.left = left;
        if (this.mActivityCreated) {
            applyCardMargins();
        }
    }

    public void setCardMarginRight(int right) {
        this.mCardMargins.right = right;
        if (this.mActivityCreated) {
            applyCardMargins();
        }
    }

    public void setCardMarginBottom(int bottom) {
        this.mCardMargins.bottom = bottom;
        if (this.mActivityCreated) {
            applyCardMargins();
        }
    }

    private void applyCardMargins() {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) this.mCard.getLayoutParams();
        if (this.mCardMargins.left != -1) {
            lp.leftMargin = this.mCardMargins.left;
        }
        if (this.mCardMargins.top != -1) {
            lp.topMargin = this.mCardMargins.top;
        }
        if (this.mCardMargins.right != -1) {
            lp.rightMargin = this.mCardMargins.right;
        }
        if (this.mCardMargins.bottom != -1) {
            lp.bottomMargin = this.mCardMargins.bottom;
        }
        this.mCard.setLayoutParams(lp);
    }

    private void applyPadding() {
        if (this.mCard != null) {
            this.mCard.setContentPadding(this.mCardPadding.left, this.mCardPadding.top, this.mCardPadding.right, this.mCardPadding.bottom);
        }
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        this.mCardPadding = new Rect(left, top, right, bottom);
        applyPadding();
    }

    public Rect getContentPadding() {
        return new Rect(this.mCardPadding);
    }

    public void setContentPaddingLeft(int leftPadding) {
        this.mCardPadding.left = leftPadding;
        applyPadding();
    }

    public int getContentPaddingLeft() {
        return this.mCardPadding.left;
    }

    public void setContentPaddingTop(int topPadding) {
        this.mCardPadding.top = topPadding;
        applyPadding();
    }

    public int getContentPaddingTop() {
        return this.mCardPadding.top;
    }

    public void setContentPaddingRight(int rightPadding) {
        this.mCardPadding.right = rightPadding;
        applyPadding();
    }

    public int getContentPaddingRight() {
        return this.mCardPadding.right;
    }

    public void setContentPaddingBottom(int bottomPadding) {
        this.mCardPadding.bottom = bottomPadding;
        applyPadding();
    }

    public int getContentPaddingBottom() {
        return this.mCardPadding.bottom;
    }

    public void setExpansionFactor(float factor) {
        this.mExpansionFactor = factor;
        if (this.mCard != null) {
            this.mCard.setExpansionFactor(factor);
        }
    }

    public void scrollToTop() {
        if (this.mCardScroll != null) {
            this.mCardScroll.scrollBy(0, this.mCardScroll.getAvailableScrollDelta(-1));
        } else {
            this.mScrollToTop = true;
            this.mScrollToBottom = false;
        }
    }

    public void scrollToBottom() {
        if (this.mCardScroll != null) {
            this.mCardScroll.scrollBy(0, this.mCardScroll.getAvailableScrollDelta(1));
        } else {
            this.mScrollToTop = true;
            this.mScrollToBottom = false;
        }
    }

    @Override // android.app.Fragment
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mCardScroll = new CardScrollView(inflater.getContext());
        this.mCardScroll.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mCard = new CardFrame(inflater.getContext());
        this.mCard.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, this.mCardGravity));
        this.mCard.setExpansionEnabled(this.mExpansionEnabled);
        this.mCard.setExpansionFactor(this.mExpansionFactor);
        this.mCard.setExpansionDirection(this.mExpansionDirection);
        if (this.mCardPadding != null) {
            applyPadding();
        }
        this.mCardScroll.addView(this.mCard);
        if (this.mScrollToTop || this.mScrollToBottom) {
            this.mCardScroll.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: android.support.wearable.view.CardFragment.1
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    CardFragment.this.mCardScroll.removeOnLayoutChangeListener(this);
                    if (CardFragment.this.mScrollToTop) {
                        CardFragment.this.mScrollToTop = false;
                        CardFragment.this.scrollToTop();
                    } else if (CardFragment.this.mScrollToBottom) {
                        CardFragment.this.mScrollToBottom = false;
                        CardFragment.this.scrollToBottom();
                    }
                }
            });
        }
        Bundle contentSavedState = null;
        if (savedInstanceState != null) {
            contentSavedState = savedInstanceState.getBundle(CONTENT_SAVED_STATE);
        }
        View content = onCreateContentView(inflater, this.mCard, contentSavedState);
        if (content != null) {
            this.mCard.addView(content);
        }
        return this.mCardScroll;
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivityCreated = true;
        applyCardMargins();
        applyCardGravity();
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        this.mActivityCreated = false;
        super.onDestroy();
    }

    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView text;
        View view = inflater.inflate(R.layout.watch_card_content, container, false);
        Bundle args = getArguments();
        if (args != null) {
            TextView title = (TextView) view.findViewById(R.id.title);
            if (args.containsKey(KEY_TITLE) && title != null) {
                title.setText(args.getCharSequence(KEY_TITLE));
            }
            if (args.containsKey(KEY_TEXT) && (text = (TextView) view.findViewById(R.id.text)) != null) {
                text.setText(args.getCharSequence(KEY_TEXT));
            }
            if (args.containsKey(KEY_ICON_RESOURCE) && title != null) {
                title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, args.getInt(KEY_ICON_RESOURCE), 0);
            }
        }
        return view;
    }
}
