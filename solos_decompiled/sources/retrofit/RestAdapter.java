package retrofit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import retrofit.Profiler;
import retrofit.RxSupport;
import retrofit.Utils;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/* JADX INFO: loaded from: classes53.dex */
public class RestAdapter {
    static final String IDLE_THREAD_NAME = "Retrofit-Idle";
    static final String THREAD_PREFIX = "Retrofit-";
    final Executor callbackExecutor;
    private final Client.Provider clientProvider;
    final Converter converter;
    final ErrorHandler errorHandler;
    final Executor httpExecutor;
    final Log log;
    volatile LogLevel logLevel;
    private final Profiler profiler;
    final RequestInterceptor requestInterceptor;
    private RxSupport rxSupport;
    final Endpoint server;
    private final Map<Class<?>, Map<Method, RestMethodInfo>> serviceMethodInfoCache;

    public interface Log {
        public static final Log NONE = new Log() { // from class: retrofit.RestAdapter.Log.1
            @Override // retrofit.RestAdapter.Log
            public void log(String message) {
            }
        };

        void log(String str);
    }

    public enum LogLevel {
        NONE,
        BASIC,
        HEADERS,
        HEADERS_AND_ARGS,
        FULL;

        public boolean log() {
            return this != NONE;
        }
    }

    private RestAdapter(Endpoint server, Client.Provider clientProvider, Executor httpExecutor, Executor callbackExecutor, RequestInterceptor requestInterceptor, Converter converter, Profiler profiler, ErrorHandler errorHandler, Log log, LogLevel logLevel) {
        this.serviceMethodInfoCache = new LinkedHashMap();
        this.server = server;
        this.clientProvider = clientProvider;
        this.httpExecutor = httpExecutor;
        this.callbackExecutor = callbackExecutor;
        this.requestInterceptor = requestInterceptor;
        this.converter = converter;
        this.profiler = profiler;
        this.errorHandler = errorHandler;
        this.log = log;
        this.logLevel = logLevel;
    }

    public void setLogLevel(LogLevel loglevel) {
        if (this.logLevel == null) {
            throw new NullPointerException("Log level may not be null.");
        }
        this.logLevel = loglevel;
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public <T> T create(Class<T> cls) {
        Utils.validateServiceClass(cls);
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new RestHandler(getMethodInfoCache(cls)));
    }

    Map<Method, RestMethodInfo> getMethodInfoCache(Class<?> service) {
        Map<Method, RestMethodInfo> methodInfoCache;
        synchronized (this.serviceMethodInfoCache) {
            methodInfoCache = this.serviceMethodInfoCache.get(service);
            if (methodInfoCache == null) {
                methodInfoCache = new LinkedHashMap<>();
                this.serviceMethodInfoCache.put(service, methodInfoCache);
            }
        }
        return methodInfoCache;
    }

    static RestMethodInfo getMethodInfo(Map<Method, RestMethodInfo> cache, Method method) {
        RestMethodInfo methodInfo;
        synchronized (cache) {
            methodInfo = cache.get(method);
            if (methodInfo == null) {
                methodInfo = new RestMethodInfo(method);
                cache.put(method, methodInfo);
            }
        }
        return methodInfo;
    }

    private class RestHandler implements InvocationHandler {
        private final Map<Method, RestMethodInfo> methodDetailsCache;

