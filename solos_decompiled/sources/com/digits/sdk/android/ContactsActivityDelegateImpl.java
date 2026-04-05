package com.digits.sdk.android;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.digits.sdk.android.DigitsScribeConstants;

/* JADX INFO: loaded from: classes18.dex */
class ContactsActivityDelegateImpl implements ContactsActivityDelegate {
    final Activity activity;
    final ContactsController controller;
    private final DigitsScribeService scribeService;

    public ContactsActivityDelegateImpl(Activity activity) {
        this(activity, new ContactsControllerImpl(), new ContactsScribeService(Digits.getInstance().getScribeClient()));
    }

    public ContactsActivityDelegateImpl(Activity activity, ContactsController controller, DigitsScribeService scribeService) {
        this.activity = activity;
        this.controller = controller;
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.ContactsActivityDelegate
    public void init() {
        this.scribeService.impression();
        setContentView();
        setUpViews();
    }

    protected void setContentView() {
        this.activity.setContentView(R.layout.dgts__activity_contacts);
    }

    protected void setUpViews() {
        Button notNowButton = (Button) this.activity.findViewById(R.id.dgts__not_now);
        Button okayButton = (Button) this.activity.findViewById(R.id.dgts__okay);
        TextView description = (TextView) this.activity.findViewById(R.id.dgts__upload_contacts);
        setUpNotNowButton(notNowButton);
        setUpOkayButton(okayButton);
        setUpDescription(description);
    }

    protected void setUpDescription(TextView textView) {
        textView.setText(getFormattedDescription());
    }

    protected String getApplicationName() {
        return this.activity.getApplicationInfo().loadLabel(this.activity.getPackageManager()).toString();
    }

    protected String getFormattedDescription() {
        return this.activity.getString(R.string.dgts__upload_contacts, new Object[]{getApplicationName()});
    }

    protected void setUpNotNowButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.ContactsActivityDelegateImpl.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ContactsActivityDelegateImpl.this.scribeService.click(DigitsScribeConstants.Element.CANCEL);
                ContactsActivityDelegateImpl.this.activity.finish();
            }
        });
    }

    protected void setUpOkayButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.ContactsActivityDelegateImpl.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ContactsActivityDelegateImpl.this.scribeService.click(DigitsScribeConstants.Element.SUBMIT);
                ContactsActivityDelegateImpl.this.controller.startUploadService(ContactsActivityDelegateImpl.this.activity);
                ContactsActivityDelegateImpl.this.activity.finish();
            }
        });
    }
}
