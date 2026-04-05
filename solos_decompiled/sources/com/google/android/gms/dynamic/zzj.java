package com.google.android.gms.dynamic;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/* JADX INFO: loaded from: classes67.dex */
@SuppressLint({"NewApi"})
public final class zzj extends zzl {
    private Fragment zzaSF;

    private zzj(Fragment fragment) {
        this.zzaSF = fragment;
    }

    public static zzj zza(Fragment fragment) {
        if (fragment != null) {
            return new zzj(fragment);
        }
        return null;
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final Bundle getArguments() {
        return this.zzaSF.getArguments();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final int getId() {
        return this.zzaSF.getId();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean getRetainInstance() {
        return this.zzaSF.getRetainInstance();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final String getTag() {
        return this.zzaSF.getTag();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final int getTargetRequestCode() {
        return this.zzaSF.getTargetRequestCode();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean getUserVisibleHint() {
        return this.zzaSF.getUserVisibleHint();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final IObjectWrapper getView() {
        return zzn.zzw(this.zzaSF.getView());
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isAdded() {
        return this.zzaSF.isAdded();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isDetached() {
        return this.zzaSF.isDetached();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isHidden() {
        return this.zzaSF.isHidden();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isInLayout() {
        return this.zzaSF.isInLayout();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isRemoving() {
        return this.zzaSF.isRemoving();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isResumed() {
        return this.zzaSF.isResumed();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isVisible() {
        return this.zzaSF.isVisible();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void setHasOptionsMenu(boolean z) {
        this.zzaSF.setHasOptionsMenu(z);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void setMenuVisibility(boolean z) {
        this.zzaSF.setMenuVisibility(z);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void setRetainInstance(boolean z) {
        this.zzaSF.setRetainInstance(z);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void setUserVisibleHint(boolean z) {
        this.zzaSF.setUserVisibleHint(z);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void startActivity(Intent intent) {
        this.zzaSF.startActivity(intent);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void startActivityForResult(Intent intent, int i) {
        this.zzaSF.startActivityForResult(intent, i);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void zzC(IObjectWrapper iObjectWrapper) {
        this.zzaSF.registerForContextMenu((View) zzn.zzE(iObjectWrapper));
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void zzD(IObjectWrapper iObjectWrapper) {
        this.zzaSF.unregisterForContextMenu((View) zzn.zzE(iObjectWrapper));
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final zzk zztA() {
        return zza(this.zzaSF.getTargetFragment());
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final IObjectWrapper zztx() {
        return zzn.zzw(this.zzaSF.getActivity());
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final zzk zzty() {
        return zza(this.zzaSF.getParentFragment());
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final IObjectWrapper zztz() {
        return zzn.zzw(this.zzaSF.getResources());
    }
}
