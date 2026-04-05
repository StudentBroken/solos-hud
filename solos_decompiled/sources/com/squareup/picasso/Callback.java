package com.squareup.picasso;

/* JADX INFO: loaded from: classes11.dex */
public interface Callback {
    void onError();

    void onSuccess();

    public static class EmptyCallback implements Callback {
        @Override // com.squareup.picasso.Callback
        public void onSuccess() {
        }

        @Override // com.squareup.picasso.Callback
        public void onError() {
        }
    }
}
