package com.google.tagmanager;

import com.google.analytics.containertag.proto.Serving;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;

/* JADX INFO: loaded from: classes49.dex */
class ProtoExtensionRegistry {
    private static ExtensionRegistryLite registry;

    ProtoExtensionRegistry() {
    }

    public static synchronized ExtensionRegistryLite getRegistry() {
        if (registry == null) {
            registry = ExtensionRegistryLite.newInstance();
            Serving.registerAllExtensions(registry);
        }
        return registry;
    }
}
