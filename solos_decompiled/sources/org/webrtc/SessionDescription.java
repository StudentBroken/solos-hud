package org.webrtc;

/* JADX INFO: loaded from: classes57.dex */
public class SessionDescription {
    public final String description;
    public final Type type;

    public enum Type {
        OFFER,
        PRANSWER,
        ANSWER;

        public String canonicalForm() {
            return name().toLowerCase();
        }

        public static Type fromCanonicalForm(String canonical) {
            return (Type) valueOf(Type.class, canonical.toUpperCase());
        }
    }

    public SessionDescription(Type type, String description) {
        this.type = type;
        this.description = description;
    }
}
