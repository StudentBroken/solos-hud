package com.google.maps.android.kml;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: loaded from: classes69.dex */
public class KmlLayer {
    private final KmlRenderer mRenderer;

    public KmlLayer(GoogleMap map, int resourceId, Context context) throws XmlPullParserException, IOException {
        this(map, context.getResources().openRawResource(resourceId), context);
    }

    public KmlLayer(GoogleMap map, InputStream stream, Context context) throws XmlPullParserException, IOException {
        if (stream == null) {
            throw new IllegalArgumentException("KML InputStream cannot be null");
        }
        this.mRenderer = new KmlRenderer(map, context);
        XmlPullParser xmlPullParser = createXmlParser(stream);
        KmlParser parser = new KmlParser(xmlPullParser);
        parser.parseKml();
        stream.close();
        this.mRenderer.storeKmlData(parser.getStyles(), parser.getStyleMaps(), parser.getPlacemarks(), parser.getContainers(), parser.getGroundOverlays());
    }

    private static XmlPullParser createXmlParser(InputStream stream) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(stream, null);
        return parser;
    }

    public void addLayerToMap() throws XmlPullParserException, IOException {
        this.mRenderer.addLayerToMap();
    }

    public void removeLayerFromMap() {
        this.mRenderer.removeLayerFromMap();
    }

    public boolean hasPlacemarks() {
        return this.mRenderer.hasKmlPlacemarks();
    }

    public Iterable<KmlPlacemark> getPlacemarks() {
        return this.mRenderer.getKmlPlacemarks();
    }

    public boolean hasContainers() {
        return this.mRenderer.hasNestedContainers();
    }

    public Iterable<KmlContainer> getContainers() {
        return this.mRenderer.getNestedContainers();
    }

    public Iterable<KmlGroundOverlay> getGroundOverlays() {
        return this.mRenderer.getGroundOverlays();
    }

    public GoogleMap getMap() {
        return this.mRenderer.getMap();
    }

    public void setMap(GoogleMap map) {
        this.mRenderer.setMap(map);
    }
}
