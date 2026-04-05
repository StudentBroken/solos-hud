package retrofit;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes53.dex */
final class Types {
    private static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    private Types() {
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                return (Class) rawType;
            }
            throw new IllegalArgumentException();
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }
        String className = type == null ? "null" : type.getClass().getName();
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
    }

    public static boolean equals(Type a, Type b) {
        if (a == b) {
            return true;
        }
        if (a instanceof Class) {
            return a.equals(b);
        }
        if (a instanceof ParameterizedType) {
            if (!(b instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType pa = (ParameterizedType) a;
            ParameterizedType pb = (ParameterizedType) b;
            return equal(pa.getOwnerType(), pb.getOwnerType()) && pa.getRawType().equals(pb.getRawType()) && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());
        }
        if (a instanceof GenericArrayType) {
            if (!(b instanceof GenericArrayType)) {
                return false;
            }
            GenericArrayType ga = (GenericArrayType) a;
            GenericArrayType gb = (GenericArrayType) b;
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
        }
        if (a instanceof WildcardType) {
            if (!(b instanceof WildcardType)) {
                return false;
            }
            WildcardType wa = (WildcardType) a;
            WildcardType wb = (WildcardType) b;
            return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds()) && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());
        }
        if (!(a instanceof TypeVariable) || !(b instanceof TypeVariable)) {
            return false;
        }
        TypeVariable<?> va = (TypeVariable) a;
        TypeVariable<?> vb = (TypeVariable) b;
        return va.getGenericDeclaration() == vb.getGenericDeclaration() && va.getName().equals(vb.getName());
    }

    static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
        if (toResolve != rawType) {
            if (toResolve.isInterface()) {
                Class<?>[] interfaces = rawType.getInterfaces();
                int length = interfaces.length;
                for (int i = 0; i < length; i++) {
                    if (interfaces[i] == toResolve) {
                        Type context2 = rawType.getGenericInterfaces()[i];
                        return context2;
                    }
                    if (toResolve.isAssignableFrom(interfaces[i])) {
                        Type context3 = getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                        return context3;
                    }
                }
            }
            if (!rawType.isInterface()) {
                while (rawType != Object.class) {
                    Class<?> rawSupertype = rawType.getSuperclass();
                    if (rawSupertype == toResolve) {
                        Type context4 = rawType.getGenericSuperclass();
                        return context4;
                    }
                    if (toResolve.isAssignableFrom(rawSupertype)) {
                        Type context5 = getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                        return context5;
                    }
                    rawType = rawSupertype;
                }
            }
            return toResolve;
        }
        return context;
    }

    private static int indexOf(Object[] array, Object toFind) {
        for (int i = 0; i < array.length; i++) {
            if (toFind.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    private static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hashCodeOrZero(Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }

    public static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
        if (supertype.isAssignableFrom(contextRawType)) {
            return resolve(context, contextRawType, getGenericSupertype(context, contextRawType, supertype));
        }
        throw new IllegalArgumentException();
    }

    public static Type resolve(Type type, Class<?> cls, Type type2) {
        Type typeResolve;
        while (type2 instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type2;
            type2 = resolveTypeVariable(type, cls, typeVariable);
            if (type2 == typeVariable) {
                return type2;
            }
        }
        if ((type2 instanceof Class) && ((Class) type2).isArray()) {
            Class cls2 = (Class) type2;
            Class<?> componentType = cls2.getComponentType();
            Type typeResolve2 = resolve(type, cls, componentType);
            Type genericArrayTypeImpl = cls2;
            if (componentType != typeResolve2) {
                genericArrayTypeImpl = new GenericArrayTypeImpl(typeResolve2);
            }
            return genericArrayTypeImpl;
        }
        if (type2 instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type2;
            Type genericComponentType = genericArrayType.getGenericComponentType();
            Type typeResolve3 = resolve(type, cls, genericComponentType);
            return genericComponentType != typeResolve3 ? new GenericArrayTypeImpl(typeResolve3) : genericArrayType;
        }
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type2;
            Type ownerType = parameterizedType.getOwnerType();
            Type typeResolve4 = resolve(type, cls, ownerType);
            boolean z = typeResolve4 != ownerType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            int length = actualTypeArguments.length;
            for (int i = 0; i < length; i++) {
                Type typeResolve5 = resolve(type, cls, actualTypeArguments[i]);
                if (typeResolve5 != actualTypeArguments[i]) {
                    if (!z) {
                        actualTypeArguments = (Type[]) actualTypeArguments.clone();
                        z = true;
                    }
                    actualTypeArguments[i] = typeResolve5;
                }
            }
            if (z) {
                return new ParameterizedTypeImpl(typeResolve4, parameterizedType.getRawType(), actualTypeArguments);
            }
            return parameterizedType;
        }
        if (!(type2 instanceof WildcardType)) {
            return type2;
        }
        WildcardType wildcardType = (WildcardType) type2;
        Type[] lowerBounds = wildcardType.getLowerBounds();
        Type[] upperBounds = wildcardType.getUpperBounds();
        if (lowerBounds.length == 1) {
            Type typeResolve6 = resolve(type, cls, lowerBounds[0]);
            if (typeResolve6 != lowerBounds[0]) {
                return new WildcardTypeImpl(new Type[]{Object.class}, new Type[]{typeResolve6});
            }
            return wildcardType;
        }
        if (upperBounds.length == 1 && (typeResolve = resolve(type, cls, upperBounds[0])) != upperBounds[0]) {
            return new WildcardTypeImpl(new Type[]{typeResolve}, EMPTY_TYPE_ARRAY);
        }
        return wildcardType;
    }

    private static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
        Class<?> declaredByRaw = declaringClassOf(unknown);
        if (declaredByRaw != null) {
            Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
            if (declaredBy instanceof ParameterizedType) {
                int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
                return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
            }
            return unknown;
        }
        return unknown;
    }

    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            return (Class) genericDeclaration;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkNotPrimitive(Type type) {
        if ((type instanceof Class) && ((Class) type).isPrimitive()) {
            throw new IllegalArgumentException();
        }
    }

    private static final class ParameterizedTypeImpl implements ParameterizedType {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
            if (rawType instanceof Class) {
                if ((ownerType == null) != (((Class) rawType).getEnclosingClass() == null)) {
                    throw new IllegalArgumentException();
                }
            }
            this.ownerType = ownerType;
            this.rawType = rawType;
            this.typeArguments = (Type[]) typeArguments.clone();
            for (Type typeArgument : this.typeArguments) {
                if (typeArgument == null) {
                    throw new NullPointerException();
                }
                Types.checkNotPrimitive(typeArgument);
            }
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return (Type[]) this.typeArguments.clone();
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return this.ownerType;
        }

        public boolean equals(Object other) {
            return (other instanceof ParameterizedType) && Types.equals(this, (ParameterizedType) other);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode()) ^ Types.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            StringBuilder result = new StringBuilder((this.typeArguments.length + 1) * 30);
            result.append(Types.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return result.toString();
            }
            result.append("<").append(Types.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; i++) {
                result.append(", ").append(Types.typeToString(this.typeArguments[i]));
            }
            return result.append(">").toString();
        }
    }

    private static final class GenericArrayTypeImpl implements GenericArrayType {
        private final Type componentType;

        public GenericArrayTypeImpl(Type componentType) {
            this.componentType = componentType;
        }

        @Override // java.lang.reflect.GenericArrayType
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public boolean equals(Object o) {
            return (o instanceof GenericArrayType) && Types.equals(this, (GenericArrayType) o);
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return Types.typeToString(this.componentType) + "[]";
        }
    }

    private static final class WildcardTypeImpl implements WildcardType {
        private final Type lowerBound;
        private final Type upperBound;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            if (lowerBounds.length > 1) {
                throw new IllegalArgumentException();
            }
            if (upperBounds.length != 1) {
                throw new IllegalArgumentException();
            }
            if (lowerBounds.length == 1) {
                if (lowerBounds[0] == null) {
                    throw new NullPointerException();
                }
                Types.checkNotPrimitive(lowerBounds[0]);
                if (upperBounds[0] != Object.class) {
                    throw new IllegalArgumentException();
                }
                this.lowerBound = lowerBounds[0];
                this.upperBound = Object.class;
                return;
            }
            if (upperBounds[0] == null) {
                throw new NullPointerException();
            }
            Types.checkNotPrimitive(upperBounds[0]);
            this.lowerBound = null;
            this.upperBound = upperBounds[0];
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getLowerBounds() {
            return this.lowerBound != null ? new Type[]{this.lowerBound} : Types.EMPTY_TYPE_ARRAY;
        }

        public boolean equals(Object other) {
            return (other instanceof WildcardType) && Types.equals(this, (WildcardType) other);
        }

        public int hashCode() {
            return (this.lowerBound != null ? this.lowerBound.hashCode() + 31 : 1) ^ (this.upperBound.hashCode() + 31);
        }

        public String toString() {
            return this.lowerBound != null ? "? super " + Types.typeToString(this.lowerBound) : this.upperBound == Object.class ? "?" : "? extends " + Types.typeToString(this.upperBound);
        }
    }
}
