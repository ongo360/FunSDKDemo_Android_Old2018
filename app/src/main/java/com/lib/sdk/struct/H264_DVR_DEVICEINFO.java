package com.lib.sdk.struct;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.basic.G;

public class H264_DVR_DEVICEINFO {
	public byte[] st_00_sSoftWareVersion = new byte[64]; // /< 软件版本信息
	public byte[] st_01_sHardWareVersion = new byte[64]; // /< 硬件版本信息
	public byte[] st_02_sEncryptVersion = new byte[64]; // /< 加密版本信息
	public SDK_SYSTEM_TIME st_03_tmBuildTime = new SDK_SYSTEM_TIME();// /<
																		// 软件创建时间
	public byte[] st_04_sSerialNumber = new byte[64]; // /< 设备序列号
	public int st_05_byChanNum; // /< 视频输入通道数
	public int st_06_iVideoOutChannel; // /< 视频输出通道数
	public int st_07_byAlarmInPortNum; // /< 报警输入通道数
	public int st_08_byAlarmOutPortNum; // /< 报警输出通道数
	public int st_09_iTalkInChannel; // /< 对讲输入通道数
	public int st_10_iTalkOutChannel; // /< 对讲输出通道数
	public int st_11_iExtraChannel; // /< 扩展通道数
	public int st_12_iAudioInChannel; // /< 音频输入通道数
	public int st_13_iCombineSwitch; // /< 组合编码通道分割模式是否支持切换
	public int st_14_iDigChannel; // /<数字通道数(没有编码配置)
	public int st_15_uiDeviceRunTime; // /<系统运行时间
	public int st_16_deviceTye; // /设备类型
	public byte[] st_17_sHardWare = new byte[64]; // /<设备型号
	public byte[] st_18_uUpdataTime = new byte[20]; // /<更新日期 例如 2013-09-03
													// 14:15:13
	public int st_19_uUpdataType; // /<更新内容
	public int st_21_nLanguage;// 国家的语言ID,0:英文/ 1:中文
	public byte st_22_sCloudErrCode[] = new byte[260];// 云登陆具体错误内容
	public int st_23_status[] = new int[32];

	// 判断新过来的连接是不是通过代理转发的，如果是那么按照服务器
	// 返回的限制条件来限制。

	// status[0] 路数限制:0代表不限制， n代表限制n路
	// status[1]码流限制。0 :不限制。1限制不能观看主码流。
	// status[2]限制时间。0:不限制。n:限制n分钟。
	// status[3]限制码率，目前分为四档。0:不限制。1:限制为CIF 6帧 100K ，后续待定
	// status[4]保留位，后续扩充。
	// 其中status[0]和status[1]在此处体现。
	// status[2]和status[3]在传输码流的过程中体现
	public String getBuildTime() {
		return String.format("%04d-%02d-%02d %02d:%02d:%02d",
				st_03_tmBuildTime.st_0_year, st_03_tmBuildTime.st_1_month,
				st_03_tmBuildTime.st_2_day, st_03_tmBuildTime.st_4_hour,
				st_03_tmBuildTime.st_5_minute, st_03_tmBuildTime.st_6_second);
	}

	public int GetChnCount() {
		return st_05_byChanNum + st_14_iDigChannel;
	}

	@Override
	public String toString() {
		return "H264_DVR_DEVICEINFO [st_00_sSoftWareVersion="
				+ G.ToString(st_00_sSoftWareVersion)
				+ ", st_01_sHardWareVersion="
				+ G.ToString(st_01_sHardWareVersion)
				+ ", st_02_sEncryptVersion=" + G.ToString(st_02_sEncryptVersion)
				+ ", st_03_tmBuildTime=" + st_03_tmBuildTime
				+ ", st_04_sSerialNumber=" + G.ToString(st_04_sSerialNumber)
				+ ", st_05_byChanNum=" + st_05_byChanNum
				+ ", st_06_iVideoOutChannel=" + st_06_iVideoOutChannel
				+ ", st_07_byAlarmInPortNum=" + st_07_byAlarmInPortNum
				+ ", st_08_byAlarmOutPortNum=" + st_08_byAlarmOutPortNum
				+ ", st_09_iTalkInChannel=" + st_09_iTalkInChannel
				+ ", st_10_iTalkOutChannel=" + st_10_iTalkOutChannel
				+ ", st_11_iExtraChannel=" + st_11_iExtraChannel
				+ ", st_12_iAudioInChannel=" + st_12_iAudioInChannel
				+ ", st_13_iCombineSwitch=" + st_13_iCombineSwitch
				+ ", st_14_iDigChannel=" + st_14_iDigChannel
				+ ", st_15_uiDeviceRunTime=" + st_15_uiDeviceRunTime
				+ ", st_16_deviceTye=" + st_16_deviceTye + ", st_17_sHardWare="
				+ G.ToString(st_17_sHardWare) + ", st_18_uUpdataTime="
				+ G.ToString(st_18_uUpdataTime) + ", st_19_uUpdataType="
				+ st_19_uUpdataType + ", st_21_nLanguage=" + st_21_nLanguage
				+ ", st_22_sCloudErrCode=" + G.ToString(st_22_sCloudErrCode)
				+ ", st_23_status=" + Arrays.toString(st_23_status) + "]";
	}

