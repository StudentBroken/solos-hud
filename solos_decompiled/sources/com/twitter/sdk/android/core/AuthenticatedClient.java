package com.twitter.sdk.android.core;

import com.twitter.sdk.android.core.internal.TwitterRequestHeaders;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import javax.net.ssl.SSLSocketFactory;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.FormUrlEncodedTypedOutput;
import retrofit.mime.TypedOutput;

/* JADX INFO: loaded from: classes62.dex */
public class AuthenticatedClient extends DefaultClient {
    private final TwitterAuthConfig authConfig;
    private final Session session;

    public AuthenticatedClient(TwitterAuthConfig config, Session session, SSLSocketFactory sslSocketFactory) {
        super(sslSocketFactory);
        this.authConfig = config;
        this.session = session;
    }

    @Override // com.twitter.sdk.android.core.DefaultClient, retrofit.client.Client
    public Response execute(Request request) throws IOException {
        Response response = this.wrappedClient.execute(new Request(request.getMethod(), request.getUrl(), getAuthHeaders(request), request.getBody()));
        return response;
    }

    protected List<Header> getAuthHeaders(Request request) throws IOException {
        TwitterRequestHeaders authHeaders = new TwitterRequestHeaders(request.getMethod(), request.getUrl(), this.authConfig, this.session, null, getPostParams(request));
        List<Header> headers = new ArrayList<>(request.getHeaders());
        for (Map.Entry<String, String> header : authHeaders.getHeaders().entrySet()) {
            headers.add(new Header(header.getKey(), header.getValue()));
        }
        return headers;
    }

    protected Map<String, String> getPostParams(Request request) throws IOException {
        Map<String, String> params = new TreeMap<>();
        if (HttpRequest.METHOD_POST.equals(request.getMethod().toUpperCase(Locale.US))) {
            TypedOutput output = request.getBody();
            if (output instanceof FormUrlEncodedTypedOutput) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                output.writeTo(os);
                String val = os.toString("UTF-8");
                if (val.length() > 0) {
                    params.putAll(getParameters(val));
                }
            }
        }
        return params;
    }

    protected Map<String, String> getParameters(String input) {
        Map<String, String> parameters = new HashMap<>();
        Scanner scanner = new Scanner(input).useDelimiter("&");
        while (scanner.hasNext()) {
            String[] param = scanner.next().split("=");
            if (param.length == 0 || param.length > 2) {
                throw new IllegalArgumentException("bad parameter");
            }
            String name = decode(param[0], "UTF-8");
            String value = "";
            if (param.length == 2) {
                value = decode(param[1], "UTF-8");
            }
            parameters.put(name, value);
        }
        return Collections.unmodifiableMap(parameters);
    }

    protected String decode(String value, String encoding) {
        try {
            return URLDecoder.decode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("bad parameter encoding");
        }
    }
}
