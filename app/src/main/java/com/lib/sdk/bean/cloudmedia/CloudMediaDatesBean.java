package com.lib.sdk.bean.cloudmedia;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.lib.sdk.bean.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hws on 2017-11-28.
 * 云存储 日历
 */

public class CloudMediaDatesBean {
    private List<CloudDate> dateTimes;
    public HashMap<Object, Boolean> parseJson(String json) {
        JSONObject jObj;
        HashMap<Object,Boolean> validDataMap = null;
        try {
            if (StringUtils.isStringNULL(json)) {
                return null;
            }
            jObj = JSON.parseObject(json);
            if (null == jObj) {
                return null;
            }
            JSONObject alarmCenterObj = jObj.getJSONObject("AlarmCenter");
            if (null == alarmCenterObj) {
                return null;
            }
            if (alarmCenterObj.containsKey("Body")) {
                JSONObject bodyObj = alarmCenterObj.getJSONObject("Body");
                if (bodyObj.containsKey("Date")) {
                    validDataMap = new HashMap<>();
                    dateTimes =  JSON.parseArray(bodyObj.getString("Date"),CloudDate.class);
                    for (CloudDate cloudDate : dateTimes) {
                        if (!StringUtils.isStringNULL(cloudDate.getTime())) {
                            validDataMap.put(cloudDate.getTime().replace("-", ""), true);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return validDataMap;
    }

    public static class CloudDate {
        @JSONField(name = "Time")
        private String time;
        public void setTime(String time) {
            this.time = time;
        }
        public String getTime() {
            return time;
        }
    }

    public List<CloudDate> getDateTimes() {
        return dateTimes;
    }

}
