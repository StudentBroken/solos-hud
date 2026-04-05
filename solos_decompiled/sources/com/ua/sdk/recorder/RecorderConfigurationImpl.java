package com.ua.sdk.recorder;

import com.ua.sdk.EntityRef;
import com.ua.sdk.activitytype.ActivityTypeRef;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderConfigurationImpl implements RecorderConfiguration {
    private ActivityTypeRef activityTypeRef;
    private List<DataSourceConfiguration> dataSourceConfigurations;
    private String name;
    private EntityRef<User> userRef;

    @Override // com.ua.sdk.recorder.RecorderConfiguration
    public /* bridge */ /* synthetic */ RecorderConfiguration setUserRef(EntityRef x0) {
        return setUserRef((EntityRef<User>) x0);
    }

    public String getName() {
        return this.name;
    }

    @Override // com.ua.sdk.recorder.RecorderConfiguration
    public RecorderConfigurationImpl setName(String name) {
        this.name = name;
        return this;
    }

    public EntityRef<User> getUserRef() {
        return this.userRef;
    }

    @Override // com.ua.sdk.recorder.RecorderConfiguration
    public RecorderConfigurationImpl setUserRef(EntityRef<User> userRef) {
        this.userRef = userRef;
        return this;
    }

    public ActivityTypeRef getActivityTypeRef() {
        return this.activityTypeRef;
    }

    @Override // com.ua.sdk.recorder.RecorderConfiguration
    public RecorderConfigurationImpl setActivityTypeRef(ActivityTypeRef activityTypeRef) {
        this.activityTypeRef = activityTypeRef;
        return this;
    }

    @Override // com.ua.sdk.recorder.RecorderConfiguration
    public RecorderConfigurationImpl addDataSource(DataSourceConfiguration dataSourceConfiguration) {
        if (this.dataSourceConfigurations == null) {
            this.dataSourceConfigurations = new ArrayList();
        }
        this.dataSourceConfigurations.add(dataSourceConfiguration);
        return this;
    }

    public List<DataSourceConfiguration> getDataSourceConfigurations() {
        return this.dataSourceConfigurations;
    }
}
