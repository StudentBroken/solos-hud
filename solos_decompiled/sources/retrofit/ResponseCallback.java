package retrofit;

import retrofit.client.Response;

/* JADX INFO: loaded from: classes53.dex */
public abstract class ResponseCallback implements Callback<Response> {
    public abstract void success(Response response);

    @Override // retrofit.Callback
    public void success(Response response, Response response2) {
        success(response);
    }
}
