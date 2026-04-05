package com.ua.sdk.activitytimeseries;

import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.JsonWriter;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.net.URL;
import java.util.concurrent.Callable;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTimeSeriesService extends AbstractResourceService<ActivityTimeSeries> {
    public ActivityTimeSeriesService(ConnectionFactory connectionFactory, AuthenticationManager authenticationManager, UrlBuilder urlBuilder, JsonWriter<ActivityTimeSeries> activityTimeSeriesJsonWriter, JsonParser<ActivityTimeSeries> activityTimeSeriesJsonParser) {
        super(connectionFactory, authenticationManager, urlBuilder, activityTimeSeriesJsonParser, activityTimeSeriesJsonWriter, null);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        URL url = this.urlBuilder.buildCreateActivityTimeSeriesUrl();
        return url;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public Callable<ActivityTimeSeries> getCreateCallable(final ActivityTimeSeries resource) throws UaException {
        return new Callable<ActivityTimeSeries>() { // from class: com.ua.sdk.activitytimeseries.ActivityTimeSeriesService.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ActivityTimeSeries call() throws Exception {
                URL url = ActivityTimeSeriesService.this.getCreateUrl();
                HttpsURLConnection conn = ActivityTimeSeriesService.this.connFactory.getSslConnection(url);
                Precondition.isAuthenticated(ActivityTimeSeriesService.this.authManager);
                try {
                    ActivityTimeSeriesService.this.authManager.sign(conn, ActivityTimeSeriesService.this.getCreateAuthenticationType());
                    conn.setRequestMethod(HttpRequest.METHOD_PUT);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    ActivityTimeSeriesService.this.jsonWriter.write(resource, conn.getOutputStream());
                    Precondition.isExpectedResponse(conn, 204);
                    return resource;
                } finally {
                    conn.disconnect();
                }
            }
        };
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        throw new UnsupportedOperationException("Fetch ActivityTimeSeries is unsupported.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(ActivityTimeSeries resource) {
        throw new UnsupportedOperationException("Save ActivityTimeSeries is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        throw new UnsupportedOperationException("Delete ActivityTimeSeries is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Patch ActivityTimeSeries is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<ActivityTimeSeries> ref) {
        throw new UnsupportedOperationException("Fetch ActivityTimeSeries Page is unsupported.");
    }
}
