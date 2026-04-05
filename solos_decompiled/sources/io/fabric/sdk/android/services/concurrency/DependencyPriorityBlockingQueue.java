package io.fabric.sdk.android.services.concurrency;

import io.fabric.sdk.android.services.concurrency.Dependency;
import io.fabric.sdk.android.services.concurrency.PriorityProvider;
import io.fabric.sdk.android.services.concurrency.Task;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes66.dex */
public class DependencyPriorityBlockingQueue<E extends Dependency & Task & PriorityProvider> extends PriorityBlockingQueue<E> {
    static final int PEEK = 1;
    static final int POLL = 2;
    static final int POLL_WITH_TIMEOUT = 3;
    static final int TAKE = 0;
    final Queue<E> blockedQueue = new LinkedList();
    private final ReentrantLock lock = new ReentrantLock();

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.concurrent.BlockingQueue
    public E take() throws InterruptedException {
        return (E) get(0, null, null);
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.Queue
    public E peek() {
        try {
            return (E) get(1, null, null);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.concurrent.BlockingQueue
    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        return (E) get(3, Long.valueOf(j), timeUnit);
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.Queue
    public E poll() {
        try {
            return (E) get(2, null, null);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.AbstractCollection, java.util.Collection
    public int size() {
        try {
            this.lock.lock();
            return this.blockedQueue.size() + super.size();
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        try {
            this.lock.lock();
            return (T[]) concatenate(super.toArray(tArr), this.blockedQueue.toArray(tArr));
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        try {
            this.lock.lock();
            return concatenate(super.toArray(), this.blockedQueue.toArray());
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection) {
        try {
            this.lock.lock();
            int iDrainTo = super.drainTo(collection) + this.blockedQueue.size();
            while (!this.blockedQueue.isEmpty()) {
                collection.add(this.blockedQueue.poll());
            }
            return iDrainTo;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection, int i) {
        try {
            this.lock.lock();
            int iDrainTo = super.drainTo(collection, i);
            while (!this.blockedQueue.isEmpty() && iDrainTo <= i) {
                collection.add(this.blockedQueue.poll());
                iDrainTo++;
            }
            return iDrainTo;
        } finally {
            this.lock.unlock();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0013  */
    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.AbstractCollection, java.util.Collection, java.util.concurrent.BlockingQueue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean contains(java.lang.Object r3) {
        /*
            r2 = this;
            java.util.concurrent.locks.ReentrantLock r0 = r2.lock     // Catch: java.lang.Throwable -> L1c
            r0.lock()     // Catch: java.lang.Throwable -> L1c
            boolean r0 = super.contains(r3)     // Catch: java.lang.Throwable -> L1c
            if (r0 != 0) goto L13
            java.util.Queue<E extends io.fabric.sdk.android.services.concurrency.Dependency & io.fabric.sdk.android.services.concurrency.Task & io.fabric.sdk.android.services.concurrency.PriorityProvider> r0 = r2.blockedQueue     // Catch: java.lang.Throwable -> L1c
            boolean r0 = r0.contains(r3)     // Catch: java.lang.Throwable -> L1c
            if (r0 == 0) goto L1a
        L13:
            r0 = 1
        L14:
            java.util.concurrent.locks.ReentrantLock r1 = r2.lock
            r1.unlock()
            return r0
        L1a:
            r0 = 0
            goto L14
        L1c:
            r0 = move-exception
            java.util.concurrent.locks.ReentrantLock r1 = r2.lock
            r1.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.fabric.sdk.android.services.concurrency.DependencyPriorityBlockingQueue.contains(java.lang.Object):boolean");
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        try {
            this.lock.lock();
            this.blockedQueue.clear();
            super.clear();
        } finally {
            this.lock.unlock();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0013  */
    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.AbstractCollection, java.util.Collection, java.util.concurrent.BlockingQueue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean remove(java.lang.Object r3) {
        /*
            r2 = this;
            java.util.concurrent.locks.ReentrantLock r0 = r2.lock     // Catch: java.lang.Throwable -> L1c
            r0.lock()     // Catch: java.lang.Throwable -> L1c
            boolean r0 = super.remove(r3)     // Catch: java.lang.Throwable -> L1c
            if (r0 != 0) goto L13
            java.util.Queue<E extends io.fabric.sdk.android.services.concurrency.Dependency & io.fabric.sdk.android.services.concurrency.Task & io.fabric.sdk.android.services.concurrency.PriorityProvider> r0 = r2.blockedQueue     // Catch: java.lang.Throwable -> L1c
            boolean r0 = r0.remove(r3)     // Catch: java.lang.Throwable -> L1c
            if (r0 == 0) goto L1a
        L13:
            r0 = 1
        L14:
            java.util.concurrent.locks.ReentrantLock r1 = r2.lock
            r1.unlock()
            return r0
        L1a:
            r0 = 0
            goto L14
        L1c:
            r0 = move-exception
            java.util.concurrent.locks.ReentrantLock r1 = r2.lock
            r1.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.fabric.sdk.android.services.concurrency.DependencyPriorityBlockingQueue.remove(java.lang.Object):boolean");
    }

    @Override // java.util.concurrent.PriorityBlockingQueue, java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        try {
            this.lock.lock();
            return super.removeAll(collection) | this.blockedQueue.removeAll(collection);
        } finally {
            this.lock.unlock();
        }
    }

    E performOperation(int i, Long l, TimeUnit timeUnit) throws InterruptedException {
        switch (i) {
            case 0:
                return (E) ((Dependency) super.take());
            case 1:
                return (E) ((Dependency) super.peek());
            case 2:
                return (E) ((Dependency) super.poll());
            case 3:
                return (E) ((Dependency) super.poll(l.longValue(), timeUnit));
            default:
                return null;
        }
    }

    boolean offerBlockedResult(int operation, E result) {
        try {
            this.lock.lock();
            if (operation == 1) {
                super.remove(result);
            }
            return this.blockedQueue.offer(result);
        } finally {
            this.lock.unlock();
        }
    }

    E get(int i, Long l, TimeUnit timeUnit) throws InterruptedException {
        E e;
        while (true) {
            e = (E) performOperation(i, l, timeUnit);
            if (e == null || canProcess(e)) {
                break;
            }
            offerBlockedResult(i, e);
        }
        return e;
    }

    boolean canProcess(E result) {
        return result.areDependenciesMet();
    }

    public void recycleBlockedQueue() {
        try {
            this.lock.lock();
            Iterator<E> iterator = this.blockedQueue.iterator();
            while (iterator.hasNext()) {
                E blockedItem = iterator.next();
                if (canProcess(blockedItem)) {
                    super.offer(blockedItem);
                    iterator.remove();
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    <T> T[] concatenate(T[] tArr, T[] tArr2) {
        int length = tArr.length;
        int length2 = tArr2.length;
        T[] tArr3 = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), length + length2));
        System.arraycopy(tArr, 0, tArr3, 0, length);
        System.arraycopy(tArr2, 0, tArr3, length, length2);
        return tArr3;
    }
}
