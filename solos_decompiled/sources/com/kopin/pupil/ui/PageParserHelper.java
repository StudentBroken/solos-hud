package com.kopin.pupil.ui;

import android.graphics.Point;
import com.kopin.pupil.exception.XmlParserException;
import com.kopin.pupil.ui.elements.BaseElement;
import com.kopin.pupil.ui.elements.Sizable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes25.dex */
public class PageParserHelper {
    private static final String TAG = "PageParserHelper";
    private ApplicationPage mPage;
    private PageParser mParser;

    public enum HorizontalAlign {
        LEFT,
        RIGHT,
        CENTER
    }

    public enum VerticalAlign {
        TOP,
        BOTTOM,
        CENTER
    }

    PageParserHelper(PageParser parser) {
        this.mParser = parser;
    }

    static class Attribute {
        XmlParserException.ParserExtraInfo extra;
        String value;

        Attribute(String value, XmlParserException.ParserExtraInfo extra) {
            this.value = value;
            this.extra = extra;
        }
    }

    static class Element {
        static final int FILL = -2;
        static final int NOT_SET = Integer.MIN_VALUE;
        static final String SPACE = "___";
        int bottom;
        BaseElement element;
        XmlParserException.ParserExtraInfo extra;
        Attribute height;
        int left;
        int marginBottom;
        int marginLeft;
        int marginRight;
        int marginTop;
        String name;
        Attribute offsetX;
        Attribute offsetY;
        Container parent;
        int right;
        int tempOffX;
        int tempOffY;
        int top;
        Attribute width;
        int tempWidth = Integer.MIN_VALUE;
        int tempHeight = Integer.MIN_VALUE;

        Element(String name, XmlParserException.ParserExtraInfo extra) {
            this.name = name;
            this.extra = extra;
        }

        int width() {
            return this.right - this.left;
        }

        int height() {
            return this.bottom - this.top;
        }

        void printName(StringBuilder sb) {
            sb.append("<").append(this.name).append(": ");
            if (this.element != null) {
                sb.append(PageParserHelper.printAlignment(this.element));
                Point offset = this.element.getOffset();
                sb.append(" offX: ").append(offset.x);
                sb.append(", offY: ").append(offset.y);
                if (this.element instanceof Sizable) {
                    sb.append(", w: ").append(((Sizable) this.element).getWidth());
                    sb.append(", h: ").append(((Sizable) this.element).getHeight()).append(" ,");
                }
            }
            sb.append("mL: ").append(this.marginLeft);
            sb.append(", mT: ").append(this.marginTop);
            sb.append(", mR: ").append(this.marginRight);
            sb.append(", mB: ").append(this.marginBottom);
        }

        public String toString() {
            return toString(0);
        }

        public String toString(int indent) {
            StringBuilder sb = new StringBuilder(PageParserHelper.repeatString(SPACE, indent));
            printName(sb);
            sb.append(" >\n");
            return sb.toString();
        }
    }

    static class Container extends Element {
        int alignment;
        ArrayList<Element> elements;
        int paddingBottom;
        int paddingLeft;
        int paddingRight;
        int paddingTop;

        Container(String name, XmlParserException.ParserExtraInfo extra) {
            super(name, extra);
            this.elements = new ArrayList<>();
        }

        @Override // com.kopin.pupil.ui.PageParserHelper.Element
        public String toString() {
            int indent = 0;
            for (Container parent = this.parent; parent != null; parent = parent.parent) {
                indent++;
            }
            return "\n" + toString(indent);
        }

        @Override // com.kopin.pupil.ui.PageParserHelper.Element
        public String toString(int indent) {
            StringBuilder sb = new StringBuilder(PageParserHelper.repeatString("___", indent));
            printName(sb);
            sb.append(", pL: ").append(this.paddingLeft);
            sb.append(", pT: ").append(this.paddingTop);
            sb.append(", pR: ").append(this.paddingRight);
            sb.append(", pB: ").append(this.paddingBottom);
            sb.append(">\n");
            for (Element element : this.elements) {
                sb.append(element.toString(indent + 1));
            }
            sb.append(PageParserHelper.repeatString("___", indent));
            sb.append("</").append(this.name).append(">\n");
            return sb.toString();
        }
    }

