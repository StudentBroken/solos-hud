package com.kopin.solos.vocon;

import android.content.res.Resources;
import android.util.Log;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.solos.RideControl;
import com.kopin.solos.common.SportType;
import com.kopin.solos.core.R;
import com.kopin.solos.notifications.MetricNotifier;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.virtualworkout.VirtualCoach;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes37.dex */
public class InRide extends AppState {
    private static final String TAG = "InRide";
    private final String[] BACK_PREFIXES;
    private final String[] INRIDE_GRAMMAR;
    private final String[] NEXT_PREFIXES;
    private final String[] PAUSE_PREFIXES;
    private final String[] REQ_METRIC;
    private final String REQ_TARGET;
    private final String REQ_UPDATE;
    private final String[] STOP_PREFIXES;
    private final String UPDATE_PREFIX;
    private final String WARN_NO_SCREEN;
    private final String WARN_NO_TARGET;
    private final String WARN_NO_UPDATE;
    private ArrayList<MetricCommand> mMetricCommands;
    private HashMap<String, String> mMetrics;
    private HashMap<String, String> mTargets;

    public InRide(Resources resources) {
        super(TAG, resources.getStringArray(R.array.inride_grammar));
        this.mMetricCommands = new ArrayList<>();
        this.INRIDE_GRAMMAR = resources.getStringArray(R.array.inride_grammar);
        this.PAUSE_PREFIXES = resources.getStringArray(R.array.ride_pause_prefixes);
        this.STOP_PREFIXES = resources.getStringArray(R.array.ride_stop_prefixes);
        this.NEXT_PREFIXES = resources.getStringArray(R.array.ride_next_prefixes);
        this.BACK_PREFIXES = resources.getStringArray(R.array.ride_back_prefixes);
        this.REQ_METRIC = resources.getStringArray(R.array.ride_show_prefixes);
        this.REQ_UPDATE = resources.getString(R.string.ride_request_update_all);
        this.UPDATE_PREFIX = resources.getString(R.string.ride_request_update_prefix);
        this.REQ_TARGET = resources.getString(R.string.ride_request_target);
        this.WARN_NO_SCREEN = resources.getString(R.string.tts_warning_no_data_screen);
        this.WARN_NO_UPDATE = resources.getString(R.string.tts_warning_no_data_update);
        this.WARN_NO_TARGET = resources.getString(R.string.tts_warning_no_data_target);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
        buildGrammar();
    }

