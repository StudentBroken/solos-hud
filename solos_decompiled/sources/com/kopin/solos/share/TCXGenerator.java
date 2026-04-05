package com.kopin.solos.share;

import android.util.Log;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.StringPrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.jstrava.entities.VCTrakFile;

/* JADX INFO: loaded from: classes4.dex */
public class TCXGenerator implements VCTrakFile.FileGenerator {
    private static final String FOOTER = "</TrainingCenterDatabase>\n";
    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<TrainingCenterDatabase\n  xsi:schemaLocation=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd\"\n  xmlns:ns5=\"http://www.garmin.com/xmlschemas/ActivityGoals/v1\"\n  xmlns:ns3=\"http://www.garmin.com/xmlschemas/ActivityExtension/v2\"\n  xmlns:ns2=\"http://www.garmin.com/xmlschemas/UserProfile/v2\"\n  xmlns=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2\"\n  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns4=\"http://www.garmin.com/xmlschemas/ProfileExtension/v1\">\n";
    private static final String SPORT = "Sport=\"%s\"";
    private double accumDist;
    private final SavedWorkout mRide;
    private final SportType mSportType;

    enum TCXSportType {
        Biking,
        Running,
        Other,
        MultiSport,
        Extensions
    }

    public TCXGenerator(SavedWorkout workout) {
        this.mRide = workout;
        if (workout == null) {
            throw new NullPointerException("Null ride");
        }
        if (workout instanceof SavedRide) {
            this.mSportType = SportType.RIDE;
        } else if (workout instanceof SavedRun) {
            this.mSportType = SportType.RUN;
        } else {
            this.mSportType = SportType.DEFAULT_TYPE;
        }
    }

    private String getSport() {
        switch (this.mSportType) {
            case RUN:
                return TCXSportType.Running.name();
            default:
                return TCXSportType.Biking.name();
        }
    }

    @Override // org.jstrava.entities.VCTrakFile.FileGenerator
    public String generate() {
        StringPrintWriter pw = new StringPrintWriter();
        try {
            generate(pw);
        } catch (IOException e) {
            Log.e("TCXGenerator", "", e);
        }
        return pw.getResult();
    }

    @Override // org.jstrava.entities.VCTrakFile.FileGenerator
    public void generate(Writer pw) throws IOException {
        String startDate = Utility.formatDateAndTimeUpload(this.mRide.getActualStartTime(), false);
        pw.append(HEADER);
        pw.append("  <Activities>\n");
        pw.append("\t<Activity ").append((CharSequence) String.format(SPORT, getSport())).append(">\n");
        pw.append("\t  <Id>").append((CharSequence) startDate).append("</Id>\n");
        writeLaps(pw);
        pw.append("\t  <Training VirtualPartner=\"false\">\n");
        pw.append("\t\t<QuickWorkoutResults>\n");
        pw.append("\t\t  <TotalTimeSeconds>").append((CharSequence) String.valueOf(this.mRide.getDuration() / 1000.0d)).append("</TotalTimeSeconds>\n");
        if (Ride.hasData(this.mRide.getDistance())) {
            pw.append("\t\t <DistanceMeters>").append((CharSequence) String.valueOf(this.mRide.getDistance())).append("</DistanceMeters>\n");
        }
        pw.append("\t\t</QuickWorkoutResults>\n");
        pw.append("\t  </Training>\n");
        pw.append("\t</Activity>\n");
        pw.append("  </Activities>\n");
        pw.append(FOOTER);
    }

