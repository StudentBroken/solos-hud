package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/* JADX INFO: loaded from: classes67.dex */
public final class zzr extends zzl {
    private Fragment zzaSI;

    private zzr(Fragment fragment) {
        this.zzaSI = fragment;
    }

    public static zzr zza(Fragment fragment) {
        if (fragment != null) {
            return new zzr(fragment);
        }
        return null;
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final Bundle getArguments() {
        return this.zzaSI.getArguments();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final int getId() {
        return this.zzaSI.getId();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean getRetainInstance() {
        return this.zzaSI.getRetainInstance();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final String getTag() {
        return this.zzaSI.getTag();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final int getTargetRequestCode() {
        return this.zzaSI.getTargetRequestCode();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean getUserVisibleHint() {
        return this.zzaSI.getUserVisibleHint();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final IObjectWrapper getView() {
        return zzn.zzw(this.zzaSI.getView());
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isAdded() {
        return this.zzaSI.isAdded();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isDetached() {
        return this.zzaSI.isDetached();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isHidden() {
        return this.zzaSI.isHidden();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isInLayout() {
        return this.zzaSI.isInLayout();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isRemoving() {
        return this.zzaSI.isRemoving();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isResumed() {
        return this.zzaSI.isResumed();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final boolean isVisible() {
        return this.zzaSI.isVisible();
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void setHasOptionsMenu(boolean z) {
        this.zzaSI.setHasOptionsMenu(z);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void setMenuVisibility(boolean z) {
        this.zzaSI.setMenuVisibility(z);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void setRetainInstance(boolean z) {
        this.zzaSI.setRetainInstance(z);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void setUserVisibleHint(boolean z) {
        this.zzaSI.setUserVisibleHint(z);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void startActivity(Intent intent) {
        this.zzaSI.startActivity(intent);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void startActivityForResult(Intent intent, int i) {
        this.zzaSI.startActivityForResult(intent, i);
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void zzC(IObjectWrapper iObjectWrapper) {
        this.zzaSI.registerForContextMenu((View) zzn.zzE(iObjectWrapper));
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final void zzD(IObjectWrapper iObjectWrapper) {
        this.zzaSI.unregisterForContextMenu((View) zzn.zzE(iObjectWrapper));
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final zzk zztA() {
        return zza(this.zzaSI.getTargetFragment());
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final IObjectWrapper zztx() {
        return zzn.zzw(this.zzaSI.getActivity());
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final zzk zzty() {
        return zza(this.zzaSI.getParentFragment());
    }

    @Override // com.google.android.gms.dynamic.zzk
    public final IObjectWrapper zztz() {
        return zzn.zzw(this.zzaSI.getResources());
    }
}
