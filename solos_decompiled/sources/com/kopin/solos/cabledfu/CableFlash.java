package com.kopin.solos.cabledfu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.update.util.FirmwareFetcher;
import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.solos.cabledfu.CableDfu;
import com.kopin.solos.cabledfu.DfuPackets;
import java.io.File;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes38.dex */
public class CableFlash implements CableDfu.DfuResponseListener {
    static final String FIRMWARE_CACHE = "/sdcard/";
    private static final int FLASH_MAX_CHUNK_ATTEMPTS = 2;
    private static final int FLASH_MAX_ERASE_ATTEMPTS = 2;
    protected static final int FLASH_RESPONSE_TIMEOUT = 30000;
    private static String NEXT_UPDATE_IMAGE = null;
    private static String NEXT_UPDATE_MANIFEST = null;
    private static final String PREFS = "cableflash-";
    private static final String PREF_SAFE_IMAGE = "safeBin";
    private static final String PREF_SAFE_MANIFEST = "safeXml";
    private static final String PREF_SAFE_UPDATED = "safeUpdated";
    private static final String PREF_SAFE_VERSION = "safeVer";
    private static final String PREF_UPDATE_IMAGE = "updateBin";
    private static final String PREF_UPDATE_MANIFEST = "updateXml";
    private static final String PREF_UPDATE_VERSION = "updateVer";
    private static final String TAG = "CableUpdater";
    private static Context mContext;
    protected static CableFlash self;
    private FirmwareFlash.MaintenanceModeListener mModeCb;
    private DfuFile mNextUpdate;
    private SharedPreferences mPrefs;
    private String mProduct;
    protected FlashUpdater mUpdater;
    private static String LAST_GOOD_IMAGE = null;
    private static String LAST_GOOD_MANIFEST = null;
    public static final FirmwareFlash.FlashProgressListener mProgress = new FirmwareFlash.FlashProgressListener() { // from class: com.kopin.solos.cabledfu.CableFlash.1
        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onStatusChanged() {
            Log.e(CableFlash.TAG, "Status changed, refresh UI");
            for (FirmwareFlash.FlashProgressListener cb : CableFlash.self.mProgressCbs) {
                cb.onStatusChanged();
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onError(FirmwareFlash.FlashError error, String message) {
            Log.e(CableFlash.TAG, "Error " + error + ": " + message);
            for (FirmwareFlash.FlashProgressListener cb : CableFlash.self.mProgressCbs) {
                cb.onError(error, message);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onProgressUpdate(String message) {
            Log.d(CableFlash.TAG, message);
            for (FirmwareFlash.FlashProgressListener cb : CableFlash.self.mProgressCbs) {
                cb.onProgressUpdate(message);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onProgress(int percent) {
            Log.d(CableFlash.TAG, "Progress: " + percent);
            for (FirmwareFlash.FlashProgressListener cb : CableFlash.self.mProgressCbs) {
                cb.onProgress(percent);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onSwitchMode(int wasBank, int targetBank) {
            Log.d(CableFlash.TAG, "rebooting from boot bank " + wasBank + " to bank " + targetBank);
            for (FirmwareFlash.FlashProgressListener cb : CableFlash.self.mProgressCbs) {
                cb.onSwitchMode(wasBank, targetBank);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onMaintenanceMode(int bootBank) {
            Log.d(CableFlash.TAG, "boot bank " + bootBank);
            for (FirmwareFlash.FlashProgressListener cb : CableFlash.self.mProgressCbs) {
                cb.onMaintenanceMode(bootBank);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onFlashComplete(int error, String message) {
            Log.d(CableFlash.TAG, "Flash complete: " + error + ": " + message);
            for (FirmwareFlash.FlashProgressListener cb : CableFlash.self.mProgressCbs) {
                cb.onFlashComplete(error, message);
            }
        }
    };
    private boolean mUserCancelled = false;
    private ArrayList<FirmwareFlash.FlashProgressListener> mProgressCbs = new ArrayList<>();
    private FirmwareFetcher.ResultCallback mFetcherListener = new FirmwareFetcher.ResultCallback() { // from class: com.kopin.solos.cabledfu.CableFlash.2
        @Override // com.kopin.pupil.update.util.FirmwareFetcher.ResultCallback
        public void onVersions(FirmwareFetcher.VersionInfo[] versions) {
            Log.d(CableFlash.TAG, "fetched versions");
        }

        @Override // com.kopin.pupil.update.util.FirmwareFetcher.ResultCallback
        public void onLatestVersion(FirmwareFetcher.VersionInfo version) {
            if (!CableFlash.updateAvailable() || !CableFlash.isUpdateHigherVersion(version.Version, true)) {
                FirmwareFetcher.downloadVersion(CableFlash.mContext, version, this);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFetcher.ResultCallback
        public void onError(String msg) {
            Log.d(CableFlash.TAG, "Error: " + msg);
        }

        @Override // com.kopin.pupil.update.util.FirmwareFetcher.ResultCallback
        public void onDownloaded(FirmwareFetcher.VersionInfo versionInfo) {
            CableFlash.this.mPrefs.edit().putString(CableFlash.PREF_UPDATE_VERSION, versionInfo.Version).putString(CableFlash.PREF_UPDATE_IMAGE, versionInfo.Filename).putString(CableFlash.PREF_UPDATE_MANIFEST, versionInfo.XmlFilename).commit();
            String unused = CableFlash.NEXT_UPDATE_IMAGE = versionInfo.Filename;
            String unused2 = CableFlash.NEXT_UPDATE_MANIFEST = versionInfo.XmlFilename;
            CableFlash.this.loadBinFileArray(versionInfo.Product);
        }
    };

    protected CableFlash(Context context, FirmwareFlash.MaintenanceModeListener modeListener) {
        mContext = context;
        this.mModeCb = modeListener;
        CableDevice.setResponseListener(this);
    }

    protected void startFirmwareUpdate(DfuFile file) {
        FlashUpdater flashUpdaterCreateFirmwareUpdater;
        if (this.mProduct.contentEquals("Cable")) {
            flashUpdaterCreateFirmwareUpdater = LegacyFirmwareUpdater.createFirmwareUpdater(file, CableDevice.getCableId());
        } else {
            flashUpdaterCreateFirmwareUpdater = SecureFirmwareUpdater.createFirmwareUpdater(file, CableDevice.getCableId());
        }
        this.mUpdater = flashUpdaterCreateFirmwareUpdater;
        this.mUpdater.start();
    }

    protected void startFirmwareUpdate(DfuFile file, int cableId) {
        FlashUpdater flashUpdaterCreateFirmwareUpdater;
        if (this.mProduct.contentEquals("Cable")) {
            flashUpdaterCreateFirmwareUpdater = LegacyFirmwareUpdater.createFirmwareUpdater(file, cableId);
        } else {
            flashUpdaterCreateFirmwareUpdater = SecureFirmwareUpdater.createFirmwareUpdater(file, cableId);
        }
        this.mUpdater = flashUpdaterCreateFirmwareUpdater;
        this.mUpdater.start();
    }

    protected void resetAndRevert() {
        FlashUpdater flashUpdaterCreateFirmwareUpdater;
        if (this.mProduct.contentEquals("Cable")) {
            flashUpdaterCreateFirmwareUpdater = LegacyFirmwareUpdater.createFirmwareUpdater(null, CableDevice.getCableId());
        } else {
            flashUpdaterCreateFirmwareUpdater = SecureFirmwareUpdater.createFirmwareUpdater(null, CableDevice.getCableId());
        }
        this.mUpdater = flashUpdaterCreateFirmwareUpdater;
        this.mUpdater.start();
    }

    public static CableFlash init(Context context) {
        self = new CableFlash(context, null);
        return self;
    }

    public static CableFlash init(Context context, FirmwareFlash.MaintenanceModeListener modeListener) {
        self = new CableFlash(context, modeListener);
        return self;
    }

    public static CableFlash init(Context context, String product, FirmwareFlash.MaintenanceModeListener modeListener, boolean devServer) {
        self = new CableFlash(context, modeListener);
        setProduct(product, devServer);
        return self;
    }

    public static void setProduct(String product) {
        setProduct(product, false);
    }

    public static void setProduct(String product, boolean devServer) {
        if (self.mUpdater != null) {
            Log.d(TAG, "Update in progress, unable to switch product");
            return;
        }
        self.mProduct = product;
        CableFlash cableFlash = self;
        CableFlash cableFlash2 = self;
        cableFlash.mPrefs = mContext.getSharedPreferences(PREFS + product, 0);
        CableFlash cableFlash3 = self;
        FirmwareFetcher.getLatestVersion(mContext, product, devServer, self.mFetcherListener);
        self.loadBinFileArray(product);
    }

    public static void setProduct(String product, FirmwareFetcher.ResultCallback resultCallback) {
        if (resultCallback != null) {
            self.mFetcherListener = resultCallback;
        }
        setProduct(product);
    }

    public static void registerFlashUIListener(FirmwareFlash.FlashProgressListener cb) {
        if (!self.mProgressCbs.contains(cb)) {
            self.mProgressCbs.add(cb);
        }
    }

    public static void unregisterFlashUIListener(FirmwareFlash.FlashProgressListener cb) {
        self.mProgressCbs.remove(cb);
    }

    public static boolean isActive() {
        if (self.mUpdater == null) {
            return false;
        }
        return self.mUpdater.isAlive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean loadBinFileArray(String product) {
        boolean safeIsDownloaded = this.mPrefs.getBoolean(PREF_SAFE_UPDATED, true);
        if (safeIsDownloaded) {
            LAST_GOOD_IMAGE = this.mPrefs.getString(PREF_SAFE_IMAGE, null);
            LAST_GOOD_MANIFEST = this.mPrefs.getString(PREF_SAFE_MANIFEST, null);
        }
        NEXT_UPDATE_IMAGE = this.mPrefs.getString(PREF_UPDATE_IMAGE, null);
        NEXT_UPDATE_MANIFEST = this.mPrefs.getString(PREF_UPDATE_MANIFEST, null);
        if (NEXT_UPDATE_IMAGE != null && NEXT_UPDATE_MANIFEST != null) {
            this.mNextUpdate = DfuFile.load(mContext, NEXT_UPDATE_MANIFEST, NEXT_UPDATE_IMAGE);
        }
        return updateAvailable();
    }

    private void markUpdateAsGood() {
        this.mPrefs.edit().putString(PREF_SAFE_VERSION, PupilDevice.currentDeviceInfo().mVersion).putString(PREF_SAFE_IMAGE, NEXT_UPDATE_IMAGE).putString(PREF_SAFE_MANIFEST, NEXT_UPDATE_MANIFEST).putBoolean(PREF_SAFE_UPDATED, true).putString(PREF_UPDATE_IMAGE, null).putString(PREF_UPDATE_MANIFEST, null).putString(PREF_UPDATE_VERSION, null).commit();
        loadBinFileArray(null);
    }

    public static boolean updateAvailable() {
        return self.mNextUpdate != null && self.mNextUpdate.exists();
    }

    public static String updateVersion() {
        if (self.mNextUpdate == null) {
            return null;
        }
        return self.mNextUpdate.getVersion();
    }

    public static boolean isUpdateHigherVersion(String verToCompare) {
        return isUpdateHigherVersion(verToCompare, false);
    }

    static boolean isUpdateHigherVersion(String verToCompare, boolean orEqual) {
        if (verToCompare == null || verToCompare.isEmpty() || !updateAvailable()) {
            return false;
        }
        String updateVer = updateVersion();
        return isHigherVersion(updateVer, verToCompare, orEqual);
    }

    static boolean isHigherVersion(String updateVer, String verToCompare, boolean orEqual) {
        if (updateVer == null || updateVer.isEmpty()) {
            return false;
        }
        String[] v1 = verToCompare.split("\\.");
        String[] v2 = updateVer.split("\\.");
        if (v1 == null || v1.length != 3) {
            return false;
        }
        if (v2 == null || v2.length != 3) {
            return false;
        }
        if (Integer.valueOf(v2[0]).intValue() > Integer.valueOf(v1[0]).intValue()) {
            return true;
        }
        if (Integer.valueOf(v2[0]).intValue() < Integer.valueOf(v1[0]).intValue()) {
            return false;
        }
        if (Integer.valueOf(v2[1]).intValue() > Integer.valueOf(v1[1]).intValue()) {
            return true;
        }
        if (Integer.valueOf(v2[1]).intValue() < Integer.valueOf(v1[1]).intValue()) {
            return false;
        }
        if (Integer.valueOf(v2[2]).intValue() > Integer.valueOf(v1[2]).intValue()) {
            return true;
        }
        if (Integer.valueOf(v2[2]).intValue() < Integer.valueOf(v1[2]).intValue()) {
            return false;
        }
        return orEqual;
    }

    public static void checkMode(FirmwareFlash.FlashProgressListener cb) {
        if (self.mUpdater != null) {
            cb.onFlashComplete(-1, "Operation in progress");
        }
    }

    public static void prepareForUpdate() {
        mProgress.onMaintenanceMode(2);
    }

    public static void flashUpdate(DfuFile updateFile, FirmwareFlash.FlashProgressListener cb) {
        if (self.mUpdater != null) {
            cb.onFlashComplete(-1, "Operation in progress");
        } else {
            registerFlashUIListener(cb);
            self.startFirmwareUpdate(updateFile);
        }
    }

    public static void flashUpdate(int cableId, FirmwareFlash.FlashProgressListener cb) {
        if (self.mNextUpdate == null) {
            cb.onFlashComplete(-1, "Image doesn't exist");
        }
        registerFlashUIListener(cb);
        self.startFirmwareUpdate(self.mNextUpdate, cableId);
    }

    public static void revertAndReset(FirmwareFlash.FlashProgressListener cb) {
        if (self.mUpdater != null) {
            cb.onFlashComplete(-1, "Operation in progress");
        } else {
            registerFlashUIListener(cb);
            self.resetAndRevert();
        }
    }

    static File updateFile(String name) {
        File dir;
        if (name.lastIndexOf(47) != -1) {
            name = name.substring(name.lastIndexOf(47));
        }
        if (isExternalStorageWritable()) {
            CableFlash cableFlash = self;
            dir = mContext.getExternalFilesDir(null);
        } else {
            CableFlash cableFlash2 = self;
            dir = mContext.getFilesDir();
        }
        return new File(dir, name);
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return "mounted".equals(state);
    }

    static void acceptUpdate() {
        self.markUpdateAsGood();
    }

    @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
    public void onConnected(boolean dfuMode) {
        notifyUpdater(false);
        if (dfuMode && this.mUpdater == null && this.mModeCb != null) {
            this.mModeCb.onUnexpectedMaintenanceMode();
        }
    }

    @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
    public void onDisconnected(boolean wasLoss) {
        notifyUpdater(wasLoss);
    }

    @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
    public void onReady() {
    }

    @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
    public void onResponse(DfuPackets.DfuPacket response) {
        if (this.mUpdater != null) {
            synchronized (this.mUpdater) {
                this.mUpdater.notify(response);
            }
        }
    }

    public static class FlashUpdater extends Thread {
        protected FirmwareFlash.FlashError flashError;
        protected int mBytesWritten;
        protected int mCableId;
        protected FirmwareFlash.FlashProgressListener mCb;

        public FlashUpdater(String name, int cableId) {
            super(name);
            this.flashError = FirmwareFlash.FlashError.NONE;
            this.mCb = CableFlash.mProgress;
            this.mCableId = cableId;
        }

        public void start(FirmwareFlash.FlashProgressListener cb) {
            this.mCb = cb;
            start();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            CableFlash.self.mUpdater = null;
            if (this.flashError != FirmwareFlash.FlashError.NONE) {
                CableFlash.mProgress.onError(this.flashError, "An error occurred");
            } else {
                CableFlash.mProgress.onFlashComplete(0, "Flash Complete");
            }
        }

        public void notify(DfuPackets.DfuPacket packet) {
        }

        public int progressAsPercent() {
            return 0;
        }
    }

    public void notifyUpdater(boolean isError) {
        if (this.mUpdater != null) {
            synchronized (this.mUpdater) {
                this.mUpdater.flashError = isError ? FirmwareFlash.FlashError.UNKNOWN : FirmwareFlash.FlashError.NONE;
                this.mUpdater.notify();
            }
        }
    }
}
