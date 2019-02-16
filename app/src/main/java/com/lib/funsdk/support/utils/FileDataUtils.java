package com.lib.funsdk.support.utils;

import android.content.Context;
import com.example.funsdkdemo.R;
import com.lib.SDKCONST.PicFileType;
import com.lib.SDKCONST.StreamType;
import com.lib.SDKCONST.VidoFileType;

public class FileDataUtils {
	// "B" 连拍 "L" 延时拍
	/**
	 * 获取录像文件类型
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getStrFileType(Context context, String fileName) {

		if (fileName.endsWith(".h264")) {

            String[] recordType = context.getResources().getStringArray(R.array.record_type);
            
			String typestr = (recordType[0]);
			int pos = fileName.indexOf('[');
			if (pos > 0) {
				String type = fileName.substring(pos + 1, pos + 2);
				if (type.equals("A"))
					typestr = (recordType[1]);
				else if (type.equals("M"))
					typestr = (recordType[2]);
				else if (type.equals("R"))
					typestr = (recordType[3]);
				else if (type.equals("H"))
					typestr = (recordType[4]);
				else if (type.equals("K"))
					typestr = (recordType[5]);
			}
			return typestr;
		} else if (fileName.endsWith(".jpg")) {
            String[] picType = context.getResources().getStringArray(R.array.pic_type);
			String typestr = (picType[0]);
			int pos = fileName.indexOf('[');
			if (pos > 0) {
				String type = fileName.substring(pos + 1, pos + 2);
				if (type.equals("A"))
					typestr = (picType[1]);
				else if (type.equals("M"))
					typestr = (picType[2]);
				else if (type.equals("R"))
					typestr = (picType[3]);
				else if (type.equals("H"))
					typestr = (picType[4]);
				else if (type.equals("K"))
					typestr = (picType[5]);
				else if (type.equals("B"))
					typestr = (picType[6]);
				else if (type.equals("L"))
					typestr = (picType[7]);
			}
			return typestr;
		}
		return "";
	}


	public static int getIntFileType(String fileName) {
		int fileType = 0;
		if (fileName.endsWith(".h264")) {
			int pos = fileName.indexOf('[');
			if (pos > 0 && pos < fileName.length()) {
				String type = fileName.substring(pos + 1, pos + 2);
				if (type.equals("A"))
					fileType = VidoFileType.VI_DETECT;
				else if (type.equals("M"))
					fileType = VidoFileType.VI_MANUAL;
				else if (type.equals("R"))
					fileType = VidoFileType.VI_MANUAL;
				else if (type.equals("H"))
					fileType = VidoFileType.VI_REGULAR;
				else if (type.equals("K"))
					fileType = VidoFileType.VI_KEY;
			}
		} else if (fileName.endsWith(".jpg")) {
			int pos = fileName.indexOf('[');
			if (pos > 0 && pos < fileName.length()) {
				String type = fileName.substring(pos + 1, pos + 2);
				if (type.equals("A"))
					fileType = PicFileType.PIC_DETECT;
				else if (type.equals("M"))
					fileType = PicFileType.PIC_MANUAL;
				else if (type.equals("R"))
					fileType = PicFileType.PIC_MANUAL;
				else if (type.equals("H"))
					fileType = PicFileType.PIC_REGULAR;
				else if (type.equals("K"))
					fileType = PicFileType.PIC_KEY;
				else if (type.equals("B"))
					fileType = PicFileType.PIC_BURST_SHOOT;
				else if (type.equals("L"))
					fileType = PicFileType.PIC_TIME_LAPSE;
			}
		}

		return fileType;
	}

	/**
	 * 获取录像码流类型
	 *
	 * @param fileName
	 * @return
	 */
	public static int getStreamType(String fileName) {
		if (StringUtils.isStringNULL(fileName)) {
			return StreamType.Main;
		} else {
			int index_0 = fileName.indexOf("(", 0);
			int index_1 = fileName.indexOf(")", index_0);
			if (index_0 == index_1) {
				return StreamType.Main;
			}
			String type = fileName.substring(index_0 + 1, index_1);
			try {
				return Integer.parseInt(type);
			} catch (Exception e) {
				return StreamType.Main;
			}
		}
	}

	/**
	 * @param fileName
	 *            文件名
	 * @param type
	 *            0: 唯一的序号 1:相同时间下的序号
	 * @Title: getOrderNum
	 * @Description: TODO(获取序号)
	 */
	public static int getOrderNum(String fileName, int type) {
		int index_0, index_1, index_2;
		String num = "";
		if (StringUtils.isStringNULL(fileName)) {
			return 0;
		} else {
			switch (type) {
			case 0:
				index_0 = fileName.indexOf('[');
				index_1 = fileName.indexOf('[', index_0 + 1);
				index_2 = fileName.indexOf("]", index_1);
				if (index_1 == index_2) {
					return 0;
				}
				num = fileName.substring(index_1 + 1, index_2);
				break;
			case 1:
				index_0 = fileName.lastIndexOf("]");
				index_1 = fileName.lastIndexOf("[");
				if (index_0 == index_1) {
					return 0;
				}
				num = fileName.substring(index_1 + 1, index_0);
				break;
			default:
				break;
			}
			
			try {
				return Integer.parseInt(num);
			} catch (Exception e) {
				return 0;
			}
		}
	}
}
