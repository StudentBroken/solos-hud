package com.kopin.solos.share;

import com.kopin.peloton.Bike;
import com.kopin.peloton.FTP;
import com.kopin.peloton.PHR;
import com.kopin.peloton.RFTP;
import com.kopin.peloton.Ride;
import com.kopin.peloton.Run;
import com.kopin.peloton.ride.Route;
import com.kopin.peloton.training.TrainingWorkout;
import com.kopin.solos.share.peloton.RideData;
import com.kopin.solos.share.peloton.RunData;
import com.kopin.solos.storage.ISyncable;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.Shared;

/* JADX INFO: loaded from: classes4.dex */
public class SyncManager {
    public static boolean isUnSynced(ISyncable localSyncableData) {
        Shared.ShareType type = Config.SYNC_PROVIDER.getShareTypeForObject(localSyncableData);
        return (type == Shared.ShareType.NONE || SQLHelper.isItemShared(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ""))) ? false : true;
    }

    public static void startSyncing(ISyncable localSyncableData) {
        if (Config.SYNC_PROVIDER != Platforms.None) {
            SQLHelper.addShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ""));
        }
    }

    public static void setSynced(ISyncable localSyncableData, Object pelotonData) {
        if (Config.SYNC_PROVIDER != Platforms.None) {
            if (pelotonData == null) {
                SQLHelper.removeShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ""));
                return;
            }
            if ((pelotonData instanceof Ride) || (pelotonData instanceof RideData)) {
                SQLHelper.updateShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ((Ride) pelotonData).RideId));
                return;
            }
            if ((pelotonData instanceof Run) || (pelotonData instanceof RunData)) {
                SQLHelper.updateShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ((Run) pelotonData).RunId));
                return;
            }
            if (pelotonData instanceof Bike) {
                SQLHelper.updateShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ((Bike) pelotonData).BikeId));
                return;
            }
            if (pelotonData instanceof FTP) {
                SQLHelper.updateShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ((FTP) pelotonData).FunctionalThresholdPowerId));
                return;
            }
            if (pelotonData instanceof RFTP) {
                SQLHelper.updateShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ((RFTP) pelotonData).RtpId));
                return;
            }
            if (pelotonData instanceof PHR) {
                SQLHelper.updateShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ((PHR) pelotonData).PhrId));
            } else if (pelotonData instanceof Route) {
                SQLHelper.updateShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ((Route) pelotonData).RouteId));
            } else if (pelotonData instanceof TrainingWorkout) {
                SQLHelper.updateShare(localSyncableData.toShared(Config.SYNC_PROVIDER.getSharedKey(), Sync.getUsername(), ((TrainingWorkout) pelotonData).TrainingId));
            }
        }
    }
}
