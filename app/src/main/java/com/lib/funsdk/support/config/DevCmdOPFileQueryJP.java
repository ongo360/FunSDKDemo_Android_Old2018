package com.lib.funsdk.support.config;

import com.lib.EDEV_JSON_ID;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeff on 16/5/6.
 */
public class DevCmdOPFileQueryJP extends DevCmdGeneral {

    public static final String CONFIG_NAME = "OPFileQuery";
    public static final int JSON_ID = EDEV_JSON_ID.GET_FILENAMES_BY_PATH_REQ;

    @Override
    public int getJsonID() {
        return JSON_ID;
    }

    @Override
    public String getConfigName() {
        return CONFIG_NAME;
    }

    private String mBeginTime = "";
    private String mEndTime = "";
    private int mChannel = 0;
    private String mEvent = "";
    private String mStreamType = "0x00000002";
    private int mFileNum = 0;
    private String mType = "";
    private String mSearchPath = "";
    private List<FunFileData> mDataList;

    public void setType(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    public int getFileNum() {
        return mFileNum;
    }

    public void setFileNum(int fileNum) {
        mFileNum = fileNum;
    }

    public void setBeginTime(String startTime) {
        this.mBeginTime = startTime;
    }

    public String getBeginTime() {
        return mBeginTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        this.mEndTime = endTime;
    }

    public int getChannel() {
        return mChannel;
    }

    public void setChannel(int channel) {
        mChannel = channel;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        mEvent = event;
    }

    public String getStreamType() {
        return mStreamType;
    }

    public void setStreamType(String streamType) {
        mStreamType = streamType;
    }

    public String getSearchPath() {
        return mSearchPath;
    }

    public void setSearchPath(String searchPath) {
        mSearchPath = searchPath;
    }

    public void setFileData(List<FunFileData> list) {
        mDataList = list;
    }

    public List<FunFileData> getFileData() {
        return mDataList;
    }

    public String getSendMsg() {

        super.getSendMsg();
        try {
            JSONObject c_jsonObj = null;
            mJsonObj.put("Name", CONFIG_NAME);
            c_jsonObj = new JSONObject();
            c_jsonObj.put("BeginTime", mBeginTime);
            c_jsonObj.put("Channel", mChannel);
            c_jsonObj.put("EndTime", mEndTime);
            c_jsonObj.put("Event", mEvent);
            c_jsonObj.put("StreamType", mStreamType);
            c_jsonObj.put("FileNum", mFileNum);
            c_jsonObj.put("SearchPath", mSearchPath);
            c_jsonObj.put("FileType", mType);
            mJsonObj.put(CONFIG_NAME, c_jsonObj);
        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
        return mJsonObj.toString();
    }

    public boolean onParse(String json) {
        if (super.onParse(json)) {
            JSONArray c_jsonobj = null;
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            try {
                c_jsonobj = mJsonObj.getJSONArray(CONFIG_NAME);
                /*if (null == mDataList) {
                    return false;
                }*/
                mFileNum = c_jsonobj.length();
                mDataList = new ArrayList<FunFileData>();
                for (int i = 0; i < c_jsonobj.length(); ++i) {
                    mDataList.add(new FunFileData(new H264_DVR_FILE_DATA(), new OPCompressPic()));
                    JSONObject obj = c_jsonobj.getJSONObject(i);
                    mDataList.get(i).setFileName(obj.getString("FileName"));
                    try {
                        if (!obj.isNull("BeginTime")) {
                            mDataList.get(i).setBeginDate(format.parse(obj
                                    .getString("BeginTime")));
                        }
                        if (!obj.isNull("EndTime")) {
                            mDataList.get(i).setEndDate(format.parse(obj
                                    .getString("EndTime")));
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return true;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
