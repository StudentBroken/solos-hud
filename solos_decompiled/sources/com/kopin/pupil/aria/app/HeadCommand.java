package com.kopin.pupil.aria.app;

import com.kopin.pupil.vocon.Grammar;

/* JADX INFO: loaded from: classes21.dex */
public class HeadCommand extends Grammar {
    public static final HeadCommand NONE = new HeadCommand("<VOID>", null) { // from class: com.kopin.pupil.aria.app.HeadCommand.1
        @Override // com.kopin.pupil.aria.app.HeadCommand
        public boolean matches(String cmd) {
            return false;
        }
    };
    private final String[] mAlternates;

    public HeadCommand(String cmd, String[] alternates) {
        super(cmd);
        this.mAlternates = alternates;
    }

    public HeadCommand(String cmd) {
        this(cmd, buildAlternates(cmd));
    }

    private static String[] buildAlternates(String cmd) {
        int s = cmd.indexOf(91);
        if (s == -1) {
            return new String[]{cmd};
        }
        StringBuilder alt1 = new StringBuilder();
        StringBuilder alt2 = new StringBuilder();
        alt1.append(cmd.substring(0, s).trim());
        alt2.append(cmd.substring(0, s).trim()).append(" ");
        int e = cmd.indexOf(93, s);
        alt2.append(cmd.substring(s + 1, e).trim()).append(" ");
        alt1.append(" ");
        alt1.append(cmd.substring(e + 1).trim());
        alt2.append(cmd.substring(e + 1).trim());
        return new String[]{alt1.toString(), alt2.toString()};
    }

    public boolean matches(String cmd) {
        for (String t : this.mAlternates) {
            if (t.contentEquals(cmd)) {
                return true;
            }
        }
        return false;
    }
}
