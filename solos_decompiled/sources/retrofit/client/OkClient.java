package retrofit.client;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes53.dex */
public class OkClient extends UrlConnectionClient {
    private final OkUrlFactory okUrlFactory;

    private static OkHttpClient generateDefaultOkHttp() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15000L, TimeUnit.MILLISECONDS);
        client.setReadTimeout(20000L, TimeUnit.MILLISECONDS);
        return client;
    }

    public OkClient() {
        this(generateDefaultOkHttp());
    }

    public OkClient(OkHttpClient client) {
        this.okUrlFactory = new OkUrlFactory(client);
    }

    @Override // retrofit.client.UrlConnectionClient
    protected HttpURLConnection openConnection(Request request) throws IOException {
        return this.okUrlFactory.open(new URL(request.getUrl()));
    }
}
