package org.apache.commons.lang3.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/* JADX INFO: loaded from: classes51.dex */
public class DefaultExceptionContext implements ExceptionContext, Serializable {
    private static final long serialVersionUID = 20110706;
    private final List<Pair<String, Object>> contextValues = new ArrayList();

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public DefaultExceptionContext addContextValue(String label, Object value) {
        this.contextValues.add(new ImmutablePair(label, value));
        return this;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public DefaultExceptionContext setContextValue(String label, Object value) {
        Iterator<Pair<String, Object>> iter = this.contextValues.iterator();
        while (iter.hasNext()) {
            Pair<String, Object> p = iter.next();
            if (StringUtils.equals(label, p.getKey())) {
                iter.remove();
            }
        }
        addContextValue(label, value);
        return this;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public List<Object> getContextValues(String label) {
        List<Object> values = new ArrayList<>();
        for (Pair<String, Object> pair : this.contextValues) {
            if (StringUtils.equals(label, pair.getKey())) {
                values.add(pair.getValue());
            }
        }
        return values;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public Object getFirstContextValue(String label) {
        for (Pair<String, Object> pair : this.contextValues) {
            if (StringUtils.equals(label, pair.getKey())) {
                return pair.getValue();
            }
        }
        return null;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public Set<String> getContextLabels() {
        Set<String> labels = new HashSet<>();
        for (Pair<String, Object> pair : this.contextValues) {
            labels.add(pair.getKey());
        }
        return labels;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public List<Pair<String, Object>> getContextEntries() {
        return this.contextValues;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public String getFormattedExceptionMessage(String baseMessage) {
        String valueStr;
        StringBuilder buffer = new StringBuilder(256);
        if (baseMessage != null) {
            buffer.append(baseMessage);
        }
        if (this.contextValues.size() > 0) {
            if (buffer.length() > 0) {
                buffer.append('\n');
            }
            buffer.append("Exception Context:\n");
            int i = 0;
            for (Pair<String, Object> pair : this.contextValues) {
                buffer.append("\t[");
                i++;
                buffer.append(i);
                buffer.append(':');
                buffer.append(pair.getKey());
                buffer.append("=");
                Object value = pair.getValue();
                if (value == null) {
                    buffer.append("null");
                } else {
                    try {
                        valueStr = value.toString();
                    } catch (Exception e) {
                        valueStr = "Exception thrown on toString(): " + ExceptionUtils.getStackTrace(e);
                    }
                    buffer.append(valueStr);
                }
                buffer.append("]\n");
            }
            buffer.append("---------------------------------");
        }
        return buffer.toString();
    }
}
