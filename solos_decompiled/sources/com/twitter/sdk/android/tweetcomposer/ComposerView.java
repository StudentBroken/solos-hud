package com.twitter.sdk.android.tweetcomposer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.internal.UserUtils;
import com.twitter.sdk.android.core.internal.util.ObservableScrollView;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.ComposerController;

/* JADX INFO: loaded from: classes29.dex */
public class ComposerView extends LinearLayout {
    ImageView avatarView;
    ComposerController.ComposerCallbacks callbacks;
    ViewGroup cardView;
    TextView charCountView;
    ImageView closeView;
    View divider;
    private Picasso imageLoader;
    ColorDrawable mediaBg;
    ObservableScrollView scrollView;
    Button tweetButton;
    EditText tweetEditView;

    public ComposerView(Context context) {
        this(context, null);
    }

    public ComposerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(11)
    public ComposerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.imageLoader = Picasso.with(getContext());
        this.mediaBg = new ColorDrawable(context.getResources().getColor(R.color.tw__composer_light_gray));
        inflate(context, R.layout.tw__composer_view, this);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        findSubviews();
        this.closeView.setOnClickListener(new View.OnClickListener() { // from class: com.twitter.sdk.android.tweetcomposer.ComposerView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ComposerView.this.callbacks.onCloseClick();
            }
        });
        this.tweetButton.setOnClickListener(new View.OnClickListener() { // from class: com.twitter.sdk.android.tweetcomposer.ComposerView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ComposerView.this.callbacks.onTweetPost(ComposerView.this.getTweetText());
            }
        });
        this.tweetEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.twitter.sdk.android.tweetcomposer.ComposerView.3
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ComposerView.this.callbacks.onTweetPost(ComposerView.this.getTweetText());
                return true;
            }
        });
        this.tweetEditView.addTextChangedListener(new TextWatcher() { // from class: com.twitter.sdk.android.tweetcomposer.ComposerView.4
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                ComposerView.this.callbacks.onTextChanged(ComposerView.this.getTweetText());
            }
        });
        this.scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() { // from class: com.twitter.sdk.android.tweetcomposer.ComposerView.5
            @Override // com.twitter.sdk.android.core.internal.util.ObservableScrollView.ScrollViewListener
            public void onScrollChanged(int scrollY) {
                if (scrollY > 0) {
                    ComposerView.this.divider.setVisibility(0);
                } else {
                    ComposerView.this.divider.setVisibility(4);
                }
            }
        });
    }

    void findSubviews() {
        this.avatarView = (ImageView) findViewById(R.id.tw__author_avatar);
        this.closeView = (ImageView) findViewById(R.id.tw__composer_close);
        this.tweetEditView = (EditText) findViewById(R.id.tw__edit_tweet);
        this.charCountView = (TextView) findViewById(R.id.tw__char_count);
        this.tweetButton = (Button) findViewById(R.id.tw__post_tweet);
        this.scrollView = (ObservableScrollView) findViewById(R.id.tw__composer_scroll_view);
        this.divider = findViewById(R.id.tw__composer_profile_divider);
        this.cardView = (ViewGroup) findViewById(R.id.tw__card_view);
    }

    void setCallbacks(ComposerController.ComposerCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    void setProfilePhotoView(User user) {
        String url = UserUtils.getProfileImageUrlHttps(user, UserUtils.AvatarSize.REASONABLY_SMALL);
        if (this.imageLoader != null) {
            this.imageLoader.load(url).placeholder(this.mediaBg).into(this.avatarView);
        }
    }

    String getTweetText() {
        return this.tweetEditView.getText().toString();
    }

    void setTweetText(String text) {
        this.tweetEditView.setText(text);
    }

    void setCursorAtEnd() {
        this.tweetEditView.setSelection(getTweetText().length());
    }

    void setCharCount(int remainingCount) {
        this.charCountView.setText(Integer.toString(remainingCount));
    }

    void setCharCountTextStyle(int textStyleResId) {
        this.charCountView.setTextAppearance(getContext(), textStyleResId);
    }

    void postTweetEnabled(boolean enabled) {
        this.tweetButton.setEnabled(enabled);
    }

    void setCardView(View card) {
        this.cardView.addView(card);
        this.cardView.setVisibility(0);
    }
}