    private void buildGrammar() {
        clearDefinitions();
        addDefinition(TAG, this.INRIDE_GRAMMAR);
        this.mMetrics = PageNav.getAvailableMetricsMap();
        if (this.mMetrics.isEmpty()) {
            this.mMetrics = VirtualCoach.getAllMetricsMap();
        }
        this.mMetricCommands.clear();
        for (String key : this.mMetrics.keySet()) {
            this.mMetricCommands.add(new MetricCommand(key, this.mMetrics.get(key)));
        }
        if (!this.mMetrics.isEmpty()) {
            ArrayList<String> filtered = new ArrayList<>();
            String[] commands = (String[]) this.mMetrics.values().toArray(new String[0]);
            for (String cmd : commands) {
                String[] parts = cmd.split("\\|");
                for (String p : parts) {
                    if (!filtered.contains(p)) {
                        filtered.add(p);
                    }
                }
            }
            addDefinition("Metric", (String[]) filtered.toArray(new String[0]));
        } else {
            addDefinition("Metric", new String[]{"<VOID>"});
        }
        this.mTargets = VirtualCoach.getTargetMetrics();
        if (!this.mTargets.isEmpty()) {
            ArrayList<String> filtered2 = new ArrayList<>();
            String[] commands2 = (String[]) this.mTargets.values().toArray(new String[0]);
            for (String cmd2 : commands2) {
                String[] parts2 = cmd2.split("\\|");
                for (String p2 : parts2) {
                    if (!filtered2.contains(p2)) {
                        filtered2.add(p2);
                    }
                }
            }
            addDefinition("Target", (String[]) filtered2.toArray(new String[0]));
        } else {
            addDefinition("Target", new String[]{"<VOID>"});
        }
        addDefinition(SportType.KEY, new String[]{SolosAriaApp.getSportName()});
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        return LiveRide.isActiveRide();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        String[] strArr = this.STOP_PREFIXES;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i < length) {
                if (!cmd.contains(strArr[i])) {
                    i++;
                } else {
                    setAppState("endride");
                    break;
                }
            } else {
                String[] strArr2 = this.PAUSE_PREFIXES;
                int length2 = strArr2.length;
                int i2 = 0;
                while (true) {
                    if (i2 < length2) {
                        if (!cmd.contains(strArr2[i2])) {
                            i2++;
                        } else {
                            RideControl.pause();
                            break;
                        }
                    } else {
                        String[] strArr3 = this.NEXT_PREFIXES;
                        int length3 = strArr3.length;
                        int i3 = 0;
                        while (true) {
                            if (i3 < length3) {
                                if (!cmd.contains(strArr3[i3])) {
                                    i3++;
                                } else {
                                    PageNav.nextPage();
                                    break;
                                }
                            } else {
                                String[] strArr4 = this.BACK_PREFIXES;
                                int length4 = strArr4.length;
                                int i4 = 0;
                                while (true) {
                                    if (i4 < length4) {
                                        if (!cmd.contains(strArr4[i4])) {
                                            i4++;
                                        } else {
                                            PageNav.prevPage();
                                            break;
                                        }
                                    } else {
                                        String[] strArr5 = this.REQ_METRIC;
                                        int length5 = strArr5.length;
                                        int i5 = 0;
                                        while (true) {
                                            if (i5 < length5) {
                                                String t = strArr5[i5];
                                                if (!cmd.startsWith(t)) {
                                                    i5++;
                                                } else {
                                                    String metricName = cmd.substring(t.length()).trim();
                                                    String metric = findMetricForCommand(metricName);
                                                    if (metric != null) {
                                                        if (!PageNav.isPageAvailable(metric)) {
                                                            sayText(AriaTTS.SayPriority.VERBOSE, String.format(this.WARN_NO_SCREEN, metricName));
                                                        } else {
                                                            PageNav.showPage(metric);
                                                        }
                                                    } else {
                                                        Log.d(TAG, "Metric Not found");
                                                    }
                                                }
                                            } else if (this.REQ_UPDATE.contentEquals(cmd)) {
                                                MetricNotifier.stop(true);
                                                MetricNotifier.next();
                                            } else if (cmd.startsWith(this.UPDATE_PREFIX)) {
                                                String metricName2 = cmd.substring(this.UPDATE_PREFIX.length()).trim();
                                                String metric2 = findMetricForCommand(metricName2);
                                                if (metric2 != null) {
                                                    if (!PageNav.isPageAvailable(metric2)) {
                                                        sayText(AriaTTS.SayPriority.VERBOSE, String.format(this.WARN_NO_UPDATE, metricName2));
                                                    } else {
                                                        MetricNotifier.update(metric2);
                                                    }
                                                }
                                            } else if (cmd.startsWith(this.REQ_TARGET)) {
                                                String metricName3 = cmd.substring(this.REQ_TARGET.length()).trim();
                                                String target = null;
                                                Iterator<String> it = this.mTargets.keySet().iterator();
                                                while (true) {
                                                    if (!it.hasNext()) {
                                                        break;
                                                    }
                                                    String key = it.next();
                                                    String command = this.mTargets.get(key);
                                                    if (command.contains(metricName3)) {
                                                        target = key;
                                                        break;
                                                    }
                                                }
                                                if (target != null) {
                                                    if (!VirtualCoach.hasTarget(target)) {
                                                        sayText(AriaTTS.SayPriority.VERBOSE, String.format(this.WARN_NO_TARGET, metricName3));
                                                    } else if (!VirtualCoach.hasLiveData(target)) {
                                                        sayText(AriaTTS.SayPriority.VERBOSE, String.format(this.WARN_NO_UPDATE, metricName3));
                                                    } else {
                                                        VirtualCoach.updateTarget(target);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    String findMetricForCommand(String command) {
        ArrayList<MetricCommand> matches = (ArrayList) this.mMetricCommands.clone();
        String[] keywords = command.split(" ");
        for (String key : keywords) {
            ArrayList<MetricCommand> nomatch = new ArrayList<>();
            for (MetricCommand cmd : matches) {
                if (!cmd.matches(key)) {
                    nomatch.add(cmd);
                }
            }
            matches.removeAll(nomatch);
        }
        MetricCommand bestMatch = null;
        int bestScore = 0;
        for (MetricCommand match : matches) {
            int score = match.getMatchScore(keywords);
            if (score > bestScore) {
                bestMatch = match;
                bestScore = score;
            }
        }
        if (bestMatch != null) {
            return bestMatch.getKey();
        }
        return null;
    }

    private static class MetricCommand {
        private final String[] mAlternates;
        private final String mKey;
        private final String[] mWords;

        MetricCommand(String key, String commands) {
            this.mKey = key;
            this.mAlternates = commands.split("\\|");
            this.mWords = this.mAlternates[0].split(" ");
        }

        String getKey() {
            return this.mKey;
        }

        boolean matches(String keyWord) {
            return this.mAlternates[0].contains(keyWord);
        }

        int getMatchScore(String[] keywords) {
            if (keywords.length > this.mWords.length) {
                return (this.mWords.length * 100) / keywords.length;
            }
            if (keywords.length < this.mWords.length) {
                return (keywords.length * 100) / this.mWords.length;
            }
            return 100;
        }

        public String toString() {
            return this.mAlternates[0];
        }
    }
}
