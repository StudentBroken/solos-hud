package com.twitter.sdk.android.core.internal;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes62.dex */
public class AuthRequestQueue {
    private final SessionProvider sessionProvider;
    final Queue<Callback<Session>> queue = new ConcurrentLinkedQueue();
    final AtomicBoolean awaitingSession = new AtomicBoolean(true);

    public AuthRequestQueue(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public synchronized boolean addRequest(Callback<Session> callback) {
        boolean z = true;
        synchronized (this) {
            if (callback == null) {
                z = false;
            } else if (!this.awaitingSession.get()) {
                Session session = getValidSession();
                if (session != null) {
                    callback.success(new Result<>(session, null));
                } else {
                    this.queue.add(callback);
                    this.awaitingSession.set(true);
                    requestAuth();
                }
            } else {
                this.queue.add(callback);
            }
        }
        return z;
    }

    public synchronized void sessionRestored(Session session) {
        if (session != null) {
            flushQueueOnSuccess(session);
        } else if (this.queue.size() > 0) {
            requestAuth();
        } else {
            this.awaitingSession.set(false);
        }
    }

    void requestAuth() {
        this.sessionProvider.requestAuth(new Callback<Session>() { // from class: com.twitter.sdk.android.core.internal.AuthRequestQueue.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<Session> result) {
                AuthRequestQueue.this.flushQueueOnSuccess(result.data);
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                AuthRequestQueue.this.flushQueueOnError(exception);
            }
        });
    }

    synchronized void flushQueueOnSuccess(Session session) {
        this.awaitingSession.set(false);
        while (!this.queue.isEmpty()) {
            Callback<Session> request = this.queue.poll();
            request.success(new Result<>(session, null));
        }
    }

    synchronized void flushQueueOnError(TwitterException error) {
        this.awaitingSession.set(false);
        while (!this.queue.isEmpty()) {
            this.queue.poll().failure(error);
        }
    }

    Session getValidSession() {
        Session session = this.sessionProvider.getActiveSession();
        if (session == null || session.getAuthToken() == null || session.getAuthToken().isExpired()) {
            return null;
        }
        return session;
    }
}
