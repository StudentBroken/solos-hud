package android.support.wearable.internal.view;

import android.R;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.wearable.view.SwipeDismissFrameLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(11)
public class SwipeDismissPreferenceFragment extends PreferenceFragment {
    private final SwipeDismissFrameLayout.Callback mCallback = new SwipeDismissFrameLayout.Callback() { // from class: android.support.wearable.internal.view.SwipeDismissPreferenceFragment.1
        @Override // android.support.wearable.view.SwipeDismissFrameLayout.Callback
        public void onSwipeStart() {
            SwipeDismissPreferenceFragment.this.onSwipeStart();
        }

        @Override // android.support.wearable.view.SwipeDismissFrameLayout.Callback
        public void onSwipeCancelled() {
            SwipeDismissPreferenceFragment.this.onSwipeCancelled();
        }

        @Override // android.support.wearable.view.SwipeDismissFrameLayout.Callback
        public void onDismissed(SwipeDismissFrameLayout layout) {
            SwipeDismissPreferenceFragment.this.onDismiss();
        }
    };
    private SwipeDismissFrameLayout mSwipeLayout;

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mSwipeLayout = new SwipeDismissFrameLayout(getActivity());
        this.mSwipeLayout.addCallback(this.mCallback);
        View contents = super.onCreateView(inflater, this.mSwipeLayout, savedInstanceState);
        this.mSwipeLayout.setBackgroundColor(getBackgroundColor());
        this.mSwipeLayout.addView(contents);
        return this.mSwipeLayout;
    }

    public void onDismiss() {
    }

    public void onSwipeStart() {
    }

    public void onSwipeCancelled() {
    }

    public void setFocusable(boolean focusable) {
        if (focusable) {
            this.mSwipeLayout.setDescendantFocusability(131072);
            this.mSwipeLayout.setFocusable(true);
        } else {
            this.mSwipeLayout.setDescendantFocusability(393216);
            this.mSwipeLayout.setFocusable(false);
            this.mSwipeLayout.clearFocus();
        }
    }

    private int getBackgroundColor() {
        TypedValue value = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorBackground, value, true);
        return value.data;
    }
}
