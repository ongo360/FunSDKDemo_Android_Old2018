package com.lib.sdk.bean;

/**
 * 
 * @ClassName: CameraParamExBean
 * @Description: TODO(摄像机扩展参数)
 * @author xxy
 * @date 2016年3月19日 下午4:43:58
 * 
 */

public class CameraParamExBean {
	public String ExposureTime;
	public String Style;
	public BroadTrends BroadTrends;
	public int Dis;
	public int LowLuxMode;
	public int Ldc;
	public int AeMeansure;
	public int CorridorMode;// 0 正常 1 90度 2 180度 3 270度

	public class BroadTrends {
		public int AutoGain;
		public int Gain;
	}
}
