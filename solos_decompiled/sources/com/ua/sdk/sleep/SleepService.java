package com.ua.sdk.sleep;

import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.JsonWriter;
import com.ua.sdk.internal.net.UrlBuilder;
import java.net.URL;

/* JADX INFO: loaded from: classes65.dex */
public class SleepService extends AbstractResourceService<SleepMetric> {
    public SleepService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<SleepMetric> jsonParser, JsonWriter<SleepMetric> jsonWriter, JsonParser<SleepMetricList> jsonPageParser) {
        super(connFactory, authManager, urlBuilder, jsonParser, jsonWriter, jsonPageParser);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        throw new UnsupportedOperationException("Create Sleep is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        return this.urlBuilder.buildGetSleepUrl(ref);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(SleepMetric resource) {
        return this.urlBuilder.buildSaveSleepUrl(resource.getRef());
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        return this.urlBuilder.buildGetSleepUrl(ref);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Patch Sleep is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<SleepMetric> ref) {
        return this.urlBuilder.buildGetSleepListUrl(ref);
    }
}
