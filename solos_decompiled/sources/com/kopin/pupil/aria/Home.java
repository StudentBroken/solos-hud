package com.kopin.pupil.aria;

import android.content.Context;
import com.kopin.accessory.packets.ActionType;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.AriaController;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.app.BaseAriaApp;
import com.kopin.pupil.aria.app.CommonApps;
import com.kopin.pupil.aria.app.HeadCommand;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.aria.contacts.Contacts;
import com.kopin.pupil.aria.dialler.Dialler;
import com.kopin.pupil.aria.messages.Messages;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.pupil.aria.tts.ConversationPoint;
import com.kopin.pupil.vocon.Grammar;
import java.util.HashMap;

/* JADX INFO: loaded from: classes43.dex */
public class Home extends BaseAriaApp {
    public static final String APP_NAME = "home";
    private static final String HOME_ID = "Home";
    private static ConversationPoint WAKE_TTS;
    private final String[] MORE_COMMANDS;
    private final String[] SLEEP_COMMANDS;
    private final BaseAriaApp.AriaAppHost mAppHost;
    private HashMap<String, BaseAriaApp> mApps;
    private Contacts mContactsApp;
    private final Context mContext;
    private BaseAriaApp mCurrentApp;
    private long mLastWarn;
    private final BaseAriaApp.AriaAppHost mParentAppHost;
    private final PupilSpeechRecognizer.SpeechConfig mParentSpeechHost;
    protected final VoConPolicy mPolicy;
    private boolean mUseHeadCommand;
    private int mWarnCount;
    private static final String[] WAKE_GRAMMAR = {"aria"};
    private static String mUsersName = null;

    interface CommandHandlerObserver {
        void onCommandHandled(String str, String str2);
    }

    public Home(Context context, String[] overrideCommands, BaseAriaApp.AriaAppHost appHost, PupilSpeechRecognizer.SpeechConfig speechHost, VoConPolicy policy, String usersname) {
        super(HOME_ID, overrideCommands == null ? context.getResources().getStringArray(R.array.home_grammar) : overrideCommands, null, speechHost);
        this.mApps = new HashMap<>();
        this.mWarnCount = 3;
        this.mUseHeadCommand = true;
        this.mAppHost = new BaseAriaApp.AriaAppHost() { // from class: com.kopin.pupil.aria.Home.1
            @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
            public void requestWake() {
                if (Home.this.isEnabled()) {
                    Home.this.mParentAppHost.requestWake();
                }
            }

            @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
            public void onAppStart(String app) {
                Home.this.mParentAppHost.onAppStart(app);
            }

            @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
            public void onAppFinished() {
                Home.this.exitApp(Config.SLEEP_ON_APP_FINISHED);
                Home.this.grammarChanged(true);
            }

            @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
            public void requestAppState(BaseAriaApp app, String state) {
                if (Home.this.isEnabled()) {
                    Home.this.mParentAppHost.requestWake();
                    Home.this.exitApp(false);
                    app.onStart(state);
                    Home.this.mCurrentApp = app;
                    Home.this.grammarChanged(false);
                    Home.this.mParentAppHost.onAppStart(Home.this.nameForApp(app));
                }
            }

            @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
            public void onAppState(String app, String state, String description) {
                Home.this.mParentAppHost.onAppState(app, state, description);
            }

            @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
            public boolean isIdle() {
                return Home.this.mCurrentApp == null;
            }

            @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
            public String getSpeechSubstitution(String subs) {
                if (subs.contentEquals("USER")) {
                    return Home.mUsersName;
                }
                return null;
            }
        };
        this.SLEEP_COMMANDS = context.getResources().getStringArray(R.array.sleep_commands);
        this.MORE_COMMANDS = context.getResources().getStringArray(R.array.more_commands);
        WAKE_TTS = ConversationPoint.build(context, HOME_ID, R.array.tts_wake_off, R.array.tts_wake_silent, R.array.tts_wake_once, R.array.tts_wake_terse, R.array.tts_wake_hint, R.array.tts_wake_verbose);
        this.mContext = context;
        this.mPolicy = policy;
        this.mParentAppHost = appHost;
        this.mParentSpeechHost = speechHost;
        addState("idle", new HomeActive(context.getResources().getStringArray(R.array.home_grammar)));
        CommonApps.init(context);
        this.mApps.put(Dialler.APP_NAME, new Dialler(context, this.mAppHost, this.mParentSpeechHost));
        this.mContactsApp = new Contacts(context, this.mAppHost, this.mParentSpeechHost);
        mUsersName = usersname;
    }

