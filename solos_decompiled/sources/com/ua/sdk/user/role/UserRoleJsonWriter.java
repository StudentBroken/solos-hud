package com.ua.sdk.user.role;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.internal.AbstractGsonWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class UserRoleJsonWriter extends AbstractGsonWriter<UserRole> {
    public UserRoleJsonWriter(Gson gson) {
        super(gson);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(UserRole entity, Gson gson, OutputStreamWriter writer) throws UaException {
        UserRoleTO to = UserRoleTO.toTransferObject(entity);
        gson.toJson(to, writer);
        try {
            writer.flush();
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            UaLog.error("Unable to flush UserRoleJsonWriter during write.");
            throw new UaException(e2);
        }
    }
}