        RestHandler(Map<Method, RestMethodInfo> methodDetailsCache) {
            this.methodDetailsCache = methodDetailsCache;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, final Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            final RestMethodInfo methodInfo = RestAdapter.getMethodInfo(this.methodDetailsCache, method);
            if (methodInfo.isSynchronous) {
                try {
                    return invokeRequest(RestAdapter.this.requestInterceptor, methodInfo, args);
                } catch (RetrofitError error) {
                    Throwable newError = RestAdapter.this.errorHandler.handleError(error);
                    if (newError == null) {
                        throw new IllegalStateException("Error handler returned null for wrapped exception.", error);
                    }
                    throw newError;
                }
            }
            if (RestAdapter.this.httpExecutor == null || RestAdapter.this.callbackExecutor == null) {
                throw new IllegalStateException("Asynchronous invocation requires calling setExecutors.");
            }
            if (methodInfo.isObservable) {
                if (RestAdapter.this.rxSupport == null) {
                    if (Platform.HAS_RX_JAVA) {
                        RestAdapter.this.rxSupport = new RxSupport(RestAdapter.this.httpExecutor, RestAdapter.this.errorHandler, RestAdapter.this.requestInterceptor);
                    } else {
                        throw new IllegalStateException("Observable method found but no RxJava on classpath.");
                    }
                }
                return RestAdapter.this.rxSupport.createRequestObservable(new RxSupport.Invoker() { // from class: retrofit.RestAdapter.RestHandler.1
                    @Override // retrofit.RxSupport.Invoker
                    public ResponseWrapper invoke(RequestInterceptor requestInterceptor) {
                        return (ResponseWrapper) RestHandler.this.invokeRequest(requestInterceptor, methodInfo, args);
                    }
                });
            }
            final RequestInterceptorTape interceptorTape = new RequestInterceptorTape();
            RestAdapter.this.requestInterceptor.intercept(interceptorTape);
            Callback<?> callback = (Callback) args[args.length - 1];
            RestAdapter.this.httpExecutor.execute(new CallbackRunnable(callback, RestAdapter.this.callbackExecutor, RestAdapter.this.errorHandler) { // from class: retrofit.RestAdapter.RestHandler.2
                @Override // retrofit.CallbackRunnable
                public ResponseWrapper obtainResponse() {
                    return (ResponseWrapper) RestHandler.this.invokeRequest(interceptorTape, methodInfo, args);
                }
            });
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Object invokeRequest(RequestInterceptor requestInterceptor, RestMethodInfo methodInfo, Object[] args) {
            try {
                try {
                    try {
                        methodInfo.init();
                        String serverUrl = RestAdapter.this.server.getUrl();
                        RequestBuilder requestBuilder = new RequestBuilder(serverUrl, methodInfo, RestAdapter.this.converter);
                        requestBuilder.setArguments(args);
                        requestInterceptor.intercept(requestBuilder);
                        Request request = requestBuilder.build();
                        String url = request.getUrl();
                        if (!methodInfo.isSynchronous) {
                            Thread.currentThread().setName(RestAdapter.THREAD_PREFIX + url.substring(serverUrl.length()));
                        }
                        if (RestAdapter.this.logLevel.log()) {
                            request = RestAdapter.this.logAndReplaceRequest("HTTP", request, args);
                        }
                        Object profilerObject = RestAdapter.this.profiler != null ? RestAdapter.this.profiler.beforeCall() : null;
                        long start = System.nanoTime();
                        Response response = RestAdapter.this.clientProvider.get().execute(request);
                        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                        int statusCode = response.getStatus();
                        if (RestAdapter.this.profiler != null) {
                            Profiler.RequestInformation requestInfo = RestAdapter.getRequestInfo(serverUrl, methodInfo, request);
                            RestAdapter.this.profiler.afterCall(requestInfo, elapsedTime, statusCode, profilerObject);
                        }
                        if (RestAdapter.this.logLevel.log()) {
                            response = RestAdapter.this.logAndReplaceResponse(url, response, elapsedTime);
                        }
                        Type type = methodInfo.responseObjectType;
                        if (statusCode < 200 || statusCode >= 300) {
                            throw RetrofitError.httpError(url, Utils.readBodyToBytesIfNecessary(response), RestAdapter.this.converter, type);
                        }
                        if (type.equals(Response.class)) {
                            if (!methodInfo.isStreaming) {
                                response = Utils.readBodyToBytesIfNecessary(response);
                            }
                            if (methodInfo.isSynchronous) {
                                return response;
                            }
                            ResponseWrapper responseWrapper = new ResponseWrapper(response, response);
                            if (methodInfo.isSynchronous) {
                                return responseWrapper;
                            }
                            Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                            return responseWrapper;
                        }
                        TypedInput body = response.getBody();
                        if (body == null) {
                            if (methodInfo.isSynchronous) {
                                if (methodInfo.isSynchronous) {
                                    return null;
                                }
                                Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                                return null;
                            }
                            ResponseWrapper responseWrapper2 = new ResponseWrapper(response, null);
                            if (methodInfo.isSynchronous) {
                                return responseWrapper2;
                            }
                            Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                            return responseWrapper2;
                        }
                        ExceptionCatchingTypedInput wrapped = new ExceptionCatchingTypedInput(body);
                        try {
                            Object convert = RestAdapter.this.converter.fromBody(wrapped, type);
                            RestAdapter.this.logResponseBody(body, convert);
                            if (methodInfo.isSynchronous) {
                                if (methodInfo.isSynchronous) {
                                    return convert;
                                }
                                Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                                return convert;
                            }
                            ResponseWrapper responseWrapper3 = new ResponseWrapper(response, convert);
                            if (!methodInfo.isSynchronous) {
                                Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                            }
                            return responseWrapper3;
                        } catch (ConversionException e) {
                            if (wrapped.threwException()) {
                                throw wrapped.getThrownException();
                            }
                            throw RetrofitError.conversionError(url, Utils.replaceResponseBody(response, null), RestAdapter.this.converter, type, e);
                        }
                    } finally {
                        if (!methodInfo.isSynchronous) {
                            Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                        }
                    }
                } catch (RetrofitError e2) {
                    throw e2;
                }
            } catch (IOException e3) {
                if (RestAdapter.this.logLevel.log()) {
                    RestAdapter.this.logException(e3, null);
                }
                throw RetrofitError.networkError(null, e3);
            } catch (Throwable t) {
                if (RestAdapter.this.logLevel.log()) {
                    RestAdapter.this.logException(t, null);
                }
                throw RetrofitError.unexpectedError(null, t);
            }
        }
    }

