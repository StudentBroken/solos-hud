package com.kopin.pupil.aria.contacts;

import android.content.Context;
import android.database.ContentObserver;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.R;
import com.kopin.pupil.aria.app.BaseAriaApp;
import com.kopin.pupil.aria.app.HeadCommand;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.vocon.Grammar;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes43.dex */
public class Contacts extends BaseAriaApp {
    static int CHOSEN_ALTERNATE = 0;
    private static final String CONTACTS_ID = "Contacts";
    static String NAME_HEARD;
    private static BaseAriaApp.AriaAppHost mParent;
    private final String COMMON_NUMBERS_DOUBLE;
    private final String COMMON_NUMBERS_HUNDRED;
    private final String COMMON_NUMBERS_PLUS;
    private final String COMMON_NUMBERS_QUADRUPLE;
    private final String COMMON_NUMBERS_TENTY;
    private final String COMMON_NUMBERS_TENTY_SIX;
    private final String COMMON_NUMBERS_THOUSAND;
    private final String COMMON_NUMBERS_TRIPLE;
    private final ToWho GRAMMAR_TOWHO;
    private ContactResolver.OnResolvedListener mCallback;
    private final ContentObserver mContentObserver;
    private static final String[] CONTACTS_GRAMMAR = {"!optional (to|for) <ToWho>"};
    static ArrayList<ContactResolver.ResolvedContact> RESOLVED_CONTACT = new ArrayList<>();
    private static final BaseAriaApp.AriaAppHost mContactsHost = new BaseAriaApp.AriaAppHost() { // from class: com.kopin.pupil.aria.contacts.Contacts.1
        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void requestWake() {
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void requestAppState(BaseAriaApp app, String state) {
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void onAppState(String app, String state, String description) {
            Contacts.mParent.onAppState(app, state, description);
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void onAppStart(String app) {
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void onAppFinished() {
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public boolean isIdle() {
            return Contacts.mParent.isIdle();
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public String getSpeechSubstitution(String subs) {
            return Contacts.mParent.getSpeechSubstitution(subs);
        }
    };

    public Contacts(Context context, BaseAriaApp.AriaAppHost parent, PupilSpeechRecognizer.SpeechConfig speech) {
        super(CONTACTS_ID, CONTACTS_GRAMMAR, mContactsHost, speech);
        this.mContentObserver = new ContentObserver(null) { // from class: com.kopin.pupil.aria.contacts.Contacts.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                ContactsCache.reset();
                Contacts.this.GRAMMAR_TOWHO.reload();
                Contacts.this.grammarChanged(true);
            }
        };
        ContactsCache.init(context, this.mContentObserver);
        this.GRAMMAR_TOWHO = new ToWho();
        this.COMMON_NUMBERS_PLUS = context.getString(R.string.common_numbers_plus);
        this.COMMON_NUMBERS_HUNDRED = context.getString(R.string.common_numbers_hundred);
        this.COMMON_NUMBERS_THOUSAND = context.getString(R.string.common_numbers_thousand);
        this.COMMON_NUMBERS_DOUBLE = context.getString(R.string.common_numbers_double);
        this.COMMON_NUMBERS_TRIPLE = context.getString(R.string.common_numbers_triple);
        this.COMMON_NUMBERS_QUADRUPLE = context.getString(R.string.common_numbers_quadruple);
        this.COMMON_NUMBERS_TENTY = context.getString(R.string.common_numbers_tenty);
        this.COMMON_NUMBERS_TENTY_SIX = context.getString(R.string.common_numbers_tenty_six);
        mParent = parent;
        GetToWho.init(this, mContactsHost);
        addState(ContactResolver.CONTACTS_GET_TO_WHO, new AskToWho(context));
        addState("which", new SelectWho(context));
        addState("pick", new PickNumber(context));
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public HeadCommand getHeadCommand() {
        return HeadCommand.NONE;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public String[] getKeywords() {
        return null;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public boolean needsToWho() {
        return RESOLVED_CONTACT.isEmpty();
    }

    public String compileToWho() {
        return this.GRAMMAR_TOWHO.compileDefinitions();
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    protected void setExtra(Object extra) {
        if (extra instanceof ContactResolver.OnResolvedListener) {
            this.mCallback = (ContactResolver.OnResolvedListener) extra;
        }
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    protected void appFinished() {
        if (this.mCallback != null) {
            if (!RESOLVED_CONTACT.isEmpty() && !RESOLVED_CONTACT.get(0).hasAlternates()) {
                this.mCallback.onNumberResolved(RESOLVED_CONTACT.get(0));
            } else {
                this.mCallback.onNumberResolved(null);
            }
        }
        this.mCallback = null;
        super.appFinished();
    }

    private String numberSaidAs(String number) {
        if (number != null && !number.isEmpty()) {
            if (!this.COMMON_NUMBERS_PLUS.isEmpty()) {
                number = number.replace("+", this.COMMON_NUMBERS_PLUS);
            }
            String number2 = number.replaceAll("(\\d{1})", " $1");
            if (!this.COMMON_NUMBERS_THOUSAND.isEmpty()) {
                number2 = number2.replace("0 0 0", this.COMMON_NUMBERS_THOUSAND);
            }
            if (!this.COMMON_NUMBERS_HUNDRED.isEmpty()) {
                number2 = number2.replace("0 0", this.COMMON_NUMBERS_HUNDRED);
            }
            if (!this.COMMON_NUMBERS_QUADRUPLE.isEmpty()) {
                number2 = number2.replaceAll("( \\d)\\1{3}", this.COMMON_NUMBERS_QUADRUPLE);
            }
            if (!this.COMMON_NUMBERS_TRIPLE.isEmpty()) {
                number2 = number2.replaceAll("( \\d)\\1{2}", this.COMMON_NUMBERS_TRIPLE);
            }
            if (!this.COMMON_NUMBERS_DOUBLE.isEmpty()) {
                number2 = number2.replaceAll("( \\d)\\1{1}", this.COMMON_NUMBERS_DOUBLE);
            }
            if (!this.COMMON_NUMBERS_TENTY.isEmpty()) {
                number2 = number2.replaceAll("(\\d{1}) (\\d{1})", this.COMMON_NUMBERS_TENTY);
            }
            if (!this.COMMON_NUMBERS_TENTY_SIX.isEmpty()) {
                return number2.replaceAll("(\\d{1}) (\\d{1}) (\\d{1}) (\\d{1}) (\\d{1}) (\\d{1})\\s*\\z", this.COMMON_NUMBERS_TENTY_SIX);
            }
            return number2;
        }
        return number;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.tts.AriaTTS.TTSListener
    public String getSpeechSubstitution(String tag, String subs) {
        String str;
        String[] numbers;
        str = null;
        numbers = RESOLVED_CONTACT.isEmpty() ? null : RESOLVED_CONTACT.get(0).getAlternates();
        switch (subs) {
            case "NAME1":
                if (RESOLVED_CONTACT.isEmpty()) {
                    return null;
                }
                return RESOLVED_CONTACT.get(0).getDisplayName();
            case "NAME2":
                if (RESOLVED_CONTACT.size() > 1) {
                    return RESOLVED_CONTACT.get(1).getDisplayName();
                }
                return null;
            case "NAME3":
                if (RESOLVED_CONTACT.size() > 2) {
                    return RESOLVED_CONTACT.get(2).getDisplayName();
                }
                return null;
            case "NUMBER1":
                if (numbers == null || numbers.length == 0) {
                    return null;
                }
                return numbers[0];
            case "NUMBER2":
                if (numbers == null || numbers.length < 1) {
                    return null;
                }
                return numbers[1];
            case "NUMBER3":
                if (numbers == null || numbers.length < 2) {
                    return null;
                }
                return numbers[2];
            case "NAME":
                return NAME_HEARD;
            case "NUMBER":
                if (numbers != null && numbers.length >= CHOSEN_ALTERNATE) {
                    str = numbers[CHOSEN_ALTERNATE];
                }
                return numberSaidAs(str);
            default:
                return super.getSpeechSubstitution(tag, subs);
        }
    }

    private static class ToWho extends Grammar {
        private static final String[] VOID_DEF = {"<VOID>"};

        ToWho() {
            super("ToWho");
            init();
        }

        private void addNamesOrVoid(String defName, String[] names) {
            if (names == null || names.length == 0) {
                names = VOID_DEF;
            }
            addDefinition(defName, names);
        }

        private void init() {
            addDefinition("ToWho", new String[]{"<Names>", "<NamesReverse>", "<NamesChinese>"});
            addDefinition("Names", new String[]{"[<NamePrefix>] [<FirstName>] [<MiddleName>] [<LastName>] [<NameSuffix>]"});
            addDefinition("NamesReverse", new String[]{"[<LastName>] [<FirstName>] [<MiddleName>]"});
            addDefinition("NamesChinese", new String[]{"[<LastName>] [<MiddleName> <FirstName>]"});
            addNamesOrVoid("NamePrefix", ContactsCache.getPrefixes());
            addNamesOrVoid("FirstName", ContactsCache.getFirstNames());
            addNamesOrVoid("MiddleName", ContactsCache.getMiddleNames());
            addNamesOrVoid("LastName", ContactsCache.getLastNames());
            addNamesOrVoid("NameSuffix", ContactsCache.getSuffixes());
        }

        void reload() {
            clearDefinitions();
            init();
        }
    }
}
