package com.twitter.sdk.android.tweetui;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;

/* JADX INFO: loaded from: classes9.dex */
public class TweetView extends BaseTweetView {
    private static final String VIEW_TYPE_NAME = "default";

    public TweetView(Context context, Tweet tweet) {
        super(context, tweet);
    }

    public TweetView(Context context, Tweet tweet, int styleResId) {
        super(context, tweet, styleResId);
    }

    TweetView(Context context, Tweet tweet, int styleResId, BaseTweetView.DependencyProvider dependencyProvider) {
        super(context, tweet, styleResId, dependencyProvider);
    }

    public TweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public TweetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTweetView
    protected int getLayout() {
        return R.layout.tw__tweet;
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTweetView
    void render() {
        super.render();
        setVerifiedCheck(this.tweet);
    }

    private void setVerifiedCheck(Tweet tweet) {
        if (tweet != null && tweet.user != null && tweet.user.verified) {
            this.verifiedCheckView.setVisibility(0);
        } else {
            this.verifiedCheckView.setVisibility(8);
        }
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTweetView
    String getViewTypeName() {
        return "default";
    }
}
