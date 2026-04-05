package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

/* JADX INFO: loaded from: classes51.dex */
public class ContextedRuntimeException extends RuntimeException implements ExceptionContext {
    private static final long serialVersionUID = 20110706;
    private final ExceptionContext exceptionContext;

    public ContextedRuntimeException() {
        this.exceptionContext = new DefaultExceptionContext();
    }

    public ContextedRuntimeException(String message) {
        super(message);
        this.exceptionContext = new DefaultExceptionContext();
    }

    public ContextedRuntimeException(Throwable cause) {
        super(cause);
        this.exceptionContext = new DefaultExceptionContext();
    }

    public ContextedRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.exceptionContext = new DefaultExceptionContext();
    }

    public ContextedRuntimeException(String message, Throwable cause, ExceptionContext context) {
        super(message, cause);
        this.exceptionContext = context == null ? new DefaultExceptionContext() : context;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public ContextedRuntimeException addContextValue(String label, Object value) {
        this.exceptionContext.addContextValue(label, value);
        return this;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public ContextedRuntimeException setContextValue(String label, Object value) {
        this.exceptionContext.setContextValue(label, value);
        return this;
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public List<Object> getContextValues(String label) {
        return this.exceptionContext.getContextValues(label);
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public Object getFirstContextValue(String label) {
        return this.exceptionContext.getFirstContextValue(label);
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public List<Pair<String, Object>> getContextEntries() {
        return this.exceptionContext.getContextEntries();
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public Set<String> getContextLabels() {
        return this.exceptionContext.getContextLabels();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return getFormattedExceptionMessage(super.getMessage());
    }

    public String getRawMessage() {
        return super.getMessage();
    }

    @Override // org.apache.commons.lang3.exception.ExceptionContext
    public String getFormattedExceptionMessage(String baseMessage) {
        return this.exceptionContext.getFormattedExceptionMessage(baseMessage);
    }
}
