package com.google.maps.android.kml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.R;
import com.kopin.pupil.ui.PageHelper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes69.dex */
class KmlRenderer {
    private static final String LOG_TAG = "KmlRenderer";
    private static final int LRU_CACHE_SIZE = 50;
    private ArrayList<KmlContainer> mContainers;
    private Context mContext;
    private HashMap<KmlGroundOverlay, GroundOverlay> mGroundOverlays;
    private GoogleMap mMap;
    private HashMap<KmlPlacemark, Object> mPlacemarks;
    private HashMap<String, String> mStyleMaps;
    private HashMap<String, KmlStyle> mStyles;
    private final LruCache<String, Bitmap> mImagesCache = new LruCache<>(50);
    private final ArrayList<String> mMarkerIconUrls = new ArrayList<>();
    private final ArrayList<String> mGroundOverlayUrls = new ArrayList<>();
    private HashMap<String, KmlStyle> mStylesRenderer = new HashMap<>();
    private boolean mLayerVisible = false;
    private boolean mMarkerIconsDownloaded = false;
    private boolean mGroundOverlayImagesDownloaded = false;

    KmlRenderer(GoogleMap map, Context context) {
        this.mContext = context;
        this.mMap = map;
    }

    private static boolean getPlacemarkVisibility(KmlPlacemark placemark) {
        if (!placemark.hasProperty("visibility")) {
            return true;
        }
        String placemarkVisibility = placemark.getProperty("visibility");
        if (Integer.parseInt(placemarkVisibility) != 0) {
            return true;
        }
        return false;
    }

    private static BitmapDescriptor scaleIcon(Bitmap unscaledBitmap, Double scale) {
        int width = (int) (((double) unscaledBitmap.getWidth()) * scale.doubleValue());
        int height = (int) (((double) unscaledBitmap.getHeight()) * scale.doubleValue());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(unscaledBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap);
    }

    private static void removePlacemarks(HashMap<KmlPlacemark, Object> placemarks) {
        for (Object mapObject : placemarks.values()) {
            if (mapObject instanceof Marker) {
                ((Marker) mapObject).remove();
            } else if (mapObject instanceof Polyline) {
                ((Polyline) mapObject).remove();
            } else if (mapObject instanceof Polygon) {
                ((Polygon) mapObject).remove();
            }
        }
    }

    static boolean getContainerVisibility(KmlContainer kmlContainer, boolean isParentContainerVisible) {
        boolean isChildContainerVisible = true;
        if (kmlContainer.hasProperty("visibility")) {
            String placemarkVisibility = kmlContainer.getProperty("visibility");
            if (Integer.parseInt(placemarkVisibility) == 0) {
                isChildContainerVisible = false;
            }
        }
        return isParentContainerVisible && isChildContainerVisible;
    }

    private void removeGroundOverlays(HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays) {
        for (GroundOverlay groundOverlay : groundOverlays.values()) {
            groundOverlay.remove();
        }
    }

    private void removeContainers(Iterable<KmlContainer> containers) {
        for (KmlContainer container : containers) {
            removePlacemarks(container.getPlacemarksHashMap());
            removeGroundOverlays(container.getGroundOverlayHashMap());
            removeContainers(container.getContainers());
        }
    }

    void assignStyleMap(HashMap<String, String> styleMap, HashMap<String, KmlStyle> styles) {
        for (String styleMapKey : styleMap.keySet()) {
            String styleMapValue = styleMap.get(styleMapKey);
            if (styles.containsKey(styleMapValue)) {
                styles.put(styleMapKey, styles.get(styleMapValue));
            }
        }
    }

    void storeKmlData(HashMap<String, KmlStyle> styles, HashMap<String, String> styleMaps, HashMap<KmlPlacemark, Object> placemarks, ArrayList<KmlContainer> folders, HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays) {
        this.mStyles = styles;
        this.mStyleMaps = styleMaps;
        this.mPlacemarks = placemarks;
        this.mContainers = folders;
        this.mGroundOverlays = groundOverlays;
    }

