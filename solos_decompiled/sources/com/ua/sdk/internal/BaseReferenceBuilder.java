package com.ua.sdk.internal;

import com.ua.sdk.UaLog;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class BaseReferenceBuilder {
    private static final String SELF_KEY = "self";
    private String href;
    private String hrefTemplate;
    private boolean dirty = false;
    private boolean multiGet = false;
    private int expectedLength = 0;
    private boolean didParseTemplateParams = false;
    ArrayList<Param> params = null;
    ArrayList<String> selfParams = null;

    protected BaseReferenceBuilder(String hrefTemplate) {
        hrefTemplate = hrefTemplate == null ? "" : hrefTemplate;
        this.hrefTemplate = hrefTemplate;
        this.expectedLength += hrefTemplate.length();
    }

    protected void addSelfParam(int value) {
        addSelfParam(String.valueOf(value));
    }

    protected void addSelfParam(String value) {
        if (value != null) {
            if (this.params == null) {
                this.params = new ArrayList<>(8);
            }
            if (this.selfParams == null) {
                this.selfParams = new ArrayList<>(8);
            }
            this.dirty = true;
            this.multiGet = true;
            this.selfParams.add(value);
            this.expectedLength += value.length();
        }
    }

    protected void setParam(String key, int value) {
        setParam(key, String.valueOf(value));
    }

    protected void setParam(String key, String value) {
        if (key != null) {
            if (value == null) {
                if (removeParam(key) != null) {
                    this.dirty = true;
                    return;
                }
                return;
            }
            if (this.params == null) {
                this.params = new ArrayList<>(8);
            }
            this.dirty = true;
            Param param = getParam(key);
            if (param == null) {
                this.params.add(new Param(key, value));
                this.expectedLength += key.length() + value.length() + 2;
            } else {
                this.expectedLength += value.length() - param.value.length();
                param.value = value;
            }
        }
    }

    protected void setParams(String key, String... values) {
        if (key != null) {
            if (values == null || values.length == 0) {
                List<Param> param = removeParams(key);
                if (param != null) {
                    this.dirty = true;
                    return;
                }
                return;
            }
            if (this.params == null) {
                this.params = new ArrayList<>(8);
            }
            this.dirty = true;
            for (int i = 0; i < values.length; i++) {
                Param param2 = new Param(key, values[i]);
                this.params.add(param2);
                this.expectedLength += key.length() + values[i].length() + 2;
            }
        }
    }

    protected void parseTemplateParams() {
        if (!this.didParseTemplateParams) {
            this.didParseTemplateParams = true;
            int paramsStart = this.hrefTemplate.indexOf(63);
            if (paramsStart >= 0) {
                String params = this.hrefTemplate.substring(paramsStart);
                this.hrefTemplate = this.hrefTemplate.substring(0, paramsStart);
                this.dirty = true;
                int paramLength = params.length();
                int startIndex = 1;
                while (startIndex < paramLength) {
                    int equalsIndex = params.indexOf(61, startIndex);
                    if (equalsIndex < 0) {
                        throw new IllegalArgumentException(this.hrefTemplate + " is incorrectly formatted.");
                    }
                    String key = params.substring(startIndex, equalsIndex);
                    int ampIndex = params.indexOf(38, startIndex);
                    if (ampIndex < 0) {
                        ampIndex = paramLength;
                    }
                    String value = params.substring(equalsIndex + 1, ampIndex);
                    setParam(key, value);
                    startIndex = ampIndex + 1;
                }
            }
        }
    }

    public String getHref() {
        if (!this.dirty) {
            if (this.href == null) {
                this.href = this.hrefTemplate;
            }
            return this.href;
        }
        if (this.multiGet) {
            this.expectedLength += "self".length();
        }
        StringBuilder out = new StringBuilder(this.expectedLength);
        writeHref(out);
        writeParams(out);
        writeSelfParams(out);
        return out.toString();
    }

    protected Param removeParam(String key) {
        parseTemplateParams();
        if (this.params == null) {
            return null;
        }
        int size = this.params.size();
        for (int i = 0; i < size; i++) {
            Param param = this.params.get(i);
            if (param.key.equals(key)) {
                this.params.remove(i);
                this.expectedLength -= (param.key.length() + param.value.length()) + 2;
                return param;
            }
        }
        return null;
    }

    protected List<Param> removeParams(String key) {
        parseTemplateParams();
        if (this.params == null) {
            return null;
        }
        int size = this.params.size();
        List<Param> removedParams = null;
        for (int i = 0; i < size; i++) {
            Param param = this.params.get(i);
            if (param.key.equals(key)) {
                if (removedParams == null) {
                    removedParams = new ArrayList<>();
                }
                this.params.remove(i);
                this.expectedLength -= (param.key.length() + param.value.length()) + 2;
                removedParams.add(param);
            }
        }
        return removedParams;
    }

    protected Param getParam(String key) {
        parseTemplateParams();
        if (this.params == null) {
            return null;
        }
        int size = this.params.size();
        for (int i = 0; i < size; i++) {
            Param param = this.params.get(i);
            if (param.key.equals(key)) {
                return param;
            }
        }
        return null;
    }

    protected List<Param> getParams(String key) {
        parseTemplateParams();
        if (this.params == null) {
            return null;
        }
        int size = this.params.size();
        List<Param> paramsList = null;
        for (int i = 0; i < size; i++) {
            Param param = this.params.get(i);
            if (param.key.equals(key)) {
                if (paramsList == null) {
                    paramsList = new ArrayList<>();
                }
                paramsList.add(param);
            }
        }
        return paramsList;
    }

    private void writeHref(StringBuilder out) {
        String template = this.hrefTemplate;
        boolean escaped = false;
        int openBrackets = 0;
        int closeBrackets = 0;
        int open = -1;
        int length = template.length();
        for (int i = 0; i < length; i++) {
            char c = template.charAt(i);
            if (escaped) {
                escaped = false;
                out.append(c);
            } else {
                switch (c) {
                    case '\\':
                        escaped = true;
                        out.append(c);
                        break;
                    case '{':
                        openBrackets++;
                        if (openBrackets == 1) {
                            open = i;
                        }
                        break;
                    case '}':
                        if (openBrackets > 0) {
                            closeBrackets++;
                            if (openBrackets == closeBrackets) {
                                String key = template.substring(open + openBrackets, (i + 1) - closeBrackets);
                                open = -1;
                                openBrackets = 0;
                                closeBrackets = 0;
                                Param param = getParam(key);
                                param.isTemplateParam = true;
                                if (param == null) {
                                    out.append("null");
                                } else {
                                    out.append(urlEncode(param.value));
                                }
                            }
                        } else {
                            out.append(c);
                        }
                        break;
                    default:
                        if (open < 0) {
                            out.append(c);
                        }
                        break;
                }
            }
        }
    }

    private void writeParams(StringBuilder out) {
        char prefix = '?';
        int size = this.params.size();
        for (int i = 0; i < size; i++) {
            Param param = this.params.get(i);
            if (!param.isTemplateParam) {
                out.append(prefix);
                prefix = '&';
                out.append(param.key);
                out.append('=');
                String templateKey = param.getValueTemplateKey();
                if (templateKey != null) {
                    Param valParam = getParam(templateKey);
                    if (valParam != null) {
                        valParam.isTemplateParam = true;
                        out.append(urlEncode(valParam.value));
                    } else {
                        out.append(urlEncode(param.value));
                    }
                } else {
                    out.append(urlEncode(param.value));
                }
            }
        }
    }

    private void writeSelfParams(StringBuilder out) {
        if (this.multiGet) {
            StringBuilder sb = new StringBuilder();
            for (String self : this.selfParams) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(self);
            }
            if (sb.length() > 0) {
                out.append(this.params.isEmpty() ? '?' : '&').append("self").append("=").append(sb.toString());
            }
        }
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            UaLog.error("UrlEncode error", (Throwable) e);
            return value;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static class Param {
        boolean isTemplateParam;
        String key;
        String value;

        private Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getValueTemplateKey() {
            int length = this.value.length();
            if (length <= 1 || this.value.charAt(0) != '{' || this.value.charAt(length - 1) != '}') {
                return null;
            }
            int start = 0;
            while (this.value.charAt(start) == '{') {
                start++;
            }
            int end = length;
            while (this.value.charAt(end - 1) == '}') {
                end--;
            }
            return this.value.substring(start, end);
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }
}
