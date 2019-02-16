package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

public class AlarmInfo extends BaseConfig {
	
	public static final String CONFIG_NAME = "AlarmInfo";
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	private String id;
	private int channel;
	private String event;
	private String startTime;
	private String status;
	private long picSize;
	private String pic;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getTime() {
		if ( null == startTime ) {
			return null;
		}
		
		int spacePos = startTime.indexOf(" ");
		if ( spacePos > 0 ) {
			return startTime.substring(spacePos + 1);
		}
		
		return startTime;
	}
	
	public String getDate() {
		if ( null == startTime ) {
			return null;
		}
		
		int spacePos = startTime.indexOf(" ");
		if ( spacePos > 0 ) {
			return startTime.substring(0, spacePos);
		}
		
		return startTime;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getPicSize() {
		return picSize;
	}

	public void setPicSize(long picSize) {
		this.picSize = picSize;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	@Override
	public boolean onParse(String json) {
		// TODO Auto-generated method stub
		if (!super.onParse(json))
			return false;
		try {
			JSONObject c_jsonobj = mJsonObj.getJSONObject(getConfigName());
			if ( mJsonObj.has("ID") ) {
				setId(mJsonObj.getString("ID"));
			}
			if ( mJsonObj.has("picSize") ) {
				setPicSize(Long.parseLong(mJsonObj.getString("picSize")));
			}
			return onParse(c_jsonobj);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean onParse(JSONObject obj) throws JSONException {
		if (null == obj)
			return false;
		setChannel(obj.getInt("Channel"));
		setEvent(obj.getString("Event"));
		setStartTime(obj.getString("StartTime"));
		setStatus(obj.getString("Status"));
		setPic(obj.optString("Pic"));
		return true;
	}

	@Override
	public String toString() {
		return "AlarmInfo [id=" + id + ", channel=" + channel + ", event="
				+ event + ", startTime=" + startTime + ", status=" + status
				+ ", picSize=" + picSize + "]";
	}
}