    void addLayerToMap() {
        this.mStylesRenderer.putAll(this.mStyles);
        assignStyleMap(this.mStyleMaps, this.mStylesRenderer);
        addGroundOverlays(this.mGroundOverlays, this.mContainers);
        addContainerGroupToMap(this.mContainers, true);
        addPlacemarksToMap(this.mPlacemarks);
        if (!this.mGroundOverlayImagesDownloaded) {
            downloadGroundOverlays();
        }
        if (!this.mMarkerIconsDownloaded) {
            downloadMarkerIcons();
        }
        this.mLayerVisible = true;
    }

    GoogleMap getMap() {
        return this.mMap;
    }

    void setMap(GoogleMap map) {
        removeLayerFromMap();
        this.mMap = map;
        addLayerToMap();
    }

    boolean hasKmlPlacemarks() {
        return this.mPlacemarks.size() > 0;
    }

    Iterable<KmlPlacemark> getKmlPlacemarks() {
        return this.mPlacemarks.keySet();
    }

    boolean hasNestedContainers() {
        return this.mContainers.size() > 0;
    }

    Iterable<KmlContainer> getNestedContainers() {
        return this.mContainers;
    }

    Iterable<KmlGroundOverlay> getGroundOverlays() {
        return this.mGroundOverlays.keySet();
    }

    void removeLayerFromMap() {
        removePlacemarks(this.mPlacemarks);
        removeGroundOverlays(this.mGroundOverlays);
        if (hasNestedContainers()) {
            removeContainers(getNestedContainers());
        }
        this.mLayerVisible = false;
        this.mStylesRenderer.clear();
    }

    private void addPlacemarksToMap(HashMap<KmlPlacemark, Object> placemarks) {
        for (KmlPlacemark kmlPlacemark : placemarks.keySet()) {
            boolean isPlacemarkVisible = getPlacemarkVisibility(kmlPlacemark);
            Object mapObject = addPlacemarkToMap(kmlPlacemark, isPlacemarkVisible);
            placemarks.put(kmlPlacemark, mapObject);
        }
    }

    private Object addPlacemarkToMap(KmlPlacemark placemark, boolean placemarkVisibility) {
        if (placemark.getGeometry() == null) {
            return null;
        }
        String placemarkId = placemark.getStyleId();
        KmlGeometry geometry = placemark.getGeometry();
        KmlStyle style = getPlacemarkStyle(placemarkId);
        KmlStyle inlineStyle = placemark.getInlineStyle();
        return addToMap(placemark, geometry, style, inlineStyle, placemarkVisibility);
    }

    private void addContainerGroupToMap(Iterable<KmlContainer> kmlContainers, boolean containerVisibility) {
        for (KmlContainer container : kmlContainers) {
            boolean isContainerVisible = getContainerVisibility(container, containerVisibility);
            if (container.getStyles() != null) {
                this.mStylesRenderer.putAll(container.getStyles());
            }
            if (container.getStyleMap() != null) {
                assignStyleMap(container.getStyleMap(), this.mStylesRenderer);
            }
            addContainerObjectToMap(container, isContainerVisible);
            if (container.hasContainers()) {
                addContainerGroupToMap(container.getContainers(), isContainerVisible);
            }
        }
    }

    private void addContainerObjectToMap(KmlContainer kmlContainer, boolean isContainerVisible) {
        for (KmlPlacemark placemark : kmlContainer.getPlacemarks()) {
            boolean isPlacemarkVisible = getPlacemarkVisibility(placemark);
            boolean isObjectVisible = isContainerVisible && isPlacemarkVisible;
            Object mapObject = addPlacemarkToMap(placemark, isObjectVisible);
            kmlContainer.setPlacemark(placemark, mapObject);
        }
    }

    private KmlStyle getPlacemarkStyle(String styleId) {
        KmlStyle style = this.mStylesRenderer.get(null);
        if (this.mStylesRenderer.get(styleId) != null) {
            KmlStyle style2 = this.mStylesRenderer.get(styleId);
            return style2;
        }
        return style;
    }

