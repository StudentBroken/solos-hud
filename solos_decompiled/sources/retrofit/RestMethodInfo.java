package retrofit;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.EncodedPath;
import retrofit.http.EncodedQuery;
import retrofit.http.EncodedQueryMap;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.http.RestMethod;
import retrofit.http.Streaming;
import rx.Observable;

/* JADX INFO: loaded from: classes53.dex */
final class RestMethodInfo {
    private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
    private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
    String contentTypeHeader;
    List<Header> headers;
    final boolean isObservable;
    boolean isStreaming;
    final boolean isSynchronous;
    final Method method;
    boolean requestHasBody;
    String requestMethod;
    Annotation[] requestParamAnnotations;
    String requestQuery;
    String requestUrl;
    Set<String> requestUrlParamNames;
    Type responseObjectType;
    boolean loaded = false;
    RequestType requestType = RequestType.SIMPLE;
    final ResponseType responseType = parseResponseType();

    enum RequestType {
        SIMPLE,
        MULTIPART,
        FORM_URL_ENCODED
    }

    private enum ResponseType {
        VOID,
        OBSERVABLE,
        OBJECT
    }

    RestMethodInfo(Method method) {
        this.method = method;
        this.isSynchronous = this.responseType == ResponseType.OBJECT;
        this.isObservable = this.responseType == ResponseType.OBSERVABLE;
    }

    private RuntimeException methodError(String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        return new IllegalArgumentException(this.method.getDeclaringClass().getSimpleName() + "." + this.method.getName() + ": " + message);
    }

    private RuntimeException parameterError(int index, String message, Object... args) {
        return methodError(message + " (parameter #" + (index + 1) + ")", args);
    }

    synchronized void init() {
        if (!this.loaded) {
            parseMethodAnnotations();
            parseParameters();
            this.loaded = true;
        }
    }