    private void writeLaps(final Writer pw) {
        SQLHelper.foreachLap(this.mRide.getId(), this.mRide.isComplete(), new SavedWorkout.foreachLapCallback() { // from class: com.kopin.solos.share.TCXGenerator.1
            @Override // com.kopin.solos.storage.SavedWorkout.foreachLapCallback
            public boolean onLap(Lap.Saved lap) {
                try {
                    pw.append((CharSequence) "\t  <Lap StartTime=\"").append((CharSequence) Utility.formatDateAndTimeUpload(lap.getStartTime() + TCXGenerator.this.mRide.getActualStartTime(), false)).append((CharSequence) "\">\n");
                    pw.append((CharSequence) "\t\t<TotalTimeSeconds>").append((CharSequence) String.valueOf(lap.getDuration() / 1000.0d)).append((CharSequence) "</TotalTimeSeconds>\n");
                    if (Ride.hasData(lap.getDistance())) {
                        pw.append((CharSequence) "\t\t<DistanceMeters>").append((CharSequence) String.valueOf(lap.getDistance())).append((CharSequence) "</DistanceMeters>\n");
                    }
                    if (Ride.hasData(lap.getMaxSpeed())) {
                        pw.append((CharSequence) "\t\t<MaximumSpeed>").append((CharSequence) String.valueOf(lap.getMaxSpeed())).append((CharSequence) "</MaximumSpeed>\n");
                    }
                    if (Ride.hasData(lap.getCalories())) {
                        pw.append((CharSequence) "\t\t<Calories>").append((CharSequence) String.valueOf(lap.getCalories())).append((CharSequence) "</Calories>\n");
                    }
                    if (Ride.hasData(lap.getMaxHeartrate())) {
                        int maxHeartrate = lap.getMaxHeartrate() < 254 ? lap.getMaxHeartrate() : 254;
                        pw.append((CharSequence) "\t\t<MaximumHeartRateBpm>\n");
                        pw.append((CharSequence) "\t\t  <Value>").append((CharSequence) String.valueOf(maxHeartrate)).append((CharSequence) "</Value>\n");
                        pw.append((CharSequence) "\t\t</MaximumHeartRateBpm>\n");
                    }
                    pw.append((CharSequence) "\t\t<TriggerMethod>").append((CharSequence) lap.getSplitTrigger()).append((CharSequence) "</TriggerMethod>\n");
                    TCXGenerator.this.writeTrackParts(pw, lap.getStartTime(), lap.getEndTime());
                    if (Ride.hasData(lap.getAverageSpeed())) {
                        pw.append((CharSequence) "\t\t<Extensions>\n");
                        pw.append((CharSequence) "\t\t  <LX xmlns=\"http://www.garmin.com/xmlschemas/ActivityExtension/v2\">\n");
                        pw.append((CharSequence) "\t\t\t<AvgSpeed>").append((CharSequence) String.valueOf(lap.getAverageSpeed())).append((CharSequence) "</AvgSpeed>\n");
                        pw.append((CharSequence) "\t\t  </LX>\n");
                        pw.append((CharSequence) "\t\t</Extensions>\n");
                    }
                    pw.append((CharSequence) "\t </Lap>\n");
                    pw.flush();
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeTrackParts(final Writer pw, long start, long end) throws IOException {
        pw.append("\t\t<Track>\n");
        final long startTime = this.mRide.getActualStartTime();
        this.accumDist = 0.0d;
        this.mRide.foreachRecord(start, end, new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.share.TCXGenerator.2
            @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
            public boolean onRecord(Record record) {
                try {
                    if (record.isBreak()) {
                        pw.append((CharSequence) "\t\t</Track>\n");
                        pw.append((CharSequence) "\t\t<Track>\n");
                        return true;
                    }
                    pw.append((CharSequence) "\t\t  <Trackpoint>\n");
                    pw.append((CharSequence) "\t\t\t<Time>").append((CharSequence) Utility.formatDateAndTimeUpload(startTime + record.getTimestamp(), false)).append((CharSequence) "</Time>\n");
                    if (record.hasLocation()) {
                        if (record.hasAltitude()) {
                            pw.append((CharSequence) "\t\t\t  <AltitudeMeters>").append((CharSequence) String.valueOf(record.getAltitude())).append((CharSequence) "</AltitudeMeters>\n");
                        }
                        pw.append((CharSequence) "\t\t\t<Position>\n");
                        pw.append((CharSequence) "\t\t\t  <LatitudeDegrees>").append((CharSequence) String.valueOf(record.getLatitude())).append((CharSequence) "</LatitudeDegrees>\n");
                        pw.append((CharSequence) "\t\t\t  <LongitudeDegrees>").append((CharSequence) String.valueOf(record.getLongitude())).append((CharSequence) "</LongitudeDegrees>\n");
                        pw.append((CharSequence) "\t\t\t</Position>\n");
                    }
                    if (record.hasDistance()) {
                        TCXGenerator.this.accumDist += record.getDistance();
                    }
                    pw.append((CharSequence) "\t\t\t<DistanceMeters>").append((CharSequence) String.valueOf(TCXGenerator.this.accumDist)).append((CharSequence) "</DistanceMeters>\n");
                    if (record.hasHeartrate()) {
                        int heartrate = record.getHeartrate() < 254 ? record.getHeartrate() : 254;
                        pw.append((CharSequence) "\t\t\t<HeartRateBpm>\n");
                        pw.append((CharSequence) "\t\t\t  <Value>").append((CharSequence) String.valueOf(heartrate)).append((CharSequence) "</Value>\n");
                        pw.append((CharSequence) "\t\t\t</HeartRateBpm>\n");
                    }
                    if (TCXGenerator.this.mSportType == SportType.RIDE && record.hasCadence()) {
                        TCXGenerator.add(pw, "Cadence", Integer.valueOf((int) record.getCadence()), 3);
                    }
                    boolean hasExtension = record.hasSpeed() || record.hasPower();
                    if (hasExtension) {
                        pw.append((CharSequence) "\t\t\t<Extensions>\n");
                        pw.append((CharSequence) "\t\t\t  <TPX xmlns=\"http://www.garmin.com/xmlschemas/ActivityExtension/v2\">\n");
                        if (record.hasSpeed()) {
                            TCXGenerator.add(pw, "Speed", Double.valueOf(record.getSpeed()), 4);
                        }
                        if (record.hasPower()) {
                            TCXGenerator.add(pw, "Watts", Integer.valueOf((int) record.getPower()), 4);
                        }
                        if (TCXGenerator.this.mSportType == SportType.RUN && record.hasCadence()) {
                            TCXGenerator.add(pw, "RunCadence", Integer.valueOf((int) record.getCadence()), 4);
                        }
                        pw.append((CharSequence) "\t\t\t  </TPX>\n");
                        pw.append((CharSequence) "\t\t\t</Extensions>\n");
                    }
                    pw.append((CharSequence) "\t\t  </Trackpoint>\n");
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        });
        pw.append("\t\t</Track>\n");
    }

    public static void exportToFile(SavedWorkout ride, File file) {
        TCXGenerator self = new TCXGenerator(ride);
        try {
            FileWriter writer = new FileWriter(file);
            self.generate(writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void add(Writer pw, String tag, Object content, int indent) throws IOException {
        add(pw, tag, String.valueOf(content), indent);
    }

    public static void add(Writer pw, String tag, String content, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            pw.append("\t");
        }
        pw.append("<").append((CharSequence) tag).append(">").append((CharSequence) content);
        pw.append("</").append((CharSequence) tag).append(">").append("\n");
    }
}
