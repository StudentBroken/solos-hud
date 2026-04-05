package io.fabric.sdk.android.services.common;

import io.fabric.sdk.android.services.common.IdManager;
import java.util.Map;

/* JADX INFO: loaded from: classes66.dex */
public interface DeviceIdentifierProvider {
    Map<IdManager.DeviceIdentifierType, String> getDeviceIdentifiers();
}
