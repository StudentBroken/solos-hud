package com.kopin.pupil.update.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.PupilMaintenance;
import com.kopin.pupil.update.R;
import com.kopin.pupil.update.util.FirmwareFlash;

/* JADX INFO: loaded from: classes13.dex */
public class FlashFragment extends Fragment {
    private static final int MIN_BATTERY = 50;
    private View mCancelButton;
    private FrameLayout mContainer;
    private TextView mTitle;
    private boolean revertOnError = false;
    private boolean recoverOnFail = false;
    private FlashPages currentPage = FlashPages.NONE;
    private View.OnClickListener cancelClick = new View.OnClickListener() { // from class: com.kopin.pupil.update.ui.FlashFragment.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashFragment.this.getActivity().finish();
        }
    };
    private View.OnClickListener flashClick = new View.OnClickListener() { // from class: com.kopin.pupil.update.ui.FlashFragment.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FlashFragment.this.showFrame(FlashPages.INITIALISE);
            FlashFragment.this.performFlash();
        }
    };
    private final FirmwareFlash.FlashProgressListener mFlashUIListener = new FirmwareFlash.FlashProgressListener() { // from class: com.kopin.pupil.update.ui.FlashFragment.8
        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onStatusChanged() {
            FlashFragment.this.checkPage();
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onProgressUpdate(String message) {
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onError(FirmwareFlash.FlashError error, String message) {
            if (!FlashFragment.this.revertOnError || error.isCriticalError) {
                if (FlashFragment.this.recoverOnFail || error == FirmwareFlash.FlashError.MAINTENANCE_VERSION_CHECK_FAILED) {
                    FlashFragment.this.performRecovery();
                    return;
                } else {
                    FlashFragment.this.showError();
                    return;
                }
            }
            FlashFragment.this.performRevert();
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onFlashComplete(int error, String message) {
            if (error == 1) {
                FlashFragment.this.showError();
            } else if (error == 2) {
                FlashFragment.this.showComplete(true);
            } else {
                FlashFragment.this.showComplete(false);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onSwitchMode(int wasBank, int targetBank) {
            if (targetBank == 1) {
                FlashFragment.this.updateScreen(FlashPages.REBOOT);
            } else if (targetBank == 2) {
                FlashFragment.this.updateScreen(FlashPages.INITIALISE);
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onMaintenanceMode(int bootBank) {
        }

        @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
        public void onProgress(int progress) {
            FlashFragment.this.updateProgress(progress);
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
        setupUI(mView);
        return mView;
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        PupilMaintenance.checkCurrentMode();
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
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
        FirmwareFlash.flashUpdate(this.mFlashUIListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performRevert() {
        this.revertOnError = false;
        if (FirmwareFlash.safeVersionAvailable()) {
            FirmwareFlash.flashLastGood(this.mFlashUIListener);
        } else {
            showError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performRecovery() {
        this.revertOnError = false;
        FirmwareFlash.recoverDevice(this.mFlashUIListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError() {
        if (isVisible()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.pupil.update.ui.FlashFragment.3
                @Override // java.lang.Runnable
                public void run() {
                    FlashFragment.this.mTitle.setText(FlashFragment.this.getActivity().getResources().getString(R.string.bt_error_title));
                    FlashFragment.this.showFrame(FlashPages.ERROR);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showComplete(boolean warn) {
        if (isVisible()) {
            final boolean warning = warn || !this.revertOnError;
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.pupil.update.ui.FlashFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    ((TextView) FlashFragment.this.mContainer.findViewById(R.id.txtComplete)).setText(warning ? R.string.bt_errorsoccurred_warn : R.string.bt_installed);
                    FlashFragment.this.mContainer.findViewById(R.id.iconComplete).setVisibility(warning ? 8 : 0);
                    FlashFragment.this.mContainer.findViewById(R.id.iconCompleteWarn).setVisibility(warning ? 0 : 8);
                    ((TextView) FlashFragment.this.mContainer.findViewById(R.id.txtVersion)).setText(String.format(!warning ? "Version %s installed" : "Reverted to version %s", PupilDevice.currentDeviceInfo().mVersion));
                    FlashFragment.this.showFrame(FlashPages.COMPLETE);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScreen(final FlashPages page) {
        if (isVisible()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.pupil.update.ui.FlashFragment.5
                @Override // java.lang.Runnable
                public void run() {
                    FlashFragment.this.showFrame(page);
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
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.pupil.update.ui.FlashFragment.6
                @Override // java.lang.Runnable
                public void run() {
                    ((TextView) FlashFragment.this.mContainer.findViewById(R.id.textProgress)).setText(msgId);
                    FlashFragment.this.showFrame(FlashPages.INSTALL);
                    ((CircularProgressBar) FlashFragment.this.mContainer.findViewById(R.id.koProgress)).setProgress(percent);
                }
            });
        }
    }

    private void updateProgress(final String msg) {
        if (isVisible()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.pupil.update.ui.FlashFragment.7
                @Override // java.lang.Runnable
                public void run() {
                    FlashFragment.this.showFrame(FlashPages.INSTALL);
                    ((TextView) FlashFragment.this.mContainer.findViewById(R.id.textProgress)).setText(msg);
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
