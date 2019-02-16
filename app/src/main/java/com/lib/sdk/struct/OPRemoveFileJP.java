package com.lib.sdk.struct;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.BaseJson;

public class OPRemoveFileJP extends BaseJson {
	public static final String CLASSNAME = "OPRemoveFile";
	private ArrayList<FileNameInfo> mFileNameList;
	private HashMap<String, Integer> mFileNameMap;
	private int mFileNum;
	private int mFailFileNum;
	private String[] mFailFileList;

	public OPRemoveFileJP() {
		mFileNameList = new ArrayList<FileNameInfo>();
		mFileNameMap = new HashMap<String, Integer>();
	}

	public void setFileNameInfo(int position, int streamType, int isDir,
			String fileName) {
		mFileNameList.add(new FileNameInfo(position, streamType, isDir,
				fileName));
		mFileNameMap.put(fileName, position);
		mFileNum = mFileNameList.size();
	}

	public void clearDataList() {
		mFileNameList.clear();
		mFileNameMap.clear();
		mFailFileList = null;
	}

	public int getDataListPos(int pos) {
		return mFileNameList.get(pos).getPosition();
	}

	public int getRemoveDataPos(int pos) {
		if (mFileNameMap.containsKey(mFileNameList.get(pos).getFileName())) {
			return mFileNameMap.get(mFileNameList.get(pos).getFileName());
		} else {
			return -1;
		}
	}

	public void setFileNum(int fileNum) {
		this.mFileNum = fileNum;
	}

	public int getFileNum() {
		return mFileNum;
	}

	public int getFailFileNum() {
		return mFailFileNum;
	}

	public void setFailFileNum(int failFileNum) {
		this.mFailFileNum = failFileNum;
	}

	public String getSendMsg() {
		if (mFileNum <= 0) {
			return null;
		}
		super.getSendMsg();
		try {
			JSONObject c_jsonObj = new JSONObject();
			JSONArray array = new JSONArray();
			jsonObj.put("Name", CLASSNAME);
			jsonObj.put("SessionID", "0x0000000c");
			for (FileNameInfo info : mFileNameList) {
				array.put(info.getJsonObj());
			}
			c_jsonObj.put("FileNameList", array);
			c_jsonObj.put("FileNum", mFileNum);
			jsonObj.put(CLASSNAME, c_jsonObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
			mFileNameList.clear();
			return null;
		}
		return jsonObj.toString();
	}

	public boolean isSuccess(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			int iret = obj.getInt("Ret");
			if (iret == 100) {
				return true;
			} else if (iret == 151) {
				JSONObject _obj = obj.getJSONObject("OPRemoveFile");
				setFailFileNum(_obj.getInt("FailFileNum"));
				if (mFailFileNum > 0) {
					mFailFileList = new String[mFailFileNum];
					JSONArray array = _obj.getJSONArray("FailFileList");
					for (int i = 0; i < array.length(); ++i) {
						mFailFileList[i] = (String) array.get(i);
						if (mFileNameMap.containsKey(mFailFileList[i])) {
							mFileNameMap.remove(mFailFileList[i]);
						}
					}
				}
				return false;
			} else {
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public String[] getFailFileList() {
		return mFailFileList;
	}

	public HashMap<String, Integer> getRemoveFileList() {
		return mFileNameMap;
	}

	class FileNameInfo {
		private int mStrmType; // 码流类型 0是主码流
		private int mIsDir;// 是否目录 1是目录
		private String mFileName;
		private int mPosition;// 对应文件列表的位置

		public FileNameInfo(int position, int streamType, int isDir,
				String fileName) {
			this.mStrmType = streamType;
			this.mIsDir = isDir;
			this.mFileName = fileName;
			this.mPosition = position;
		}

		public int getStrmType() {
			return mStrmType;
		}

		public void setStrmType(int strmType) {
			this.mStrmType = strmType;
		}

		public int getIsDir() {
			return mIsDir;
		}

		public void setIsDir(int isDir) {
			this.mIsDir = isDir;
		}

		public String getFileName() {
			return mFileName;
		}

		public void setFileName(String fileName) {
			this.mFileName = fileName;
		}

		public void setPosition(int pos) {
			this.mPosition = pos;
		}

		public int getPosition() {
			return mPosition;
		}

		public JSONObject getJsonObj() throws JSONException {
			JSONObject obj = new JSONObject();
			obj.put("StrmType", mStrmType);
			obj.put("IsDir", mIsDir);
			obj.put("FileName", mFileName);
			return obj;
		}
	}
}
