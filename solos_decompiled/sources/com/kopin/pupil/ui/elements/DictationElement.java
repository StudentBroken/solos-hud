package com.kopin.pupil.ui.elements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.util.MathHelper;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes25.dex */
public class DictationElement extends BaseElement {
    private static final float INITIAL_GAIN = 0.2f;
    public static final int INNER_RADIUS = 28;
    public static final int OUTER_DIAMETER = 64;
    public static final int OUTER_RADIUS = 32;
    private ArrayList<Float> mGainValueHistory;
    private Bitmap mMicIcon = null;
    private float mLevel = 0.0f;
    private float mTimeout = 0.0f;
    private float mProgress = 0.0f;
    private State mState = State.WAITING;

    public enum State {
        WAITING,
        LISTENING,
        PROCESSING,
        FINISHED
    }

    public DictationElement() {
        this.mGainValueHistory = null;
        this.mGainValueHistory = new ArrayList<>();
        this.mGainValueHistory.add(Float.valueOf(INITIAL_GAIN));
    }

    public void reset() {
        this.mGainValueHistory.clear();
        this.mGainValueHistory.add(Float.valueOf(INITIAL_GAIN));
        this.mLevel = 0.0f;
        this.mTimeout = 0.0f;
        this.mProgress = 0.0f;
        this.mState = State.WAITING;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        Rect rect = getElementRect(theme, screenBounds.width(), screenBounds.height());
        int centreX = rect.left + (rect.width() / 2);
        int centreY = rect.top + (rect.height() / 2);
        float backgroundRadius = 28.0f;
        if (this.mState == State.PROCESSING || this.mState == State.FINISHED) {
            backgroundRadius = 32.0f;
        }
        canvas.drawCircle(centreX, centreY, backgroundRadius, theme.getSolidColorPaint(getBackColor()));
        if (this.mState == State.LISTENING || this.mState == State.WAITING) {
            Rect levelRect = new Rect(centreX - 28, centreY - 32, centreX + 28, centreY + 32);
            if (this.mState == State.LISTENING) {
                levelRect.top = (int) (levelRect.top + (levelRect.height() - (levelRect.height() * this.mLevel)));
            }
            canvas.save();
            canvas.clipRect(levelRect);
            canvas.drawCircle(centreX, centreY, 28.0f, theme.getSolidColorPaint(getColor()));
            canvas.restore();
        }
        int borderWidth = getBorderThickness();
        rect.inset(borderWidth, borderWidth);
        canvas.drawArc(new RectF(rect), 0.0f, 360.0f, false, theme.getStrokePaint(getBorderColor(), borderWidth));
        if (this.mState == State.LISTENING) {
            float angle = MathHelper.wrap(0.0f, 360.0f, 360.0f * this.mTimeout);
            canvas.drawArc(new RectF(rect), angle, angle + 45.0f, false, theme.getStrokePaint(getActiveColor(), borderWidth));
        } else if (this.mState == State.PROCESSING) {
            canvas.drawArc(new RectF(rect), MathHelper.wrap(0.0f, 360.0f, (360.0f * this.mProgress) - 90.0f), 60.0f, false, theme.getStrokePaint(getInActiveColor(), borderWidth));
        }
        if (this.mMicIcon != null) {
            int micColour = -12500671;
            if (this.mState == State.PROCESSING || this.mState == State.FINISHED) {
                micColour = -5263441;
            }
            int x = rect.left + ((rect.width() - this.mMicIcon.getWidth()) / 2);
            int y = rect.top + ((rect.height() - this.mMicIcon.getHeight()) / 2);
            canvas.drawBitmap(this.mMicIcon, (Rect) null, new Rect(x, y, this.mMicIcon.getWidth() + x, this.mMicIcon.getHeight() + y), theme.getMaskPaint(micColour));
        }
    }

    public synchronized Bitmap getMicIcon() {
        return this.mMicIcon;
    }

    public synchronized void setMicIcon(Bitmap micIcon) {
        this.mHasChanged = true;
        this.mMicIcon = micIcon;
    }

    public synchronized float getLevel() {
        return this.mLevel;
    }

    public synchronized void setLevel(float level) {
        this.mGainValueHistory.clear();
        this.mGainValueHistory.add(Float.valueOf(level));
        this.mHasChanged = true;
        this.mLevel = MathHelper.clamp(0.0f, 1.0f, level);
    }

    public synchronized float getTimeout() {
        return this.mTimeout;
    }

    public synchronized void updateLevel(float level) {
        this.mGainValueHistory.remove(0);
        this.mGainValueHistory.add(Float.valueOf(MathHelper.clamp(0.0f, 1.0f, level)));
        this.mHasChanged = true;
        float gainSum = 0.0f;
        for (int i = 0; i < this.mGainValueHistory.size(); i++) {
            gainSum += this.mGainValueHistory.get(i).floatValue();
        }
        this.mLevel = gainSum / this.mGainValueHistory.size();
    }

    public synchronized void setTimeout(float timeout) {
        this.mHasChanged = true;
        this.mTimeout = MathHelper.clamp(0.0f, 1.0f, timeout);
    }

    public synchronized float getProgress() {
        return this.mProgress;
    }

    public synchronized void setProgress(float progress) {
        this.mHasChanged = true;
        this.mProgress = MathHelper.clamp(0.0f, 1.0f, progress);
    }

    public synchronized State getState() {
        return this.mState;
    }

    public synchronized void setState(State state) {
        this.mHasChanged = true;
        this.mState = state;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int width, int height, Rect out_bounds) {
        super.calculateRect(theme, width, height, out_bounds);
        int totalSize = (getBorderThickness() * 2) + 64;
        out_bounds.set(0, 0, totalSize, totalSize);
    }
}
