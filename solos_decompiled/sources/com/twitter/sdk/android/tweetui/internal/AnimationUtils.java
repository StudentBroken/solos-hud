package com.twitter.sdk.android.tweetui.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewPropertyAnimator;

/* JADX INFO: loaded from: classes9.dex */
public class AnimationUtils {
    @TargetApi(12)
    public static ViewPropertyAnimator fadeOut(final View from, int duration) {
        if (from.getVisibility() != 0) {
            return null;
        }
        from.clearAnimation();
        ViewPropertyAnimator animator = from.animate();
        animator.alpha(0.0f).setDuration(duration).setListener(new AnimatorListenerAdapter() { // from class: com.twitter.sdk.android.tweetui.internal.AnimationUtils.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                from.setVisibility(4);
                from.setAlpha(1.0f);
            }
        });
        return animator;
    }

    @TargetApi(12)
    public static ViewPropertyAnimator fadeIn(View to, int duration) {
        if (to.getVisibility() != 0) {
            to.setAlpha(0.0f);
            to.setVisibility(0);
        }
        to.clearAnimation();
        ViewPropertyAnimator animator = to.animate();
        animator.alpha(1.0f).setDuration(duration).setListener(null);
        return animator;
    }
}
