package com.ua.sdk.suggestedfriends;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.net.UrlBuilder;
import java.net.URL;

/* JADX INFO: loaded from: classes65.dex */
public class SuggestedFriendsService extends AbstractResourceService<SuggestedFriends> {
    public SuggestedFriendsService(ConnectionFactory connectionFactory, AuthenticationManager authenticationManager, UrlBuilder urlBuilder, JsonParser<? extends EntityList<SuggestedFriends>> suggestedFriendsPageParser, JsonParser<SuggestedFriends> suggestedFriendsParser) {
        super(connectionFactory, authenticationManager, urlBuilder, suggestedFriendsParser, null, suggestedFriendsPageParser);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        throw new UnsupportedOperationException("Create SuggestedFriends is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        throw new UnsupportedOperationException("Fetch SuggestedFriends is unsupported.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(SuggestedFriends resource) {
        throw new UnsupportedOperationException("Save SuggestedFriends is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        throw new UnsupportedOperationException("Save SuggestedFriends is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<SuggestedFriends> ref) {
        URL url = this.urlBuilder.buildGetSuggestedFriendsUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Patch SuggestedFriends is unsupported.");
    }
}