	public boolean ParserFromJson(String jsonData) {
		try {
			JSONObject obj = new JSONObject(jsonData);
			JSONObject sys = obj.getJSONObject("SystemInfo");
			if (sys.has("SoftWareVersion"))
				st_00_sSoftWareVersion = (sys.getString("SoftWareVersion"))
						.getBytes();
			if (sys.has("HardWareVersion"))
				st_01_sHardWareVersion = (sys.getString("HardWareVersion"))
						.getBytes();
			if (sys.has("EncryptVersion"))
				st_02_sEncryptVersion = (sys.getString("EncryptVersion"))
						.getBytes();
			if (sys.has("SerialNo"))
				st_04_sSerialNumber = (sys.getString("SerialNo")).getBytes();
			// String str = new String(st_04_sSerialNumber);
			// System.out.println("zsw st_04_sSerialNumber : " + str);
			if (sys.has("HardWare"))
				st_17_sHardWare = (sys.getString("HardWare")).getBytes();
			if (sys.has("UpdataTime"))
				st_18_uUpdataTime = (sys.getString("UpdataTime")).getBytes();
			// st_22_sCloudErrCode =
			// (sys.getString("PowerSocket.AutoLight")).getBytes();
			if (sys.has("VideoInChannel"))
				st_05_byChanNum = sys.getInt("VideoInChannel"); // /< 视频输入通道数
			if (sys.has("VideoOutChannel"))
				st_06_iVideoOutChannel = sys.getInt("VideoOutChannel"); // /<
																		// 视频输出通道数
			if (sys.has("AlarmInChannel"))
				st_07_byAlarmInPortNum = sys.getInt("AlarmInChannel"); // /<
																		// 报警输入通道数
			if (sys.has("AlarmOutChannel"))
				st_08_byAlarmOutPortNum = sys.getInt("AlarmOutChannel"); // /<
																			// 报警输出通道数
			if (sys.has("TalkInChannel"))
				st_09_iTalkInChannel = sys.getInt("TalkInChannel"); // /<
																	// 对讲输入通道数
			if (sys.has("TalkOutChannel"))
				st_10_iTalkOutChannel = sys.getInt("TalkOutChannel"); // /<
																		// 对讲输出通道数
			if (sys.has("ExtraChannel"))
				st_11_iExtraChannel = sys.getInt("ExtraChannel"); // /< 扩展通道数
			if (sys.has("AudioInChannel"))
				st_12_iAudioInChannel = sys.getInt("AudioInChannel"); // /<
																		// 音频输入通道数
			if (sys.has("CombineSwitch"))
				st_13_iCombineSwitch = sys.getInt("CombineSwitch"); // /<
																	// 组合编码通道分割模式是否支持切换
			if (sys.has("DigChannel"))
				st_14_iDigChannel = sys.getInt("DigChannel"); // /<数字通道数(没有编码配置)
			if (sys.has("DeviceRunTime")
					&& sys.getString("DeviceRunTime").length() > 2)
				st_15_uiDeviceRunTime = Integer.parseInt(
						sys.getString("DeviceRunTime").substring(2), 16);// /<系统运行时间
			// System.out.println("zsw RunTime : " + st_15_uiDeviceRunTime);

			// st_16_deviceTye = ; // /设备类型
			if (sys.has("UpdataType")
					&& sys.getString("UpdataType").length() > 2)
				st_19_uUpdataType = Integer
						.parseInt(sys.getString("UpdataType").substring(2), 16);
			if (sys.has("BuildTime")) {
				String buildTime = sys.getString("BuildTime");
				String[] buildTimeArray = buildTime.split(" ");
				if (2 == buildTimeArray.length) {
					String[] dateArray = buildTimeArray[0].split("-");
					st_03_tmBuildTime.st_0_year = Integer
							.parseInt(dateArray[0]);
					st_03_tmBuildTime.st_1_month = Integer
							.parseInt(dateArray[1]);
					st_03_tmBuildTime.st_2_day = Integer.parseInt(dateArray[2]);
					String[] timeArray = buildTimeArray[1].split(":");
					st_03_tmBuildTime.st_4_hour = Integer
							.parseInt(timeArray[0]);
					st_03_tmBuildTime.st_5_minute = Integer
							.parseInt(timeArray[1]);
					st_03_tmBuildTime.st_6_second = Integer
							.parseInt(timeArray[2]);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean ParserFromJson(byte[] pData) {
//		System.out.println("ToStringJson" + G.ToStringJson(pData));
		return ParserFromJson(G.ToStringJson(pData));
	}

}
