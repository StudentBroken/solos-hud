package com.kopin.solos.navigation;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.Template;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.util.PaceUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class Navigation extends Template<Long> {
    private static final String ATTRIB_CONTENT = "content";
    private static final String IMG_DESTINATION_REACHED = "destination_reached_icon";
    private static final String IMG_DISTANCE_COMPASS = "distance_compass";
    private static final String IMG_NAV_PATH = "navigation_path";
    private static final String IMG_SWERVE_ARROW = "swervearrow";
    private static final int ROUTE_WIDTH = 40;
    private static final String TAG = "Navigation";
    private static final int WAYPOINT_SIZE = 30;
    private static final int WAYPOINT_WIDTH = 20;
    private boolean compassShowing;
    public Context context;
    private Reminder currentReminder;
    private boolean isNotifications;
    private final PathMap mNavMap;
    private Verbosity mReminderVerbosity;
    private TimeHelper mTimeHelper;
    private int maxLineLength;
    private List<String> messageBoxes;
    private String pageToShow;
    private RouteFollower routeFollower;
    private boolean shouldReset;
    private static boolean rideInProgress = false;
    private static boolean liveNavigationRide = false;

    private enum Reminder {
        CONTINUE_AHEAD,
        LONG_REMINDER,
        SHORT_REMINDER,
        PREPARE,
        IMMEDIATE,
        NONE;

        private static int CONTINUE_AHEAD_TIME;
        private static int IMMEDIATE_DISTANCE = 5;
        private static int LONG_TTS_REMINDER;
        private static int SHORT_TTS_REMINDER;

        static void initSettings(int shortTime, int longTime) {
            LONG_TTS_REMINDER = longTime;
            SHORT_TTS_REMINDER = shortTime;
            CONTINUE_AHEAD_TIME = (LONG_TTS_REMINDER * 6) / 5;
        }

        boolean shouldTrigger(int timeToGo, int distance) {
            switch (this) {
                case CONTINUE_AHEAD:
                    return timeToGo < CONTINUE_AHEAD_TIME;
                case LONG_REMINDER:
                    return timeToGo < LONG_TTS_REMINDER;
                case SHORT_REMINDER:
                    return timeToGo < SHORT_TTS_REMINDER;
                case PREPARE:
                    return timeToGo < SHORT_TTS_REMINDER && distance > IMMEDIATE_DISTANCE;
                case IMMEDIATE:
                    return distance < IMMEDIATE_DISTANCE;
                default:
                    return false;
            }
        }

        public int getTime() {
            switch (this) {
                case CONTINUE_AHEAD:
                    return CONTINUE_AHEAD_TIME;
                case LONG_REMINDER:
                    return LONG_TTS_REMINDER;
                case SHORT_REMINDER:
                    return SHORT_TTS_REMINDER;
                default:
                    return 0;
            }
        }

        public Reminder nextReminder() {
            switch (this) {
                case CONTINUE_AHEAD:
                    return LONG_REMINDER;
                case LONG_REMINDER:
                    return SHORT_REMINDER;
                case SHORT_REMINDER:
                    return PREPARE;
                case PREPARE:
                    return IMMEDIATE;
                default:
                    return NONE;
            }
        }

        static Reminder reminderFor(int eta, int dist) {
            if (eta > CONTINUE_AHEAD_TIME) {
                return CONTINUE_AHEAD;
            }
            if (eta > LONG_TTS_REMINDER) {
                return LONG_REMINDER;
            }
            if (eta > SHORT_TTS_REMINDER) {
                return SHORT_REMINDER;
            }
            if (dist > IMMEDIATE_DISTANCE) {
                return PREPARE;
            }
            return IMMEDIATE;
        }
    }

    private enum Verbosity {
        OFF(new Reminder[0]),
        LOW(new Reminder[]{Reminder.CONTINUE_AHEAD, Reminder.SHORT_REMINDER, Reminder.IMMEDIATE}),
        HIGH(new Reminder[]{Reminder.CONTINUE_AHEAD, Reminder.LONG_REMINDER, Reminder.SHORT_REMINDER, Reminder.IMMEDIATE}),
        FULL(new Reminder[]{Reminder.CONTINUE_AHEAD, Reminder.LONG_REMINDER, Reminder.SHORT_REMINDER, Reminder.PREPARE, Reminder.IMMEDIATE});

        private final Reminder[] mReminders;

        Verbosity(Reminder[] reminders) {
            this.mReminders = reminders;
        }

        boolean shouldPlay(Reminder reminder) {
            for (Reminder r : this.mReminders) {
                if (r == reminder) {
                    return true;
                }
            }
            return false;
        }
    }

    public Navigation(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        this.shouldReset = false;
        this.isNotifications = false;
        this.messageBoxes = new ArrayList();
        this.maxLineLength = 20;
        this.compassShowing = false;
        this.mReminderVerbosity = Verbosity.HIGH;
        addPage(MetricType.NAVIGATION_NO_COMPASS);
        addPage(MetricType.NAVIGATION);
        addPage(MetricType.NAVIGATION_DESTINATION_REACHED);
        addPage(MetricType.COMPASS);
        addPage(MetricType.NAVIGATION_WITH_MAP);
        this.context = appService.getBaseContext();
        this.messageBoxes.clear();
        this.messageBoxes.add("directionalmessage1");
        this.messageBoxes.add("directionalmessage2");
        this.messageBoxes.add("directionalmessage3");
        this.messageBoxes.add("directionalmessage4");
        this.messageBoxes.add("directionalmessage5");
        updateSettings();
        refreshPageSetting();
        Navigator.mNavPages = this;
        this.mNavMap = new PathMap(appService.getResources());
    }

    String getCurrentPageId() {
        return this.pageToShow;
    }

    private void refreshPageSetting() {
        if (!Prefs.navigationDirectionsOrMap()) {
            this.pageToShow = MetricType.NAVIGATION_WITH_MAP.getResource();
            this.compassShowing = false;
            this.maxLineLength = 20;
        } else if (Prefs.navigationShowCompass()) {
            this.pageToShow = MetricType.NAVIGATION.getResource();
            this.compassShowing = true;
            this.maxLineLength = 30;
        } else {
            this.pageToShow = MetricType.NAVIGATION_NO_COMPASS.getResource();
            this.compassShowing = false;
            this.maxLineLength = 20;
        }
    }

    void setRoute(RouteFollower follower) {
        this.routeFollower = follower;
        this.shouldReset = true;
        this.mNavMap.setRoute(this.routeFollower.currentRoute());
    }

    public void startNavigation() {
        Log.d(TAG, "startNavigation");
        rideInProgress = true;
        this.shouldReset = false;
        liveNavigationRide = true;
        updateSettings();
        refreshPageSetting();
        clearMessages();
        showManeuver();
        showMessage(this.routeFollower.currentInstruction());
        refreshPage();
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        this.mTimeHelper = service.getTimeHelper();
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
        this.mTimeHelper = null;
    }

    public void updateSettings() {
        Prefs.getNavigationTTSSettings();
        this.isNotifications = !Prefs.navigationIsFullScreen();
        Reminder.initSettings(10, 40);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return isBound() && LiveRide.isNavigtionRideMode();
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
        updatePage((PageBoxInfo) null, updateValue);
    }

    @Override // com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        if (LiveRide.isActiveRide()) {
            if (this.shouldReset) {
                startNavigation();
            }
            if (rideInProgress) {
                realUpdate(pageBoxInfo);
                return "";
            }
            return "";
        }
        return "";
    }

    void refreshPage() {
        this.mAppService.updateElements(this.pageToShow);
    }

    void updateSpeed() {
        int lastSpeed = (int) Conversion.speedForLocale(this.routeFollower.currentSpeed());
        List<String> displaySpeed = TimeDistanceHelper.kphToUnits(this.context, lastSpeed);
        updateElement(this.pageToShow, "distance_speed_metric", displaySpeed.get(0));
        updateElement(this.pageToShow, "distance_speed_unit", displaySpeed.get(1));
    }

    void updateDistanceAndTime() {
        displayData();
    }

    void updateCompass() {
        int bearingNorth = ((int) this.routeFollower.getHeading()) + 180;
        int newCurrentBearing = ((int) this.routeFollower.bearingToNextWaypoint()) - bearingNorth;
        int newNextBearing = ((int) this.routeFollower.bearingToFollowingWaypoint()) - bearingNorth;
        updateCompass(newCurrentBearing, newNextBearing);
    }

    void updateMap(Location curLoc) {
        this.mNavMap.updatePositionAndBearing(curLoc.getLatitude(), curLoc.getLongitude(), this.routeFollower.getHeading());
        this.mAppService.addBitmap(IMG_NAV_PATH, this.mNavMap.draw());
    }

    public void realUpdate(PageBoxInfo pageBoxInfo) {
        if (this.mTimeHelper != null) {
            updateElement(this.pageToShow, "distance_time_metric", this.mTimeHelper.getTimeAsString());
            if (this.mTimeHelper.isPaused()) {
                if (pageBoxInfo != null) {
                    String msg = this.mAppService.getString(Prefs.isAutoPauseEnabled() ? R.string.auto_pause_message : R.string.manual_pause_message);
                    updateElement(pageBoxInfo.metric, "pause_message", msg);
                } else if (this.mTimeHelper.isStarted()) {
                    updateElement(MetricType.TIME.getResource(), "bottom_text", this.mAppService.getString(R.string.elapsed_time));
                } else {
                    updateElement(MetricType.TIME.getResource(), "bottom_text", this.mAppService.getStringFromTheme(R.attr.strAutoStartMsg));
                }
            }
        }
    }

    public void displayData() {
        double distanceToGo = this.routeFollower.distanceToNextWaypoint();
        List<String> displayDistance = TimeDistanceHelper.metresToUnits(this.context, distanceToGo);
        if (rideInProgress) {
            NavLog.d(TAG, "Update distance to go: " + displayDistance.get(0) + " " + displayDistance.get(1));
            updateElement(this.pageToShow, "maindistance", displayDistance.get(0));
            updateElement(this.pageToShow, "distancetype", displayDistance.get(1));
        }
        checkForReminder();
    }

    void showManeuver() {
        NavLog.d(TAG, "showManeuever");
        int theDtoG = (int) this.routeFollower.distanceToNextWaypoint();
        int expectedTime = this.routeFollower.timeToNextWaypoint();
        String currentInstruction = this.routeFollower.currentInstruction();
        String currentManeuver = this.routeFollower.currentManeuver();
        setNextReminder(expectedTime, theDtoG);
        if (rideInProgress) {
            List<String> displayDistance = TimeDistanceHelper.metresToUnits(this.context, theDtoG);
            updateElement(this.pageToShow, "maindistance", displayDistance.get(0));
            updateElement(this.pageToShow, "distancetype", displayDistance.get(1));
            if (this.currentReminder == Reminder.CONTINUE_AHEAD) {
                addArrowFromResource(null, null);
                showMessage(R.string.continue_ahead);
            } else {
                addArrowFromResource(currentManeuver, currentInstruction);
                showMessage(currentInstruction);
            }
        }
        this.mNavMap.setNextWaypoint(this.routeFollower.nextWaypoint());
        this.mAppService.refresh(true);
    }

    private void updateInstructionAndManouevre() {
    }

    void updateCompass(int newCurrentBearing, int newNextBearing) {
        if (this.compassShowing) {
            Log.d(TAG, "Update compass: bearing to waypoint: " + newCurrentBearing + ", route bearing: " + newNextBearing);
            if (rideInProgress) {
                this.mAppService.addBitmap(IMG_DISTANCE_COMPASS, Compass.updateCompass(newNextBearing, newCurrentBearing));
            } else {
                this.mAppService.addBitmap(IMG_DISTANCE_COMPASS, Compass.drawFinalCompass());
            }
        }
    }

    private void showMessage(int theMessage) {
        showMessage(this.context.getString(theMessage));
    }

    private void showMessage(String theMessage) {
        NavLog.d(TAG, "showMessage: " + theMessage);
        if (theMessage != null && !theMessage.isEmpty()) {
            clearMessages();
            String[] myWords = theMessage.split(" ");
            List<String> myLines = new ArrayList<>();
            StringBuilder sB = new StringBuilder();
            for (String s : myWords) {
                if (sB.length() + s.length() > this.maxLineLength) {
                    myLines.add(sB.toString());
                    sB = new StringBuilder();
                }
                sB.append(s + " ");
            }
            if (sB.length() > 0) {
                myLines.add(sB.toString());
            }
            int startLine = 0;
            if (!this.compassShowing) {
                switch (myLines.size()) {
                    case 1:
                        startLine = 2;
                        break;
                    case 2:
                    case 3:
                        startLine = 1;
                        break;
                }
            }
            Iterator<String> it = myLines.iterator();
            while (it.hasNext()) {
                updateElement(this.pageToShow, this.messageBoxes.get(startLine), it.next());
                startLine++;
                if (startLine == this.messageBoxes.size()) {
                    return;
                }
            }
        }
    }

    private void clearMessages() {
        for (String s : this.messageBoxes) {
            updateElement(this.pageToShow, s, "");
        }
    }

    void signalDestination() {
        NavLog.d(TAG, "Destination reached");
        if (!this.pageToShow.contentEquals("navigation_destination_reached")) {
            this.pageToShow = "navigation_destination_reached";
            PageNav.showPage(this.pageToShow);
        }
        String currentInstruction = this.routeFollower.currentInstruction();
        showMessage(currentInstruction);
        updateElement(this.pageToShow, "rideTime", TimeDistanceHelper.secondsToString((int) (LiveRide.getTime() / 1000)));
        if (SensorsConnector.isAllowedType(Sensor.DataType.SPEED)) {
            List<String> speedData = TimeDistanceHelper.speedToUnits(LiveRide.getAverageSpeed());
            updateElement(this.pageToShow, "avgSpeed", speedData.get(0));
            updateElement(this.pageToShow, "units", Conversion.getUnitOfSpeed(this.mAppService, true));
            updateElement(this.pageToShow, "avgSpeedLabel", this.mAppService.getString(R.string.vc_title_average_speed_abbrev));
        } else if (SensorsConnector.isAllowedType(Sensor.DataType.PACE)) {
            updateElement(this.pageToShow, "avgSpeed", PaceUtil.formatPace(LiveRide.getAveragePaceLocale()));
            updateElement(this.pageToShow, "units", Conversion.getUnitOfPace(this.mAppService, true));
            updateElement(this.pageToShow, "avgSpeedLabel", this.mAppService.getString(R.string.vc_title_average_pace));
        }
        this.mAppService.addBitmap(IMG_DESTINATION_REACHED, Compass.getColouredImage(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_flags_headset), Compass.HEADSET_GREEN));
        speakOrNotify(currentInstruction);
        updateCompass(0, 0);
        this.mAppService.refresh(true);
        rideInProgress = false;
    }

    void signalOffRoute() {
        String msg = this.context.getString(R.string.msg_off_route);
        showMessage(msg);
        speakOrNotify(msg);
    }

    void signalReRouting() {
        String msg = this.context.getString(R.string.msg_rerouting);
        showMessage(msg);
        speakOrNotify(msg);
    }

    private void setNextReminder(int eta, int dist) {
        this.currentReminder = Reminder.reminderFor(eta, dist);
        Log.d(TAG, "Next reminder: " + this.currentReminder + " (eta: " + eta + ", dist: " + dist + ")");
        switch (this.currentReminder) {
            case CONTINUE_AHEAD:
                giveContinueAhead();
                break;
            case PREPARE:
                sendPrepare();
                break;
        }
    }

    private void checkForReminder() {
        if (this.routeFollower.checkOnRoute() && this.routeFollower.isMoving() && this.currentReminder != null && this.currentReminder.shouldTrigger(this.routeFollower.timeToNextWaypoint(), (int) this.routeFollower.distanceToNextWaypoint())) {
            Log.d(TAG, "Triggering reminder: " + this.currentReminder);
            if (this.mReminderVerbosity.shouldPlay(this.currentReminder)) {
                switch (this.currentReminder) {
                    case CONTINUE_AHEAD:
                        String currentInstruction = this.routeFollower.currentInstruction();
                        addArrowFromResource(this.routeFollower.currentManeuver(), currentInstruction);
                        showMessage(currentInstruction);
                        this.mAppService.refresh(true);
                        break;
                    case LONG_REMINDER:
                    case SHORT_REMINDER:
                        giveReminder();
                        break;
                    case PREPARE:
                        sendPrepare();
                        break;
                    case IMMEDIATE:
                        giveImmediateReminder();
                        break;
                }
            }
            this.currentReminder = this.currentReminder.nextReminder();
        }
    }

    private void giveContinueAhead() {
        double distanceToGo = this.routeFollower.distanceToNextWaypoint();
        List<String> distStrings = TimeDistanceHelper.metresToUnits(this.mAppService, distanceToGo, true);
        speakOrNotify(Integer.valueOf(R.string.nav_tts_continue_ahead), distStrings.get(0), distStrings.get(1));
    }

    private void giveReminder() {
        String currentInstruction = this.routeFollower.currentInstruction();
        List<String> distStrings = TimeDistanceHelper.metresToUnits(this.mAppService, this.routeFollower.distanceToNextWaypoint(), true);
        speakOrNotify(Integer.valueOf(R.string.nav_tts_in), distStrings.get(0), distStrings.get(1), currentInstruction);
    }

    private void giveImmediateReminder() {
        String currentInstruction = this.routeFollower.currentInstruction();
        speakOrNotify(currentInstruction);
    }

    private void sendPrepare() {
        if (this.routeFollower.isApproachingDestination()) {
            String destinationAheadString = this.context.getString(R.string.destination_ahead);
            speakOrNotify(destinationAheadString);
        } else {
            String currentInstruction = this.routeFollower.currentInstruction();
            speakOrNotify(this.mAppService.getString(R.string.nav_tts_prepare) + " " + currentInstruction);
        }
    }

    private void speakOrNotify(Object... parts) {
        StringBuilder builder = new StringBuilder();
        for (Object o : parts) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            if (o instanceof Integer) {
                o = this.mAppService.getString(((Integer) o).intValue());
            }
            builder.append(o);
        }
        String msg = builder.toString();
        Log.d(TAG, msg);
        if (!this.isNotifications) {
            this.mAppService.speakText(AppService.NOTIFICATION_TTS, msg);
        } else if (rideInProgress) {
            this.mAppService.sendDropDownNotification(AppService.PUPIL_IMG, 3000, msg);
        }
    }

    void markRideStopped() {
        rideInProgress = false;
        liveNavigationRide = false;
    }

    protected void updateElement(String page, String element, Object value) {
        updateElement(page, element, "content", value);
    }

    private void addArrowFromResource(String maneuver, String instruction) {
        Log.d(TAG, "Pick direction arrow for: " + maneuver + ", " + instruction);
        DirectionArrows arrow1 = DirectionArrows.fromInstruction(maneuver);
        DirectionArrows arrow2 = DirectionArrows.DEFAULT;
        Log.d(TAG, " chosen: " + arrow1);
        if (arrow1.isBasic()) {
            arrow2 = DirectionArrows.fromInstruction(instruction);
            Log.d(TAG, "   and: " + arrow2);
        }
        this.mAppService.addBitmapFromResource(IMG_SWERVE_ARROW, arrow2.isBasic() ? arrow1.resId : arrow2.resId);
    }
}
