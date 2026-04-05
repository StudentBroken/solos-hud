package retrofit.android;

import android.util.Log;
import retrofit.RestAdapter;

/* JADX INFO: loaded from: classes53.dex */
public class AndroidLog implements RestAdapter.Log {
    private static final int LOG_CHUNK_SIZE = 4000;
    private final String tag;

    public AndroidLog(String tag) {
        this.tag = tag;
    }

    @Override // retrofit.RestAdapter.Log
    public final void log(String message) {
        int len = message.length();
        for (int i = 0; i < len; i += 4000) {
            int end = Math.min(len, i + 4000);
            logChunk(message.substring(i, end));
        }
    }

    public void logChunk(String chunk) {
        Log.d(getTag(), chunk);
    }

    public String getTag() {
        return this.tag;
    }
}
