package com.kopin.pupil.ui;

import android.graphics.Point;
import android.support.v4.view.ViewCompat;
import com.facebook.internal.ServerProtocol;
import com.kopin.accessory.ImageCodec;
import com.kopin.pupil.VCContext;
import com.kopin.pupil.exception.NotFoundException;
import com.kopin.pupil.exception.XmlParserException;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.PageParserHelper;
import com.kopin.pupil.ui.elements.BaseElement;
import com.kopin.pupil.ui.elements.BoxElement;
import com.kopin.pupil.ui.elements.ImageElement;
import com.kopin.pupil.ui.elements.ProgressElement;
import com.kopin.pupil.ui.elements.RelativeElement;
import com.kopin.pupil.ui.elements.SingleLineTextElement;
import com.kopin.pupil.ui.elements.TextElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes25.dex */
public class PageHelper {
    public static final String ALIGNMENT_BOTTOM = "bottom";
    public static final String ALIGNMENT_LEFT = "left";
    public static final String ALIGNMENT_RIGHT = "right";
    public static final String ALIGNMENT_TOP = "top";
    public static final String ALIGN_ATTRIBUTE = "align";
    public static final String ALIGN_CENTER_H = "center_horizontal";
    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_RIGHT = "right";
    public static final String BACKCOLOR_ATTRIBUTE = "backcolor";
    public static final String BOLD_ATTRIBUTE = "bold";
    public static final String BOX_TAG = "rectangle";
    public static final String COLOR_ATTRIBUTE = "color";
    public static final String COMPRESSED_ATTRIBUTE = "compression";
    public static final String CONTENT_TEXT_PART_ATTRIBUTE = "content";
    private static final boolean DEBUG = false;
    public static final String DIV_LEADING = "leading";
    public static final String HEIGHT_ATTRIBUTE = "height";
    public static final String ID_ATTRIBUTE = "name";
    public static final String IMAGE_TAG = "image";
    public static final String ITALIC_ATTRIBUTE = "italic";
    public static final String OFFSET_ATTRIBUTE = "offset";
    public static final String OFFSET_X_ATTRIBUTE = "offset_x";
    public static final String OFFSET_Y_ATTRIBUTE = "offset_y";
    public static final String PAGE_TAG = "page";
    public static final String PAGING_ATTRIBUTE = "paging";
    public static final String PROGRESS_BAR_PROGRESS_ATTRIBUTE = "progress";
    public static final String PROGRESS_BAR_TAG = "progressbar";
    public static final String RIGHT_OF_ATTRIBUTE = "rightof";
    public static final String SIZE_ATTRIBUTE = "size";
    public static final String SOURCE_ATTRIBUTE = "source";
    private static final String TAG = "PageHelper";
    public static final String TEXT_ALIGN = "text_align";
    public static final String TEXT_AREA_TAG = "textarea";
    public static final String TEXT_ELEMENT_TAG = "div";
    public static final String TEXT_ELLIPSE_ATTRIBUTE = "ellipse";
    public static final String TEXT_LINE_BREAK = "br";
    public static final String TEXT_PAGING_ATTRIBUTE = "paging";
    public static final String TEXT_PAGING_CURRENT = "currentview";
    public static final String TEXT_PAGING_TOTAL = "totalview";
    public static final String TEXT_PART_TAG = "text";
    public static final String TEXT_SINGLE_LINE_ATTRIBUTE = "singleline";
    public static final String THIN_ATTRIBUTE = "thin";
    public static final String WIDTH_ATTRIBUTE = "width";

    public interface IResolver {
        int resolveColour(String str) throws NotFoundException;

        float resolveDimen(String str) throws NotFoundException;

        String resolveString(String str) throws NotFoundException;
    }

