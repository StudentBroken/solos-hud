package com.kopin.pupil.pagerenderer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import com.kopin.core.Core;
import com.kopin.pupil.bluetooth.R;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.ui.VCNotification;
import com.kopin.pupil.ui.elements.BaseElement;
import com.kopin.pupil.ui.elements.ImageElement;
import com.kopin.pupil.ui.elements.SingleLineTextElement;
import com.kopin.pupil.ui.elements.TextElement;
import com.kopin.pupil.util.MathHelper;
import com.kopin.pupil.util.MutableInt;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes25.dex */
public class PageRenderer {
    public static final String BORDER_COLOR = "border_color";
    public static final String BORDER_HEIGHT = "border_height";
    public static final String COLOR = "color";
    public static final String HEIGHT = "height";
    public static final int JPEG_COMPRESSION = 100;
    private static final int SMALL_TEXT_SIZE = 24;
    private Context mContext;
    private Bitmap mBitmap = null;
    private Canvas mCanvas = null;
    private Rect mScreenRect = null;
    private Rect mPreviousMask = null;
    private Rect mUpdatedArea = null;
    private BasePage mPreviousPage = null;
    private VCNotification mPreviousToast = null;
    private final Object mLockObject = new Object();
    private byte[] mRLEBuffer = null;

    public PageRenderer(int width, int height, Context context) {
        this.mContext = null;
        this.mContext = context;
        initialSetup(width, height);
    }

    private void initialSetup(int width, int height) {
        this.mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        this.mScreenRect = new Rect(0, 0, width, height);
        this.mPreviousMask = new Rect(this.mScreenRect);
        this.mPreviousPage = null;
        this.mRLEBuffer = new byte[width * height * 3];
    }

    public Bitmap getBitmap() {
        Bitmap bitmap;
        synchronized (this.mLockObject) {
            bitmap = this.mBitmap;
        }
        return bitmap;
    }

