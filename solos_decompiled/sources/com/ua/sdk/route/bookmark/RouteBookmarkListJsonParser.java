package com.ua.sdk.route.bookmark;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.EntityList;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.route.RouteBookmark;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class RouteBookmarkListJsonParser implements JsonParser<EntityList<RouteBookmark>> {
    private Gson gson;

    public RouteBookmarkListJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public EntityList<RouteBookmark> parse(InputStream inputStream) throws UaException {
        RouteBookmarkListTO to = (RouteBookmarkListTO) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), RouteBookmarkListTO.class);
        RouteBookmarkList list = RouteBookmarkListTO.fromTransferObject(to);
        return list;
    }
}
