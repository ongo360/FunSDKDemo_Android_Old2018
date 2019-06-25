package com.lib.sdk.bean.cloudmedia;

import com.alibaba.fastjson.annotation.JSONField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hws on 2017-11-24.
 */

public class CloudMediaFileInfoBean {
    @JSONField(name = "IndexFile")
    private String indexFile;
    @JSONField(name = "StartTime")
    public String startTime;
    @JSONField(name = "StopTime")
    public String endTime;

    @Override
    public String toString() {
        return "startTime:"+startTime+" endTime:"+endTime;
    }

    /**
     * 获取开始时间戳
     * @return
     */
    public Date getStartTimeByYear() {
        if (null == startTime)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(startTime);
            if (null != date) {
                return date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取结束时间戳
     * @return
     */
    public Date getEndTimeByYear() {
        if (null == endTime)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(endTime);
            if (null != date) {
                return date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public long getStartTimes() {
        Date date = getStartTimeByYear();
        if (null != date) {
            long times =  date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds();
            return times;
        }
        return 0;
    }

    public long getEndTimes() {
        Date date = getEndTimeByYear();
        if (null != date) {
            long times =  date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds();
            return times;
        }
        return 0;
    }
    /**
     * 获取文件时长 单位 秒
     * @return
     */
    public int getFileTimeLong() {
        return (int)(getEndTimes() - getStartTimes());
    }


    public String getIndexFile() {
        return indexFile;
    }

    public void setIndexFile(String indexFile) {
        this.indexFile = indexFile;
    }
}
