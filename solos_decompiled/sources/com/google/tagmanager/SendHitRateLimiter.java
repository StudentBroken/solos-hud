package com.google.tagmanager;

import com.google.android.gms.common.util.VisibleForTesting;

/* JADX INFO: loaded from: classes49.dex */
class SendHitRateLimiter implements RateLimiter {
    private long mLastTrackTime;
    private final int mMaxTokens;
    private final long mMillisecondsPerToken;
    private final Object mTokenLock;
    private double mTokens;

    public SendHitRateLimiter(int maxTokenCount, long millisecondsPerToken) {
        this.mTokenLock = new Object();
        this.mMaxTokens = maxTokenCount;
        this.mTokens = this.mMaxTokens;
        this.mMillisecondsPerToken = millisecondsPerToken;
    }

    public SendHitRateLimiter() {
        this(60, 2000L);
    }

    @VisibleForTesting
    void setLastTrackTime(long lastTrackTime) {
        this.mLastTrackTime = lastTrackTime;
    }

    @VisibleForTesting
    void setTokensAvailable(long tokens) {
        this.mTokens = tokens;
    }

    @Override // com.google.tagmanager.RateLimiter
    public boolean tokenAvailable() {
        boolean z;
        synchronized (this.mTokenLock) {
            long timeNow = System.currentTimeMillis();
            if (this.mTokens < this.mMaxTokens) {
                long timeElapsed = timeNow - this.mLastTrackTime;
                double tokensDue = timeElapsed / this.mMillisecondsPerToken;
                if (tokensDue > 0.0d) {
                    this.mTokens = Math.min(this.mMaxTokens, this.mTokens + tokensDue);
                }
            }
            this.mLastTrackTime = timeNow;
            if (this.mTokens >= 1.0d) {
                this.mTokens -= 1.0d;
                z = true;
            } else {
                Log.w("No more tokens available.");
                z = false;
            }
        }
        return z;
    }
}
