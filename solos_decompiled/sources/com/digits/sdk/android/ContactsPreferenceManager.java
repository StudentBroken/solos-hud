package com.digits.sdk.android;

import android.annotation.SuppressLint;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;

/* JADX INFO: loaded from: classes18.dex */
class ContactsPreferenceManager {
    static final String KEY_CONTACTS_IMPORT_PERMISSION = "CONTACTS_IMPORT_PERMISSION";
    static final String KEY_CONTACTS_READ_TIMESTAMP = "CONTACTS_READ_TIMESTAMP";
    static final String KEY_CONTACTS_UPLOADED = "CONTACTS_CONTACTS_UPLOADED";
    private final PreferenceStore prefStore = new PreferenceStoreImpl(Fabric.getKit(Digits.class));

    ContactsPreferenceManager() {
    }

    @SuppressLint({"CommitPrefEdits"})
    protected boolean hasContactImportPermissionGranted() {
        return this.prefStore.get().getBoolean(KEY_CONTACTS_IMPORT_PERMISSION, false);
    }

    @SuppressLint({"CommitPrefEdits"})
    protected void setContactImportPermissionGranted() {
        this.prefStore.save(this.prefStore.edit().putBoolean(KEY_CONTACTS_IMPORT_PERMISSION, true));
    }

    @SuppressLint({"CommitPrefEdits"})
    protected void clearContactImportPermissionGranted() {
        this.prefStore.save(this.prefStore.edit().remove(KEY_CONTACTS_IMPORT_PERMISSION));
    }

    @SuppressLint({"CommitPrefEdits"})
    protected void setContactsReadTimestamp(long timestamp) {
        this.prefStore.save(this.prefStore.edit().putLong(KEY_CONTACTS_READ_TIMESTAMP, timestamp));
    }

    @SuppressLint({"CommitPrefEdits"})
    protected void setContactsUploaded(int count) {
        this.prefStore.save(this.prefStore.edit().putInt(KEY_CONTACTS_UPLOADED, count));
    }
}
