package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SD卡分区格式化命令
 */
public class OPStorageManager extends BaseConfig {

    public static final String CONFIG_NAME = JsonConfig.OPSTORAGE_MANAGER;

    @Override
    public String getConfigName() {
        return CONFIG_NAME;
    }

    private String Action;
    private int PartNo;
    private int SerialNo;
    private String Type;

    public String getAction() {
		return Action;
	}

	public void setAction(String action) {
		this.Action = action;
	}

	public int getPartNo() {
		return PartNo;
	}

	public void setPartNo(int partNo) {
		this.PartNo = partNo;
	}

	public int getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(int serialNo) {
		this.SerialNo = serialNo;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		this.Type = type;
	}

    public String getSendMsg() {

        super.getSendMsg();
        try {
            JSONObject c_jsonObj = null;
            mJsonObj.put("Name", CONFIG_NAME);
            c_jsonObj = new JSONObject();
            c_jsonObj.put("Action", Action);
            c_jsonObj.put("PartNo", PartNo);
            c_jsonObj.put("SerialNo", SerialNo);
            c_jsonObj.put("Type", Type);
            mJsonObj.put(CONFIG_NAME, c_jsonObj);
        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
        return mJsonObj.toString();
    }

    @Override
    public boolean onParse(String json) {
        return true;
    }
}
