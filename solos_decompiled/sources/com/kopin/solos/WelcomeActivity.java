package com.kopin.solos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.solos.common.BaseActivity;

/* JADX INFO: loaded from: classes24.dex */
public class WelcomeActivity extends BaseActivity {
    public static final String WELCOME_NEW_USER = "welcome_new_user";
    boolean newUser = true;

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            this.newUser = getIntent().getBooleanExtra(WELCOME_NEW_USER, true);
        }
        setContentView(R.layout.activity_welcome);
        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setText(this.newUser ? "GET STARTED" : "CONTINUE");
        btnNext.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.WelcomeActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, (Class<?>) SetupActivity.class);
                if (!WelcomeActivity.this.newUser) {
                    intent.putExtra(SetupActivity.SETUP_INTENT_EXTRA_KEY, 4);
                }
                intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });
    }
}
