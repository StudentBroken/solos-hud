package com.kopin.solos.graphics;

import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.NormalisedPower;

/* JADX INFO: loaded from: classes37.dex */
class GraphValueProviders {
    private static final int GRAPH_DATA_POINTS = 240;

    GraphValueProviders() {
    }

    static class AltitudeValueProvider implements GraphBuilder.GraphValue {
        AltitudeValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.ALTITUDE;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasAltitude() && record.hasSpeed();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) record.getAltitudeForLocale();
        }
    }

    static class SpeedValueProvider implements GraphBuilder.GraphValue {
        SpeedValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.SPEED;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasSpeed();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) record.getSpeedForLocale();
        }
    }

    static class PaceValueProvider implements GraphBuilder.GraphValue {
        PaceValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.PACE;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasSpeed();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) Conversion.speedToPaceForLocale(record.getSpeed());
        }
    }

    static class CadenceValueProvider implements GraphBuilder.GraphValue {
        CadenceValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.CADENCE;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasCadence();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) record.getCadence();
        }
    }

    static class StepValueProvider implements GraphBuilder.GraphValue {
        StepValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.STEP;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasCadence();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) record.getCadence();
        }
    }

    static class HeartrateValueProvider implements GraphBuilder.GraphValue {
        HeartrateValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.HEARTRATE;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasHeartrate();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return record.getHeartrate();
        }
    }

    static class PowerValueProvider implements GraphBuilder.GraphValue {
        PowerValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.POWER;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasPower();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) record.getPower();
        }
    }

    static class OxygenValueProvider implements GraphBuilder.GraphValue {
        OxygenValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.OXYGEN;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasOxygen();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return record.getOxygen();
        }
    }

    static class NormalisedPowerValueProvider implements GraphBuilder.GraphValue {
        NormalisedPower mNormalisedPowerCalc = new NormalisedPower();

        NormalisedPowerValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.NORMALISED_POWER;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 0;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasPower();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            double y = record.getPower();
            long timeElapsed = record.getTimestamp();
            if (timeElapsed <= 0) {
                return -2.14748365E9f;
            }
            double np = this.mNormalisedPowerCalc.calculateNormalisedPower(y, timeElapsed);
            if (np > 0.0d) {
                return (float) np;
            }
            return -2.14748365E9f;
        }
    }

    static class IntensityFactorValueProvider implements GraphBuilder.GraphValue {
        IntensityFactorValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.INTENSITY_FACTOR;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 0;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasPower();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) record.getPower();
        }
    }

    static class CorrectedAltitudeValueProvider implements GraphBuilder.GraphValue {
        CorrectedAltitudeValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.CORRECTED_ALTITUDE;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return false;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return 0.0f;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return 0.0f;
        }
    }

    static class StrideValueProvider implements GraphBuilder.GraphValue {
        StrideValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.STRIDE;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasStride();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) record.getStrideForLocale();
        }
    }

    static class KickValueProvider implements GraphBuilder.GraphValue {
        KickValueProvider() {
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public Record.MetricData getValueType() {
            return Record.MetricData.KICK;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public int preferredResolution() {
            return 240;
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public boolean hasData(Record record) {
            return record.hasPower();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getX(Record record) {
            return record.getTimestamp();
        }

        @Override // com.kopin.solos.graphics.GraphBuilder.GraphValue
        public float getY(Record record) {
            return (float) record.getPower();
        }
    }
}
