package com.example.gamingpc.easyplants.Models;

public class UserThreshold {

    int thresholdId;
    int thresholdMax;
    int thresholdMin;

    public UserThreshold(int thresholdId, int thresholdMax, int thresholdMin) {
        this.thresholdId = thresholdId;
        this.thresholdMax = thresholdMax;
        this.thresholdMin = thresholdMin;
    }

    public UserThreshold(int thresholdMax, int thresholdMin) {
        this.thresholdMax = thresholdMax;
        this.thresholdMin = thresholdMin;
    }

    public int getThresholdId() {
        return thresholdId;
    }

    public int getThresholdMax() {
        return thresholdMax;
    }

    public int getThresholdMin() {
        return thresholdMin;
    }

    public void setThresholdId(int thresholdId) {
        this.thresholdId = thresholdId;
    }

    public void setThresholdMax(int thresholdMax) {
        this.thresholdMax = thresholdMax;
    }

    public void setThresholdMin(int thresholdMin) {
        this.thresholdMin = thresholdMin;
    }

    public boolean isWithinThreshold(int value){
        return value <= thresholdMax && value >= thresholdMin;
    }
}
