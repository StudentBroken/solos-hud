package android.support.wearable.view;

import android.animation.Animator;
import android.annotation.TargetApi;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
public class SimpleAnimatorListener implements Animator.AnimatorListener {
    private boolean mWasCanceled;

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
        this.mWasCanceled = true;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        if (!this.mWasCanceled) {
            onAnimationComplete(animator);
        }
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        this.mWasCanceled = false;
    }

    public void onAnimationComplete(Animator animator) {
    }

    public boolean wasCanceled() {
        return this.mWasCanceled;
    }
}