    public void calculate(ApplicationPage page, Container container) throws XmlParserException {
        this.mPage = page;
        calculateBoundsHorizontal(container);
        calculateBoundsVertical(container);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void calculateBoundsHorizontal(com.kopin.pupil.ui.PageParserHelper.Container r18) throws com.kopin.pupil.exception.XmlParserException {
        /*
            Method dump skipped, instruction units count: 310
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kopin.pupil.ui.PageParserHelper.calculateBoundsHorizontal(com.kopin.pupil.ui.PageParserHelper$Container):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void calculateBoundsVertical(com.kopin.pupil.ui.PageParserHelper.Container r18) throws com.kopin.pupil.exception.XmlParserException {
        /*
            Method dump skipped, instruction units count: 310
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kopin.pupil.ui.PageParserHelper.calculateBoundsVertical(com.kopin.pupil.ui.PageParserHelper$Container):void");
    }

    private int processFitHorizontal(Element element) throws XmlParserException {
        if (element instanceof Container) {
            switch (getHorizontalAlign(((Container) element).alignment)) {
                case LEFT:
                    element.tempWidth = processFitAlignHorizontal((Container) element);
                    break;
                case RIGHT:
                    element.tempWidth = processFitAlignHorizontal((Container) element);
                    break;
                case CENTER:
                    element.tempWidth = processFitAlignHCenter((Container) element);
                    break;
            }
            return element.tempWidth;
        }
        Object obj = element.element;
        if (obj != null && (obj instanceof Sizable)) {
            try {
                element.tempWidth = ((Sizable) obj).getInnerWidth();
                return element.tempWidth;
            } catch (UnsupportedOperationException e) {
                throw new XmlParserException(e.getMessage(), element.extra);
            }
        }
        throw new XmlParserException("Element fit not supported for width with this type of element", element.extra);
    }

    private int processFitVertical(Element element) throws XmlParserException {
        if (element instanceof Container) {
            switch (getVerticalAlign(((Container) element).alignment)) {
                case TOP:
                    element.tempHeight = processFitAlignVertical((Container) element);
                    break;
                case BOTTOM:
                    element.tempHeight = processFitAlignVertical((Container) element);
                    break;
                case CENTER:
                    element.tempHeight = processFitAlignVCenter((Container) element);
                    break;
            }
            return element.tempHeight;
        }
        Object obj = element.element;
        if (obj != null && (obj instanceof Sizable)) {
            try {
                element.tempHeight = ((Sizable) obj).getInnerHeight();
                return element.tempHeight;
            } catch (UnsupportedOperationException e) {
                throw new XmlParserException(e.getMessage(), element.extra);
            }
        }
        throw new XmlParserException("Element fit not supported for height with this type of element", element.extra);
    }

    private int processFitAlignHorizontal(Container container) throws XmlParserException {
        int maxWidth = 0;
        for (Element element : container.elements) {
            Attribute width = element.width;
            if (width.value == null) {
                element.tempWidth = processFitHorizontal(element);
                if (element.tempWidth == -2) {
                    return -2;
                }
                maxWidth += element.marginLeft + element.tempWidth + element.marginRight;
            } else {
                if (width.value.equals("*") || width.value.endsWith("%")) {
                    throw new XmlParserException("Container does not have a width. Percentage width can't be used.", element.extra);
                }
                element.tempWidth = this.mParser.parseInt(width.value, width.extra);
                maxWidth += element.marginLeft + element.tempWidth + element.marginRight;
            }
        }
        return container.marginLeft + container.paddingLeft + maxWidth + container.paddingRight + container.marginRight;
    }

    private int processFitAlignHCenter(Container container) throws XmlParserException {
        int maxWidth = 0;
        boolean fill = false;
        for (Element element : container.elements) {
            Attribute width = element.width;
            if (width.value == null) {
                element.tempWidth = processFitHorizontal(element);
                if (element.tempWidth != -2) {
                    int elementWidth = element.marginLeft + element.tempWidth + element.marginRight;
                    if (maxWidth < elementWidth) {
                        maxWidth = elementWidth;
                    }
                } else {
                    fill = true;
                }
            } else if (width.value.equals("*") || width.value.endsWith("%")) {
                fill = true;
            } else {
                element.tempWidth = this.mParser.parseInt(width.value, width.extra);
                int elementWidth2 = element.marginLeft + element.tempWidth + element.marginRight;
                if (maxWidth < elementWidth2) {
                    maxWidth = elementWidth2;
                }
            }
        }
        if (maxWidth == 0 && fill) {
            return -2;
        }
        return container.marginLeft + container.paddingLeft + maxWidth + container.paddingRight + container.marginRight;
    }

    private int processFitAlignVertical(Container container) throws XmlParserException {
        int maxHeight = 0;
        for (Element element : container.elements) {
            Attribute height = element.height;
            if (height.value == null) {
                element.tempHeight = processFitVertical(element);
                if (element.tempHeight == -2) {
                    return -2;
                }
                maxHeight += element.marginTop + element.tempHeight + element.marginBottom;
            } else {
                if (height.value.equals("*")) {
                    return -2;
                }
                if (height.value.endsWith("%")) {
                    throw new XmlParserException("Container does not have a height. Percentage width can't be used.", element.extra);
                }
                element.tempHeight = this.mParser.parseInt(height.value, height.extra);
                maxHeight += element.marginTop + element.tempHeight + element.marginBottom;
            }
        }
        return container.marginTop + container.paddingTop + maxHeight + container.paddingBottom + container.marginBottom;
    }

    private int processFitAlignVCenter(Container container) throws XmlParserException {
        int maxHeight = 0;
        boolean fill = false;
        for (Element element : container.elements) {
            Attribute height = element.height;
            if (height.value == null) {
                element.tempHeight = processFitVertical(element);
                if (element.tempHeight != -2) {
                    int elementHeight = element.marginTop + element.tempHeight + element.marginBottom;
                    if (maxHeight < elementHeight) {
                        maxHeight = elementHeight;
                    }
                } else {
                    fill = true;
                }
            } else if (height.value.equals("*") || height.value.endsWith("%")) {
                fill = true;
            } else {
                element.tempHeight = this.mParser.parseInt(height.value, height.extra);
                int elementHeight2 = element.marginTop + element.tempHeight + element.marginBottom;
                if (maxHeight < elementHeight2) {
                    maxHeight = elementHeight2;
                }
            }
        }
        if (maxHeight == 0 && fill) {
            return -2;
        }
        return container.marginTop + container.paddingTop + maxHeight + container.paddingBottom + container.marginBottom;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void processElementsAlignLeft(Container container, int elementFillSpace) throws XmlParserException {
        int x;
        int x2 = container.left + container.paddingLeft;
        int maxRight = container.right - container.paddingRight;
        for (Element element : container.elements) {
            int x3 = element.tempOffX + x2 + element.marginLeft;
            element.left = x3;
            if (element.tempWidth == -2) {
                x = x3 + elementFillSpace;
            } else {
                x = x3 + element.tempWidth;
            }
            element.right = x;
            x2 = x + element.marginRight;
            if (element.left > maxRight) {
                element.left = maxRight;
            }
            if (element.right > maxRight) {
                element.right = maxRight;
            }
            BaseElement baseElement = element.element;
            if (baseElement != 0) {
                baseElement.setAlignment(baseElement.getAlignment() | 1);
                baseElement.setOffsetX(element.left);
                if (baseElement instanceof Sizable) {
                    ((Sizable) baseElement).setWidth(element.width());
                }
                this.mPage.addElement(baseElement);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void processElementsAlignRight(Container container, int elementFillSpace) throws XmlParserException {
        int x;
        int x2 = container.right - container.paddingRight;
        int maxLeft = container.left + container.paddingLeft;
        for (Element element : container.elements) {
            int x3 = (x2 - element.tempOffX) - element.marginRight;
            element.right = x3;
            if (element.tempWidth == -2) {
                x = x3 - elementFillSpace;
            } else {
                x = x3 - element.tempWidth;
            }
            element.left = x;
            x2 = x - element.marginLeft;
            if (element.left < maxLeft) {
                element.left = maxLeft;
            }
            if (element.right < maxLeft) {
                element.right = maxLeft;
            }
            BaseElement baseElement = element.element;
            if (baseElement != 0) {
                baseElement.setAlignment(baseElement.getAlignment() | 2);
                baseElement.setOffsetX(this.mParser.getScreenWidth() - element.right);
                if (baseElement instanceof Sizable) {
                    ((Sizable) baseElement).setWidth(element.width());
                }
                this.mPage.addElement(baseElement);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void processElementsAlignHCenter(Container container) throws XmlParserException {
        int maxWidth = (container.width() - container.paddingLeft) - container.paddingRight;
        int center = container.left + (container.width() / 2);
        for (Element element : container.elements) {
            if (element.tempWidth == -2) {
                element.tempWidth = (maxWidth - element.marginLeft) - element.marginRight;
            }
            int width = element.marginLeft + element.tempWidth + element.marginRight;
            element.left = (int) (center - (width / 2.0f));
            element.right = (int) (center + (width / 2.0f));
            element.left += element.marginLeft;
            element.right -= element.marginRight;
            BaseElement baseElement = element.element;
            if (baseElement != 0) {
                baseElement.setAlignment(baseElement.getAlignment() | 1);
                baseElement.setOffsetX(element.left);
                if (baseElement instanceof Sizable) {
                    ((Sizable) baseElement).setWidth(element.width());
                }
                this.mPage.addElement(baseElement);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void processElementsAlignTop(Container container, int elementFillSpace) throws XmlParserException {
        int y;
        int y2 = container.top + container.paddingTop;
        int maxBottom = container.bottom - container.paddingBottom;
        for (Element element : container.elements) {
            int y3 = element.tempOffY + y2 + element.marginTop;
            element.top = y3;
            if (element.tempHeight == -2) {
                y = y3 + elementFillSpace;
            } else {
                y = y3 + element.tempHeight;
            }
            element.bottom = y;
            y2 = y + element.marginBottom;
            if (element.top > maxBottom) {
                element.top = maxBottom;
            }
            if (element.bottom > maxBottom) {
                element.bottom = maxBottom;
            }
            BaseElement baseElement = element.element;
            if (baseElement != 0) {
                baseElement.setAlignment(baseElement.getAlignment() | 4);
                baseElement.setOffsetY(element.top);
                if (baseElement instanceof Sizable) {
                    ((Sizable) baseElement).setHeight(element.height());
                }
                this.mPage.addElement(baseElement);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void processElementsAlignBottom(Container container, int elementFillSpace) throws XmlParserException {
        int y;
        int y2 = container.bottom - container.paddingBottom;
        int maxTop = container.top + container.paddingTop;
        for (Element element : container.elements) {
            int y3 = (y2 - element.tempOffY) - element.marginBottom;
            element.bottom = y3;
            if (element.tempHeight == -2) {
                y = y3 - elementFillSpace;
            } else {
                y = y3 - element.tempHeight;
            }
            element.top = y;
            y2 = y - element.marginTop;
            if (element.top < maxTop) {
                element.top = maxTop;
            }
            if (element.bottom < maxTop) {
                element.bottom = maxTop;
            }
            BaseElement baseElement = element.element;
            if (baseElement != 0) {
                baseElement.setAlignment(baseElement.getAlignment() | 8);
                baseElement.setOffsetY(this.mParser.getScreenHeight() - element.bottom);
                if (baseElement instanceof Sizable) {
                    ((Sizable) baseElement).setHeight(element.height());
                }
                this.mPage.addElement(baseElement);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void processElementsAlignVCenter(Container container) throws XmlParserException {
        int maxHeight = (container.height() - container.paddingTop) - container.paddingBottom;
        int center = container.top + container.paddingTop + (maxHeight / 2);
        for (Element element : container.elements) {
            if (element.tempHeight == -2) {
                element.tempHeight = (maxHeight - element.marginTop) - element.marginBottom;
            }
            int height = element.marginTop + element.tempHeight + element.marginBottom;
            element.top = (int) (center - (height / 2.0f));
            element.bottom = (int) (center + (height / 2.0f));
            element.top += element.marginTop;
            element.bottom -= element.marginBottom;
            BaseElement baseElement = element.element;
            if (baseElement != 0) {
                baseElement.setOffsetY(element.top);
                baseElement.setAlignment(baseElement.getAlignment() | 4);
                if (baseElement instanceof Sizable) {
                    ((Sizable) baseElement).setHeight(element.height());
                }
                this.mPage.addElement(baseElement);
            }
        }
    }

    public static String printAlignment(BaseElement element) {
        StringBuilder sb = new StringBuilder("(");
        int align = element.getAlignment();
        boolean hasAlign = false;
        if ((align & 1) == 1) {
            sb.append("left");
            hasAlign = true;
        }
        if ((align & 4) == 4) {
            sb.append(hasAlign ? "|top" : PageHelper.ALIGNMENT_TOP);
            hasAlign = true;
        }
        if ((align & 2) == 2) {
            sb.append(hasAlign ? "|right" : "right");
            hasAlign = true;
        }
        if ((align & 8) == 8) {
            sb.append(hasAlign ? "|bottom" : PageHelper.ALIGNMENT_BOTTOM);
            hasAlign = true;
        }
        sb.append(hasAlign ? ")" : "none)");
        return sb.toString();
    }

    public static HorizontalAlign getHorizontalAlign(int align) {
        if ((align & 1) == 1) {
            return HorizontalAlign.LEFT;
        }
        if ((align & 2) == 2) {
            return HorizontalAlign.RIGHT;
        }
        return HorizontalAlign.CENTER;
    }

    public static VerticalAlign getVerticalAlign(int align) {
        if ((align & 4) == 4) {
            return VerticalAlign.TOP;
        }
        if ((align & 8) == 8) {
            return VerticalAlign.BOTTOM;
        }
        return VerticalAlign.CENTER;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String repeatString(String string, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(string);
        }
        return sb.toString();
    }
}
