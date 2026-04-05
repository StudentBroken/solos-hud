package retrofit.android;

import android.net.http.AndroidHttpClient;
import retrofit.client.ApacheClient;

/* JADX INFO: loaded from: classes53.dex */
public final class AndroidApacheClient extends ApacheClient {
    public AndroidApacheClient() {
        super(AndroidHttpClient.newInstance("Retrofit"));
    }
}
