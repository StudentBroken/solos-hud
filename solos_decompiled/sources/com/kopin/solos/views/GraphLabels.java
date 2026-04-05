package com.kopin.solos.views;

import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.view.graphics.GraphRenderer;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes37.dex */
public class GraphLabels {
    public static final boolean LAP_LABEL_X_START_ZERO = true;
    private final LabelConverter mConverter;
    private ArrayList<GraphRenderer.GraphLabel> mLabels;
    private double mMax;
    private double mMin;
    private BigDecimal mNiceMax;
    private BigDecimal mNiceMin;
    private BigDecimal mNiceRange;
    private NiceScale mScale;
    private boolean mUseSmall;

    public enum LabelConverter {
        DEFAULT,
        TIME,
        DISTANCE,
        DISTANCE_METRIC,
        DISTANCE_IMPERIAL,
        MINUTES;

        public GraphRenderer.GraphLabel getLabel(double val, boolean isSmall, double pinTo) {
            return new GraphRenderer.GraphLabel(getValue(val, isSmall, pinTo), (float) val);
        }

        public String getValue(double val, boolean isSmall, double pinTo) {
            double val2 = Math.abs(val);
            switch (this) {
                case DISTANCE_METRIC:
                    if (val2 < 0.0d) {
                        val2 = 0.0d;
                    }
                    double distance = isSmall ? val2 : val2 / 1000.0d;
                    String fmt = (distance > 10.0d || isSmall) ? "%.0f" : "%.1f";
                    return String.format(fmt, Double.valueOf(distance));
                case DISTANCE_IMPERIAL:
                    if (val2 < 0.0d) {
                        val2 = 0.0d;
                    }
                    double distance2 = isSmall ? val2 : val2 / 5280.0d;
                    String fmt2 = (distance2 > 10.0d || isSmall) ? "%.0f" : "%.1f";
                    return String.format(fmt2, Double.valueOf(distance2));
                case DEFAULT:
                    if (isSmall) {
                        return String.format("%.1f", Double.valueOf(val2));
                    }
                    return String.format("%.0f", Double.valueOf(val2));
                case TIME:
                    if (isSmall) {
                        return String.format("%.0f", Double.valueOf(val2 / 1000.0d));
                    }
                    return String.format("%.0f", Double.valueOf(val2 / 60000.0d));
                case MINUTES:
                    return PaceUtil.formatPace(val2);
                case DISTANCE:
                    return String.format("%.0f", Double.valueOf(Utility.convertToUserUnits(0, val2)));
                default:
                    return "";
            }
        }
    }

    private GraphLabels(double min, double max, LabelConverter converter) {
        this.mConverter = converter;
        this.mMin = min;
        this.mMax = max;
    }

    public int getCount() {
        return this.mLabels.size();
    }

    public boolean useSmall() {
        return this.mUseSmall;
    }

    public ArrayList<GraphRenderer.GraphLabel> getLabels() {
        return this.mLabels;
    }

    private int findBestSF(int sf) {
        MathContext roundUp = new MathContext(sf + 2, RoundingMode.UP);
        MathContext roundDown = new MathContext(sf + 2, RoundingMode.DOWN);
        this.mNiceMax = new BigDecimal(this.mMax, roundUp);
        int scale = this.mNiceMax.scale();
        this.mNiceMin = new BigDecimal(this.mMin, roundDown).setScale(scale, RoundingMode.DOWN);
        this.mNiceRange = this.mNiceMax.subtract(this.mNiceMin, roundUp);
        int span = this.mNiceRange.unscaledValue().intValue();
        if (span < 5 && sf == 1) {
            return findBestSF(sf + 1);
        }
        return sf;
    }

    private int findBestStep(int sf) {
        int span = this.mNiceRange.unscaledValue().intValue();
        while (span > 9) {
            if (span % 7 == 0) {
                span /= 7;
            } else if (span % 5 == 0) {
                span /= 5;
            } else if (span % 4 == 0) {
                span /= 4;
            } else if (span % 3 == 0) {
                span /= 3;
            } else if (span % 2 == 0) {
                span /= 2;
            } else {
                span /= 2;
            }
        }
        switch (span) {
            case 3:
            case 6:
            case 9:
                return 3;
            case 4:
            case 8:
                return 4;
            case 5:
            case 7:
            default:
                return span;
        }
    }

