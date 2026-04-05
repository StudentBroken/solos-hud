package org.apache.commons.lang3;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.mutable.MutableInt;

/* JADX INFO: loaded from: classes51.dex */
public class ObjectUtils {
    public static final Null NULL = new Null();

    public static <T> T defaultIfNull(T object, T defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static <T> T firstNonNull(T... values) {
        if (values != null) {
            for (T val : values) {
                if (val != null) {
                    return val;
                }
            }
        }
        return null;
    }

    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

    public static boolean notEqual(Object object1, Object object2) {
        return !equals(object1, object2);
    }

    public static int hashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public static int hashCodeMulti(Object... objects) {
        int hash = 1;
        if (objects != null) {
            for (Object object : objects) {
                hash = (hash * 31) + hashCode(object);
            }
        }
        return hash;
    }

    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        identityToString(buffer, object);
        return buffer.toString();
    }

    public static void identityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        buffer.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String toString(Object obj, String nullStr) {
        if (obj == null) {
            return nullStr;
        }
        String nullStr2 = obj.toString();
        return nullStr2;
    }

    public static <T extends Comparable<? super T>> T min(T... values) {
        T result = null;
        if (values != null) {
            for (T value : values) {
                if (compare(value, result, true) < 0) {
                    result = value;
                }
            }
        }
        return result;
    }

    public static <T extends Comparable<? super T>> T max(T... values) {
        T result = null;
        if (values != null) {
            for (T value : values) {
                if (compare(value, result, false) > 0) {
                    result = value;
                }
            }
        }
        return result;
    }

    public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
        return compare(c1, c2, false);
    }

    public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean nullGreater) {
        if (c1 == c2) {
            return 0;
        }
        if (c1 == null) {
            return !nullGreater ? -1 : 1;
        }
        if (c2 == null) {
            return nullGreater ? -1 : 1;
        }
        return c1.compareTo(c2);
    }

    public static <T extends Comparable<? super T>> T median(T... items) {
        Validate.notEmpty(items);
        Validate.noNullElements(items);
        TreeSet<T> sort = new TreeSet<>();
        Collections.addAll(sort, items);
        return (T) sort.toArray()[(sort.size() - 1) / 2];
    }

    public static <T> T median(Comparator<T> comparator, T... tArr) {
        Validate.notEmpty(tArr, "null/empty items", new Object[0]);
        Validate.noNullElements(tArr);
        Validate.notNull(comparator, "null comparator", new Object[0]);
        TreeSet treeSet = new TreeSet(comparator);
        Collections.addAll(treeSet, tArr);
        return (T) treeSet.toArray()[(treeSet.size() - 1) / 2];
    }

    public static <T> T mode(T... items) {
        if (ArrayUtils.isNotEmpty(items)) {
            HashMap<T, MutableInt> occurrences = new HashMap<>(items.length);
            for (T t : items) {
                MutableInt count = occurrences.get(t);
                if (count == null) {
                    occurrences.put(t, new MutableInt(1));
                } else {
                    count.increment();
                }
            }
            T result = null;
            int max = 0;
            for (Map.Entry<T, MutableInt> e : occurrences.entrySet()) {
                int cmp = e.getValue().intValue();
                if (cmp == max) {
                    result = null;
                } else if (cmp > max) {
                    max = cmp;
                    result = e.getKey();
                }
            }
            return result;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T clone(T t) throws CloneNotSupportedException {
        Object objInvoke;
        if (t instanceof Cloneable) {
            if (t.getClass().isArray()) {
                Class<?> componentType = t.getClass().getComponentType();
                if (!componentType.isPrimitive()) {
                    objInvoke = ((Object[]) t).clone();
                } else {
                    int length = Array.getLength(t);
                    objInvoke = Array.newInstance(componentType, length);
                    while (true) {
                        int i = length;
                        length = i - 1;
                        if (i <= 0) {
                            break;
                        }
                        Array.set(objInvoke, length, Array.get(t, length));
                    }
                }
            } else {
                try {
                    objInvoke = t.getClass().getMethod("clone", new Class[0]).invoke(t, new Object[0]);
                } catch (IllegalAccessException e) {
                    throw new CloneFailedException("Cannot clone Cloneable type " + t.getClass().getName(), e);
                } catch (NoSuchMethodException e2) {
                    throw new CloneFailedException("Cloneable type " + t.getClass().getName() + " has no clone method", e2);
                } catch (InvocationTargetException e3) {
                    throw new CloneFailedException("Exception cloning Cloneable type " + t.getClass().getName(), e3.getCause());
                }
            }
            return (T) objInvoke;
        }
        return null;
    }

    public static <T> T cloneIfPossible(T t) {
        T t2 = (T) clone(t);
        return t2 == null ? t : t2;
    }

    public static class Null implements Serializable {
        private static final long serialVersionUID = 7092611880189329093L;

        Null() {
        }

        private Object readResolve() {
            return ObjectUtils.NULL;
        }
    }
}
