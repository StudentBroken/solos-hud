package com.ua.sdk.bodymass;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface BodyMass extends Entity<EntityRef<BodyMass>> {
    String getBmi();

    Date getCreatedDateTime();

    String getDateTimeTimezone();

    Date getDateTimeUtc();

    String getFatMass();

    String getFatPercent();

    String getLeanMass();

    String getMass();

    String getRecorderType();

    String getReferenceKey();

    Date getUpdatedDateTime();

    EntityRef<User> getUserRef();

    void setBmi(String str);

    void setCreatedDateTime(Date date);

    void setDateTimeTimezone(String str);

    void setDateTimeUtc(Date date);

    void setFatMass(String str);

    void setFatPercent(String str);

    void setLeanMass(String str);

    void setMass(String str);

    void setRecorderType(String str);

    void setReferenceKey(String str);

    void setUpdatedDateTime(Date date);
}
