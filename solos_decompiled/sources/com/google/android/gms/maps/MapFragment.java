package com.google.android.gms.maps;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.google.android.gms.maps.internal.zzbw;
import com.google.android.gms.maps.internal.zzbx;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
@TargetApi(11)
public class MapFragment extends Fragment {
    private final zzb zzbmt = new zzb(this);

    static class zza implements MapLifecycleDelegate {
        private final Fragment zzaSF;
        private final IMapFragmentDelegate zzbmu;

        public zza(Fragment fragment, IMapFragmentDelegate iMapFragmentDelegate) {
            this.zzbmu = (IMapFragmentDelegate) zzbr.zzu(iMapFragmentDelegate);
            this.zzaSF = (Fragment) zzbr.zzu(fragment);
        }

        @Override // com.google.android.gms.maps.internal.MapLifecycleDelegate
        public final void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
            try {
                this.zzbmu.getMapAsync(new zzaa(this, onMapReadyCallback));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onCreate(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                Bundle arguments = this.zzaSF.getArguments();
                if (arguments != null && arguments.containsKey("MapOptions")) {
                    zzbw.zza(bundle2, "MapOptions", arguments.getParcelable("MapOptions"));
                }
                this.zzbmu.onCreate(bundle2);
                zzbw.zzd(bundle2, bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                IObjectWrapper iObjectWrapperOnCreateView = this.zzbmu.onCreateView(com.google.android.gms.dynamic.zzn.zzw(layoutInflater), com.google.android.gms.dynamic.zzn.zzw(viewGroup), bundle2);
                zzbw.zzd(bundle2, bundle);
                return (View) com.google.android.gms.dynamic.zzn.zzE(iObjectWrapperOnCreateView);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onDestroy() {
            try {
                this.zzbmu.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onDestroyView() {
            try {
                this.zzbmu.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onEnterAmbient(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                this.zzbmu.onEnterAmbient(bundle2);
                zzbw.zzd(bundle2, bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onExitAmbient() {
            try {
                this.zzbmu.onExitAmbient();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            GoogleMapOptions googleMapOptions = (GoogleMapOptions) bundle.getParcelable("MapOptions");
            try {
                Bundle bundle3 = new Bundle();
                zzbw.zzd(bundle2, bundle3);
                this.zzbmu.onInflate(com.google.android.gms.dynamic.zzn.zzw(activity), googleMapOptions, bundle3);
                zzbw.zzd(bundle3, bundle2);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onLowMemory() {
            try {
                this.zzbmu.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onPause() {
            try {
                this.zzbmu.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onResume() {
            try {
                this.zzbmu.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onSaveInstanceState(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                this.zzbmu.onSaveInstanceState(bundle2);
                zzbw.zzd(bundle2, bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onStart() {
            try {
                this.zzbmu.onStart();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onStop() {
            try {
                this.zzbmu.onStop();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private Activity mActivity;
        private final Fragment zzaSF;
        private com.google.android.gms.dynamic.zzo<zza> zzbmw;
        private final List<OnMapReadyCallback> zzbmx = new ArrayList();

        zzb(Fragment fragment) {
            this.zzaSF = fragment;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void setActivity(Activity activity) {
            this.mActivity = activity;
            zzwf();
        }

        private final void zzwf() {
            if (this.mActivity == null || this.zzbmw == null || zztw() != null) {
                return;
            }
            try {
                MapsInitializer.initialize(this.mActivity);
                IMapFragmentDelegate iMapFragmentDelegateZzH = zzbx.zzbh(this.mActivity).zzH(com.google.android.gms.dynamic.zzn.zzw(this.mActivity));
                if (iMapFragmentDelegateZzH == null) {
                    return;
                }
                this.zzbmw.zza(new zza(this.zzaSF, iMapFragmentDelegateZzH));
                Iterator<OnMapReadyCallback> it = this.zzbmx.iterator();
                while (it.hasNext()) {
                    zztw().getMapAsync(it.next());
                }
                this.zzbmx.clear();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            } catch (GooglePlayServicesNotAvailableException e2) {
            }
        }

        public final void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
            if (zztw() != null) {
                zztw().getMapAsync(onMapReadyCallback);
            } else {
                this.zzbmx.add(onMapReadyCallback);
            }
        }

        @Override // com.google.android.gms.dynamic.zza
        protected final void zza(com.google.android.gms.dynamic.zzo<zza> zzoVar) {
            this.zzbmw = zzoVar;
            zzwf();
        }
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public static MapFragment newInstance(GoogleMapOptions googleMapOptions) {
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", googleMapOptions);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        zzbr.zzcz("getMapAsync must be called on the main thread.");
        this.zzbmt.getMapAsync(onMapReadyCallback);
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MapFragment.class.getClassLoader());
        }
        super.onActivityCreated(bundle);
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.zzbmt.setActivity(activity);
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzbmt.onCreate(bundle);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewOnCreateView = this.zzbmt.onCreateView(layoutInflater, viewGroup, bundle);
        viewOnCreateView.setClickable(true);
        return viewOnCreateView;
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        this.zzbmt.onDestroy();
        super.onDestroy();
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        this.zzbmt.onDestroyView();
        super.onDestroyView();
    }

    public final void onEnterAmbient(Bundle bundle) {
        zzbr.zzcz("onEnterAmbient must be called on the main thread.");
        zzb zzbVar = this.zzbmt;
        if (zzbVar.zztw() != null) {
            zzbVar.zztw().onEnterAmbient(bundle);
        }
    }

    public final void onExitAmbient() {
        zzbr.zzcz("onExitAmbient must be called on the main thread.");
        zzb zzbVar = this.zzbmt;
        if (zzbVar.zztw() != null) {
            zzbVar.zztw().onExitAmbient();
        }
    }

    @Override // android.app.Fragment
    @SuppressLint({"NewApi"})
    public void onInflate(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        super.onInflate(activity, attributeSet, bundle);
        this.zzbmt.setActivity(activity);
        GoogleMapOptions googleMapOptionsCreateFromAttributes = GoogleMapOptions.createFromAttributes(activity, attributeSet);
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("MapOptions", googleMapOptionsCreateFromAttributes);
        this.zzbmt.onInflate(activity, bundle2, bundle);
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        this.zzbmt.onLowMemory();
        super.onLowMemory();
    }

    @Override // android.app.Fragment
    public void onPause() {
        this.zzbmt.onPause();
        super.onPause();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        this.zzbmt.onResume();
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MapFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(bundle);
        this.zzbmt.onSaveInstanceState(bundle);
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        this.zzbmt.onStart();
    }

    @Override // android.app.Fragment
    public void onStop() {
        this.zzbmt.onStop();
        super.onStop();
    }

    @Override // android.app.Fragment
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
    }
}
