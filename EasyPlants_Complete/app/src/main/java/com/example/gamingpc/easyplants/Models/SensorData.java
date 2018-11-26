package com.example.gamingpc.easyplants.Models;


import java.util.Date;

public class SensorData implements Comparable<SensorData> {

    int humidityValue;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int seconds;


    public SensorData(int humidityValue, String date) {
        this.humidityValue = humidityValue;
        String[] temp = date.split(".");
        this.year = Integer.parseInt(temp[0]);
        this.month = Integer.parseInt(temp[1]);
        this.day = Integer.parseInt(temp[2]);
        this.hour = Integer.parseInt(temp[3]);
        this.minute = Integer.parseInt(temp[4]);
        this.seconds = Integer.parseInt(temp[5]);
    }


    public int getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(int humidityValue) {
        this.humidityValue = humidityValue;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSeconds() {
        return seconds;
    }

    public static boolean isSameDate(SensorData data, int year, int month, int day){
        return data.year == year && data.month == month && data.day == day;
    }

    @Override
    public int compareTo(SensorData data2){

        int secondsA = hour*3600+minute*60+seconds;
        int secondsB = data2.getHour()*3600 + data2.getMinute()*60 + data2.getSeconds();

        return Integer.compare(secondsA, secondsB);
    }
}
