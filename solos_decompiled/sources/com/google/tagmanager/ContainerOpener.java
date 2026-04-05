package com.google.tagmanager;

import com.google.tagmanager.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/* JADX INFO: loaded from: classes49.dex */
public class ContainerOpener {
    public static final long DEFAULT_TIMEOUT_IN_MILLIS = 2000;
    private static final Map<String, List<Notifier>> mContainerIdNotifiersMap = new HashMap();
    private Clock mClock = new Clock() { // from class: com.google.tagmanager.ContainerOpener.1
        @Override // com.google.tagmanager.Clock
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    };
    private volatile Container mContainer;
    private final String mContainerId;
    private boolean mHaveNotified;
    private Notifier mNotifier;
    private final TagManager mTagManager;
    private final long mTimeoutInMillis;

    public interface ContainerFuture {
        Container get();

        boolean isDone();
    }

    public interface Notifier {
        void containerAvailable(Container container);
    }

    public enum OpenType {
        PREFER_NON_DEFAULT,
        PREFER_FRESH
    }

    private class WaitForNonDefaultRefresh implements Container.Callback {
        public WaitForNonDefaultRefresh() {
        }

        @Override // com.google.tagmanager.Container.Callback
        public void containerRefreshBegin(Container container, Container.RefreshType refreshType) {
        }

        @Override // com.google.tagmanager.Container.Callback
        public void containerRefreshSuccess(Container container, Container.RefreshType refreshType) {
            ContainerOpener.this.callNotifiers(container);
        }

        @Override // com.google.tagmanager.Container.Callback
        public void containerRefreshFailure(Container container, Container.RefreshType refreshType, Container.RefreshFailure refreshFailure) {
            if (refreshType == Container.RefreshType.NETWORK) {
                ContainerOpener.this.callNotifiers(container);
            }
        }
    }

    private class WaitForFresh implements Container.Callback {
        private final long mOldestTimeToBeFresh;

        public WaitForFresh(long oldestTimeToBeFresh) {
            this.mOldestTimeToBeFresh = oldestTimeToBeFresh;
        }

        @Override // com.google.tagmanager.Container.Callback
        public void containerRefreshBegin(Container container, Container.RefreshType refreshType) {
        }

        @Override // com.google.tagmanager.Container.Callback
        public void containerRefreshSuccess(Container container, Container.RefreshType refreshType) {
            if (refreshType == Container.RefreshType.NETWORK || isFresh()) {
                ContainerOpener.this.callNotifiers(container);
            }
        }

        @Override // com.google.tagmanager.Container.Callback
        public void containerRefreshFailure(Container container, Container.RefreshType refreshType, Container.RefreshFailure refreshFailure) {
            if (refreshType == Container.RefreshType.NETWORK) {
                ContainerOpener.this.callNotifiers(container);
            }
        }

        private boolean isFresh() {
            return this.mOldestTimeToBeFresh < ContainerOpener.this.mContainer.getLastRefreshTime();
        }
    }

    private ContainerOpener(TagManager tagManager, String containerId, Long timeoutInMillis, Notifier notifier) {
        this.mTagManager = tagManager;
        this.mContainerId = containerId;
        this.mTimeoutInMillis = timeoutInMillis != null ? Math.max(1L, timeoutInMillis.longValue()) : 2000L;
        this.mNotifier = notifier;
    }

    public static void openContainer(TagManager tagManager, String containerId, OpenType openType, Long timeoutInMillis, Notifier notifier) {
        if (tagManager == null) {
            throw new NullPointerException("TagManager cannot be null.");
        }
        if (containerId == null) {
            throw new NullPointerException("ContainerId cannot be null.");
        }
        if (openType == null) {
            throw new NullPointerException("OpenType cannot be null.");
        }
        if (notifier == null) {
            throw new NullPointerException("Notifier cannot be null.");
        }
        ContainerOpener containerLoader = new ContainerOpener(tagManager, containerId, timeoutInMillis, notifier);
        containerLoader.open(openType == OpenType.PREFER_FRESH ? Container.RefreshType.NETWORK : Container.RefreshType.SAVED);
    }

