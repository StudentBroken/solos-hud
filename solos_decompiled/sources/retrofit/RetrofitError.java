package retrofit;

import java.io.IOException;
import java.lang.reflect.Type;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;

/* JADX INFO: loaded from: classes53.dex */
public class RetrofitError extends RuntimeException {
    private final Converter converter;
    private final Kind kind;
    private final Response response;
    private final Type successType;
    private final String url;

    public enum Kind {
        NETWORK,
        CONVERSION,
        HTTP,
        UNEXPECTED
    }

    public static RetrofitError networkError(String url, IOException exception) {
        return new RetrofitError(exception.getMessage(), url, null, null, null, Kind.NETWORK, exception);
    }

    public static RetrofitError conversionError(String url, Response response, Converter converter, Type successType, ConversionException exception) {
        return new RetrofitError(exception.getMessage(), url, response, converter, successType, Kind.CONVERSION, exception);
    }

    public static RetrofitError httpError(String url, Response response, Converter converter, Type successType) {
        String message = response.getStatus() + " " + response.getReason();
        return new RetrofitError(message, url, response, converter, successType, Kind.HTTP, null);
    }

    public static RetrofitError unexpectedError(String url, Throwable exception) {
        return new RetrofitError(exception.getMessage(), url, null, null, null, Kind.UNEXPECTED, exception);
    }

    RetrofitError(String message, String url, Response response, Converter converter, Type successType, Kind kind, Throwable exception) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.converter = converter;
        this.successType = successType;
        this.kind = kind;
    }

    public String getUrl() {
        return this.url;
    }

    public Response getResponse() {
        return this.response;
    }

    @Deprecated
    public boolean isNetworkError() {
        return this.kind == Kind.NETWORK;
    }

    public Kind getKind() {
        return this.kind;
    }

    public Object getBody() {
        return getBodyAs(this.successType);
    }

    public Type getSuccessType() {
        return this.successType;
    }

    public Object getBodyAs(Type type) {
        TypedInput body;
        if (this.response == null || (body = this.response.getBody()) == null) {
            return null;
        }
        try {
            return this.converter.fromBody(body, type);
        } catch (ConversionException e) {
            throw new RuntimeException(e);
        }
    }
}
