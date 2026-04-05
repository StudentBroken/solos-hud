package com.ua.sdk.authentication;

import com.ua.sdk.UaException;
import com.ua.sdk.authentication.InternalTokenCredentialManager;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class InternalTokenCredentialManagerImpl implements InternalTokenCredentialManager {
    private final ExecutorService executor;
    private final InternalTokenCredentialService service;

    public InternalTokenCredentialManagerImpl(InternalTokenCredentialService service, ExecutorService executor) {
        this.service = service;
        this.executor = executor;
    }

    @Override // com.ua.sdk.authentication.InternalTokenCredentialManager
    public OAuth2Credentials createForLogin(InternalTokenCredentialManager.TokenType tokenType, String token, String tokenSecret) throws UaException {
        InternalTokenCredential internalTokenCredential = new InternalTokenCredentialImpl();
        internalTokenCredential.setTokenType(tokenType.toString());
        internalTokenCredential.setToken(token);
        internalTokenCredential.setSecret(tokenSecret);
        OAuth2Credentials credentials = this.service.save(internalTokenCredential, false);
        return credentials;
    }

    @Override // com.ua.sdk.authentication.InternalTokenCredentialManager
    public OAuth2Credentials updateForSync(InternalTokenCredentialManager.TokenType tokenType, String token, String tokenSecret) throws UaException {
        InternalTokenCredential internalTokenCredential = new InternalTokenCredentialImpl();
        internalTokenCredential.setTokenType(tokenType.toString());
        internalTokenCredential.setToken(token);
        internalTokenCredential.setSecret(tokenSecret);
        OAuth2Credentials credentials = this.service.save(internalTokenCredential, true);
        return credentials;
    }
}
