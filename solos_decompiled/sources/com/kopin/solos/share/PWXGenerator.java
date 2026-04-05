package com.kopin.solos.share;

import android.util.Log;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.StringPrintWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jstrava.entities.VCTrakFile;

/* JADX INFO: loaded from: classes4.dex */
public class PWXGenerator implements VCTrakFile.FileGenerator {
    private static final String FOOTER = "</pwx>\n";
    private static final String HEADER = "<?xml version=\"1.0\"?>\n<pwx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xsi:schemaLocation=\"http://www.peaksware.com/PWX/1/0 http://www.peaksware.com/PWX/1/0/pwx.xsd\" version=\"1.0\" xmlns=\"http://www.peaksware.com/PWX/1/0\" creator=\"SoloS\">\n";
    private static final SimpleDateFormat UPLOAD_SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private int lapNumber;
    private SavedWorkout mRide;

    static /* synthetic */ int access$008(PWXGenerator x0) {
        int i = x0.lapNumber;
        x0.lapNumber = i + 1;
        return i;
    }

    public PWXGenerator(SavedWorkout ride) {
        this.mRide = ride;
        if (ride == null) {
            throw new NullPointerException("Null ride");
        }
    }

    @Override // org.jstrava.entities.VCTrakFile.FileGenerator
    public String generate() {
        StringPrintWriter pw = new StringPrintWriter();
        try {
            generate(pw);
        } catch (IOException e) {
            Log.e("PWXGenerator", "", e);
        }
        return pw.getResult();
    }

    @Override // org.jstrava.entities.VCTrakFile.FileGenerator
    public void generate(Writer pw) throws IOException {
        pw.append(HEADER);
        pw.append("  <workout>\n");
        pw.append((CharSequence) ("\t<sportType>" + getSportType(this.mRide) + "</sportType>\n"));
        if (this.mRide.hasTitle()) {
            pw.append("\t<title>").append((CharSequence) Utility.escapeXMLChars(this.mRide.getTitle())).append("</title>\n");
        }
        if (this.mRide.hasComment()) {
            pw.append("\t<cmt>").append((CharSequence) Utility.escapeXMLChars(this.mRide.getComment())).append("</cmt>\n");
        }
        pw.append("\t<device id=\"SoloS\">\n");
        pw.append("\t  <make>Kopin</make>\n");
        pw.append("\t  <model>SoloS</model>\n");
        pw.append("\t  <extension />\n");
        pw.append("\t</device>\n");
        pw.append("\t<time>").append((CharSequence) UPLOAD_SDF.format(new Date(this.mRide.getActualStartTime()))).append("</time>\n");
        pw.append("\t<summarydata>\n");
        pw.append("\t  <beginning>0</beginning>\n");
        pw.append("\t  <duration>").append((CharSequence) String.valueOf(this.mRide.getDuration() / 1000.0d)).append("</duration>\n");
        if (Ride.hasData(this.mRide.getMaxHeartrate())) {
            pw.append("\t  <hr max=\"").append((CharSequence) String.valueOf(this.mRide.getMaxHeartrate())).append("\" />\n");
        }
        if (Ride.hasData(this.mRide.getMaxSpeed()) && Ride.hasData(this.mRide.getAverageSpeed())) {
            String maxSpeed = String.valueOf(this.mRide.getMaxSpeed());
            String avgSpeed = String.valueOf(this.mRide.getAverageSpeed());
            pw.append("\t  <spd max=\"").append((CharSequence) maxSpeed).append("\" avg=\"").append((CharSequence) avgSpeed).append("\" />\n");
        }
        if (Ride.hasData(this.mRide.getMaxPower())) {
            pw.append("\t  <pwr max=\"").append((CharSequence) String.valueOf((int) this.mRide.getMaxPower())).append("\" />\n");
        }
        pw.append("\t  <dist>").append((CharSequence) String.valueOf(this.mRide.getDistance())).append("</dist>\n");
        pw.append("\t</summarydata>\n");
        writeLaps(pw);
        pw.append("  </workout>\n");
        pw.append(FOOTER);
    }

