package com.twitter.sdk.android.tweetui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.IntentUtils;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.UserUtils;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetBuilder;
import com.twitter.sdk.android.core.models.VideoInfo;
import com.twitter.sdk.android.tweetui.internal.TweetMediaView;
import io.fabric.sdk.android.Fabric;
import java.text.DateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes9.dex */
public abstract class BaseTweetView extends LinearLayout {
    static final double DEFAULT_ASPECT_RATIO = 1.7777777777777777d;
    private static final int DEFAULT_STYLE = R.style.tw__TweetLightStyle;
    private static final String EMPTY_STRING = "";
    static final long INVALID_ID = -1;
    static final double MEDIA_BG_DARK_OPACITY = 0.12d;
    static final double MEDIA_BG_LIGHT_OPACITY = 0.08d;
    static final double SECONDARY_TEXT_COLOR_DARK_OPACITY = 0.35d;
    static final double SECONDARY_TEXT_COLOR_LIGHT_OPACITY = 0.4d;
    private static final String TAG = "TweetUi";
    int actionColor;
    ImageView avatarView;
    int birdLogoResId;
    View bottomSeparator;
    int containerBgColor;
    RelativeLayout containerView;
    TextView contentView;
    final DependencyProvider dependencyProvider;
    TextView fullNameView;
    private LinkClickListener linkClickListener;
    ColorDrawable mediaBg;
    int mediaBgColor;
    TweetMediaView mediaView;
    private Uri permalinkUri;
    int photoErrorResId;
    int primaryTextColor;
    int retweetIconResId;
    TextView retweetedByView;
    TextView screenNameView;
    int secondaryTextColor;
    int styleResId;
    TextView timestampView;
    Tweet tweet;
    TweetActionBarView tweetActionBarView;
    boolean tweetActionsEnabled;
    ImageView twitterLogoView;
    ImageView verifiedCheckView;

    abstract int getLayout();

    abstract String getViewTypeName();

    BaseTweetView(Context context, Tweet tweet) {
        this(context, tweet, DEFAULT_STYLE);
    }

    BaseTweetView(Context context, Tweet tweet, int styleResId) {
        this(context, tweet, styleResId, new DependencyProvider());
    }

    BaseTweetView(Context context, Tweet tweet, int styleResId, DependencyProvider dependencyProvider) {
        super(context, null);
        this.dependencyProvider = dependencyProvider;
        initAttributes(styleResId);
        inflateView(context);
        findSubviews();
        applyStyles();
        if (isTweetUiEnabled()) {
            initTweetActions();
            setTweet(tweet);
        }
    }

    public BaseTweetView(Context context, AttributeSet attrs) {
        this(context, attrs, new DependencyProvider());
    }

    BaseTweetView(Context context, AttributeSet attrs, DependencyProvider dependencyProvider) {
        super(context, attrs);
        this.dependencyProvider = dependencyProvider;
        initXmlAttributes(context, attrs);
        inflateView(context);
    }

