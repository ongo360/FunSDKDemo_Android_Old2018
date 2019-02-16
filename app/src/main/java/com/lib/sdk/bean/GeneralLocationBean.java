package com.lib.sdk.bean;

/**
* 
* @ClassName: GeneralLocationBean 
* @Description: TODO(普通配置<Location>) 
* @author xxy 
* @date 2016年3月19日 下午4:46:06 
*
*/

public class GeneralLocationBean {

	public int WorkDay;
	public String DSTRule;
	public String TimeFormat;
	public String DateSeparator;
	public String Language;
	public String DateFormat;
	public String VideoFormat;
	public DSTEnd DSTEnd;

	public class DSTEnd {
		public int Month;
		public int Minute;
		public int Year;
		public int Hour;
		public int Day;
		public int Week;
	}

	public DSTStart DSTStart;

	public class DSTStart {
		public int Month;
		public int Minute;
		public int Year;
		public int Hour;
		public int Day;
		public int Week;
	}
}