    private void writeLaps(final Writer pw) {
        this.lapNumber = 1;
        SQLHelper.foreachLap(this.mRide.getId(), this.mRide.isComplete(), new SavedWorkout.foreachLapCallback() { // from class: com.kopin.solos.share.PWXGenerator.1
            @Override // com.kopin.solos.storage.SavedWorkout.foreachLapCallback
            public boolean onLap(Lap.Saved lap) {
                try {
                    pw.append((CharSequence) "\t<segment>\n");
                    pw.append((CharSequence) "\t  <name>Lap ").append((CharSequence) String.valueOf(PWXGenerator.access$008(PWXGenerator.this))).append((CharSequence) "</name>\n");
                    pw.append((CharSequence) "\t  <summarydata>\n");
                    pw.append((CharSequence) "\t\t<beginning>").append((CharSequence) String.valueOf(((lap.getStartTime() - PWXGenerator.this.mRide.getStartTime()) + PWXGenerator.this.mRide.getActualStartTime()) / 1000.0d)).append((CharSequence) "</beginning>\n");
                    pw.append((CharSequence) "\t\t<duration>").append((CharSequence) String.valueOf(lap.getDuration() / 1000.0d)).append((CharSequence) "</duration>\n");
                    if (Ride.hasData(lap.getMaxHeartrate())) {
                        pw.append((CharSequence) "\t\t<hr max=\"").append((CharSequence) String.valueOf(lap.getMaxHeartrate())).append((CharSequence) "\" />\n");
                    }
                    if (Ride.hasData(lap.getMaxSpeed()) && Ride.hasData(lap.getAverageSpeed())) {
                        String maxSpeed = String.valueOf(lap.getMaxSpeed());
                        String avgSpeed = String.valueOf(lap.getAverageSpeed());
                        pw.append((CharSequence) "\t\t<spd max=\"").append((CharSequence) maxSpeed).append((CharSequence) "\" avg=\"").append((CharSequence) avgSpeed).append((CharSequence) "\" />\n");
                    }
                    if (Ride.hasData(lap.getMaxPower())) {
                        pw.append((CharSequence) "\t\t<pwr max=\"").append((CharSequence) String.valueOf((int) lap.getMaxPower())).append((CharSequence) "\" />\n");
                    }
                    pw.append((CharSequence) "\t\t<dist>").append((CharSequence) String.valueOf(lap.getDistance())).append((CharSequence) "</dist>\n");
                    pw.append((CharSequence) "\t  </summarydata>\n");
                    if (Ride.hasData(lap.getMaxSpeed())) {
                        pw.append((CharSequence) "\t  <extension>\n");
                        pw.append((CharSequence) "\t\t<maxspd>").append((CharSequence) String.valueOf(lap.getMaxSpeed())).append((CharSequence) "</maxspd>\n");
                        pw.append((CharSequence) "\t </extension>\n");
                    }
                    pw.append((CharSequence) "\t</segment>\n");
                    return true;
                } catch (IOException ioe) {
                    Log.e("PWXGenerator", "", ioe);
                    return false;
                }
            }
        });
        this.mRide.foreachRecord(new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.share.PWXGenerator.2
            @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
            public boolean onRecord(Record record) {
                try {
                    pw.append((CharSequence) "\t<sample>\n");
                    pw.append((CharSequence) "\t  <timeoffset>").append((CharSequence) String.valueOf(record.getTimestamp() / 1000.0d)).append((CharSequence) "</timeoffset>\n");
                    if (record.hasHeartrate()) {
                        pw.append((CharSequence) "\t  <hr>").append((CharSequence) String.valueOf(record.getHeartrate() < 254 ? record.getHeartrate() : 254)).append((CharSequence) "</hr>\n");
                    }
                    if (record.hasSpeed()) {
                        double speed = record.getSpeed();
                        pw.append((CharSequence) "\t  <spd>").append((CharSequence) String.valueOf(speed)).append((CharSequence) "</spd>\n");
                    }
                    if (record.hasPower()) {
                        pw.append((CharSequence) "\t  <pwr>").append((CharSequence) String.valueOf((int) record.getPower())).append((CharSequence) "</pwr>\n");
                    }
                    if (record.hasCadence()) {
                        pw.append((CharSequence) "\t  <cad>").append((CharSequence) String.valueOf(record.getCadence() < 254.0d ? (int) record.getCadence() : 254)).append((CharSequence) "</cad>\n");
                    }
                    if (record.hasSpeed()) {
                        pw.append((CharSequence) "\t  <dist>").append((CharSequence) String.valueOf(record.getDistance())).append((CharSequence) "</dist>\n");
                    }
                    if (record.hasLocation()) {
                        if (record.hasAltitude()) {
                            pw.append((CharSequence) "\t  <alt>").append((CharSequence) String.valueOf(record.getAltitude())).append((CharSequence) "</alt>\n");
                        }
                        pw.append((CharSequence) "\t  <lat>").append((CharSequence) String.valueOf(record.getLatitude())).append((CharSequence) "</lat>\n");
                        pw.append((CharSequence) "\t  <lon>").append((CharSequence) String.valueOf(record.getLongitude())).append((CharSequence) "</lon>\n");
                    }
                    pw.append((CharSequence) "\t  <time>").append((CharSequence) PWXGenerator.UPLOAD_SDF.format(new Date(record.getTimestamp()))).append((CharSequence) "</time>\n");
                    pw.append((CharSequence) "\t</sample>\n");
                    return true;
                } catch (IOException ioe) {
                    Log.e("PWXGenerator", "", ioe);
                    return false;
                }
            }
        });
    }

    private String getSportType(SavedWorkout workout) {
        switch (workout.getSportType()) {
            case RUN:
                return Run.TABLE_NAME;
            default:
                return Bike.TABLE;
        }
    }
}
