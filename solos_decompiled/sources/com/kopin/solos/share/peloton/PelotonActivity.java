package com.kopin.solos.share.peloton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.peloton.Failure;
import com.kopin.peloton.Peloton;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.peloton.UserData;
import com.kopin.peloton.UserInfo;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.R;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.settings.UserProfile;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ClassUtils;

/* JADX INFO: loaded from: classes4.dex */
public class PelotonActivity extends BaseActivity {
    private static final int ANIMATION_DURATION = 350;
    private static final int MAX_EMAIL_LENGTH = 128;
    private static final int MAX_PASSWORD_LENGTH = 128;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern[] PASSWORD_REGEX = {Pattern.compile(".*[A-Z].*"), Pattern.compile(".*[a-z].*"), Pattern.compile(".*\\d.*")};
    private Animation animationFadeIn;
    private Animation animationFadeOut;
    int areaAnim1Height;
    int areaAnim2Height;
    Button btnLogin;
    Button btnRegister;
    EditText edEmail;
    EditText edEmailReg;
    EditText edPassword;
    EditText edPasswordReg;
    View imgTickBtnLogin;
    View imgTickBtnRegister;
    ImageView imgValidEmail;
    ImageView imgValidEmailReg;
    ImageView imgValidPassword;
    ImageView imgValidPasswordReg;
    View layoutLoginItems;
    View layoutLoginTerms;
    View layoutRegisterItems;
    View layoutRegisterTerms;
    View layoutSectionLogin;
    View layoutSectionRegistration;
    View layoutWelcome;
    View mTextForgotPwd;
    View progressBar;
    View progressBarReg;
    private boolean showWelcome = true;
    private ButtonState state;
    TextView txtResponse;
    TextView txtResponseReg;

