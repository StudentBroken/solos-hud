package android.support.wearable.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.wearable.R;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class DismissOverlayView extends FrameLayout {
    private static final String KEY_FIRST_RUN = "first_run";
    private static final String PREF_NAME = "android.support.wearable.DismissOverlay";
    private final View mDismissButton;
    private boolean mFirstRun;
    private final TextView mFirstRunText;
    private SharedPreferences mPrefs;

    public DismissOverlayView(Context context) {
        this(context, null, 0);
    }

    public DismissOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DismissOverlayView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mFirstRun = true;
        LayoutInflater.from(context).inflate(R.layout.dismiss_overlay, (ViewGroup) this, true);
        setBackgroundResource(R.color.dismiss_overlay_bg);
        setClickable(true);
        if (!isInEditMode()) {
            this.mPrefs = context.getSharedPreferences(PREF_NAME, 0);
            this.mFirstRun = this.mPrefs.getBoolean(KEY_FIRST_RUN, true);
        }
        this.mFirstRunText = (TextView) findViewById(R.id.dismiss_overlay_explain);
        this.mDismissButton = findViewById(R.id.dismiss_overlay_button);
        this.mDismissButton.setOnClickListener(new View.OnClickListener(this) { // from class: android.support.wearable.view.DismissOverlayView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
        setVisibility(8);
    }

    public void setIntroText(CharSequence str) {
        this.mFirstRunText.setText(str);
    }

    public void setIntroText(int textResId) {
        this.mFirstRunText.setText(textResId);
    }

    public void showIntroIfNecessary() {
        if (this.mFirstRun) {
            if (TextUtils.isEmpty(this.mFirstRunText.getText())) {
                this.mFirstRun = false;
                return;
            }
            this.mFirstRunText.setVisibility(0);
            this.mDismissButton.setVisibility(8);
            setVisibility(0);
            postDelayed(new Runnable() { // from class: android.support.wearable.view.DismissOverlayView.2
                @Override // java.lang.Runnable
                public void run() {
                    DismissOverlayView.this.hide();
                }
            }, 3000L);
        }
    }

    static void resetPrefs(Context context) {
        context.getSharedPreferences(PREF_NAME, 0).edit().clear().apply();
    }

    public void show() {
        setAlpha(0.0f);
        this.mFirstRunText.setVisibility(8);
        this.mDismissButton.setVisibility(0);
        setVisibility(0);
        animate().alpha(1.0f).setDuration(200L).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hide() {
        animate().alpha(0.0f).setDuration(200L).withEndAction(new Runnable() { // from class: android.support.wearable.view.DismissOverlayView.3
            @Override // java.lang.Runnable
            public void run() {
                DismissOverlayView.this.setVisibility(8);
                DismissOverlayView.this.setAlpha(1.0f);
            }
        }).start();
        if (this.mFirstRun) {
            this.mFirstRun = false;
            this.mPrefs.edit().putBoolean(KEY_FIRST_RUN, false).apply();
        }
    }

    @Override // android.view.View
    public boolean performClick() {
        super.performClick();
        hide();
        return true;
    }
}
