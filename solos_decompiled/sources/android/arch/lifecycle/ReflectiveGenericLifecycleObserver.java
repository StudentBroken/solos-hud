package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes40.dex */
class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver {
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static final Map<Class, CallbackInfo> sInfoCache = new HashMap();
    private final CallbackInfo mInfo;
    private final Object mWrapped;

    ReflectiveGenericLifecycleObserver(Object wrapped) {
        this.mWrapped = wrapped;
        this.mInfo = getInfo(this.mWrapped.getClass());
    }

    @Override // android.arch.lifecycle.GenericLifecycleObserver
    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        invokeCallbacks(this.mInfo, source, event);
    }

    private void invokeMethodsForEvent(List<MethodReference> handlers, LifecycleOwner source, Lifecycle.Event event) {
        if (handlers != null) {
            for (int i = handlers.size() - 1; i >= 0; i--) {
                MethodReference reference = handlers.get(i);
                invokeCallback(reference, source, event);
            }
        }
    }

    private void invokeCallbacks(CallbackInfo info, LifecycleOwner source, Lifecycle.Event event) {
        invokeMethodsForEvent(info.mEventToHandlers.get(event), source, event);
        invokeMethodsForEvent(info.mEventToHandlers.get(Lifecycle.Event.ON_ANY), source, event);
    }

    private void invokeCallback(MethodReference reference, LifecycleOwner source, Lifecycle.Event event) {
        try {
            switch (reference.mCallType) {
                case 0:
                    reference.mMethod.invoke(this.mWrapped, new Object[0]);
                    return;
                case 1:
                    reference.mMethod.invoke(this.mWrapped, source);
                    return;
                case 2:
                    reference.mMethod.invoke(this.mWrapped, source, event);
                    return;
                default:
                    return;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw new RuntimeException("Failed to call observer method", e2.getCause());
        }
    }

    private static CallbackInfo getInfo(Class klass) {
        CallbackInfo existing = sInfoCache.get(klass);
        return existing != null ? existing : createInfo(klass);
    }

    private static void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> handlers, MethodReference newHandler, Lifecycle.Event newEvent, Class klass) {
        Lifecycle.Event event = handlers.get(newHandler);
        if (event != null && newEvent != event) {
            Method method = newHandler.mMethod;
            throw new IllegalArgumentException("Method " + method.getName() + " in " + klass.getName() + " already declared with different @OnLifecycleEvent value: previous value " + event + ", new value " + newEvent);
        }
        if (event == null) {
            handlers.put(newHandler, newEvent);
        }
    }

    private static CallbackInfo createInfo(Class klass) {
        CallbackInfo superInfo;
        Class superclass = klass.getSuperclass();
        Map<MethodReference, Lifecycle.Event> handlerToEvent = new HashMap<>();
        if (superclass != null && (superInfo = getInfo(superclass)) != null) {
            handlerToEvent.putAll(superInfo.mHandlerToEvent);
        }
        Method[] methods = klass.getDeclaredMethods();
        Class<?>[] interfaces = klass.getInterfaces();
        int length = interfaces.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= length) {
                break;
            }
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : getInfo(interfaces[i2]).mHandlerToEvent.entrySet()) {
                verifyAndPutHandler(handlerToEvent, entry.getKey(), entry.getValue(), klass);
            }
            i = i2 + 1;
        }
        for (Method method : methods) {
            OnLifecycleEvent annotation = (OnLifecycleEvent) method.getAnnotation(OnLifecycleEvent.class);
            if (annotation != null) {
                Class<?>[] params = method.getParameterTypes();
                int callType = 0;
                if (params.length > 0) {
                    callType = 1;
                    if (!params[0].isAssignableFrom(LifecycleOwner.class)) {
                        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                    }
                }
                Lifecycle.Event event = annotation.value();
                if (params.length > 1) {
                    callType = 2;
                    if (!params[1].isAssignableFrom(Lifecycle.Event.class)) {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    }
                    if (event != Lifecycle.Event.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                }
                if (params.length > 2) {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
                MethodReference methodReference = new MethodReference(callType, method);
                verifyAndPutHandler(handlerToEvent, methodReference, event, klass);
            }
        }
        CallbackInfo info = new CallbackInfo(handlerToEvent);
        sInfoCache.put(klass, info);
        return info;
    }

    static class CallbackInfo {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers = new HashMap();
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;

        CallbackInfo(Map<MethodReference, Lifecycle.Event> handlerToEvent) {
            this.mHandlerToEvent = handlerToEvent;
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : handlerToEvent.entrySet()) {
                Lifecycle.Event event = entry.getValue();
                List<MethodReference> methodReferences = this.mEventToHandlers.get(event);
                if (methodReferences == null) {
                    methodReferences = new ArrayList<>();
                    this.mEventToHandlers.put(event, methodReferences);
                }
                methodReferences.add(entry.getKey());
            }
        }
    }

    static class MethodReference {
        final int mCallType;
        final Method mMethod;

        MethodReference(int callType, Method method) {
            this.mCallType = callType;
            this.mMethod = method;
            this.mMethod.setAccessible(true);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MethodReference that = (MethodReference) o;
            return this.mCallType == that.mCallType && this.mMethod.getName().equals(that.mMethod.getName());
        }

        public int hashCode() {
            return (this.mCallType * 31) + this.mMethod.getName().hashCode();
        }
    }
}
