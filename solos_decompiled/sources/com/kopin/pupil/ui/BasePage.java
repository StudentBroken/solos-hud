package com.kopin.pupil.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import com.kopin.accessory.ImageCodec;
import com.kopin.pupil.VCContext;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.pagerenderer.ThemeElement;
import com.kopin.pupil.ui.elements.BaseElement;
import com.kopin.pupil.ui.elements.BoxElement;
import com.kopin.pupil.ui.elements.ImageElement;
import com.kopin.pupil.ui.elements.ProgressElement;
import com.kopin.pupil.ui.elements.SingleLineTextElement;
import com.kopin.pupil.ui.elements.TextElement;
import com.kopin.pupil.util.UpdateElementValues;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes25.dex */
public abstract class BasePage {
    protected static final String TAG = "BasePage";
    private int mBackgroundColor;
    private ImageCodec mCompression;
    protected ArrayList<BaseElement> mElements;
    private volatile boolean mIsReady;
    private boolean mOnPage;
    protected String mPageName;
    private VCContext mParentState;
    protected String mTemplate;
    private Theme mTheme;
    private Deque<UpdateElementValues> pageUpdates;

    public abstract void preDraw(Theme theme);

    public BasePage(VCContext parentState, Theme theme) {
        this(parentState, theme, false);
    }

    public BasePage(VCContext parentState, Theme theme, boolean isReady) {
        this.mPageName = "";
        this.mTemplate = "";
        this.mElements = new ArrayList<>();
        this.mTheme = null;
        this.mBackgroundColor = 0;
        this.mOnPage = false;
        this.pageUpdates = new LinkedList();
        this.mCompression = ImageCodec.RLE565;
        this.mIsReady = false;
        this.mParentState = parentState;
        this.mTheme = theme;
        ThemeElement element = theme.getTheme(BaseElement.class);
        setBackgroundColor(element.backColor);
        if (isReady) {
            markReady(false, false);
        }
    }

    public BasePage(VCContext parentState) {
        this.mPageName = "";
        this.mTemplate = "";
        this.mElements = new ArrayList<>();
        this.mTheme = null;
        this.mBackgroundColor = 0;
        this.mOnPage = false;
        this.pageUpdates = new LinkedList();
        this.mCompression = ImageCodec.RLE565;
        this.mIsReady = false;
        this.mParentState = parentState;
    }

    public void onStart() {
        this.mOnPage = true;
    }

    public void onStop() {
        this.mOnPage = false;
    }

    public ImageCodec compression() {
        return this.mCompression;
    }

    public void setCompression(ImageCodec compression) {
        this.mCompression = compression;
    }

    public final Theme getTheme() {
        return this.mTheme;
    }

    public String getPageName() {
        return this.mPageName;
    }

    public String getTemplate() {
        return this.mTemplate;
    }

    public void setTemplate(String template) {
        this.mTemplate = template;
    }

    public void setPageName(String mPageName) {
        this.mPageName = mPageName;
    }

