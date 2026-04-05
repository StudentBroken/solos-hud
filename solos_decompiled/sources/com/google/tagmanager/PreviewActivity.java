package com.google.tagmanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/* JADX INFO: loaded from: classes49.dex */
public class PreviewActivity extends Activity {
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Log.i("Preview activity");
            Uri data = getIntent().getData();
            if (!TagManager.getInstance(this).setPreviewData(data)) {
                Log.w("Cannot preview the app with the uri: " + data);
            } else {
                Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                if (intent != null) {
                    Log.i("Invoke the launch activity for package name: " + getPackageName());
                    startActivity(intent);
                } else {
                    Log.i("No launch activity found for package name: " + getPackageName());
                }
            }
        } catch (Exception e) {
            Log.e("Calling preview threw an exception: " + e.getMessage());
        }
    }
}
