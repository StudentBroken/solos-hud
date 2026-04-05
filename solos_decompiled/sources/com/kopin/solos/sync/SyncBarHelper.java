package com.kopin.solos.sync;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.Sync;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class SyncBarHelper implements Sync.SyncUpdateListener {
    private static final String TAG = "SyncBarHelper";
    private final Animation mAnimFade;
    private final Activity mContext;
    private final ImageView mImgCloud;
    private final ImageView mImgCloudIcon;
    private final ImageView mImgShare;
    private final View mSyncBar;
    private final TextView mTxtSyncItems;
    private final TextView mTxtSyncOnline;
    private UISyncRefresh mUiSyncRefresh;
    private boolean mConnected = false;
    public Sync.ConnectionListener connectionListener = new Sync.ConnectionListener() { // from class: com.kopin.solos.sync.SyncBarHelper.1
        @Override // com.kopin.solos.share.Sync.ConnectionListener
        public void onConnectionChange(boolean connected) {
            SyncBarHelper.this.updateConnectivity(connected);
        }
    };

    private enum SyncState {
        DISCONNECTED,
        SYNCING,
        SYNCED,
        SHARED
    }

    public interface UISyncRefresh {
        void refresh();
    }

    public SyncBarHelper(Activity context, UISyncRefresh uiSyncRefresh, View syncBar, TextView txtSyncOnline, TextView txtSyncItems, ImageView imgCloud, ImageView imgCloudIcon, ImageView imgShare) {
        this.mContext = context;
        this.mUiSyncRefresh = uiSyncRefresh;
        this.mSyncBar = syncBar;
        this.mTxtSyncOnline = txtSyncOnline;
        this.mTxtSyncItems = txtSyncItems;
        this.mImgCloud = imgCloud;
        this.mImgCloudIcon = imgCloudIcon;
        this.mImgShare = imgShare;
        this.mAnimFade = AnimationUtils.loadAnimation(this.mContext, R.anim.fade_slow);
    }

    public void updateConnectivity(boolean connected) {
        this.mConnected = connected;
        if (this.mTxtSyncOnline != null) {
            this.mTxtSyncOnline.setText(connected ? R.string.sync_online : R.string.sync_offline);
            this.mImgCloudIcon.setImageResource(connected ? R.drawable.ic_sync : R.drawable.ic_cross_sync);
            int colorRef = this.mConnected ? R.color.solos_orange : R.color.sync_bar_offline_text;
            this.mTxtSyncOnline.setTextColor(this.mContext.getResources().getColor(colorRef));
            this.mTxtSyncItems.setTextColor(this.mContext.getResources().getColor(colorRef));
            this.mImgCloud.setColorFilter(this.mContext.getResources().getColor(colorRef));
            this.mImgCloudIcon.setColorFilter(this.mContext.getResources().getColor(colorRef));
            this.mSyncBar.setVisibility(Sync.getNumUnsyncedItems() > 0 ? 0 : 8);
            updateSyncing(Sync.getNumSyncedItems(), Sync.getNumUnsyncedItems());
        }
    }

    public void sharedNotification(Platforms platform, int message) {
        if (this.mTxtSyncItems != null) {
            this.mSyncBar.setVisibility(0);
            this.mTxtSyncItems.setText(message);
            this.mImgShare.setImageResource(platform.getMenuIconId());
            updateIcons(SyncState.SHARED);
            new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.sync.SyncBarHelper.2
                @Override // java.lang.Runnable
                public void run() {
                    SyncBarHelper.this.mSyncBar.startAnimation(SyncBarHelper.this.mAnimFade);
                    SyncBarHelper.this.mSyncBar.setVisibility(8);
                }
            }, 1500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSyncing(int num, int total) {
        if (total > 0) {
            this.mSyncBar.setVisibility(0);
        }
        if (this.mConnected) {
            this.mTxtSyncItems.setText(String.format(Locale.US, this.mContext.getString(R.string.sync_syncing), Integer.valueOf(num), Integer.valueOf(total)));
        } else {
            int unsynced = total - num;
            this.mTxtSyncItems.setText(String.format(Locale.US, this.mContext.getString(R.string.sync_unsynced), Integer.valueOf(unsynced)));
        }
        updateIcons(this.mConnected ? SyncState.SYNCING : SyncState.DISCONNECTED);
    }

    @Override // com.kopin.solos.share.Sync.SyncUpdateListener
    public void onStart() {
        updateSyncing(Sync.getNumSyncedItems(), Sync.getNumUnsyncedItems());
    }

    @Override // com.kopin.solos.share.Sync.SyncUpdateListener
    public void onProgress(final int current, final int max) {
        if (!this.mContext.isFinishing()) {
            this.mContext.runOnUiThread(new Runnable() { // from class: com.kopin.solos.sync.SyncBarHelper.3
                @Override // java.lang.Runnable
                public void run() {
                    Log.i(SyncBarHelper.TAG, String.valueOf(current) + " / " + max);
                    SyncBarHelper.this.updateSyncing(current, max);
                }
            });
        }
    }

    @Override // com.kopin.solos.share.Sync.SyncUpdateListener
    public void onComplete() {
        if (!this.mContext.isFinishing()) {
            this.mContext.runOnUiThread(new Runnable() { // from class: com.kopin.solos.sync.SyncBarHelper.4
                @Override // java.lang.Runnable
                public void run() {
                    if (Sync.getNumSyncedItems() == Sync.getNumUnsyncedItems()) {
                        SyncBarHelper.this.updateIcons(SyncState.SYNCED);
                        SyncBarHelper.this.mTxtSyncItems.setText(R.string.sync_done);
                        SyncBarHelper.this.mImgCloudIcon.setImageResource(R.drawable.ic_tick_sync);
                        SyncBarHelper.this.mSyncBar.startAnimation(SyncBarHelper.this.mAnimFade);
                        SyncBarHelper.this.mSyncBar.setVisibility(8);
                    }
                    if (SyncBarHelper.this.mUiSyncRefresh != null) {
                        SyncBarHelper.this.mUiSyncRefresh.refresh();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIcons(SyncState syncState) {
        this.mImgShare.setVisibility(syncState == SyncState.SHARED ? 0 : 8);
        this.mImgCloud.setVisibility(syncState == SyncState.SHARED ? 8 : 0);
        this.mImgCloudIcon.setVisibility(syncState != SyncState.SHARED ? 0 : 8);
        switch (syncState) {
            case DISCONNECTED:
                this.mImgCloudIcon.setImageResource(R.drawable.ic_cross_sync);
                break;
            case SYNCING:
                this.mImgCloudIcon.setImageResource(R.drawable.ic_sync);
                break;
            case SYNCED:
                this.mImgCloudIcon.setImageResource(R.drawable.ic_sync);
                break;
        }
    }
}
