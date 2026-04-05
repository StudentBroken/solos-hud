package com.kopin.pupil.ui;

import android.util.Log;
import com.kopin.pupil.VCContext;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.elements.BaseElement;
import com.kopin.pupil.ui.elements.RelativeElement;
import com.kopin.pupil.ui.elements.SingleLineTextElement;
import com.kopin.pupil.ui.elements.TextElement;
import com.kopin.pupil.ui.elements.TextInterface;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes25.dex */
public class ApplicationPage extends BasePage {
    private HashMap<String, BaseElement> mElementMap;
    private boolean mPaging;
    protected SingleLineTextElement.TextPart mTimePart;
    private LinkedList<TextElement> pagingElements;
    private LinkedList<RelativeElement> relativeElements;

    public ApplicationPage(VCContext parentState, Theme theme) {
        super(parentState, theme, false);
        this.mTimePart = null;
        this.mPaging = false;
    }

    @Override // com.kopin.pupil.ui.BasePage
    public void preDraw(Theme theme) {
    }

    @Override // com.kopin.pupil.ui.BasePage
    public void addElement(BaseElement element) {
        super.addElement(element);
        if (element.getName() != null) {
            if (this.mElementMap == null) {
                this.mElementMap = new HashMap<>();
            }
            this.mElementMap.put(element.getName(), element);
        }
        if (element instanceof TextElement) {
            TextElement te = (TextElement) element;
            if (te.isPaging()) {
                if (this.pagingElements == null) {
                    this.pagingElements = new LinkedList<>();
                }
                this.pagingElements.add(te);
            }
        }
        if (this.relativeElements == null) {
            this.relativeElements = new LinkedList<>();
        }
        if (element.hasRelativePosition()) {
            this.relativeElements.add(element);
        }
    }

    public void setPaging(boolean paging) {
        this.mPaging = paging;
    }

    public void configure() {
        for (RelativeElement element : this.relativeElements) {
            String rightOf = element.getElementName();
            if (rightOf != null) {
                BaseElement parent = this.mElementMap.get(rightOf);
                if (parent != null) {
                    element.setElement(parent);
                } else {
                    Log.e("RelativePositioning", "Could not find element with ID: " + rightOf);
                }
            }
        }
        configurePaging();
    }

    private void configurePaging() {
        if (this.mPaging) {
            for (TextElement pagingElement : this.pagingElements) {
                for (BaseElement element : getElements()) {
                    if (element instanceof SingleLineTextElement) {
                        SingleLineTextElement div = (SingleLineTextElement) element;
                        for (SingleLineTextElement.Part part : div.getParts()) {
                            if (part instanceof SingleLineTextElement.TextPart) {
                                checkElement((SingleLineTextElement.TextPart) part, pagingElement);
                            }
                        }
                    } else {
                        checkElement(element, pagingElement);
                    }
                }
                pagingElement.resetTotalPageCounterView();
            }
        }
    }

    private void checkElement(SingleLineTextElement.TextPart element, TextElement pElement) {
        if (pElement.getCurrentPageIdViewId() != null && pElement.getCurrentPageIdViewId().equalsIgnoreCase(element.getName())) {
            if (pElement != null) {
                pElement.setCurrentPageIdView(element);
            }
        } else if (pElement.getTotalPageCounterViewId() != null && pElement.getTotalPageCounterViewId().equalsIgnoreCase(element.getName()) && pElement != null) {
            pElement.setTotalPageCounterView(element);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void checkElement(BaseElement baseElement, TextElement pElement) {
        if (pElement.getCurrentPageIdViewId() != null && pElement.getCurrentPageIdViewId().equalsIgnoreCase(baseElement.getName())) {
            if (baseElement instanceof TextInterface) {
                TextInterface pCurrent = (TextInterface) baseElement;
                if (pElement != null) {
                    pElement.setCurrentPageIdView(pCurrent);
                    return;
                }
                return;
            }
            return;
        }
        if (pElement.getTotalPageCounterViewId() != null && pElement.getTotalPageCounterViewId().equalsIgnoreCase(baseElement.getName()) && (baseElement instanceof TextInterface)) {
            TextInterface pTotal = (TextInterface) baseElement;
            if (pElement != null) {
                pElement.setTotalPageCounterView(pTotal);
            }
        }
    }

    public String getTime() {
        DateFormat formatter = android.text.format.DateFormat.getTimeFormat(getContext());
        return formatter.format(new Date());
    }

    public String getDate() {
        DateFormat formatter = android.text.format.DateFormat.getDateFormat(getContext());
        return formatter.format(new Date());
    }

    private void updateTime(String time) {
        this.mTimePart.setText(time);
    }

    private GregorianCalendar createCreationDate() {
        GregorianCalendar calCreationDate = new GregorianCalendar();
        int milliseconds = calCreationDate.get(14);
        int seconds = calCreationDate.get(13);
        calCreationDate.add(14, milliseconds * (-1));
        calCreationDate.add(13, seconds * (-1));
        calCreationDate.add(12, 1);
        return calCreationDate;
    }
}
