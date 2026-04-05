package com.ua.sdk.privacy;

import com.ua.sdk.Entity;

/* JADX INFO: loaded from: classes65.dex */
public interface Privacy extends Entity {
    Level getLevel();

    String getPrivacyDescription();

    public enum Level {
        PRIVATE(0),
        FRIENDS(1),
        PUBLIC(3);

        public final int id;

        Level(int id) {
            this.id = id;
        }
    }
}