    public static ContainerFuture openContainer(TagManager tagManager, String containerId, OpenType openType, Long timeoutInMillis) {
        final ContainerFutureImpl future = new ContainerFutureImpl();
        openContainer(tagManager, containerId, openType, timeoutInMillis, new Notifier() { // from class: com.google.tagmanager.ContainerOpener.2
            @Override // com.google.tagmanager.ContainerOpener.Notifier
            public void containerAvailable(Container container) {
                future.setContainer(container);
            }
        });
        return future;
    }

    private static class ContainerFutureImpl implements ContainerFuture {
        private volatile Container mContainer;
        private Semaphore mContainerIsReady;
        private volatile boolean mHaveGotten;

        private ContainerFutureImpl() {
            this.mContainerIsReady = new Semaphore(0);
        }

        @Override // com.google.tagmanager.ContainerOpener.ContainerFuture
        public Container get() {
            if (this.mHaveGotten) {
                return this.mContainer;
            }
            try {
                this.mContainerIsReady.acquire();
            } catch (InterruptedException e) {
            }
            this.mHaveGotten = true;
            return this.mContainer;
        }

        public void setContainer(Container container) {
            this.mContainer = container;
            this.mContainerIsReady.release();
        }

        @Override // com.google.tagmanager.ContainerOpener.ContainerFuture
        public boolean isDone() {
            return this.mHaveGotten || this.mContainerIsReady.availablePermits() > 0;
        }
    }

    private void open(Container.RefreshType refreshType) {
        long loadStartTime = this.mClock.currentTimeMillis();
        boolean callNotifierImmediately = false;
        synchronized (ContainerOpener.class) {
            this.mContainer = this.mTagManager.getContainer(this.mContainerId);
            if (this.mContainer == null) {
                List<Notifier> notifiers = new ArrayList<>();
                notifiers.add(this.mNotifier);
                this.mNotifier = null;
                mContainerIdNotifiersMap.put(this.mContainerId, notifiers);
                this.mContainer = this.mTagManager.openContainer(this.mContainerId, refreshType == Container.RefreshType.SAVED ? new WaitForNonDefaultRefresh() : new WaitForFresh(loadStartTime - 43200000));
            } else {
                List<Notifier> notifiers2 = mContainerIdNotifiersMap.get(this.mContainerId);
                if (notifiers2 == null) {
                    callNotifierImmediately = true;
                } else {
                    notifiers2.add(this.mNotifier);
                    this.mNotifier = null;
                    return;
                }
            }
            if (callNotifierImmediately) {
                this.mNotifier.containerAvailable(this.mContainer);
                this.mNotifier = null;
            } else {
                long remainingTimeout = this.mTimeoutInMillis - (this.mClock.currentTimeMillis() - loadStartTime);
                setTimer(Math.max(1L, remainingTimeout));
            }
        }
    }

    private void setTimer(long timeoutInMillis) {
        TimerTask timerExpires = new TimerTask() { // from class: com.google.tagmanager.ContainerOpener.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Log.i("Timer expired.");
                ContainerOpener.this.callNotifiers(ContainerOpener.this.mContainer);
            }
        };
        new Timer("ContainerOpener").schedule(timerExpires, timeoutInMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void callNotifiers(Container container) {
        List<Notifier> notifiers;
        if (!this.mHaveNotified) {
            synchronized (ContainerOpener.class) {
                notifiers = mContainerIdNotifiersMap.remove(this.mContainerId);
            }
            if (notifiers != null) {
                for (Notifier notifier : notifiers) {
                    notifier.containerAvailable(container);
                }
            }
            this.mHaveNotified = true;
        }
    }
}
