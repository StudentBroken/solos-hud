package com.ua.sdk.gear.brand;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class GearBrandService implements EntityService<GearBrand> {
    private final AuthenticationManager authenticationManager;
    private final ConnectionFactory connectionFactory;
    private final JsonParser<EntityList<GearBrand>> listJsonParser;
    private final UrlBuilder urlBuilder;

    public GearBrandService(ConnectionFactory connectionFactory, AuthenticationManager authenticationManager, UrlBuilder urlBuilder, JsonParser<EntityList<GearBrand>> listJsonParser) {
        this.connectionFactory = connectionFactory;
        this.authenticationManager = authenticationManager;
        this.urlBuilder = urlBuilder;
        this.listJsonParser = listJsonParser;
    }

    @Override // com.ua.sdk.internal.EntityService
    public GearBrand create(GearBrand entity) throws UaException {
        return null;
    }

    @Override // com.ua.sdk.internal.EntityService
    public GearBrand fetch(Reference ref) throws UaException {
        return null;
    }

    @Override // com.ua.sdk.internal.EntityService
    public GearBrand save(GearBrand entity) throws UaException {
        return null;
    }

    @Override // com.ua.sdk.internal.EntityService
    public void delete(Reference ref) throws UaException {
    }

    @Override // com.ua.sdk.internal.EntityService
    public GearBrand patch(GearBrand entity, Reference ref) throws UaException {
        return null;
    }

    @Override // com.ua.sdk.internal.EntityService
    public EntityList<GearBrand> fetchPage(EntityListRef<GearBrand> ref) throws UaException {
        try {
            if (ref != null) {
                Precondition.isAuthenticated(this.authenticationManager);
                URL url = this.urlBuilder.buildGetGearBrandUrl(ref);
                HttpsURLConnection conn = this.connectionFactory.getSslConnection(url);
                try {
                    this.authenticationManager.signAsUser(conn);
                    conn.setRequestMethod(HttpRequest.METHOD_GET);
                    conn.setDoOutput(false);
                    conn.setUseCaches(false);
                    Precondition.isResponseHttpOk(conn);
                    return this.listJsonParser.parse(conn.getInputStream());
                } finally {
                    conn.disconnect();
                }
            }
            throw new UaException("The ref must not be null!");
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN, e2);
        }
    }
}
