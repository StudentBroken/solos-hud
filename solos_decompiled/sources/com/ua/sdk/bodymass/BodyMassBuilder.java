package com.ua.sdk.bodymass;

import com.ua.sdk.internal.Precondition;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassBuilder {
    private static final String RECORDER_DEFAULT = "client_manual_creation";
    String bmi;
    String dateTimeTimezone;
    Date dateTimeUtc;
    String fatMass;
    String fatPercent;
    String leanMass;
    String mass;
    String recorderType;
    String referenceKey;

    public BodyMassBuilder setDateTimeUtc(Date dateTimeUtc) {
        this.dateTimeUtc = dateTimeUtc;
        return this;
    }

    public BodyMassBuilder setDateTimeTimezone(String dateTimeTimezone) {
        this.dateTimeTimezone = dateTimeTimezone;
        return this;
    }

    public BodyMassBuilder setRecorderType(String recorderType) {
        this.recorderType = recorderType;
        return this;
    }

    public BodyMassBuilder setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
        return this;
    }

    public BodyMassBuilder setMass(String mass) {
        this.mass = mass;
        return this;
    }

    public BodyMassBuilder setBmi(String bmi) {
        this.bmi = bmi;
        return this;
    }

    public BodyMassBuilder setFatPercent(String fatPercent) {
        this.fatPercent = fatPercent;
        return this;
    }

    public BodyMassBuilder setLeanMass(String leanMass) {
        this.leanMass = leanMass;
        return this;
    }

    public BodyMassBuilder setFatMass(String fatMass) {
        this.fatMass = fatMass;
        return this;
    }

    public BodyMass build() {
        Precondition.isNotNull(this.dateTimeUtc, "Measurement DateTime");
        Precondition.isNotNull(this.dateTimeTimezone, "Measurement DateTime Timezone");
        Precondition.isNotNull(this.referenceKey, "Measurement Device's Reference Key");
        if (this.recorderType == null) {
            this.recorderType = RECORDER_DEFAULT;
        }
        BodyMass impl = new BodyMassImpl();
        impl.setDateTimeUtc(this.dateTimeUtc);
        impl.setDateTimeTimezone(this.dateTimeTimezone);
        impl.setRecorderType(this.recorderType);
        impl.setReferenceKey(this.referenceKey);
        impl.setMass(this.mass);
        impl.setBmi(this.bmi);
        impl.setFatPercent(this.fatPercent);
        impl.setLeanMass(this.leanMass);
        impl.setFatMass(this.fatMass);
        return impl;
    }
}