    public Bitmap getImage(String id) {
        return this.mParentState.getImageFromCache(id);
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    protected void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    public void setParent(VCContext parent) {
        this.mParentState = parent;
    }

    public void addElement(BaseElement element) {
        if (!this.mElements.contains(element)) {
            element.setParent(this);
            this.mElements.add(element);
        }
    }

    public void removeElement(BaseElement element) {
        if (this.mElements.contains(element)) {
            element.setParent(null);
            this.mElements.remove(element);
        }
    }

    public void removeAllElements() {
        for (BaseElement element : this.mElements) {
            element.setParent(null);
        }
        this.mElements.clear();
    }

    protected Context getContext() {
        if (this.mParentState != null) {
            return this.mParentState.getContext();
        }
        return null;
    }

    protected void refresh(boolean full) {
        if (this.mParentState != null) {
            synchronized (this) {
                this.mParentState.refresh(full);
            }
        }
    }

    protected Bitmap getImage(int id) {
        if (this.mParentState == null) {
            return null;
        }
        return this.mParentState.getImage(id);
    }

    protected String getString(int id) {
        return this.mParentState != null ? this.mParentState.getString(id) : "";
    }

    protected String[] getStringArray(int id) {
        if (this.mParentState != null) {
            return this.mParentState.getStringArray(id);
        }
        return null;
    }

    protected Rect getBounds() {
        return this.mParentState != null ? this.mParentState.getBounds() : new Rect(0, 0, 0, 0);
    }

    public ArrayList<BaseElement> getElements() {
        return this.mElements;
    }

    public synchronized void updateTextPage(String elementID, int page) {
        for (int i = 0; i < this.mElements.size(); i++) {
            BaseElement e = this.mElements.get(i);
            if ((e instanceof TextElement) && e.getName().equals(elementID)) {
                ((TextElement) e).setPage(page);
            }
        }
        refresh(false);
    }

    public void bitmapChanged(String ID) {
        String source;
        String source2;
        if (this.mElements != null) {
            for (int i = 0; i < this.mElements.size(); i++) {
                BaseElement e = this.mElements.get(i);
                if ((e instanceof ImageElement) && (source2 = ((ImageElement) e).getSource()) != null && source2.equals(ID)) {
                    ((ImageElement) e).setSource(source2);
                }
                if ((e instanceof TextElement) && (source = ((TextElement) e).getSource()) != null && source.equals(ID)) {
                    ((TextElement) e).setSource(source);
                }
            }
        }
    }

    public Bundle getElementProperties(String elementID) {
        for (int i = 0; i < this.mElements.size(); i++) {
            BaseElement element = this.mElements.get(i);
            if (element.getName().equals(elementID)) {
                return element.getProperties();
            }
        }
        return null;
    }

    public void updateElement(String elementID, String attribute, Object value, boolean update) {
        if (!elementID.isEmpty()) {
            addPageUpdate(new UpdateElementValues(elementID, attribute, value));
        }
        if (update) {
            updateElements();
        }
    }

    public void updateElements() {
        try {
            synchronized (this) {
                while (!this.pageUpdates.isEmpty()) {
                    UpdateElementValues u = this.pageUpdates.remove();
                    updateElement(u.elementId, u.attribute, u.value);
                }
            }
            refresh(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPageUpdate(UpdateElementValues values) {
        this.pageUpdates.add(values);
    }

    private void updateElement(String elementID, String attribute, Object value) {
        for (int i = 0; i < this.mElements.size(); i++) {
            BaseElement element = this.mElements.get(i);
            if (element.getName().equals(elementID)) {
                if (element instanceof BoxElement) {
                    if (attribute.equals("color")) {
                        if (value instanceof String) {
                            int colour = getTheme().getColor((String) value).intValue();
                            element.setColor(colour);
                        } else {
                            element.setColor(((Integer) value).intValue());
                        }
                    }
                } else if (element instanceof ImageElement) {
                    ImageElement imageElement = (ImageElement) element;
                    if (attribute.equals("source")) {
                        imageElement.setSource((String) value);
                    }
                    if (attribute.equals("color")) {
                        imageElement.setColor(((Integer) value).intValue());
                    }
                } else if (!(element instanceof SingleLineTextElement)) {
                    if (element instanceof TextElement) {
                        TextElement textElement = (TextElement) element;
                        if (attribute.equals("source")) {
                            textElement.setSource((String) value);
                        }
                        if (attribute.equals("content")) {
                            textElement.setText((String) value);
                        } else if (attribute.equals("color")) {
                            if (value instanceof String) {
                                int colour2 = getTheme().getColor((String) value).intValue();
                                textElement.setColor(colour2);
                            } else {
                                textElement.setColor(((Integer) value).intValue());
                            }
                        } else if (attribute.equals("size")) {
                            textElement.setSize(((Integer) value).intValue());
                        } else if (attribute.equals("italic")) {
                            if (((Boolean) value).booleanValue()) {
                                textElement.setStyle(textElement.getStyle() | 2);
                            } else {
                                textElement.setStyle(textElement.getStyle() | (-3));
                            }
                        } else if (attribute.equals("bold")) {
                            if (((Boolean) value).booleanValue()) {
                                textElement.setStyle(textElement.getStyle() | 1);
                            } else {
                                textElement.setStyle(textElement.getStyle() | (-2));
                            }
                        } else if (attribute.equals(TextElement.TEXT_CURRENT_PAGE)) {
                            textElement.setPage(((Integer) value).intValue());
                        }
                    } else if (element instanceof ProgressElement) {
                        ProgressElement progressBar = (ProgressElement) element;
                        if (attribute.equals("progress")) {
                            progressBar.setProgress(((Integer) value).intValue());
                        }
                    }
                }
            }
            if (element instanceof SingleLineTextElement) {
                SingleLineTextElement textElement2 = (SingleLineTextElement) element;
                for (int j = 0; j < textElement2.getParts().size(); j++) {
                    SingleLineTextElement.Part part = textElement2.getParts().get(j);
                    if (part instanceof SingleLineTextElement.TextPart) {
                        SingleLineTextElement.TextPart textPart = (SingleLineTextElement.TextPart) part;
                        if (textPart.getName().equals(elementID)) {
                            if (attribute.equals("content")) {
                                textPart.setText((String) value);
                            } else if (attribute.equals("color")) {
                                if (value instanceof String) {
                                    int colour3 = getTheme().getColor((String) value).intValue();
                                    textPart.setColor(colour3);
                                } else {
                                    textPart.setColor(((Integer) value).intValue());
                                }
                            } else if (attribute.equals("size")) {
                                textPart.setSize(((Integer) value).intValue());
                            } else if (attribute.equals("italic")) {
                                if (((Boolean) value).booleanValue()) {
                                    textPart.setStyle(textPart.getStyle() | 2);
                                } else {
                                    textPart.setStyle(textPart.getStyle() | (-3));
                                }
                            } else if (attribute.equals("bold")) {
                                if (((Boolean) value).booleanValue()) {
                                    textPart.setStyle(textPart.getStyle() | 1);
                                } else {
                                    textPart.setStyle(textPart.getStyle() | (-2));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public final boolean isReady() {
        return this.mIsReady;
    }

    public final void markReady(boolean refresh, boolean forceRedraw) {
        this.mIsReady = true;
        if (refresh) {
            refresh(forceRedraw);
        }
    }

    public String toString() {
        return getClass().getSimpleName() + ": [name: " + this.mPageName + ", ready: " + this.mIsReady + "]";
    }
}
