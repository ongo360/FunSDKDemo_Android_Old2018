package com.lib.funsdk.support.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import com.basic.G;
import com.lib.funsdk.support.utils.FileDataUtils;
import com.lib.funsdk.support.config.OPCompressPic;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.sdk.struct.SDK_SYSTEM_TIME;

public class FunFileData {

	private String mFileName;
	private int mFileType;
	private int mStreamType;
	private String mFileBeginDate;
	private String mFileBeginTime;
    private String mFileEndDate;
    private String mFileEndTime;
    private String mCaptTempPath = null;  // 播放视频时截图的保存位置（缩略图用）
	
	private H264_DVR_FILE_DATA mFileData = null;
	private OPCompressPic mFileOpComPic = null;
	
	public FunFileData(H264_DVR_FILE_DATA fileData, OPCompressPic op) {
		this.parseFromData(fileData);
		mFileOpComPic = op;
	}
	
	public void parseFromData(H264_DVR_FILE_DATA fileData) {
		this.mFileData = fileData;
		this.mFileName = G.ToString(fileData.st_2_fileName);
		this.mFileType = FileDataUtils.getIntFileType(mFileName);
		this.mStreamType = fileData.st_6_StreamType;
		this.mFileBeginDate = String.format("%04d-%02d-%02d",
				fileData.st_3_beginTime.st_0_year,
				fileData.st_3_beginTime.st_1_month,
				fileData.st_3_beginTime.st_2_day);
		this.mFileBeginTime = String.format("%02d:%02d:%02d",
				fileData.st_3_beginTime.st_4_hour,
				fileData.st_3_beginTime.st_5_minute,
				fileData.st_3_beginTime.st_6_second);
        this.mFileEndDate = String.format("%04d-%02d-%02d",
                fileData.st_4_endTime.st_0_year,
                fileData.st_4_endTime.st_1_month,
                fileData.st_4_endTime.st_2_day);
        this.mFileEndTime = String.format("%02d:%02d:%02d",
                fileData.st_4_endTime.st_4_hour,
                fileData.st_4_endTime.st_5_minute,
                fileData.st_4_endTime.st_6_second);
	}
	
	public OPCompressPic getOPCompressPic() {
		return mFileOpComPic;
	}

    public void setFileName(String name) {
        if(!TextUtils.isEmpty(name)){
            G.SetValue(mFileData.st_2_fileName, name);
            mFileName = name;
        }
    }

	public String getFileName() {
		return mFileName;
	}
	
	public int getStreamType() {
		return mStreamType;
	}
	
	public int getFileType() {
		return mFileType;
	}
	
	public String getBeginDateStr() {
		if ( null == mFileBeginDate) {
			return "";
		}
		return mFileBeginDate;
	}
	
	public String getBeginTimeStr() {
		if ( null == mFileBeginTime) {
			return "";
		}
		return mFileBeginTime;
	}

    public String getBeginTimeStr(int hour, int minute, int second) {
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", mFileData.st_3_beginTime.st_0_year,
                mFileData.st_3_beginTime.st_1_month, mFileData.st_3_beginTime.st_2_day, hour, minute, second);
    }

    public String getStartTimeOfYear() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date(
                mFileData.st_3_beginTime.st_0_year - 1900, mFileData.st_3_beginTime.st_1_month - 1,
                mFileData.st_3_beginTime.st_2_day, mFileData.st_3_beginTime.st_4_hour,
                mFileData.st_3_beginTime.st_5_minute, mFileData.st_3_beginTime.st_6_second));
    }

    public String getEndTimeStr() {
        if ( null == mFileEndTime) {
            return "";
        }
        return mFileEndTime;
    }

    public String getEndTimeStr(int hour, int minute, int second) {
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", mFileData.st_4_endTime.st_0_year,
                mFileData.st_4_endTime.st_1_month, mFileData.st_4_endTime.st_2_day, hour, minute, second);
    }

    public String getEndTimeOfYear() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date(
                mFileData.st_4_endTime.st_0_year - 1900, mFileData.st_4_endTime.st_1_month - 1,
                mFileData.st_4_endTime.st_2_day, mFileData.st_4_endTime.st_4_hour,
                mFileData.st_4_endTime.st_5_minute, mFileData.st_4_endTime.st_6_second));
    }


	
	public H264_DVR_FILE_DATA getFileData() {
		return mFileData;
	}

    public void setBeginDate(Date date){
        mFileData.st_3_beginTime.st_0_year = date.getYear() + 1900;
        mFileData.st_3_beginTime.st_1_month = date.getMonth() + 1;
        mFileData.st_3_beginTime.st_2_day = date.getDate();
        mFileData.st_3_beginTime.st_4_hour = date.getHours();
        mFileData.st_3_beginTime.st_5_minute = date.getMinutes();
        mFileData.st_3_beginTime.st_6_second = date.getSeconds();
        parseFromData(mFileData);
    }
	
	public Date getBeginDate() {
		SDK_SYSTEM_TIME beginTm = mFileData.st_3_beginTime;
		return new Date(beginTm.st_0_year - 1900, beginTm.st_1_month - 1,
				beginTm.st_2_day, beginTm.st_4_hour,
				beginTm.st_5_minute, beginTm.st_6_second);
	}

    public void setEndDate(Date date){
        mFileData.st_4_endTime.st_0_year = date.getYear() + 1900;
        mFileData.st_4_endTime.st_1_month = date.getMonth() + 1;
        mFileData.st_4_endTime.st_2_day = date.getDate();
        mFileData.st_4_endTime.st_4_hour = date.getHours();
        mFileData.st_4_endTime.st_5_minute = date.getMinutes();
        mFileData.st_4_endTime.st_6_second = date.getSeconds();
        parseFromData(mFileData);
    }

    public Date getEndDate() {
        SDK_SYSTEM_TIME endTm = mFileData.st_4_endTime;
        return new Date(endTm.st_0_year - 1900, endTm.st_1_month - 1,
                endTm.st_2_day, endTm.st_4_hour,
                endTm.st_5_minute, endTm.st_6_second);
    }

    public boolean hasSeachedFile() {
		return !(null == mFileName || mFileName.length() == 0);
	}
    
    public void setCapTempPath(String path){
    	this.mCaptTempPath = path;
    }
    
    public String getCapTempPath(){
    	return mCaptTempPath;
    }


}
