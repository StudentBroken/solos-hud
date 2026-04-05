package com.kopin.pupil.ui.elements;

import android.graphics.Point;
import android.graphics.Rect;
import com.kopin.pupil.pagerenderer.Theme;

/* JADX INFO: loaded from: classes25.dex */
public interface RelativeElement {

    public enum RelativePosition {
        NONE,
        RIGHT_OF
    }

    void configure(Theme theme, int i, int i2);

    String getElementName();

    Rect getElementRect(Theme theme, int i, int i2);

    Point getOffset();

    boolean hasRelativePosition();

    void removeRelation(RelativePosition relativePosition);

    void setElement(RelativeElement relativeElement);

    void setRelation(String str, RelativePosition relativePosition);
}
