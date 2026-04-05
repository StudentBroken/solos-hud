package com.kopin.solos.update;

import android.os.Bundle;
import com.kopin.solos.R;
import com.kopin.solos.cabledfu.CableFlash;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.pages.PageNav;

/* JADX INFO: loaded from: classes24.dex */
public class CableFlashActivity extends BaseActivity {
    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        CableFlashFragment fragment = new CableFlashFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        PageNav.refreshPages(PageNav.PageMode.FWFLASH);
        PageNav.showPage(FwFlash.PAGE_ID);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        if (!CableFlash.isActive()) {
            PageNav.refreshPages(PageNav.PageMode.NORMAL);
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onBackPressed() {
        if (!CableFlash.isActive()) {
            super.onBackPressed();
        }
    }
}
