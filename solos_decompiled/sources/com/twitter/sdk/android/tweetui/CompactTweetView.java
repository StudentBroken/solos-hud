package com.twitter.sdk.android.tweetui;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;

/* JADX INFO: loaded from: classes9.dex */
public class CompactTweetView extends BaseTweetView {
    private static final double MAX_LANDSCAPE_ASPECT_RATIO = 3.0d;
    private static final double MIN_LANDSCAPE_ASPECT_RATIO = 1.3333333333333333d;
    private static final double SQUARE_ASPECT_RATIO = 1.0d;
    private static final String VIEW_TYPE_NAME = "compact";

    public CompactTweetView(Context context, Tweet tweet) {
        super(context, tweet);
    }

    public CompactTweetView(Context context, Tweet tweet, int styleResId) {
        super(context, tweet, styleResId);
    }

    CompactTweetView(Context context, Tweet tweet, int styleResId, BaseTweetView.DependencyProvider dependencyProvider) {
        super(context, tweet, styleResId, dependencyProvider);
    }

    public CompactTweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public CompactTweetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTweetView
    protected int getLayout() {
        return R.layout.tw__tweet_compact;
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTweetView
    void render() {
        super.render();
        this.screenNameView.requestLayout();
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTweetView
    protected double getAspectRatio(MediaEntity photoEntity) {
        double ratio = super.getAspectRatio(photoEntity);
        if (ratio <= 1.0d) {
            return 1.0d;
        }
        return ratio > MAX_LANDSCAPE_ASPECT_RATIO ? MAX_LANDSCAPE_ASPECT_RATIO : ratio < MIN_LANDSCAPE_ASPECT_RATIO ? MIN_LANDSCAPE_ASPECT_RATIO : ratio;
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTweetView
    String getViewTypeName() {
        return VIEW_TYPE_NAME;
    }
}
