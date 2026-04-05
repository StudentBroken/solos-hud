package retrofit;

import android.os.Build;
import android.os.Process;
import com.google.gson.Gson;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import retrofit.RestAdapter;
import retrofit.Utils;
import retrofit.android.AndroidApacheClient;
import retrofit.android.AndroidLog;
import retrofit.android.MainThreadExecutor;
import retrofit.appengine.UrlFetchClient;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.UrlConnectionClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/* JADX INFO: loaded from: classes53.dex */
abstract class Platform {
    private static final Platform PLATFORM = findPlatform();
    static final boolean HAS_RX_JAVA = hasRxJavaOnClasspath();

    abstract Executor defaultCallbackExecutor();

    abstract Client.Provider defaultClient();

    abstract Converter defaultConverter();

    abstract Executor defaultHttpExecutor();

    abstract RestAdapter.Log defaultLog();

    Platform() {
    }

    static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException e) {
        }
        if (System.getProperty("com.google.appengine.runtime.version") != null) {
            return new AppEngine();
        }
        return new Base();
    }

    private static class Base extends Platform {
        private Base() {
        }

        @Override // retrofit.Platform
        Converter defaultConverter() {
            return new GsonConverter(new Gson());
        }

        @Override // retrofit.Platform
        Client.Provider defaultClient() {
            final Client client;
            if (Platform.hasOkHttpOnClasspath()) {
                client = OkClientInstantiator.instantiate();
            } else {
                client = new UrlConnectionClient();
            }
            return new Client.Provider() { // from class: retrofit.Platform.Base.1
                @Override // retrofit.client.Client.Provider
                public Client get() {
                    return client;
                }
            };
        }

        @Override // retrofit.Platform
        Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() { // from class: retrofit.Platform.Base.2
                @Override // java.util.concurrent.ThreadFactory
                public Thread newThread(final Runnable r) {
                    return new Thread(new Runnable() { // from class: retrofit.Platform.Base.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Thread.currentThread().setPriority(1);
                            r.run();
                        }
                    }, "Retrofit-Idle");
                }
            });
        }

        @Override // retrofit.Platform
        Executor defaultCallbackExecutor() {
            return new Utils.SynchronousExecutor();
        }

        @Override // retrofit.Platform
        RestAdapter.Log defaultLog() {
            return new RestAdapter.Log() { // from class: retrofit.Platform.Base.3
                @Override // retrofit.RestAdapter.Log
                public void log(String message) {
                    System.out.println(message);
                }
            };
        }
    }

    private static class Android extends Platform {
        private Android() {
        }

        @Override // retrofit.Platform
        Converter defaultConverter() {
            return new GsonConverter(new Gson());
        }

        @Override // retrofit.Platform
        Client.Provider defaultClient() {
            final Client client;
            if (Platform.hasOkHttpOnClasspath()) {
                client = OkClientInstantiator.instantiate();
            } else if (Build.VERSION.SDK_INT < 9) {
                client = new AndroidApacheClient();
            } else {
                client = new UrlConnectionClient();
            }
            return new Client.Provider() { // from class: retrofit.Platform.Android.1
                @Override // retrofit.client.Client.Provider
                public Client get() {
                    return client;
                }
            };
        }

        @Override // retrofit.Platform
        Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() { // from class: retrofit.Platform.Android.2
                @Override // java.util.concurrent.ThreadFactory
                public Thread newThread(final Runnable r) {
                    return new Thread(new Runnable() { // from class: retrofit.Platform.Android.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Process.setThreadPriority(10);
                            r.run();
                        }
                    }, "Retrofit-Idle");
                }
            });
        }

        @Override // retrofit.Platform
        Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        @Override // retrofit.Platform
        RestAdapter.Log defaultLog() {
            return new AndroidLog("Retrofit");
        }
    }

    private static class AppEngine extends Base {
        private AppEngine() {
            super();
        }

        @Override // retrofit.Platform.Base, retrofit.Platform
        Client.Provider defaultClient() {
            final UrlFetchClient client = new UrlFetchClient();
            return new Client.Provider() { // from class: retrofit.Platform.AppEngine.1
                @Override // retrofit.client.Client.Provider
                public Client get() {
                    return client;
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasOkHttpOnClasspath() {
        boolean okUrlFactory = false;
        try {
            Class.forName("com.squareup.okhttp.OkUrlFactory");
            okUrlFactory = true;
        } catch (ClassNotFoundException e) {
        }
        boolean okHttpClient = false;
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            okHttpClient = true;
        } catch (ClassNotFoundException e2) {
        }
        if (okHttpClient != okUrlFactory) {
            throw new RuntimeException("Retrofit detected an unsupported OkHttp on the classpath.\nTo use OkHttp with this version of Retrofit, you'll need:\n1. com.squareup.okhttp:okhttp:1.6.0 (or newer)\n2. com.squareup.okhttp:okhttp-urlconnection:1.6.0 (or newer)\nNote that OkHttp 2.0.0+ is supported!");
        }
        return okHttpClient;
    }

    private static class OkClientInstantiator {
        private OkClientInstantiator() {
        }

        static Client instantiate() {
            return new OkClient();
        }
    }

    private static boolean hasRxJavaOnClasspath() {
        try {
            Class.forName("rx.Observable");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