    public void setUsersName(String name) {
        mUsersName = name;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public HeadCommand getHeadCommand() {
        return HeadCommand.NONE;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public String[] getKeywords() {
        return new String[0];
    }

    public void setKeywordMode(boolean useHeadCommand) {
        this.mUseHeadCommand = useHeadCommand;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public boolean isEnabled() {
        return this.mPolicy.isEnabled();
    }

    public void addApp(String name, AriaController.AriaAppFactory app) {
        this.mApps.put(name, app.createApp(this.mContext, this.mAppHost, this.mParentSpeechHost, this.mPolicy));
    }

    public boolean isAppEnabled(String tag) {
        if (tag.contentEquals("home")) {
            return true;
        }
        BaseAriaApp app = this.mApps.get(tag);
        if (app != null) {
            return app.isEnabled();
        }
        return false;
    }

    public void enableApp(String tag) {
        BaseAriaApp app = this.mApps.get(tag);
        if (app != null) {
            app.enable();
        }
    }

    public String getAppStatus(String tag) {
        if (tag.contentEquals("home")) {
            return getStatus();
        }
        BaseAriaApp app = this.mApps.get(tag);
        return app != null ? app.getStatus() : "not loaded";
    }

    String getAppStatus() {
        return this.mCurrentApp == null ? getStatus() : this.mCurrentApp.getStatus();
    }

    protected String getCurrentAppState() {
        return this.mCurrentApp == null ? getStatus() : this.mCurrentApp.getStatus();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
    }

    public void onStart(boolean userAction) {
        if (userAction) {
            this.mLastWarn = System.currentTimeMillis();
            sayText(mUsersName == null ? AriaTTS.SayPriority.HINT : AriaTTS.SayPriority.VERBOSE, WAKE_TTS);
            PupilDevice.sendAction(ActionType.PLAY_LISTENING_CHIME);
        }
        onStart();
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public void onStart() {
        super.onStart();
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public void onStop() {
        super.onStop();
        for (String a : this.mApps.keySet()) {
            BaseAriaApp app = this.mApps.get(a);
            if (app.isEnabled()) {
                app.onStop();
            }
        }
        this.mCurrentApp = null;
    }

    public void onInactivity() {
        exitApp(Config.SLEEP_ON_INACTIVITY);
        grammarChanged(true);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public void onIdle() {
        super.onIdle();
        for (String a : this.mApps.keySet()) {
            BaseAriaApp app = this.mApps.get(a);
            if (app.isEnabled() && this.mUseHeadCommand) {
                app.onIdle();
            }
            if (this.mCurrentApp != null) {
                return;
            }
        }
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public boolean onButtonPress(boolean isShort) {
        return this.mCurrentApp != null ? this.mCurrentApp.onButtonPress(isShort) : super.onButtonPress(isShort);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.tts.AriaTTS.TTSListener
    public String getSpeechSubstitution(String tag, String subs) {
        return subs.contentEquals("USER") ? mUsersName : super.getSpeechSubstitution(tag, subs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public void setAppState(String name) {
        if (isEnabled()) {
            super.setAppState(name);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public void requestState(String name) {
        if (isEnabled()) {
            this.mParentAppHost.requestWake();
            if (this.mCurrentApp != null) {
                exitApp(false);
            }
            onStart(name);
            grammarChanged(false);
            this.mParentAppHost.onAppStart(nameForApp(null));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public void appFinished() {
        exitApp(Config.SLEEP_ON_APP_FINISHED);
        grammarChanged(true);
    }

    private void enterApp(String name) {
        for (String a : this.mApps.keySet()) {
            BaseAriaApp app = this.mApps.get(a);
            if (!name.contentEquals(a)) {
                app.onStop();
            } else {
                this.mCurrentApp = app;
            }
        }
        if (this.mCurrentApp != null) {
            this.mParentAppHost.onAppStart(name);
        }
    }

    private void startApp(String name) {
        for (String a : this.mApps.keySet()) {
            BaseAriaApp app = this.mApps.get(a);
            if (name.contentEquals(a)) {
                this.mCurrentApp = app;
                app.onStart();
            }
        }
        if (this.mCurrentApp != null) {
            this.mParentAppHost.onAppStart(name);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitApp(boolean andSleep) {
        if (this.mCurrentApp != null) {
            this.mCurrentApp.onStop();
        }
        this.mCurrentApp = null;
        if (shouldIdle()) {
            if (andSleep) {
                this.mParentAppHost.onAppFinished();
                return;
            } else {
                this.mParentAppHost.onAppStart(null);
                return;
            }
        }
        if (!andSleep) {
            onIdle();
        }
    }

    protected boolean shouldIdle() {
        return true;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public boolean needsToWho() {
        if (this.mCurrentApp != null) {
            return this.mCurrentApp.needsToWho();
        }
        boolean includeContacts = false;
        if (this.mUseHeadCommand) {
            return false;
        }
        for (String id : this.mApps.keySet()) {
            BaseAriaApp app = this.mApps.get(id);
            if (app.isEnabled()) {
                includeContacts |= app.needsToWho();
            }
        }
        return includeContacts;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.vocon.Grammar
    public String compile() {
        StringBuilder sb = new StringBuilder(this.mCurrentApp != null ? "<Sleep>" : super.compile());
        if (this.mCurrentApp != null) {
            sb.append('|').append(this.mCurrentApp.compile()).append(";\n");
            sb.append(this.mCurrentApp.compileDefinitions());
        } else {
            for (String id : this.mApps.keySet()) {
                BaseAriaApp app = this.mApps.get(id);
                if (app.isEnabled()) {
                    sb.append('|').append(this.mUseHeadCommand ? app.getHeadCommand().compile() : app.compile());
                }
            }
            sb.append(";\n");
            if (!this.mUseHeadCommand) {
                for (String id2 : this.mApps.keySet()) {
                    BaseAriaApp app2 = this.mApps.get(id2);
                    if (app2.isEnabled()) {
                        sb.append(app2.compileDefinitions());
                    }
                }
            }
        }
        if (needsToWho()) {
            sb.append(this.mContactsApp.compileToWho());
        }
        sb.append(super.compileDefinitions());
        if (this.mCurrentApp != null) {
            sb.append("<Sleep>:home|cancel|switch off|main menu;");
        }
        sb.append("<Unused>:<VOID>");
        return sb.toString();
    }

    @Override // com.kopin.pupil.vocon.Grammar
    public Grammar compile(String id) {
        return super.compile(id);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.vocon.Grammar
    public boolean needsDictation() {
        if (this.mCurrentApp == null) {
            return false;
        }
        return this.mCurrentApp.needsDictation();
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        if (this.mCurrentApp == null) {
            return true;
        }
        return this.mCurrentApp.holdIdle();
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onCommand(int r11, java.lang.String r12, com.kopin.pupil.aria.Home.CommandHandlerObserver r13) {
        /*
            r10 = this;
            r4 = 0
            r9 = 1
            com.kopin.accessory.packets.ActionType r3 = com.kopin.accessory.packets.ActionType.PLAY_CHIRP
            com.kopin.pupil.PupilDevice.sendAction(r3)
            java.lang.String[] r5 = r10.SLEEP_COMMANDS
            int r6 = r5.length
            r3 = r4
        Lb:
            if (r3 >= r6) goto L21
            r2 = r5[r3]
            boolean r7 = r12.contentEquals(r2)
            if (r7 == 0) goto L1e
            java.lang.String r3 = "home"
            r13.onCommandHandled(r3, r12)
            r10.exitApp(r9)
        L1d:
            return r9
        L1e:
            int r3 = r3 + 1
            goto Lb
        L21:
            java.lang.String[] r5 = r10.MORE_COMMANDS
            int r6 = r5.length
            r3 = r4
        L25:
            if (r3 >= r6) goto L3b
            r2 = r5[r3]
            boolean r7 = r12.contentEquals(r2)
            if (r7 == 0) goto L38
            java.lang.String r3 = "home"
            r13.onCommandHandled(r3, r12)
            r10.exitApp(r4)
            goto L1d
        L38:
            int r3 = r3 + 1
            goto L25
        L3b:
            com.kopin.pupil.aria.app.BaseAriaApp r3 = r10.mCurrentApp
            if (r3 != 0) goto L8c
            java.util.HashMap<java.lang.String, com.kopin.pupil.aria.app.BaseAriaApp> r3 = r10.mApps
            java.util.Set r3 = r3.keySet()
            java.util.Iterator r5 = r3.iterator()
        L49:
            boolean r3 = r5.hasNext()
            if (r3 == 0) goto L8c
            java.lang.Object r0 = r5.next()
            java.lang.String r0 = (java.lang.String) r0
            java.util.HashMap<java.lang.String, com.kopin.pupil.aria.app.BaseAriaApp> r3 = r10.mApps
            java.lang.Object r1 = r3.get(r0)
            com.kopin.pupil.aria.app.BaseAriaApp r1 = (com.kopin.pupil.aria.app.BaseAriaApp) r1
            boolean r3 = r10.mUseHeadCommand
            if (r3 == 0) goto L72
            com.kopin.pupil.aria.app.HeadCommand r3 = r1.getHeadCommand()
            boolean r3 = r3.matches(r12)
            if (r3 == 0) goto L49
            r13.onCommandHandled(r0, r12)
            r10.startApp(r0)
            goto L1d
        L72:
            java.lang.String[] r6 = r1.getKeywords()
            int r7 = r6.length
            r3 = r4
        L78:
            if (r3 >= r7) goto L49
            r2 = r6[r3]
            boolean r8 = r12.contains(r2)
            if (r8 == 0) goto L89
            r13.onCommandHandled(r0, r12)
            r10.enterApp(r0)
            goto L49
        L89:
            int r3 = r3 + 1
            goto L78
        L8c:
            com.kopin.pupil.aria.app.BaseAriaApp r3 = r10.mCurrentApp
            if (r3 == 0) goto La8
            com.kopin.pupil.aria.app.BaseAriaApp r3 = r10.mCurrentApp
            java.lang.String r3 = r10.nameForApp(r3)
            r13.onCommandHandled(r3, r12)
            com.kopin.pupil.aria.app.BaseAriaApp r3 = r10.mCurrentApp
            boolean r3 = r3.onCommand(r11, r12)
            if (r3 != 0) goto L1d
            boolean r3 = com.kopin.pupil.aria.Config.SLEEP_ON_APP_FINISHED
            r10.exitApp(r3)
            goto L1d
        La8:
            java.lang.String r3 = r10.nameForApp(r10)
            r13.onCommandHandled(r3, r12)
            boolean r3 = r10.onCommand(r11, r12)
            if (r3 != 0) goto L1d
            boolean r3 = com.kopin.pupil.aria.Config.SLEEP_ON_APP_FINISHED
            r10.exitApp(r3)
            goto L1d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kopin.pupil.aria.Home.onCommand(int, java.lang.String, com.kopin.pupil.aria.Home$CommandHandlerObserver):boolean");
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean onASRError(PupilSpeechRecognizer.ASRErrors code) {
        if (this.mCurrentApp != null) {
            return this.mCurrentApp.onASRError(code);
        }
        return false;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean onASRWarn(PupilSpeechRecognizer.ASRWarnings code) {
        if (this.mCurrentApp != null) {
            return this.mCurrentApp.onASRWarn(code);
        }
        long now = System.currentTimeMillis();
        if (now - this.mLastWarn < 2000) {
            return false;
        }
        int i = this.mWarnCount;
        this.mWarnCount = i - 1;
        if (i != 0) {
            return false;
        }
        this.mLastWarn = now;
        return super.onASRWarn(code);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String nameForApp(BaseAriaApp app) {
        if (app == this) {
            return "home";
        }
        if (app instanceof Contacts) {
            return ContactResolver.CONTACTS_APP_NAME;
        }
        if (app instanceof Messages) {
            return Messages.APP_NAME;
        }
        if (app instanceof Dialler) {
            return Dialler.APP_NAME;
        }
        return "home";
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public void onSilence() {
        if (this.mCurrentApp != null) {
            this.mCurrentApp.onSilence();
            return;
        }
        for (String a : this.mApps.keySet()) {
            BaseAriaApp app = this.mApps.get(a);
            if (app.requestAudioIn()) {
                app.onSilence();
            }
        }
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public void onAudioData(byte[] buf) {
        for (String a : this.mApps.keySet()) {
            BaseAriaApp app = this.mApps.get(a);
            if (app.requestAudioIn()) {
                app.onAudioData(buf);
            }
        }
    }

    private class HomeActive extends AppState {
        public HomeActive(String[] grammar) {
            super("active", grammar);
        }
    }
}
