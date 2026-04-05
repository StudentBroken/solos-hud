package com.ua.sdk.page.follow;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.JsonWriter;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.InterruptedIOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowService extends AbstractResourceService<PageFollow> {
    public PageFollowService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<PageFollow> pageFollowParser, JsonParser<EntityList<PageFollow>> pageFollowPageParser, JsonWriter<PageFollow> pageFollowRequestWriter) {
        super(connFactory, authManager, urlBuilder, pageFollowParser, pageFollowRequestWriter, pageFollowPageParser);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        URL url = this.urlBuilder.buildCreatePageFollowUrl();
        return url;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(PageFollow p) {
        throw new UnsupportedOperationException("Update PageFollow is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        URL url = this.urlBuilder.buildDeletePageFollowUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        URL url = this.urlBuilder.buildGetPageFollowUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<PageFollow> ref) {
        URL url = this.urlBuilder.buildGetPageFollowPageUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        return this.urlBuilder.buildPatchPageFollowUrl(ref);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    protected EntityList<PageFollow> patch(PageFollow pageFollow) throws UaException {
        Precondition.isNotNull(this.jsonWriter, "jsonWriter");
        Precondition.isNotNull(this.jsonPageParser, "jsonPageParser");
        try {
            HttpsURLConnection sslConnection = this.connFactory.getSslConnection(getPatchUrl(null));
            try {
                this.authManager.sign(sslConnection, getPatchAuthenticationType());
                sslConnection.setRequestMethod(HttpRequest.METHOD_POST);
                sslConnection.setDoOutput(true);
                sslConnection.setUseCaches(false);
                sslConnection.addRequestProperty("X-HTTP-Method-Override", "PATCH");
                this.jsonWriter.write(pageFollow, sslConnection.getOutputStream());
                Precondition.isResponseSuccess(sslConnection);
                return (EntityList) this.jsonPageParser.parse(sslConnection.getInputStream());
            } finally {
                sslConnection.disconnect();
            }
        } catch (InterruptedIOException e) {
            UaLog.debug("Multi patch cancelled.");
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (Throwable th) {
            UaLog.error("Unable to multi patch page follows.", th);
            throw new UaException("Unable to multi patch page follows.", th);
        }
    }
}
