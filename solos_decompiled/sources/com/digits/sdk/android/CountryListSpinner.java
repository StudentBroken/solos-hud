package com.digits.sdk.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.digits.sdk.android.CountryListLoadTask;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes18.dex */
public class CountryListSpinner extends TextView implements View.OnClickListener, CountryListLoadTask.Listener {
    private CountryListAdapter countryListAdapter;
    private DialogPopup dialogPopup;
    private View.OnClickListener listener;
    private String selectedCountryName;
    private String textFormat;

    public CountryListSpinner(Context context) {
        this(context, null, android.R.attr.spinnerStyle);
    }

    public CountryListSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.spinnerStyle);
    }

    public CountryListSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void setDialogPopup(DialogPopup dialog) {
        this.dialogPopup = dialog;
    }

    private void init() {
        super.setOnClickListener(this);
        this.countryListAdapter = new CountryListAdapter(getContext());
        this.dialogPopup = new DialogPopup(this.countryListAdapter);
        this.textFormat = getResources().getString(R.string.dgts__country_spinner_format);
        this.selectedCountryName = "";
        String defaultCountry = Locale.US.getDisplayCountry();
        setSpinnerText(1, defaultCountry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpinnerText(int countryCode, String country) {
        setText(String.format(this.textFormat, country, Integer.valueOf(countryCode)));
        setTag(Integer.valueOf(countryCode));
    }

    public void setSelectedForCountry(String countryName, String countryCode) {
        if (!TextUtils.isEmpty(countryName) && !TextUtils.isEmpty(countryCode)) {
            this.selectedCountryName = countryName;
            setSpinnerText(Integer.valueOf(countryCode).intValue(), countryName);
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.dialogPopup.isShowing()) {
            this.dialogPopup.dismiss();
        }
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener l) {
        this.listener = l;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.countryListAdapter.getCount() == 0) {
            loadCountryList();
        } else {
            this.dialogPopup.show(this.countryListAdapter.getPositionForCountry(this.selectedCountryName));
        }
        CommonUtils.hideKeyboard(getContext(), this);
        executeUserClickListener(view);
    }

    private void loadCountryList() {
        new CountryListLoadTask(this).executeOnExecutor(Digits.getInstance().getExecutorService(), new Void[0]);
    }

    private void executeUserClickListener(View view) {
        if (this.listener != null) {
            this.listener.onClick(view);
        }
    }

    @Override // com.digits.sdk.android.CountryListLoadTask.Listener
    public void onLoadComplete(List<CountryInfo> result) {
        this.countryListAdapter.setData(result);
        this.dialogPopup.show(this.countryListAdapter.getPositionForCountry(this.selectedCountryName));
    }

    public class DialogPopup implements DialogInterface.OnClickListener {
        private static final long DELAY_MILLIS = 10;
        private AlertDialog dialog;
        private final CountryListAdapter listAdapter;

        DialogPopup(CountryListAdapter adapter) {
            this.listAdapter = adapter;
        }

        public void dismiss() {
            if (this.dialog != null) {
                this.dialog.dismiss();
                this.dialog = null;
            }
        }

        public boolean isShowing() {
            return this.dialog != null && this.dialog.isShowing();
        }

        public void show(final int selected) {
            if (this.listAdapter != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CountryListSpinner.this.getContext());
                this.dialog = builder.setSingleChoiceItems(this.listAdapter, 0, this).create();
                this.dialog.setCanceledOnTouchOutside(true);
                final ListView listView = this.dialog.getListView();
                listView.setFastScrollEnabled(true);
                listView.postDelayed(new Runnable() { // from class: com.digits.sdk.android.CountryListSpinner.DialogPopup.1
                    @Override // java.lang.Runnable
                    public void run() {
                        listView.setSelection(selected);
                    }
                }, DELAY_MILLIS);
                this.dialog.show();
            }
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            CountryInfo countryInfo = this.listAdapter.getItem(which);
            CountryListSpinner.this.selectedCountryName = countryInfo.country;
            CountryListSpinner.this.setSpinnerText(countryInfo.countryCode, countryInfo.country);
            dismiss();
        }
    }
}
