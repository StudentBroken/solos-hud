package com.ua.sdk.heartrate;

import com.ua.sdk.EntityList;
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
public class HeartRateZonesService extends AbstractResourceService<HeartRateZones> {
    public HeartRateZonesService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<HeartRateZones> jsonParser, JsonWriter<HeartRateZones> jsonWriter, JsonParser<? extends EntityList<HeartRateZones>> jsonPageParser) {
        super(connFactory, authManager, urlBuilder, jsonParser, jsonWriter, jsonPageParser);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        return this.urlBuilder.buildCreateHeartRateZonesUrl();
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        return this.urlBuilder.buildFetchHeartRateZonesUrl(ref);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(HeartRateZones resource) {
        throw new UnsupportedOperationException("Heart Rate Zones cannot be saved");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        throw new UnsupportedOperationException("Heart Rate Zones cannot be deleted");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Heart Rate Zones cannot be patched");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<HeartRateZones> ref) {
        return this.urlBuilder.buildFetchHeartRateZonesListUrl(ref);
    }
}
