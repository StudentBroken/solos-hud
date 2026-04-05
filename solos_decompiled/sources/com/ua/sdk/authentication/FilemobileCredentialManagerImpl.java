package com.ua.sdk.authentication;

import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public class FilemobileCredentialManagerImpl implements FilemobileCredentialManager {
    private final FilemobileCredentialService service;

    public FilemobileCredentialManagerImpl(FilemobileCredentialService service) {
        this.service = service;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredentialManager
    public FilemobileCredential getFilemobileTokenCredentials() throws UaException {
        return this.service.fetchCredentials();
    }
}
