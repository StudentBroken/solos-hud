package com.kopin.solos.views;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

/* JADX INFO: loaded from: classes24.dex */
public class LongTextDialog extends Dialog {
    TextView textView;

    public LongTextDialog(@NonNull Context context) {
        super(context, R.style.Theme.DeviceDefault.Dialog.NoActionBar);
        setContentView(com.kopin.solos.R.layout.dialog_long_text);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        this.textView = (TextView) findViewById(com.kopin.solos.R.id.textLong);
        findViewById(com.kopin.solos.R.id.btnCancel).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.views.LongTextDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LongTextDialog.this.cancel();
            }
        });
    }

    public void show(String text) {
        this.textView.setText(text);
        show();
    }
}
