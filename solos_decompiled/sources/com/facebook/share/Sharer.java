package com.facebook.share;

/* JADX INFO: loaded from: classes41.dex */
public interface Sharer {
    boolean getShouldFailOnDataError();

    void setShouldFailOnDataError(boolean z);

    public static class Result {
        final String postId;

        public Result(String postId) {
            this.postId = postId;
        }

        public String getPostId() {
            return this.postId;
        }
    }
}
