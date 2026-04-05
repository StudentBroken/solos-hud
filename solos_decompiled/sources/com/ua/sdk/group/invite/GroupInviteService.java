package com.ua.sdk.group.invite;

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
public class GroupInviteService extends AbstractResourceService<GroupInvite> {
    public GroupInviteService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<GroupInvite> jsonParser, JsonWriter<GroupInvite> jsonWriter, JsonParser<? extends EntityList<GroupInvite>> jsonPageParser) {
        super(connFactory, authManager, urlBuilder, jsonParser, jsonWriter, jsonPageParser);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        return this.urlBuilder.buildCreateGroupInviteUrl();
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(GroupInvite resource) {
        throw new UnsupportedOperationException();
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        return this.urlBuilder.buildDeleteGroupInviteUrl(ref);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        return this.urlBuilder.buildPatchGroupInviteUrl(ref);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<GroupInvite> ref) {
        return this.urlBuilder.buildGetGroupInviteUrl(ref);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    protected EntityList<GroupInvite> patch(GroupInvite groupInvite) throws UaException {
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
                this.jsonWriter.write(groupInvite, sslConnection.getOutputStream());
                Precondition.isResponseSuccess(sslConnection);
                return (EntityList) this.jsonPageParser.parse(sslConnection.getInputStream());
            } finally {
                sslConnection.disconnect();
            }
        } catch (InterruptedIOException e) {
            UaLog.debug("Multi patch cancelled.");
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (Throwable th) {
            UaLog.error("Unable to multi patch group invites.", th);
            throw new UaException("Unable to multi patch group invites.", th);
        }
    }
}