    private enum ButtonState {
        INTRO,
        LOGIN,
        REGISTER
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peloton);
        this.state = ButtonState.INTRO;
        findViewById(R.id.textDevServer).setVisibility(Config.CLOUD_LIVE ? 8 : 0);
        this.layoutSectionLogin = findViewById(R.id.layoutSectionLogin);
        this.layoutSectionRegistration = findViewById(R.id.layoutSectionRegistration);
        this.mTextForgotPwd = findViewById(R.id.textForgotPwd);
        this.layoutLoginItems = findViewById(R.id.layoutLoginItems);
        this.layoutLoginTerms = findViewById(R.id.layoutLoginTerms);
        this.layoutRegisterItems = findViewById(R.id.layoutRegisterItems);
        this.layoutRegisterTerms = findViewById(R.id.layoutRegisterTerms);
        this.layoutWelcome = findViewById(R.id.layoutWelcome);
        this.edEmail = (EditText) findViewById(R.id.edEmail);
        this.edPassword = (EditText) findViewById(R.id.edPassword);
        this.txtResponse = (TextView) findViewById(R.id.txtResponse);
        this.progressBar = findViewById(R.id.progressBar);
        this.imgValidEmail = (ImageView) findViewById(R.id.imgValidEmail);
        this.imgValidPassword = (ImageView) findViewById(R.id.imgValidPassword);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnLogin.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.share.peloton.PelotonActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PelotonActivity.this.loginAction();
            }
        });
        this.imgTickBtnLogin = findViewById(R.id.imgTickBtnLogin);
        this.edEmailReg = (EditText) findViewById(R.id.edEmailReg);
        this.edPasswordReg = (EditText) findViewById(R.id.edPasswordReg);
        this.txtResponseReg = (TextView) findViewById(R.id.txtResponseReg);
        this.progressBarReg = findViewById(R.id.progressBarReg);
        this.imgValidEmailReg = (ImageView) findViewById(R.id.imgValidEmailReg);
        this.imgValidPasswordReg = (ImageView) findViewById(R.id.imgValidPasswordReg);
        this.btnRegister = (Button) findViewById(R.id.btnRegister);
        this.btnRegister.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.share.peloton.PelotonActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PelotonActivity.this.registerAction();
            }
        });
        this.imgTickBtnRegister = findViewById(R.id.imgTickBtnRegister);
        this.edEmail.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.share.peloton.PelotonActivity.3
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PelotonActivity.this.imgValidEmail.setVisibility(s.length() == 0 ? 8 : 0);
                PelotonActivity.this.imgValidEmail.setImageResource(PelotonActivity.isEmailValid(s) ? R.drawable.ic_tick_login : R.drawable.ic_error_login);
                PelotonActivity.this.refreshSignIn();
            }
        });
        this.edEmailReg.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.share.peloton.PelotonActivity.4
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PelotonActivity.this.imgValidEmailReg.setVisibility(s.length() == 0 ? 8 : 0);
                PelotonActivity.this.imgValidEmailReg.setImageResource(PelotonActivity.isEmailValid(s) ? R.drawable.ic_tick_login : R.drawable.ic_error_login);
                PelotonActivity.this.refreshRegistration();
            }
        });
        findViewById(R.id.txtShowPassword).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.share.peloton.PelotonActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PelotonActivity.this.togglePasswordShowing(v, PelotonActivity.this.edPassword);
            }
        });
        this.edPassword.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.share.peloton.PelotonActivity.6
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PelotonActivity.this.imgValidPassword.setVisibility(s.length() == 0 ? 8 : 0);
                PelotonActivity.this.imgValidPassword.setImageResource(PelotonActivity.isPasswordValid(s.toString()) ? R.drawable.ic_tick_login : R.drawable.ic_error_login);
                PelotonActivity.this.refreshSignIn();
            }
        });
        findViewById(R.id.txtShowPasswordReg).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.share.peloton.PelotonActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PelotonActivity.this.togglePasswordShowing(v, PelotonActivity.this.edPasswordReg);
            }
        });
        this.edPasswordReg.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.share.peloton.PelotonActivity.8
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PelotonActivity.this.imgValidPasswordReg.setVisibility(s.length() == 0 ? 8 : 0);
                PelotonActivity.this.imgValidPasswordReg.setImageResource(PelotonActivity.isPasswordValid(s.toString()) ? R.drawable.ic_tick_login : R.drawable.ic_error_login);
                PelotonActivity.this.refreshRegistration();
            }
        });
        this.animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        this.animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSignIn() {
        this.btnLogin.setEnabled(isEmailValid(this.edEmail.getText()) && isPasswordValid(this.edPassword.getText()));
        this.btnRegister.setEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshRegistration() {
        this.btnRegister.setEnabled(isEmailValid(this.edEmailReg.getText()) && isPasswordValid(this.edPasswordReg.getText()));
        this.btnLogin.setEnabled(true);
    }

    private void clearAnimFields(boolean login) {
        if (login) {
            this.edPasswordReg.setText("");
            if (this.layoutRegisterItems.getVisibility() == 0) {
                this.layoutRegisterItems.startAnimation(this.animationFadeOut);
            }
            this.layoutRegisterItems.setVisibility(4);
            this.layoutRegisterTerms.setVisibility(4);
        } else {
            this.edPassword.setText("");
            if (this.layoutLoginItems.getVisibility() == 0) {
                this.layoutLoginItems.startAnimation(this.animationFadeOut);
            }
            this.layoutLoginItems.setVisibility(4);
            this.layoutLoginTerms.setVisibility(4);
            this.mTextForgotPwd.setVisibility(4);
        }
        this.txtResponse.setText("");
        this.txtResponseReg.setText("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginAction() {
        fadeWelcome();
        this.btnRegister.setEnabled(true);
        animate(this.btnLogin, this.areaAnim1Height / 2);
        if (this.state == ButtonState.LOGIN) {
            login();
            return;
        }
        new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.share.peloton.PelotonActivity.9
            @Override // java.lang.Runnable
            public void run() {
                if (PelotonActivity.this.layoutLoginItems.getVisibility() != 0) {
                    PelotonActivity.this.layoutLoginItems.setVisibility(0);
                    PelotonActivity.this.layoutLoginTerms.setVisibility(0);
                    PelotonActivity.this.mTextForgotPwd.setVisibility(0);
                    PelotonActivity.this.layoutLoginItems.startAnimation(PelotonActivity.this.animationFadeIn);
                }
                PelotonActivity.this.refreshSignIn();
            }
        }, 350L);
        this.btnLogin.setBackgroundResource(R.drawable.background_record_btn);
        clearAnimFields(true);
        animate(this.btnRegister, this.areaAnim2Height - this.btnRegister.getHeight());
        this.btnRegister.setBackgroundColor(getResources().getColor(R.color.solos_orange_dark));
        this.state = ButtonState.LOGIN;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerAction() {
        fadeWelcome();
        this.btnLogin.setEnabled(true);
        animate(this.btnRegister, 0);
        if (this.state == ButtonState.REGISTER) {
            register();
            return;
        }
        new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.share.peloton.PelotonActivity.10
            @Override // java.lang.Runnable
            public void run() {
                if (PelotonActivity.this.layoutRegisterItems.getVisibility() != 0) {
                    PelotonActivity.this.layoutRegisterItems.setVisibility(0);
                    PelotonActivity.this.layoutRegisterTerms.setVisibility(0);
                    PelotonActivity.this.layoutRegisterItems.startAnimation(PelotonActivity.this.animationFadeIn);
                }
                PelotonActivity.this.refreshRegistration();
            }
        }, 350L);
        this.btnRegister.setBackgroundResource(R.drawable.background_record_btn);
        clearAnimFields(false);
        animate(this.btnLogin, 0);
        this.btnLogin.setBackgroundColor(getResources().getColor(R.color.solos_orange_dark));
        this.state = ButtonState.REGISTER;
    }

    private void fadeWelcome() {
        if (this.showWelcome) {
            this.showWelcome = false;
            if (this.layoutWelcome.getVisibility() == 0) {
                this.layoutWelcome.startAnimation(this.animationFadeOut);
                this.layoutWelcome.setVisibility(4);
            }
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        this.areaAnim1Height = this.layoutSectionLogin.getMeasuredHeight();
        this.areaAnim2Height = this.layoutSectionRegistration.getMeasuredHeight();
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

    private void login() {
        loginRefresh(0);
        String email = this.edEmail.getText().toString();
        String pswd = this.edPassword.getText().toString();
        if (!isEmailValid(email)) {
            loginRefresh(R.string.valid_email_required);
            return;
        }
        if (!isPasswordValid(pswd)) {
            loginRefresh(R.string.valid_password_required);
            return;
        }
        this.btnLogin.setEnabled(false);
        this.btnLogin.setText("");
        this.progressBar.setVisibility(0);
        Peloton.signIn(email, pswd, new AnonymousClass11());
    }

    /* JADX INFO: renamed from: com.kopin.solos.share.peloton.PelotonActivity$11, reason: invalid class name */
    class AnonymousClass11 implements Peloton.LoginUserDataListener {
        AnonymousClass11() {
        }

        @Override // com.kopin.peloton.Peloton.LoginUserDataListener
        public void onLoggedIn(UserData userData) {
            PelotonActivity.this.btnLogin.setText("");
            PelotonActivity.this.imgTickBtnLogin.setVisibility(0);
            SavedRides.clearCache();
            SavedTrainingWorkouts.clearCache();
            SQLHelper.clear(new AnonymousClass1(userData));
        }

        /* JADX INFO: renamed from: com.kopin.solos.share.peloton.PelotonActivity$11$1, reason: invalid class name */
        class AnonymousClass1 implements SQLHelper.DatabaseWorkCallback {
            final /* synthetic */ UserData val$userData;

            AnonymousClass1(UserData userData) {
                this.val$userData = userData;
            }

            @Override // com.kopin.solos.storage.SQLHelper.DatabaseWorkCallback
            public void onComplete() {
                SQLHelper.setCurrentUsername(PelotonPrefs.getEmail());
                PelotonHelper.syncBikes(this.val$userData.Bikes, PelotonPrefs.getEmail(), new Sync.CompleteListener() { // from class: com.kopin.solos.share.peloton.PelotonActivity.11.1.1
                    @Override // com.kopin.solos.share.Sync.CompleteListener
                    public void onComplete() {
                        PelotonActivity.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.share.peloton.PelotonActivity.11.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                PelotonActivity.this.loginRefresh(0, false);
                                PelotonActivity.this.setResult(-1, new Intent());
                                PelotonActivity.this.finish();
                            }
                        });
                    }
                });
            }
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
            PelotonActivity.this.loginRefresh(errorMsgResId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginRefresh(int response) {
        loginRefresh(response, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginRefresh(int response, boolean showLabel) {
        this.txtResponse.setText("");
        if (response > 0) {
            this.txtResponse.setText(response);
        }
        if (showLabel) {
            this.btnLogin.setText(R.string.peloton_login);
            refreshSignIn();
        }
        this.progressBar.setVisibility(8);
    }

    private void register() {
        registerRefresh(0);
        String email = this.edEmailReg.getText().toString();
        String pswd = this.edPasswordReg.getText().toString();
        if (!isEmailValid(email)) {
            registerRefresh(R.string.valid_email_required);
            return;
        }
        if (!isPasswordValid(pswd)) {
            registerRefresh(R.string.valid_password_required);
            return;
        }
        this.btnRegister.setEnabled(false);
        this.btnRegister.setText("");
        this.progressBarReg.setVisibility(0);
        UserInfo userInfo = new UserInfo(email, email, pswd);
        userInfo.Name = getString(R.string.default_rider_name);
        userInfo.DOB = UserProfile.getDOBMillis();
        userInfo.Weight = UserProfile.getWeightKG();
        Peloton.register(userInfo, new AnonymousClass12());
    }

    /* JADX INFO: renamed from: com.kopin.solos.share.peloton.PelotonActivity$12, reason: invalid class name */
    class AnonymousClass12 implements Peloton.LoginListener {
        AnonymousClass12() {
        }

        @Override // com.kopin.peloton.Peloton.LoginListener
        public void onLoggedIn() {
            PelotonActivity.this.btnRegister.setText("");
            PelotonActivity.this.imgTickBtnRegister.setVisibility(0);
            SavedRides.clearCache();
            SavedTrainingWorkouts.clearCache();
            SQLHelper.clear(new SQLHelper.DatabaseWorkCallback() { // from class: com.kopin.solos.share.peloton.PelotonActivity.12.1
                @Override // com.kopin.solos.storage.SQLHelper.DatabaseWorkCallback
                public void onComplete() {
                    PelotonActivity.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.share.peloton.PelotonActivity.12.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            PelotonActivity.this.registerRefresh(0, false);
                            PelotonActivity.this.setResult(-1);
                            PelotonActivity.this.finish();
                        }
                    });
                }
            });
        }

        @Override // com.kopin.peloton.Peloton.CloudListener
        public void onFailure(Failure failure, int httpError, String s) {
            PelotonActivity.this.registerRefresh(httpError == 400 ? R.string.peloton_register_invalid_username : R.string.peloton_register_fail);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRefresh(int response) {
        registerRefresh(response, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRefresh(int response, boolean showLabel) {
        this.txtResponseReg.setText("");
        if (response > 0) {
            this.txtResponseReg.setText(response);
        }
        if (showLabel) {
            this.btnRegister.setText(R.string.peloton_register);
            refreshRegistration();
        }
        this.progressBarReg.setVisibility(8);
    }

    private static void animate(View view, int y) {
        if (view != null) {
            view.animate().setInterpolator(new AccelerateDecelerateInterpolator()).y(y).setDuration(350L);
        }
    }

    public static boolean isPasswordValid(CharSequence password) {
        return password != null && password.length() >= 8 && PASSWORD_REGEX[0].matcher(password).matches() && PASSWORD_REGEX[1].matcher(password).matches() && PASSWORD_REGEX[2].matcher(password).matches();
    }

    public static boolean isEmailValid(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static String emailToName(String email) {
        String name = email.replace('_', ' ').replace(ClassUtils.PACKAGE_SEPARATOR_CHAR, ' ').trim();
        if (name.length() >= 2) {
            int posAt = name.indexOf("@");
            if (posAt > 0) {
                name = name.substring(0, posAt);
            }
            return name.substring(0, 1).toUpperCase(Locale.US) + name.substring(1);
        }
        return name;
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
