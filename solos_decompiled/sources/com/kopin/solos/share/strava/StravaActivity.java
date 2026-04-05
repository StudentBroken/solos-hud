package com.kopin.solos.share.strava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.share.R;
import com.kopin.solos.share.strava.StravaHelper;
import org.jstrava.entities.athlete.Athlete;

/* JADX INFO: loaded from: classes4.dex */
public class StravaActivity extends BaseActivity {
    private static final int SIGN_IN_CALLBACK = 20;
    private TextView mTextView;

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strava);
        this.mTextView = (TextView) findViewById(R.id.strava_text_view);
        StravaHelper.refreshAuthResponse(this, new StravaHelper.StravaListener() { // from class: com.kopin.solos.share.strava.StravaActivity.1
            @Override // com.kopin.solos.share.strava.StravaHelper.StravaListener
            public void onResult(boolean success, final String message) {
                if (success) {
                    StravaActivity.this.updateStatus();
                } else {
                    StravaActivity.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.share.strava.StravaActivity.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            StravaActivity.this.mTextView.setText("Failed: " + message);
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatus() {
        runOnUiThread(new Runnable() { // from class: com.kopin.solos.share.strava.StravaActivity.2
            @Override // java.lang.Runnable
            public void run() {
                Athlete athlete = StravaHelper.getAthlete();
                if (athlete == null) {
                    StravaActivity.this.mTextView.setText("not logged in");
                } else {
                    StravaActivity.this.mTextView.setText("First name: " + athlete.getFirstname());
                }
            }
        });
    }

    public void login(View view) {
        StravaHelper.login(this, 20);
    }

    public void logout(View view) {
        StravaHelper.logOut(this);
        updateStatus();
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            switch (resultCode) {
                case -1:
                    StravaHelper.onActivityResult(this, data, new StravaHelper.StravaListener() { // from class: com.kopin.solos.share.strava.StravaActivity.3
                        @Override // com.kopin.solos.share.strava.StravaHelper.StravaListener
                        public void onResult(boolean success, final String message) {
                            if (success) {
                                StravaActivity.this.updateStatus();
                            } else {
                                StravaActivity.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.share.strava.StravaActivity.3.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        StravaActivity.this.mTextView.setText("Failed: " + message);
                                    }
                                });
                            }
                        }
                    });
                    break;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
