package com.kopin.pupil.aria.contacts;

/* JADX INFO: loaded from: classes21.dex */
public class ContactResolver {
    public static final String CONTACTS_APP_NAME = "contacts";
    public static final String CONTACTS_GET_TO_WHO = "who";
    private static ContactCacheLookup mCache;

    interface ContactCacheLookup {
        String getNameFor(String str);

        String getNumberFor(String str);
    }

    public interface OnResolvedListener {
        void onNumberResolved(ResolvedContact resolvedContact);
    }

    static void init(ContactCacheLookup cache) {
        mCache = cache;
    }

    public static String getNumberFor(String name) {
        if (mCache != null) {
            return mCache.getNumberFor(name);
        }
        return null;
    }

    public static String getNameFor(String number) {
        if (mCache != null) {
            return mCache.getNameFor(number);
        }
        return null;
    }

    public static class ResolvedContact {
        private String[] mAlternateNumbers;
        private String mDisplayName;
        private String mExpandedName;
        private String mNumberToUse;
        private String[] mUniqueNameParts;

        public ResolvedContact(String numberOnly, String sayAs) {
            this.mNumberToUse = numberOnly;
            this.mDisplayName = sayAs;
        }

        public ResolvedContact(String name, String expanded, String[] altMatches, String[] numbers) {
            this.mDisplayName = name;
            this.mExpandedName = expanded;
            this.mUniqueNameParts = altMatches;
            if (numbers.length > 1) {
                this.mAlternateNumbers = numbers;
            } else {
                this.mNumberToUse = numbers[0];
            }
        }

        public String getDisplayName() {
            return this.mDisplayName == null ? this.mNumberToUse : this.mDisplayName;
        }

        public String getExpandedName() {
            return this.mExpandedName == null ? getDisplayName() : this.mExpandedName;
        }

        public String[] getUniqueNameParts() {
            int count;
            int i = 0;
            int count2 = 1;
            for (String str : this.mUniqueNameParts) {
                if (str != null) {
                    count2++;
                }
            }
            String[] parts = new String[count2];
            parts[0] = getExpandedName();
            String[] strArr = this.mUniqueNameParts;
            int length = strArr.length;
            int count3 = 1;
            while (i < length) {
                String p = strArr[i];
                if (p != null) {
                    count = count3 + 1;
                    parts[count3] = p;
                } else {
                    count = count3;
                }
                i++;
                count3 = count;
            }
            return parts;
        }

        public String getNumberToUse() {
            return (this.mAlternateNumbers == null || this.mAlternateNumbers.length <= 0) ? this.mNumberToUse : this.mAlternateNumbers[0];
        }

        public boolean isNumberOnly() {
            return this.mExpandedName == null;
        }

        public boolean hasAlternates() {
            return this.mAlternateNumbers != null && this.mAlternateNumbers.length > 0;
        }

        public String[] getAlternates() {
            return this.mAlternateNumbers;
        }

        public void selectAlternate(int idx) {
            if (hasAlternates() && this.mAlternateNumbers.length > idx) {
                this.mNumberToUse = this.mAlternateNumbers[idx];
                this.mAlternateNumbers = null;
            }
        }
    }
}
