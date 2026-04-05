package com.kopin.pupil.aria.contacts;

import android.content.Context;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.app.BaseAriaApp;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.vocon.Grammar;

/* JADX INFO: loaded from: classes21.dex */
public abstract class GetToWho extends AppState implements ContactResolver.OnResolvedListener {
    private static final String[] CALLWHO_GRAMMAR = {"<ToWho>"};
    protected static BaseAriaApp mContactsApp;
    protected static BaseAriaApp.AriaAppHost mContactsHost;

    static void init(BaseAriaApp app, BaseAriaApp.AriaAppHost host) {
        mContactsApp = app;
        mContactsHost = host;
    }

    public GetToWho(Context context, String id) {
        super(id, CALLWHO_GRAMMAR);
    }

    @Override // com.kopin.pupil.vocon.Grammar
    public String compile() {
        return mContactsApp.compile();
    }

    @Override // com.kopin.pupil.vocon.Grammar
    public Grammar compile(String id) {
        return mContactsApp.compile(id);
    }

    @Override // com.kopin.pupil.vocon.Grammar
    public String compileDefinitions() {
        return mContactsApp.compileDefinitions();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
        mContactsApp.onStart(ContactResolver.CONTACTS_GET_TO_WHO, this);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onExit() {
        mContactsApp.onStop();
        super.onExit();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        return mContactsApp.holdIdle();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        mContactsApp.onCommandResult(cmd);
        return true;
    }
}
