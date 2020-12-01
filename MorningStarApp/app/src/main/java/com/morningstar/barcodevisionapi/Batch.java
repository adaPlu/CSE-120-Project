package com.morningstar.barcodevisionapi;

import java.util.Arrays;
import java.util.Objects;

public class Batch {
    private int batchID;
    private int numOfContainers;
    private double latitude;
    private double longitude;

    //Default Constructor
    public Batch(){
        batchID =0;
        numOfContainers =0;
        latitude = 0.0;
        longitude = 0.0;
    }
    public Batch (int batchID, int numOfContainers, double latitude, double longitude){
       this.batchID =  batchID;
        this.numOfContainers = numOfContainers;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchID=" + batchID +
                ", numOfContainers=" + numOfContainers +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Batch batch = (Batch) o;
        return batchID == batch.batchID &&
                numOfContainers == batch.numOfContainers &&
                Double.compare(batch.latitude, latitude) == 0 &&
                Double.compare(batch.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchID, numOfContainers, latitude, longitude);
    }

    //Setters
    public void setNumOfContainers(int numOfContainers) {
        this.numOfContainers = numOfContainers;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }


    //Getters
    public int getNumOfContainers() {
        return numOfContainers;
    }

    public int getBatchID() {
        return batchID;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
