package org.bitbucket.globehacks.models;

/**
 * Created by Emmanuel Victor Garcia on 22/07/2017.
 */

public class GeoPoint {

    private double latitude;
    private double longitude;
    private Store metadata;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Store getMetadata() {
        return metadata;
    }

    public void setMetadata(Store metadata) {
        this.metadata = metadata;
    }
}
