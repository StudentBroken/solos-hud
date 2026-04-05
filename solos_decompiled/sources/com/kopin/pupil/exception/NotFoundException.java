package com.kopin.pupil.exception;

import android.os.RemoteException;

/* JADX INFO: loaded from: classes25.dex */
public class NotFoundException extends RemoteException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