    public static GraphLabels create(double min, double max, LabelConverter converter, boolean forceSmall) {
        GraphLabels self = new GraphLabels(min, max, converter);
        self.mUseSmall = forceSmall;
        int sf = self.findBestSF(2);
        int count = self.findBestStep(sf);
        self.makeLabels(count);
        return self;
    }

    private void findScale() {
        double tMin;
        double tMin2 = Double.valueOf(this.mConverter.getValue(this.mMin, false, this.mMin)).doubleValue();
        double tMax = Double.valueOf(this.mConverter.getValue(this.mMax, false, this.mMax)).doubleValue();
        if (tMin2 == tMax || tMin2 < 0.0d || tMax < 0.0d) {
            double tMin3 = Double.valueOf(this.mConverter.getValue(this.mMin, true, this.mMin)).doubleValue();
            tMax = Double.valueOf(this.mConverter.getValue(this.mMax, true, this.mMax)).doubleValue();
            tMin = tMin3;
        } else {
            tMin = tMin2;
        }
        this.mScale = new NiceScale(tMin, tMax);
    }

    private void makeLabels() {
        int count = (int) ((this.mScale.getNiceMax() - this.mScale.getNiceMin()) / this.mScale.getTickSpacing());
        double step = -(Math.abs(this.mMax - this.mMin) / ((double) count));
        makeLabels(count + 1, step);
    }

    private void makeLabels(int count) {
        makeLabels(count + 1, (-this.mNiceRange.doubleValue()) / ((double) count));
    }

    private void makeLabels(int iterations, double step) {
        double cur = this.mNiceMax.doubleValue();
        this.mLabels = new ArrayList<>();
        GraphRenderer.GraphLabel last = this.mConverter.getLabel(cur, false, this.mNiceMax.doubleValue());
        this.mLabels.add(last);
        int i = 1;
        while (true) {
            if (i >= iterations) {
                break;
            }
            cur += step;
            GraphRenderer.GraphLabel current = this.mConverter.getLabel(cur, false, this.mNiceMax.doubleValue());
            if (last.equals(current)) {
                this.mUseSmall = true;
                break;
            }
            last = current;
            if (!this.mLabels.contains(current)) {
                this.mLabels.add(current);
            }
            i++;
        }
        if (this.mUseSmall) {
            this.mLabels.clear();
            double cur2 = this.mMax;
            double step2 = -(cur2 / ((double) (iterations - 1)));
            for (int i2 = 0; i2 < iterations; i2++) {
                GraphRenderer.GraphLabel value = this.mConverter.getLabel(cur2, true, this.mMax);
                if (!this.mLabels.contains(value)) {
                    this.mLabels.add(value);
                }
                cur2 += step2;
            }
        }
    }

    class NiceScale {
        private double maxPoint;
        private double maxTicks = 7.0d;
        private double minPoint;
        private double niceMax;
        private double niceMin;
        private double range;
        private double tickSpacing;

        public NiceScale(double min, double max) {
            this.minPoint = min;
            this.maxPoint = max;
            calculate();
        }

        private void calculate() {
            this.range = niceNum(this.maxPoint - this.minPoint, false);
            this.tickSpacing = niceNum(this.range / (this.maxTicks - 1.0d), true);
            this.niceMin = Math.floor(this.minPoint / this.tickSpacing) * this.tickSpacing;
            this.niceMax = Math.ceil(this.maxPoint / this.tickSpacing) * this.tickSpacing;
        }

        private double niceNum(double range, boolean round) {
            double niceFraction;
            double exponent = Math.floor(Math.log10(range));
            double fraction = range / Math.pow(10.0d, exponent);
            if (round) {
                if (fraction < 1.5d) {
                    niceFraction = 1.0d;
                } else if (fraction < 3.0d) {
                    niceFraction = 2.0d;
                } else if (fraction < 7.0d) {
                    niceFraction = 5.0d;
                } else {
                    niceFraction = 10.0d;
                }
            } else if (fraction <= 1.0d) {
                niceFraction = 1.0d;
            } else if (fraction <= 2.0d) {
                niceFraction = 2.0d;
            } else if (fraction <= 5.0d) {
                niceFraction = 5.0d;
            } else {
                niceFraction = 10.0d;
            }
            return Math.pow(10.0d, exponent) * niceFraction;
        }

        public void setMaxTicks(double maxTicks) {
            this.maxTicks = maxTicks;
            calculate();
        }

        public double getNiceMin() {
            return this.niceMin;
        }

        public double getNiceMax() {
            return this.niceMax;
        }

        public double getTickSpacing() {
            return this.tickSpacing;
        }
    }
}
