package com.ua.sdk.recorder.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.activitytype.ActivityTypeRef;
import com.ua.sdk.cache.database.definition.ColumnDefinition;
import com.ua.sdk.cache.database.definition.DateColumnDefinition;
import com.ua.sdk.cache.database.definition.LocalIdColumnDefinition;
import com.ua.sdk.cache.database.definition.StringColumnDefinition;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.recorder.BluetoothSensorDataSourceConfiguration;
import com.ua.sdk.recorder.DataSourceConfigurationList;
import com.ua.sdk.recorder.DataSourceConfigurationListParser;
import com.ua.sdk.recorder.DataSourceConfigurationListWriter;
import com.ua.sdk.recorder.DerivedDataSourceConfiguration;
import com.ua.sdk.recorder.LocationSensorDataSourceConfiguration;
import com.ua.sdk.recorder.RecorderConfiguration;
import com.ua.sdk.recorder.RecorderConfigurationImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderConfigurationDatabaseMapper {
    public static final ColumnDefinition<Long> LOCAL_ID = new LocalIdColumnDefinition(0, "_id");
    public static final ColumnDefinition<String> NAME = new StringColumnDefinition(1, "name");
    public static final ColumnDefinition<String> USER_ID = new StringColumnDefinition(2, "user_id");
    public static final ColumnDefinition<String> ACTIVITY_TYPE_ID = new StringColumnDefinition(3, "activity_id");
    public static final ColumnDefinition<Date> CREATE_DATE = new DateColumnDefinition(4, "create_date");
    public static final ColumnDefinition<Date> UPDATE_DATE = new DateColumnDefinition(5, "update_date");
    public static final ColumnDefinition<String> CONFIGURATION = new StringColumnDefinition(6, "configuration");
    protected static final ColumnDefinition[] COLUMNS = {LOCAL_ID, NAME, USER_ID, ACTIVITY_TYPE_ID, CREATE_DATE, UPDATE_DATE, CONFIGURATION};

    protected static ContentValues getContentValues(String name, String userId, String activityTypeId, Date createDate, Date updateDate, DataSourceConfigurationList configuration) {
        ContentValues values = new ContentValues();
        NAME.write(name, values);
        USER_ID.write(userId, values);
        ACTIVITY_TYPE_ID.write(activityTypeId, values);
        CREATE_DATE.write(createDate, values);
        UPDATE_DATE.write(updateDate, values);
        CONFIGURATION.write(getConfigurationOutputStream(configuration), values);
        return values;
    }

    protected static ContentValues getUpdateValues(Date updateDate, DataSourceConfigurationList configuration) {
        ContentValues values = new ContentValues();
        UPDATE_DATE.write(updateDate, values);
        if (configuration != null) {
            CONFIGURATION.write(getConfigurationOutputStream(configuration), values);
        }
        return values;
    }

    protected static String getConfigurationOutputStream(DataSourceConfigurationList configuration) {
        DataSourceConfigurationListWriter writer = new DataSourceConfigurationListWriter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            writer.write(configuration, outputStream);
        } catch (UaException e) {
            UaLog.error("Unable to serialize message producer configuration", (Throwable) e);
        }
        return outputStream.toString();
    }

    protected static List<RecorderConfiguration> getCachedConfigurations(Cursor cursor) {
        List<RecorderConfiguration> recorderConfigurations = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                RecorderConfiguration configuration = new RecorderConfigurationImpl();
                configuration.setName(NAME.read(cursor));
                configuration.setActivityTypeRef(ActivityTypeRef.getBuilder().setActivityTypeId(ACTIVITY_TYPE_ID.read(cursor)).build());
                configuration.setUserRef(new LinkEntityRef<>(USER_ID.read(cursor), "ref"));
                DataSourceConfigurationListParser parser = new DataSourceConfigurationListParser();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(CONFIGURATION.read(cursor).getBytes());
                try {
                    DataSourceConfigurationList dataSourceConfigurationList = parser.parse(inputStream);
                    if (!dataSourceConfigurationList.isBluetoothDataSourceConfigurationEmpty()) {
                        for (BluetoothSensorDataSourceConfiguration bluetoothMessageProducerConfiguration : dataSourceConfigurationList.getBluetoothDataSourceConfigurations()) {
                            configuration.addDataSource(bluetoothMessageProducerConfiguration);
                        }
                    }
                    if (!dataSourceConfigurationList.isLocationDataSourceConfigurationEmpty()) {
                        for (LocationSensorDataSourceConfiguration locationMessageProducerConfiguration : dataSourceConfigurationList.getLocationDataSourceConfigurations()) {
                            configuration.addDataSource(locationMessageProducerConfiguration);
                        }
                    }
                    if (!dataSourceConfigurationList.isDerivedDataSourceConfigurationEmpty()) {
                        for (DerivedDataSourceConfiguration derivedDataSourceConfiguration : dataSourceConfigurationList.getDerivedDataSourceConfigurations()) {
                            configuration.addDataSource(derivedDataSourceConfiguration);
                        }
                    }
                } catch (UaException e) {
                    UaLog.error("error parsing message producer configurations", (Throwable) e);
                }
                recorderConfigurations.add(configuration);
            } while (cursor.moveToNext());
            cursor.close();
            return recorderConfigurations;
        }
        cursor.close();
        return null;
    }
}
