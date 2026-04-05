package com.ua.sdk.user;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.internal.JsonWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class UserJsonWriter implements JsonWriter<User> {
    private Gson gson;

    public UserJsonWriter(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonWriter
    public void write(User user, OutputStream out) throws UaException {
        UserTO uto = UserTO.toTransferObject(user);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        this.gson.toJson(uto, writer);
        try {
            writer.flush();
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            UaLog.error("Unable to flush UserJsonWriter during write.");
            throw new UaException(e2);
        }
    }
}
