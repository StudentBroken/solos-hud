package bolts;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes23.dex */
public class CancellationTokenSource implements Closeable {
    private boolean cancellationRequested;
    private boolean closed;
    private ScheduledFuture<?> scheduledCancellation;
    private final Object lock = new Object();
    private final List<CancellationTokenRegistration> registrations = new ArrayList();
    private final ScheduledExecutorService executor = BoltsExecutors.scheduled();

    public boolean isCancellationRequested() {
        boolean z;
        synchronized (this.lock) {
            throwIfClosed();
            z = this.cancellationRequested;
        }
        return z;
    }

    public CancellationToken getToken() {
        CancellationToken cancellationToken;
        synchronized (this.lock) {
            throwIfClosed();
            cancellationToken = new CancellationToken(this);
        }
        return cancellationToken;
    }

    public void cancel() {
        synchronized (this.lock) {
            throwIfClosed();
            if (!this.cancellationRequested) {
                cancelScheduledCancellation();
                this.cancellationRequested = true;
                List<CancellationTokenRegistration> registrations = new ArrayList<>(this.registrations);
                notifyListeners(registrations);
            }
        }
    }

    public void cancelAfter(long delay) {
        cancelAfter(delay, TimeUnit.MILLISECONDS);
    }

    private void cancelAfter(long delay, TimeUnit timeUnit) {
        if (delay < -1) {
            throw new IllegalArgumentException("Delay must be >= -1");
        }
        if (delay == 0) {
            cancel();
            return;
        }
        synchronized (this.lock) {
            if (!this.cancellationRequested) {
                cancelScheduledCancellation();
                if (delay != -1) {
                    this.scheduledCancellation = this.executor.schedule(new Runnable() { // from class: bolts.CancellationTokenSource.1
                        @Override // java.lang.Runnable
                        public void run() {
                            synchronized (CancellationTokenSource.this.lock) {
                                CancellationTokenSource.this.scheduledCancellation = null;
                            }
                            CancellationTokenSource.this.cancel();
                        }
                    }, delay, timeUnit);
                }
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.lock) {
            if (!this.closed) {
                cancelScheduledCancellation();
                for (CancellationTokenRegistration registration : this.registrations) {
                    registration.close();
                }
                this.registrations.clear();
                this.closed = true;
            }
        }
    }

    CancellationTokenRegistration register(Runnable action) {
        CancellationTokenRegistration ctr;
        synchronized (this.lock) {
            throwIfClosed();
            ctr = new CancellationTokenRegistration(this, action);
            if (this.cancellationRequested) {
                ctr.runAction();
            } else {
                this.registrations.add(ctr);
            }
        }
        return ctr;
    }

    void throwIfCancellationRequested() throws CancellationException {
        synchronized (this.lock) {
            throwIfClosed();
            if (this.cancellationRequested) {
                throw new CancellationException();
            }
        }
    }

    void unregister(CancellationTokenRegistration registration) {
        synchronized (this.lock) {
            throwIfClosed();
            this.registrations.remove(registration);
        }
    }

    private void notifyListeners(List<CancellationTokenRegistration> registrations) {
        for (CancellationTokenRegistration registration : registrations) {
            registration.runAction();
        }
    }

    public String toString() {
        return String.format(Locale.US, "%s@%s[cancellationRequested=%s]", getClass().getName(), Integer.toHexString(hashCode()), Boolean.toString(isCancellationRequested()));
    }

    private void throwIfClosed() {
        if (this.closed) {
            throw new IllegalStateException("Object already closed");
        }
    }

    private void cancelScheduledCancellation() {
        if (this.scheduledCancellation != null) {
            this.scheduledCancellation.cancel(true);
            this.scheduledCancellation = null;
        }
    }
}
