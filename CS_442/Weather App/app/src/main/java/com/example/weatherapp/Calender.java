package com.example.weatherapp;

import java.io.Serializable;

public class Calender implements Serializable {
    private String C_day;
    private String C_temp;
    private String C_desc;
    private String C_precp;
    private String C_UV;
    private String Cmorn;
    private String Cafter;
    private String Ceven;
    private String Cnight;
    private Integer iconId;


    public Calender(String c_day, String c_temp, String c_desc, String c_precp, String c_UV, String cmorn, String cafter, String ceven, String cnight, Integer iconId) {
        C_day = c_day;
        C_temp = c_temp;
        C_desc = c_desc;
        C_precp = c_precp;
        C_UV = c_UV;
        Cmorn = cmorn;
        Cafter = cafter;
        Ceven = ceven;
        Cnight = cnight;
        this.iconId = iconId;
    }

    public String getC_day() {
        return C_day;
    }

    public void setC_day(String c_day) {
        C_day = c_day;
    }

    public String getC_temp() {
        return C_temp;
    }

    public void setC_temp(String c_temp) {
        C_temp = c_temp;
    }

    public String getC_desc() {
        return C_desc;
    }

    public void setC_desc(String c_desc) {
        C_desc = c_desc;
    }

    public String getC_precp() {
        return C_precp;
    }

    public void setC_precp(String c_precp) {
        C_precp = c_precp;
    }

    public String getC_UV() {
        return C_UV;
    }

    public void setC_UV(String c_UV) {
        C_UV = c_UV;
    }

    public String getCmorn() {
        return Cmorn;
    }

    public void setCmorn(String cmorn) {
        Cmorn = cmorn;
    }

    public String getCafter() {
        return Cafter;
    }

    public void setCafter(String cafter) {
        Cafter = cafter;
    }

    public String getCeven() {
        return Ceven;
    }

    public void setCeven(String ceven) {
        Ceven = ceven;
    }

    public String getCnight() {
        return Cnight;
    }

    public void setCnight(String cnight) {
        Cnight = cnight;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }
}
