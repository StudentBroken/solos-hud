package com.ua.sdk.activitystory;

import com.ua.sdk.EntityList;
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
public class ActivityStoryService extends AbstractResourceService<ActivityStory> {
    public ActivityStoryService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<? extends EntityList<ActivityStory>> activityStoryPageParser, JsonParser<ActivityStory> activityStoryParser, JsonWriter<ActivityStory> activityStoryWriter) {
        super(connFactory, authManager, urlBuilder, activityStoryParser, activityStoryWriter, activityStoryPageParser);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public Callable<ActivityStory> getSaveCallable(final ActivityStory resource) throws UaException {
        return new Callable<ActivityStory>() { // from class: com.ua.sdk.activitystory.ActivityStoryService.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ActivityStory call() throws Exception {
                URL url = ActivityStoryService.this.getSaveUrl(resource);
                HttpsURLConnection conn = ActivityStoryService.this.connFactory.getSslConnection(url);
                try {
                    ActivityStoryService.this.authManager.sign(conn, ActivityStoryService.this.getSaveAuthenticationType());
                    conn.setRequestMethod(HttpRequest.METHOD_POST);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    ActivityStoryService.this.jsonWriter.write(resource, conn.getOutputStream());
                    Precondition.isExpectedResponse(conn, 204);
                    return resource;
                } finally {
                    conn.disconnect();
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(ActivityStory resource) {
        URL url = this.urlBuilder.buildRpcPatchActivityStoryUrl(resource.getRef());
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        URL url = this.urlBuilder.buildGetActivityStoryUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        URL url = this.urlBuilder.buildCreateActivityStoryUrl();
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<ActivityStory> ref) {
        URL url = this.urlBuilder.buildGetActivityFeedUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        URL url = this.urlBuilder.buildGetActivityStoryUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        URL url = this.urlBuilder.buildGetActivityStoryUrl(ref);
        return url;
    }
}
