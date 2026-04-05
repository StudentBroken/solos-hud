package android.support.wearable.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.StyleRes;
import android.support.wearable.R;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public class AcceptDenyDialog extends Dialog {
    private final View.OnClickListener mButtonHandler;
    protected View mButtonPanel;
    protected ImageView mIcon;
    protected TextView mMessage;
    protected ImageButton mNegativeButton;
    protected DialogInterface.OnClickListener mNegativeButtonListener;
    protected ImageButton mPositiveButton;
    protected DialogInterface.OnClickListener mPositiveButtonListener;
    protected View mSpacer;
    protected TextView mTitle;

    public AcceptDenyDialog(Context context) {
        this(context, 0);
    }

    public AcceptDenyDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mButtonHandler = new View.OnClickListener() { // from class: android.support.wearable.view.AcceptDenyDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (v == AcceptDenyDialog.this.mPositiveButton && AcceptDenyDialog.this.mPositiveButtonListener != null) {
                    AcceptDenyDialog.this.mPositiveButtonListener.onClick(AcceptDenyDialog.this, -1);
                } else if (v == AcceptDenyDialog.this.mNegativeButton && AcceptDenyDialog.this.mNegativeButtonListener != null) {
                    AcceptDenyDialog.this.mNegativeButtonListener.onClick(AcceptDenyDialog.this, -2);
                }
                AcceptDenyDialog.this.dismiss();
            }
        };
        setContentView(R.layout.accept_deny_dialog);
        this.mTitle = (TextView) findViewById(android.R.id.title);
        this.mMessage = (TextView) findViewById(android.R.id.message);
        this.mIcon = (ImageView) findViewById(android.R.id.icon);
        this.mPositiveButton = (ImageButton) findViewById(android.R.id.button1);
        this.mPositiveButton.setOnClickListener(this.mButtonHandler);
        this.mNegativeButton = (ImageButton) findViewById(android.R.id.button2);
        this.mNegativeButton.setOnClickListener(this.mButtonHandler);
        this.mSpacer = findViewById(R.id.spacer);
        this.mButtonPanel = findViewById(R.id.buttonPanel);
    }

    public ImageButton getButton(int whichButton) {
        switch (whichButton) {
            case -2:
                return this.mNegativeButton;
            case -1:
                return this.mPositiveButton;
            default:
                return null;
        }
    }

    public void setIcon(Drawable icon) {
        this.mIcon.setVisibility(icon == null ? 8 : 0);
        this.mIcon.setImageDrawable(icon);
    }

    public void setIcon(int resId) {
        this.mIcon.setVisibility(resId == 0 ? 8 : 0);
        this.mIcon.setImageResource(resId);
    }

    public void setMessage(CharSequence message) {
        this.mMessage.setText(message);
        this.mMessage.setVisibility(message == null ? 8 : 0);
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence title) {
        this.mTitle.setText(title);
    }

    public void setButton(int whichButton, DialogInterface.OnClickListener listener) {
        switch (whichButton) {
            case -2:
                this.mNegativeButtonListener = listener;
                break;
            case -1:
                this.mPositiveButtonListener = listener;
                break;
            default:
                return;
        }
        this.mSpacer.setVisibility((this.mPositiveButtonListener == null || this.mNegativeButtonListener == null) ? 8 : 4);
        this.mPositiveButton.setVisibility(this.mPositiveButtonListener == null ? 8 : 0);
        this.mNegativeButton.setVisibility(this.mNegativeButtonListener == null ? 8 : 0);
        this.mButtonPanel.setVisibility((this.mPositiveButtonListener == null && this.mNegativeButtonListener == null) ? 8 : 0);
    }

    public void setPositiveButton(DialogInterface.OnClickListener listener) {
        setButton(-1, listener);
    }

    public void setNegativeButton(DialogInterface.OnClickListener listener) {
        setButton(-2, listener);
    }
}
