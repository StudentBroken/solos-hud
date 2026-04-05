package com.twitter.sdk.android.tweetcomposer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

/* JADX INFO: loaded from: classes29.dex */
public class ComposerActivity extends Activity {
    static final String EXTRA_CARD = "EXTRA_CARD";
    static final String EXTRA_THEME = "EXTRA_THEME";
    static final String EXTRA_USER_TOKEN = "EXTRA_USER_TOKEN";
    private static final int PLACEHOLDER_ID = -1;
    private static final String PLACEHOLDER_SCREEN_NAME = "";

    interface Finisher {
        void finish();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        TwitterAuthToken token = (TwitterAuthToken) intent.getParcelableExtra(EXTRA_USER_TOKEN);
        TwitterSession session = new TwitterSession(token, -1L, "");
        Card card = (Card) intent.getSerializableExtra(EXTRA_CARD);
        int themeResId = intent.getIntExtra(EXTRA_THEME, R.style.ComposerLight);
        setTheme(themeResId);
        setContentView(R.layout.tw__activity_composer);
        ComposerView composerView = (ComposerView) findViewById(R.id.tw__composer_view);
        new ComposerController(composerView, session, card, new FinisherImpl());
    }

    class FinisherImpl implements Finisher {
        FinisherImpl() {
        }

        @Override // com.twitter.sdk.android.tweetcomposer.ComposerActivity.Finisher
        public void finish() {
            ComposerActivity.this.finish();
        }
    }

    public static class Builder {
        private Card card;
        private final Context context;
        private int themeResId = R.style.ComposerLight;
        private TwitterAuthToken token;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null");
            }
            this.context = context;
        }

        public Builder session(TwitterSession session) {
            if (session == null) {
                throw new IllegalArgumentException("TwitterSession must not be null");
            }
            TwitterAuthToken token = session.getAuthToken();
            if (token == null) {
                throw new IllegalArgumentException("TwitterSession token must not be null");
            }
            this.token = token;
            return this;
        }

        public Builder card(Card card) {
            this.card = card;
            return this;
        }

        public Builder darkTheme() {
            this.themeResId = R.style.ComposerDark;
            return this;
        }

        public Intent createIntent() {
            if (this.token == null) {
                throw new IllegalStateException("Must set a TwitterSession");
            }
            Intent intent = new Intent(this.context, (Class<?>) ComposerActivity.class);
            intent.putExtra(ComposerActivity.EXTRA_USER_TOKEN, this.token);
            intent.putExtra(ComposerActivity.EXTRA_CARD, this.card);
            intent.putExtra(ComposerActivity.EXTRA_THEME, this.themeResId);
            return intent;
        }
    }
}
