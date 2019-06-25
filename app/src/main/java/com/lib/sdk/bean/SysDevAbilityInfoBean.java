package com.lib.sdk.bean;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.funsdkdemo.utils.XUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务器端提供的设备能力集信息
 * Created by hws on 2018-03-13.
 */

public class SysDevAbilityInfoBean {
    private HashMap<String, Object> devAbilityMap = new HashMap<>();
    private SystemInfoBean sysInfo;
    private String sn;
    private int devType;
    private String[] devAbilityNames;
    public SysDevAbilityInfoBean(SystemInfoBean sysInfo, String sn, int devType) {
        this.sysInfo = sysInfo;
        this.sn = sn;
        this.devType = devType;
    }

    public SysDevAbilityInfoBean(String sn) {
        this.sn = sn;
    }

    public String getSendJson(Context context,String ... devAbilityNames) {
        if (devAbilityNames == null || sn == null) {
            return "";
        }
        this.devAbilityNames = devAbilityNames;
        JSONObject sendObj = new JSONObject();
        try {
            if (sysInfo != null) {
                sendObj.put("hw", sysInfo.getHardWare());//设备版本
                sendObj.put("sw", sysInfo.getSoftWareVersion());//设备固件版本
            }
            sendObj.put("tp", devType);//设备类型
            sendObj.put("sn", sn);//设备序列号
            sendObj.put("caps",devAbilityNames);
            sendObj.put("appType", XUtils.getPackageName(context));//APP 包名需要填写
            for (String devAbilityName : devAbilityNames) {
                devAbilityMap.put(devAbilityName,false);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sendObj.toString();
    }

    public boolean parseJson(String sysDevAbilityJson) {
        if (StringUtils.isStringNULL(sysDevAbilityJson)) {
            return false;
        }

        JSONObject jObj;
        try {
            jObj = JSON.parseObject(sysDevAbilityJson);
            if (null == jObj) {
                return false;
            }
            devAbilityMap.putAll(jObj);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isConfigSupport() {
        if (devAbilityNames == null) {
            return false;
        }
        if (devAbilityMap == null || !devAbilityMap.containsKey(devAbilityNames[0])) {
            return false;
        }

        return (boolean) devAbilityMap.get(devAbilityNames[0]);
    }

    public boolean isConfigSupport(String key) {
        if (null == devAbilityMap || !devAbilityMap.containsKey(key)) {
            return false;
        }

        return (boolean) devAbilityMap.get(key);
    }

    public Map<String, Boolean> isConfigSupports() {
        if (null == devAbilityMap) {
            return null;
        }
        Map<String, Boolean> supports = new HashMap<>();
        for(Map.Entry<String, Object> entry: devAbilityMap.entrySet()) {
            String key = entry.getKey();
            if (entry.getValue() instanceof  Boolean) {
                supports.put(key, (Boolean) entry.getValue());
            }
        }
        return supports;
    }
}
