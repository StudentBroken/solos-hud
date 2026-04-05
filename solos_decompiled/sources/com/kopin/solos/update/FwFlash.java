package com.kopin.solos.update;

import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.cabledfu.CableFlash;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.Template;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class FwFlash extends Template<Long> {
    public static final String PAGE_ID = MetricType.FWFLASH.getResource();
    private int[] PROGRESS_ICONS;
    private int mCurProgressIdx;
    private final FirmwareFlash.FlashProgressListener mFlashProgress;

    public FwFlash(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        this.PROGRESS_ICONS = new int[]{R.drawable.headset_progress_0, R.drawable.headset_progress_1, R.drawable.headset_progress_2, R.drawable.headset_progress_3};
        this.mCurProgressIdx = 0;
        this.mFlashProgress = new FirmwareFlash.FlashProgressListener() { // from class: com.kopin.solos.update.FwFlash.1
            @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
            public void onStatusChanged() {
            }

            @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
            public void onProgressUpdate(String s) {
            }

            @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
            public void onError(FirmwareFlash.FlashError flashError, String s) {
                FwFlash.this.updateProgressIcon(R.drawable.ic_bricked);
                FwFlash.this.updateProgressMessage(s);
            }

            @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
            public void onFlashComplete(int i, String s) {
                if (i != 1 && i != 2) {
                    FwFlash.this.updateProgressIcon(R.drawable.ic_maintenance_mode);
                } else {
                    FwFlash.this.updateProgressIcon(R.drawable.ic_bricked);
                }
                FwFlash.this.updateProgressMessage(s);
            }

            @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
            public void onSwitchMode(int i, int i1) {
                if (i != 2 || i1 != 1) {
                    FwFlash.this.updateProgressIcon(R.drawable.ic_maintenance_mode);
                    FwFlash.this.updateProgressMessage(String.format(FwFlash.this.mAppService.getString(R.string.fwflash_installing), 0));
                } else {
                    FwFlash.this.updateProgressSpinner();
                    FwFlash.this.updateProgressMessage(String.format(FwFlash.this.mAppService.getString(R.string.fwflash_installing), 100));
                }
            }

            @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
            public void onMaintenanceMode(int i) {
                FwFlash.this.updateProgressIcon(i == 1 ? R.drawable.ic_vc_icon : R.drawable.ic_maintenance_mode);
                FwFlash.this.updateProgressMessage(FwFlash.this.mAppService.getString(i == 1 ? R.string.fwflash_application_mode : R.string.fwflash_maintenance_mode));
            }

            @Override // com.kopin.pupil.update.util.FirmwareFlash.FlashProgressListener
            public void onProgress(int i) {
                FwFlash.this.updateProgressSpinner();
                FwFlash.this.updateProgressMessage(String.format(FwFlash.this.mAppService.getString(R.string.fwflash_installing), Integer.valueOf(i)));
            }
        };
        addPage(MetricType.FWFLASH);
        FirmwareFlash.registerFlashUIListener(this.mFlashProgress);
        CableFlash.registerFlashUIListener(this.mFlashProgress);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return true;
    }

    @Override // com.kopin.solos.metrics.Template
    protected void onServiceConnected(HardwareReceiverService service) {
    }

    @Override // com.kopin.solos.metrics.Template
    protected void onServiceDisconnected() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgressSpinner() {
        this.mAppService.addBitmapFromResource("fwflash_update", this.PROGRESS_ICONS[this.mCurProgressIdx]);
        this.mCurProgressIdx++;
        if (this.mCurProgressIdx == this.PROGRESS_ICONS.length) {
            this.mCurProgressIdx = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgressIcon(int iconId) {
        this.mAppService.addBitmapFromResource("fwflash_update", iconId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgressMessage(String msg) {
        updateElement(PAGE_ID, "message", "content", msg);
    }
}
