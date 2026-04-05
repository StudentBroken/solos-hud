package com.ua.sdk.bodymass;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface BodyMassManager {
    Request fetchBodyMass(EntityRef<BodyMass> entityRef, FetchCallback<BodyMass> fetchCallback);

    BodyMass fetchBodyMass(EntityRef<BodyMass> entityRef) throws UaException;

    EntityList<BodyMass> fetchBodyMassList(EntityListRef<BodyMass> entityListRef) throws UaException;

    Request fetchBodyMassList(EntityListRef<BodyMass> entityListRef, FetchCallback<EntityList<BodyMass>> fetchCallback);

    Request updateBodyMass(BodyMass bodyMass, SaveCallback<BodyMass> saveCallback);

    BodyMass updateBodyMass(BodyMass bodyMass) throws UaException;
}
