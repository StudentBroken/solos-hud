package com.kopin.peloton;

import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class Token {
    public String access_token = "";
    public String token_type = "";
    public long expires_in = 0;
    public String refresh_token = "";

    public String toString() {
        return String.format(Locale.US, "access token %s\ntype %s, refresh token %s", this.access_token, this.token_type, this.refresh_token);
    }
}
