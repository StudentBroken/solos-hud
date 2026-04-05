package org.apache.commons.lang3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;

/* JADX INFO: loaded from: classes51.dex */
public class SerializationUtils {
    public static <T extends Serializable> T clone(T object) throws Throwable {
        ClassLoaderAwareObjectInputStream in;
        if (object == null) {
            return null;
        }
        byte[] objectData = serialize(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        ClassLoaderAwareObjectInputStream in2 = null;
        try {
            try {
                in = new ClassLoaderAwareObjectInputStream(bais, object.getClass().getClassLoader());
            } catch (Throwable th) {
                th = th;
            }
            try {
                T t = (T) in.readObject();
                if (in == null) {
                    return t;
                }
                try {
                    in.close();
                    return t;
                } catch (IOException ex) {
                    throw new SerializationException("IOException on closing cloned object data InputStream.", ex);
                }
            } catch (IOException e) {
                ex = e;
                throw new SerializationException("IOException while reading cloned object data", ex);
            } catch (ClassNotFoundException e2) {
                ex = e2;
                throw new SerializationException("ClassNotFoundException while reading cloned object data", ex);
            } catch (Throwable th2) {
                th = th2;
                in2 = in;
                if (in2 != null) {
                    try {
                        in2.close();
                    } catch (IOException ex2) {
                        throw new SerializationException("IOException on closing cloned object data InputStream.", ex2);
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            ex = e3;
        } catch (ClassNotFoundException e4) {
            ex = e4;
        }
    }

    public static void serialize(Serializable obj, OutputStream outputStream) throws Throwable {
        ObjectOutputStream out;
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out2 = null;
        try {
            try {
                out = new ObjectOutputStream(outputStream);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            ex = e;
        }
        try {
            out.writeObject(obj);
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e2) {
                }
            }
        } catch (IOException e3) {
            ex = e3;
            throw new SerializationException(ex);
        } catch (Throwable th2) {
            th = th2;
            out2 = out;
            if (out2 != null) {
                try {
                    out2.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    public static byte[] serialize(Serializable obj) throws Throwable {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream) throws Throwable {
        ObjectInputStream in;
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream in2 = null;
        try {
            try {
                in = new ObjectInputStream(inputStream);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            ex = e;
        } catch (ClassNotFoundException e2) {
            ex = e2;
        }
        try {
            Object object = in.readObject();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                }
            }
            return object;
        } catch (IOException e4) {
            ex = e4;
            throw new SerializationException(ex);
        } catch (ClassNotFoundException e5) {
            ex = e5;
            throw new SerializationException(ex);
        } catch (Throwable th2) {
            th = th2;
            in2 = in;
            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    public static Object deserialize(byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        return deserialize(bais);
    }

    static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
        private ClassLoader classLoader;

        public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
            super(in);
            this.classLoader = classLoader;
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            String name = desc.getName();
            try {
                return Class.forName(name, false, this.classLoader);
            } catch (ClassNotFoundException e) {
                return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
            }
        }
    }
}
