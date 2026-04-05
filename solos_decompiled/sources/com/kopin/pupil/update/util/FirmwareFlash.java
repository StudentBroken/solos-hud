package com.kopin.pupil.update.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import com.kopin.accessory.packets.FlashResponseContent;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.PupilMaintenance;
import com.kopin.pupil.update.util.FirmwareFetcher;
import com.kopin.pupil.update.util.FirmwareFile;
import java.io.File;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes13.dex */
public class FirmwareFlash {
    private static final int DEVICE_BANK_UNDEFINED = 0;
    static final String FIRMWARE_CACHE = "/sdcard/";
    private static final int FLASH_MAX_CHUNK_ATTEMPTS = 2;
    private static final int FLASH_MAX_ERASE_ATTEMPTS = 2;
    public static final int FLASH_PROGRESS_BOOT_TO_APP = -2;
    public static final int FLASH_PROGRESS_BOOT_TO_MAINTENANCE = -1;
    private static final int FLASH_RESPONSE_TIMEOUT = 60000;
    public static final int FLASH_RESULT_CRITICAL = 1;
    public static final int FLASH_RESULT_OK = 0;
    public static final int FLASH_RESULT_WARNING = 2;
    private static final String MAINTENANCE_MODE_MIN_VERSION = "6.1.0";
    private static String NEXT_UPDATE_IMAGE = null;
    private static String NEXT_UPDATE_MANIFEST = null;
    private static final String PREFS = "pupilflash-";
    private static final String PREF_PRODUCT = "product";
    private static final String PREF_SAFE_IMAGE = "safeBin";
    private static final String PREF_SAFE_MANIFEST = "safeXml";
    private static final String PREF_SAFE_UPDATED = "safeUpdated";
    private static final String PREF_SAFE_VERSION = "safeVer";
    private static final String PREF_UPDATE_IMAGE = "updateBin";
    private static final String PREF_UPDATE_MANIFEST = "updateXml";
    private static final String PREF_UPDATE_VERSION = "updateVer";
    private static final String TAG = "FlashUpdater";
    private static FirmwareFlash self;
    private int mBytesWritten;
    private Context mContext;
    private FirmwareFile mCustomUpdate;
    private FirmwareFile mLastKnownGood;
    private MaintenanceModeListener mModeCb;
    private FirmwareFile mNextUpdate;
    private SharedPreferences mPrefs;
    private FlashUpdater mUpdater;
    private static String LAST_GOOD_IMAGE = null;
    private static String LAST_GOOD_MANIFEST = null;
    private static final FlashProgressListener mProgress = new FlashProgressListener() { // from class: com.kopin.pupil.update.util.FirmwareFlash.1
        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onStatusChanged() {
            Log.e(FirmwareFlash.TAG, "Status changed, refresh UI");
            for (FlashProgressListener cb : FirmwareFlash.self.mProgressCbs) {
                cb.onStatusChanged();
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onError(FlashError error, String message) {
            Log.e(FirmwareFlash.TAG, "Error " + error + ": " + message);
            for (FlashProgressListener cb : FirmwareFlash.self.mProgressCbs) {
                cb.onError(error, message);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onProgressUpdate(String message) {
            Log.d(FirmwareFlash.TAG, message);
            for (FlashProgressListener cb : FirmwareFlash.self.mProgressCbs) {
                cb.onProgressUpdate(message);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onProgress(int percent) {
            Log.d(FirmwareFlash.TAG, "Progress: " + percent);
            for (FlashProgressListener cb : FirmwareFlash.self.mProgressCbs) {
                cb.onProgress(percent);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onSwitchMode(int wasBank, int targetBank) {
            Log.d(FirmwareFlash.TAG, "rebooting from boot bank " + wasBank + " to bank " + targetBank);
            for (FlashProgressListener cb : FirmwareFlash.self.mProgressCbs) {
                cb.onSwitchMode(wasBank, targetBank);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onMaintenanceMode(int bootBank) {
            Log.d(FirmwareFlash.TAG, "boot bank " + bootBank);
            for (FlashProgressListener cb : FirmwareFlash.self.mProgressCbs) {
                cb.onMaintenanceMode(bootBank);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onFlashComplete(int error, String message) {
            Log.d(FirmwareFlash.TAG, "Flash complete: " + error + ": " + message);
            for (FlashProgressListener cb : FirmwareFlash.self.mProgressCbs) {
                cb.onFlashComplete(error, message);
            }
        }
    };
    private int mBootBank = 0;
    protected int mExpectedBootBank = 0;
    private ImageToFlash mImageToFlash = ImageToFlash.NONE;
    private int mCurrentBootBank = -1;
    private boolean mUserCancelled = false;
    private ArrayList<FlashProgressListener> mProgressCbs = new ArrayList<>();
    private final PupilMaintenance.MaintenanceModeListener maintenanceModeListener = new PupilMaintenance.MaintenanceModeListener() { // from class: com.kopin.pupil.update.util.FirmwareFlash.2
        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onReconnection(boolean isMaintenanceMode) {
            FirmwareFlash.this.mBootBank = isMaintenanceMode ? 2 : 1;
            FirmwareFlash.this.notifyUpdater(false);
            FirmwareFlash.mProgress.onMaintenanceMode(FirmwareFlash.this.mBootBank);
            if (isMaintenanceMode && FirmwareFlash.this.mUpdater == null && FirmwareFlash.this.mModeCb != null) {
                FirmwareFlash.this.mModeCb.onUnexpectedMaintenanceMode();
            }
        }

        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onDisconnect(boolean wasLoss) {
            FirmwareFlash.this.mBootBank = 0;
        }

        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onFlashProgress(int bytes) {
            if (FirmwareFlash.this.mUpdater != null) {
                FirmwareFlash.mProgress.onProgress(progressAsPercent());
            }
        }

        private int progressAsPercent() {
            double pc = ((double) FirmwareFlash.this.mBytesWritten) / FirmwareFlash.this.mUpdater.bytesToFlash;
            return (int) Math.round(100.0d * pc);
        }

        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onFlashComplete(int totalBytes) {
            if (FirmwareFlash.this.mUpdater != null) {
                FirmwareFlash.this.mBytesWritten += totalBytes;
                FirmwareFlash.mProgress.onProgress(progressAsPercent());
            }
        }

        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onFlashErased() {
            FirmwareFlash.this.notifyUpdater(false);
        }

        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onChecksum(boolean checkOk) {
            if (checkOk) {
                FirmwareFlash.this.notifyUpdater(false);
            } else {
                FirmwareFlash.this.notifyUpdater(FlashError.FLASH_CHECKSUM_FAILED);
            }
        }

        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onVerified(boolean sigOk) {
            if (sigOk) {
                FirmwareFlash.this.notifyUpdater(false);
            } else {
                FirmwareFlash.this.notifyUpdater(FlashError.SIGNATURE_VERIFY_FAILED);
            }
        }

        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onFlashError(FlashResponseContent.FlashErrorCodes error) {
            FirmwareFlash.this.notifyUpdater(FlashError.errorResponse(error));
        }

        @Override // com.kopin.pupil.PupilMaintenance.MaintenanceModeListener
        public void onPowerChanged(int battery, boolean charging) {
            FirmwareFlash.mProgress.onStatusChanged();
        }
    };
    private final FirmwareFetcher.ResultCallback mFetcherListener = new FirmwareFetcher.ResultCallback() { // from class: com.kopin.pupil.update.util.FirmwareFlash.3
        @Override // com.kopin.pupil.update.util.FirmwareFetcher.ResultCallback
        public void onVersions(FirmwareFetcher.VersionInfo[] versions) {
        }

        @Override // com.kopin.pupil.update.util.FirmwareFetcher.ResultCallback
        public void onLatestVersion(FirmwareFetcher.VersionInfo version) {
            if (!FirmwareFlash.updateAvailable() || !FirmwareFlash.isUpdateHigherVersion(version.Version, true)) {
                FirmwareFetcher.downloadVersion(FirmwareFlash.this.mContext, version, this);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFetcher.ResultCallback
        public void onError(String msg) {
        }

        @Override // com.kopin.pupil.update.util.FirmwareFetcher.ResultCallback
        public void onDownloaded(FirmwareFetcher.VersionInfo versionInfo) {
            FirmwareFlash.this.mPrefs.edit().putString(FirmwareFlash.PREF_UPDATE_VERSION, versionInfo.Version).putString(FirmwareFlash.PREF_UPDATE_IMAGE, versionInfo.Filename).putString(FirmwareFlash.PREF_UPDATE_MANIFEST, versionInfo.XmlFilename).commit();
            String unused = FirmwareFlash.NEXT_UPDATE_IMAGE = versionInfo.Filename;
            String unused2 = FirmwareFlash.NEXT_UPDATE_MANIFEST = versionInfo.XmlFilename;
            FirmwareFlash.this.loadBinFileArray(versionInfo.Product);
        }
    };

    private enum FlashCommands {
        ENSURE_BOOT_BANK_1,
        SWITCH_TO_BOOT_BANK_2,
        ENSURE_BOOT_BANK_2,
        CHECK_MAINTENANCE_VERSION,
        ERASE_BANK_1,
        ERASE_BANK_1_A,
        ERASE_BANK_1_B,
        SEND_CHUNK,
        SWITCH_TO_BOOT_BANK_1,
        QUERY_BOOT_BANK,
        CHECK_VERSIONS,
        OVERWRITE_LOCAL_FILE
    }

    public interface FlashProgressListener {
        void onError(FlashError flashError, String str);

        void onFlashComplete(int i, String str);

        void onMaintenanceMode(int i);

        void onProgress(int i);

        void onProgressUpdate(String str);

        void onStatusChanged();

        void onSwitchMode(int i, int i2);
    }

    private enum ImageToFlash {
        NONE,
        UPDATE,
        LAST_GOOD,
        CUSTOM
    }

    public interface MaintenanceModeListener {
        void onUnexpectedMaintenanceMode();
    }

    public enum FlashError {
        NONE(false),
        MAINTENANCE_MODE_FAILED(true),
        APPLICATION_MODE_FAILED(true),
        ERASE_FAILED(true),
        FLASH_FAILED(false),
        FLASH_CHECKSUM_FAILED(false),
        SIGNATURE_VERIFY_FAILED(false),
        VERSION_CHECK_FAILED(false),
        MAINTENANCE_VERSION_CHECK_FAILED(true),
        UNKNOWN(true),
        WAITING(true);

        public final boolean isCriticalError;

        FlashError(boolean critical) {
            this.isCriticalError = critical;
        }

        static FlashError commandFailed(FlashCommands cmd) {
            switch (cmd) {
                case SWITCH_TO_BOOT_BANK_2:
                case ENSURE_BOOT_BANK_2:
                    return MAINTENANCE_MODE_FAILED;
                case SWITCH_TO_BOOT_BANK_1:
                case ENSURE_BOOT_BANK_1:
                    return APPLICATION_MODE_FAILED;
                case ERASE_BANK_1:
                case ERASE_BANK_1_A:
                case ERASE_BANK_1_B:
                    return ERASE_FAILED;
                case SEND_CHUNK:
                    return FLASH_FAILED;
                case CHECK_VERSIONS:
                    return VERSION_CHECK_FAILED;
                case CHECK_MAINTENANCE_VERSION:
                    return MAINTENANCE_VERSION_CHECK_FAILED;
                default:
                    return UNKNOWN;
            }
        }

        static FlashError errorResponse(FlashResponseContent.FlashErrorCodes err) {
            switch (err) {
                case ERASE_ERROR:
                case ERASE_INVALID_ARGUMENT:
                    return ERASE_FAILED;
                case PROGRAM_ERROR:
                case PROGRAM_INVALID_ARGUMENT:
                    return FLASH_FAILED;
                case NONE:
                    return NONE;
                default:
                    return UNKNOWN;
            }
        }
    }

    private FirmwareFlash(Context context, String product, MaintenanceModeListener modeListener) {
        this.mContext = context;
        this.mModeCb = modeListener;
        PupilMaintenance.setResponseListener(this.maintenanceModeListener);
    }

    public static void init(Context context, String product, MaintenanceModeListener modeListener) {
        self = new FirmwareFlash(context, product, modeListener);
        setProduct(product);
    }

    public static void setProduct(String product) {
        setProduct(product, false);
    }

    public static void setProduct(String product, boolean devServer) {
        if (self.mUpdater != null) {
            Log.d(TAG, "Update in progress, unable to switch product");
            return;
        }
        self.mPrefs = self.mContext.getSharedPreferences(PREFS + product, 0);
        self.mPrefs.edit().putString(PREF_PRODUCT, product).commit();
        FirmwareFetcher.getLatestVersion(self.mContext, product, devServer, self.mFetcherListener);
        self.loadBinFileArray(product);
    }

    public static void registerFlashUIListener(FlashProgressListener cb) {
        if (!self.mProgressCbs.contains(cb)) {
            self.mProgressCbs.add(cb);
        }
    }

    public static void unregisterFlashUIListener(FlashProgressListener cb) {
        self.mProgressCbs.remove(cb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean loadBinFileArray(String product) {
        this.mNextUpdate = null;
        boolean safeIsDownloaded = this.mPrefs.getBoolean(PREF_SAFE_UPDATED, true);
        if (safeIsDownloaded) {
            LAST_GOOD_IMAGE = this.mPrefs.getString(PREF_SAFE_IMAGE, null);
            LAST_GOOD_MANIFEST = this.mPrefs.getString(PREF_SAFE_MANIFEST, null);
        }
        if (LAST_GOOD_IMAGE != null && LAST_GOOD_MANIFEST != null) {
            this.mLastKnownGood = FirmwareFile.load(this.mContext, LAST_GOOD_MANIFEST, LAST_GOOD_IMAGE);
        } else {
            this.mLastKnownGood = FirmwareFile.loadFromAssets(this.mContext, product.toLowerCase() + "/lastknowngood.xml", product.toLowerCase() + "/lastknowngood.bin");
        }
        NEXT_UPDATE_IMAGE = this.mPrefs.getString(PREF_UPDATE_IMAGE, null);
        NEXT_UPDATE_MANIFEST = this.mPrefs.getString(PREF_UPDATE_MANIFEST, null);
        if (NEXT_UPDATE_IMAGE != null && NEXT_UPDATE_MANIFEST != null) {
            this.mNextUpdate = FirmwareFile.load(this.mContext, NEXT_UPDATE_MANIFEST, NEXT_UPDATE_IMAGE);
        }
        return updateAvailable();
    }

    private void markUpdateAsGood() {
        this.mPrefs.edit().putString(PREF_SAFE_VERSION, PupilDevice.currentDeviceInfo().mVersion).putString(PREF_SAFE_IMAGE, NEXT_UPDATE_IMAGE).putString(PREF_SAFE_MANIFEST, NEXT_UPDATE_MANIFEST).putBoolean(PREF_SAFE_UPDATED, true).putString(PREF_UPDATE_IMAGE, null).putString(PREF_UPDATE_MANIFEST, null).putString(PREF_UPDATE_VERSION, null).commit();
        loadBinFileArray(null);
    }

    public static boolean safeVersionAvailable() {
        return self.mLastKnownGood != null && self.mLastKnownGood.exists();
    }

    public static boolean updateAvailable() {
        PupilDevice.DeviceInfo deviceInfo = PupilDevice.currentDeviceInfo();
        if (deviceInfo.mModel == null || deviceInfo.mModel.isEmpty() || self.mPrefs == null) {
            return false;
        }
        boolean productMatched = deviceInfo.mModel.toLowerCase().contentEquals(self.mPrefs.getString(PREF_PRODUCT, ""));
        return productMatched && self.mNextUpdate != null && self.mNextUpdate.exists();
    }

    public static String safeVersion() {
        if (self.mLastKnownGood == null) {
            return null;
        }
        return self.mLastKnownGood.getVersion();
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

    public static void checkMode(FlashProgressListener cb) {
        if (self.mUpdater != null) {
            cb.onFlashComplete(-1, "Operation in progress");
        } else {
            self.mUpdater = new FlashUpdater("Check Boot Bank", new FlashCommands[]{FlashCommands.QUERY_BOOT_BANK});
            self.mUpdater.start();
        }
    }

    public static void flashUpdate(FlashProgressListener cb) {
        if (self.mUpdater != null) {
            cb.onFlashComplete(-1, "Operation in progress");
            return;
        }
        self.mImageToFlash = ImageToFlash.UPDATE;
        self.mUpdater = new FlashUpdater("Flash Update", new FlashCommands[]{FlashCommands.ENSURE_BOOT_BANK_1, FlashCommands.SWITCH_TO_BOOT_BANK_2, FlashCommands.ENSURE_BOOT_BANK_2, FlashCommands.CHECK_MAINTENANCE_VERSION, FlashCommands.ERASE_BANK_1_A, FlashCommands.ERASE_BANK_1_B, FlashCommands.SEND_CHUNK, FlashCommands.SWITCH_TO_BOOT_BANK_1, FlashCommands.ENSURE_BOOT_BANK_1, FlashCommands.CHECK_VERSIONS});
        self.mUpdater.start(cb);
    }

    public static void flashUpdate(FirmwareFile update, FlashProgressListener cb) {
        if (self.mUpdater != null) {
            cb.onFlashComplete(-1, "Operation in progress");
            return;
        }
        self.mImageToFlash = ImageToFlash.CUSTOM;
        self.mCustomUpdate = update;
        self.mUpdater = new FlashUpdater("Flash Update", new FlashCommands[]{FlashCommands.ENSURE_BOOT_BANK_1, FlashCommands.SWITCH_TO_BOOT_BANK_2, FlashCommands.ENSURE_BOOT_BANK_2, FlashCommands.CHECK_MAINTENANCE_VERSION, FlashCommands.ERASE_BANK_1_A, FlashCommands.ERASE_BANK_1_B, FlashCommands.SEND_CHUNK, FlashCommands.SWITCH_TO_BOOT_BANK_1, FlashCommands.ENSURE_BOOT_BANK_1, FlashCommands.CHECK_VERSIONS});
        self.mUpdater.start(cb);
    }

    public static void flashLastGood(FlashProgressListener cb) {
        if (self.mUpdater != null) {
            cb.onFlashComplete(-1, "Operation in progress");
            return;
        }
        self.mImageToFlash = ImageToFlash.LAST_GOOD;
        self.mUpdater = new FlashUpdater("Flash Revert", new FlashCommands[]{FlashCommands.ENSURE_BOOT_BANK_2, FlashCommands.ERASE_BANK_1_A, FlashCommands.ERASE_BANK_1_B, FlashCommands.SEND_CHUNK, FlashCommands.SWITCH_TO_BOOT_BANK_1, FlashCommands.ENSURE_BOOT_BANK_1});
        self.mUpdater.start(cb);
    }

    public static void recoverDevice(FlashProgressListener cb) {
        if (self.mUpdater != null) {
            cb.onFlashComplete(-1, "Operation in progress");
        } else {
            self.mUpdater = new FlashUpdater("Flash Recover", new FlashCommands[]{FlashCommands.QUERY_BOOT_BANK, FlashCommands.SWITCH_TO_BOOT_BANK_1, FlashCommands.ENSURE_BOOT_BANK_1});
            self.mUpdater.start(cb);
        }
    }

    public static void cancel() {
    }

    public static boolean isActive() {
        return self.mBootBank == 2 || self.mUpdater != null;
    }

    static File updateFile(String name) {
        if (name.lastIndexOf(47) != -1) {
            name = name.substring(name.lastIndexOf(47));
        }
        File dir = isExternalStorageWritable() ? self.mContext.getExternalFilesDir(null) : self.mContext.getFilesDir();
        return new File(dir, name);
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return "mounted".equals(state);
    }

    static void acceptUpdate() {
        self.markUpdateAsGood();
    }

    private static class FlashUpdater extends Thread {
        long bytesToFlash;
        FirmwareFile.FlashChunker chunker;
        FlashError hasError;
        boolean isCancelled;
        boolean isComplete;
        FlashProgressListener mCb;
        private int mChunkAttempt;
        private final FlashCommands[] mCommandSequence;
        private int mCurCommand;
        private int mFlashAttempt;

        public FlashUpdater(String name, FlashCommands[] sequence) {
            super(name);
            this.hasError = FlashError.NONE;
            this.mChunkAttempt = 0;
            this.mFlashAttempt = 0;
            this.mCommandSequence = sequence;
            if (FirmwareFlash.self.mImageToFlash != ImageToFlash.NONE) {
                switch (FirmwareFlash.self.mImageToFlash) {
                    case LAST_GOOD:
                        this.chunker = FirmwareFlash.self.mLastKnownGood.openBinary();
                        break;
                    case UPDATE:
                        this.chunker = FirmwareFlash.self.mNextUpdate.openBinary();
                        break;
                    case CUSTOM:
                        this.chunker = FirmwareFlash.self.mCustomUpdate.openBinary();
                        break;
                }
                this.bytesToFlash = this.chunker != null ? this.chunker.getBinarySize() : 0L;
            }
            FirmwareFlash.self.mBytesWritten = 0;
        }

        public void start(FlashProgressListener cb) {
            this.mCb = cb;
            start();
        }

        /* JADX WARN: Removed duplicated region for block: B:100:0x02c5  */
        /* JADX WARN: Removed duplicated region for block: B:134:0x0030 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instruction units count: 850
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.kopin.pupil.update.util.FirmwareFlash.FlashUpdater.run():void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyUpdater(boolean isError) {
        if (this.mUpdater != null) {
            synchronized (this.mUpdater) {
                this.mUpdater.hasError = isError ? FlashError.UNKNOWN : FlashError.NONE;
                this.mUpdater.notify();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyUpdater(FlashError error) {
        if (this.mUpdater != null) {
            synchronized (this.mUpdater) {
                this.mUpdater.hasError = error;
                this.mUpdater.notify();
            }
        }
    }
}
