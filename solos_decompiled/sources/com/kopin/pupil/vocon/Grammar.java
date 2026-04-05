package com.kopin.pupil.vocon;

import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class Grammar {
    private final String mCommand;
    private HashMap<String, Grammar> mDefinitions = new HashMap<>();

    public Grammar(String cmd) {
        this.mCommand = cmd;
    }

    public Grammar(String id, String[] cmds) {
        this.mCommand = "<" + id + ">";
        StringBuilder sb = new StringBuilder(this.mCommand).append(":").append(cmds[0]);
        for (int i = 1; i < cmds.length; i++) {
            sb.append('|').append(cmds[i]);
        }
        this.mDefinitions.put(id, new Grammar(sb.append(';').toString()));
    }

    protected void addDefinition(String name, String[] cmds) {
        StringBuilder sb = new StringBuilder().append("<").append(name).append("> : ").append(cmds[0]);
        for (int i = 1; i < cmds.length; i++) {
            sb.append('|').append(cmds[i]);
        }
        this.mDefinitions.put(name, new Grammar(sb.append(';').toString()));
    }

    protected void clearDefinitions() {
        this.mDefinitions.clear();
    }

    public String compile() {
        return this.mCommand;
    }

    public String compileDefinitions() {
        StringBuilder sb = new StringBuilder();
        for (String id : this.mDefinitions.keySet()) {
            Grammar def = this.mDefinitions.get(id);
            sb.append(def.compile()).append('\n');
        }
        return sb.toString();
    }

    public boolean needsDictation() {
        return false;
    }

    public Grammar compile(String id) {
        return this.mDefinitions.get(id);
    }

    public boolean hasDefinitions() {
        return !this.mDefinitions.isEmpty();
    }

    public static Grammar createSimpleCommand(String cmd) {
        return new Grammar(cmd);
    }

    public static Grammar createMultiCommand(String id, String... cmds) {
        Grammar self = new Grammar("<" + id + ">");
        StringBuilder sb = new StringBuilder(self.mCommand).append(":").append(cmds[0]);
        for (int i = 1; i < cmds.length; i++) {
            sb.append('|').append(cmds[i]);
        }
        self.mDefinitions.put(id, new Grammar(sb.append(';').toString()));
        return self;
    }
}
