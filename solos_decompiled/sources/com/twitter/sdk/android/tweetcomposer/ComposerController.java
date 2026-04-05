package com.twitter.sdk.android.tweetcomposer;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import com.twitter.Validator;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

/* JADX INFO: loaded from: classes29.dex */
class ComposerController {
    Card card;
    ComposerView composerView;
    final DependencyProvider dependencyProvider;
    ComposerActivity.Finisher finisher;
    TwitterSession session;

    public interface ComposerCallbacks {
        void onCloseClick();

        void onTextChanged(String str);

        void onTweetPost(String str);
    }

    ComposerController(ComposerView composerView, TwitterSession session, Card card, ComposerActivity.Finisher finisher) {
        this(composerView, session, card, finisher, new DependencyProvider());
    }

    ComposerController(ComposerView composerView, TwitterSession session, Card card, ComposerActivity.Finisher finisher, DependencyProvider dependencyProvider) {
        this.composerView = composerView;
        this.session = session;
        this.card = card;
        this.finisher = finisher;
        this.dependencyProvider = dependencyProvider;
        composerView.setCallbacks(new ComposerCallbacksImpl());
        composerView.setTweetText("");
        composerView.setCursorAtEnd();
        setProfilePhoto();
        setCardView(card);
        dependencyProvider.getScribeClient().impression(card);
    }

    void setProfilePhoto() {
        this.dependencyProvider.getApiClient(this.session).getAccountService().verifyCredentials(false, true, new Callback<User>() { // from class: com.twitter.sdk.android.tweetcomposer.ComposerController.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<User> result) {
                ComposerController.this.composerView.setProfilePhotoView(result.data);
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                ComposerController.this.composerView.setProfilePhotoView(null);
            }
        });
    }

    void setCardView(Card card) {
        if (card != null) {
            CardViewFactory cardViewFactory = this.dependencyProvider.getCardViewFactory();
            View view = cardViewFactory.createCard(this.composerView.getContext(), card);
            this.composerView.setCardView(view);
        }
    }

    class ComposerCallbacksImpl implements ComposerCallbacks {
        ComposerCallbacksImpl() {
        }

        @Override // com.twitter.sdk.android.tweetcomposer.ComposerController.ComposerCallbacks
        public void onTextChanged(String text) {
            int charCount = ComposerController.this.tweetTextLength(text);
            ComposerController.this.composerView.setCharCount(ComposerController.remainingCharCount(charCount));
            if (ComposerController.isTweetTextOverflow(charCount)) {
                ComposerController.this.composerView.setCharCountTextStyle(R.style.tw__ComposerCharCountOverflow);
            } else {
                ComposerController.this.composerView.setCharCountTextStyle(R.style.tw__ComposerCharCount);
            }
            ComposerController.this.composerView.postTweetEnabled(ComposerController.isPostEnabled(charCount));
        }

        @Override // com.twitter.sdk.android.tweetcomposer.ComposerController.ComposerCallbacks
        public void onTweetPost(String text) {
            ComposerController.this.dependencyProvider.getScribeClient().click(ComposerController.this.card, "tweet");
            Intent intent = new Intent(ComposerController.this.composerView.getContext(), (Class<?>) TweetUploadService.class);
            intent.putExtra("EXTRA_USER_TOKEN", ComposerController.this.session.getAuthToken());
            intent.putExtra("EXTRA_TWEET_TEXT", text);
            intent.putExtra("EXTRA_TWEET_CARD", ComposerController.this.card);
            ComposerController.this.composerView.getContext().startService(intent);
            ComposerController.this.finisher.finish();
        }

        @Override // com.twitter.sdk.android.tweetcomposer.ComposerController.ComposerCallbacks
        public void onCloseClick() {
            ComposerController.this.dependencyProvider.getScribeClient().click(ComposerController.this.card, "cancel");
            ComposerController.this.finisher.finish();
        }
    }

    int tweetTextLength(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        return this.dependencyProvider.getTweetValidator().getTweetLength(text);
    }

    static int remainingCharCount(int charCount) {
        return 140 - charCount;
    }

    static boolean isPostEnabled(int charCount) {
        return charCount > 0 && charCount <= 140;
    }

    static boolean isTweetTextOverflow(int charCount) {
        return charCount > 140;
    }

    static class DependencyProvider {
        final CardViewFactory cardViewFactory = new CardViewFactory();
        final Validator tweetValidator = new Validator();

        DependencyProvider() {
        }

        TwitterApiClient getApiClient(TwitterSession session) {
            return TwitterCore.getInstance().getApiClient(session);
        }

        CardViewFactory getCardViewFactory() {
            return this.cardViewFactory;
        }

        Validator getTweetValidator() {
            return this.tweetValidator;
        }

        ComposerScribeClient getScribeClient() {
            return new ComposerScribeClientImpl(TweetComposer.getInstance().getScribeClient());
        }
    }
}
