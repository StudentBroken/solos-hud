package com.digits.sdk.android;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/* JADX INFO: loaded from: classes18.dex */
abstract class DigitsActivityDelegateImpl implements DigitsActivityDelegate {
    DigitsActivityDelegateImpl() {
    }

    @Override // com.digits.sdk.android.ActivityLifecycle
    public void onDestroy() {
    }

    public void setUpSendButton(final Activity activity, final DigitsController controller, StateButton stateButton) {
        stateButton.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                controller.clearError();
                controller.executeRequest(activity);
            }
        });
    }

    public void setUpEditText(final Activity activity, final DigitsController controller, EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 5) {
                    return false;
                }
                controller.clearError();
                controller.executeRequest(activity);
                return true;
            }
        });
        editText.addTextChangedListener(controller.getTextWatcher());
    }

    public void setUpTermsText(final Activity activity, final DigitsController controller, TextView termsText) {
        termsText.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                controller.clearError();
                controller.showTOS(activity);
            }
        });
    }

    protected Spanned getFormattedTerms(Activity activity, @StringRes int termsResId) {
        return Html.fromHtml(activity.getString(termsResId, new Object[]{"\"", "<u>", "</u>"}));
    }

    @Override // com.digits.sdk.android.ActivityLifecycle
    public void onActivityResult(int requestCode, int resultCode, Activity activity) {
    }
}
