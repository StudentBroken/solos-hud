package com.ua.sdk.actigraphysettings;

import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.net.UrlBuilder;
import java.net.URL;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphySettingsService extends AbstractResourceService<ActigraphySettings> {
    public ActigraphySettingsService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, ActigraphySettingsJsonParser actigraphySettingsParser, ActigraphySettingsRequestWriter actigraphySettingsRequestWriter) {
        super(connFactory, authManager, urlBuilder, actigraphySettingsParser, actigraphySettingsRequestWriter, null);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        URL url = this.urlBuilder.buildGetActigraphySettingsUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        URL url = this.urlBuilder.buildGetActigraphyRecorderPriorityUrl();
        return url;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(ActigraphySettings resource) {
        throw new UnsupportedOperationException("Save ActigraphySettings is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        throw new UnsupportedOperationException("Delete ActigraphySettings is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<ActigraphySettings> ref) {
        throw new UnsupportedOperationException("Fetch ActigraphySettings page is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Patch ActigraphySettings is unsupported.");
    }
}
