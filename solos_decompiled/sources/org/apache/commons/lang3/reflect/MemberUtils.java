package org.apache.commons.lang3.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ClassUtils;

/* JADX INFO: loaded from: classes51.dex */
abstract class MemberUtils {
    private static final int ACCESS_TEST = 7;
    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = {Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};

    MemberUtils() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void setAccessibleWorkaround(AccessibleObject accessibleObject) {
        if (accessibleObject != 0 && !accessibleObject.isAccessible()) {
            Member m = (Member) accessibleObject;
            if (Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                try {
                    accessibleObject.setAccessible(true);
                } catch (SecurityException e) {
                }
            }
        }
    }

    static boolean isPackageAccess(int modifiers) {
        return (modifiers & 7) == 0;
    }

    static boolean isAccessible(Member m) {
        return (m == null || !Modifier.isPublic(m.getModifiers()) || m.isSynthetic()) ? false : true;
    }

    static int compareParameterTypes(Class<?>[] left, Class<?>[] right, Class<?>[] actual) {
        float leftCost = getTotalTransformationCost(actual, left);
        float rightCost = getTotalTransformationCost(actual, right);
        if (leftCost < rightCost) {
            return -1;
        }
        return rightCost < leftCost ? 1 : 0;
    }

    private static float getTotalTransformationCost(Class<?>[] srcArgs, Class<?>[] destArgs) {
        float totalCost = 0.0f;
        for (int i = 0; i < srcArgs.length; i++) {
            Class<?> srcClass = srcArgs[i];
            Class<?> destClass = destArgs[i];
            totalCost += getObjectTransformationCost(srcClass, destClass);
        }
        return totalCost;
    }

    private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
        if (destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        }
        float cost = 0.0f;
        while (true) {
            if (srcClass != null && !destClass.equals(srcClass)) {
                if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
                    cost += 0.25f;
                    break;
                }
                cost += 1.0f;
                srcClass = srcClass.getSuperclass();
            } else {
                break;
            }
        }
        if (srcClass == null) {
            return cost + 1.5f;
        }
        return cost;
    }

    private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
        float cost = 0.0f;
        Class<?> cls = srcClass;
        if (!cls.isPrimitive()) {
            cost = 0.0f + 0.1f;
            cls = ClassUtils.wrapperToPrimitive(cls);
        }
        for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
            if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1f;
                if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }
        return cost;
    }
}
