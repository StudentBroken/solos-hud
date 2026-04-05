package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableWrapperApi14;

/* JADX INFO: loaded from: classes27.dex */
@RequiresApi(19)
class DrawableWrapperApi19 extends DrawableWrapperApi14 {
    DrawableWrapperApi19(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperApi19(DrawableWrapperApi14.DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean mirrored) {
        this.mDrawable.setAutoMirrored(mirrored);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }

    @Override // android.support.v4.graphics.drawable.DrawableWrapperApi14
    @NonNull
    DrawableWrapperApi14.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateKitKat(this.mState, null);
    }

    private static class DrawableWrapperStateKitKat extends DrawableWrapperApi14.DrawableWrapperState {
        DrawableWrapperStateKitKat(@Nullable DrawableWrapperApi14.DrawableWrapperState orig, @Nullable Resources res) {
            super(orig, res);
        }

        @Override // android.support.v4.graphics.drawable.DrawableWrapperApi14.DrawableWrapperState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(@Nullable Resources res) {
            return new DrawableWrapperApi19(this, res);
        }
    }
}