    private void addMarkerIcons(String styleUrl, MarkerOptions markerOptions) {
        if (this.mImagesCache.get(styleUrl) != null) {
            Bitmap bitmap = this.mImagesCache.get(styleUrl);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        } else if (!this.mMarkerIconUrls.contains(styleUrl)) {
            this.mMarkerIconUrls.add(styleUrl);
        }
    }

    private void downloadMarkerIcons() {
        this.mMarkerIconsDownloaded = true;
        Iterator<String> iterator = this.mMarkerIconUrls.iterator();
        while (iterator.hasNext()) {
            String markerIconUrl = iterator.next();
            new MarkerIconImageDownload(markerIconUrl).execute(new String[0]);
            iterator.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addIconToMarkers(String iconUrl, HashMap<KmlPlacemark, Object> placemarks) {
        for (KmlPlacemark placemark : placemarks.keySet()) {
            KmlStyle urlStyle = this.mStylesRenderer.get(placemark.getStyleId());
            KmlStyle inlineStyle = placemark.getInlineStyle();
            if (KmlPoint.GEOMETRY_TYPE.equals(placemark.getGeometry().getGeometryType())) {
                boolean isInlineStyleIcon = inlineStyle != null && iconUrl.equals(inlineStyle.getIconUrl());
                boolean isPlacemarkStyleIcon = urlStyle != null && iconUrl.equals(urlStyle.getIconUrl());
                if (isInlineStyleIcon) {
                    scaleBitmap(inlineStyle, placemarks, placemark);
                } else if (isPlacemarkStyleIcon) {
                    scaleBitmap(urlStyle, placemarks, placemark);
                }
            }
        }
    }

    private void scaleBitmap(KmlStyle style, HashMap<KmlPlacemark, Object> placemarks, KmlPlacemark placemark) {
        double bitmapScale = style.getIconScale();
        String bitmapUrl = style.getIconUrl();
        Bitmap bitmapImage = this.mImagesCache.get(bitmapUrl);
        BitmapDescriptor scaledBitmap = scaleIcon(bitmapImage, Double.valueOf(bitmapScale));
        ((Marker) placemarks.get(placemark)).setIcon(scaledBitmap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addContainerGroupIconsToMarkers(String iconUrl, Iterable<KmlContainer> kmlContainers) {
        for (KmlContainer container : kmlContainers) {
            addIconToMarkers(iconUrl, container.getPlacemarksHashMap());
            if (container.hasContainers()) {
                addContainerGroupIconsToMarkers(iconUrl, container.getContainers());
            }
        }
    }

    private Object addToMap(KmlPlacemark placemark, KmlGeometry geometry, KmlStyle style, KmlStyle inlineStyle, boolean isVisible) {
        String geometryType = geometry.getGeometryType();
        boolean hasDrawOrder = placemark.hasProperty("drawOrder");
        float drawOrder = 0.0f;
        if (hasDrawOrder) {
            try {
                drawOrder = Float.parseFloat(placemark.getProperty("drawOrder"));
            } catch (NumberFormatException e) {
                hasDrawOrder = false;
            }
        }
        if (geometryType.equals(KmlPoint.GEOMETRY_TYPE)) {
            Marker marker = addPointToMap(placemark, (KmlPoint) geometry, style, inlineStyle);
            marker.setVisible(isVisible);
            if (hasDrawOrder) {
                marker.setZIndex(drawOrder);
                return marker;
            }
            return marker;
        }
        if (geometryType.equals(KmlLineString.GEOMETRY_TYPE)) {
            Polyline polyline = addLineStringToMap((KmlLineString) geometry, style, inlineStyle);
            polyline.setVisible(isVisible);
            if (hasDrawOrder) {
                polyline.setZIndex(drawOrder);
            }
            return polyline;
        }
        if (geometryType.equals(KmlPolygon.GEOMETRY_TYPE)) {
            Polygon polygon = addPolygonToMap((KmlPolygon) geometry, style, inlineStyle);
            polygon.setVisible(isVisible);
            if (hasDrawOrder) {
                polygon.setZIndex(drawOrder);
            }
            return polygon;
        }
        if (geometryType.equals("MultiGeometry")) {
            return addMultiGeometryToMap(placemark, (KmlMultiGeometry) geometry, style, inlineStyle, isVisible);
        }
        return null;
    }

    private Marker addPointToMap(KmlPlacemark placemark, KmlPoint point, KmlStyle style, KmlStyle markerInlineStyle) {
        MarkerOptions markerUrlStyle = style.getMarkerOptions();
        markerUrlStyle.position(point.getGeometryObject());
        if (markerInlineStyle != null) {
            setInlinePointStyle(markerUrlStyle, markerInlineStyle, style.getIconUrl());
        } else if (style.getIconUrl() != null) {
            addMarkerIcons(style.getIconUrl(), markerUrlStyle);
        }
        Marker marker = this.mMap.addMarker(markerUrlStyle);
        setMarkerInfoWindow(style, marker, placemark);
        return marker;
    }

    private void setMarkerInfoWindow(KmlStyle style, Marker marker, KmlPlacemark placemark) {
        boolean hasName = placemark.hasProperty("name");
        boolean hasDescription = placemark.hasProperty("description");
        boolean hasBalloonOptions = style.hasBalloonStyle();
        boolean hasBalloonText = style.getBalloonOptions().containsKey(PageHelper.TEXT_PART_TAG);
        if (hasBalloonOptions && hasBalloonText) {
            marker.setTitle(style.getBalloonOptions().get(PageHelper.TEXT_PART_TAG));
            createInfoWindow();
            return;
        }
        if (hasBalloonOptions && hasName) {
            marker.setTitle(placemark.getProperty("name"));
            createInfoWindow();
        } else if (hasName && hasDescription) {
            marker.setTitle(placemark.getProperty("name"));
            marker.setSnippet(placemark.getProperty("description"));
            createInfoWindow();
        } else if (hasDescription) {
            marker.setTitle(placemark.getProperty("description"));
            createInfoWindow();
        }
    }

    private void createInfoWindow() {
        this.mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() { // from class: com.google.maps.android.kml.KmlRenderer.1
            @Override // com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override // com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
            public View getInfoContents(Marker arg0) {
                View view = LayoutInflater.from(KmlRenderer.this.mContext).inflate(R.layout.amu_info_window, (ViewGroup) null);
                TextView infoWindowText = (TextView) view.findViewById(R.id.window);
                if (arg0.getSnippet() != null) {
                    infoWindowText.setText(Html.fromHtml(arg0.getTitle() + "<br>" + arg0.getSnippet()));
                } else {
                    infoWindowText.setText(Html.fromHtml(arg0.getTitle()));
                }
                return view;
            }
        });
    }

    private void setInlinePointStyle(MarkerOptions markerOptions, KmlStyle inlineStyle, String markerUrlIconUrl) {
        MarkerOptions inlineMarkerOptions = inlineStyle.getMarkerOptions();
        if (inlineStyle.isStyleSet("heading")) {
            markerOptions.rotation(inlineMarkerOptions.getRotation());
        }
        if (inlineStyle.isStyleSet("hotSpot")) {
            markerOptions.anchor(inlineMarkerOptions.getAnchorU(), inlineMarkerOptions.getAnchorV());
        }
        if (inlineStyle.isStyleSet("markerColor")) {
            markerOptions.icon(inlineMarkerOptions.getIcon());
        }
        if (inlineStyle.isStyleSet("iconUrl")) {
            addMarkerIcons(inlineStyle.getIconUrl(), markerOptions);
        } else if (markerUrlIconUrl != null) {
            addMarkerIcons(markerUrlIconUrl, markerOptions);
        }
    }

    private Polyline addLineStringToMap(KmlLineString lineString, KmlStyle style, KmlStyle inlineStyle) {
        PolylineOptions polylineOptions = style.getPolylineOptions();
        polylineOptions.addAll(lineString.getGeometryObject());
        if (inlineStyle != null) {
            setInlineLineStringStyle(polylineOptions, inlineStyle);
        } else if (style.isLineRandomColorMode()) {
            polylineOptions.color(KmlStyle.computeRandomColor(polylineOptions.getColor()));
        }
        return this.mMap.addPolyline(polylineOptions);
    }

    private void setInlineLineStringStyle(PolylineOptions polylineOptions, KmlStyle inlineStyle) {
        PolylineOptions inlinePolylineOptions = inlineStyle.getPolylineOptions();
        if (inlineStyle.isStyleSet("outlineColor")) {
            polylineOptions.color(inlinePolylineOptions.getColor());
        }
        if (inlineStyle.isStyleSet("width")) {
            polylineOptions.width(inlinePolylineOptions.getWidth());
        }
        if (inlineStyle.isLineRandomColorMode()) {
            polylineOptions.color(KmlStyle.computeRandomColor(inlinePolylineOptions.getColor()));
        }
    }

    private Polygon addPolygonToMap(KmlPolygon polygon, KmlStyle style, KmlStyle inlineStyle) {
        PolygonOptions polygonOptions = style.getPolygonOptions();
        polygonOptions.addAll(polygon.getOuterBoundaryCoordinates());
        for (ArrayList<LatLng> innerBoundary : polygon.getInnerBoundaryCoordinates()) {
            polygonOptions.addHole(innerBoundary);
        }
        if (inlineStyle != null) {
            setInlinePolygonStyle(polygonOptions, inlineStyle);
        } else if (style.isPolyRandomColorMode()) {
            polygonOptions.fillColor(KmlStyle.computeRandomColor(polygonOptions.getFillColor()));
        }
        return this.mMap.addPolygon(polygonOptions);
    }

    private void setInlinePolygonStyle(PolygonOptions polygonOptions, KmlStyle inlineStyle) {
        PolygonOptions inlinePolygonOptions = inlineStyle.getPolygonOptions();
        if (inlineStyle.hasFill() && inlineStyle.isStyleSet("fillColor")) {
            polygonOptions.fillColor(inlinePolygonOptions.getFillColor());
        }
        if (inlineStyle.hasOutline()) {
            if (inlineStyle.isStyleSet("outlineColor")) {
                polygonOptions.strokeColor(inlinePolygonOptions.getStrokeColor());
            }
            if (inlineStyle.isStyleSet("width")) {
                polygonOptions.strokeWidth(inlinePolygonOptions.getStrokeWidth());
            }
        }
        if (inlineStyle.isPolyRandomColorMode()) {
            polygonOptions.fillColor(KmlStyle.computeRandomColor(inlinePolygonOptions.getFillColor()));
        }
    }

    private ArrayList<Object> addMultiGeometryToMap(KmlPlacemark placemark, KmlMultiGeometry multiGeometry, KmlStyle urlStyle, KmlStyle inlineStyle, boolean isContainerVisible) {
        ArrayList<Object> mapObjects = new ArrayList<>();
        ArrayList<KmlGeometry> kmlObjects = multiGeometry.getGeometryObject();
        for (KmlGeometry kmlGeometry : kmlObjects) {
            mapObjects.add(addToMap(placemark, kmlGeometry, urlStyle, inlineStyle, isContainerVisible));
        }
        return mapObjects;
    }

    private void addGroundOverlays(HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays, Iterable<KmlContainer> kmlContainers) {
        addGroundOverlays(groundOverlays);
        for (KmlContainer container : kmlContainers) {
            addGroundOverlays(container.getGroundOverlayHashMap(), container.getContainers());
        }
    }

    private void addGroundOverlays(HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays) {
        for (KmlGroundOverlay groundOverlay : groundOverlays.keySet()) {
            String groundOverlayUrl = groundOverlay.getImageUrl();
            if (groundOverlayUrl != null && groundOverlay.getLatLngBox() != null) {
                if (this.mImagesCache.get(groundOverlayUrl) != null) {
                    addGroundOverlayToMap(groundOverlayUrl, this.mGroundOverlays, true);
                } else if (!this.mGroundOverlayUrls.contains(groundOverlayUrl)) {
                    this.mGroundOverlayUrls.add(groundOverlayUrl);
                }
            }
        }
    }

    private void downloadGroundOverlays() {
        this.mGroundOverlayImagesDownloaded = true;
        Iterator<String> iterator = this.mGroundOverlayUrls.iterator();
        while (iterator.hasNext()) {
            String groundOverlayUrl = iterator.next();
            new GroundOverlayImageDownload(groundOverlayUrl).execute(new String[0]);
            iterator.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addGroundOverlayToMap(String groundOverlayUrl, HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays, boolean containerVisibility) {
        BitmapDescriptor groundOverlayBitmap = BitmapDescriptorFactory.fromBitmap(this.mImagesCache.get(groundOverlayUrl));
        for (KmlGroundOverlay kmlGroundOverlay : groundOverlays.keySet()) {
            if (kmlGroundOverlay.getImageUrl().equals(groundOverlayUrl)) {
                GroundOverlayOptions groundOverlayOptions = kmlGroundOverlay.getGroundOverlayOptions().image(groundOverlayBitmap);
                GroundOverlay mapGroundOverlay = this.mMap.addGroundOverlay(groundOverlayOptions);
                if (!containerVisibility) {
                    mapGroundOverlay.setVisible(false);
                }
                groundOverlays.put(kmlGroundOverlay, mapGroundOverlay);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addGroundOverlayInContainerGroups(String groundOverlayUrl, Iterable<KmlContainer> kmlContainers, boolean containerVisibility) {
        for (KmlContainer container : kmlContainers) {
            boolean isContainerVisible = getContainerVisibility(container, containerVisibility);
            addGroundOverlayToMap(groundOverlayUrl, container.getGroundOverlayHashMap(), isContainerVisible);
            if (container.hasContainers()) {
                addGroundOverlayInContainerGroups(groundOverlayUrl, container.getContainers(), isContainerVisible);
            }
        }
    }

    private class MarkerIconImageDownload extends AsyncTask<String, Void, Bitmap> {
        private final String mIconUrl;

        public MarkerIconImageDownload(String iconUrl) {
            this.mIconUrl = iconUrl;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(String... params) {
            try {
                return BitmapFactory.decodeStream((InputStream) new URL(this.mIconUrl).getContent());
            } catch (MalformedURLException e) {
                return BitmapFactory.decodeFile(this.mIconUrl);
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                KmlRenderer.this.mImagesCache.put(this.mIconUrl, bitmap);
                if (KmlRenderer.this.mLayerVisible) {
                    KmlRenderer.this.addIconToMarkers(this.mIconUrl, KmlRenderer.this.mPlacemarks);
                    KmlRenderer.this.addContainerGroupIconsToMarkers(this.mIconUrl, KmlRenderer.this.mContainers);
                    return;
                }
                return;
            }
            Log.e(KmlRenderer.LOG_TAG, "Image at this URL could not be found " + this.mIconUrl);
        }
    }

    private class GroundOverlayImageDownload extends AsyncTask<String, Void, Bitmap> {
        private final String mGroundOverlayUrl;

        public GroundOverlayImageDownload(String groundOverlayUrl) {
            this.mGroundOverlayUrl = groundOverlayUrl;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(String... params) {
            try {
                return BitmapFactory.decodeStream((InputStream) new URL(this.mGroundOverlayUrl).getContent());
            } catch (MalformedURLException e) {
                return BitmapFactory.decodeFile(this.mGroundOverlayUrl);
            } catch (IOException e2) {
                Log.e(KmlRenderer.LOG_TAG, "Image [" + this.mGroundOverlayUrl + "] download issue", e2);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                KmlRenderer.this.mImagesCache.put(this.mGroundOverlayUrl, bitmap);
                if (KmlRenderer.this.mLayerVisible) {
                    KmlRenderer.this.addGroundOverlayToMap(this.mGroundOverlayUrl, KmlRenderer.this.mGroundOverlays, true);
                    KmlRenderer.this.addGroundOverlayInContainerGroups(this.mGroundOverlayUrl, KmlRenderer.this.mContainers, true);
                    return;
                }
                return;
            }
            Log.e(KmlRenderer.LOG_TAG, "Image at this URL could not be found " + this.mGroundOverlayUrl);
        }
    }
}
