package com.ua.sdk.page.follow;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.internal.JsonWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowJsonWriter implements JsonWriter<PageFollow> {
    private Gson gson;

    public PageFollowJsonWriter(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonWriter
    public void write(PageFollow pageFollow, OutputStream out) throws UaException {
        PageFollowTransferObject pfto = PageFollowTransferObject.fromPageFollow(pageFollow);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        this.gson.toJson(pfto, writer);
        try {
            writer.flush();
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            UaLog.error("Unable to flush PageFollowJsonWriter during write.");
            throw new UaException(e2);
        }
    }
}
