package com.lib.sdk.bean;

public class SystemFunctionBean {
	public AlarmFunction AlarmFunction;
	public EncodeFunction EncodeFunction;
	public NetServerFunction NetServerFunction;
	public OtherFunction OtherFunction;
	public PreviewFunction PreviewFunction;

	public class AlarmFunction {
		public boolean AlarmConfig;
		public boolean BlindDetect;
		public boolean LossDetect;
		public boolean MotionDetect;
		public boolean NetAbort;
		public boolean NetAlarm;
		public boolean NetIpConflict;
		public boolean StorageFailure;
		public boolean StorageLowSpace;
		public boolean StorageNotExist;
	}

	public class EncodeFunction {
		public boolean DoubleStream;
		public boolean SnapStream;
	}

	public class NetServerFunction {
		public boolean Net3G;
		public boolean NetARSP;
		public boolean NetAlarmCenter;
		public boolean NetDDNS;
		public boolean NetDHCP;
		public boolean NetDNS;
		public boolean NetEmail;
		public boolean NetFTP;
		public boolean NetIPFilter;
		public boolean NetMobile;
		public boolean NetMutlicast;
		public boolean NetNTP;
		public boolean NetPPPoE;
		public boolean NetWifi;
	}

	public class OtherFunction {
		public boolean SupportLowLuxMode;
		public boolean SupportSlowMotion;
		public boolean SupportTimeZone;
		public boolean SupportImpRecord;// 标示使能
		public boolean XMModeSwitch;// 模式切换使能
	}

	public class PreviewFunction {
		public boolean Talk;
		public boolean Tour;
	}
}