    private void parseMethodAnnotations() {
        for (Annotation methodAnnotation : this.method.getAnnotations()) {
            Class<? extends Annotation> annotationType = methodAnnotation.annotationType();
            RestMethod methodInfo = null;
            Annotation[] annotations = annotationType.getAnnotations();
            int length = annotations.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Annotation innerAnnotation = annotations[i];
                if (RestMethod.class != innerAnnotation.annotationType()) {
                    i++;
                } else {
                    methodInfo = (RestMethod) innerAnnotation;
                    break;
                }
            }
            if (methodInfo != null) {
                if (this.requestMethod != null) {
                    throw methodError("Only one HTTP method is allowed. Found: %s and %s.", this.requestMethod, methodInfo.value());
                }
                try {
                    String path = (String) annotationType.getMethod(FirebaseAnalytics.Param.VALUE, new Class[0]).invoke(methodAnnotation, new Object[0]);
                    parsePath(path);
                    this.requestMethod = methodInfo.value();
                    this.requestHasBody = methodInfo.hasBody();
                } catch (Exception e) {
                    throw methodError("Failed to extract String 'value' from @%s annotation.", annotationType.getSimpleName());
                }
            } else if (annotationType == Headers.class) {
                String[] headersToParse = ((Headers) methodAnnotation).value();
                if (headersToParse.length == 0) {
                    throw methodError("@Headers annotation is empty.", new Object[0]);
                }
                this.headers = parseHeaders(headersToParse);
            } else if (annotationType == Multipart.class) {
                if (this.requestType != RequestType.SIMPLE) {
                    throw methodError("Only one encoding annotation is allowed.", new Object[0]);
                }
                this.requestType = RequestType.MULTIPART;
            } else if (annotationType == FormUrlEncoded.class) {
                if (this.requestType != RequestType.SIMPLE) {
                    throw methodError("Only one encoding annotation is allowed.", new Object[0]);
                }
                this.requestType = RequestType.FORM_URL_ENCODED;
            } else if (annotationType != Streaming.class) {
                continue;
            } else {
                if (this.responseObjectType != Response.class) {
                    throw methodError("Only methods having %s as data type are allowed to have @%s annotation.", Response.class.getSimpleName(), Streaming.class.getSimpleName());
                }
                this.isStreaming = true;
            }
        }
        if (this.requestMethod == null) {
            throw methodError("HTTP method annotation is required (e.g., @GET, @POST, etc.).", new Object[0]);
        }
        if (!this.requestHasBody) {
            if (this.requestType == RequestType.MULTIPART) {
                throw methodError("Multipart can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
            }
            if (this.requestType == RequestType.FORM_URL_ENCODED) {
                throw methodError("FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
            }
        }
    }

    private void parsePath(String path) {
        if (path == null || path.length() == 0 || path.charAt(0) != '/') {
            throw methodError("URL path \"%s\" must start with '/'.", path);
        }
        String url = path;
        String query = null;
        int question = path.indexOf(63);
        if (question != -1 && question < path.length() - 1) {
            url = path.substring(0, question);
            query = path.substring(question + 1);
            Matcher queryParamMatcher = PARAM_URL_REGEX.matcher(query);
            if (queryParamMatcher.find()) {
                throw methodError("URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.", query);
            }
        }
        Set<String> urlParams = parsePathParameters(path);
        this.requestUrl = url;
        this.requestUrlParamNames = urlParams;
        this.requestQuery = query;
    }

    List<Header> parseHeaders(String[] headers) {
        List<Header> headerList = new ArrayList<>();
        for (String header : headers) {
            int colon = header.indexOf(58);
            if (colon == -1 || colon == 0 || colon == header.length() - 1) {
                throw methodError("@Headers value must be in the form \"Name: Value\". Found: \"%s\"", header);
            }
            String headerName = header.substring(0, colon);
            String headerValue = header.substring(colon + 1).trim();
            if ("Content-Type".equalsIgnoreCase(headerName)) {
                this.contentTypeHeader = headerValue;
            } else {
                headerList.add(new Header(headerName, headerValue));
            }
        }
        return headerList;
    }

    private ResponseType parseResponseType() {
        Type returnType = this.method.getGenericReturnType();
        Type lastArgType = null;
        Class<?> lastArgClass = null;
        Type[] parameterTypes = this.method.getGenericParameterTypes();
        if (parameterTypes.length > 0) {
            Type typeToCheck = parameterTypes[parameterTypes.length - 1];
            lastArgType = typeToCheck;
            if (typeToCheck instanceof ParameterizedType) {
                typeToCheck = ((ParameterizedType) typeToCheck).getRawType();
            }
            if (typeToCheck instanceof Class) {
                lastArgClass = (Class) typeToCheck;
            }
        }
        boolean hasReturnType = returnType != Void.TYPE;
        boolean hasCallback = lastArgClass != null && Callback.class.isAssignableFrom(lastArgClass);
        if (hasReturnType && hasCallback) {
            throw methodError("Must have return type or Callback as last argument, not both.", new Object[0]);
        }
        if (!hasReturnType && !hasCallback) {
            throw methodError("Must have either a return type or Callback as last argument.", new Object[0]);
        }
        if (hasReturnType) {
            if (Platform.HAS_RX_JAVA) {
                Class<?> rawType = Types.getRawType(returnType);
                if (RxSupport.isObservable(rawType)) {
                    this.responseObjectType = getParameterUpperBound((ParameterizedType) RxSupport.getObservableType(returnType, rawType));
                    return ResponseType.OBSERVABLE;
                }
            }
            this.responseObjectType = returnType;
            return ResponseType.OBJECT;
        }
        Type lastArgType2 = Types.getSupertype(lastArgType, Types.getRawType(lastArgType), Callback.class);
        if (lastArgType2 instanceof ParameterizedType) {
            this.responseObjectType = getParameterUpperBound((ParameterizedType) lastArgType2);
            return ResponseType.VOID;
        }
        throw methodError("Last parameter must be of type Callback<X> or Callback<? super X>.", new Object[0]);
    }

    private static Type getParameterUpperBound(ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        for (int i = 0; i < types.length; i++) {
            Type paramType = types[i];
            if (paramType instanceof WildcardType) {
                types[i] = ((WildcardType) paramType).getUpperBounds()[0];
            }
        }
        return types[0];
    }

    private void parseParameters() {
        Class<?>[] methodParameterTypes = this.method.getParameterTypes();
        Annotation[][] methodParameterAnnotationArrays = this.method.getParameterAnnotations();
        int count = methodParameterAnnotationArrays.length;
        if (!this.isSynchronous && !this.isObservable) {
            count--;
        }
        Annotation[] requestParamAnnotations = new Annotation[count];
        boolean gotField = false;
        boolean gotPart = false;
        boolean gotBody = false;
        for (int i = 0; i < count; i++) {
            Class<?> methodParameterType = methodParameterTypes[i];
            Annotation[] methodParameterAnnotations = methodParameterAnnotationArrays[i];
            if (methodParameterAnnotations != null) {
                int length = methodParameterAnnotations.length;
                int i2 = 0;
                while (true) {
                    int i3 = i2;
                    if (i3 >= length) {
                        break;
                    }
                    Annotation methodParameterAnnotation = methodParameterAnnotations[i3];
                    Class<? extends Annotation> methodAnnotationType = methodParameterAnnotation.annotationType();
                    if (methodAnnotationType == Path.class) {
                        String name = ((Path) methodParameterAnnotation).value();
                        validatePathName(i, name);
                    } else if (methodAnnotationType == EncodedPath.class) {
                        String name2 = ((EncodedPath) methodParameterAnnotation).value();
                        validatePathName(i, name2);
                    } else {
                        if (methodAnnotationType != Query.class && methodAnnotationType != EncodedQuery.class) {
                            if (methodAnnotationType == QueryMap.class) {
                                if (!Map.class.isAssignableFrom(methodParameterType)) {
                                    throw parameterError(i, "@QueryMap parameter type must be Map.", new Object[0]);
                                }
                            } else if (methodAnnotationType == EncodedQueryMap.class) {
                                if (!Map.class.isAssignableFrom(methodParameterType)) {
                                    throw parameterError(i, "@EncodedQueryMap parameter type must be Map.", new Object[0]);
                                }
                            } else if (methodAnnotationType != retrofit.http.Header.class) {
                                if (methodAnnotationType == Field.class) {
                                    if (this.requestType != RequestType.FORM_URL_ENCODED) {
                                        throw parameterError(i, "@Field parameters can only be used with form encoding.", new Object[0]);
                                    }
                                    gotField = true;
                                } else if (methodAnnotationType == FieldMap.class) {
                                    if (this.requestType != RequestType.FORM_URL_ENCODED) {
                                        throw parameterError(i, "@FieldMap parameters can only be used with form encoding.", new Object[0]);
                                    }
                                    if (!Map.class.isAssignableFrom(methodParameterType)) {
                                        throw parameterError(i, "@FieldMap parameter type must be Map.", new Object[0]);
                                    }
                                    gotField = true;
                                } else if (methodAnnotationType == Part.class) {
                                    if (this.requestType != RequestType.MULTIPART) {
                                        throw parameterError(i, "@Part parameters can only be used with multipart encoding.", new Object[0]);
                                    }
                                    gotPart = true;
                                } else if (methodAnnotationType == PartMap.class) {
                                    if (this.requestType != RequestType.MULTIPART) {
                                        throw parameterError(i, "@PartMap parameters can only be used with multipart encoding.", new Object[0]);
                                    }
                                    if (!Map.class.isAssignableFrom(methodParameterType)) {
                                        throw parameterError(i, "@PartMap parameter type must be Map.", new Object[0]);
                                    }
                                    gotPart = true;
                                } else if (methodAnnotationType != Body.class) {
                                    continue;
                                } else {
                                    if (this.requestType != RequestType.SIMPLE) {
                                        throw parameterError(i, "@Body parameters cannot be used with form or multi-part encoding.", new Object[0]);
                                    }
                                    if (gotBody) {
                                        throw methodError("Multiple @Body method annotations found.", new Object[0]);
                                    }
                                    gotBody = true;
                                }
                            }
                        }
                        i2 = i3 + 1;
                    }
                    if (requestParamAnnotations[i] != null) {
                        throw parameterError(i, "Multiple Retrofit annotations found, only one allowed: @%s, @%s.", requestParamAnnotations[i].annotationType().getSimpleName(), methodAnnotationType.getSimpleName());
                    }
                    requestParamAnnotations[i] = methodParameterAnnotation;
                    i2 = i3 + 1;
                }
            }
            if (requestParamAnnotations[i] == null) {
                throw parameterError(i, "No Retrofit annotation found.", new Object[0]);
            }
        }
        if (this.requestType == RequestType.SIMPLE && !this.requestHasBody && gotBody) {
            throw methodError("Non-body HTTP method cannot contain @Body or @TypedOutput.", new Object[0]);
        }
        if (this.requestType == RequestType.FORM_URL_ENCODED && !gotField) {
            throw methodError("Form-encoded method must contain at least one @Field.", new Object[0]);
        }
        if (this.requestType == RequestType.MULTIPART && !gotPart) {
            throw methodError("Multipart method must contain at least one @Part.", new Object[0]);
        }
        this.requestParamAnnotations = requestParamAnnotations;
    }

    private void validatePathName(int index, String name) {
        if (!PARAM_NAME_REGEX.matcher(name).matches()) {
            throw parameterError(index, "@Path parameter name must match %s. Found: %s", PARAM_URL_REGEX.pattern(), name);
        }
        if (!this.requestUrlParamNames.contains(name)) {
            throw parameterError(index, "URL \"%s\" does not contain \"{%s}\".", this.requestUrl, name);
        }
    }

    static Set<String> parsePathParameters(String path) {
        Matcher m = PARAM_URL_REGEX.matcher(path);
        Set<String> patterns = new LinkedHashSet<>();
        while (m.find()) {
            patterns.add(m.group(1));
        }
        return patterns;
    }

    private static final class RxSupport {
        private RxSupport() {
        }

        public static boolean isObservable(Class rawType) {
            return rawType == Observable.class;
        }

        public static Type getObservableType(Type contextType, Class contextRawType) {
            return Types.getSupertype(contextType, contextRawType, Observable.class);
        }
    }
}
