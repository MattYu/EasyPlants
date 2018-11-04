package com.example.gamingpc.easyplants.Models;


import java.util.Date;

public class SensorData {

    int dataId;
    int sensorId;
    int humidityValue;
    int humidityAlert;
    Date alertDate;
    int readStatus;

    public SensorData(int dataId, int sensorId, int humidityValue, int humidityAlert, Date alertDate, int readStatus) {
        //For fetching data
        this.dataId = dataId;
        this.sensorId = sensorId;
        this.humidityValue = humidityValue;
        this.humidityAlert = humidityAlert;
        this.alertDate = alertDate;
        this.readStatus = readStatus;
    }

    public SensorData(int sensorId, int humidityValue, int humidityAlert) {
        //for new SensorData

        this.dataId = -1;
        this.sensorId = sensorId;
        this.humidityValue = humidityValue;
        this.humidityAlert = humidityAlert;
        this.alertDate = new Date();
        this.readStatus = 0;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
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

    public int getHumidityAlert() {
        return humidityAlert;
    }

    public void setHumidityAlert(int humidityAlert) {
        this.humidityAlert = humidityAlert;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
