package com.kopin.solos.share.peloton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.peloton.Failure;
import com.kopin.peloton.Peloton;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.peloton.UserData;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.R;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.view.RoundedImageView;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;

/* JADX INFO: loaded from: classes4.dex */
public class PelotonReauthenticate extends BaseActivity {
    Button btnLogin;
    EditText edPassword;
    private RoundedImageView imgProfileUser;
    private ImageView imgProfileUserDefault;
    ImageView imgValidPassword;
    protected Bitmap mBmpProfile;
    View progressBar;
    private TextView txtEmailName;
    private TextView txtProfileName;
    TextView txtResponse;

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peloton_reauthenticate);
        this.edPassword = (EditText) findViewById(R.id.edPassword);
        this.txtResponse = (TextView) findViewById(R.id.txtResponse);
        this.progressBar = findViewById(R.id.progressBar);
        this.imgValidPassword = (ImageView) findViewById(R.id.imgValidPassword);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnLogin.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.share.peloton.PelotonReauthenticate.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PelotonReauthenticate.this.login();
            }
        });
        findViewById(R.id.txtShowPassword).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.share.peloton.PelotonReauthenticate.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PelotonReauthenticate.this.togglePasswordShowing(v, PelotonReauthenticate.this.edPassword);
            }
        });
        this.edPassword.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.share.peloton.PelotonReauthenticate.3
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PelotonReauthenticate.this.imgValidPassword.setVisibility(s.length() == 0 ? 8 : 0);
                PelotonReauthenticate.this.imgValidPassword.setImageResource(PelotonActivity.isPasswordValid(s.toString()) ? R.drawable.ic_tick_login : R.drawable.ic_error_login);
            }
        });
        findViewById(R.id.txtNewAccount).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.share.peloton.PelotonReauthenticate.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PelotonReauthenticate.this.startActivity(new Intent(PelotonReauthenticate.this, (Class<?>) PelotonActivity.class));
                PelotonReauthenticate.this.finish();
            }
        });
        this.imgProfileUser = (RoundedImageView) findViewById(R.id.imgProfileUser);
        this.imgProfileUserDefault = (ImageView) findViewById(R.id.imgProfileUserDefault);
        this.txtProfileName = (TextView) findViewById(R.id.txtProfileName);
        this.txtEmailName = (TextView) findViewById(R.id.txtEmailName);
        int mAge = UserProfile.getAge();
        String mName = UserProfile.getName();
        if (mName != null && !mName.isEmpty()) {
            this.txtProfileName.setText("");
            this.txtProfileName.setText(mName + ", " + mAge);
        }
        this.txtEmailName.setVisibility(8);
        String email = PelotonPrefs.getEmail();
        if (Config.SYNC_PROVIDER != Platforms.None && email != null && !email.isEmpty()) {
            this.txtEmailName.setText(email);
            this.txtEmailName.setVisibility(0);
        }
        if (this.mBmpProfile == null) {
            this.imgProfileUser.setVisibility(8);
            this.imgProfileUserDefault.setVisibility(0);
        } else {
            this.imgProfileUserDefault.setVisibility(8);
            this.imgProfileUser.setImageBitmap(this.mBmpProfile);
            this.imgProfileUser.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void togglePasswordShowing(View view, EditText editText) {
        TextView textView = (TextView) view;
        int pos = editText.getSelectionStart();
        int len = editText.getText().length();
        if (editText.getInputType() != 144) {
            editText.setInputType(TwitterApiConstants.Errors.ALREADY_UNFAVORITED);
            textView.setText(R.string.hide_password);
        } else {
            editText.setInputType(129);
            textView.setText(R.string.show_password);
        }
        editText.setSelection(Math.min(pos, len));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void login() {
        loginRefresh(0);
        String email = PelotonPrefs.getEmail();
        String pswd = this.edPassword.getText().toString();
        if (!PelotonActivity.isEmailValid(email)) {
            loginRefresh(R.string.valid_email_required);
            return;
        }
        if (!PelotonActivity.isPasswordValid(pswd)) {
            loginRefresh(R.string.valid_password_required);
            return;
        }
        this.btnLogin.setEnabled(false);
        this.btnLogin.setText("");
        this.progressBar.setVisibility(0);
        Peloton.signIn(email, pswd, new Peloton.LoginUserDataListener() { // from class: com.kopin.solos.share.peloton.PelotonReauthenticate.5
            @Override // com.kopin.peloton.Peloton.LoginUserDataListener
            public void onLoggedIn(UserData userData) {
                PelotonReauthenticate.this.loginRefresh(0);
                PelotonReauthenticate.this.setResult(-1, new Intent());
                PelotonReauthenticate.this.finish();
            }

            @Override // com.kopin.peloton.Peloton.CloudListener
            public void onFailure(Failure failure, int httpError, String s) {
                int errorMsgResId = R.string.peloton_signin_fail;
                if (httpError == 400) {
                    if (s != null && s.toLowerCase().contains("account") && s.toLowerCase().contains("disabled")) {
                        errorMsgResId = R.string.peloton_account_disabled;
                    } else {
                        errorMsgResId = R.string.peloton_signin_incorrect_username_password;
                    }
                }
                PelotonReauthenticate.this.loginRefresh(errorMsgResId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginRefresh(int resourceId) {
        if (resourceId != 0) {
            this.txtResponse.setText(resourceId);
        } else {
            this.txtResponse.setText((CharSequence) null);
        }
        this.progressBar.setVisibility(8);
        this.btnLogin.setText(R.string.peloton_login);
        this.btnLogin.setEnabled(true);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onBackPressed() {
        setResult(0);
        finish();
    }

    public void showForgotPasswordPage(View v) {
        Intent intent = new Intent(this, (Class<?>) TermsActivity.class).putExtra(TermsActivity.LOAD_URL, Peloton.getPasswordRecoveryUrl()).putExtra(TermsActivity.PAGE_TITLE_RESOURCE, R.string.forgot_password);
        startActivity(intent);
    }

    public void showTermsPage(View v) {
        Intent intent = new Intent(this, (Class<?>) TermsActivity.class).putExtra(TermsActivity.LOAD_URL, TermsActivity.URL_TERMS).putExtra(TermsActivity.PAGE_TITLE_RESOURCE, R.string.title_terms);
        startActivity(intent);
    }

    public void showPrivacyPage(View v) {
        Intent intent = new Intent(this, (Class<?>) TermsActivity.class).putExtra(TermsActivity.LOAD_URL, TermsActivity.URL_PRIVACY_POLICY).putExtra(TermsActivity.PAGE_TITLE_RESOURCE, R.string.title_privacy);
        startActivity(intent);
    }
}
