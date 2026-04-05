package retrofit;

import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

/* JADX INFO: loaded from: classes53.dex */
final class RxSupport {
    private final ErrorHandler errorHandler;
    private final Executor executor;
    private final RequestInterceptor requestInterceptor;

    interface Invoker {
        ResponseWrapper invoke(RequestInterceptor requestInterceptor);
    }

    RxSupport(Executor executor, ErrorHandler errorHandler, RequestInterceptor requestInterceptor) {
        this.executor = executor;
        this.errorHandler = errorHandler;
        this.requestInterceptor = requestInterceptor;
    }

    Observable createRequestObservable(final Invoker invoker) {
        return Observable.create(new Observable.OnSubscribe<Object>() { // from class: retrofit.RxSupport.1
            public void call(Subscriber<? super Object> subscriber) {
                RequestInterceptorTape interceptorTape = new RequestInterceptorTape();
                RxSupport.this.requestInterceptor.intercept(interceptorTape);
                Runnable runnable = RxSupport.this.getRunnable(subscriber, invoker, interceptorTape);
                FutureTask<Void> task = new FutureTask<>(runnable, null);
                subscriber.add(Subscriptions.from(task));
                RxSupport.this.executor.execute(task);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Runnable getRunnable(final Subscriber<? super Object> subscriber, final Invoker invoker, final RequestInterceptorTape interceptorTape) {
        return new Runnable() { // from class: retrofit.RxSupport.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        ResponseWrapper wrapper = invoker.invoke(interceptorTape);
                        subscriber.onNext(wrapper.responseBody);
                        subscriber.onCompleted();
                    }
                } catch (RetrofitError e) {
                    subscriber.onError(RxSupport.this.errorHandler.handleError(e));
                }
            }
        };
    }
}