    public static ApplicationPage createPageFromXml(XmlPullParser xmlPage, VCContext parentState, Theme theme, IResolver resolver) throws XmlPullParserException, XmlParserException, IOException {
        String text;
        int eventType = xmlPage.getEventType();
        ApplicationPage page = new ApplicationPage(parentState, theme);
        while (eventType != 1) {
            if (eventType != 0 && eventType != 1 && eventType == 2) {
                if (xmlPage.getName().equalsIgnoreCase(TEXT_ELEMENT_TAG)) {
                    SingleLineTextElement textElement = createTextElement(xmlPage, eventType, theme, resolver);
                    page.addElement(textElement);
                } else if (xmlPage.getName().equalsIgnoreCase("rectangle")) {
                    XmlParserException.ParserExtraInfo extra = new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber());
                    BoxElement box = createBoxElement(parentState, getAttributes(xmlPage), theme, resolver, extra);
                    page.addElement(box);
                } else if (xmlPage.getName().equalsIgnoreCase("image")) {
                    XmlParserException.ParserExtraInfo extra2 = new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber());
                    ImageElement bitmap = createImageElement(getAttributes(xmlPage), theme, resolver, extra2);
                    page.addElement(bitmap);
                } else if (xmlPage.getName().equalsIgnoreCase("progressbar")) {
                    XmlParserException.ParserExtraInfo extra3 = new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber());
                    ProgressElement progressBar = createProgressElement(getAttributes(xmlPage), theme, resolver, extra3);
                    page.addElement(progressBar);
                } else if (xmlPage.getName().equalsIgnoreCase("page")) {
                    Map<String, String> attributes = getAttributes(xmlPage);
                    populatePageAttributes(page, attributes, theme, resolver, new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber()));
                } else if (xmlPage.getName().equalsIgnoreCase("textarea")) {
                    XmlParserException.ParserExtraInfo extra4 = new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber());
                    TextElement mlTextElement = createMultiLineTextElement(getAttributes(xmlPage), theme, resolver, extra4);
                    if (!xmlPage.isEmptyElementTag() && (text = xmlPage.nextText()) != null && text.length() > 0) {
                        mlTextElement.setText(text);
                    }
                    page.addElement(mlTextElement);
                } else if (eventType != 3) {
                    if (eventType == 4) {
                        throw new XmlParserException("There should never be plain text in a layout file unless it is in an attribute.", new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber()));
                    }
                    throw new XmlParserException("Unknown Tag '" + xmlPage.getName() + "'", new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber()));
                }
            }
            eventType = xmlPage.next();
        }
        page.configure();
        return page;
    }

    private static void populatePageAttributes(ApplicationPage page, Map<String, String> attributes, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        String compression;
        if (attributes.containsKey("name")) {
            page.setPageName(attributes.get("name"));
            page.setTemplate(attributes.get("name"));
        }
        if (attributes.containsKey("backcolor")) {
            String backColor = attributes.get("backcolor");
            int color = parseColor(backColor, theme, resolver, extra);
            page.setBackgroundColor(color);
        }
        if (attributes.containsKey("paging")) {
            boolean paging = parseBoolean(attributes.get("paging"), extra);
            page.setPaging(paging);
        }
        if (attributes.containsKey("compression") && (compression = attributes.get("compression")) != null && compression.length() > 0) {
            ImageCodec comp = ImageCodec.RLE565;
            if (compression.equalsIgnoreCase("rle")) {
                comp = ImageCodec.RLE565;
            } else if (compression.equalsIgnoreCase("raw")) {
                comp = ImageCodec.RGB565;
            } else if (compression.equalsIgnoreCase("jpeg")) {
                comp = ImageCodec.JPEG;
            }
            page.setCompression(comp);
        }
        if (page.getPageName().isEmpty()) {
            throw new XmlParserException("Page name can't be empty!", extra);
        }
    }

    private static TextElement createMultiLineTextElement(Map<String, String> attributes, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        TextElement textElement = new TextElement();
        textElement.styleFromTheme(theme);
        textElement.setColor(theme.getColor("default_font").intValue());
        textElement.setSize(theme.getTextSize(Theme.TEXT_SIZE_DEFAULT).intValue());
        populateMultiLineTextElementAttributes(textElement, attributes, theme, resolver, extra);
        return textElement;
    }

    private static void populateMultiLineTextElementAttributes(TextElement textElement, Map<String, String> attributes, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        populateGenericAttributes(textElement, attributes, resolver, extra);
        if (attributes.containsKey("width")) {
            textElement.setWrapWidth(parseInt(attributes.get("width"), resolver, extra));
        }
        if (attributes.containsKey("height")) {
            textElement.setWrapHeight(parseInt(attributes.get("height"), resolver, extra));
        }
        if (attributes.containsKey("size")) {
            textElement.setSize(parseSize(attributes.get("size"), theme, resolver, extra));
        }
        if (attributes.containsKey("content")) {
            String text = parseContentText(attributes.get("content"), resolver, extra);
            textElement.setText(text);
        }
        if (attributes.containsKey("color")) {
            textElement.setColor(parseColor(attributes.get("color"), theme, resolver, extra));
        }
        if (attributes.containsKey("italic")) {
            boolean isItalic = parseBoolean(attributes.get("italic"), extra);
            if (isItalic) {
                textElement.setStyle(textElement.getStyle() | 2);
            }
        }
        if (attributes.containsKey("bold")) {
            boolean isItalic2 = parseBoolean(attributes.get("bold"), extra);
            if (isItalic2) {
                textElement.setStyle(textElement.getStyle() | 1);
            }
        }
        if (attributes.containsKey("thin")) {
            boolean isThin = parseBoolean(attributes.get("thin"), extra);
            if (isThin) {
                textElement.setStyle(textElement.getStyle() | 4);
            }
        }
        if (attributes.containsKey("ellipse")) {
            boolean ellipse = parseBoolean(attributes.get("ellipse"), extra);
            textElement.setEllipse(ellipse);
        }
        if (attributes.containsKey("singleline")) {
            boolean isSingleLine = parseBoolean(attributes.get("singleline"), extra);
            textElement.setSingleLine(isSingleLine);
        }
        if (attributes.containsKey("text_align")) {
            textElement.setHorizontalAlign(parseHorizontalTextAlign(attributes.get("text_align"), extra));
        }
        if (attributes.containsKey("paging")) {
            boolean isPaging = parseBoolean(attributes.get("paging"), extra);
            textElement.setPaging(isPaging);
        }
        if (attributes.containsKey("currentview")) {
            String name = attributes.get("currentview");
            textElement.setCurrentPageIdViewId(name);
        }
        if (attributes.containsKey("totalview")) {
            String name2 = attributes.get("totalview");
            textElement.setTotalPageCounterViewId(name2);
        }
    }

    private static BoxElement createBoxElement(VCContext parentState, Map<String, String> attributes, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        BoxElement box = new BoxElement();
        box.styleFromTheme(theme);
        populateGenericAttributes(box, attributes, resolver, extra);
        if (attributes.containsKey("width")) {
            String widthContent = attributes.get("width").trim();
            if (widthContent.equals("*")) {
                box.setWidth(parentState.getBounds().width());
            } else {
                box.setWidth(parseInt(widthContent, resolver, extra));
            }
        }
        if (attributes.containsKey("height")) {
            String heightContent = attributes.get("height").trim();
            if (heightContent.equals("*")) {
                box.setHeight(parentState.getBounds().height());
            } else {
                box.setHeight(parseInt(heightContent, resolver, extra));
            }
        }
        if (attributes.containsKey("color")) {
            box.setColor(parseColor(attributes.get("color"), theme, resolver, extra));
        }
        return box;
    }

    private static ProgressElement createProgressElement(Map<String, String> attributes, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        ProgressElement progressBar = new ProgressElement();
        progressBar.styleFromTheme(theme);
        populateGenericAttributes(progressBar, attributes, resolver, extra);
        if (attributes.containsKey("color")) {
            progressBar.setColor(parseColor(attributes.get("color"), theme, resolver, extra));
        }
        if (attributes.containsKey("width")) {
            progressBar.setWidth(parseInt(attributes.get("width"), resolver, extra));
        } else {
            progressBar.setWidth(-1);
        }
        if (attributes.containsKey("height")) {
            progressBar.setHeight(parseInt(attributes.get("height"), resolver, extra));
        } else {
            progressBar.setHeight(3);
        }
        if (attributes.containsKey("progress")) {
            progressBar.setProgress(parseInt(attributes.get("progress"), resolver, extra));
        } else {
            progressBar.setProgress(0);
        }
        return progressBar;
    }

    private static ImageElement createImageElement(Map<String, String> attributes, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        ImageElement image = new ImageElement();
        image.styleFromTheme(theme);
        populateGenericAttributes(image, attributes, resolver, extra);
        if (attributes.containsKey("color")) {
            image.setColor(parseColor(attributes.get("color"), theme, resolver, extra));
        }
        if (attributes.containsKey("width")) {
            image.setWidth(parseInt(attributes.get("width"), resolver, extra));
        } else {
            image.setWidth(-1);
        }
        if (attributes.containsKey("height")) {
            image.setHeight(parseInt(attributes.get("height"), resolver, extra));
        } else {
            image.setHeight(-1);
        }
        String source = attributes.get("source");
        if (source == null || source.trim().isEmpty()) {
            throw new XmlParserException("Invalid source attribute for the image tag.", extra);
        }
        image.setSource(source);
        return image;
    }

    private static SingleLineTextElement createTextElement(XmlPullParser xmlPage, int eventType, Theme theme, IResolver resolver) throws XmlPullParserException, XmlParserException, IOException {
        SingleLineTextElement textElement = new SingleLineTextElement();
        textElement.styleFromTheme(theme);
        Map<String, String> attributes = getAttributes(xmlPage);
        populateSingleLineTextElementAttributes(textElement, attributes, resolver, new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber()));
        while (true) {
            if (eventType != 3 || !xmlPage.getName().equalsIgnoreCase(TEXT_ELEMENT_TAG)) {
                String name = xmlPage.getName();
                if (name == null) {
                    eventType = xmlPage.next();
                } else {
                    if (name.equalsIgnoreCase(TEXT_PART_TAG) && eventType != 3) {
                        SingleLineTextElement.TextPart part = new SingleLineTextElement.TextPart();
                        part.setColor(theme.getColor("default_font").intValue());
                        part.setSize(theme.getTextSize(Theme.TEXT_SIZE_DEFAULT).intValue());
                        Map<String, String> partAttributes = getAttributes(xmlPage);
                        populateTextPartAttributes(part, partAttributes, theme, resolver, new XmlParserException.ParserExtraInfo(xmlPage.getLineNumber()));
                        textElement.addPart(part);
                    } else if (name.equalsIgnoreCase(TEXT_LINE_BREAK) && eventType != 3) {
                        textElement.addPart(new SingleLineTextElement.LineEndPart());
                    }
                    eventType = xmlPage.next();
                }
            } else {
                return textElement;
            }
        }
    }

    private static void populateTextPartAttributes(SingleLineTextElement.TextPart part, Map<String, String> attributes, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (attributes.containsKey("name")) {
            part.setName(attributes.get("name"));
        }
        if (attributes.containsKey("content")) {
            String text = parseContentText(attributes.get("content"), resolver, extra);
            part.setText(text);
        }
        if (attributes.containsKey("color")) {
            part.setColor(parseColor(attributes.get("color"), theme, resolver, extra));
        }
        if (attributes.containsKey("size")) {
            part.setSize(parseSize(attributes.get("size"), theme, resolver, extra));
        }
        if (attributes.containsKey("italic")) {
            boolean isItalic = parseBoolean(attributes.get("italic"), extra);
            if (isItalic) {
                part.setStyle(part.getStyle() | 2);
            }
        }
        if (attributes.containsKey("bold")) {
            boolean isItalic2 = parseBoolean(attributes.get("bold"), extra);
            if (isItalic2) {
                part.setStyle(part.getStyle() | 1);
            }
        }
        if (attributes.containsKey("thin")) {
            boolean isThin = parseBoolean(attributes.get("thin"), extra);
            if (isThin) {
                part.setStyle(part.getStyle() | 4);
            }
        }
    }

    public static String parseContentText(String string, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (string == null) {
            return null;
        }
        if (resolver != null) {
            String tempString = string.trim();
            if (tempString.startsWith("@") && !tempString.startsWith("@@")) {
                try {
                    return resolver.resolveString(tempString);
                } catch (NotFoundException e) {
                    throw new XmlParserException("Could not resolve string: '" + string + "'", extra);
                }
            }
            if (tempString.contains("@@")) {
                return string.replace("@@", "@");
            }
            return string;
        }
        return string;
    }

    public static int parseSize(String sizeString, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extraInfo) throws XmlParserException {
        sizeString.trim();
        char firstChar = sizeString.charAt(0);
        if (!Character.isLetter(firstChar) && firstChar != '_') {
            return parseInt(sizeString, resolver, extraInfo);
        }
        Integer size = theme.getTextSize(sizeString);
        if (size == null) {
            throw new XmlParserException("Could not resolve size: '" + sizeString + "'", extraInfo);
        }
        return size.intValue();
    }

    public static int parseInt(String string, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        try {
            String tempString = string.trim();
            return (resolver == null || !tempString.startsWith("@")) ? Integer.parseInt(string) : (int) resolver.resolveDimen(string);
        } catch (Exception e) {
            throw new XmlParserException("Could not parse int '" + string + "'", extra);
        }
    }

    public static int parseColor(String colorString, Theme theme, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        int i;
        String colorString2 = colorString.trim();
        char firstChar = colorString2.charAt(0);
        if (Character.isLetter(firstChar) || firstChar == '_') {
            Integer color = theme.getColor(colorString2);
            if (color == null) {
                throw new XmlParserException("Could not resolve color: '" + colorString2 + "'", extra);
            }
            return color.intValue();
        }
        if (firstChar == '@') {
            try {
                return resolver.resolveColour(colorString2);
            } catch (NotFoundException e) {
                throw new XmlParserException("Could not resolve color: '" + colorString2 + "'", extra);
            }
        }
        if (firstChar == '#') {
            String colorComponent = colorString2.substring(1);
            try {
                if (colorComponent.length() == 6) {
                    i = Integer.parseInt(colorComponent, 16) | ViewCompat.MEASURED_STATE_MASK;
                } else if (colorComponent.length() == 8) {
                    i = Integer.parseInt(colorComponent, 16);
                }
                return i;
            } catch (Exception e2) {
                throw new XmlParserException("Could not parse color integer: '" + colorString2 + "'", extra);
            }
        }
        throw new XmlParserException("Invalid color string: '" + colorString2 + "'", extra);
    }

    public static boolean parseBoolean(String bool, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equalsIgnoreCase(bool)) {
            return true;
        }
        if ("false".equalsIgnoreCase(bool)) {
            return false;
        }
        throw new XmlParserException("Invalid boolean: '" + bool + "'", extra);
    }

    private static void populateSingleLineTextElementAttributes(SingleLineTextElement textElement, Map<String, String> attributes, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        populateGenericAttributes(textElement, attributes, resolver, extra);
        try {
            if (attributes.containsKey("width")) {
                textElement.setMaxWidth(parseInt(attributes.get("width"), resolver, extra));
            } else {
                textElement.setMaxWidth(-1);
            }
            if (attributes.containsKey(DIV_LEADING)) {
                textElement.setLeading(parseInt(attributes.get(DIV_LEADING), resolver, extra));
            }
        } catch (XmlParserException e) {
            throw e;
        } catch (Exception e2) {
            throw new XmlParserException("Message: " + e2.getLocalizedMessage(), extra);
        }
    }

    private static void populateGenericAttributes(BaseElement element, Map<String, String> attributes, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (attributes.containsKey("name")) {
            element.setName(attributes.get("name").toString());
        }
        if (attributes.containsKey("align")) {
            element.setAlignment(parseAlignment(attributes.get("align"), extra));
        }
        if (attributes.containsKey(OFFSET_ATTRIBUTE)) {
            element.setOffset(parseOffset(attributes.get(OFFSET_ATTRIBUTE), resolver, extra));
        }
        if (attributes.containsKey("offset_x")) {
            element.setOffsetX(parseInt(attributes.get("offset_x"), resolver, extra));
        }
        if (attributes.containsKey("offset_y")) {
            element.setOffsetY(parseInt(attributes.get("offset_y"), resolver, extra));
        }
        if (attributes.containsKey("rightof")) {
            element.setRelation(attributes.get("rightof"), RelativeElement.RelativePosition.RIGHT_OF);
        }
    }

    public static Point parseOffset(String offsetString, IResolver resolver, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (offsetString.isEmpty()) {
            return new Point();
        }
        String[] parts = offsetString.split("\\s+");
        Point offset = new Point();
        if (parts.length == 2) {
            try {
                offset.set(parseInt(parts[0], resolver, extra), parseInt(parts[1], resolver, extra));
                return offset;
            } catch (Exception e) {
                e.printStackTrace();
                return offset;
            }
        }
        throw new XmlParserException("Invalid Offset value, must follow the format 'nn nn'. eg '-13 56'.", extra);
    }

    public static int parseAlignment(String alignmentString, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
        if (alignmentString.isEmpty()) {
            return 0;
        }
        int alignment = 0;
        String[] parts = alignmentString.split("\\|");
        boolean hasHorizontal = false;
        boolean hasVertical = false;
        for (String part : parts) {
            if (part.equalsIgnoreCase("left")) {
                if (hasHorizontal) {
                    throw new XmlParserException("Invalid alignment: '" + alignmentString + "'", extra);
                }
                hasHorizontal = true;
                alignment |= 1;
            } else if (part.equalsIgnoreCase("right")) {
                if (hasHorizontal) {
                    throw new XmlParserException("Invalid alignment: '" + alignmentString + "'", extra);
                }
                hasHorizontal = true;
                alignment |= 2;
            } else if (part.equalsIgnoreCase(ALIGNMENT_TOP)) {
                if (hasVertical) {
                    throw new XmlParserException("Invalid alignment: '" + alignmentString + "'", extra);
                }
                hasVertical = true;
                alignment |= 4;
            } else if (part.equalsIgnoreCase(ALIGNMENT_BOTTOM)) {
                if (hasVertical) {
                    throw new XmlParserException("Invalid alignment: '" + alignmentString + "'", extra);
                }
                hasVertical = true;
                alignment |= 8;
            } else {
                throw new XmlParserException("Invalid alignment: '" + alignmentString + "'", extra);
            }
        }
        return alignment;
    }

    private static PageParserHelper.HorizontalAlign parseHorizontalTextAlign(String value, XmlParserException.ParserExtraInfo extra) throws XmlParserException {
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
}
