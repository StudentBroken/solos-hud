package com.facebook.internal;

import com.facebook.FacebookSdk;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes41.dex */
public class WorkQueue {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final int DEFAULT_MAX_CONCURRENT = 8;
    private final Executor executor;
    private final int maxConcurrent;
    private WorkNode pendingJobs;
    private int runningCount;
    private WorkNode runningJobs;
    private final Object workLock;

    public interface WorkItem {
        boolean cancel();

        boolean isRunning();

        void moveToFront();
    }

    static {
        $assertionsDisabled = !WorkQueue.class.desiredAssertionStatus();
    }

    public WorkQueue() {
        this(8);
    }

    public WorkQueue(int maxConcurrent) {
        this(maxConcurrent, FacebookSdk.getExecutor());
    }

    public WorkQueue(int maxConcurrent, Executor executor) {
        this.workLock = new Object();
        this.runningJobs = null;
        this.runningCount = 0;
        this.maxConcurrent = maxConcurrent;
        this.executor = executor;
    }

    public WorkItem addActiveWorkItem(Runnable callback) {
        return addActiveWorkItem(callback, true);
    }

    public WorkItem addActiveWorkItem(Runnable callback, boolean addToFront) {
        WorkNode node = new WorkNode(callback);
        synchronized (this.workLock) {
            this.pendingJobs = node.addToList(this.pendingJobs, addToFront);
        }
        startItem();
        return node;
    }

    public void validate() {
        synchronized (this.workLock) {
            int count = 0;
            if (this.runningJobs != null) {
                WorkNode walk = this.runningJobs;
                do {
                    walk.verify(true);
                    count++;
                    walk = walk.getNext();
                } while (walk != this.runningJobs);
            }
            if (!$assertionsDisabled && this.runningCount != count) {
                throw new AssertionError();
            }
        }
    }

    private void startItem() {
        finishItemAndStartNew(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishItemAndStartNew(WorkNode finished) {
        WorkNode ready = null;
        synchronized (this.workLock) {
            if (finished != null) {
                this.runningJobs = finished.removeFromList(this.runningJobs);
                this.runningCount--;
                if (this.runningCount < this.maxConcurrent && (ready = this.pendingJobs) != null) {
                    this.pendingJobs = ready.removeFromList(this.pendingJobs);
                    this.runningJobs = ready.addToList(this.runningJobs, false);
                    this.runningCount++;
                    ready.setIsRunning(true);
                }
            } else if (this.runningCount < this.maxConcurrent) {
                this.pendingJobs = ready.removeFromList(this.pendingJobs);
                this.runningJobs = ready.addToList(this.runningJobs, false);
                this.runningCount++;
                ready.setIsRunning(true);
            }
        }
        if (ready != null) {
            execute(ready);
        }
    }

    private void execute(final WorkNode node) {
        this.executor.execute(new Runnable() { // from class: com.facebook.internal.WorkQueue.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    node.getCallback().run();
                } finally {
                    WorkQueue.this.finishItemAndStartNew(node);
                }
            }
        });
    }

    private class WorkNode implements WorkItem {
        static final /* synthetic */ boolean $assertionsDisabled;
        private final Runnable callback;
        private boolean isRunning;
        private WorkNode next;
        private WorkNode prev;

        static {
            $assertionsDisabled = !WorkQueue.class.desiredAssertionStatus();
        }

        WorkNode(Runnable callback) {
            this.callback = callback;
        }

        @Override // com.facebook.internal.WorkQueue.WorkItem
        public boolean cancel() {
            synchronized (WorkQueue.this.workLock) {
                if (!isRunning()) {
                    WorkQueue.this.pendingJobs = removeFromList(WorkQueue.this.pendingJobs);
                    return true;
                }
                return false;
            }
        }

        @Override // com.facebook.internal.WorkQueue.WorkItem
        public void moveToFront() {
            synchronized (WorkQueue.this.workLock) {
                if (!isRunning()) {
                    WorkQueue.this.pendingJobs = removeFromList(WorkQueue.this.pendingJobs);
                    WorkQueue.this.pendingJobs = addToList(WorkQueue.this.pendingJobs, true);
                }
            }
        }

        @Override // com.facebook.internal.WorkQueue.WorkItem
        public boolean isRunning() {
            return this.isRunning;
        }

        Runnable getCallback() {
            return this.callback;
        }

        WorkNode getNext() {
            return this.next;
        }

        void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        WorkNode addToList(WorkNode list, boolean addToFront) {
            if (!$assertionsDisabled && this.next != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.prev != null) {
                throw new AssertionError();
            }
            if (list == null) {
                this.prev = this;
                this.next = this;
                list = this;
            } else {
                this.next = list;
                this.prev = list.prev;
                WorkNode workNode = this.next;
                this.prev.next = this;
                workNode.prev = this;
            }
            return addToFront ? this : list;
        }

        WorkNode removeFromList(WorkNode list) {
            if (!$assertionsDisabled && this.next == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.prev == null) {
                throw new AssertionError();
            }
            if (list == this) {
                if (this.next == this) {
                    list = null;
                } else {
                    list = this.next;
                }
            }
            this.next.prev = this.prev;
            this.prev.next = this.next;
            this.prev = null;
            this.next = null;
            return list;
        }

        void verify(boolean shouldBeRunning) {
            if (!$assertionsDisabled && this.prev.next != this) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.next.prev != this) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && isRunning() != shouldBeRunning) {
                throw new AssertionError();
            }
        }
    }
}
