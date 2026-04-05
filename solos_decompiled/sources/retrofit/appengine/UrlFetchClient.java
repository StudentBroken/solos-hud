package retrofit.appengine;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedOutput;

/* JADX INFO: loaded from: classes53.dex */
public class UrlFetchClient implements Client {
    private final URLFetchService urlFetchService;

    private static HTTPMethod getHttpMethod(String method) {
        if (HttpRequest.METHOD_GET.equals(method)) {
            return HTTPMethod.GET;
        }
        if (HttpRequest.METHOD_POST.equals(method)) {
            return HTTPMethod.POST;
        }
        if ("PATCH".equals(method)) {
            return HTTPMethod.PATCH;
        }
        if (HttpRequest.METHOD_PUT.equals(method)) {
            return HTTPMethod.PUT;
        }
        if (HttpRequest.METHOD_DELETE.equals(method)) {
            return HTTPMethod.DELETE;
        }
        if (HttpRequest.METHOD_HEAD.equals(method)) {
            return HTTPMethod.HEAD;
        }
        throw new IllegalStateException("Illegal HTTP method: " + method);
    }

    public UrlFetchClient() {
        this(URLFetchServiceFactory.getURLFetchService());
    }

    public UrlFetchClient(URLFetchService urlFetchService) {
        this.urlFetchService = urlFetchService;
    }

    @Override // retrofit.client.Client
    public Response execute(Request request) throws IOException {
        HTTPRequest fetchRequest = createRequest(request);
        HTTPResponse fetchResponse = execute(this.urlFetchService, fetchRequest);
        return parseResponse(fetchResponse, fetchRequest);
    }

    protected HTTPResponse execute(URLFetchService urlFetchService, HTTPRequest request) throws IOException {
        return urlFetchService.fetch(request);
    }

    static HTTPRequest createRequest(Request request) throws IOException {
        HTTPMethod httpMethod = getHttpMethod(request.getMethod());
        URL url = new URL(request.getUrl());
        HTTPRequest fetchRequest = new HTTPRequest(url, httpMethod);
        for (Header header : request.getHeaders()) {
            fetchRequest.addHeader(new HTTPHeader(header.getName(), header.getValue()));
        }
        TypedOutput body = request.getBody();
        if (body != null) {
            String mimeType = body.mimeType();
            if (mimeType != null) {
                fetchRequest.addHeader(new HTTPHeader("Content-Type", mimeType));
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            body.writeTo(baos);
            fetchRequest.setPayload(baos.toByteArray());
        }
        return fetchRequest;
    }

    static Response parseResponse(HTTPResponse response, HTTPRequest creatingRequest) {
        URL responseUrl = response.getFinalUrl();
        if (responseUrl == null) {
            responseUrl = creatingRequest.getURL();
        }
        String urlString = responseUrl.toString();
        int status = response.getResponseCode();
        List<HTTPHeader> fetchHeaders = response.getHeaders();
        List<Header> headers = new ArrayList<>(fetchHeaders.size());
        String contentType = "application/octet-stream";
        for (HTTPHeader fetchHeader : fetchHeaders) {
            String name = fetchHeader.getName();
            String value = fetchHeader.getValue();
            if ("Content-Type".equalsIgnoreCase(name)) {
                contentType = value;
            }
            headers.add(new Header(name, value));
        }
        TypedByteArray body = null;
        byte[] fetchBody = response.getContent();
        if (fetchBody != null) {
            body = new TypedByteArray(contentType, fetchBody);
        }
        return new Response(urlString, status, "", headers, body);
    }
}
