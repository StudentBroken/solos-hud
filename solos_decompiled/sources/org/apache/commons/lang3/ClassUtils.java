package org.apache.commons.lang3;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes51.dex */
public class ClassUtils {
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    private static final Map<String, String> abbreviationMap;
    private static final Map<String, String> reverseAbbreviationMap;
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap;
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);
    public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap();

    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        wrapperPrimitiveMap = new HashMap();
        for (Class<?> primitiveClass : primitiveWrapperMap.keySet()) {
            Class<?> wrapperClass = primitiveWrapperMap.get(primitiveClass);
            if (!primitiveClass.equals(wrapperClass)) {
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }
        abbreviationMap = new HashMap();
        reverseAbbreviationMap = new HashMap();
        addAbbreviation("int", "I");
        addAbbreviation("boolean", "Z");
        addAbbreviation("float", "F");
        addAbbreviation("long", "J");
        addAbbreviation("short", "S");
        addAbbreviation("byte", "B");
        addAbbreviation("double", "D");
        addAbbreviation("char", "C");
    }

    private static void addAbbreviation(String primitive, String abbreviation) {
        abbreviationMap.put(primitive, abbreviation);
        reverseAbbreviationMap.put(abbreviation, primitive);
    }

    public static String getShortClassName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        String valueIfNull2 = getShortClassName(object.getClass());
        return valueIfNull2;
    }

    public static String getShortClassName(Class<?> cls) {
        return cls == null ? "" : getShortClassName(cls.getName());
    }

    public static String getShortClassName(String className) {
        if (className == null || className.length() == 0) {
            return "";
        }
        StringBuilder arrayPrefix = new StringBuilder();
        if (className.startsWith("[")) {
            while (className.charAt(0) == '[') {
                className = className.substring(1);
                arrayPrefix.append("[]");
            }
            if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
                className = className.substring(1, className.length() - 1);
            }
        }
        if (reverseAbbreviationMap.containsKey(className)) {
            className = reverseAbbreviationMap.get(className);
        }
        int lastDotIdx = className.lastIndexOf(46);
        int innerIdx = className.indexOf(36, lastDotIdx != -1 ? lastDotIdx + 1 : 0);
        String out = className.substring(lastDotIdx + 1);
        if (innerIdx != -1) {
            out = out.replace('$', PACKAGE_SEPARATOR_CHAR);
        }
        return out + ((Object) arrayPrefix);
    }

    public static String getSimpleName(Class<?> cls) {
        return cls == null ? "" : cls.getSimpleName();
    }

    public static String getSimpleName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        String valueIfNull2 = getSimpleName(object.getClass());
        return valueIfNull2;
    }

    public static String getPackageName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        String valueIfNull2 = getPackageName(object.getClass());
        return valueIfNull2;
    }

    public static String getPackageName(Class<?> cls) {
        return cls == null ? "" : getPackageName(cls.getName());
    }

    public static String getPackageName(String className) {
        if (className == null || className.length() == 0) {
            return "";
        }
        while (className.charAt(0) == '[') {
            className = className.substring(1);
        }
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
            className = className.substring(1);
        }
        int i = className.lastIndexOf(46);
        if (i == -1) {
            return "";
        }
        return className.substring(0, i);
    }

    public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> superclass = cls.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            classes.add(superclass);
        }
        return classes;
    }

    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<>();
        getAllInterfaces(cls, interfacesFound);
        return new ArrayList(interfacesFound);
    }

    private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            for (Class<?> i : interfaces) {
                if (interfacesFound.add(i)) {
                    getAllInterfaces(i, interfacesFound);
                }
            }
            cls = cls.getSuperclass();
        }
    }

    public static List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
        if (classNames == null) {
            return null;
        }
        List<Class<?>> classes = new ArrayList<>(classNames.size());
        for (String className : classNames) {
            try {
                classes.add(Class.forName(className));
            } catch (Exception e) {
                classes.add(null);
            }
        }
        return classes;
    }

    public static List<String> convertClassesToClassNames(List<Class<?>> classes) {
        if (classes == null) {
            return null;
        }
        List<String> classNames = new ArrayList<>(classes.size());
        for (Class<?> cls : classes) {
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }

    public static boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray) {
        return isAssignable(classArray, toClassArray, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
    }

    public static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, boolean autoboxing) {
        if (!ArrayUtils.isSameLength(classArray, toClassArray)) {
            return false;
        }
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; i++) {
            if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        if (type == null) {
            return false;
        }
        return type.isPrimitive() || isPrimitiveWrapper(type);
    }

    public static boolean isPrimitiveWrapper(Class<?> type) {
        return wrapperPrimitiveMap.containsKey(type);
    }

    public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
        return isAssignable(cls, toClass, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
    }

    public static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
        if (toClass == null) {
            return false;
        }
        if (cls == null) {
            return toClass.isPrimitive() ? false : true;
        }
        if (autoboxing) {
            if (cls.isPrimitive() && !toClass.isPrimitive() && (cls = primitiveToWrapper(cls)) == null) {
                return false;
            }
            if (toClass.isPrimitive() && !cls.isPrimitive() && (cls = wrapperToPrimitive(cls)) == null) {
                return false;
            }
        }
        if (cls.equals(toClass)) {
            return true;
        }
        if (cls.isPrimitive()) {
            if (!toClass.isPrimitive()) {
                return false;
            }
            if (Integer.TYPE.equals(cls)) {
                return Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Long.TYPE.equals(cls)) {
                return Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Boolean.TYPE.equals(cls) || Double.TYPE.equals(cls)) {
                return false;
            }
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(toClass);
            }
            if (Character.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Short.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Byte.TYPE.equals(cls)) {
                return Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            return false;
        }
        return toClass.isAssignableFrom(cls);
    }

    public static Class<?> primitiveToWrapper(Class<?> cls) {
        if (cls == null || !cls.isPrimitive()) {
            return cls;
        }
        Class<?> convertedClass = primitiveWrapperMap.get(cls);
        return convertedClass;
    }

    public static Class<?>[] primitivesToWrappers(Class<?>... classes) {
        if (classes == null) {
            return null;
        }
        if (classes.length != 0) {
            Class<?>[] convertedClasses = new Class[classes.length];
            for (int i = 0; i < classes.length; i++) {
                convertedClasses[i] = primitiveToWrapper(classes[i]);
            }
            return convertedClasses;
        }
        return classes;
    }

    public static Class<?> wrapperToPrimitive(Class<?> cls) {
        return wrapperPrimitiveMap.get(cls);
    }

    public static Class<?>[] wrappersToPrimitives(Class<?>... classes) {
        if (classes == null) {
            return null;
        }
        if (classes.length != 0) {
            Class<?>[] convertedClasses = new Class[classes.length];
            for (int i = 0; i < classes.length; i++) {
                convertedClasses[i] = wrapperToPrimitive(classes[i]);
            }
            return convertedClasses;
        }
        return classes;
    }

    public static boolean isInnerClass(Class<?> cls) {
        return (cls == null || cls.getEnclosingClass() == null) ? false : true;
    }

    public static Class<?> getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
        Class<?> cls;
        try {
            if (abbreviationMap.containsKey(className)) {
                String clsName = "[" + abbreviationMap.get(className);
                cls = Class.forName(clsName, initialize, classLoader).getComponentType();
            } else {
                cls = Class.forName(toCanonicalName(className), initialize, classLoader);
            }
            return cls;
        } catch (ClassNotFoundException ex) {
            int lastDotIndex = className.lastIndexOf(46);
            if (lastDotIndex != -1) {
                try {
                    return getClass(classLoader, className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1), initialize);
                } catch (ClassNotFoundException e) {
                    throw ex;
                }
            }
            throw ex;
        }
    }

    public static Class<?> getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return getClass(classLoader, className, true);
    }

    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return getClass(className, true);
    }

    public static Class<?> getClass(String className, boolean initialize) throws ClassNotFoundException {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        ClassLoader loader = contextCL == null ? ClassUtils.class.getClassLoader() : contextCL;
        return getClass(loader, className, initialize);
    }

    public static Method getPublicMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        Method declaredMethod = cls.getMethod(methodName, parameterTypes);
        if (!Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
            List<Class<?>> candidateClasses = new ArrayList<>();
            candidateClasses.addAll(getAllInterfaces(cls));
            candidateClasses.addAll(getAllSuperclasses(cls));
            for (Class<?> candidateClass : candidateClasses) {
                if (Modifier.isPublic(candidateClass.getModifiers())) {
                    try {
                        Method candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
                        if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
                            return candidateMethod;
                        }
                    } catch (NoSuchMethodException e) {
                    }
                }
            }
            throw new NoSuchMethodException("Can't find a public method for " + methodName + " " + ArrayUtils.toString(parameterTypes));
        }
        return declaredMethod;
    }

    private static String toCanonicalName(String className) {
        String className2 = StringUtils.deleteWhitespace(className);
        if (className2 == null) {
            throw new NullPointerException("className must not be null.");
        }
        if (className2.endsWith("[]")) {
            StringBuilder classNameBuffer = new StringBuilder();
            while (className2.endsWith("[]")) {
                className2 = className2.substring(0, className2.length() - 2);
                classNameBuffer.append("[");
            }
            String abbreviation = abbreviationMap.get(className2);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            } else {
                classNameBuffer.append("L").append(className2).append(";");
            }
            String className3 = classNameBuffer.toString();
            return className3;
        }
        return className2;
    }

    public static Class<?>[] toClass(Object... array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i] == null ? null : array[i].getClass();
        }
        return classes;
    }

    public static String getShortCanonicalName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        String valueIfNull2 = getShortCanonicalName(object.getClass().getName());
        return valueIfNull2;
    }

    public static String getShortCanonicalName(Class<?> cls) {
        return cls == null ? "" : getShortCanonicalName(cls.getName());
    }

    public static String getShortCanonicalName(String canonicalName) {
        return getShortClassName(getCanonicalName(canonicalName));
    }

    public static String getPackageCanonicalName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        String valueIfNull2 = getPackageCanonicalName(object.getClass().getName());
        return valueIfNull2;
    }

    public static String getPackageCanonicalName(Class<?> cls) {
        return cls == null ? "" : getPackageCanonicalName(cls.getName());
    }

    public static String getPackageCanonicalName(String canonicalName) {
        return getPackageName(getCanonicalName(canonicalName));
    }

    private static String getCanonicalName(String className) {
        String className2 = StringUtils.deleteWhitespace(className);
        if (className2 == null) {
            return null;
        }
        int dim = 0;
        while (className2.startsWith("[")) {
            dim++;
            className2 = className2.substring(1);
        }
        if (dim >= 1) {
            if (className2.startsWith("L")) {
                className2 = className2.substring(1, className2.endsWith(";") ? className2.length() - 1 : className2.length());
            } else if (className2.length() > 0) {
                className2 = reverseAbbreviationMap.get(className2.substring(0, 1));
            }
            StringBuilder canonicalClassNameBuffer = new StringBuilder(className2);
            for (int i = 0; i < dim; i++) {
                canonicalClassNameBuffer.append("[]");
            }
            String className3 = canonicalClassNameBuffer.toString();
            return className3;
        }
        return className2;
    }
}
