package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by niutong on 2017-10-19.
 * Discription:
 */

public class CameraFishEye extends BaseConfig{

    public static final String CONFIG_NAME = JsonConfig.CFG_FISH_EYE_PARAM;

    public int AppType;
    public int Duty;
    public int WorkMode;
    public int Secene;
    public LightOnSec mLightOnSec;

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

    public boolean onParse(JSONObject o){
        if (o == null) {
            return false;
        }

        AppType = o.optInt("AppType");
        Duty = o.optInt("Duty");
        WorkMode = o.optInt("WorkMode");
        Secene = o.optInt("Secene");
        try {
            JSONObject jsonObject1 = o.getJSONObject("LightOnSec");
            mLightOnSec = new LightOnSec(jsonObject1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getConfigName() {
        return CONFIG_NAME;
    }

    @Override
    public String getSendMsg() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject.put("Name", CONFIG_NAME);
                jsonObject2.put("AppType", AppType);
                jsonObject2.put("Duty", Duty);
                jsonObject2.put("WorkMode", WorkMode);
                jsonObject2.put("Secene", Secene);
                jsonObject2.put("LightOnSec", mLightOnSec.getSendObjeck());
            jsonObject.put(CONFIG_NAME, jsonObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public class LightOnSec{

        public int EHour;
        public int EMinute;
        public int Enable;
        public int SHour;
        public int SMinute;

        public LightOnSec(JSONObject json){
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
}
