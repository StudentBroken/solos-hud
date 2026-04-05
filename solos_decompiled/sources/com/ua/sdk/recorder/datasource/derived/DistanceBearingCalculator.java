package com.ua.sdk.recorder.datasource.derived;

/* JADX INFO: loaded from: classes65.dex */
public class DistanceBearingCalculator {
    public static void calculateDistanceAndBearing(double lat1, double lon1, double lat2, double lon2, float[] results) {
        double f = (6378137.0d - 6356752.3142d) / 6378137.0d;
        double aSqMinusBSqOverBSq = ((6378137.0d * 6378137.0d) - (6356752.3142d * 6356752.3142d)) / (6356752.3142d * 6356752.3142d);
        double L = (lon2 * 0.017453292519943295d) - (lon1 * 0.017453292519943295d);
        double A = 0.0d;
        double U1 = Math.atan((1.0d - f) * Math.tan(lat1 * 0.017453292519943295d));
        double U2 = Math.atan((1.0d - f) * Math.tan(lat2 * 0.017453292519943295d));
        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;
        double sigma = 0.0d;
        double deltaSigma = 0.0d;
        double cosLambda = 0.0d;
        double sinLambda = 0.0d;
        double lambda = L;
        for (int iter = 0; iter < 20; iter++) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = (cosU1 * sinU2) - ((sinU1 * cosU2) * cosLambda);
            double sinSqSigma = (t1 * t1) + (t2 * t2);
            double sinSigma = Math.sqrt(sinSqSigma);
            double cosSigma = sinU1sinU2 + (cosU1cosU2 * cosLambda);
            sigma = Math.atan2(sinSigma, cosSigma);
            double sinAlpha = sinSigma == 0.0d ? 0.0d : (cosU1cosU2 * sinLambda) / sinSigma;
            double cosSqAlpha = 1.0d - (sinAlpha * sinAlpha);
            double cos2SM = cosSqAlpha == 0.0d ? 0.0d : cosSigma - ((2.0d * sinU1sinU2) / cosSqAlpha);
            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq;
            A = 1.0d + ((uSquared / 16384.0d) * (4096.0d + (((-768.0d) + ((320.0d - (175.0d * uSquared)) * uSquared)) * uSquared)));
            double B = (uSquared / 1024.0d) * (256.0d + (((-128.0d) + ((74.0d - (47.0d * uSquared)) * uSquared)) * uSquared));
            double C = (f / 16.0d) * cosSqAlpha * (4.0d + ((4.0d - (3.0d * cosSqAlpha)) * f));
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B * sinSigma * (((B / 4.0d) * ((((-1.0d) + (2.0d * cos2SMSq)) * cosSigma) - ((((B / 6.0d) * cos2SM) * ((-3.0d) + ((4.0d * sinSigma) * sinSigma))) * ((-3.0d) + (4.0d * cos2SMSq))))) + cos2SM);
            lambda = L + ((1.0d - C) * f * sinAlpha * ((C * sinSigma * ((C * cosSigma * ((-1.0d) + (2.0d * cos2SM * cos2SM))) + cos2SM)) + sigma));
            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.abs(delta) < 1.0E-12d) {
                break;
            }
        }
        float distance = (float) (6356752.3142d * A * (sigma - deltaSigma));
        results[0] = distance;
        if (results.length > 1) {
            float initialBearing = (float) Math.atan2(cosU2 * sinLambda, (cosU1 * sinU2) - ((sinU1 * cosU2) * cosLambda));
            results[1] = (float) (((double) initialBearing) * 57.29577951308232d);
            if (results.length > 2) {
                float finalBearing = (float) Math.atan2(cosU1 * sinLambda, ((-sinU1) * cosU2) + (cosU1 * sinU2 * cosLambda));
                results[2] = (float) (((double) finalBearing) * 57.29577951308232d);
            }
        }
    }
}
