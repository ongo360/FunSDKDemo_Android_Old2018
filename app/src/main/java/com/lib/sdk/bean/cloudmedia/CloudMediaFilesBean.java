package com.lib.sdk.bean.cloudmedia;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lib.funsdk.support.utils.Define;
import com.lib.sdk.bean.StringUtils;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hws on 2017-11-24.
 * 云存储视频文件集
 */

public class CloudMediaFilesBean {
    private Calendar searchCalendar;
    private int fileNum;
    private List<CloudMediaFileInfoBean> fileList;
    public CloudMediaFilesBean(Calendar searchCalendar) {
        this.searchCalendar = searchCalendar;
    }
    public boolean parseJson(String json) {
        JSONObject jObj;
        try {
            if (null != fileList) {
                fileList.clear();
            }
            if (StringUtils.isStringNULL(json)) {
                return false;
            }
            jObj = JSON.parseObject(json);
            if (null == jObj) {
                return false;
            }
            JSONObject alarmCenterObj = jObj.getJSONObject("AlarmCenter");
            if (null == alarmCenterObj) {
                return false;
            }
            if (alarmCenterObj.containsKey("Body")) {
                JSONObject bodyObj = alarmCenterObj.getJSONObject("Body");
                if (bodyObj.containsKey("VideoArray")) {
                    fileList =  JSON.parseArray(bodyObj.getString("VideoArray"),CloudMediaFileInfoBean.class);
                    if (null != fileList) {
                        fileNum = fileList.size();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<H264_DVR_FILE_DATA> cloudMediaInfoToH264FileData() {
        List<H264_DVR_FILE_DATA> list = new ArrayList<H264_DVR_FILE_DATA>();
        for (CloudMediaFileInfoBean info : fileList) {
            H264_DVR_FILE_DATA data = new H264_DVR_FILE_DATA();
            data.downloadType = Define.MEDIA_TYPE_CLOUD;
            Date sDate = info.getStartTimeByYear();
            Date eDate = info.getEndTimeByYear();
            data.st_3_beginTime.st_0_year = sDate.getYear() + 1900;
            data.st_3_beginTime.st_1_month = sDate.getMonth() + 1;
            data.st_3_beginTime.st_2_day = sDate.getDate();
            data.st_3_beginTime.st_4_hour = sDate.getHours();
            data.st_3_beginTime.st_5_minute = sDate.getMinutes();
            data.st_3_beginTime.st_6_second = sDate.getSeconds();
            data.st_4_endTime.st_0_year = eDate.getYear() + 1900;
            data.st_4_endTime.st_1_month = eDate.getMonth() + 1;
            data.st_4_endTime.st_2_day = eDate.getDate();
            data.st_4_endTime.st_4_hour = eDate.getHours();
            data.st_4_endTime.st_5_minute = eDate.getMinutes();
            data.st_4_endTime.st_6_second = eDate.getSeconds();
            data.st_2_fileName = info.getIndexFile().getBytes();
            list.add(data);
        }
        return list;
    }

    public Calendar getSearchCalendar() {
        return searchCalendar;
    }

    public void setSearchCalendar(Calendar searchCalendar) {
        this.searchCalendar = searchCalendar;
    }

    public int getFileNum() {
        return fileNum;
    }

    public List<CloudMediaFileInfoBean> getFileList() {
        return fileList;
    }
}
