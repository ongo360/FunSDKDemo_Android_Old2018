package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by hws on 2018-08-07.
 */

public class DSTimeBean {
    @JSONField(name = "Year")
    private int year;
    @JSONField(name = "Month")
    private int month;
    @JSONField(name = "Week")
    private int week;
    @JSONField(name = "Day")
    private int day;
    @JSONField(name = "Hour")
    private int hour;
    @JSONField(name = "Minute")
    private int minute;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
