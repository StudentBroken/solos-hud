package com.kopin.peloton;

import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class Failure {
    public boolean Success = true;
    public int ErrorCode = 0;
    public String ErrorMessage = "";

    public boolean isSuccess() {
        return this.Success;
    }

    public String toString() {
        return String.format(Locale.US, "success %b, error %d, error message %s", Boolean.valueOf(this.Success), Integer.valueOf(this.ErrorCode), this.ErrorMessage);
    }
}
