package com.kopin.pupil.ui;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.util.Log;
import com.kopin.accessory.ImageCodec;
import com.kopin.pupil.VCContext;
import com.kopin.pupil.exception.XmlParserException;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.PageHelper;
import com.kopin.pupil.ui.PageParserHelper;
import com.kopin.pupil.ui.elements.BaseElement;
import com.kopin.pupil.ui.elements.BoxElement;
import com.kopin.pupil.ui.elements.ImageElement;
import com.kopin.pupil.ui.elements.ProgressElement;
import com.kopin.pupil.ui.elements.RelativeElement;
import com.kopin.pupil.ui.elements.TextElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes25.dex */
public class PageParser {
    public static final String ACTIVE_COLOR_ATTRIBUTE = "active_color";
    public static final String ACTIVE_COLOUR_ATTRIBUTE = "active_colour";
    public static final String ALIGN_ATTRIBUTE = "align";
    public static final String ALIGN_CENTER_H = "center_horizontal";
    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_RIGHT = "right";
    public static final String BACKCOLOR_ATTRIBUTE = "backcolor";
    public static final String BOLD_ATTRIBUTE = "bold";
    public static final String BOX_TAG = "box";
    public static final String COLOR_ATTRIBUTE = "color";
    public static final String COLOUR_ATTRIBUTE = "colour";
    public static final String COMPRESSED_ATTRIBUTE = "compression";
    public static final String CONTENT_TEXT_PART_ATTRIBUTE = "content";
    public static final boolean DEBUG = false;
    public static final String HEIGHT_ATTRIBUTE = "height";
    static final String ICON_HEIGHT = "iconHeight";
    static final String ICON_NAME = "iconName";
    static final String ICON_WIDTH = "iconWidth";
    public static final String ID_ATTRIBUTE = "name";
    public static final String IMAGE_TAG = "image";
    public static final String INACTIVE_COLOR_ATTRIBUTE = "inactive_color";
    public static final String INACTIVE_COLOUR_ATTRIBUTE = "inactive_colour";
    public static final String INCLUDE_TAG = "include";
    public static final String ITALIC_ATTRIBUTE = "italic";
    public static final String MARGIN = "margin";
    public static final String MARGIN_BOTTOM = "marginBottom";
    public static final String MARGIN_LEFT = "marginLeft";
    public static final String MARGIN_RIGHT = "marginRight";
    public static final String MARGIN_TOP = "marginTop";
    public static final String OFFSET_X_ATTRIBUTE = "offset_x";
    public static final String OFFSET_Y_ATTRIBUTE = "offset_y";
    public static final String PADDING = "padding";
    public static final String PADDING_BOTTOM = "paddingBottom";
    public static final String PADDING_LEFT = "paddingLeft";
    public static final String PADDING_RIGHT = "paddingRight";
    public static final String PADDING_TOP = "paddingTop";
    public static final String PAGE_TAG = "page";
    public static final String PAGING_ATTRIBUTE = "paging";
    public static final String PROGRESS_BAR_PROGRESS_ATTRIBUTE = "progress";
    public static final String PROGRESS_BAR_TAG = "progressbar";
    public static final String RECT_TAG = "rectangle";
    public static final String RIGHT_OF_ATTRIBUTE = "rightof";
    public static final String SIZE_ATTRIBUTE = "size";
    public static final String SOURCE_ATTRIBUTE = "source";
    public static final String SPACE_TAG = "space";
    private static final String TAG = "PageParser";
    public static final String TEXT_ALIGN = "text_align";
    public static final String TEXT_AREA_TAG = "textarea";
    public static final String TEXT_ELLIPSE_ATTRIBUTE = "ellipse";
    public static final String TEXT_PAGING_ATTRIBUTE = "paging";
    public static final String TEXT_PAGING_CURRENT = "currentview";
    public static final String TEXT_PAGING_TOTAL = "totalview";
    public static final String TEXT_SINGLE_LINE_ATTRIBUTE = "singleline";
    public static final String THIN_ATTRIBUTE = "thin";
    private static final String VERSION = "version";
    public static final String WIDTH_ATTRIBUTE = "width";
    private LinkedList<PageParserHelper.Element> mHistory;
    private ApplicationPage mPage;
    private VCContext mParentState;
    private PageHelper.IResolver mResolver;
    private Theme mTheme;
    private XmlPullParser mXmlPage;
    private PageParserHelper parserHelper;
    private PageParserHelper.Container mContainer = null;
    private PageParserHelper.Container rootContainer = null;

