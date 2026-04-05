package com.twitter.sdk.android.core.internal;

import android.content.Context;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

/* JADX INFO: loaded from: classes62.dex */
public class MigrationHelper {
    private static final String SHARED_PREFS_DIR = "shared_prefs";

    public void migrateSessionStore(Context context, String prefixMatch, String expectedFileName) {
        File oldPrefsharedPrefsFile;
        File sharedPrefsDir = getSharedPreferencesDir(context);
        if (sharedPrefsDir.exists() && sharedPrefsDir.isDirectory()) {
            File expectedSharedPrefsFile = new File(sharedPrefsDir, expectedFileName);
            if (!expectedSharedPrefsFile.exists() && (oldPrefsharedPrefsFile = getLatestFile(sharedPrefsDir, prefixMatch)) != null) {
                oldPrefsharedPrefsFile.renameTo(expectedSharedPrefsFile);
            }
        }
    }

    File getSharedPreferencesDir(Context context) {
        return new File(context.getApplicationInfo().dataDir, SHARED_PREFS_DIR);
    }

    File getLatestFile(File sharedPrefsDir, String prefix) {
        File[] files = sharedPrefsDir.listFiles(new PrefixFileNameFilter(prefix));
        Arrays.sort(files, new FileLastModifiedComparator());
        if (files.length > 0) {
            return files[0];
        }
        return null;
    }

    static class FileLastModifiedComparator implements Comparator<File> {
        FileLastModifiedComparator() {
        }

        @Override // java.util.Comparator
        public int compare(File file1, File file2) {
            return Long.valueOf(file2.lastModified()).compareTo(Long.valueOf(file1.lastModified()));
        }
    }

    static class PrefixFileNameFilter implements FilenameFilter {
        final String prefix;

        public PrefixFileNameFilter(String prefix) {
            this.prefix = prefix;
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String filename) {
            return filename.startsWith(this.prefix);
        }
    }
}
