package com.twitter.sdk.android.tweetui.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.twitter.sdk.android.tweetui.R;

/* JADX INFO: loaded from: classes9.dex */
public class VideoControlView extends FrameLayout {
    static final int FADE_DURATION_MS = 150;
    static final long PROGRESS_BAR_TICKS = 1000;
    private static final int SHOW_PROGRESS_MSG = 1001;
    TextView currentTime;
    TextView duration;

    @SuppressLint({"HandlerLeak"})
    private final Handler handler;
    MediaPlayerControl player;
    SeekBar seekBar;
    ImageButton stateControl;

    public interface MediaPlayerControl {
        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        int getAudioSessionId();

        int getBufferPercentage();

        int getCurrentPosition();

        int getDuration();

        boolean isPlaying();

        void pause();

        void seekTo(int i);

        void start();
    }

    public VideoControlView(Context context) {
        super(context);
        this.handler = new Handler() { // from class: com.twitter.sdk.android.tweetui.internal.VideoControlView.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1001) {
                    VideoControlView.this.updateProgress();
                    VideoControlView.this.updateStateControl();
                    if (VideoControlView.this.isShowing() && VideoControlView.this.player.isPlaying()) {
                        Message msg2 = obtainMessage(1001);
                        sendMessageDelayed(msg2, 500L);
                    }
                }
            }
        };
    }

    public VideoControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.handler = new Handler() { // from class: com.twitter.sdk.android.tweetui.internal.VideoControlView.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1001) {
                    VideoControlView.this.updateProgress();
                    VideoControlView.this.updateStateControl();
                    if (VideoControlView.this.isShowing() && VideoControlView.this.player.isPlaying()) {
                        Message msg2 = obtainMessage(1001);
                        sendMessageDelayed(msg2, 500L);
                    }
                }
            }
        };
    }

    public VideoControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.handler = new Handler() { // from class: com.twitter.sdk.android.tweetui.internal.VideoControlView.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1001) {
                    VideoControlView.this.updateProgress();
                    VideoControlView.this.updateStateControl();
                    if (VideoControlView.this.isShowing() && VideoControlView.this.player.isPlaying()) {
                        Message msg2 = obtainMessage(1001);
                        sendMessageDelayed(msg2, 500L);
                    }
                }
            }
        };
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        this.player = player;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        initSubviews();
    }

    void initSubviews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        inflater.inflate(R.layout.tw__video_control, this);
        this.stateControl = (ImageButton) findViewById(R.id.tw__state_control);
        this.currentTime = (TextView) findViewById(R.id.tw__current_time);
        this.duration = (TextView) findViewById(R.id.tw__duration);
        this.seekBar = (SeekBar) findViewById(R.id.tw__progress);
        this.seekBar.setMax(1000);
        this.seekBar.setOnSeekBarChangeListener(createProgressChangeListener());
        this.stateControl.setOnClickListener(createStateControlClickListener());
        setDuration(0);
        setCurrentTime(0);
        setProgress(0, 0, 0);
    }

    View.OnClickListener createStateControlClickListener() {
        return new View.OnClickListener() { // from class: com.twitter.sdk.android.tweetui.internal.VideoControlView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (VideoControlView.this.player.isPlaying()) {
                    VideoControlView.this.player.pause();
                } else {
                    VideoControlView.this.player.start();
                }
                VideoControlView.this.show();
            }
        };
    }

    SeekBar.OnSeekBarChangeListener createProgressChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() { // from class: com.twitter.sdk.android.tweetui.internal.VideoControlView.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int duration = VideoControlView.this.player.getDuration();
                    long position = ((long) (duration * progress)) / 1000;
                    VideoControlView.this.player.seekTo((int) position);
                    VideoControlView.this.setCurrentTime((int) position);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                VideoControlView.this.handler.removeMessages(1001);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                VideoControlView.this.updateStateControl();
                VideoControlView.this.handler.sendEmptyMessage(1001);
            }
        };
    }

    void updateProgress() {
        int duration = this.player.getDuration();
        int currentTime = this.player.getCurrentPosition();
        int bufferPercentage = this.player.getBufferPercentage();
        setDuration(duration);
        setCurrentTime(currentTime);
        setProgress(currentTime, duration, bufferPercentage);
    }

    void setDuration(int durationMillis) {
        this.duration.setText(formatPlaybackTime(durationMillis));
    }

    void setCurrentTime(int currentTimeMillis) {
        this.currentTime.setText(formatPlaybackTime(currentTimeMillis));
    }

    void setProgress(int currentTimeMillis, int durationMillis, int bufferPercentage) {
        long pos = durationMillis > 0 ? (1000 * ((long) currentTimeMillis)) / ((long) durationMillis) : 0L;
        this.seekBar.setProgress((int) pos);
        this.seekBar.setSecondaryProgress(bufferPercentage * 10);
    }

    void updateStateControl() {
        if (this.player.isPlaying()) {
            setPauseDrawable();
        } else if (this.player.getCurrentPosition() >= this.player.getDuration() - 500) {
            setReplayDrawable();
        } else {
            setPlayDrawable();
        }
    }

    void setPlayDrawable() {
        this.stateControl.setImageResource(R.drawable.tw__video_play_btn);
        this.stateControl.setContentDescription(getContext().getString(R.string.tw__play));
    }

    void setPauseDrawable() {
        this.stateControl.setImageResource(R.drawable.tw__video_pause_btn);
        this.stateControl.setContentDescription(getContext().getString(R.string.tw__pause));
    }

    void setReplayDrawable() {
        this.stateControl.setImageResource(R.drawable.tw__video_replay_btn);
        this.stateControl.setContentDescription(getContext().getString(R.string.tw__replay));
    }

    void hide() {
        this.handler.removeMessages(1001);
        if (Build.VERSION.SDK_INT >= 12) {
            AnimationUtils.fadeOut(this, FADE_DURATION_MS);
        } else {
            setVisibility(4);
        }
    }

    void show() {
        this.handler.sendEmptyMessage(1001);
        updateStateControl();
        if (Build.VERSION.SDK_INT >= 12) {
            AnimationUtils.fadeIn(this, FADE_DURATION_MS);
        } else {
            setVisibility(0);
        }
    }

    public boolean isShowing() {
        return getVisibility() == 0;
    }

    String formatPlaybackTime(long timeMillis) {
        int timeSeconds = (int) (timeMillis / 1000);
        int seconds = timeSeconds % 60;
        int minutes = (timeSeconds / 60) % 60;
        int hours = timeSeconds / 3600;
        return hours > 0 ? getContext().getString(R.string.tw__time_format_long, Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)) : getContext().getString(R.string.tw__time_format_short, Integer.valueOf(minutes), Integer.valueOf(seconds));
    }
}