    public BaseTweetView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, new DependencyProvider());
    }

    @TargetApi(11)
    BaseTweetView(Context context, AttributeSet attrs, int defStyle, DependencyProvider dependencyProvider) {
        super(context, attrs, defStyle);
        this.dependencyProvider = dependencyProvider;
        initXmlAttributes(context, attrs);
        inflateView(context);
    }

    private void initAttributes(int styleResId) {
        this.styleResId = styleResId;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(styleResId, R.styleable.tw__TweetView);
        try {
            setStyleAttributes(a);
        } finally {
            a.recycle();
        }
    }

    private void initXmlAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.tw__TweetView, 0, 0);
            try {
                setXmlDataAttributes(a);
                setStyleAttributes(a);
            } finally {
                a.recycle();
            }
        }
    }

    private void setXmlDataAttributes(TypedArray a) {
        long tweetId = Utils.numberOrDefault(a.getString(R.styleable.tw__TweetView_tw__tweet_id), -1L).longValue();
        if (tweetId <= 0) {
            throw new IllegalArgumentException("Invalid tw__tweet_id");
        }
        setPermalinkUri(null, Long.valueOf(tweetId));
        this.tweet = new TweetBuilder().setId(tweetId).build();
    }

    private void setStyleAttributes(TypedArray a) {
        int i = ViewCompat.MEASURED_STATE_MASK;
        this.containerBgColor = a.getColor(R.styleable.tw__TweetView_tw__container_bg_color, getResources().getColor(R.color.tw__tweet_light_container_bg_color));
        this.primaryTextColor = a.getColor(R.styleable.tw__TweetView_tw__primary_text_color, getResources().getColor(R.color.tw__tweet_light_primary_text_color));
        this.actionColor = a.getColor(R.styleable.tw__TweetView_tw__action_color, getResources().getColor(R.color.tw__tweet_action_color));
        this.tweetActionsEnabled = a.getBoolean(R.styleable.tw__TweetView_tw__tweet_actions_enabled, false);
        boolean isLightBg = ColorUtils.isLightColor(this.containerBgColor);
        if (isLightBg) {
            this.photoErrorResId = R.drawable.tw__ic_tweet_photo_error_light;
            this.birdLogoResId = R.drawable.tw__ic_logo_blue;
            this.retweetIconResId = R.drawable.tw__ic_retweet_light;
        } else {
            this.photoErrorResId = R.drawable.tw__ic_tweet_photo_error_dark;
            this.birdLogoResId = R.drawable.tw__ic_logo_white;
            this.retweetIconResId = R.drawable.tw__ic_retweet_dark;
        }
        this.secondaryTextColor = ColorUtils.calculateOpacityTransform(isLightBg ? SECONDARY_TEXT_COLOR_LIGHT_OPACITY : SECONDARY_TEXT_COLOR_DARK_OPACITY, isLightBg ? -1 : -16777216, this.primaryTextColor);
        double d = isLightBg ? MEDIA_BG_LIGHT_OPACITY : MEDIA_BG_DARK_OPACITY;
        if (!isLightBg) {
            i = -1;
        }
        this.mediaBgColor = ColorUtils.calculateOpacityTransform(d, i, this.containerBgColor);
        this.mediaBg = new ColorDrawable(this.mediaBgColor);
    }

    private void inflateView(Context context) {
        LayoutInflater localInflater = LayoutInflater.from(context);
        View v = localInflater.inflate(getLayout(), (ViewGroup) null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        v.setLayoutParams(layoutParams);
        addView(v);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isTweetUiEnabled()) {
            findSubviews();
            applyStyles();
            initTweetActions();
            loadTweet();
        }
    }

    private void initTweetActions() {
        setTweetActionsEnabled(this.tweetActionsEnabled);
        this.tweetActionBarView.setOnActionCallback(new ResetTweetCallback(this, this.dependencyProvider.getTweetUi().getTweetRepository(), null));
    }

    boolean isTweetUiEnabled() {
        if (isInEditMode()) {
            return false;
        }
        try {
            this.dependencyProvider.getTweetUi();
            return true;
        } catch (IllegalStateException e) {
            Fabric.getLogger().e(TAG, e.getMessage());
            setEnabled(false);
            return false;
        }
    }

    void findSubviews() {
        this.containerView = (RelativeLayout) findViewById(R.id.tw__tweet_view);
        this.avatarView = (ImageView) findViewById(R.id.tw__tweet_author_avatar);
        this.fullNameView = (TextView) findViewById(R.id.tw__tweet_author_full_name);
        this.screenNameView = (TextView) findViewById(R.id.tw__tweet_author_screen_name);
        this.verifiedCheckView = (ImageView) findViewById(R.id.tw__tweet_author_verified);
        this.mediaView = (TweetMediaView) findViewById(R.id.tw__tweet_media);
        this.contentView = (TextView) findViewById(R.id.tw__tweet_text);
        this.timestampView = (TextView) findViewById(R.id.tw__tweet_timestamp);
        this.twitterLogoView = (ImageView) findViewById(R.id.tw__twitter_logo);
        this.retweetedByView = (TextView) findViewById(R.id.tw__tweet_retweeted_by);
        this.tweetActionBarView = (TweetActionBarView) findViewById(R.id.tw__tweet_action_bar);
        this.bottomSeparator = findViewById(R.id.bottom_separator);
    }

    public long getTweetId() {
        if (this.tweet == null) {
            return -1L;
        }
        return this.tweet.id;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
        render();
    }

    public Tweet getTweet() {
        return this.tweet;
    }

    public void setOnActionCallback(Callback<Tweet> actionCallback) {
        this.tweetActionBarView.setOnActionCallback(new ResetTweetCallback(this, this.dependencyProvider.getTweetUi().getTweetRepository(), actionCallback));
        this.tweetActionBarView.setTweet(this.tweet);
    }

    void render() {
        Tweet displayTweet = TweetUtils.getDisplayTweet(this.tweet);
        setProfilePhotoView(displayTweet);
        setName(displayTweet);
        setScreenName(displayTweet);
        setTimestamp(displayTweet);
        setTweetMedia(displayTweet);
        setText(displayTweet);
        setContentDescription(displayTweet);
        setTweetActions(this.tweet);
        showRetweetedBy(this.tweet);
        if (TweetUtils.isTweetResolvable(this.tweet)) {
            setPermalinkUri(this.tweet.user.screenName, Long.valueOf(getTweetId()));
        } else {
            this.permalinkUri = null;
        }
        setPermalinkLauncher();
        scribeImpression();
    }

    private void loadTweet() {
        final long tweetId = getTweetId();
        Callback<Tweet> repoCb = new Callback<Tweet>() { // from class: com.twitter.sdk.android.tweetui.BaseTweetView.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<Tweet> result) {
                BaseTweetView.this.setTweet(result.data);
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                Fabric.getLogger().d(BaseTweetView.TAG, String.format("loadTweet failure for Tweet Id %d.", Long.valueOf(tweetId)));
            }
        };
        this.dependencyProvider.getTweetUi().getTweetRepository().loadTweet(getTweetId(), repoCb);
    }

    Uri getPermalinkUri() {
        return this.permalinkUri;
    }

    void setPermalinkUri(String screenName, Long tweetId) {
        if (tweetId.longValue() > 0) {
            this.permalinkUri = TweetUtils.getPermalink(screenName, tweetId.longValue());
        }
    }

    private void setPermalinkLauncher() {
        View.OnClickListener listener = new PermalinkClickListener();
        setOnClickListener(listener);
        this.contentView.setOnClickListener(listener);
    }

    void showRetweetedBy(Tweet tweet) {
        if (tweet == null || tweet.retweetedStatus == null) {
            this.retweetedByView.setVisibility(8);
        } else {
            this.retweetedByView.setText(getResources().getString(R.string.tw__retweeted_by_format, tweet.user.name));
            this.retweetedByView.setVisibility(0);
        }
    }

    void launchPermalink() {
        Intent intent = new Intent("android.intent.action.VIEW", getPermalinkUri());
        if (!IntentUtils.safeStartActivity(getContext(), intent)) {
            Fabric.getLogger().e(TAG, "Activity cannot be found to open permalink URI");
        }
    }

    void scribeImpression() {
        if (this.tweet != null) {
            this.dependencyProvider.getTweetScribeClient().impression(this.tweet, getViewTypeName(), this.tweetActionsEnabled);
        }
    }

    void scribePermalinkClick() {
        if (this.tweet != null) {
            this.dependencyProvider.getTweetScribeClient().click(this.tweet, getViewTypeName());
        }
    }

    protected void applyStyles() {
        this.containerView.setBackgroundColor(this.containerBgColor);
        this.avatarView.setImageDrawable(this.mediaBg);
        this.mediaView.setImageDrawable(this.mediaBg);
        this.fullNameView.setTextColor(this.primaryTextColor);
        this.screenNameView.setTextColor(this.secondaryTextColor);
        this.contentView.setTextColor(this.primaryTextColor);
        this.timestampView.setTextColor(this.secondaryTextColor);
        this.twitterLogoView.setImageResource(this.birdLogoResId);
        this.retweetedByView.setTextColor(this.secondaryTextColor);
    }

    private void setName(Tweet displayTweet) {
        if (displayTweet != null && displayTweet.user != null) {
            this.fullNameView.setText(Utils.stringOrEmpty(displayTweet.user.name));
        } else {
            this.fullNameView.setText("");
        }
    }

    private void setScreenName(Tweet displayTweet) {
        if (displayTweet != null && displayTweet.user != null) {
            this.screenNameView.setText(UserUtils.formatScreenName(Utils.stringOrEmpty(displayTweet.user.screenName)));
        } else {
            this.screenNameView.setText("");
        }
    }

    @TargetApi(16)
    private void setText(Tweet displayTweet) {
        this.contentView.setMovementMethod(LinkMovementMethod.getInstance());
        this.contentView.setFocusable(false);
        if (Build.VERSION.SDK_INT >= 16) {
            this.contentView.setImportantForAccessibility(2);
        }
        CharSequence tweetText = Utils.charSeqOrEmpty(getLinkifiedText(displayTweet));
        if (!TextUtils.isEmpty(tweetText)) {
            this.contentView.setText(tweetText);
            this.contentView.setVisibility(0);
        } else {
            this.contentView.setText("");
            this.contentView.setVisibility(8);
        }
    }

    private void setTimestamp(Tweet displayTweet) {
        String formattedTimestamp;
        if (displayTweet != null && displayTweet.createdAt != null && TweetDateUtils.isValidTimestamp(displayTweet.createdAt)) {
            Long createdAtTimestamp = Long.valueOf(TweetDateUtils.apiTimeToLong(displayTweet.createdAt));
            String timestamp = TweetDateUtils.getRelativeTimeString(getResources(), System.currentTimeMillis(), createdAtTimestamp.longValue());
            formattedTimestamp = TweetDateUtils.dotPrefix(timestamp);
        } else {
            formattedTimestamp = "";
        }
        this.timestampView.setText(formattedTimestamp);
    }

    void setProfilePhotoView(Tweet displayTweet) {
        String url;
        Picasso imageLoader = this.dependencyProvider.getImageLoader();
        if (imageLoader != null) {
            if (displayTweet == null || displayTweet.user == null) {
                url = null;
            } else {
                url = UserUtils.getProfileImageUrlHttps(displayTweet.user, UserUtils.AvatarSize.REASONABLY_SMALL);
            }
            imageLoader.load(url).placeholder(this.mediaBg).into(this.avatarView);
        }
    }

    final void setTweetMedia(Tweet displayTweet) {
        clearMediaView();
        if (displayTweet != null && TweetMediaUtils.hasVideo(displayTweet)) {
            MediaEntity mediaEntity = TweetMediaUtils.getVideoEntity(displayTweet);
            this.mediaView.setVisibility(0);
            this.mediaView.setOverlayDrawable(getContext().getResources().getDrawable(R.drawable.tw__player_overlay));
            setMediaLauncher(displayTweet, mediaEntity);
            setTweetMedia(mediaEntity);
            this.dependencyProvider.getVideoScribeClient().impression(displayTweet.id, mediaEntity);
            return;
        }
        if (displayTweet != null && TweetMediaUtils.hasPhoto(displayTweet)) {
            MediaEntity mediaEntity2 = TweetMediaUtils.getPhotoEntity(displayTweet);
            this.mediaView.setVisibility(0);
            setPhotoLauncher(displayTweet, mediaEntity2);
            setTweetMedia(mediaEntity2);
            return;
        }
        this.mediaView.setVisibility(8);
    }

    private void setMediaLauncher(final Tweet displayTweet, final MediaEntity entity) {
        this.mediaView.setOnClickListener(new View.OnClickListener() { // from class: com.twitter.sdk.android.tweetui.BaseTweetView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoInfo.Variant variant = TweetMediaUtils.getSupportedVariant(entity);
                if (variant != null) {
                    Intent intent = new Intent(BaseTweetView.this.getContext(), (Class<?>) PlayerActivity.class);
                    intent.putExtra("MEDIA_ENTITY", entity);
                    intent.putExtra("TWEET_ID", displayTweet.id);
                    IntentUtils.safeStartActivity(BaseTweetView.this.getContext(), intent);
                }
            }
        });
    }

    private void setPhotoLauncher(final Tweet displayTweet, final MediaEntity entity) {
        this.mediaView.setOnClickListener(new View.OnClickListener() { // from class: com.twitter.sdk.android.tweetui.BaseTweetView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent(BaseTweetView.this.getContext(), (Class<?>) GalleryActivity.class);
                intent.putExtra("MEDIA_ENTITY", entity);
                intent.putExtra("TWEET_ID", displayTweet.id);
                IntentUtils.safeStartActivity(BaseTweetView.this.getContext(), intent);
            }
        });
    }

    void setTweetMedia(MediaEntity photoEntity) {
        Picasso imageLoader = this.dependencyProvider.getImageLoader();
        if (imageLoader != null) {
            this.mediaView.resetSize();
            this.mediaView.setAspectRatio(getAspectRatio(photoEntity));
            imageLoader.load(photoEntity.mediaUrlHttps).placeholder(this.mediaBg).fit().centerCrop().into(this.mediaView, new PicassoCallback());
        }
    }

    protected double getAspectRatio(MediaEntity photoEntity) {
        return (photoEntity == null || photoEntity.sizes == null || photoEntity.sizes.medium == null || photoEntity.sizes.medium.w == 0 || photoEntity.sizes.medium.h == 0) ? DEFAULT_ASPECT_RATIO : ((double) photoEntity.sizes.medium.w) / ((double) photoEntity.sizes.medium.h);
    }

    @TargetApi(16)
    protected void clearMediaView() {
        if (Build.VERSION.SDK_INT >= 16) {
            this.mediaView.setBackground(null);
        } else {
            this.mediaView.setBackgroundDrawable(null);
        }
        this.mediaView.setOverlayDrawable(null);
        this.mediaView.setOnClickListener(null);
        this.mediaView.setClickable(false);
    }

    class PicassoCallback implements com.squareup.picasso.Callback {
        PicassoCallback() {
        }

        @Override // com.squareup.picasso.Callback
        public void onSuccess() {
        }

        @Override // com.squareup.picasso.Callback
        public void onError() {
            BaseTweetView.this.setErrorImage();
        }
    }

    protected void setErrorImage() {
        Picasso imageLoader = this.dependencyProvider.getImageLoader();
        if (imageLoader != null) {
            imageLoader.load(this.photoErrorResId).into(this.mediaView, new com.squareup.picasso.Callback() { // from class: com.twitter.sdk.android.tweetui.BaseTweetView.4
                @Override // com.squareup.picasso.Callback
                public void onSuccess() {
                    BaseTweetView.this.mediaView.setBackgroundColor(BaseTweetView.this.mediaBgColor);
                }

                @Override // com.squareup.picasso.Callback
                public void onError() {
                }
            });
        }
    }

    protected CharSequence getLinkifiedText(Tweet displayTweet) {
        FormattedTweetText formattedText = this.dependencyProvider.getTweetUi().getTweetRepository().formatTweetText(displayTweet);
        if (formattedText == null) {
            return null;
        }
        boolean stripPhotoEntity = TweetMediaUtils.hasPhoto(displayTweet);
        return TweetTextLinkifier.linkifyUrls(formattedText, getLinkClickListener(), stripPhotoEntity, this.actionColor);
    }

    void setContentDescription(Tweet displayTweet) {
        if (!TweetUtils.isTweetResolvable(displayTweet)) {
            setContentDescription(getResources().getString(R.string.tw__loading_tweet));
            return;
        }
        FormattedTweetText formattedTweetText = this.dependencyProvider.getTweetUi().getTweetRepository().formatTweetText(displayTweet);
        String tweetText = formattedTweetText != null ? formattedTweetText.text : null;
        long createdAt = TweetDateUtils.apiTimeToLong(displayTweet.createdAt);
        String timestamp = null;
        if (createdAt != -1) {
            timestamp = DateFormat.getDateInstance().format(new Date(createdAt));
        }
        setContentDescription(getResources().getString(R.string.tw__tweet_content_description, Utils.stringOrEmpty(displayTweet.user.name), Utils.stringOrEmpty(tweetText), Utils.stringOrEmpty(timestamp)));
    }

    void setTweetActions(Tweet tweet) {
        this.tweetActionBarView.setTweet(tweet);
    }

    public void setTweetActionsEnabled(boolean enabled) {
        this.tweetActionsEnabled = enabled;
        if (this.tweetActionsEnabled) {
            this.tweetActionBarView.setVisibility(0);
            this.bottomSeparator.setVisibility(8);
        } else {
            this.tweetActionBarView.setVisibility(8);
            this.bottomSeparator.setVisibility(0);
        }
    }

    protected LinkClickListener getLinkClickListener() {
        if (this.linkClickListener == null) {
            this.linkClickListener = new LinkClickListener() { // from class: com.twitter.sdk.android.tweetui.BaseTweetView.5
                @Override // com.twitter.sdk.android.tweetui.LinkClickListener
                public void onUrlClicked(String url) {
                    if (!TextUtils.isEmpty(url)) {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        if (!IntentUtils.safeStartActivity(BaseTweetView.this.getContext(), intent)) {
                            Fabric.getLogger().e(BaseTweetView.TAG, "Activity cannot be found to open URL");
                        }
                    }
                }

                @Override // com.twitter.sdk.android.tweetui.LinkClickListener
                public void onPhotoClicked(MediaEntity mediaEntity) {
                }
            };
        }
        return this.linkClickListener;
    }

    class PermalinkClickListener implements View.OnClickListener {
        PermalinkClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (BaseTweetView.this.getPermalinkUri() != null) {
                BaseTweetView.this.scribePermalinkClick();
                BaseTweetView.this.launchPermalink();
            }
        }
    }

    static class DependencyProvider {
        TweetScribeClient tweetScribeClient;
        VideoScribeClient videoScribeClient;

        DependencyProvider() {
        }

        TweetUi getTweetUi() {
            return TweetUi.getInstance();
        }

        TweetScribeClient getTweetScribeClient() {
            if (this.tweetScribeClient == null) {
                this.tweetScribeClient = new TweetScribeClientImpl(getTweetUi());
            }
            return this.tweetScribeClient;
        }

        VideoScribeClient getVideoScribeClient() {
            if (this.videoScribeClient == null) {
                this.videoScribeClient = new VideoScribeClientImpl(getTweetUi());
            }
            return this.videoScribeClient;
        }

        Picasso getImageLoader() {
            return TweetUi.getInstance().getImageLoader();
        }
    }
}
