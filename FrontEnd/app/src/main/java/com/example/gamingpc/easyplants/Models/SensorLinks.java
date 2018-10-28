package com.example.gamingpc.easyplants.Models;

import java.util.Random;

public class SensorLinks {

    int sensorId;
    int userId;
    String sensorSecurityCode;
    int sensorPairingSuccess;
    int sensorDeleted;

    public SensorLinks(int sensorId, int userId, String sensorSecurityCode, int sensorPairingSuccess, int sensorDeleted) {
        this.sensorId = sensorId;
        this.userId = userId;
        this.sensorSecurityCode = sensorSecurityCode;
        this.sensorPairingSuccess = sensorPairingSuccess;
        this.sensorDeleted = sensorDeleted;
    }

    public SensorLinks(int sensorId, int userId) {
        this.sensorId = sensorId;
        this.userId = userId;
        this.sensorDeleted = 0;
        this.sensorSecurityCode = generateSensorSecurityCode(sensorId, userId);
        this.sensorPairingSuccess = 0;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public int getUserId() {
        return userId;
    }

    public String getSensorSecurityCode() {
        return sensorSecurityCode;
    }

    public boolean compareSecurityCodes(String CodeA, String CodeB){
        return CodeA.equals(CodeB);
    }

    public int getSensorDeleted() {
        return sensorDeleted;
    }

    public void setSensorDeleted(int sensorDeleted) {
        this.sensorDeleted = sensorDeleted;
    }

    public int getSensorPairingSuccess() {
        return sensorPairingSuccess;
    }

    public void setSensorSecurityCode(String sensorSecurityCode) {
        this.sensorSecurityCode = sensorSecurityCode;
    }

    public void setSensorPairingSuccess(int sensorPairingSuccess) {
        this.sensorPairingSuccess = sensorPairingSuccess;
    }

    private String generateSensorSecurityCode(int sensorId, int userId){
        Random rand = new Random();

        long x = (long)(rand.nextDouble()*10000000000000000L);
        int sensorFactor = rand.nextInt()*100;
        int userFactor = rand.nextInt()*1000;
        long time = System.currentTimeMillis();

        return Long.toString((x + sensorFactor * sensorId + userFactor * userId + time) % 9999999999999999L);
    }
}
