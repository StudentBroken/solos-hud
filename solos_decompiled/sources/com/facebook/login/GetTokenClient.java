package com.facebook.login;

import android.content.Context;
import android.os.Bundle;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.PlatformServiceClient;

/* JADX INFO: loaded from: classes41.dex */
final class GetTokenClient extends PlatformServiceClient {
    GetTokenClient(Context context, String applicationId) {
        super(context, 65536, NativeProtocol.MESSAGE_GET_ACCESS_TOKEN_REPLY, NativeProtocol.PROTOCOL_VERSION_20121101, applicationId);
    }

    @Override // com.facebook.internal.PlatformServiceClient
    protected void populateRequestBundle(Bundle data) {
    }
}
