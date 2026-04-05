package com.kopin.pupil.aria.contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.R;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.share.Sync;
import com.nuance.android.vocalizer.VocalizerEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/* JADX INFO: loaded from: classes43.dex */
public class ContactsCache {
    private static String NAME_BUILDER_FORMAT = null;
    private static String NAME_UNKNOWN = null;
    private static String NUMBER_WITHHELD = null;
    private static final String TAG = "ContactsCache";
    private static ContentObserver mContactsObserver;
    private static ContentResolver mContentResolver;
    private static Context mContext;
    private static final HashMap<String, String> COMMON_ABBREVS = new HashMap<>();
    private static final ArrayList<CachedContact> CACHED_CONTACTS = new ArrayList<>();
    private static final ArrayList<String> PREFIXES = new ArrayList<>();
    private static final ArrayList<String> FIRST_NAMES = new ArrayList<>();
    private static final ArrayList<String> MIDDLE_NAMES = new ArrayList<>();
    private static final ArrayList<String> LAST_NAMES = new ArrayList<>();
    private static final ArrayList<String> SUFFIXES = new ArrayList<>();
    private static boolean registered = false;
    private static final String[] CONTACTS_DB_FIELDS = {"contact_id", "data4", "data2", "data3", "has_phone_number", "data5", "data6"};
    private static final ContactResolver.ContactCacheLookup mResolverHook = new ContactResolver.ContactCacheLookup() { // from class: com.kopin.pupil.aria.contacts.ContactsCache.1
        @Override // com.kopin.pupil.aria.contacts.ContactResolver.ContactCacheLookup
        public String getNumberFor(String name) {
            return ContactsCache.getNumberFor(name);
        }

        @Override // com.kopin.pupil.aria.contacts.ContactResolver.ContactCacheLookup
        public String getNameFor(String number) {
            return ContactsCache.getNameFor(number);
        }
    };

    enum NamePart {
        PREFIX,
        FIRST,
        MIDDLE,
        LAST,
        SUFFIX
    }

    static void init(Context context, ContentObserver obs) {
        mContext = context;
        mContactsObserver = obs;
        String[] abbrevs = context.getResources().getStringArray(R.array.common_abbrevs_key);
        String[] full = context.getResources().getStringArray(R.array.common_abbrevs_full);
        if (abbrevs == null || full == null || abbrevs.length != full.length) {
            throw new IllegalArgumentException("Couldn't build abbreviations map, inconsistent string arrays");
        }
        for (int i = 0; i < abbrevs.length; i++) {
            COMMON_ABBREVS.put(abbrevs[i], full[i]);
        }
        NAME_BUILDER_FORMAT = context.getString(R.string.common_name_format);
        NAME_UNKNOWN = context.getString(R.string.common_name_unknown);
        NUMBER_WITHHELD = context.getString(R.string.common_name_withheld);
        mContentResolver = context.getContentResolver();
        getListOfContactNames(PREFIXES, FIRST_NAMES, MIDDLE_NAMES, LAST_NAMES, SUFFIXES, context);
        if (PermissionUtil.permitted(context, Permission.READ_CONTACTS)) {
            mContentResolver.registerContentObserver(ContactsContract.Data.CONTENT_URI, true, obs);
            registered = true;
        }
        ContactResolver.init(mResolverHook);
    }

    static void reset() {
        synchronized (CACHED_CONTACTS) {
            CACHED_CONTACTS.clear();
        }
        synchronized (PREFIXES) {
            PREFIXES.clear();
        }
        synchronized (FIRST_NAMES) {
            FIRST_NAMES.clear();
        }
        synchronized (MIDDLE_NAMES) {
            MIDDLE_NAMES.clear();
        }
        synchronized (LAST_NAMES) {
            LAST_NAMES.clear();
        }
        synchronized (SUFFIXES) {
            SUFFIXES.clear();
        }
        getListOfContactNames(PREFIXES, FIRST_NAMES, MIDDLE_NAMES, LAST_NAMES, SUFFIXES, mContext);
    }

    public static void reload() {
        if (!registered && PermissionUtil.permitted(mContext, Permission.READ_CONTACTS)) {
            mContentResolver.registerContentObserver(ContactsContract.Data.CONTENT_URI, true, mContactsObserver);
            registered = true;
        }
        if (mContactsObserver != null) {
            mContactsObserver.onChange(true);
        }
    }

