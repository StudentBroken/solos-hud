package retrofit.client;

import java.io.IOException;

/* JADX INFO: loaded from: classes53.dex */
public interface Client {

    public interface Provider {
        Client get();
    }

    Response execute(Request request) throws IOException;
}
