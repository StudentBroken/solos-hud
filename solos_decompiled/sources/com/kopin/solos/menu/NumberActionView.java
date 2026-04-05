package com.kopin.solos.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.menu.CustomActionProvider;

/* JADX INFO: loaded from: classes24.dex */
public class NumberActionView implements CustomActionProvider.ActionView {
    private int mActiveColor;
    private ImageView mImageView;
    private int mInactiveColor;
    private TextView mTextView;
    private View mView;

    public NumberActionView(Context context) {
        int color = context.getResources().getColor(R.color.unfocused_grey);
        this.mInactiveColor = color;
        this.mActiveColor = color;
        this.mView = LayoutInflater.from(context).inflate(R.layout.number_action_view, (ViewGroup) null);
        this.mImageView = (ImageView) this.mView.findViewById(R.id.image);
        this.mTextView = (TextView) this.mView.findViewById(R.id.text);
    }

    @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
    public View getView() {
        return this.mView;
    }

    @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
    public void setImageDrawable(Drawable drawable) {
        this.mImageView.setImageDrawable(drawable);
    }

    public void setNumber(int number) {
        this.mTextView.setText(String.valueOf(number));
    }

    public void setTextColor(int color) {
        this.mTextView.setTextColor(color);
    }

    @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
    public void setActiveColor(int color) {
        this.mActiveColor = color;
    }

    @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
    public void setInactiveColor(int color) {
        this.mInactiveColor = color;
    }

    @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
    public void setActive(boolean active) {
        if (active) {
            this.mImageView.setColorFilter(this.mActiveColor);
        } else {
            this.mImageView.setColorFilter(this.mInactiveColor);
        }
    }
}
