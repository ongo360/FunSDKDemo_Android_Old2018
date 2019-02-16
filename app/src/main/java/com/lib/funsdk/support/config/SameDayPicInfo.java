package com.lib.funsdk.support.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.basic.G;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.sdk.struct.SDK_SYSTEM_TIME;

public class SameDayPicInfo {
	private int mPicNum = 0;// 照片张数
	private SDK_SYSTEM_TIME mTime = new SDK_SYSTEM_TIME();// 时间
	private List<FunFileData> mPicDatas;// 照片信息
	private List<OPCompressPic> mOpCompressPicList;
	private HashMap<Integer, Boolean> mSelectedMap = new HashMap<Integer, Boolean>();

	private boolean mHasRequestFileNum = false;
	private boolean mHasGotFileNum = false;
	
	public void setPicNum(int picNum) {
		if (picNum > 0) {
			mPicNum = picNum;
			mPicDatas = new ArrayList<FunFileData>();
			if (null == mOpCompressPicList) {
				mOpCompressPicList = new ArrayList<OPCompressPic>();
			}
			mOpCompressPicList.clear();
			OPCompressPic mOpCompressPic;
			for (int i = 0; i < picNum; ++i) {
				mOpCompressPic = new OPCompressPic();
				mOpCompressPic.setWidth(160);
				mOpCompressPic.setHeight(90);
				mOpCompressPic.setIsGeo(1);
				mOpCompressPicList.add(mOpCompressPic);
				mPicDatas.add(new FunFileData(new H264_DVR_FILE_DATA(), mOpCompressPic));
			}
		} else {
			mPicNum = picNum;
		}
		
		// 设置标志:已经获取到了文件个数
		setGetFileNum(true);
	}

	public int getPicNum() {
		return mPicNum;
	}
	
	public boolean hasRequestFileNum() {
		return mHasRequestFileNum;
	}
	
	public void setRequestFileNum(boolean requested) {
		mHasRequestFileNum = requested;
	}
	
	public boolean hasGotFileNum() {
		return mHasGotFileNum;
	}
	
	public void setGetFileNum(boolean hasGot) {
		mHasGotFileNum = hasGot;
	}

	public void setTime(int year, int month, int day) {
		mTime.st_0_year = year;
		mTime.st_1_month = month;
		mTime.st_2_day = day;
	}

	public SDK_SYSTEM_TIME getTime() {
		return mTime;
	}
	
	public Date getDate() {
		return new Date(mTime.st_0_year - 1900,
				mTime.st_1_month - 1, 
				mTime.st_2_day, 
				mTime.st_4_hour,
				mTime.st_5_minute, 
				mTime.st_6_second);
	}
	
	public String getDispDate() {
		return String.format("%04d-%02d-%02d",
				mTime.st_0_year, mTime.st_1_month, mTime.st_2_day);
	}

	public void setDayTime(int hour, int minute, int second) {
		mTime.st_4_hour = hour;
		mTime.st_5_minute = minute;
		mTime.st_6_second = second;
	}

	public SDK_SYSTEM_TIME getDayTime() {
		return mTime;
	}

//	public void addPicData(int position, H264_DVR_FILE_DATA data) {
//		synchronized (mPicDatas) {
//			if (null != data && position < mPicNum) {
//				data.isEffective = true;
//				mPicDatas.set(position, data);
//			}
//		}
//	}

	public void removePicData(int position) {
		synchronized (mPicDatas) {
			if (position < mPicNum) {
				mPicDatas.remove(position);
				mPicNum--;
			}
		}
	}

	public List<FunFileData> getAllPicData() {
		return mPicDatas;
	}

	public FunFileData getPicData(int position) {
		if (position < mPicNum) {
			return mPicDatas.get(position);
		}
		return null;
	}
	
	private FunFileData getPicDataByFileName(String fileName) {
		for ( FunFileData fileData : mPicDatas ) {
			if ( fileData.hasSeachedFile() ) {
				if ( fileData.getFileName().equals(fileName) ) {
					// 已经存在了
					return fileData;
				}
			} else {
				// 遇到已经搜索到无效的文件信息都没有的话,直接返回,以标识替换到这个坑位
				return fileData;
			}
		}
		return null;
	}
	
	public void setPicData(H264_DVR_FILE_DATA data) {
		if ( null == data ) {
			return;
		}
		String fileName = G.ToString(data.st_2_fileName);
		if ( null == fileName
				|| fileName.length() == 0 ) {
			// 无效的信息
			FunLog.e("setPicData", "unuseful file name !");
			return;
		}
		FunFileData fileData = getPicDataByFileName(fileName);
		if ( null == fileData ) {
			// 没有坑位了,程序有问题
			FunLog.e("setPicData", "has no empty file data !");
			return;
		}
		
		fileData.parseFromData(data);
	}

	public List<OPCompressPic> getOpCompressPicList() {
		return mOpCompressPicList;
	}

	public OPCompressPic getmOpCompressPicList(int pos) {
		if (null == mOpCompressPicList)
			return null;
		if (pos >= mOpCompressPicList.size())
			return null;
		return mOpCompressPicList.get(pos);
	}

	public void initOPCompressPic() {
		if (null == mOpCompressPicList) {
			mOpCompressPicList = new ArrayList<OPCompressPic>();
		}
		mOpCompressPicList.clear();
		OPCompressPic mOpCompressPic;
		if (null == mPicDatas)
			return;
		for (int i = 0; i < mPicDatas.size(); ++i) {
			mOpCompressPic = new OPCompressPic();
			mOpCompressPic.setWidth(160);
			mOpCompressPic.setHeight(90);
			mOpCompressPic.setIsGeo(1);
			mOpCompressPicList.add(mOpCompressPic);
		}
	}

	public void setOpCompressPicList(List<OPCompressPic> opCompressPicList) {
		this.mOpCompressPicList = opCompressPicList;
	}

	public void setSelected(int pos, boolean sel) {
		mSelectedMap.put(pos, sel);
		FunLog.e("SameDayPicInfo", "childPos pos:" + pos
				+ " sel:" + sel);
	}

	public boolean isSelected(int pos) {
		return mSelectedMap.containsKey(pos) ? mSelectedMap.get(pos) : false;
	}

	public void unSelectedAll() {
		mSelectedMap.clear();
	}
}
