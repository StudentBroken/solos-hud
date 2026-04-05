package android.support.wearable.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public class AcceptDenyDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String EXTRA_DIALOG_BUILDER = "extra_dialog_builder";

    public interface OnCancelListener {
        void onCancel(@NonNull AcceptDenyDialogFragment acceptDenyDialogFragment);
    }

    public interface OnClickListener {
        void onClick(@NonNull AcceptDenyDialogFragment acceptDenyDialogFragment, int i);
    }

    public interface OnDismissListener {
        void onDismiss(@NonNull AcceptDenyDialogFragment acceptDenyDialogFragment);
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AcceptDenyDialog dialog = new AcceptDenyDialog(getActivity());
        Builder builder = (Builder) getArguments().getParcelable(EXTRA_DIALOG_BUILDER);
        if (builder != null) {
            builder.createDialog(dialog, this);
        }
        onPrepareDialog(dialog);
        return dialog;
    }

    protected void onPrepareDialog(@NonNull AcceptDenyDialog dialog) {
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        if (getActivity() instanceof OnClickListener) {
            ((OnClickListener) getActivity()).onClick(this, which);
        }
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (getActivity() instanceof OnCancelListener) {
            ((OnCancelListener) getActivity()).onCancel(this);
        }
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() instanceof OnDismissListener) {
            ((OnDismissListener) getActivity()).onDismiss(this);
        }
    }

    public static class Builder implements Parcelable {
        public static final Parcelable.Creator<Builder> CREATOR = new Parcelable.Creator<Builder>() { // from class: android.support.wearable.view.AcceptDenyDialogFragment.Builder.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Builder createFromParcel(Parcel in) {
                return new Builder(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };
        private int mIconRes;
        private String mMessage;
        private boolean mShowNegativeButton;
        private boolean mShowPositiveButton;
        private String mTitle;

        public Builder() {
        }

        @NonNull
        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        @NonNull
        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        @NonNull
        public Builder setIconRes(@DrawableRes int iconRes) {
            this.mIconRes = iconRes;
            return this;
        }

        @NonNull
        public Builder setShowPositiveButton(boolean show) {
            this.mShowPositiveButton = show;
            return this;
        }

        @NonNull
        public Builder setShowNegativeButton(boolean show) {
            this.mShowNegativeButton = show;
            return this;
        }

        public <T extends AcceptDenyDialogFragment> T apply(T f) {
            Bundle args = f.getArguments();
            if (args == null) {
                args = new Bundle();
                f.setArguments(args);
            }
            args.putParcelable(AcceptDenyDialogFragment.EXTRA_DIALOG_BUILDER, this);
            return f;
        }

        @NonNull
        public AcceptDenyDialogFragment build() {
            return apply(new AcceptDenyDialogFragment());
        }

        protected void createDialog(@NonNull AcceptDenyDialog dialog, DialogInterface.OnClickListener buttonListener) {
            dialog.setTitle(this.mTitle);
            dialog.setMessage(this.mMessage);
            if (this.mIconRes != 0) {
                dialog.setIcon(dialog.getContext().getDrawable(this.mIconRes));
            }
            if (this.mShowPositiveButton) {
                if (buttonListener == null) {
                    throw new IllegalArgumentException("buttonListener must not be null when used with buttons");
                }
                dialog.setPositiveButton(buttonListener);
            }
            if (this.mShowNegativeButton) {
                if (buttonListener == null) {
                    throw new IllegalArgumentException("buttonListener must not be null when used with buttons");
                }
                dialog.setNegativeButton(buttonListener);
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.mTitle);
            out.writeString(this.mMessage);
            out.writeInt(this.mIconRes);
            out.writeValue(Boolean.valueOf(this.mShowPositiveButton));
            out.writeValue(Boolean.valueOf(this.mShowNegativeButton));
        }

        private Builder(Parcel in) {
            this.mTitle = in.readString();
            this.mMessage = in.readString();
            this.mIconRes = in.readInt();
            this.mShowPositiveButton = ((Boolean) in.readValue(null)).booleanValue();
            this.mShowNegativeButton = ((Boolean) in.readValue(null)).booleanValue();
        }
    }
}
