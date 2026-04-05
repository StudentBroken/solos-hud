package com.ua.sdk.route.bookmark;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.route.RouteBookmark;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class RouteBookmarkJsonParser implements JsonParser<RouteBookmark> {
    Gson gson;

    public RouteBookmarkJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public RouteBookmark parse(InputStream inputStream) throws UaException {
        RouteBookmarkTO to = (RouteBookmarkTO) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), RouteBookmarkTO.class);
        return RouteBookmarkTO.fromTransferObject(to);
    }
}
