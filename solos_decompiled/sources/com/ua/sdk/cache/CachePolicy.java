package com.ua.sdk.cache;

/* JADX INFO: loaded from: classes65.dex */
public enum CachePolicy {
    CACHE_ONLY(true, false, false, false),
    CACHE_ONLY_IGNORE_MAX_AGE(true, false, false, true),
    CACHE_ELSE_NETWORK(true, true, false, false),
    NETWORK_ONLY(false, true, true, false),
    NETWORK_ELSE_CACHE(true, true, true, false);

    final boolean checkCache;
    final boolean checkNetwork;
    final boolean checkNetworkFirst;
    final boolean ignoreAge;

    CachePolicy(boolean checkCache, boolean checkNetwork, boolean checkNetworkFirst, boolean ignoreAge) {
        this.checkCache = checkCache;
        this.checkNetwork = checkNetwork;
        this.checkNetworkFirst = checkNetworkFirst;
        this.ignoreAge = ignoreAge;
    }

    public boolean ignoreAge() {
        return this.ignoreAge;
    }

    public boolean checkCache() {
        return this.checkCache;
    }

    public boolean checkNetwork() {
        return this.checkNetwork;
    }

    public boolean checkNetworkFirst() {
        return this.checkNetworkFirst;
    }

    public boolean checkCacheFirst() {
        return this.checkCache && !this.checkNetworkFirst;
    }
}
