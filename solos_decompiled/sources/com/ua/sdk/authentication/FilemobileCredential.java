package com.ua.sdk.authentication;

import android.net.Uri;
import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface FilemobileCredential extends Entity<EntityRef> {
    Date getCreated();

    String getToken();

    String getUid();

    Uri getUploaderUri();

    String getVhost();

    void setCreated(Date date);

    void setToken(String str);

    void setUid(String str);

    void setUploaderUri(Uri uri);

    void setVhost(String str);
}
