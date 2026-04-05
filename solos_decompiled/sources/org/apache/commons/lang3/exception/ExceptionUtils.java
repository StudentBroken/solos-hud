package org.apache.commons.lang3.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

/* JADX INFO: loaded from: classes51.dex */
public class ExceptionUtils {
    private static final String[] CAUSE_METHOD_NAMES = {"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
    static final String WRAPPED_MARKER = " [wrapped] ";

    @Deprecated
    public static String[] getDefaultCauseMethodNames() {
        return (String[]) ArrayUtils.clone(CAUSE_METHOD_NAMES);
    }

    @Deprecated
    public static Throwable getCause(Throwable throwable) {
        return getCause(throwable, CAUSE_METHOD_NAMES);
    }

    @Deprecated
    public static Throwable getCause(Throwable throwable, String[] methodNames) {
        Throwable cause;
        if (throwable == null) {
            return null;
        }
        if (methodNames == null) {
            methodNames = CAUSE_METHOD_NAMES;
        }
        String[] arr$ = methodNames;
        for (String methodName : arr$) {
            if (methodName != null && (cause = getCauseUsingMethodName(throwable, methodName)) != null) {
                return cause;
            }
        }
        return null;
    }

    public static Throwable getRootCause(Throwable throwable) {
        List<Throwable> list = getThrowableList(throwable);
        if (list.size() < 2) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
        Method method = null;
        try {
            method = throwable.getClass().getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e2) {
        }
        if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
            try {
                return (Throwable) method.invoke(throwable, new Object[0]);
            } catch (IllegalAccessException e3) {
            } catch (IllegalArgumentException e4) {
            } catch (InvocationTargetException e5) {
            }
        }
        return null;
    }

    public static int getThrowableCount(Throwable throwable) {
        return getThrowableList(throwable).size();
    }

    public static Throwable[] getThrowables(Throwable throwable) {
        List<Throwable> list = getThrowableList(throwable);
        return (Throwable[]) list.toArray(new Throwable[list.size()]);
    }

    public static List<Throwable> getThrowableList(Throwable throwable) {
        List<Throwable> list = new ArrayList<>();
        while (throwable != null && !list.contains(throwable)) {
            list.add(throwable);
            throwable = getCause(throwable);
        }
        return list;
    }

    public static int indexOfThrowable(Throwable throwable, Class<?> clazz) {
        return indexOf(throwable, clazz, 0, false);
    }

    public static int indexOfThrowable(Throwable throwable, Class<?> clazz, int fromIndex) {
        return indexOf(throwable, clazz, fromIndex, false);
    }

    public static int indexOfType(Throwable throwable, Class<?> type) {
        return indexOf(throwable, type, 0, true);
    }

    public static int indexOfType(Throwable throwable, Class<?> type, int fromIndex) {
        return indexOf(throwable, type, fromIndex, true);
    }

    private static int indexOf(Throwable throwable, Class<?> type, int fromIndex, boolean subclass) {
        if (throwable == null || type == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        Throwable[] throwables = getThrowables(throwable);
        if (fromIndex >= throwables.length) {
            return -1;
        }
        if (subclass) {
            for (int i = fromIndex; i < throwables.length; i++) {
                if (type.isAssignableFrom(throwables[i].getClass())) {
                    return i;
                }
            }
        } else {
            for (int i2 = fromIndex; i2 < throwables.length; i2++) {
                if (type.equals(throwables[i2].getClass())) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public static void printRootCauseStackTrace(Throwable throwable) {
        printRootCauseStackTrace(throwable, System.err);
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintStream stream) {
        if (throwable != null) {
            if (stream == null) {
                throw new IllegalArgumentException("The PrintStream must not be null");
            }
            String[] trace = getRootCauseStackTrace(throwable);
            for (String element : trace) {
                stream.println(element);
            }
            stream.flush();
        }
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintWriter writer) {
        if (throwable != null) {
            if (writer == null) {
                throw new IllegalArgumentException("The PrintWriter must not be null");
            }
            String[] trace = getRootCauseStackTrace(throwable);
            for (String element : trace) {
                writer.println(element);
            }
            writer.flush();
        }
    }

    public static String[] getRootCauseStackTrace(Throwable throwable) {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        Throwable[] throwables = getThrowables(throwable);
        int count = throwables.length;
        List<String> frames = new ArrayList<>();
        List<String> nextTrace = getStackFrameList(throwables[count - 1]);
        int i = count;
        while (true) {
            i--;
            if (i >= 0) {
                List<String> trace = nextTrace;
                if (i != 0) {
                    nextTrace = getStackFrameList(throwables[i - 1]);
                    removeCommonFrames(trace, nextTrace);
                }
                if (i == count - 1) {
                    frames.add(throwables[i].toString());
                } else {
                    frames.add(WRAPPED_MARKER + throwables[i].toString());
                }
                for (int j = 0; j < trace.size(); j++) {
                    frames.add(trace.get(j));
                }
            } else {
                return (String[]) frames.toArray(new String[frames.size()]);
            }
        }
    }

    public static void removeCommonFrames(List<String> causeFrames, List<String> wrapperFrames) {
        if (causeFrames == null || wrapperFrames == null) {
            throw new IllegalArgumentException("The List must not be null");
        }
        int causeFrameIndex = causeFrames.size() - 1;
        for (int wrapperFrameIndex = wrapperFrames.size() - 1; causeFrameIndex >= 0 && wrapperFrameIndex >= 0; wrapperFrameIndex--) {
            String causeFrame = causeFrames.get(causeFrameIndex);
            String wrapperFrame = wrapperFrames.get(wrapperFrameIndex);
            if (causeFrame.equals(wrapperFrame)) {
                causeFrames.remove(causeFrameIndex);
            }
            causeFrameIndex--;
        }
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter((Writer) sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static String[] getStackFrames(Throwable throwable) {
        return throwable == null ? ArrayUtils.EMPTY_STRING_ARRAY : getStackFrames(getStackTrace(throwable));
    }

    static String[] getStackFrames(String stackTrace) {
        String linebreak = SystemUtils.LINE_SEPARATOR;
        StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        List<String> list = new ArrayList<>();
        while (frames.hasMoreTokens()) {
            list.add(frames.nextToken());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    static List<String> getStackFrameList(Throwable t) {
        String stackTrace = getStackTrace(t);
        String linebreak = SystemUtils.LINE_SEPARATOR;
        StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        List<String> list = new ArrayList<>();
        boolean traceStarted = false;
        while (frames.hasMoreTokens()) {
            String token = frames.nextToken();
            int at = token.indexOf("at");
            if (at != -1 && token.substring(0, at).trim().length() == 0) {
                traceStarted = true;
                list.add(token);
            } else if (traceStarted) {
                break;
            }
        }
        return list;
    }

    public static String getMessage(Throwable th) {
        if (th == null) {
            return "";
        }
        String clsName = ClassUtils.getShortClassName(th, null);
        String msg = th.getMessage();
        return clsName + ": " + StringUtils.defaultString(msg);
    }

    public static String getRootCauseMessage(Throwable th) {
        Throwable root = getRootCause(th);
        if (root == null) {
            root = th;
        }
        return getMessage(root);
    }
}