    static String[] getPrefixes() {
        String[] strArr;
        synchronized (PREFIXES) {
            strArr = (String[]) PREFIXES.toArray(new String[0]);
        }
        return strArr;
    }

    static String[] getFirstNames() {
        String[] strArr;
        synchronized (FIRST_NAMES) {
            strArr = (String[]) FIRST_NAMES.toArray(new String[0]);
        }
        return strArr;
    }

    static String[] getMiddleNames() {
        String[] strArr;
        synchronized (MIDDLE_NAMES) {
            strArr = (String[]) MIDDLE_NAMES.toArray(new String[0]);
        }
        return strArr;
    }

    static String[] getLastNames() {
        String[] strArr;
        synchronized (LAST_NAMES) {
            strArr = (String[]) LAST_NAMES.toArray(new String[0]);
        }
        return strArr;
    }

    static String[] getSuffixes() {
        String[] strArr;
        synchronized (SUFFIXES) {
            strArr = (String[]) SUFFIXES.toArray(new String[0]);
        }
        return strArr;
    }

    private static String sanitiseName(String name) {
        return name.replaceAll("\\W", " ");
    }

    private static String replaceCommonAbbreviations(String name) {
        String replacement = COMMON_ABBREVS.get(name.toLowerCase(Locale.ENGLISH));
        return replacement != null ? replacement : name;
    }

    private static void sanitiseAndAddUnique(String name, ArrayList<String> toList) {
        if (PupilSpeechRecognizer.isValidAsr(name)) {
            String name2 = replaceCommonAbbreviations(sanitiseName(name));
            synchronized (toList) {
                if (!toList.contains(name2)) {
                    toList.add(name2);
                    Log.d("Aria", "  added '" + name2 + "'");
                }
            }
        }
    }

    private static void getListOfContactNames(ArrayList<String> prefixes, ArrayList<String> firstNames, ArrayList<String> middleNames, ArrayList<String> lastNames, ArrayList<String> suffixes, Context context) {
        if (!PermissionUtil.permitted(context, Permission.READ_CONTACTS)) {
            Log.i(TAG, "No READ_CONTACTS permission, will not load contacts to cache");
            return;
        }
        Cursor people = mContentResolver.query(ContactsContract.Data.CONTENT_URI, CONTACTS_DB_FIELDS, "mimetype = 'vnd.android.cursor.item/name'", null, null);
        while (people.moveToNext()) {
            try {
                int hasPhoneNumber = people.getInt(people.getColumnIndex("has_phone_number"));
                if (hasPhoneNumber != 0) {
                    String prefix = people.getString(1);
                    String firstName = people.getString(2);
                    String lastName = people.getString(3);
                    String middleName = people.getString(5);
                    String suffix = people.getString(6);
                    Log.d("Aria", "Got contact '" + prefix + "' '" + firstName + "' '" + middleName + "' '" + lastName + "' '" + suffix + "'");
                    if ((firstName == null || firstName.isEmpty()) && lastName != null && !lastName.isEmpty() && lastName.contains(" ")) {
                        firstName = lastName.substring(0, lastName.indexOf(32)).trim();
                        lastName = lastName.substring(firstName.length()).trim();
                    }
                    if ((lastName == null || lastName.isEmpty()) && firstName != null && !firstName.isEmpty() && firstName.contains(" ")) {
                        lastName = firstName.substring(firstName.indexOf(32)).trim();
                        firstName = firstName.substring(0, firstName.indexOf(32)).trim();
                    }
                    Log.d("Aria", "  using '" + prefix + "' '" + firstName + "' '" + middleName + "' '" + lastName + "' '" + suffix + "'");
                    if (prefix != null && !prefix.isEmpty()) {
                        sanitiseAndAddUnique(prefix, PREFIXES);
                    }
                    if (firstName != null && !firstName.isEmpty()) {
                        sanitiseAndAddUnique(firstName, FIRST_NAMES);
                    }
                    if (middleName != null && !middleName.isEmpty()) {
                        sanitiseAndAddUnique(middleName, MIDDLE_NAMES);
                    }
                    if (lastName != null && !lastName.isEmpty()) {
                        sanitiseAndAddUnique(lastName, LAST_NAMES);
                    }
                    if (suffix != null && !suffix.isEmpty()) {
                        sanitiseAndAddUnique(suffix, SUFFIXES);
                    }
                    synchronized (CACHED_CONTACTS) {
                        int id = people.getInt(0);
                        if (id >= 0) {
                            CACHED_CONTACTS.add(new CachedContact(id, prefix, firstName, middleName, lastName, suffix));
                        }
                    }
                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        people.close();
    }

    private static void appendNum(StringBuilder sb, String text, int count) {
        while (count > 0) {
            sb.append(text);
            count--;
        }
    }

    private static String parseNumber(String text) {
        String[] parts = text.split(" ");
        StringBuilder ret = new StringBuilder();
        int nextMulti = 1;
        for (String p : parts) {
            try {
                Integer.parseInt(p);
                appendNum(ret, p, nextMulti);
                nextMulti = 1;
            } catch (NumberFormatException e) {
                if (p.contentEquals("oh") || p.contentEquals("zero")) {
                    appendNum(ret, "0", nextMulti);
                    nextMulti = 1;
                } else if (p.contentEquals("hundred")) {
                    appendNum(ret, "00", nextMulti);
                    nextMulti = 1;
                } else if (p.contentEquals("thousand")) {
                    appendNum(ret, "000", nextMulti);
                    nextMulti = 1;
                } else if (p.contentEquals("double")) {
                    nextMulti = 2;
                } else if (p.contentEquals("triple")) {
                    nextMulti = 3;
                } else if (ret.length() == 0 && nextMulti == 1 && p.contentEquals("plus")) {
                    ret.append("+");
                } else {
                    return null;
                }
            }
        }
        return ret.toString();
    }

    private static String getNumberFor(CachedContact contact) {
        Cursor c;
        if (contact.mNumber == null && (c = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"data1"}, "contact_id = ?", new String[]{Integer.toString(contact.mContactId)}, null)) != null) {
            if (c.moveToFirst()) {
                contact.mNumber = c.getString(0).replaceAll("\\s+", "");
            }
            c.close();
        }
        return contact.mNumber;
    }

    private static String[] getNumbersFor(CachedContact contact) {
        if (contact.mNumber == null) {
            Cursor c = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"data1"}, "contact_id = ?", new String[]{Integer.toString(contact.mContactId)}, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            String[] ret = new String[c.getCount()];
            int idx = 0;
            while (true) {
                int idx2 = idx + 1;
                ret[idx] = c.getString(0).replaceAll("\\s+", "");
                if (!c.moveToNext()) {
                    c.close();
                    return ret;
                }
                idx = idx2;
            }
        } else {
            return new String[]{contact.mNumber};
        }
    }

