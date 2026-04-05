package android.support.v4.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/* JADX INFO: loaded from: classes63.dex */
public class Space extends View {
    public Space(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (getVisibility() == 0) {
            setVisibility(4);
        }
    }

    public Space(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Space(Context context) {
        this(context, null);
    }

    @Override // android.view.View
    @SuppressLint({"MissingSuperCall"})
    public void draw(Canvas canvas) {
    }

    private static int getDefaultSize2(int size, int measureSpec) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
                int result = Math.min(size, specSize);
                break;
            case 0:
                break;
            case 1073741824:
                break;
        }
        return size;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize2(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize2(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
}