    Request logAndReplaceRequest(String name, Request request, Object[] args) throws IOException {
        this.log.log(String.format("---> %s %s %s", name, request.getMethod(), request.getUrl()));
        if (this.logLevel.ordinal() >= LogLevel.HEADERS.ordinal()) {
            for (Header header : request.getHeaders()) {
                this.log.log(header.toString());
            }
            String bodySize = "no";
            TypedOutput body = request.getBody();
            if (body != null) {
                String bodyMime = body.mimeType();
                if (bodyMime != null) {
                    this.log.log("Content-Type: " + bodyMime);
                }
                long bodyLength = body.length();
                bodySize = bodyLength + "-byte";
                if (bodyLength != -1) {
                    this.log.log("Content-Length: " + bodyLength);
                }
                if (this.logLevel.ordinal() >= LogLevel.FULL.ordinal()) {
                    if (!request.getHeaders().isEmpty()) {
                        this.log.log("");
                    }
                    if (!(body instanceof TypedByteArray)) {
                        request = Utils.readBodyToBytesIfNecessary(request);
                        body = request.getBody();
                    }
                    byte[] bodyBytes = ((TypedByteArray) body).getBytes();
                    String bodyCharset = MimeUtil.parseCharset(body.mimeType());
                    this.log.log(new String(bodyBytes, bodyCharset));
                } else if (this.logLevel.ordinal() >= LogLevel.HEADERS_AND_ARGS.ordinal()) {
                    if (!request.getHeaders().isEmpty()) {
                        this.log.log("---> REQUEST:");
                    }
                    for (int i = 0; i < args.length; i++) {
                        this.log.log("#" + i + ": " + args[i]);
                    }
                }
            }
            this.log.log(String.format("---> END %s (%s body)", name, bodySize));
        }
        return request;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Response logAndReplaceResponse(String url, Response response, long elapsedTime) throws Throwable {
        this.log.log(String.format("<--- HTTP %s %s (%sms)", Integer.valueOf(response.getStatus()), url, Long.valueOf(elapsedTime)));
        if (this.logLevel.ordinal() >= LogLevel.HEADERS.ordinal()) {
            for (Header header : response.getHeaders()) {
                this.log.log(header.toString());
            }
            long bodySize = 0;
            TypedInput body = response.getBody();
            if (body != null) {
                bodySize = body.length();
                if (this.logLevel.ordinal() >= LogLevel.FULL.ordinal()) {
                    if (!response.getHeaders().isEmpty()) {
                        this.log.log("");
                    }
                    if (!(body instanceof TypedByteArray)) {
                        response = Utils.readBodyToBytesIfNecessary(response);
                        body = response.getBody();
                    }
                    byte[] bodyBytes = ((TypedByteArray) body).getBytes();
                    bodySize = bodyBytes.length;
                    String bodyMime = body.mimeType();
                    String bodyCharset = MimeUtil.parseCharset(bodyMime);
                    this.log.log(new String(bodyBytes, bodyCharset));
                }
            }
            this.log.log(String.format("<--- END HTTP (%s-byte body)", Long.valueOf(bodySize)));
        }
        return response;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logResponseBody(TypedInput body, Object convert) {
        if (this.logLevel.ordinal() == LogLevel.HEADERS_AND_ARGS.ordinal()) {
            this.log.log("<--- BODY:");
            this.log.log(convert.toString());
        }
    }

    void logException(Throwable t, String url) {
        Log log = this.log;
        Object[] objArr = new Object[1];
        if (url == null) {
            url = "";
        }
        objArr[0] = url;
        log.log(String.format("---- ERROR %s", objArr));
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        this.log.log(sw.toString());
        this.log.log("---- END ERROR");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Profiler.RequestInformation getRequestInfo(String serverUrl, RestMethodInfo methodDetails, Request request) {
        long contentLength = 0;
        String contentType = null;
        TypedOutput body = request.getBody();
        if (body != null) {
            contentLength = body.length();
            contentType = body.mimeType();
        }
        return new Profiler.RequestInformation(methodDetails.requestMethod, serverUrl, methodDetails.requestUrl, contentLength, contentType);
    }

    public static class Builder {
        private Executor callbackExecutor;
        private Client.Provider clientProvider;
        private Converter converter;
        private Endpoint endpoint;
        private ErrorHandler errorHandler;
        private Executor httpExecutor;
        private Log log;
        private LogLevel logLevel = LogLevel.NONE;
        private Profiler profiler;
        private RequestInterceptor requestInterceptor;

        public Builder setEndpoint(String endpoint) {
            if (endpoint == null || endpoint.trim().length() == 0) {
                throw new NullPointerException("Endpoint may not be blank.");
            }
            this.endpoint = Endpoints.newFixedEndpoint(endpoint);
            return this;
        }

        public Builder setEndpoint(Endpoint endpoint) {
            if (endpoint == null) {
                throw new NullPointerException("Endpoint may not be null.");
            }
            this.endpoint = endpoint;
            return this;
        }

        public Builder setClient(final Client client) {
            if (client == null) {
                throw new NullPointerException("Client may not be null.");
            }
            return setClient(new Client.Provider() { // from class: retrofit.RestAdapter.Builder.1
                @Override // retrofit.client.Client.Provider
                public Client get() {
                    return client;
                }
            });
        }

        public Builder setClient(Client.Provider clientProvider) {
            if (clientProvider == null) {
                throw new NullPointerException("Client provider may not be null.");
            }
            this.clientProvider = clientProvider;
            return this;
        }

        public Builder setExecutors(Executor httpExecutor, Executor callbackExecutor) {
            if (httpExecutor == null) {
                throw new NullPointerException("HTTP executor may not be null.");
            }
            if (callbackExecutor == null) {
                callbackExecutor = new Utils.SynchronousExecutor();
            }
            this.httpExecutor = httpExecutor;
            this.callbackExecutor = callbackExecutor;
            return this;
        }

        public Builder setRequestInterceptor(RequestInterceptor requestInterceptor) {
            if (requestInterceptor == null) {
                throw new NullPointerException("Request interceptor may not be null.");
            }
            this.requestInterceptor = requestInterceptor;
            return this;
        }

        public Builder setConverter(Converter converter) {
            if (converter == null) {
                throw new NullPointerException("Converter may not be null.");
            }
            this.converter = converter;
            return this;
        }

        public Builder setProfiler(Profiler profiler) {
            if (profiler == null) {
                throw new NullPointerException("Profiler may not be null.");
            }
            this.profiler = profiler;
            return this;
        }

        public Builder setErrorHandler(ErrorHandler errorHandler) {
            if (errorHandler == null) {
                throw new NullPointerException("Error handler may not be null.");
            }
            this.errorHandler = errorHandler;
            return this;
        }

        public Builder setLog(Log log) {
            if (log == null) {
                throw new NullPointerException("Log may not be null.");
            }
            this.log = log;
            return this;
        }

        public Builder setLogLevel(LogLevel logLevel) {
            if (logLevel == null) {
                throw new NullPointerException("Log level may not be null.");
            }
            this.logLevel = logLevel;
            return this;
        }

        public RestAdapter build() {
            if (this.endpoint == null) {
                throw new IllegalArgumentException("Endpoint may not be null.");
            }
            ensureSaneDefaults();
            return new RestAdapter(this.endpoint, this.clientProvider, this.httpExecutor, this.callbackExecutor, this.requestInterceptor, this.converter, this.profiler, this.errorHandler, this.log, this.logLevel);
        }

        private void ensureSaneDefaults() {
            if (this.converter == null) {
                this.converter = Platform.get().defaultConverter();
            }
            if (this.clientProvider == null) {
                this.clientProvider = Platform.get().defaultClient();
            }
            if (this.httpExecutor == null) {
                this.httpExecutor = Platform.get().defaultHttpExecutor();
            }
            if (this.callbackExecutor == null) {
                this.callbackExecutor = Platform.get().defaultCallbackExecutor();
            }
            if (this.errorHandler == null) {
                this.errorHandler = ErrorHandler.DEFAULT;
            }
            if (this.log == null) {
                this.log = Platform.get().defaultLog();
            }
            if (this.requestInterceptor == null) {
                this.requestInterceptor = RequestInterceptor.NONE;
            }
        }
    }
}
