package bolts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes23.dex */
public class Task<TResult> {
    private static volatile UnobservedExceptionHandler unobservedExceptionHandler;
    private boolean cancelled;
    private boolean complete;
    private Exception error;
    private boolean errorHasBeenObserved;
    private TResult result;
    private UnobservedErrorNotifier unobservedErrorNotifier;
    public static final ExecutorService BACKGROUND_EXECUTOR = BoltsExecutors.background();
    private static final Executor IMMEDIATE_EXECUTOR = BoltsExecutors.immediate();
    public static final Executor UI_THREAD_EXECUTOR = AndroidExecutors.uiThread();
    private static Task<?> TASK_NULL = new Task<>((Object) null);
    private static Task<Boolean> TASK_TRUE = new Task<>(true);
    private static Task<Boolean> TASK_FALSE = new Task<>(false);
    private static Task<?> TASK_CANCELLED = new Task<>(true);
    private final Object lock = new Object();
    private List<Continuation<TResult, Void>> continuations = new ArrayList();

    public interface UnobservedExceptionHandler {
        void unobservedException(Task<?> task, UnobservedTaskException unobservedTaskException);
    }

    public static UnobservedExceptionHandler getUnobservedExceptionHandler() {
        return unobservedExceptionHandler;
    }

    public static void setUnobservedExceptionHandler(UnobservedExceptionHandler eh) {
        unobservedExceptionHandler = eh;
    }

    Task() {
    }

    private Task(TResult result) {
        trySetResult(result);
    }

    private Task(boolean cancelled) {
        if (cancelled) {
            trySetCancelled();
        } else {
            trySetResult(null);
        }
    }

    public static <TResult> Task<TResult>.TaskCompletionSource create() {
        Task<TResult> task = new Task<>();
        task.getClass();
        return task.new TaskCompletionSource();
    }

    public boolean isCompleted() {
        boolean z;
        synchronized (this.lock) {
            z = this.complete;
        }
        return z;
    }

    public boolean isCancelled() {
        boolean z;
        synchronized (this.lock) {
            z = this.cancelled;
        }
        return z;
    }

    public boolean isFaulted() {
        boolean z;
        synchronized (this.lock) {
            z = getError() != null;
        }
        return z;
    }

    public TResult getResult() {
        TResult tresult;
        synchronized (this.lock) {
            tresult = this.result;
        }
        return tresult;
    }

    public Exception getError() {
        Exception exc;
        synchronized (this.lock) {
            if (this.error != null) {
                this.errorHasBeenObserved = true;
                if (this.unobservedErrorNotifier != null) {
                    this.unobservedErrorNotifier.setObserved();
                    this.unobservedErrorNotifier = null;
                }
            }
            exc = this.error;
        }
        return exc;
    }

    public void waitForCompletion() throws InterruptedException {
        synchronized (this.lock) {
            if (!isCompleted()) {
                this.lock.wait();
            }
        }
    }

