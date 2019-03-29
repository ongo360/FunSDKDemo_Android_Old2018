package com.lib.funsdk.support.config;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.basic.BaseJson;
import com.lib.sdk.bean.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AlarmInfo extends BaseJson implements Serializable {
    public static final String CLASSNAME = "AlarmInfo";
    private String id;
    private int channel;
    private String event;
    private String eventEx;//0：没有图片 1:本地有图片 2：云端有图片
    private String startTime;
    private String status;
    private long picSize;
    private String sn;
    private String extInfo;
    private String pushMsg;
    private String alarmRing;
    private String pic;
    private LinkCenterExt mLinkCenterExt; //设备上的传感器报警信息
    private boolean videoInfo = false;   // 有则代表有云存储回放 ("VideoInfo":	{"VideoLength":	10})
    private String originJson;  //原始完整的json
    public boolean isVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(boolean videoInfo) {
        this.videoInfo = videoInfo;
    }

    public String getOriginJson() {
        if (null == originJson) {
            jsonObj = new JSONObject();
            try {
                jsonObj.put("ID",id);
                JSONObject object = new JSONObject();
                object.put("Event",event+":"+eventEx);
                object.put("StartTime",startTime);
                object.put("DevMac",sn);
                jsonObj.put("AlarmInfo",object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObj.toString();
        }
        return originJson;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
    
    public String getExtUserData() {
    	try {
    		// userdata: NULL
    		String userdataKey = "userdata:";
    		if ( this.extInfo.contains(userdataKey) ) {
    			String tmp = this.extInfo.substring(this.extInfo.indexOf(userdataKey) + userdataKey.length()).trim();
    			if ( tmp.toLowerCase().equals("null") ) {
    				return null;
    			} else {
    				return tmp;
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

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

    public String getPushMsg() {
        return pushMsg;
    }

    public void setPushMsg(String pushMsg) {
        this.pushMsg = pushMsg;
    }

    public String getAlarmRing() {
        return alarmRing;
    }

    public void setAlarmRing(String alarmRing) {
        this.alarmRing = alarmRing;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public LinkCenterExt getLinkCenterExt() {
        return mLinkCenterExt;
    }

    public void setLinkCenterExt(LinkCenterExt linkCenterExt) {
        mLinkCenterExt = linkCenterExt;
    }

    @Override
    public boolean onParse(String json) {
        if (!super.onParse(json))
            return false;
        try {
            originJson = json;
            JSONObject c_jsonobj = jsonObj.getJSONObject(CLASSNAME);
            setId(jsonObj.getString("ID"));
            setPicSize(Long.parseLong(jsonObj.getString("picSize")));
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
        extInfo = obj.optString("ExtInfo");
        pushMsg = obj.optString("PushMsg");
        alarmRing = obj.optString("AlarmRing");
        pic = obj.optString("Pic");
        mLinkCenterExt = LinkCenterExt.parseJson(extInfo);
        videoInfo = obj.has("VideoInfo");
        return true;
    }

    @Override
    public String toString() {
        return "AlarmInfo [id=" + id + ", channel=" + channel + ", event=" + event + ", startTime=" + startTime
                + ", status=" + status + ", picSize=" + picSize + "]";
    }

    public String getEventEx() {
        return eventEx;
    }

    public void setEventEx(String eventEx) {
        this.eventEx = eventEx;
    }

    public static class LinkCenterExt implements Serializable{
        private String msgType;
        private String msg;
        private String subSn;
        private int modelType;
        private DoorLockOpenCountBean dlOpenCountBean;
        public String getMsgType() {
            return msgType == null ? "" : msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getMsg() {
            return msg == null ? "" : msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSubSn() {
            return subSn == null ? "" : subSn;
        }

        public void setSubSn(String subSn) {
            this.subSn = subSn;
        }

        public int getModelType() {
            return modelType;
        }

        public void setModelType(int modelType) {
            this.modelType = modelType;
        }

        public static LinkCenterExt parseJson(String jsonStr) {
            if (TextUtils.isEmpty(jsonStr))
                return null;
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                LinkCenterExt ext = new LinkCenterExt();
                if (jsonObject.has("MsgType")) {
                    ext.msgType = jsonObject.optString("MsgType");
                }
                if (jsonObject.has("ModelType")) {
                    ext.modelType = jsonObject.optInt("ModelType");
                }
                if (jsonObject.has("Msg")) {
                    ext.msg = jsonObject.optString("Msg");
                }
                if (jsonObject.has("SubSn")) {
                    ext.subSn = jsonObject.getString("SubSn");
                }
                if (jsonObject.has("DoorLockOpenCount")) {
                    String dlOpenCountJson = jsonObject.getString("DoorLockOpenCount");
                    if (!StringUtils.isStringNULL(dlOpenCountJson)) {
                        DoorLockOpenCountBean bean = JSON.parseObject(dlOpenCountJson,DoorLockOpenCountBean.class);
                        ext.setDlOpenCountBean(bean);
                    }
                }
                return ext;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String toString() {
            return "msgType = " + msgType + ";msg = " +msg + ";subSn = " + subSn + ";modelType = " + modelType;
        }

        public DoorLockOpenCountBean getDlOpenCountBean() {
            return dlOpenCountBean;
        }
        public void setDlOpenCountBean(DoorLockOpenCountBean dlOpenCountBean) {
            this.dlOpenCountBean = dlOpenCountBean;
        }
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
}
