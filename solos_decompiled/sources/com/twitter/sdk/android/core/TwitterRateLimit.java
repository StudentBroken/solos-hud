package com.twitter.sdk.android.core;

import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import java.util.List;
import retrofit.client.Header;

/* JADX INFO: loaded from: classes62.dex */
class TwitterRateLimit {
    private static final String LIMIT_KEY = "x-rate-limit-limit";
    private static final String REMAINING_KEY = "x-rate-limit-remaining";
    private static final String RESET_KEY = "x-rate-limit-reset";
    private final long epochSeconds;
    private int remainingRequest;
    private int requestLimit;
    private long resetSeconds;

    TwitterRateLimit(List<Header> headers) {
        this(headers, new SystemCurrentTimeProvider());
    }

    TwitterRateLimit(List<Header> headers, CurrentTimeProvider timeProvider) {
        if (headers == null) {
            throw new IllegalArgumentException("headers must not be null");
        }
        this.epochSeconds = timeProvider.getCurrentTimeMillis() / 1000;
        for (Header header : headers) {
            if (LIMIT_KEY.equals(header.getName())) {
                this.requestLimit = Integer.valueOf(header.getValue()).intValue();
            } else if (REMAINING_KEY.equals(header.getName())) {
                this.remainingRequest = Integer.valueOf(header.getValue()).intValue();
            } else if (RESET_KEY.equals(header.getName())) {
                this.resetSeconds = Long.valueOf(header.getValue()).longValue();
            }
        }
    }

    public int getLimit() {
        return this.requestLimit;
    }

    public int getRemaining() {
        return this.remainingRequest;
    }

    public long getReset() {
        return this.resetSeconds;
    }

    public long getRequestedTime() {
        return this.epochSeconds;
    }

    public long getRemainingTime() {
        if (this.epochSeconds > this.resetSeconds) {
            return 0L;
        }
        return this.resetSeconds - this.epochSeconds;
    }
}
