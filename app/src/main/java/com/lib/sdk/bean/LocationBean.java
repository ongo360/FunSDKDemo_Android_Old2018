package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by hws on 2018-08-07.
 * 本地化配置
 */

public class LocationBean {
    @JSONField(name = "DateFormat")
    private String dateFormat;
    @JSONField(name = "DateSeparator")
    private String dateSeparator;
    @JSONField(name = "TimeFormat")
    private String timeFormat;
    @JSONField(name = "Language")
    private String language;
    @JSONField(name = "VideoFormat")
    private String videoFormat;
    @JSONField(name = "DSTRule")
    private String dSTRule;
    @JSONField(name = "DSTStart")
    private DSTimeBean dSTStart;
    @JSONField(name = "DSTEnd")
    private DSTimeBean dSTEnd;
    @JSONField(name = "WorkDay")
    private int workDay;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateSeparator() {
        return dateSeparator;
    }

    public void setDateSeparator(String dateSeparator) {
        this.dateSeparator = dateSeparator;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    public String getdSTRule() {
        return dSTRule;
    }

    public void setdSTRule(String dSTRule) {
        this.dSTRule = dSTRule;
    }

    public DSTimeBean getdSTStart() {
        return dSTStart;
    }

    public void setdSTStart(DSTimeBean dSTStart) {
        this.dSTStart = dSTStart;
    }

    public DSTimeBean getdSTEnd() {
        return dSTEnd;
    }

    public void setdSTEnd(DSTimeBean dSTEnd) {
        this.dSTEnd = dSTEnd;
    }

    public int getWorkDay() {
        return workDay;
    }

    public void setWorkDay(int workDay) {
        this.workDay = workDay;
    }
}
