package com.twitter.sdk.android.tweetcomposer;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.internal.CardCreate;
import com.twitter.sdk.android.tweetcomposer.internal.CardData;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import retrofit.mime.TypedFile;

/* JADX INFO: loaded from: classes29.dex */
public class TweetUploadService extends IntentService {
    public static final String EXTRA_RETRY_INTENT = "EXTRA_RETRY_INTENT";
    static final String EXTRA_TWEET_CARD = "EXTRA_TWEET_CARD";
    public static final String EXTRA_TWEET_ID = "EXTRA_TWEET_ID";
    static final String EXTRA_TWEET_TEXT = "EXTRA_TWEET_TEXT";
    static final String EXTRA_USER_TOKEN = "EXTRA_USER_TOKEN";
    private static final int PLACEHOLDER_ID = -1;
    private static final String PLACEHOLDER_SCREEN_NAME = "";
    static final String TAG = "TweetUploadService";
    public static final String UPLOAD_FAILURE = "com.twitter.sdk.android.tweetcomposer.UPLOAD_FAILURE";
    public static final String UPLOAD_SUCCESS = "com.twitter.sdk.android.tweetcomposer.UPLOAD_SUCCESS";
    DependencyProvider dependencyProvider;
    Intent intent;
    Card tweetCard;
    String tweetText;
    TwitterSession twitterSession;

    public TweetUploadService() {
        this(new DependencyProvider());
    }

    TweetUploadService(DependencyProvider dependencyProvider) {
        super(TAG);
        this.dependencyProvider = dependencyProvider;
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        TwitterAuthToken token = (TwitterAuthToken) intent.getParcelableExtra(EXTRA_USER_TOKEN);
        this.intent = intent;
        this.twitterSession = new TwitterSession(token, -1L, "");
        this.tweetText = intent.getStringExtra(EXTRA_TWEET_TEXT);
        this.tweetCard = (Card) intent.getSerializableExtra(EXTRA_TWEET_CARD);
        if (Card.isAppCard(this.tweetCard)) {
            uploadAppCardTweet(this.twitterSession, this.tweetText, this.tweetCard);
        } else {
            uploadTweet(this.twitterSession, this.tweetText);
        }
    }

    void uploadTweet(TwitterSession session, String text) {
        ComposerApiClient client = this.dependencyProvider.getComposerApiClient(session);
        client.getComposerStatusesService().update(text, null, new Callback<Tweet>() { // from class: com.twitter.sdk.android.tweetcomposer.TweetUploadService.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<Tweet> result) {
                TweetUploadService.this.sendSuccessBroadcast(result.data.getId());
                TweetUploadService.this.stopSelf();
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                TweetUploadService.this.fail(exception);
            }
        });
    }

    void uploadAppCardTweet(TwitterSession session, String text, Card card) {
        ComposerApiClient client = this.dependencyProvider.getComposerApiClient(session);
        Uri uri = Uri.parse(card.imageUri);
        String path = FileUtils.getPath(this, uri);
        if (path == null) {
            fail(new TwitterException("Uri file path resolved to null"));
            return;
        }
        File file = new File(path);
        String mimeType = FileUtils.getMimeType(file);
        TypedFile media = new TypedFile(mimeType, file);
        client.getMediaService().upload(media, null, null, new AnonymousClass2(card, client, text));
    }

    /* JADX INFO: renamed from: com.twitter.sdk.android.tweetcomposer.TweetUploadService$2, reason: invalid class name */
    class AnonymousClass2 extends Callback<Media> {
        final /* synthetic */ Card val$card;
        final /* synthetic */ ComposerApiClient val$client;
        final /* synthetic */ String val$text;

        AnonymousClass2(Card card, ComposerApiClient composerApiClient, String str) {
            this.val$card = card;
            this.val$client = composerApiClient;
            this.val$text = str;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<Media> result) {
            CardData cardData = CardDataFactory.createAppCardData(this.val$card, Long.valueOf(result.data.mediaId), TweetUploadService.this.dependencyProvider.getAdvertisingId());
            this.val$client.getCardService().create(cardData, new Callback<CardCreate>() { // from class: com.twitter.sdk.android.tweetcomposer.TweetUploadService.2.1
                @Override // com.twitter.sdk.android.core.Callback
                public void success(Result<CardCreate> result2) {
                    String cardUri = result2.data.cardUri;
                    AnonymousClass2.this.val$client.getComposerStatusesService().update(AnonymousClass2.this.val$text, cardUri, new Callback<Tweet>() { // from class: com.twitter.sdk.android.tweetcomposer.TweetUploadService.2.1.1
                        @Override // com.twitter.sdk.android.core.Callback
                        public void success(Result<Tweet> result3) {
                            TweetUploadService.this.sendSuccessBroadcast(result3.data.getId());
                            TweetUploadService.this.stopSelf();
                        }

                        @Override // com.twitter.sdk.android.core.Callback
                        public void failure(TwitterException exception) {
                            TweetUploadService.this.fail(exception);
                        }
                    });
                }

                @Override // com.twitter.sdk.android.core.Callback
                public void failure(TwitterException exception) {
                    TweetUploadService.this.fail(exception);
                }
            });
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            TweetUploadService.this.fail(exception);
        }
    }

    void fail(TwitterException e) {
        sendFailureBroadcast(this.intent);
        Fabric.getLogger().e(TAG, "Post Tweet failed", e);
        stopSelf();
    }

    void sendSuccessBroadcast(long tweetId) {
        Intent intent = new Intent(UPLOAD_SUCCESS);
        intent.putExtra(EXTRA_TWEET_ID, tweetId);
        sendBroadcast(intent);
    }

    void sendFailureBroadcast(Intent original) {
        Intent intent = new Intent(UPLOAD_FAILURE);
        intent.putExtra(EXTRA_RETRY_INTENT, original);
        sendBroadcast(intent);
    }

    static class DependencyProvider {
        DependencyProvider() {
        }

        ComposerApiClient getComposerApiClient(TwitterSession session) {
            return TweetComposer.getInstance().getApiClient(session);
        }

        String getAdvertisingId() {
            return TweetComposer.getInstance().getAdvertisingId();
        }
    }
}
