package com.google.android.gms.dynamic;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.internal.zzs;
import com.google.android.gms.dynamic.LifecycleDelegate;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zza<T extends LifecycleDelegate> {
    private T zzaSv;
    private Bundle zzaSw;
    private LinkedList<zzi> zzaSx;
    private final zzo<T> zzaSy = new zzb(this);

    static /* synthetic */ Bundle zza(zza zzaVar, Bundle bundle) {
        zzaVar.zzaSw = null;
        return null;
    }

    private final void zza(Bundle bundle, zzi zziVar) {
        if (this.zzaSv != null) {
            zziVar.zzb(this.zzaSv);
            return;
        }
        if (this.zzaSx == null) {
            this.zzaSx = new LinkedList<>();
        }
        this.zzaSx.add(zziVar);
        if (bundle != null) {
            if (this.zzaSw == null) {
                this.zzaSw = (Bundle) bundle.clone();
            } else {
                this.zzaSw.putAll(bundle);
            }
        }
        zza(this.zzaSy);
    }

    private final void zzaR(int i) {
        while (!this.zzaSx.isEmpty() && this.zzaSx.getLast().getState() >= i) {
            this.zzaSx.removeLast();
        }
    }

    public static void zzb(FrameLayout frameLayout) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Context context = frameLayout.getContext();
        int iIsGooglePlayServicesAvailable = googleApiAvailability.isGooglePlayServicesAvailable(context);
        String strZzi = zzs.zzi(context, iIsGooglePlayServicesAvailable);
        String strZzk = zzs.zzk(context, iIsGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        textView.setText(strZzi);
        linearLayout.addView(textView);
        Intent intentZza = com.google.android.gms.common.zze.zza(context, iIsGooglePlayServicesAvailable, null);
        if (intentZza != null) {
            Button button = new Button(context);
            button.setId(R.id.button1);
            button.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            button.setText(strZzk);
            linearLayout.addView(button);
            button.setOnClickListener(new zzf(context, intentZza));
        }
    }

    public final void onCreate(Bundle bundle) {
        zza(bundle, new zzd(this, bundle));
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
        zza(bundle, new zze(this, frameLayout, layoutInflater, viewGroup, bundle));
        if (this.zzaSv == null) {
            zza(frameLayout);
        }
        return frameLayout;
    }

    public final void onDestroy() {
        if (this.zzaSv != null) {
            this.zzaSv.onDestroy();
        } else {
            zzaR(1);
        }
    }

    public final void onDestroyView() {
        if (this.zzaSv != null) {
            this.zzaSv.onDestroyView();
        } else {
            zzaR(2);
        }
    }

    public final void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
        zza(bundle2, new zzc(this, activity, bundle, bundle2));
    }

    public final void onLowMemory() {
        if (this.zzaSv != null) {
            this.zzaSv.onLowMemory();
        }
    }

    public final void onPause() {
        if (this.zzaSv != null) {
            this.zzaSv.onPause();
        } else {
            zzaR(5);
        }
    }

    public final void onResume() {
        zza((Bundle) null, new zzh(this));
    }

    public final void onSaveInstanceState(Bundle bundle) {
        if (this.zzaSv != null) {
            this.zzaSv.onSaveInstanceState(bundle);
        } else if (this.zzaSw != null) {
            bundle.putAll(this.zzaSw);
        }
    }

    public final void onStart() {
        zza((Bundle) null, new zzg(this));
    }

    public final void onStop() {
        if (this.zzaSv != null) {
            this.zzaSv.onStop();
        } else {
            zzaR(4);
        }
    }

    protected void zza(FrameLayout frameLayout) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Context context = frameLayout.getContext();
        int iIsGooglePlayServicesAvailable = googleApiAvailability.isGooglePlayServicesAvailable(context);
        String strZzi = zzs.zzi(context, iIsGooglePlayServicesAvailable);
        String strZzk = zzs.zzk(context, iIsGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        textView.setText(strZzi);
        linearLayout.addView(textView);
        Intent intentZza = com.google.android.gms.common.zze.zza(context, iIsGooglePlayServicesAvailable, null);
        if (intentZza != null) {
            Button button = new Button(context);
            button.setId(R.id.button1);
            button.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            button.setText(strZzk);
            linearLayout.addView(button);
            button.setOnClickListener(new zzf(context, intentZza));
        }
    }

    protected abstract void zza(zzo<T> zzoVar);

    public final T zztw() {
        return this.zzaSv;
    }
}
