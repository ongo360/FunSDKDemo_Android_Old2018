package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by niutong on 2017-11-20.
 *
 * @author niutong
 *         Discription:
 */

public class FishEyePlatform extends BaseConfig{

    public static final String CONFIG_NAME="FishEyePlatform";

    public int LedAbility;
    public int SetupMode;

    @Override
    public boolean onParse(String json) {
        if (!super.onParse(json)) {
            return false;
        }
        try {
            JSONObject c_jsonobj = mJsonObj.getJSONObject(mJsonObj.getString("Name"));
            LedAbility = c_jsonobj.optInt("LedAbility");
            SetupMode = c_jsonobj.optInt("SetupMode");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return super.onParse(json);
    }


    @Override
    public String getConfigName() {
        return CONFIG_NAME;
    }
}
