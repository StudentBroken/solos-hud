package com.twitter.sdk.android.tweetui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.tweetui.internal.VideoControlView;
import com.twitter.sdk.android.tweetui.internal.VideoView;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes9.dex */
class PlayerController {
    private static final String TAG = "PlayerController";
    final VideoControlView videoControlView;
    final VideoView videoView;

    PlayerController(VideoView videoView, VideoControlView videoControlView) {
        this.videoView = videoView;
        this.videoControlView = videoControlView;
    }

    void prepare(MediaEntity entity) {
        try {
            boolean looping = TweetMediaUtils.isLooping(entity);
            String url = TweetMediaUtils.getSupportedVariant(entity).url;
            Uri uri = Uri.parse(url);
            setUpMediaControl(looping);
            this.videoView.setVideoURI(uri, looping);
            this.videoView.requestFocus();
            this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.twitter.sdk.android.tweetui.PlayerController.1
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mediaPlayer) {
                    PlayerController.this.videoView.start();
                }
            });
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "Error occurred during video playback", e);
        }
    }

    void setUpMediaControl(boolean looping) {
        if (looping) {
            setUpLoopControl();
        } else {
            setUpMediaControl();
        }
    }

    void setUpLoopControl() {
        this.videoControlView.setVisibility(4);
        this.videoView.setOnClickListener(new View.OnClickListener() { // from class: com.twitter.sdk.android.tweetui.PlayerController.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PlayerController.this.videoView.isPlaying()) {
                    PlayerController.this.videoView.pause();
                } else {
                    PlayerController.this.videoView.start();
                }
            }
        });
    }

    void setUpMediaControl() {
        this.videoView.setMediaController(this.videoControlView);
    }

    void cleanup() {
        this.videoView.stopPlayback();
    }
}