    public boolean waitForCompletion(long duration, TimeUnit timeUnit) throws InterruptedException {
        boolean zIsCompleted;
        synchronized (this.lock) {
            if (!isCompleted()) {
                this.lock.wait(timeUnit.toMillis(duration));
            }
            zIsCompleted = isCompleted();
        }
        return zIsCompleted;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <TResult> Task<TResult> forResult(TResult tresult) {
        if (tresult == 0) {
            return (Task<TResult>) TASK_NULL;
        }
        if (tresult instanceof Boolean) {
            return ((Boolean) tresult).booleanValue() ? (Task<TResult>) TASK_TRUE : (Task<TResult>) TASK_FALSE;
        }
        bolts.TaskCompletionSource taskCompletionSource = new bolts.TaskCompletionSource();
        taskCompletionSource.setResult(tresult);
        return taskCompletionSource.getTask();
    }

    public static <TResult> Task<TResult> forError(Exception error) {
        bolts.TaskCompletionSource<TResult> tcs = new bolts.TaskCompletionSource<>();
        tcs.setError(error);
        return tcs.getTask();
    }

    public static <TResult> Task<TResult> cancelled() {
        return (Task<TResult>) TASK_CANCELLED;
    }

    public static Task<Void> delay(long delay) {
        return delay(delay, BoltsExecutors.scheduled(), null);
    }

    public static Task<Void> delay(long delay, CancellationToken cancellationToken) {
        return delay(delay, BoltsExecutors.scheduled(), cancellationToken);
    }

    static Task<Void> delay(long delay, ScheduledExecutorService executor, CancellationToken cancellationToken) {
        if (cancellationToken != null && cancellationToken.isCancellationRequested()) {
            return cancelled();
        }
        if (delay <= 0) {
            return forResult(null);
        }
        final bolts.TaskCompletionSource<Void> tcs = new bolts.TaskCompletionSource<>();
        final ScheduledFuture<?> scheduled = executor.schedule(new Runnable() { // from class: bolts.Task.1
            @Override // java.lang.Runnable
            public void run() {
                tcs.trySetResult(null);
            }
        }, delay, TimeUnit.MILLISECONDS);
        if (cancellationToken != null) {
            cancellationToken.register(new Runnable() { // from class: bolts.Task.2
                @Override // java.lang.Runnable
                public void run() {
                    scheduled.cancel(true);
                    tcs.trySetCancelled();
                }
            });
        }
        return tcs.getTask();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <TOut> Task<TOut> cast() {
        return this;
    }

    public Task<Void> makeVoid() {
        return continueWithTask(new Continuation<TResult, Task<Void>>() { // from class: bolts.Task.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // bolts.Continuation
            public Task<Void> then(Task<TResult> task) throws Exception {
                if (task.isCancelled()) {
                    return Task.cancelled();
                }
                if (task.isFaulted()) {
                    return Task.forError(task.getError());
                }
                return Task.forResult(null);
            }
        });
    }

    public static <TResult> Task<TResult> callInBackground(Callable<TResult> callable) {
        return call(callable, BACKGROUND_EXECUTOR, null);
    }

    public static <TResult> Task<TResult> callInBackground(Callable<TResult> callable, CancellationToken ct) {
        return call(callable, BACKGROUND_EXECUTOR, ct);
    }

    public static <TResult> Task<TResult> call(Callable<TResult> callable, Executor executor) {
        return call(callable, executor, null);
    }

    public static <TResult> Task<TResult> call(final Callable<TResult> callable, Executor executor, final CancellationToken ct) {
        final bolts.TaskCompletionSource<TResult> tcs = new bolts.TaskCompletionSource<>();
        try {
            executor.execute(new Runnable() { // from class: bolts.Task.4
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    if (ct != null && ct.isCancellationRequested()) {
                        tcs.setCancelled();
                        return;
                    }
                    try {
                        tcs.setResult(callable.call());
                    } catch (CancellationException e) {
                        tcs.setCancelled();
                    } catch (Exception e2) {
                        tcs.setError(e2);
                    }
                }
            });
        } catch (Exception e) {
            tcs.setError(new ExecutorException(e));
        }
        return tcs.getTask();
    }

    public static <TResult> Task<TResult> call(Callable<TResult> callable) {
        return call(callable, IMMEDIATE_EXECUTOR, null);
    }

    public static <TResult> Task<TResult> call(Callable<TResult> callable, CancellationToken ct) {
        return call(callable, IMMEDIATE_EXECUTOR, ct);
    }

    public static <TResult> Task<Task<TResult>> whenAnyResult(Collection<? extends Task<TResult>> collection) {
        if (collection.size() == 0) {
            return forResult(null);
        }
        final bolts.TaskCompletionSource taskCompletionSource = new bolts.TaskCompletionSource();
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Iterator<? extends Task<TResult>> it = collection.iterator();
        while (it.hasNext()) {
            it.next().continueWith(new Continuation<TResult, Void>() { // from class: bolts.Task.5
                @Override // bolts.Continuation
                public Void then(Task<TResult> task) {
                    if (atomicBoolean.compareAndSet(false, true)) {
                        taskCompletionSource.setResult(task);
                        return null;
                    }
                    task.getError();
                    return null;
                }
            });
        }
        return taskCompletionSource.getTask();
    }

    public static Task<Task<?>> whenAny(Collection<? extends Task<?>> tasks) {
        if (tasks.size() == 0) {
            return forResult(null);
        }
        final bolts.TaskCompletionSource<Task<?>> firstCompleted = new bolts.TaskCompletionSource<>();
        final AtomicBoolean isAnyTaskComplete = new AtomicBoolean(false);
        for (Task<?> task : tasks) {
            task.continueWith(new Continuation<Object, Void>() { // from class: bolts.Task.6
                @Override // bolts.Continuation
                public Void then(Task<Object> task2) {
                    if (isAnyTaskComplete.compareAndSet(false, true)) {
                        firstCompleted.setResult(task2);
                        return null;
                    }
                    task2.getError();
                    return null;
                }
            });
        }
        return firstCompleted.getTask();
    }

    public static <TResult> Task<List<TResult>> whenAllResult(final Collection<? extends Task<TResult>> collection) {
        return (Task<List<TResult>>) whenAll(collection).onSuccess(new Continuation<Void, List<TResult>>() { // from class: bolts.Task.7
            @Override // bolts.Continuation
            public List<TResult> then(Task<Void> task) throws Exception {
                if (collection.size() == 0) {
                    return Collections.emptyList();
                }
                ArrayList arrayList = new ArrayList();
                for (Task<TResult> individualTask : collection) {
                    arrayList.add(individualTask.getResult());
                }
                return arrayList;
            }
        });
    }

    public static Task<Void> whenAll(Collection<? extends Task<?>> tasks) {
        if (tasks.size() == 0) {
            return forResult(null);
        }
        final bolts.TaskCompletionSource<Void> allFinished = new bolts.TaskCompletionSource<>();
        final ArrayList<Exception> causes = new ArrayList<>();
        final Object errorLock = new Object();
        final AtomicInteger count = new AtomicInteger(tasks.size());
        final AtomicBoolean isCancelled = new AtomicBoolean(false);
        for (Task<?> task : tasks) {
            task.continueWith(new Continuation<Object, Void>() { // from class: bolts.Task.8
                @Override // bolts.Continuation
                public Void then(Task<Object> task2) {
                    if (task2.isFaulted()) {
                        synchronized (errorLock) {
                            causes.add(task2.getError());
                        }
                    }
                    if (task2.isCancelled()) {
                        isCancelled.set(true);
                    }
                    if (count.decrementAndGet() == 0) {
                        if (causes.size() != 0) {
                            if (causes.size() == 1) {
                                allFinished.setError((Exception) causes.get(0));
                            } else {
                                Exception error = new AggregateException(String.format("There were %d exceptions.", Integer.valueOf(causes.size())), causes);
                                allFinished.setError(error);
                            }
                        } else if (isCancelled.get()) {
                            allFinished.setCancelled();
                        } else {
                            allFinished.setResult(null);
                        }
                    }
                    return null;
                }
            });
        }
        return allFinished.getTask();
    }

    public Task<Void> continueWhile(Callable<Boolean> predicate, Continuation<Void, Task<Void>> continuation) {
        return continueWhile(predicate, continuation, IMMEDIATE_EXECUTOR, null);
    }

    public Task<Void> continueWhile(Callable<Boolean> predicate, Continuation<Void, Task<Void>> continuation, CancellationToken ct) {
        return continueWhile(predicate, continuation, IMMEDIATE_EXECUTOR, ct);
    }

    public Task<Void> continueWhile(Callable<Boolean> predicate, Continuation<Void, Task<Void>> continuation, Executor executor) {
        return continueWhile(predicate, continuation, executor, null);
    }

    public Task<Void> continueWhile(final Callable<Boolean> predicate, final Continuation<Void, Task<Void>> continuation, final Executor executor, final CancellationToken ct) {
        final Capture<Continuation<Void, Task<Void>>> predicateContinuation = new Capture<>();
        predicateContinuation.set(new Continuation<Void, Task<Void>>() { // from class: bolts.Task.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // bolts.Continuation
            public Task<Void> then(Task<Void> task) throws Exception {
                if (ct != null && ct.isCancellationRequested()) {
                    return Task.cancelled();
                }
                if (((Boolean) predicate.call()).booleanValue()) {
                    return Task.forResult(null).onSuccessTask(continuation, executor).onSuccessTask((Continuation) predicateContinuation.get(), executor);
                }
                return Task.forResult(null);
            }
        });
        return makeVoid().continueWithTask((Continuation<Void, Task<TContinuationResult>>) predicateContinuation.get(), executor);
    }

    public <TContinuationResult> Task<TContinuationResult> continueWith(Continuation<TResult, TContinuationResult> continuation, Executor executor) {
        return continueWith(continuation, executor, null);
    }

    public <TContinuationResult> Task<TContinuationResult> continueWith(final Continuation<TResult, TContinuationResult> continuation, final Executor executor, final CancellationToken ct) {
        boolean completed;
        final bolts.TaskCompletionSource<TContinuationResult> tcs = new bolts.TaskCompletionSource<>();
        synchronized (this.lock) {
            completed = isCompleted();
            if (!completed) {
                this.continuations.add(new Continuation<TResult, Void>() { // from class: bolts.Task.10
                    @Override // bolts.Continuation
                    public Void then(Task<TResult> task) {
                        Task.completeImmediately(tcs, continuation, task, executor, ct);
                        return null;
                    }
                });
            }
        }
        if (completed) {
            completeImmediately(tcs, continuation, this, executor, ct);
        }
        return tcs.getTask();
    }

    public <TContinuationResult> Task<TContinuationResult> continueWith(Continuation<TResult, TContinuationResult> continuation) {
        return continueWith(continuation, IMMEDIATE_EXECUTOR, null);
    }

    public <TContinuationResult> Task<TContinuationResult> continueWith(Continuation<TResult, TContinuationResult> continuation, CancellationToken ct) {
        return continueWith(continuation, IMMEDIATE_EXECUTOR, ct);
    }

    public <TContinuationResult> Task<TContinuationResult> continueWithTask(Continuation<TResult, Task<TContinuationResult>> continuation, Executor executor) {
        return continueWithTask(continuation, executor, null);
    }

    public <TContinuationResult> Task<TContinuationResult> continueWithTask(final Continuation<TResult, Task<TContinuationResult>> continuation, final Executor executor, final CancellationToken ct) {
        boolean completed;
        final bolts.TaskCompletionSource<TContinuationResult> tcs = new bolts.TaskCompletionSource<>();
        synchronized (this.lock) {
            completed = isCompleted();
            if (!completed) {
                this.continuations.add(new Continuation<TResult, Void>() { // from class: bolts.Task.11
                    @Override // bolts.Continuation
                    public Void then(Task<TResult> task) {
                        Task.completeAfterTask(tcs, continuation, task, executor, ct);
                        return null;
                    }
                });
            }
        }
        if (completed) {
            completeAfterTask(tcs, continuation, this, executor, ct);
        }
        return tcs.getTask();
    }

    public <TContinuationResult> Task<TContinuationResult> continueWithTask(Continuation<TResult, Task<TContinuationResult>> continuation) {
        return continueWithTask(continuation, IMMEDIATE_EXECUTOR, null);
    }

    public <TContinuationResult> Task<TContinuationResult> continueWithTask(Continuation<TResult, Task<TContinuationResult>> continuation, CancellationToken ct) {
        return continueWithTask(continuation, IMMEDIATE_EXECUTOR, ct);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccess(Continuation<TResult, TContinuationResult> continuation, Executor executor) {
        return onSuccess(continuation, executor, null);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccess(final Continuation<TResult, TContinuationResult> continuation, Executor executor, final CancellationToken ct) {
        return continueWithTask(new Continuation<TResult, Task<TContinuationResult>>() { // from class: bolts.Task.12
            @Override // bolts.Continuation
            public Task<TContinuationResult> then(Task<TResult> task) {
                if (ct != null && ct.isCancellationRequested()) {
                    return Task.cancelled();
                }
                if (task.isFaulted()) {
                    return Task.forError(task.getError());
                }
                if (task.isCancelled()) {
                    return Task.cancelled();
                }
                return task.continueWith(continuation);
            }
        }, executor);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccess(Continuation<TResult, TContinuationResult> continuation) {
        return onSuccess(continuation, IMMEDIATE_EXECUTOR, null);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccess(Continuation<TResult, TContinuationResult> continuation, CancellationToken ct) {
        return onSuccess(continuation, IMMEDIATE_EXECUTOR, ct);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccessTask(Continuation<TResult, Task<TContinuationResult>> continuation, Executor executor) {
        return onSuccessTask(continuation, executor, null);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccessTask(final Continuation<TResult, Task<TContinuationResult>> continuation, Executor executor, final CancellationToken ct) {
        return continueWithTask(new Continuation<TResult, Task<TContinuationResult>>() { // from class: bolts.Task.13
            @Override // bolts.Continuation
            public Task<TContinuationResult> then(Task<TResult> task) {
                if (ct != null && ct.isCancellationRequested()) {
                    return Task.cancelled();
                }
                if (task.isFaulted()) {
                    return Task.forError(task.getError());
                }
                if (task.isCancelled()) {
                    return Task.cancelled();
                }
                return task.continueWithTask(continuation);
            }
        }, executor);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccessTask(Continuation<TResult, Task<TContinuationResult>> continuation) {
        return onSuccessTask(continuation, IMMEDIATE_EXECUTOR);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccessTask(Continuation<TResult, Task<TContinuationResult>> continuation, CancellationToken ct) {
        return onSuccessTask(continuation, IMMEDIATE_EXECUTOR, ct);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <TContinuationResult, TResult> void completeImmediately(final bolts.TaskCompletionSource<TContinuationResult> tcs, final Continuation<TResult, TContinuationResult> continuation, final Task<TResult> task, Executor executor, final CancellationToken ct) {
        try {
            executor.execute(new Runnable() { // from class: bolts.Task.14
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    if (ct != null && ct.isCancellationRequested()) {
                        tcs.setCancelled();
                        return;
                    }
                    try {
                        tcs.setResult(continuation.then(task));
                    } catch (CancellationException e) {
                        tcs.setCancelled();
                    } catch (Exception e2) {
                        tcs.setError(e2);
                    }
                }
            });
        } catch (Exception e) {
            tcs.setError(new ExecutorException(e));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <TContinuationResult, TResult> void completeAfterTask(final bolts.TaskCompletionSource<TContinuationResult> tcs, final Continuation<TResult, Task<TContinuationResult>> continuation, final Task<TResult> task, Executor executor, final CancellationToken ct) {
        try {
            executor.execute(new Runnable() { // from class: bolts.Task.15
                /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x0026 -> B:18:0x0011). Please report as a decompilation issue!!! */
                /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x0036 -> B:18:0x0011). Please report as a decompilation issue!!! */
                @Override // java.lang.Runnable
                public void run() {
                    if (ct != null && ct.isCancellationRequested()) {
                        tcs.setCancelled();
                        return;
                    }
                    try {
                        Task task2 = (Task) continuation.then(task);
                        if (task2 == null) {
                            tcs.setResult(null);
                        } else {
                            task2.continueWith(new Continuation<TContinuationResult, Void>() { // from class: bolts.Task.15.1
                                @Override // bolts.Continuation
                                public Void then(Task<TContinuationResult> task3) {
                                    if ((ct != null && ct.isCancellationRequested()) || task3.isCancelled()) {
                                        tcs.setCancelled();
                                    } else if (task3.isFaulted()) {
                                        tcs.setError(task3.getError());
                                    } else {
                                        tcs.setResult(task3.getResult());
                                    }
                                    return null;
                                }
                            });
                        }
                    } catch (CancellationException e) {
                        tcs.setCancelled();
                    } catch (Exception e2) {
                        tcs.setError(e2);
                    }
                }
            });
        } catch (Exception e) {
            tcs.setError(new ExecutorException(e));
        }
    }

    private void runContinuations() {
        synchronized (this.lock) {
            for (Continuation<TResult, ?> continuation : this.continuations) {
                try {
                    continuation.then(this);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
            this.continuations = null;
        }
    }

    boolean trySetCancelled() {
        boolean z = true;
        synchronized (this.lock) {
            if (this.complete) {
                z = false;
            } else {
                this.complete = true;
                this.cancelled = true;
                this.lock.notifyAll();
                runContinuations();
            }
        }
        return z;
    }

    boolean trySetResult(TResult result) {
        boolean z = true;
        synchronized (this.lock) {
            if (this.complete) {
                z = false;
            } else {
                this.complete = true;
                this.result = result;
                this.lock.notifyAll();
                runContinuations();
            }
        }
        return z;
    }

    boolean trySetError(Exception error) {
        synchronized (this.lock) {
            if (this.complete) {
                return false;
            }
            this.complete = true;
            this.error = error;
            this.errorHasBeenObserved = false;
            this.lock.notifyAll();
            runContinuations();
            if (!this.errorHasBeenObserved && getUnobservedExceptionHandler() != null) {
                this.unobservedErrorNotifier = new UnobservedErrorNotifier(this);
            }
            return true;
        }
    }

    public class TaskCompletionSource extends bolts.TaskCompletionSource<TResult> {
        TaskCompletionSource() {
        }
    }
}
