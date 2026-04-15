package com.example.weatherapp;

import java.io.Serializable;

public class Hours implements Serializable {
    String hourDay;
    String hourTime;
    int hourIconID;
    String hourTemp;
    String hourDesc;


    public Hours(String hourDay, String hourTime, int hourIconID, String hourTemp, String hourDesc) {
        this.hourDay = hourDay;
        this.hourTime = hourTime;
        this.hourIconID = hourIconID;
        this.hourTemp = hourTemp;
        this.hourDesc = hourDesc;
    }

    public String getHourDay() {
        return hourDay;
    }

    public void setHourDay(String hourDay) {
        this.hourDay = hourDay;
    }

    public String getHourTime() {
        return hourTime;
    }

    public void setHourTime(String hourTime) {
        this.hourTime = hourTime;
    }

    public int getHourIconID() {
        return hourIconID;
    }

    public void setHourIconID(int hourIconID) {
        this.hourIconID = hourIconID;
    }

    public String getHourTemp() {
        return hourTemp;
    }

    public void setHourTemp(String hourTemp) {
        this.hourTemp = hourTemp;
    }

    public String getHourDesc() {
        return hourDesc;
    }

    public void setHourDesc(String hourDesc) {
        this.hourDesc = hourDesc;
    }
}
