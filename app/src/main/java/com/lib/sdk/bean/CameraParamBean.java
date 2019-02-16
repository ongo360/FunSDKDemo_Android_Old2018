package com.lib.sdk.bean;
/**
* 
* @ClassName: CameraParamBean 
* @Description: TODO(摄像机参数) 
* @author xxy 
* @date 2016年3月19日 下午4:41:25 
*
*/
public class CameraParamBean {
	
	public int DncThr;
	public int IRCUTMode;
	public int ElecLevel;
	public int IrcutSwap;
	public int Day_nfLevel;
	public int Night_nfLevel;
	public int AeSensitivity;
	public String EsShutter;
	public String PictureFlip;
	public String WhiteBalance;
	public String ApertureMode;
	public String PictureMirror;
	public String RejectFlicker;
	public String DayNightColor;
	public String BLCMode;
	public GainParam GainParam;

	public class GainParam {
		public int AutoGain;
		public int Gain;
	}

	public ExposureParam ExposureParam;

	public class ExposureParam {
		public int Level;
		public String MostTime;
		public String LeastTime;
	}
}