    public PageParser(VCContext parentState, Theme theme, PageHelper.IResolver resolver) {
        this.mParentState = parentState;
        this.mTheme = theme;
        this.mResolver = resolver;
    }

    public ApplicationPage parse(XmlPullParser xmlPage, Context context) throws XmlPullParserException, XmlParserException, IOException {
        this.mXmlPage = xmlPage;
        this.mPage = null;
        this.mHistory = new LinkedList<>();
        this.parserHelper = new PageParserHelper(this);
        int eventType = xmlPage.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    switch (xmlPage.getName()) {
                        case "box":
                        case "rectangle":
                            createBoxElement(xmlPage);
                            break;
                        case "image":
                            createImageElement(xmlPage);
                            break;
                        case "progressbar":
                            createProgressElement();
                            break;
                        case "page":
                            XmlParserException.ParserExtraInfo extra = new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber());
                            if (this.mContainer != null) {
                                throw new XmlParserException("Invalid page tag inside another container.", extra);
                            }
                            Map<String, String> attributes = getAttributes(xmlPage);
                            this.mPage = new ApplicationPage(this.mParentState, this.mTheme);
                            this.mContainer = new PageParserHelper.Container("page", extra);
                            processContainerAttributes(this.mContainer, attributes, extra);
                            populatePage(attributes, extra);
                            this.rootContainer = this.mContainer;
                            break;
                            break;
                        case "textarea":
                            createMultiLineTextElement(xmlPage);
                            break;
                        case "space":
                            createSpace(xmlPage);
                            break;
                        case "include":
                            parseInclude(context, xmlPage);
                            this.mContainer = this.rootContainer;
                            break;
                        default:
                            throw new XmlParserException("Unknown Tag '" + xmlPage.getName() + "'", new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber()));
                    }
                    break;
                case 3:
                    goUp();
                    break;
                case 4:
                    String message = "There should never be plain text in a layout file unless it is in an attribute.<" + this.mXmlPage.getText() + ">";
                    throw new XmlParserException(message, new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber()));
            }
            eventType = xmlPage.next();
        }
        calculateBounds();
        getPage().configure();
        return getPage();
    }

    private void parseInclude(Context context, XmlPullParser xmlPage) throws XmlPullParserException, XmlParserException, IOException {
        String xmlid = xmlPage.getAttributeValue(null, "source");
        int xmlIncludedId = context.getResources().getIdentifier(xmlid, "xml", context.getPackageName());
        if (xmlIncludedId != 0) {
            XmlResourceParser xmlResourceParser = context.getResources().getXml(xmlIncludedId);
            for (int tag = xmlResourceParser.getEventType(); tag != 1; tag = xmlResourceParser.next()) {
                if (tag == 2) {
                    switch (xmlResourceParser.getName()) {
                        case "box":
                        case "rectangle":
                            createBoxElement(xmlResourceParser);
                            break;
                        case "image":
                            createImageElement(xmlResourceParser);
                            break;
                        case "progressbar":
                            createProgressElement();
                            break;
                        case "textarea":
                            createMultiLineTextElement(xmlResourceParser);
                            break;
                        case "space":
                            createSpace(xmlResourceParser);
                            break;
                        default:
                            Log.e(TAG, "other tag");
                            break;
                    }
                }
            }
        }
    }

    private void populateGenericAttributes(BaseElement element, Map<String, String> attributes, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (attributes.containsKey("name")) {
            element.setName(attributes.get("name"));
        }
        if (attributes.containsKey("color")) {
            element.setColor(PageHelper.parseColor(attributes.get("color"), this.mTheme, this.mResolver, extra));
        }
        if (attributes.containsKey(COLOUR_ATTRIBUTE)) {
            element.setColor(PageHelper.parseColor(attributes.get(COLOUR_ATTRIBUTE), this.mTheme, this.mResolver, extra));
        }
        if (attributes.containsKey(ACTIVE_COLOR_ATTRIBUTE)) {
            element.setActiveColor(PageHelper.parseColor(attributes.get(ACTIVE_COLOR_ATTRIBUTE), this.mTheme, this.mResolver, extra));
        }
        if (attributes.containsKey(ACTIVE_COLOUR_ATTRIBUTE)) {
            element.setActiveColor(PageHelper.parseColor(attributes.get(ACTIVE_COLOUR_ATTRIBUTE), this.mTheme, this.mResolver, extra));
        }
        if (attributes.containsKey(INACTIVE_COLOR_ATTRIBUTE)) {
            element.setInActiveColor(PageHelper.parseColor(attributes.get(INACTIVE_COLOR_ATTRIBUTE), this.mTheme, this.mResolver, extra));
        }
        if (attributes.containsKey(INACTIVE_COLOUR_ATTRIBUTE)) {
            element.setInActiveColor(PageHelper.parseColor(attributes.get(INACTIVE_COLOUR_ATTRIBUTE), this.mTheme, this.mResolver, extra));
        }
        if (attributes.containsKey("rightof")) {
            element.setRelation(attributes.get("rightof"), RelativeElement.RelativePosition.RIGHT_OF);
        }
    }

    private void populateSizableAttributes(PageParserHelper.Element element, Map<String, String> attributes, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (attributes.containsKey("offset_x")) {
            element.offsetX = new PageParserHelper.Attribute(attributes.get("offset_x"), extra);
        }
        if (attributes.containsKey("offset_y")) {
            element.offsetY = new PageParserHelper.Attribute(attributes.get("offset_y"), extra);
        }
        if (attributes.containsKey("width")) {
            element.width = new PageParserHelper.Attribute(attributes.get("width"), extra);
        }
        if (attributes.containsKey("height")) {
            element.height = new PageParserHelper.Attribute(attributes.get("height"), extra);
        }
        if (element.width == null) {
            element.width = new PageParserHelper.Attribute(null, extra);
        }
        if (element.height == null) {
            element.height = new PageParserHelper.Attribute(null, extra);
        }
        if (attributes.containsKey(MARGIN_LEFT)) {
            element.marginLeft = PageHelper.parseInt(attributes.get(MARGIN_LEFT), this.mResolver, extra);
        }
        if (attributes.containsKey(MARGIN_TOP)) {
            element.marginTop = PageHelper.parseInt(attributes.get(MARGIN_TOP), this.mResolver, extra);
        }
        if (attributes.containsKey(MARGIN_RIGHT)) {
            element.marginRight = PageHelper.parseInt(attributes.get(MARGIN_RIGHT), this.mResolver, extra);
        }
        if (attributes.containsKey(MARGIN_BOTTOM)) {
            element.marginBottom = PageHelper.parseInt(attributes.get(MARGIN_BOTTOM), this.mResolver, extra);
        }
        if (attributes.containsKey(MARGIN)) {
            int padding = PageHelper.parseInt(attributes.get(MARGIN), this.mResolver, extra);
            element.marginLeft = padding;
            element.marginTop = padding;
            element.marginRight = padding;
            element.marginBottom = padding;
        }
    }

    private void createBoxElement(XmlPullParser xmlPage) throws XmlParserException {
        Map<String, String> attributes = getAttributes(xmlPage);
        XmlParserException.ParserExtraInfo extra = new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber());
        PageParserHelper.Container boxContainer = new PageParserHelper.Container(BOX_TAG, extra);
        BaseElement box = new BoxElement();
        box.styleFromTheme(this.mTheme);
        box.setBackColor(0);
        boxContainer.element = box;
        populateGenericAttributes(box, attributes, extra);
        populateSizableAttributes(boxContainer, attributes, extra);
        addContainer(boxContainer, attributes, extra);
        checkAttributes(attributes, extra);
    }

    private void createImageElement(XmlPullParser xmlPage) throws XmlParserException {
        Map<String, String> attributes = getAttributes(xmlPage);
        XmlParserException.ParserExtraInfo extra = new XmlParserException.ParserExtraInfo(this.mXmlPage.getLineNumber());
        PageParserHelper.Element element = new PageParserHelper.Element("image", extra);
        ImageElement image = new ImageElement();
        image.styleFromTheme(this.mTheme);
        element.element = image;
        populateGenericAttributes(image, attributes, extra);
        populateSizableAttributes(element, attributes, extra);
        String source = attributes.get("source");
        if (source == null || source.trim().isEmpty()) {
            throw new XmlParserException("Invalid source attribute for the image tag.", extra);
        }
        attributes.remove("source");
        image.setSource(source);
        addLeaf(element, extra);
        checkAttributes(attributes, extra);
    }

    private void createProgressElement() throws XmlParserException {
        Map<String, String> attributes = getAttributes(this.mXmlPage);
        XmlParserException.ParserExtraInfo extra = new XmlParserException.ParserExtraInfo(this.mXmlPage.getLineNumber());
        PageParserHelper.Element element = new PageParserHelper.Element("progressbar", extra);
        ProgressElement progressBar = new ProgressElement();
        progressBar.styleFromTheme(this.mTheme);
        element.element = progressBar;
        populateGenericAttributes(progressBar, attributes, extra);
        populateSizableAttributes(element, attributes, extra);
        if (attributes.containsKey("progress")) {
            progressBar.setProgress(PageHelper.parseInt(attributes.get("progress"), this.mResolver, extra));
            attributes.remove("progress");
        }
        addLeaf(element, extra);
        checkAttributes(attributes, extra);
    }

    private void populatePage(Map<String, String> attributes, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        String compression;
        if (attributes.containsKey("name")) {
            this.mPage.setPageName(attributes.get("name"));
            this.mPage.setTemplate(attributes.get("name"));
        }
        if (attributes.containsKey("backcolor")) {
            String backColor = attributes.get("backcolor");
            int color = PageHelper.parseColor(backColor, this.mTheme, this.mResolver, extra);
            this.mPage.setBackgroundColor(color);
        }
        if (attributes.containsKey("paging")) {
            boolean paging = PageHelper.parseBoolean(attributes.get("paging"), extra);
            this.mPage.setPaging(paging);
        }
        if (attributes.containsKey("compression") && (compression = attributes.get("compression")) != null && !compression.isEmpty()) {
            if (compression.equalsIgnoreCase("rle")) {
                this.mPage.setCompression(ImageCodec.RLE565);
            } else if (compression.equalsIgnoreCase("raw")) {
                this.mPage.setCompression(ImageCodec.RGB565);
            } else if (compression.equalsIgnoreCase("jpeg")) {
                this.mPage.setCompression(ImageCodec.JPEG);
            } else {
                throw new XmlParserException("Invalid compression: " + compression, extra);
            }
        }
        if (attributes.containsKey("version")) {
            int version = parseInt(attributes.get("version"), extra);
            if (version != 2) {
                throw new XmlParserException("Invalid version! (" + version + ")", extra);
            }
            if (this.mPage.getPageName().isEmpty()) {
                throw new XmlParserException("Page name can't be empty!", extra);
            }
            checkAttributes(attributes, extra);
            return;
        }
        throw new XmlParserException("Invalid xml file (does not contain version number)", extra);
    }

    private void createMultiLineTextElement(XmlPullParser xmlPage) throws XmlParserException {
        Map<String, String> attributes = getAttributes(xmlPage);
        XmlParserException.ParserExtraInfo extra = new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber());
        PageParserHelper.Element element = new PageParserHelper.Element("textarea", extra);
        TextElement textElement = new TextElement();
        textElement.styleFromTheme(this.mTheme);
        textElement.setColor(this.mTheme.getColor("default_font").intValue());
        textElement.setSize(this.mTheme.getTextSize(Theme.TEXT_SIZE_DEFAULT).intValue());
        element.element = textElement;
        populateGenericAttributes(textElement, attributes, extra);
        populateSizableAttributes(element, attributes, extra);
        String source = attributes.get("source");
        attributes.remove("source");
        String iconName = attributes.get(ICON_NAME);
        attributes.remove(ICON_NAME);
        String iconWidth = attributes.get(ICON_WIDTH);
        attributes.remove(ICON_WIDTH);
        String iconHeight = attributes.get(ICON_HEIGHT);
        attributes.remove(ICON_HEIGHT);
        if (source != null && iconName != null && iconWidth != null && iconHeight != null) {
            textElement.setIcon(iconName, source, iconWidth, iconHeight);
        }
        if (attributes.containsKey("size")) {
            textElement.setSize(PageHelper.parseSize(attributes.get("size"), this.mTheme, this.mResolver, extra));
            attributes.remove("size");
        }
        if (attributes.containsKey("content")) {
            String text = PageHelper.parseContentText(attributes.get("content"), this.mResolver, extra);
            textElement.setText(text);
            attributes.remove("content");
        }
        if (attributes.containsKey("italic")) {
            boolean isItalic = PageHelper.parseBoolean(attributes.get("italic"), extra);
            if (isItalic) {
                textElement.setStyle(textElement.getStyle() | 2);
            }
            attributes.remove("italic");
        }
        if (attributes.containsKey("bold")) {
            boolean isItalic2 = PageHelper.parseBoolean(attributes.get("bold"), extra);
            if (isItalic2) {
                textElement.setStyle(textElement.getStyle() | 1);
            }
            attributes.remove("bold");
        }
        if (attributes.containsKey("thin")) {
            boolean isThin = PageHelper.parseBoolean(attributes.get("thin"), extra);
            if (isThin) {
                textElement.setStyle(textElement.getStyle() | 4);
            }
            attributes.remove("thin");
        }
        if (attributes.containsKey("ellipse")) {
            boolean ellipse = PageHelper.parseBoolean(attributes.get("ellipse"), extra);
            textElement.setEllipse(ellipse);
            attributes.remove("ellipse");
        }
        if (attributes.containsKey("singleline")) {
            boolean isSingleLine = PageHelper.parseBoolean(attributes.get("singleline"), extra);
            textElement.setSingleLine(isSingleLine);
            attributes.remove("singleline");
        }
        if (attributes.containsKey("paging")) {
            boolean isPaging = PageHelper.parseBoolean(attributes.get("paging"), extra);
            textElement.setPaging(isPaging);
            attributes.remove("paging");
        }
        if (attributes.containsKey("currentview")) {
            String name = attributes.get("currentview");
            textElement.setCurrentPageIdViewId(name);
            attributes.remove("currentview");
        }
        if (attributes.containsKey("totalview")) {
            String name2 = attributes.get("totalview");
            textElement.setTotalPageCounterViewId(name2);
            attributes.remove("totalview");
        }
        if (attributes.containsKey("text_align")) {
            textElement.setHorizontalAlign(parseHorizontalTextAlign(attributes.get("text_align"), extra));
            attributes.remove("text_align");
        }
        if (element.width == null || element.width.value == null) {
            if (!textElement.isSingleLine()) {
                textElement.setSingleLine(true);
            }
            textElement.setIsFit(true);
        }
        addLeaf(element, extra);
        checkAttributes(attributes, extra);
    }

    private void createSpace(XmlPullParser xmlPage) throws XmlParserException {
        Map<String, String> attributes = getAttributes(xmlPage);
        XmlParserException.ParserExtraInfo extra = new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber());
        PageParserHelper.Element element = new PageParserHelper.Element(SPACE_TAG, extra);
        populateSizableAttributes(element, attributes, extra);
        addLeaf(element, extra);
        checkAttributes(attributes, extra);
    }

    private ApplicationPage getPage() throws XmlParserException {
        if (this.mPage == null) {
            throw new XmlParserException("Invalid tag! All elements should be contained in a page tag.", new XmlParserException.ParserExtraInfo(this.mXmlPage.getLineNumber()));
        }
        return this.mPage;
    }

    private void checkAttributes(Map<String, String> attributes, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (!attributes.isEmpty()) {
            boolean comma = false;
            StringBuilder sb = new StringBuilder("Invalid attributes: ");
            for (String key : attributes.keySet()) {
                if (comma) {
                    sb.append(", ");
                }
                sb.append(key);
                comma = true;
            }
        }
    }

    private void calculateBounds() throws XmlParserException {
        Rect bounds = this.mParentState.getBounds();
        this.mContainer.left = 0;
        this.mContainer.top = 0;
        this.mContainer.right = bounds.width();
        this.mContainer.bottom = bounds.height();
        this.parserHelper.calculate(getPage(), this.mContainer);
    }

    private void addContainer(PageParserHelper.Container container, Map<String, String> attributes, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (this.mContainer == null) {
            throw new XmlParserException("Invalid top container!", extra);
        }
        container.parent = this.mContainer;
        this.mContainer.elements.add(container);
        this.mContainer = container;
        this.mHistory.push(container);
        processContainerAttributes(container, attributes, extra);
    }

    private void processContainerAttributes(PageParserHelper.Container container, Map<String, String> attributes, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (attributes.containsKey("align")) {
            container.alignment = PageHelper.parseAlignment(attributes.get("align"), extra);
        }
        if (attributes.containsKey(PADDING_LEFT)) {
            container.paddingLeft = PageHelper.parseInt(attributes.get(PADDING_LEFT), this.mResolver, extra);
        }
        if (attributes.containsKey(PADDING_TOP)) {
            container.paddingTop = PageHelper.parseInt(attributes.get(PADDING_TOP), this.mResolver, extra);
        }
        if (attributes.containsKey(PADDING_RIGHT)) {
            container.paddingRight = PageHelper.parseInt(attributes.get(PADDING_RIGHT), this.mResolver, extra);
        }
        if (attributes.containsKey(PADDING_BOTTOM)) {
            container.paddingBottom = PageHelper.parseInt(attributes.get(PADDING_BOTTOM), this.mResolver, extra);
        }
        if (attributes.containsKey(PADDING)) {
            int padding = PageHelper.parseInt(attributes.get(PADDING), this.mResolver, extra);
            container.paddingLeft = padding;
            container.paddingTop = padding;
            container.paddingRight = padding;
            container.paddingBottom = padding;
        }
    }

    private void addLeaf(PageParserHelper.Element leaf, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (this.mContainer == null) {
            throw new XmlParserException("Leaf element with no container!", extra);
        }
        leaf.parent = this.mContainer;
        this.mContainer.elements.add(leaf);
        this.mHistory.push(leaf);
    }

    private void goUp() {
        if (!this.mHistory.isEmpty()) {
            PageParserHelper.Element element = this.mHistory.poll();
            if ((element instanceof PageParserHelper.Container) && this.mContainer.parent != null) {
                this.mContainer = this.mContainer.parent;
            }
        }
    }

    public static Map<String, String> getAttributes(XmlPullParser parser) throws XmlParserException {
        int count = parser.getAttributeCount();
        if (count != -1) {
            Map<String, String> attributes = new HashMap<>(count);
            for (int x = 0; x < count; x++) {
                attributes.put(parser.getAttributeName(x), parser.getAttributeValue(x));
            }
            return attributes;
        }
        throw new XmlParserException("Invalid tag attributes.", new XmlParserException.ParserExtraInfo(parser.getLineNumber()));
    }

    public int parseInt(String value, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        return PageHelper.parseInt(value, this.mResolver, extra);
    }

    public int getScreenWidth() {
        Rect bounds = this.mParentState.getBounds();
        if (bounds != null) {
            return bounds.width();
        }
        return 428;
    }

    public int getScreenHeight() {
        Rect bounds = this.mParentState.getBounds();
        if (bounds != null) {
            return bounds.height();
        }
        return 240;
    }

    public PageParserHelper.HorizontalAlign parseHorizontalTextAlign(String value, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        switch (value.trim()) {
            case "left":
                return PageParserHelper.HorizontalAlign.LEFT;
            case "right":
                return PageParserHelper.HorizontalAlign.RIGHT;
            case "center_horizontal":
                return PageParserHelper.HorizontalAlign.CENTER;
            default:
                throw new XmlParserException("Invalid alignment: '" + value + "'. Should be one of: left, right, center_horizontal", extra);
        }
    }
}
