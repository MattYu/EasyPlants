package com.example.gamingpc.easyplants.Models;


import java.util.Date;

public class SensorData {

    int sensorId;
    int humidityValue;
    int sensorSecurityCode;
    int sensorThresholdMax;
    int sensorThresholdMin;
    Date alertDate;


    public SensorData(int sensorId, int humidityValue, int sensorSecurityCode, int sensorThresholdMax, int sensorThresholdMin, Date alertDate) {
        this.sensorId = sensorId;
        this.humidityValue = humidityValue;
        this.sensorSecurityCode = sensorSecurityCode;
        this.sensorThresholdMax = sensorThresholdMax;
        this.sensorThresholdMin = sensorThresholdMin;
        this.alertDate = alertDate;
    }



    public SensorData(int sensorId, int humidityValue, int sensorSecurityCode, Date alertDate) {
        //For fetching data
        this.sensorId = sensorId;
        this.humidityValue = humidityValue;
        this.sensorSecurityCode = sensorSecurityCode;
        this.alertDate = alertDate;

    }

    public SensorData(int sensorId, int humidityValue, int sensorSecurityCode) {
        //for new SensorData

        this.sensorId = sensorId;
        this.humidityValue = humidityValue;
        this.sensorSecurityCode = sensorSecurityCode;
        this.alertDate = new Date();
    }


    public int getSensorThresholdMax() {
        return sensorThresholdMax;
    }

    public void setSensorThresholdMax(int sensorThresholdMax) {
        this.sensorThresholdMax = sensorThresholdMax;
    }

    public int getSensorThresholdMin() {
        return sensorThresholdMin;
    }

    public void setSensorThresholdMin(int sensorThresholdMin) {
        this.sensorThresholdMin = sensorThresholdMin;
    }


    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(int humidityValue) {
        this.humidityValue = humidityValue;
    }

    public int getSensorSecurityCode() {
        return sensorSecurityCode;
    }

    public void setSensorSecurityCode(int sensorSecurityCode) { this.sensorSecurityCode = sensorSecurityCode;}

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

}
