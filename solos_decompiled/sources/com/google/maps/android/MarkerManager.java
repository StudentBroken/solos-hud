package com.google.maps.android;

import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes69.dex */
public class MarkerManager implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.InfoWindowAdapter {
    private final GoogleMap mMap;
    private final Map<String, Collection> mNamedCollections = new HashMap();
    private final Map<Marker, Collection> mAllMarkers = new HashMap();

    public MarkerManager(GoogleMap map) {
        this.mMap = map;
    }

    public Collection newCollection() {
        return new Collection();
    }

    public Collection newCollection(String id) {
        if (this.mNamedCollections.get(id) != null) {
            throw new IllegalArgumentException("collection id is not unique: " + id);
        }
        Collection collection = new Collection();
        this.mNamedCollections.put(id, collection);
        return collection;
    }

    public Collection getCollection(String id) {
        return this.mNamedCollections.get(id);
    }

    @Override // com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
    public View getInfoWindow(Marker marker) {
        Collection collection = this.mAllMarkers.get(marker);
        if (collection == null || collection.mInfoWindowAdapter == null) {
            return null;
        }
        return collection.mInfoWindowAdapter.getInfoWindow(marker);
    }

    @Override // com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
    public View getInfoContents(Marker marker) {
        Collection collection = this.mAllMarkers.get(marker);
        if (collection == null || collection.mInfoWindowAdapter == null) {
            return null;
        }
        return collection.mInfoWindowAdapter.getInfoContents(marker);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
    public void onInfoWindowClick(Marker marker) {
        Collection collection = this.mAllMarkers.get(marker);
        if (collection == null || collection.mInfoWindowClickListener == null) {
            return;
        }
        collection.mInfoWindowClickListener.onInfoWindowClick(marker);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
    public boolean onMarkerClick(Marker marker) {
        Collection collection = this.mAllMarkers.get(marker);
        if (collection == null || collection.mMarkerClickListener == null) {
            return false;
        }
        return collection.mMarkerClickListener.onMarkerClick(marker);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
    public void onMarkerDragStart(Marker marker) {
        Collection collection = this.mAllMarkers.get(marker);
        if (collection == null || collection.mMarkerDragListener == null) {
            return;
        }
        collection.mMarkerDragListener.onMarkerDragStart(marker);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
    public void onMarkerDrag(Marker marker) {
        Collection collection = this.mAllMarkers.get(marker);
        if (collection == null || collection.mMarkerDragListener == null) {
            return;
        }
        collection.mMarkerDragListener.onMarkerDrag(marker);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
    public void onMarkerDragEnd(Marker marker) {
        Collection collection = this.mAllMarkers.get(marker);
        if (collection == null || collection.mMarkerDragListener == null) {
            return;
        }
        collection.mMarkerDragListener.onMarkerDragEnd(marker);
    }

    public boolean remove(Marker marker) {
        Collection collection = this.mAllMarkers.get(marker);
        return collection != null && collection.remove(marker);
    }

    public class Collection {
        private GoogleMap.InfoWindowAdapter mInfoWindowAdapter;
        private GoogleMap.OnInfoWindowClickListener mInfoWindowClickListener;
        private GoogleMap.OnMarkerClickListener mMarkerClickListener;
        private GoogleMap.OnMarkerDragListener mMarkerDragListener;
        private final Set<Marker> mMarkers = new HashSet();

        public Collection() {
        }

        public Marker addMarker(MarkerOptions opts) {
            Marker marker = MarkerManager.this.mMap.addMarker(opts);
            this.mMarkers.add(marker);
            MarkerManager.this.mAllMarkers.put(marker, this);
            return marker;
        }

        public boolean remove(Marker marker) {
            if (!this.mMarkers.remove(marker)) {
                return false;
            }
            MarkerManager.this.mAllMarkers.remove(marker);
            marker.remove();
            return true;
        }

        public void clear() {
            for (Marker marker : this.mMarkers) {
                marker.remove();
                MarkerManager.this.mAllMarkers.remove(marker);
            }
            this.mMarkers.clear();
        }

        public java.util.Collection<Marker> getMarkers() {
            return Collections.unmodifiableCollection(this.mMarkers);
        }

        public void setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener infoWindowClickListener) {
            this.mInfoWindowClickListener = infoWindowClickListener;
        }

        public void setOnMarkerClickListener(GoogleMap.OnMarkerClickListener markerClickListener) {
            this.mMarkerClickListener = markerClickListener;
        }

        public void setOnMarkerDragListener(GoogleMap.OnMarkerDragListener markerDragListener) {
            this.mMarkerDragListener = markerDragListener;
        }

        public void setOnInfoWindowAdapter(GoogleMap.InfoWindowAdapter infoWindowAdapter) {
            this.mInfoWindowAdapter = infoWindowAdapter;
        }
    }
}
