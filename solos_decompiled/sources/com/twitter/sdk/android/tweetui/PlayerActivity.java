package com.twitter.sdk.android.tweetui;

import android.app.Activity;
import android.os.Bundle;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.tweetui.internal.VideoControlView;
import com.twitter.sdk.android.tweetui.internal.VideoView;

/* JADX INFO: loaded from: classes9.dex */
public class PlayerActivity extends Activity {
    static final String MEDIA_ENTITY = "MEDIA_ENTITY";
    static final String TWEET_ID = "TWEET_ID";
    PlayerController playerController;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tw__player_activity);
        VideoView videoView = (VideoView) findViewById(R.id.video_view);
        VideoControlView videoControlView = (VideoControlView) findViewById(R.id.video_control_view);
        long tweetId = getIntent().getLongExtra(TWEET_ID, 0L);
        MediaEntity entity = (MediaEntity) getIntent().getSerializableExtra(MEDIA_ENTITY);
        VideoScribeClient scribeClient = new VideoScribeClientImpl(TweetUi.getInstance());
        scribeClient.play(tweetId, entity);
        this.playerController = new PlayerController(videoView, videoControlView);
        this.playerController.prepare(entity);
    }

    @Override // android.app.Activity
    public void onDestroy() {
        this.playerController.cleanup();
        super.onDestroy();
    }
}
