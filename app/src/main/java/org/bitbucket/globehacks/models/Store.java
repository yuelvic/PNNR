package org.bitbucket.globehacks.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public class Store {

    private String objectId;
    private String contactNumber;
    private String name;
    private double latitude;
    private double longitude;
    private String owner;
    private boolean availability;
    @SerializedName("operation_time")
    private String operationTime;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getMobile() {
        return contactNumber;
    }

    public void setMobile(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
