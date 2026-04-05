package com.kopin.solos.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes52.dex */
public enum SportType {
    RIDE(Feature.BIKE, Feature.FTP),
    RUN(Feature.FTP);

    public static final String KEY = "SportType";
    private final Set<Feature> features = new HashSet();
    public static final SportType DEFAULT_TYPE = RIDE;

    public enum Feature {
        BIKE,
        FTP
    }

    SportType(Feature... featureSet) {
        this.features.addAll(Arrays.asList(featureSet));
    }

    public boolean hasFeature(Feature feature) {
        return this.features.contains(feature);
    }

    public static SportType get(String name) {
        for (SportType sportType : values()) {
            if (sportType.name().equalsIgnoreCase(name)) {
                return sportType;
            }
        }
        return DEFAULT_TYPE;
    }
}
