package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by niutong on 2017-11-20.
 *
 * @author niutong
 *         Discription:
 */

public class CameraWhiteLight extends BaseConfig{

    public static final String CONFIGNAME="Camera.WhiteLight";

    public String WorkMode;
    public WorkPeriod workPeriod;
    public MoveTrigLight moveTrigLight;

    @Override
    public boolean onParse(String json) {
        if (!super.onParse(json)) {
            return false;
        }
        try {
            JSONObject c_jsonobj = mJsonObj.getJSONObject(mJsonObj.getString("Name"));
            return onParse(c_jsonobj);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return super.onParse(json);
    }

    private boolean onParse(JSONObject jsonObject){
        if (jsonObject == null) {
            return false;
        }

        WorkMode = jsonObject.optString("WorkMode");
        try {
            workPeriod = new WorkPeriod(jsonObject.getJSONObject("WorkPeriod"));
            if (jsonObject.getJSONObject("MoveTrigLight") != null) {
                moveTrigLight = new MoveTrigLight(jsonObject.getJSONObject("MoveTrigLight"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public String getSendMsg() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject.put("Name", CONFIGNAME);
            jsonObject1.put("WorkMode", WorkMode);
            jsonObject1.put("WorkPeriod", workPeriod.getSendObjeck());
            if (moveTrigLight != null) {
                jsonObject1.put("MoveTrigLight", moveTrigLight.getSendObject());
            }
            jsonObject.put(CONFIGNAME, jsonObject1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public String getConfigName() {
        return CONFIGNAME;
    }

    public class WorkPeriod{
            public int EHour;
            public int EMinute;
            public int Enable;
            public int SHour;
            public int SMinute;

            public WorkPeriod(JSONObject json){
                if (json == null){
                    return;
                }
                EHour = json.optInt("EHour");
                EMinute = json.optInt("EMinute");
                Enable = json.optInt("Enable");
                SHour = json.optInt("SHour");
                SMinute = json.optInt("SMinute");
            }

            public JSONObject getSendObjeck(){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("EHour", EHour);
                    jsonObject.put("EMinute", EMinute);
                    jsonObject.put("Enable", Enable);
                    jsonObject.put("SHour", SHour);
                    jsonObject.put("SMinute", SMinute);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return jsonObject;
            }

    }

    public class MoveTrigLight{
        public int Level;
        public int Duration;

        public MoveTrigLight(JSONObject jsonObject){
            Level = jsonObject.optInt("Level");
            Duration = jsonObject.optInt("Duration");
        }

        public JSONObject getSendObject(){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Level", Level);
                jsonObject.put("Duration", Duration);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
}
