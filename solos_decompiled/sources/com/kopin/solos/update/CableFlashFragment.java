package com.kopin.solos.update;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.kopin.accessory.packets.CapabilityType;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.SolosDevice;
import com.kopin.pupil.update.ui.CircularProgressBar;
import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.solos.R;
import com.kopin.solos.cabledfu.CableDevice;
import com.kopin.solos.cabledfu.CableFlash;
import com.kopin.solos.sensors.SensorsConnector;

/* JADX INFO: loaded from: classes24.dex */
public class CableFlashFragment extends Fragment {
    private static final int MIN_BATTERY = 50;
    private View mCancelButton;
    private FrameLayout mContainer;
    private TextView mTitle;
    private boolean revertOnError = false;
    private boolean recoverOnFail = false;
    private FlashPages currentPage = FlashPages.NONE;
    private View.OnClickListener cancelClick = new View.OnClickListener() { // from class: com.kopin.solos.update.CableFlashFragment.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CableFlashFragment.this.getActivity().finish();
        }
    };
    private View.OnClickListener flashClick = new View.OnClickListener() { // from class: com.kopin.solos.update.CableFlashFragment.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CableFlashFragment.this.showFrame(FlashPages.INITIALISE);
            CableFlashFragment.this.performFlash();
        }
    };
    private final FirmwareFlash.FlashProgressListener mFlashUIListener = new FirmwareFlash.FlashProgressListener() { // from class: com.kopin.solos.update.CableFlashFragment.8
        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onStatusChanged() {
            CableFlashFragment.this.checkPage();
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onProgressUpdate(String message) {
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onError(FirmwareFlash.FlashError error, String message) {
            if (!CableFlashFragment.this.revertOnError || error.isCriticalError) {
                if (CableFlashFragment.this.recoverOnFail || error == FirmwareFlash.FlashError.MAINTENANCE_VERSION_CHECK_FAILED) {
                    CableFlashFragment.this.performRecovery();
                    return;
                } else {
                    CableFlashFragment.this.showError();
                    return;
                }
            }
            CableFlashFragment.this.performRevert();
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onFlashComplete(int error, String message) {
            if (error == 1) {
                CableFlashFragment.this.showError();
            } else if (error == 2) {
                CableFlashFragment.this.showComplete(true);
            } else {
                CableFlashFragment.this.showComplete(false);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onSwitchMode(int wasBank, int targetBank) {
            if (targetBank == 1) {
                CableFlashFragment.this.updateScreen(FlashPages.REBOOT);
            } else if (targetBank == 2) {
                CableFlashFragment.this.updateScreen(FlashPages.INITIALISE);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onMaintenanceMode(int bootBank) {
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onProgress(int progress) {
            CableFlashFragment.this.updateProgress(progress);
        }
    };

    private enum FlashPages {
        NONE(0, false),
        INITIALISE(R.id.frameInitialise, false),
        INSTRUCTIONS(R.id.frameInstructions, true),
        INSTALL(R.id.frameInstall, false),
        LOW_BATTERY(R.id.frameLowBattery, true),
        REBOOT(R.id.frameReboot, false),
        ERROR(R.id.frameError, false),
        COMPLETE(R.id.frameComplete, false);

        private final boolean mCancelable;
        private final int mFrameId;

        FlashPages(int resId, boolean cancel) {
            this.mFrameId = resId;
            this.mCancelable = cancel;
        }

        public int getFrameResId() {
            return this.mFrameId;
        }

        public boolean canCancel() {
            return this.mCancelable;
        }
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // android.app.Fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_flash, container, false);
        CableDevice.init(getActivity());
        setupUI(mView);
        return mView;
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
        CableDevice.stopScan();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        CableFlash.prepareForUpdate();
        PupilDevice.requestStatus();
        checkPage();
    }

    private void setupUI(View view) {
        this.mTitle = (TextView) view.findViewById(R.id.txtFlashTitle);
        this.mContainer = (FrameLayout) view.findViewById(R.id.viewContainer);
        this.mCancelButton = view.findViewById(R.id.btnCancel);
        this.mCancelButton.setOnClickListener(this.cancelClick);
        PupilDevice.DeviceStatus deviceStatus = PupilDevice.currentDeviceStatus();
        if (deviceStatus.getBattery() < 50) {
            ((TextView) this.mContainer.findViewById(R.id.txtLowBattery)).setText(String.format(getActivity().getString(R.string.bt_lowbattery), 50));
            showFrame(FlashPages.LOW_BATTERY);
            return;
        }
        showFrame(FlashPages.INSTRUCTIONS);
        this.mContainer.findViewById(R.id.btnUpdate).setOnClickListener(this.flashClick);
        for (int i = 0; i < this.mContainer.getChildCount(); i++) {
            View v = this.mContainer.getChildAt(i).findViewById(R.id.btnDone);
            if (v != null) {
                v.setOnClickListener(this.cancelClick);
            }
        }
        ((CircularProgressBar) this.mContainer.findViewById(R.id.koProgress)).setProgress(0);
        ((CircularProgressBar) this.mContainer.findViewById(R.id.koProgress)).setTextView((TextView) this.mContainer.findViewById(R.id.txtPercent));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFrame(FlashPages frame) {
        this.currentPage = frame;
        int idToShow = frame.getFrameResId();
        for (int i = 0; i < this.mContainer.getChildCount(); i++) {
            View v = this.mContainer.getChildAt(i);
            v.setVisibility(v.getId() == idToShow ? 0 : 8);
        }
        this.mCancelButton.setVisibility(frame.canCancel() ? 0 : 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performFlash() {
        this.revertOnError = true;
        CableFlash.flashUpdate(SolosDevice.getAntBridgeId(), this.mFlashUIListener);
        SensorsConnector.connectAntBridge(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performRevert() {
        this.revertOnError = false;
        if (CableDevice.isDfuMode()) {
            CableFlash.revertAndReset(this.mFlashUIListener);
        } else {
            showError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performRecovery() {
        this.revertOnError = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError() {
        if (isVisible()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.update.CableFlashFragment.3
                @Override // java.lang.Runnable
                public void run() {
                    CableFlashFragment.this.mTitle.setText(CableFlashFragment.this.getActivity().getResources().getString(R.string.bt_error_title));
                    CableFlashFragment.this.showFrame(FlashPages.ERROR);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showComplete(boolean warn) {
        if (isVisible()) {
            final boolean warning = warn || !this.revertOnError;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() { // from class: com.kopin.solos.update.CableFlashFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    PupilDevice.requestCapabilities(CapabilityType.ANT_MODULE, CapabilityType.ANT_MODULE_VERSION);
                    ((TextView) CableFlashFragment.this.mContainer.findViewById(R.id.txtComplete)).setText(warning ? R.string.bt_errorsoccurred_warn : R.string.bt_installed);
                    CableFlashFragment.this.mContainer.findViewById(R.id.iconComplete).setVisibility(warning ? 8 : 0);
                    CableFlashFragment.this.mContainer.findViewById(R.id.iconCompleteWarn).setVisibility(warning ? 0 : 8);
                    ((TextView) CableFlashFragment.this.mContainer.findViewById(R.id.txtVersion)).setText(String.format(!warning ? "Version %s installed" : "Reverted to version %s", CableFlash.updateVersion()));
                    CableFlashFragment.this.showFrame(FlashPages.COMPLETE);
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScreen(final FlashPages page) {
        if (isVisible()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.update.CableFlashFragment.5
                @Override // java.lang.Runnable
                public void run() {
                    CableFlashFragment.this.showFrame(page);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgress(final int percent) {
        final int msgId;
        if (isVisible()) {
            if (this.revertOnError) {
                msgId = R.string.bt_installing;
            } else {
                msgId = this.recoverOnFail ? R.string.bt_reverting : R.string.bt_recovering;
            }
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.update.CableFlashFragment.6
                @Override // java.lang.Runnable
                public void run() {
                    ((TextView) CableFlashFragment.this.mContainer.findViewById(R.id.textProgress)).setText(msgId);
                    CableFlashFragment.this.showFrame(FlashPages.INSTALL);
                    ((CircularProgressBar) CableFlashFragment.this.mContainer.findViewById(R.id.koProgress)).setProgress(percent);
                }
            });
        }
    }

    private void updateProgress(final String msg) {
        if (isVisible()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.update.CableFlashFragment.7
                @Override // java.lang.Runnable
                public void run() {
                    CableFlashFragment.this.showFrame(FlashPages.INSTALL);
                    ((TextView) CableFlashFragment.this.mContainer.findViewById(R.id.textProgress)).setText(msg);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPage() {
        PupilDevice.DeviceStatus deviceStatus = PupilDevice.currentDeviceStatus();
        switch (this.currentPage) {
            case LOW_BATTERY:
                if (deviceStatus.getBattery() >= 50) {
                    showFrame(FlashPages.INSTRUCTIONS);
                }
                break;
            case INSTRUCTIONS:
                if (deviceStatus.getBattery() < 50) {
                    showFrame(FlashPages.LOW_BATTERY);
                }
                break;
        }
    }
}
