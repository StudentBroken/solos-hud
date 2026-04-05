package com.kopin.pupil.ui;

/* JADX INFO: loaded from: classes25.dex */
public class VCToast {
    public static final int BORDER = 4;
    public static final int PADDING_HEIGHT = 10;
    public static final int PADDING_WIDTH = 24;
    private String mMessage;

    public VCToast(String message) {
        this.mMessage = null;
        this.mMessage = message;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
}
