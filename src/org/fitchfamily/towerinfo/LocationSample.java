package org.fitchfamily.towerinfo;

import java.io.ByteArrayOutputStream;

class LocationSample {
    private final double latitude;
    private final double longitude;
    private final double accuracy;      // use radius/range seen as proxy
    private final int samples;
    private double weighting;

    private static final double minAccuracy = 500.0;    // 0.5 Km min assumed
    private static final double minWeighting = 1.0;
    private static final double maxWeighting = 100000.0;    // Assume 100Km
    private static final double ageWeighting = 0.8;

    public LocationSample(double latitude, double longitude, double accuracy, int samples ) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (accuracy < minAccuracy)
            this.accuracy = minAccuracy;
        else
            this.accuracy = accuracy;
        this.weighting = (maxWeighting / this.accuracy);
        if (this.weighting < minWeighting)
            this.weighting = minWeighting;
        this.samples = samples;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getWeight() {
        return weighting;
    }

    public int getSamples() {
        return samples;
    }

    public void ageSample() {
        this.weighting = this.weighting * ageWeighting;
        if (this.weighting < minWeighting)
            this.weighting = 0.0;
    }

    @Override
    public String toString() {
        return "LocationSample{" +
               "lat=" + latitude +
               ", lon=" + longitude +
               ", radius=" + accuracy +
               ", samples=" + samples +
               '}';
    }

}
