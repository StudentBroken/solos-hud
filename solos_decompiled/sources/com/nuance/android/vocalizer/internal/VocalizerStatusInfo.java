package com.nuance.android.vocalizer.internal;

import android.util.Log;

/* JADX INFO: loaded from: classes16.dex */
public class VocalizerStatusInfo {
    public int mHeapBlocks = 0;
    public int mSystemFileHandles = 0;
    public int mAssetFileHandles = 0;

    public void printLeaks(String str) {
        if (hasLeaks()) {
            Log.e(str, "PRINTING LEAKS!");
            print(str);
        }
    }

    public void print(String str) {
        Log.i(str, "Heap Blocks: " + this.mHeapBlocks);
        Log.i(str, "System File Handles: " + this.mSystemFileHandles);
        Log.i(str, "Asset File Handles: " + this.mAssetFileHandles);
    }

    public boolean hasLeaks() {
        return (this.mHeapBlocks == 0 && this.mSystemFileHandles == 0 && this.mAssetFileHandles == 0) ? false : true;
    }
}
