package com.twitter.sdk.android.tweetui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import com.twitter.sdk.android.core.IntentUtils;
import com.twitter.sdk.android.core.models.Tweet;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes9.dex */
class ShareTweetAction implements View.OnClickListener {
    final Tweet tweet;
    final TweetScribeClient tweetScribeClient;
    final TweetUi tweetUi;

    ShareTweetAction(Tweet tweet, TweetUi tweetUi) {
        this(tweet, tweetUi, new TweetScribeClientImpl(tweetUi));
    }

    ShareTweetAction(Tweet tweet, TweetUi tweetUi, TweetScribeClient tweetScribeClient) {
        this.tweet = tweet;
        this.tweetUi = tweetUi;
        this.tweetScribeClient = tweetScribeClient;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        onClick(v.getContext(), v.getResources());
    }

    void scribeShareAction() {
        this.tweetScribeClient.share(this.tweet);
    }

    void onClick(Context context, Resources resources) {
        if (this.tweet != null && this.tweet.user != null) {
            scribeShareAction();
            String shareSubject = getShareSubject(resources);
            String shareContent = getShareContent(resources);
            Intent shareIntent = getShareIntent(shareSubject, shareContent);
            String shareText = resources.getString(R.string.tw__share_tweet);
            Intent chooser = Intent.createChooser(shareIntent, shareText);
            launchShareIntent(chooser, context);
        }
    }

    String getShareContent(Resources resources) {
        return resources.getString(R.string.tw__share_content_format, this.tweet.user.screenName, Long.valueOf(this.tweet.id));
    }

    String getShareSubject(Resources resources) {
        return resources.getString(R.string.tw__share_subject_format, this.tweet.user.name, this.tweet.user.screenName);
    }

    void launchShareIntent(Intent chooser, Context context) {
        if (!IntentUtils.safeStartActivity(context, chooser)) {
            Fabric.getLogger().e("TweetUi", "Activity cannot be found to handle share intent");
        }
    }

    Intent getShareIntent(String subject, String content) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.SUBJECT", subject);
        intent.putExtra("android.intent.extra.TEXT", content);
        intent.setType("text/plain");
        return intent;
    }
}
