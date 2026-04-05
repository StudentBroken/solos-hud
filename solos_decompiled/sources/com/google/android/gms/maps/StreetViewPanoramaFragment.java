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
import com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate;
import com.google.android.gms.maps.internal.StreetViewLifecycleDelegate;
import com.google.android.gms.maps.internal.zzbw;
import com.google.android.gms.maps.internal.zzbx;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
@TargetApi(11)
public class StreetViewPanoramaFragment extends Fragment {
    private final zzb zzbmL = new zzb(this);

    static class zza implements StreetViewLifecycleDelegate {
        private final Fragment zzaSF;
        private final IStreetViewPanoramaFragmentDelegate zzbmM;

        public zza(Fragment fragment, IStreetViewPanoramaFragmentDelegate iStreetViewPanoramaFragmentDelegate) {
            this.zzbmM = (IStreetViewPanoramaFragmentDelegate) zzbr.zzu(iStreetViewPanoramaFragmentDelegate);
            this.zzaSF = (Fragment) zzbr.zzu(fragment);
        }

        @Override // com.google.android.gms.maps.internal.StreetViewLifecycleDelegate
        public final void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
            try {
                this.zzbmM.getStreetViewPanoramaAsync(new zzag(this, onStreetViewPanoramaReadyCallback));
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
                if (arguments != null && arguments.containsKey("StreetViewPanoramaOptions")) {
                    zzbw.zza(bundle2, "StreetViewPanoramaOptions", arguments.getParcelable("StreetViewPanoramaOptions"));
                }
                this.zzbmM.onCreate(bundle2);
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
                IObjectWrapper iObjectWrapperOnCreateView = this.zzbmM.onCreateView(com.google.android.gms.dynamic.zzn.zzw(layoutInflater), com.google.android.gms.dynamic.zzn.zzw(viewGroup), bundle2);
                zzbw.zzd(bundle2, bundle);
                return (View) com.google.android.gms.dynamic.zzn.zzE(iObjectWrapperOnCreateView);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onDestroy() {
            try {
                this.zzbmM.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onDestroyView() {
            try {
                this.zzbmM.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            try {
                Bundle bundle3 = new Bundle();
                zzbw.zzd(bundle2, bundle3);
                this.zzbmM.onInflate(com.google.android.gms.dynamic.zzn.zzw(activity), null, bundle3);
                zzbw.zzd(bundle3, bundle2);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onLowMemory() {
            try {
                this.zzbmM.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onPause() {
            try {
                this.zzbmM.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onResume() {
            try {
                this.zzbmM.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onSaveInstanceState(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                this.zzbmM.onSaveInstanceState(bundle2);
                zzbw.zzd(bundle2, bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onStart() {
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onStop() {
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private Activity mActivity;
        private final Fragment zzaSF;
        private final List<OnStreetViewPanoramaReadyCallback> zzbmO = new ArrayList();
        private com.google.android.gms.dynamic.zzo<zza> zzbmw;

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
                this.zzbmw.zza(new zza(this.zzaSF, zzbx.zzbh(this.mActivity).zzI(com.google.android.gms.dynamic.zzn.zzw(this.mActivity))));
                Iterator<OnStreetViewPanoramaReadyCallback> it = this.zzbmO.iterator();
                while (it.hasNext()) {
                    zztw().getStreetViewPanoramaAsync(it.next());
                }
                this.zzbmO.clear();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            } catch (GooglePlayServicesNotAvailableException e2) {
            }
        }

        public final void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
            if (zztw() != null) {
                zztw().getStreetViewPanoramaAsync(onStreetViewPanoramaReadyCallback);
            } else {
                this.zzbmO.add(onStreetViewPanoramaReadyCallback);
            }
        }

        @Override // com.google.android.gms.dynamic.zza
        protected final void zza(com.google.android.gms.dynamic.zzo<zza> zzoVar) {
            this.zzbmw = zzoVar;
            zzwf();
        }
    }

    public static StreetViewPanoramaFragment newInstance() {
        return new StreetViewPanoramaFragment();
    }

    public static StreetViewPanoramaFragment newInstance(StreetViewPanoramaOptions streetViewPanoramaOptions) {
        StreetViewPanoramaFragment streetViewPanoramaFragment = new StreetViewPanoramaFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("StreetViewPanoramaOptions", streetViewPanoramaOptions);
        streetViewPanoramaFragment.setArguments(bundle);
        return streetViewPanoramaFragment;
    }

    public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
        zzbr.zzcz("getStreetViewPanoramaAsync() must be called on the main thread");
        this.zzbmL.getStreetViewPanoramaAsync(onStreetViewPanoramaReadyCallback);
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(StreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onActivityCreated(bundle);
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.zzbmL.setActivity(activity);
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzbmL.onCreate(bundle);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.zzbmL.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        this.zzbmL.onDestroy();
        super.onDestroy();
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        this.zzbmL.onDestroyView();
        super.onDestroyView();
    }

    @Override // android.app.Fragment
    @SuppressLint({"NewApi"})
    public void onInflate(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        super.onInflate(activity, attributeSet, bundle);
        this.zzbmL.setActivity(activity);
        this.zzbmL.onInflate(activity, new Bundle(), bundle);
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        this.zzbmL.onLowMemory();
        super.onLowMemory();
    }

    @Override // android.app.Fragment
    public void onPause() {
        this.zzbmL.onPause();
        super.onPause();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        this.zzbmL.onResume();
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(StreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(bundle);
        this.zzbmL.onSaveInstanceState(bundle);
    }

    @Override // android.app.Fragment
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
    }
}