    public void reset(boolean full) {
        if (full) {
            this.mScreenRect.set(0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight());
        }
        this.mPreviousMask.set(this.mScreenRect);
        this.mPreviousPage = null;
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00b6 A[Catch: Exception -> 0x00ce, all -> 0x01aa, TRY_ENTER, TryCatch #0 {Exception -> 0x00ce, blocks: (B:19:0x0043, B:21:0x0049, B:23:0x0051, B:24:0x0061, B:26:0x006c, B:27:0x0083, B:57:0x00f0, B:60:0x0105, B:43:0x00b6, B:45:0x00bc), top: B:87:0x0043, outer: #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean renderPage(com.kopin.pupil.ui.BasePage r19, com.kopin.pupil.pagerenderer.Theme r20, com.kopin.pupil.ui.VCNotification r21) {
        /*
            Method dump skipped, instruction units count: 488
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kopin.pupil.pagerenderer.PageRenderer.renderPage(com.kopin.pupil.ui.BasePage, com.kopin.pupil.pagerenderer.Theme, com.kopin.pupil.ui.VCNotification):boolean");
    }

    public byte[] getBytesPNG(Rect out_size, boolean rotate) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        compressBitmap(Bitmap.CompressFormat.PNG, 0, outStream, rotate);
        if (out_size != null) {
            out_size.set(this.mUpdatedArea);
            if (rotate) {
                Rect temp = new Rect(out_size);
                out_size.left = this.mScreenRect.width() - temp.right;
                out_size.top = this.mScreenRect.height() - temp.bottom;
                out_size.right = this.mScreenRect.width() - temp.left;
                out_size.bottom = this.mScreenRect.height() - temp.top;
            }
        }
        return outStream.toByteArray();
    }

    public byte[] getBytesJPEG(Rect out_size, boolean rotate) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        compressBitmap(Bitmap.CompressFormat.JPEG, 100, outStream, rotate);
        if (out_size != null) {
            out_size.set(getUpdatedRect());
            if (rotate) {
                Rect temp = new Rect(out_size);
                out_size.left = this.mScreenRect.width() - temp.right;
                out_size.top = this.mScreenRect.height() - temp.bottom;
                out_size.right = this.mScreenRect.width() - temp.left;
                out_size.bottom = this.mScreenRect.height() - temp.top;
            }
        }
        return outStream.toByteArray();
    }

    public byte[] getBytesRLE565(Rect out_size, MutableInt length, boolean rotate) {
        byte[] buffer;
        Rect rect = getUpdatedRect();
        if (rotate) {
            rect = new Rect(this.mScreenRect.width() - rect.right, this.mScreenRect.height() - rect.bottom, this.mScreenRect.width() - rect.left, this.mScreenRect.height() - rect.top);
        }
        synchronized (this.mLockObject) {
            length.setValue(Core.convertToRLE(this.mBitmap, rect.left, rect.top, rect.width(), rect.height(), this.mRLEBuffer, rotate));
            buffer = Arrays.copyOf(this.mRLEBuffer, length.getValue());
        }
        if (out_size != null) {
            out_size.set(rect);
        }
        return buffer;
    }

    public byte[] getBytesRGB565(Rect out_size, MutableInt length, boolean rotate) {
        Rect rect = getUpdatedRect();
        if (rotate) {
            rect = new Rect(this.mScreenRect.width() - rect.right, this.mScreenRect.height() - rect.bottom, this.mScreenRect.width() - rect.left, this.mScreenRect.height() - rect.top);
        }
        synchronized (this.mLockObject) {
            length.setValue(Core.convertToRGB565(this.mBitmap, rect.left, rect.top, rect.width(), rect.height(), this.mRLEBuffer, rotate));
        }
        if (out_size != null) {
            out_size.set(rect);
        }
        return this.mRLEBuffer;
    }

    public byte[] getBytesRGB111(Rect out_size, MutableInt length, boolean rotate) {
        byte[] buffer;
        Rect rect = getUpdatedRect(rotate);
        synchronized (this.mLockObject) {
            length.setValue(Core.convertToRGB111(this.mBitmap, rect.left, rect.top, rect.width(), rect.height(), this.mRLEBuffer, rotate));
            buffer = Arrays.copyOf(this.mRLEBuffer, length.getValue());
        }
        if (out_size != null) {
            out_size.set(rect);
        }
        return buffer;
    }

    public byte[] getBytesRLE111(Rect out_size, MutableInt length, boolean rotate) {
        return null;
    }

    private void compressBitmap(Bitmap.CompressFormat format, int quality, ByteArrayOutputStream out_stream, boolean rotate) {
        Bitmap bitmap;
        synchronized (this.mLockObject) {
            if (this.mPreviousMask.equals(this.mScreenRect)) {
                bitmap = this.mBitmap;
                if (rotate) {
                    Matrix matrix = new Matrix();
                    matrix.setRotate(180.0f, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                }
            } else {
                Rect clamped = MathHelper.shrinkRect(this.mScreenRect, this.mUpdatedArea);
                if (clamped.equals(new Rect(0, 0, 0, 0))) {
                    clamped = new Rect(0, 0, 1, 1);
                }
                bitmap = Bitmap.createBitmap(this.mBitmap, clamped.left, clamped.top, clamped.width(), clamped.height());
                if (rotate) {
                    Matrix matrix2 = new Matrix();
                    matrix2.setRotate(180.0f, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix2, false);
                }
            }
            bitmap.compress(format, quality, out_stream);
        }
    }

    private Rect getUpdatedRect() {
        return this.mPreviousMask.equals(this.mScreenRect) ? this.mPreviousMask : MathHelper.shrinkRect(this.mScreenRect, this.mUpdatedArea);
    }

    private Rect getUpdatedRect(boolean andRotate) {
        Rect rect = getUpdatedRect();
        if (andRotate) {
            return new Rect(this.mScreenRect.width() - rect.right, this.mScreenRect.height() - rect.bottom, this.mScreenRect.width() - rect.left, this.mScreenRect.height() - rect.top);
        }
        return rect;
    }

    private Rect calculateToastRect(Theme theme, VCNotification notification) {
        Rect rect = new Rect();
        Paint paint = theme.getTextPaint(0, 24, 0);
        int width = ((int) paint.measureText(notification.getHeadline())) + 21 + 17 + 14;
        int height = (int) paint.getFontSpacing();
        rect.left = (this.mScreenRect.width() - width) / 2;
        rect.right = rect.left + width;
        rect.top = (this.mScreenRect.height() - height) / 2;
        rect.bottom = rect.top + height;
        rect.inset(-10, -10);
        return rect;
    }

    private void drawPage(Theme theme, BasePage page, Rect maskedRect, ArrayList<BaseElement> effected) {
        if (effected == null || effected.size() == 0 || maskedRect == null) {
            drawBackground(page, theme, this.mScreenRect);
            this.mUpdatedArea = new Rect(maskedRect);
        }
        this.mCanvas.save();
        this.mCanvas.clipRect(maskedRect);
        drawElements(theme, effected, maskedRect, page);
        this.mCanvas.restore();
        this.mUpdatedArea = new Rect(maskedRect);
    }

    private void drawElements(Theme theme, ArrayList<BaseElement> effected, Rect maskedRect, BasePage parentPage) {
        drawBackground(parentPage, theme, maskedRect);
        for (BaseElement element : effected) {
            if (!(element instanceof TextElement) && !(element instanceof SingleLineTextElement)) {
                this.mCanvas.save();
                this.mCanvas.clipRect(element.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height()));
            }
            try {
                element.onDraw(this.mCanvas, theme, this.mScreenRect, parentPage);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            if (!(element instanceof TextElement) && !(element instanceof SingleLineTextElement)) {
                this.mCanvas.restore();
            }
        }
    }

    private void drawNotification(Theme theme, VCNotification notification, Rect toastMask, BasePage currentPage) {
        if (notification != null) {
            VCNotification.VCNotificationType type = notification.getNotificationType();
            switch (type) {
                case ACTIONABLE:
                    drawFullscreenNotification(notification, theme);
                    break;
                case POPUP:
                    drawPopupNotification(notification, theme);
                    break;
                case TOAST:
                    drawToastNotification(notification, theme, toastMask, currentPage);
                    break;
                case DROP_DOWN:
                    drawDropDown(notification, theme);
                    break;
            }
        }
    }

    private void drawFullscreenNotification(VCNotification notification, Theme theme) {
        ThemeElement backgroundElement = theme.getTheme(BaseElement.class);
        this.mCanvas.drawRect(new Rect(0, 0, this.mScreenRect.width(), this.mScreenRect.height()), theme.getSolidColorPaint(backgroundElement.backColor));
        int themeColor = theme.getColor(Theme.COLOR_THEME).intValue();
        this.mCanvas.drawRect(new Rect(0, 0, 480, 53), theme.getSolidColorPaint(themeColor));
        this.mCanvas.drawRect(new Rect(0, 187, 480, 188), theme.getSolidColorPaint(themeColor));
        SingleLineTextElement actionElement = new SingleLineTextElement();
        SingleLineTextElement.TextPart actionTextPart = new SingleLineTextElement.TextPart(theme.getColor("default_font").intValue(), theme.getTextSize("small").intValue(), notification.getAcceptText(), (byte) 0);
        actionElement.addPart(actionTextPart);
        actionElement.setAlignment(9);
        actionElement.setOffset(new Point(30, 13));
        drawText(theme, actionElement);
        SingleLineTextElement cancelElement = new SingleLineTextElement();
        SingleLineTextElement.TextPart cancelTextPart = new SingleLineTextElement.TextPart(theme.getColor("default_font").intValue(), theme.getTextSize("small").intValue(), notification.getDismissText(), (byte) 0);
        cancelElement.addPart(cancelTextPart);
        cancelElement.setAlignment(10);
        cancelElement.setOffset(new Point(30, 13));
        drawText(theme, cancelElement);
        ImageElement notificationIcon = new ImageElement();
        notificationIcon.styleFromTheme(theme);
        notificationIcon.setImage(getImage(R.drawable.notification));
        notificationIcon.setAlignment(6);
        notificationIcon.setOffset(new Point(30, 14));
        notificationIcon.setSize(32, 32);
        Rect rect = notificationIcon.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height());
        Paint mask = theme.getMaskPaint(notificationIcon.getColor());
        this.mCanvas.drawBitmap(notificationIcon.getImage(), (Rect) null, rect, mask);
        TextElement titleElement = new TextElement();
        titleElement.styleFromTheme(theme);
        titleElement.setAlignment(5);
        titleElement.setOffset(new Point(30, 10));
        titleElement.setText(notification.getHeadline());
        titleElement.setSize(24);
        titleElement.setColor(theme.getColor(Theme.COLOR_CONTRAST_FONT).intValue());
        titleElement.setWrapWidth(325);
        titleElement.setWrapHeight(40);
        drawMultiline(theme, titleElement);
        TextElement messageElement = new TextElement();
        messageElement.styleFromTheme(theme);
        messageElement.setAlignment(5);
        messageElement.setOffset(new Point(30, 70));
        messageElement.setText(notification.getSubtitle());
        messageElement.setSize(26);
        messageElement.setWrapWidth(390);
        messageElement.setWrapHeight(100);
        drawMultiline(theme, messageElement);
    }

    private void drawPopupNotification(VCNotification notification, Theme theme) {
        Paint paint;
        Bundle extra = notification.getBundle();
        ThemeElement element = theme.getTheme(BaseElement.class);
        if (extra != null) {
            paint = theme.getSolidColorPaint(extra.getInt("color", element.backColor));
        } else {
            paint = theme.getSolidColorPaint(element.backColor);
        }
        this.mCanvas.drawRect(new Rect(0, 0, 428, 50), paint);
        this.mCanvas.drawRect(new Rect(0, 50, 428, 51), theme.getSolidColorPaint(theme.getColor(Theme.COLOR_THEME).intValue()));
        TextElement text = new TextElement();
        text.setText(notification.getHeadline());
        text.styleFromTheme(theme);
        text.setSize(20);
        text.setWrapWidth(346);
        text.setAlignment(5);
        text.setOffset(50, 33);
        text.setSingleLine(true);
        Paint paintText = theme.getTextPaint(text.getColor(), text.getSize(), text.getStyle());
        Rect rectText = text.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height());
        List<String> lines = StringHelper.wrap(0, text.getText(), paintText, text.getWrapWidth(), (int) paintText.getFontSpacing(), true, new MutableInt(0), false);
        if (!lines.isEmpty()) {
            this.mCanvas.drawText(lines.get(0), rectText.left, rectText.top, paintText);
        }
        ImageElement icon = new ImageElement();
        if (extra != null && extra.containsKey("bitmap")) {
            try {
                Bitmap source = (Bitmap) extra.getParcelable("bitmap");
                if (source != null) {
                    icon.setImage(source);
                }
            } catch (Exception e) {
                Log.e("PageRenderer", "new exception", e);
            }
        }
        icon.setAlignment(5);
        icon.setOffset(new Point(16, 16));
        icon.setWidth(19);
        icon.setHeight(19);
        Rect rect = icon.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height());
        Paint mask = theme.getMaskPaint(-1);
        this.mCanvas.drawBitmap(icon.getImage(), (Rect) null, rect, mask);
    }

    private void drawToastNotification(VCNotification notification, Theme theme, Rect mask, BasePage currentPage) {
        if (notification != null) {
            Rect tempRect = new Rect(mask);
            this.mCanvas.drawRect(tempRect, theme.getSolidColorPaint(theme.getColor(Theme.COLOR_THEME_BORDER).intValue()));
            tempRect.inset(2, 2);
            this.mCanvas.drawRect(tempRect, theme.getSolidColorPaint(theme.getColor(Theme.COLOR_THEME).intValue()));
            tempRect.inset(-2, -2);
            ImageElement icon = new ImageElement();
            icon.setParent(currentPage);
            Bundle extra = notification.getBundle();
            if (extra != null) {
                if (extra.containsKey(SettingsJsonConstants.APP_ICON_KEY)) {
                    String source = extra.getString(SettingsJsonConstants.APP_ICON_KEY);
                    if (source != null) {
                        icon.setImage(currentPage.getImage(source));
                    }
                } else if (extra.containsKey("bitmap")) {
                    try {
                        Bitmap source2 = (Bitmap) extra.getParcelable("bitmap");
                        if (source2 != null) {
                            icon.setImage(source2);
                        } else {
                            Log.e("PageRenderer", "source is null");
                        }
                    } catch (Exception e) {
                        Log.e("PageRenderer", "new exception", e);
                    }
                }
            }
            icon.setAlignment(1);
            icon.setOffset(new Point(mask.left + 17, 0));
            icon.setWidth(21);
            SingleLineTextElement element = new SingleLineTextElement();
            element.addPart(new SingleLineTextElement.TextPart(theme.getColor(Theme.COLOR_CONTRAST_FONT).intValue(), 24, notification.getHeadline(), (byte) 0));
            element.setAlignment(1);
            element.setOffset(mask.left + 21 + 17 + 14, 0);
            drawText(theme, element);
            icon.onDraw(this.mCanvas, theme, this.mScreenRect, currentPage);
        }
    }

    private void drawDropDown(VCNotification notification, Theme theme) {
        Bundle extra = notification.getBundle();
        int height = 50;
        int borderHeight = 2;
        ThemeElement element = theme.getTheme(BaseElement.class);
        int color = element.backColor;
        int borderColor = element.borderColor;
        if (extra != null) {
            color = extra.getInt("color", color);
            borderColor = extra.getInt(BORDER_COLOR, borderColor);
            height = extra.getInt("height", 50);
            borderHeight = extra.getInt(BORDER_HEIGHT, 2);
        }
        this.mCanvas.drawRect(new Rect(0, 0, 428, height), theme.getSolidColorPaint(color));
        this.mCanvas.drawRect(new Rect(0, height - borderHeight, 428, height), theme.getSolidColorPaint(borderColor));
        if (notification.getIcon() != null) {
            ImageElement icon = new ImageElement();
            icon.setImage(notification.getIcon());
            icon.setAlignment(5);
            icon.setOffset(new Point(16, 10));
            icon.setWidth(28);
            icon.setHeight(28);
            Rect rect = icon.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height());
            Paint mask = theme.getMaskPaint(-1);
            this.mCanvas.drawBitmap(icon.getImage(), (Rect) null, rect, mask);
        }
        int x = 44 + 15;
        int width = 369 - 15;
        for (VCNotification.TextPart textPart : notification.getTextParts()) {
            TextElement text = new TextElement();
            text.setText(textPart.getText());
            text.styleFromTheme(theme);
            text.setSize(textPart.getSize());
            text.setWrapWidth(width);
            text.setAlignment(5);
            text.setOffset(x, 32);
            text.setSingleLine(true);
            text.setColor(textPart.getColor());
            Paint textPaint = theme.getTextPaint(textPart.getColor(), textPart.getSize(), textPart.getTextStyle());
            Rect rectText = text.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height());
            List<String> lines = StringHelper.wrap(0, text.getText(), textPaint, text.getWrapWidth(), (int) textPaint.getFontSpacing(), true, new MutableInt(0), false);
            if (!lines.isEmpty()) {
                int textWidth = (int) textPaint.measureText(lines.get(0));
                this.mCanvas.drawText(lines.get(0), rectText.left, rectText.top, textPaint);
                x += textWidth + 8;
                width -= textWidth + 8;
                if (width <= 0) {
                    return;
                }
            }
        }
    }

    public List<VCNotification.TextPart> getBrakedMessage(VCNotification.TextPart message, Theme theme) {
        List<VCNotification.TextPart> result = new ArrayList<>();
        int x = 46 + 15;
        int width = 367 - 15;
        TextElement text = new TextElement();
        text.setText(message.getText());
        text.styleFromTheme(theme);
        text.setSize(message.getSize());
        text.setWrapWidth(width);
        text.setAlignment(5);
        text.setOffset(x, 32);
        text.setSingleLine(true);
        text.setColor(message.getColor());
        Paint textPaint = theme.getTextPaint(message.getColor(), message.getSize(), message.getTextStyle());
        List<String> lines = StringHelper.wrap(0, text.getText(), textPaint, text.getWrapWidth(), 10000000, false, new MutableInt(0), false);
        for (String string : lines) {
            result.add(new VCNotification.TextPart(message, string));
        }
        return result;
    }

    private void drawMultiline(Theme theme, TextElement element) {
        Paint paint = theme.getTextPaint(element.getColor(), element.getSize(), element.getStyle());
        Rect rect = element.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height());
        this.mCanvas.save();
        this.mCanvas.clipRect(rect);
        int offsetY = element.getStartYOffset(theme);
        List<String> lines = StringHelper.wrap(0, element.getText(), paint, element.getWrapWidth(), element.getWrapHeight(), true, null, false);
        for (String line : lines) {
            this.mCanvas.drawText(line, rect.left, rect.top + offsetY, paint);
            offsetY = (int) (offsetY + paint.getFontSpacing());
        }
        this.mCanvas.restore();
    }

    private void drawBackground(BasePage page, Theme theme, Rect rect) {
        Paint paint = theme.getSolidColorPaint(page.getBackgroundColor() | ViewCompat.MEASURED_STATE_MASK);
        this.mCanvas.drawRect(rect, paint);
    }

    private void drawText(Theme theme, SingleLineTextElement element) {
        Rect rect = element.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height());
        this.mCanvas.save();
        this.mCanvas.clipRect(rect);
        int positionX = 0;
        int positionY = rect.top + element.getStartYOffset(theme);
        int currentSpacing = 0;
        for (SingleLineTextElement.Part part : element.getParts()) {
            if (part instanceof SingleLineTextElement.LineEndPart) {
                positionY += currentSpacing;
                currentSpacing = 0;
                positionX = 0;
            } else if (part instanceof SingleLineTextElement.TextPart) {
                SingleLineTextElement.TextPart textPart = (SingleLineTextElement.TextPart) part;
                Paint textPaint = theme.getTextPaint(textPart.getColor(), textPart.getSize(), textPart.getStyle());
                if (textPaint != null) {
                    this.mCanvas.drawText(textPart.getText(), rect.left + positionX, positionY, textPaint);
                    currentSpacing = Math.max(currentSpacing, (int) textPaint.getFontSpacing());
                    positionX = (int) (positionX + textPaint.measureText(textPart.getText()));
                }
            }
        }
        this.mCanvas.restore();
    }

    private Rect getEffectedRect(Theme theme, ArrayList<BaseElement> elements) {
        Rect result = null;
        for (BaseElement element : elements) {
            if (element != null && element.hasChanged()) {
                Rect rect = element.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height());
                if (result == null) {
                    result = new Rect(rect);
                }
                result = MathHelper.combineRects(rect, result);
            }
        }
        return result;
    }

    private Rect getMinimumRect(Theme theme, ArrayList<BaseElement> elements, Rect forcedMask, ArrayList<BaseElement> out_effected) {
        Rect result;
        Rect mask = getEffectedRect(theme, elements);
        if (mask == null) {
            if (forcedMask != null) {
                mask = forcedMask;
            } else {
                Rect result2 = this.mPreviousMask;
                return result2;
            }
        }
        if (mask.contains(this.mPreviousMask)) {
            this.mPreviousMask = mask;
            result = mask;
        } else {
            result = MathHelper.combineRects(mask, this.mPreviousMask);
            this.mPreviousMask = mask;
        }
        if (out_effected == null) {
            return null;
        }
        for (BaseElement element : elements) {
            if (Rect.intersects(result, element.getElementRect(theme, this.mScreenRect.width(), this.mScreenRect.height()))) {
                out_effected.add(element);
            }
        }
        return result;
    }

    private Bitmap getImage(int id) {
        Resources resources;
        if (this.mContext == null || (resources = this.mContext.getResources()) == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = 1;
        options.inScaled = false;
        return BitmapFactory.decodeResource(resources, id, options);
    }
}
