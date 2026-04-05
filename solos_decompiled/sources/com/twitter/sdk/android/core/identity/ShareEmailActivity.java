package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.TextView;
import com.twitter.sdk.android.core.R;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes62.dex */
public class ShareEmailActivity extends Activity {
    static final String EXTRA_RESULT_RECEIVER = "result_receiver";
    static final String EXTRA_SESSION_ID = "session_id";
    ShareEmailController controller;
    private TwitterSession session;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tw__activity_share_email);
        try {
            Intent startIntent = getIntent();
            ResultReceiver resultReceiver = getResultReceiver(startIntent);
            this.session = getSession(startIntent);
            this.controller = new ShareEmailController(new ShareEmailClient(this.session), resultReceiver);
            TextView shareEmailDescView = (TextView) findViewById(R.id.tw__share_email_desc);
            setUpShareEmailDesc(this, shareEmailDescView);
        } catch (IllegalArgumentException e) {
            Fabric.getLogger().e(TwitterCore.TAG, "Failed to create ShareEmailActivity.", e);
            finish();
        }
    }

    private ResultReceiver getResultReceiver(Intent intent) {
        ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        if (resultReceiver == null) {
            throw new IllegalArgumentException("ResultReceiver must not be null. This activity should not be started directly.");
        }
        return resultReceiver;
    }

    private TwitterSession getSession(Intent intent) {
        long sessionId = intent.getLongExtra(EXTRA_SESSION_ID, -1L);
        TwitterSession session = (TwitterSession) TwitterCore.getInstance().getSessionManager().getSession(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("No TwitterSession for id:" + sessionId);
        }
        return session;
    }

    void setUpShareEmailDesc(Context context, TextView shareEmailDescView) {
        PackageManager packageManager = context.getPackageManager();
        shareEmailDescView.setText(getResources().getString(R.string.tw__share_email_desc, packageManager.getApplicationLabel(context.getApplicationInfo()), this.session.getUserName()));
    }

    public void onClickNotNow(View view) {
        this.controller.cancelRequest();
        finish();
    }

    public void onClickAllow(View view) {
        this.controller.executeRequest();
        finish();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        this.controller.cancelRequest();
        super.onBackPressed();
    }
}