    public static String getNumberFor(String nameToCheck) {
        String number;
        if (nameToCheck == null || nameToCheck.isEmpty()) {
            return null;
        }
        String number2 = parseNumber(nameToCheck);
        if (number2 == null || number2.isEmpty()) {
            String first = null;
            synchronized (FIRST_NAMES) {
                for (String t : FIRST_NAMES) {
                    if (t.contains(nameToCheck) || nameToCheck.contains(t)) {
                        first = t;
                    }
                }
            }
            String last = null;
            synchronized (LAST_NAMES) {
                for (String t2 : LAST_NAMES) {
                    if (t2.contains(nameToCheck) || nameToCheck.contains(t2)) {
                        last = t2;
                    }
                }
            }
            synchronized (CACHED_CONTACTS) {
                Iterator<CachedContact> it = CACHED_CONTACTS.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        number = null;
                        break;
                    }
                    CachedContact c = it.next();
                    if (c.matches(first, last)) {
                        number = getNumberFor(c);
                        break;
                    }
                }
            }
            return number;
        }
        return number2;
    }

    private static void checkNamePart(NamePart part, String nameToCheck, ArrayList<ContactResolver.ResolvedContact> out) {
        String[] numbers;
        ArrayList<String> listToCheck = null;
        switch (part) {
            case PREFIX:
                listToCheck = PREFIXES;
                break;
            case FIRST:
                listToCheck = FIRST_NAMES;
                break;
            case MIDDLE:
                listToCheck = MIDDLE_NAMES;
                break;
            case LAST:
                listToCheck = LAST_NAMES;
                break;
            case SUFFIX:
                listToCheck = SUFFIXES;
                break;
        }
        if (listToCheck != null) {
            synchronized (listToCheck) {
                for (String t : listToCheck) {
                    if (t.contains(nameToCheck) || nameToCheck.contains(t)) {
                        synchronized (CACHED_CONTACTS) {
                            for (CachedContact c : CACHED_CONTACTS) {
                                if (c.matches(part, t)) {
                                    boolean add = true;
                                    Iterator<ContactResolver.ResolvedContact> it = out.iterator();
                                    while (true) {
                                        if (it.hasNext()) {
                                            ContactResolver.ResolvedContact r = it.next();
                                            if (c.matches(r)) {
                                                add = false;
                                                Log.d("Aria", " Possibly duplicate contact match");
                                            }
                                        }
                                    }
                                    if (add && (numbers = getNumbersFor(c)) != null) {
                                        out.add(new ContactResolver.ResolvedContact(c.getDisplayName(), c.getExpandedName(), c.nonMatchingParts(nameToCheck), numbers));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void getNumbersFor(String nameToCheck, ArrayList<ContactResolver.ResolvedContact> out) {
        String[] numbers;
        String number = parseNumber(nameToCheck);
        if (number != null && !number.isEmpty()) {
            out.add(new ContactResolver.ResolvedContact(number, nameToCheck));
            return;
        }
        String[] nameParts = nameToCheck.split(" ");
        if (nameParts != null && nameParts.length != 0) {
            synchronized (CACHED_CONTACTS) {
                ArrayList<CachedContact> tmp = CACHED_CONTACTS;
                int i = 0;
                while (i < nameParts.length) {
                    ArrayList<CachedContact> in = tmp;
                    boolean filter = i != nameParts.length + (-1);
                    tmp = filter ? new ArrayList<>() : null;
                    String t = nameParts[i];
                    for (CachedContact c : in) {
                        if (c.matches(t)) {
                            if (filter) {
                                tmp.add(c);
                            } else {
                                boolean add = true;
                                Iterator<ContactResolver.ResolvedContact> it = out.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    ContactResolver.ResolvedContact r = it.next();
                                    if (c.matches(r)) {
                                        add = false;
                                        Log.d("Aria", " Possibly duplicate contact match");
                                        break;
                                    }
                                }
                                if (add && (numbers = getNumbersFor(c)) != null) {
                                    out.add(new ContactResolver.ResolvedContact(c.getDisplayName(), c.getExpandedName(), c.nonMatchingParts(nameToCheck), numbers));
                                }
                            }
                        }
                    }
                    i++;
                }
            }
        }
    }

    private static CachedContact getContactFor(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor c = mContentResolver.query(uri, new String[]{"_id"}, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                int cId = c.getInt(0);
                synchronized (CACHED_CONTACTS) {
                    for (CachedContact cached : CACHED_CONTACTS) {
                        if (cached.mContactId == cId) {
                            if (cached.mNumber == null) {
                                cached.mNumber = number;
                            }
                            c.close();
                            return cached;
                        }
                    }
                }
            }
            c.close();
        }
        return null;
    }

    public static String getNameFor(String number) {
        if (number == null || number.isEmpty()) {
            return NUMBER_WITHHELD;
        }
        CachedContact contact = null;
        synchronized (CACHED_CONTACTS) {
            Iterator<CachedContact> it = CACHED_CONTACTS.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CachedContact c = it.next();
                if (c.mNumber != null && c.mNumber.contentEquals(number)) {
                    contact = c;
                    break;
                }
            }
        }
        if (contact == null) {
            contact = getContactFor(number);
        }
        if (contact != null) {
            if (contact.mFirstName == null) {
                return contact.mLastName;
            }
            if (contact.mLastName == null) {
                return contact.mFirstName;
            }
            return contact.mFirstName + " " + contact.mLastName;
        }
        return null;
    }

    private static class CachedContact {
        public int mContactId;
        public String mFirstName;
        public String mLastName;
        public String mMiddleName;
        public String mNumber;
        public String mPrefix;
        public String mSuffix;

        public CachedContact(int id, String prefix, String first, String middle, String last, String suffix) {
            this.mContactId = id;
            this.mPrefix = prefix == null ? null : prefix.trim();
            this.mFirstName = first == null ? null : first.trim();
            this.mMiddleName = middle == null ? null : middle.trim();
            this.mLastName = last == null ? null : last.trim();
            this.mSuffix = suffix != null ? suffix.trim() : null;
        }

        public String getDisplayName() {
            return buildFullName(this.mPrefix, this.mFirstName, this.mMiddleName, this.mLastName, this.mSuffix);
        }

        private String buildFullName(String prefix, String first, String middle, String last, String suffix) {
            boolean spc = false;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ContactsCache.NAME_BUILDER_FORMAT.length(); i++) {
                Character ch = Character.valueOf(ContactsCache.NAME_BUILDER_FORMAT.charAt(i));
                switch (ch.charValue()) {
                    case ' ':
                        if (spc) {
                            sb.append(' ');
                        }
                        spc = false;
                        break;
                    case 'F':
                        if (first != null && !first.isEmpty()) {
                            sb.append(first);
                            spc = true;
                        }
                        break;
                    case 'L':
                        if (last != null && !last.isEmpty()) {
                            sb.append(last);
                            spc = true;
                        }
                        break;
                    case Sync.LOGIN_ACTIVITY_CALLBACK /* 77 */:
                        if (middle != null && !middle.isEmpty()) {
                            sb.append(middle);
                            spc = true;
                        }
                        break;
                    case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                        if (prefix != null && !prefix.isEmpty()) {
                            sb.append(prefix);
                            spc = true;
                        }
                        break;
                    case 'S':
                        if (suffix != null && !suffix.isEmpty()) {
                            sb.append(suffix);
                            spc = true;
                        }
                        break;
                }
            }
            String name = sb.toString().trim();
            return name.isEmpty() ? ContactsCache.NAME_UNKNOWN : name;
        }

        public String getExpandedName() {
            return buildFullName(getExpandedName(this.mPrefix), getExpandedName(this.mFirstName), getExpandedName(this.mMiddleName), getExpandedName(this.mLastName), getExpandedName(this.mSuffix));
        }

        private String getExpandedName(String name) {
            return (name == null || name.isEmpty()) ? name : ContactsCache.COMMON_ABBREVS.containsKey(name.toLowerCase(Locale.ENGLISH)) ? (String) ContactsCache.COMMON_ABBREVS.get(name.toLowerCase(Locale.ENGLISH)) : name;
        }

        boolean matches(NamePart part, String name) {
            String toCheck = null;
            switch (part) {
                case PREFIX:
                    toCheck = this.mPrefix;
                    break;
                case FIRST:
                    toCheck = this.mFirstName;
                    break;
                case MIDDLE:
                    toCheck = this.mMiddleName;
                    break;
                case LAST:
                    toCheck = this.mLastName;
                    break;
                case SUFFIX:
                    toCheck = this.mSuffix;
                    break;
            }
            if (toCheck == null) {
                return false;
            }
            return getExpandedName(toCheck).contentEquals(name);
        }

        boolean matches(String part) {
            return matches(NamePart.PREFIX, part) || matches(NamePart.FIRST, part) || matches(NamePart.MIDDLE, part) || matches(NamePart.LAST, part) || matches(NamePart.SUFFIX, part);
        }

        public boolean matches(String first, String last) {
            if (first != null) {
                if (this.mFirstName == null) {
                    return false;
                }
                if (last != null) {
                    return getExpandedName(this.mFirstName).contentEquals(first) && this.mLastName != null && getExpandedName(this.mLastName).contentEquals(last);
                }
                return getExpandedName(this.mFirstName).contentEquals(first);
            }
            if (last != null) {
                return this.mLastName != null && getExpandedName(this.mLastName).contentEquals(last);
            }
            return false;
        }

        public boolean matches(ContactResolver.ResolvedContact contact) {
            return getDisplayName().contentEquals(contact.getDisplayName());
        }

        String[] nonMatchingParts(String matched) {
            String[] ret = new String[5];
            ret[0] = (this.mPrefix == null || !matched.contains(this.mPrefix)) ? this.mPrefix : null;
            ret[1] = (this.mFirstName == null || !matched.contains(this.mFirstName)) ? this.mFirstName : null;
            ret[2] = (this.mMiddleName == null || !matched.contains(this.mMiddleName)) ? this.mMiddleName : null;
            ret[3] = (this.mLastName == null || !matched.contains(this.mLastName)) ? this.mLastName : null;
            ret[4] = (this.mSuffix == null || !matched.contains(this.mSuffix)) ? this.mSuffix : null;
            return ret;
        }

        public String toString() {
            return getDisplayName() + " (" + this.mNumber + ")";
        }
    }
}
