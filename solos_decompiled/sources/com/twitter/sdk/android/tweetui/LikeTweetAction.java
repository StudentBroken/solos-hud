package com.twitter.sdk.android.tweetui;

import android.view.View;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetBuilder;

/* JADX INFO: loaded from: classes9.dex */
class LikeTweetAction extends BaseTweetAction implements View.OnClickListener {
    final Tweet tweet;
    final TweetRepository tweetRepository;
    final TweetScribeClient tweetScribeClient;
    final TweetUi tweetUi;

    LikeTweetAction(Tweet tweet, TweetUi tweetUi, Callback<Tweet> cb) {
        this(tweet, tweetUi, cb, new TweetScribeClientImpl(tweetUi));
    }

    LikeTweetAction(Tweet tweet, TweetUi tweetUi, Callback<Tweet> cb, TweetScribeClient tweetScribeClient) {
        super(cb);
        this.tweet = tweet;
        this.tweetUi = tweetUi;
        this.tweetScribeClient = tweetScribeClient;
        this.tweetRepository = tweetUi.getTweetRepository();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view instanceof ToggleImageButton) {
            ToggleImageButton toggleImageButton = (ToggleImageButton) view;
            if (this.tweet.favorited) {
                scribeUnFavoriteAction();
                this.tweetRepository.unfavorite(this.tweet.id, new LikeCallback(toggleImageButton, this.tweet, getActionCallback()));
            } else {
                scribeFavoriteAction();
                this.tweetRepository.favorite(this.tweet.id, new LikeCallback(toggleImageButton, this.tweet, getActionCallback()));
            }
        }
    }

    void scribeFavoriteAction() {
        this.tweetScribeClient.favorite(this.tweet);
    }

    void scribeUnFavoriteAction() {
        this.tweetScribeClient.unfavorite(this.tweet);
    }

    static class LikeCallback extends Callback<Tweet> {
        ToggleImageButton button;
        Callback<Tweet> cb;
        Tweet tweet;

        LikeCallback(ToggleImageButton button, Tweet tweet, Callback<Tweet> cb) {
            this.button = button;
            this.tweet = tweet;
            this.cb = cb;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<Tweet> result) {
            this.cb.success(result);
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            if (exception instanceof TwitterApiException) {
                TwitterApiException apiException = (TwitterApiException) exception;
                int errorCode = apiException.getErrorCode();
                switch (errorCode) {
                    case TwitterApiConstants.Errors.ALREADY_FAVORITED /* 139 */:
                        Tweet favorited = new TweetBuilder().copy(this.tweet).setFavorited(true).build();
                        this.cb.success(new Result<>(favorited, null));
                        break;
                    case TwitterApiConstants.Errors.ALREADY_UNFAVORITED /* 144 */:
                        Tweet unfavorited = new TweetBuilder().copy(this.tweet).setFavorited(false).build();
                        this.cb.success(new Result<>(unfavorited, null));
                        break;
                    default:
                        this.button.setToggledOn(this.tweet.favorited);
                        this.cb.failure(exception);
                        break;
                }
                return;
            }
            this.button.setToggledOn(this.tweet.favorited);
            this.cb.failure(exception);
        }
    }
}
