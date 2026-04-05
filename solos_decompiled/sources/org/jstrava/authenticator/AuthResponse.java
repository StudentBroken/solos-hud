package org.jstrava.authenticator;

import org.jstrava.entities.athlete.Athlete;

/* JADX INFO: loaded from: classes68.dex */
public class AuthResponse {
    String access_token;
    Athlete athlete;

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String token) {
        this.access_token = token;
    }

    public Athlete getAthlete() {
        return this.athlete;
    }

    public void setAthlete() {
        this.athlete = this.athlete;
    }
}
