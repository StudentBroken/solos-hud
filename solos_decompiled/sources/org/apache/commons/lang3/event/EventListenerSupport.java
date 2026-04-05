package org.apache.commons.lang3.event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.Validate;

/* JADX INFO: loaded from: classes51.dex */
public class EventListenerSupport<L> implements Serializable {
    private static final long serialVersionUID = 3593265990380473632L;
    private List<L> listeners;
    private transient L[] prototypeArray;
    private transient L proxy;

    public static <T> EventListenerSupport<T> create(Class<T> listenerInterface) {
        return new EventListenerSupport<>(listenerInterface);
    }

    public EventListenerSupport(Class<L> listenerInterface) {
        this(listenerInterface, Thread.currentThread().getContextClassLoader());
    }

    public EventListenerSupport(Class<L> listenerInterface, ClassLoader classLoader) {
        this();
        Validate.notNull(listenerInterface, "Listener interface cannot be null.", new Object[0]);
        Validate.notNull(classLoader, "ClassLoader cannot be null.", new Object[0]);
        Validate.isTrue(listenerInterface.isInterface(), "Class {0} is not an interface", listenerInterface.getName());
        initializeTransientFields(listenerInterface, classLoader);
    }

    private EventListenerSupport() {
        this.listeners = new CopyOnWriteArrayList();
    }

    public L fire() {
        return this.proxy;
    }

    public void addListener(L listener) {
        Validate.notNull(listener, "Listener object cannot be null.", new Object[0]);
        this.listeners.add(listener);
    }

    int getListenerCount() {
        return this.listeners.size();
    }

    public void removeListener(L listener) {
        Validate.notNull(listener, "Listener object cannot be null.", new Object[0]);
        this.listeners.remove(listener);
    }

    public L[] getListeners() {
        return (L[]) this.listeners.toArray(this.prototypeArray);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ArrayList<L> serializableListeners = new ArrayList<>();
        ObjectOutputStream testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
        for (L listener : this.listeners) {
            try {
                testObjectOutputStream.writeObject(listener);
                serializableListeners.add(listener);
            } catch (IOException e) {
                testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
            }
        }
        objectOutputStream.writeObject(serializableListeners.toArray(this.prototypeArray));
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Object[] objArr = (Object[]) objectInputStream.readObject();
        this.listeners = new CopyOnWriteArrayList(objArr);
        initializeTransientFields(objArr.getClass().getComponentType(), Thread.currentThread().getContextClassLoader());
    }

    private void initializeTransientFields(Class<L> cls, ClassLoader classLoader) {
        this.prototypeArray = (L[]) ((Object[]) Array.newInstance((Class<?>) cls, 0));
        createProxy(cls, classLoader);
    }

    private void createProxy(Class<L> listenerInterface, ClassLoader classLoader) {
        this.proxy = listenerInterface.cast(Proxy.newProxyInstance(classLoader, new Class[]{listenerInterface}, createInvocationHandler()));
    }

    protected InvocationHandler createInvocationHandler() {
        return new ProxyInvocationHandler();
    }

    protected class ProxyInvocationHandler implements InvocationHandler {
        private static final long serialVersionUID = 1;

        protected ProxyInvocationHandler() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Iterator i$ = EventListenerSupport.this.listeners.iterator();
            while (i$.hasNext()) {
                method.invoke(i$.next(), args);
            }
            return null;
        }
    }
}
