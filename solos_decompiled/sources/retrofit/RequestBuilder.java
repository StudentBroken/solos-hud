package retrofit;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import retrofit.RequestInterceptor;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.converter.Converter;
import retrofit.http.Body;
import retrofit.http.EncodedPath;
import retrofit.http.EncodedQuery;
import retrofit.http.EncodedQueryMap;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.mime.FormUrlEncodedTypedOutput;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedOutput;
import retrofit.mime.TypedString;

/* JADX INFO: loaded from: classes53.dex */
final class RequestBuilder implements RequestInterceptor.RequestFacade {
    private final String apiUrl;
    private TypedOutput body;
    private String contentTypeHeader;
    private final Converter converter;
    private final FormUrlEncodedTypedOutput formBody;
    private List<Header> headers;
    private final boolean isObservable;
    private final boolean isSynchronous;
    private final MultipartTypedOutput multipartBody;
    private final Annotation[] paramAnnotations;
    private StringBuilder queryParams;
    private String relativeUrl;
    private final String requestMethod;

    RequestBuilder(String apiUrl, RestMethodInfo methodInfo, Converter converter) {
        this.apiUrl = apiUrl;
        this.converter = converter;
        this.paramAnnotations = methodInfo.requestParamAnnotations;
        this.requestMethod = methodInfo.requestMethod;
        this.isSynchronous = methodInfo.isSynchronous;
        this.isObservable = methodInfo.isObservable;
        if (methodInfo.headers != null) {
            this.headers = new ArrayList(methodInfo.headers);
        }
        this.contentTypeHeader = methodInfo.contentTypeHeader;
        this.relativeUrl = methodInfo.requestUrl;
        String requestQuery = methodInfo.requestQuery;
        if (requestQuery != null) {
            this.queryParams = new StringBuilder().append('?').append(requestQuery);
        }
        switch (methodInfo.requestType) {
            case FORM_URL_ENCODED:
                this.formBody = new FormUrlEncodedTypedOutput();
                this.multipartBody = null;
                this.body = this.formBody;
                return;
            case MULTIPART:
                this.formBody = null;
                this.multipartBody = new MultipartTypedOutput();
                this.body = this.multipartBody;
                return;
            case SIMPLE:
                this.formBody = null;
                this.multipartBody = null;
                return;
            default:
                throw new IllegalArgumentException("Unknown request type: " + methodInfo.requestType);
        }
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addHeader(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Header name must not be null.");
        }
        if ("Content-Type".equalsIgnoreCase(name)) {
            this.contentTypeHeader = value;
            return;
        }
        List<Header> headers = this.headers;
        if (headers == null) {
            headers = new ArrayList<>(2);
            this.headers = headers;
        }
        headers.add(new Header(name, value));
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addPathParam(String name, String value) {
        addPathParam(name, value, true);
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addEncodedPathParam(String name, String value) {
        addPathParam(name, value, false);
    }

    private void addPathParam(String name, String value, boolean urlEncodeValue) {
        if (name == null) {
            throw new IllegalArgumentException("Path replacement name must not be null.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Path replacement \"" + name + "\" value must not be null.");
        }
        try {
            if (urlEncodeValue) {
                String encodedValue = URLEncoder.encode(String.valueOf(value), "UTF-8");
                this.relativeUrl = this.relativeUrl.replace("{" + name + "}", encodedValue.replace("+", "%20"));
            } else {
                this.relativeUrl = this.relativeUrl.replace("{" + name + "}", String.valueOf(value));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to convert path parameter \"" + name + "\" value to UTF-8:" + value, e);
        }
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addQueryParam(String name, String value) {
        addQueryParam(name, value, false, true);
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addEncodedQueryParam(String name, String value) {
        addQueryParam(name, value, false, false);
    }

    private void addQueryParam(String name, Object value, boolean encodeName, boolean encodeValue) {
        if (value instanceof Iterable) {
            for (Object iterableValue : (Iterable) value) {
                if (iterableValue != null) {
                    addQueryParam(name, iterableValue.toString(), encodeName, encodeValue);
                }
            }
            return;
        }
        if (value.getClass().isArray()) {
            int arrayLength = Array.getLength(value);
            for (int x = 0; x < arrayLength; x++) {
                Object arrayValue = Array.get(value, x);
                if (arrayValue != null) {
                    addQueryParam(name, arrayValue.toString(), encodeName, encodeValue);
                }
            }
            return;
        }
        addQueryParam(name, value.toString(), encodeName, encodeValue);
    }

    private void addQueryParam(String name, String value, boolean encodeName, boolean encodeValue) {
        if (name == null) {
            throw new IllegalArgumentException("Query param name must not be null.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Query param \"" + name + "\" value must not be null.");
        }
        try {
            StringBuilder queryParams = this.queryParams;
            if (queryParams == null) {
                queryParams = new StringBuilder();
                this.queryParams = queryParams;
            }
            queryParams.append(queryParams.length() > 0 ? '&' : '?');
            if (encodeName) {
                name = URLEncoder.encode(name, "UTF-8");
            }
            if (encodeValue) {
                value = URLEncoder.encode(value, "UTF-8");
            }
            queryParams.append(name).append('=').append(value);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to convert query parameter \"" + name + "\" value to UTF-8: " + value, e);
        }
    }

    private void addQueryParamMap(int parameterNumber, Map<?, ?> map, boolean encodeNames, boolean encodeValues) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object entryKey = entry.getKey();
            if (entryKey == null) {
                throw new IllegalArgumentException("Parameter #" + (parameterNumber + 1) + " query map contained null key.");
            }
            Object entryValue = entry.getValue();
            if (entryValue != null) {
                addQueryParam(entryKey.toString(), entryValue.toString(), encodeNames, encodeValues);
            }
        }
    }

    void setArguments(Object[] args) {
        if (args != null) {
            int count = args.length;
            if (!this.isSynchronous && !this.isObservable) {
                count--;
            }
            for (int i = 0; i < count; i++) {
                Object value = args[i];
                Annotation annotation = this.paramAnnotations[i];
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType == Path.class) {
                    Path path = (Path) annotation;
                    String name = path.value();
                    if (value == null) {
                        throw new IllegalArgumentException("Path parameter \"" + name + "\" value must not be null.");
                    }
                    addPathParam(name, value.toString(), path.encode());
                } else if (annotationType == EncodedPath.class) {
                    String name2 = ((EncodedPath) annotation).value();
                    if (value == null) {
                        throw new IllegalArgumentException("Path parameter \"" + name2 + "\" value must not be null.");
                    }
                    addPathParam(name2, value.toString(), false);
                } else if (annotationType == Query.class) {
                    if (value != null) {
                        Query query = (Query) annotation;
                        addQueryParam(query.value(), value, query.encodeName(), query.encodeValue());
                    }
                } else if (annotationType == EncodedQuery.class) {
                    if (value != null) {
                        addQueryParam(((EncodedQuery) annotation).value(), value, false, false);
                    }
                } else if (annotationType == QueryMap.class) {
                    if (value != null) {
                        QueryMap queryMap = (QueryMap) annotation;
                        addQueryParamMap(i, (Map) value, queryMap.encodeNames(), queryMap.encodeValues());
                    }
                } else if (annotationType == EncodedQueryMap.class) {
                    if (value != null) {
                        addQueryParamMap(i, (Map) value, false, false);
                    }
                } else if (annotationType == retrofit.http.Header.class) {
                    if (value != null) {
                        addHeader(((retrofit.http.Header) annotation).value(), value.toString());
                    }
                } else if (annotationType == Field.class) {
                    String name3 = ((Field) annotation).value();
                    if (value != null) {
                        if (value instanceof Iterable) {
                            for (Object iterableValue : (Iterable) value) {
                                if (iterableValue != null) {
                                    this.formBody.addField(name3, iterableValue.toString());
                                }
                            }
                        } else if (value.getClass().isArray()) {
                            int arrayLength = Array.getLength(value);
                            for (int x = 0; x < arrayLength; x++) {
                                Object arrayValue = Array.get(value, x);
                                if (arrayValue != null) {
                                    this.formBody.addField(name3, arrayValue.toString());
                                }
                            }
                        } else {
                            this.formBody.addField(name3, value.toString());
                        }
                    }
                } else if (annotationType == FieldMap.class) {
                    if (value != null) {
                        for (Map.Entry<?, ?> entry : ((Map) value).entrySet()) {
                            Object entryKey = entry.getKey();
                            if (entryKey == null) {
                                throw new IllegalArgumentException("Parameter #" + (i + 1) + " field map contained null key.");
                            }
                            Object entryValue = entry.getValue();
                            if (entryValue != null) {
                                this.formBody.addField(entryKey.toString(), entryValue.toString());
                            }
                        }
                    } else {
                        continue;
                    }
                } else if (annotationType == Part.class) {
                    String name4 = ((Part) annotation).value();
                    if (value != null) {
                        String transferEncoding = ((Part) annotation).encoding();
                        if (value instanceof TypedOutput) {
                            this.multipartBody.addPart(name4, transferEncoding, (TypedOutput) value);
                        } else if (value instanceof String) {
                            this.multipartBody.addPart(name4, transferEncoding, new TypedString((String) value));
                        } else {
                            this.multipartBody.addPart(name4, transferEncoding, this.converter.toBody(value));
                        }
                    }
                } else if (annotationType == PartMap.class) {
                    if (value != null) {
                        String transferEncoding2 = ((PartMap) annotation).encoding();
                        for (Map.Entry<?, ?> entry2 : ((Map) value).entrySet()) {
                            Object entryKey2 = entry2.getKey();
                            if (entryKey2 == null) {
                                throw new IllegalArgumentException("Parameter #" + (i + 1) + " part map contained null key.");
                            }
                            String entryName = entryKey2.toString();
                            Object entryValue2 = entry2.getValue();
                            if (entryValue2 != null) {
                                if (entryValue2 instanceof TypedOutput) {
                                    this.multipartBody.addPart(entryName, transferEncoding2, (TypedOutput) entryValue2);
                                } else if (entryValue2 instanceof String) {
                                    this.multipartBody.addPart(entryName, transferEncoding2, new TypedString((String) entryValue2));
                                } else {
                                    this.multipartBody.addPart(entryName, transferEncoding2, this.converter.toBody(entryValue2));
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                } else if (annotationType == Body.class) {
                    if (value == null) {
                        throw new IllegalArgumentException("Body parameter value must not be null.");
                    }
                    if (value instanceof TypedOutput) {
                        this.body = (TypedOutput) value;
                    } else {
                        this.body = this.converter.toBody(value);
                    }
                } else {
                    throw new IllegalArgumentException("Unknown annotation: " + annotationType.getCanonicalName());
                }
            }
        }
    }

    Request build() throws UnsupportedEncodingException {
        if (this.multipartBody != null && this.multipartBody.getPartCount() == 0) {
            throw new IllegalStateException("Multipart requests must contain at least one part.");
        }
        String apiUrl = this.apiUrl;
        StringBuilder url = new StringBuilder(apiUrl);
        if (apiUrl.endsWith("/")) {
            url.deleteCharAt(url.length() - 1);
        }
        url.append(this.relativeUrl);
        StringBuilder queryParams = this.queryParams;
        if (queryParams != null) {
            url.append((CharSequence) queryParams);
        }
        TypedOutput body = this.body;
        List<Header> headers = this.headers;
        if (this.contentTypeHeader != null) {
            if (body != null) {
                body = new MimeOverridingTypedOutput(body, this.contentTypeHeader);
            } else {
                Header header = new Header("Content-Type", this.contentTypeHeader);
                if (headers == null) {
                    headers = Collections.singletonList(header);
                } else {
                    headers.add(header);
                }
            }
        }
        return new Request(this.requestMethod, url.toString(), headers, body);
    }

    private static class MimeOverridingTypedOutput implements TypedOutput {
        private final TypedOutput delegate;
        private final String mimeType;

        MimeOverridingTypedOutput(TypedOutput delegate, String mimeType) {
            this.delegate = delegate;
            this.mimeType = mimeType;
        }

        @Override // retrofit.mime.TypedOutput
        public String fileName() {
            return this.delegate.fileName();
        }

        @Override // retrofit.mime.TypedOutput
        public String mimeType() {
            return this.mimeType;
        }

        @Override // retrofit.mime.TypedOutput
        public long length() {
            return this.delegate.length();
        }

        @Override // retrofit.mime.TypedOutput
        public void writeTo(OutputStream out) throws IOException {
            this.delegate.writeTo(out);
        }
    }
}
