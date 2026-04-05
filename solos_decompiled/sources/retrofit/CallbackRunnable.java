package retrofit;

import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes53.dex */
abstract class CallbackRunnable<T> implements Runnable {
    private final Callback<T> callback;
    private final Executor callbackExecutor;
    private final ErrorHandler errorHandler;

    public abstract ResponseWrapper obtainResponse();

    CallbackRunnable(Callback<T> callback, Executor callbackExecutor, ErrorHandler errorHandler) {
        this.callback = callback;
        this.callbackExecutor = callbackExecutor;
        this.errorHandler = errorHandler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            final ResponseWrapper wrapper = obtainResponse();
            this.callbackExecutor.execute(new Runnable() { // from class: retrofit.CallbackRunnable.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    CallbackRunnable.this.callback.success(wrapper.responseBody, wrapper.response);
                }
            });
        } catch (RetrofitError e) {
            Throwable cause = this.errorHandler.handleError(e);
            final RetrofitError handled = cause == e ? e : RetrofitError.unexpectedError(e.getUrl(), cause);
            this.callbackExecutor.execute(new Runnable() { // from class: retrofit.CallbackRunnable.2
                @Override // java.lang.Runnable
                public void run() {
                    CallbackRunnable.this.callback.failure(handled);
                }
            });
        }
    }
}
