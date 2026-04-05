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
public class PageFollowRequestJsonWriter implements JsonWriter<PageFollow> {
    private Gson gson;

    public PageFollowRequestJsonWriter(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonWriter
    public void write(PageFollow pageFollow, OutputStream out) throws UaException {
        PageFollowRequestTransferObject pfrto = PageFollowRequestTransferObject.fromPageFollow(pageFollow);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        this.gson.toJson(pfrto, writer);
        try {
            writer.flush();
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            UaLog.error("Unable to flush PageFollowRequestJsonWriter during write.");
            throw new UaException(e2);
        }
    }
}
