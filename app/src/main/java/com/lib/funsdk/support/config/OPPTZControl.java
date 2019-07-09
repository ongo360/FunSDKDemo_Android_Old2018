package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

public class OPPTZControl extends DevCmdGeneral{

	public static final String CONFIG_NAME = "OPPTZControl";
    public static final int JSON_ID = 1400;	// 云台控制(目前枚举里面还没有,先直接用这个值)
    
    public static final String CMD_SET_PRESET = "SetPreset";
	public static final String CMD_CLEAR_PRESET = "ClearPreset";
	public static final String CMD_GO_TO_PRESET = "GotoPreset";
	public static final String CMD_CORRECT = "Correct";
	

	@Override
	public String getConfigName() {
		// TODO Auto-generated method stub
		return CONFIG_NAME;
	}
	
	@Override
	public int getJsonID() {
		// TODO Auto-generated method stub
		return JSON_ID;
	}

	private String mCommand;
	private int mChannel = 0;
	private int mPreset= 0;
	
	public OPPTZControl() {
		super();
	}
    
	public OPPTZControl(String command, int channel, int preset) {
		super();
		this.mCommand = command;
		this.mChannel = channel;
		this.mPreset = preset;
	}
	
	public String getmCommand() {
		return mCommand;
	}

	public void setmCommand(String mCommand) {
		this.mCommand = mCommand;
	}

	public int getmChannel() {
		return mChannel;
	}

	public void setmChannel(int mChannel) {
		this.mChannel = mChannel;
	}

	public int getmPreset() {
		return mPreset;
	}

	public void setmPreset(int mPreset) {
		this.mPreset = mPreset;
	}

	@Override
	public boolean onParse(String json) {
		return true;
	}

	@Override
	public String getSendMsg() {

        super.getSendMsg();
        JSONObject b_jsonObj = new JSONObject();
        JSONObject paramObj = new JSONObject();
        JSONObject auxObj = new JSONObject();
        JSONObject pointObj = new JSONObject();
        try {
          
            mJsonObj.put("Name", CONFIG_NAME);
                        
            //mJsonObj.put("SessionID", 0x2a);
            b_jsonObj.put("Command", mCommand);
                       
            auxObj.put("Number", 0);
            auxObj.put("Status", "On");
            paramObj.put("AUX",auxObj);
            
            paramObj.put("Channel", mChannel);
            paramObj.put("MenuOpts", "Enter");
            
            pointObj.put("bottom", 0);
            pointObj.put("left", 0);
            pointObj.put("right", 0);
            pointObj.put("top", 0);
            paramObj.put("POINT", pointObj);
            
            paramObj.put("Pattern", "SetBegin");
            paramObj.put("Preset", mPreset);
            paramObj.put("Step", 10);
            paramObj.put("Tour", 0);
            
            b_jsonObj.put("Parameter", paramObj);
            
            mJsonObj.put("OPPTZControl", b_jsonObj);
            mJsonObj.put("SessionID", "0x00001234");
        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
        return mJsonObj.toString();
    
    }

}
