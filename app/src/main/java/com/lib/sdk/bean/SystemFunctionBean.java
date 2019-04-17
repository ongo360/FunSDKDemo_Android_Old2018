package com.lib.sdk.bean;

public class SystemFunctionBean {
	public AlarmFunction AlarmFunction = new AlarmFunction();
	public EncodeFunction EncodeFunction = new EncodeFunction();
	public NetServerFunction NetServerFunction = new NetServerFunction();
	public OtherFunction OtherFunction = new OtherFunction();
	public PreviewFunction PreviewFunction = new PreviewFunction();

	public class AlarmFunction {
		public boolean AlarmConfig; //报警配置
		public boolean BlindDetect; //遮挡
		public boolean LossDetect;//丢失侦测
		public boolean MotionDetect;//移动侦测
		public boolean NetAbort;//网络终止
		public boolean NetAlarm;//网络报警
		public boolean NetIpConflict;//ip冲突
		public boolean StorageFailure;//存储失败
		public boolean StorageLowSpace;//存储空间不足
		public boolean StorageNotExist;//硬盘不存在

		public boolean Consumer433Alarm;
		public boolean ConsumerRemote;
		public boolean IPCAlarm;//ipc报警
		public boolean NetAbortExtend;//网络异常扩展
		public boolean SensorAlarmCenter;
		public boolean SerialAlarm;
		public boolean VideoAnalyze;//视频分析
		public boolean NewVideoAnalyze;//新智能分析
	}

	public class EncodeFunction {
		public boolean DoubleStream;//双码流
		public boolean SnapStream;//抓图
		public boolean WaterMark;//水印
		public boolean CombineStream;
		public boolean SmartH264; //图像增强
		public boolean SmartH264V2;//图像增强
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
		public boolean DownLoadPause;//录像下载暂停
		public boolean USBsupportRecord;//usb支持录像
		public boolean SDsupportRecord;//SD支持录像
		public boolean SupportOnvifClient;//是否支持ONVIF客户端
		public boolean SupportNetLocalSearch;//是否支持远程搜索
		public boolean SupportMaxPlayback;//是否支持最大回放通道数显示
		public boolean SupportNVR;//是否是专业NVR
		public boolean SupportC7Platform;
		public boolean SupportMailTest;
		public boolean HideDigital;//通道模式屏蔽
		public boolean NotSupportAH;//水平锐度
		public boolean NotSupportAV;//垂直锐度
		public boolean SupportBT;//宽动态
		public boolean NotSupportTalk;//对讲
		public boolean AlterDigitalName;//数字通道名称修改
		public boolean SupportShowConnectStatus;//支持显示wifi 3G 主动注册等的连接状态
		public boolean SupportPlayBackExactSeek;//支持回放精准定位
		public boolean TitleAndStateUpload;//通道标题和数字通道状态上传能力集
		public boolean MusicFilePlay;
		public boolean SupportSetDigIP;//设置前端ip
		public boolean SupportShowProductType;
		public boolean SupportCamareStyle;//支持摄像机图像风格
		public boolean Supportonviftitle;
		public boolean ShowFalseCheckTime;
		public boolean SupportStatusLed;//是否支持状态灯控制
		public boolean SupportLowLuxMode;
		public boolean SupportSlowMotion;
		public boolean SupportTimeZone;
		public boolean SupportImpRecord;// 标示使能
		public boolean XMModeSwitch;// 模式切换使能
		public boolean SupportSetPTZPresetAttribute;  //支持设置预置点
		public boolean SupportConsSensorAlarmLink; //支持智联报警联动
		public boolean SupportPTZTour;//支持巡航
		public boolean SupportSetSnapFormat; //支持设置拍照画质
		public boolean SupportCapturePriority;//支持设置拍照优先
		public boolean SupportWifiSmartWakeup;//支持设置wifi唤醒
		public boolean SupportPushLowBatteryMsg;//支持低电量提醒（门铃）
		public boolean SupportDoorLock;//支持门锁
		public boolean SupportReserveWakeUp;//支持门铃来电预约
		public boolean SupportNoDisturbing;//支持免打扰功能
		public boolean SupportElectronicPTZ;//支持电子云台能力集
		public boolean SupportAlarmVoiceTips;//提示音
		public boolean SupportNetWorkMode;//支持网络模式切换
		public boolean SupportCameraWhiteLight;//支持基础白光灯
		public boolean SupportDoubleLightBulb;//支持双光灯
		public boolean SupportDoubleLightBoxCamera;//支持双光枪机
		public boolean SupportMusicLightBulb;//支持音乐灯
		public boolean WifiModeSwitch;//支持AP和路由模式的切换
		public boolean WifiRouteSignalLevel;//支持设备WiFi信号强度获取
		public boolean SupportSuspiciousDetection;//支持可疑检测
		public boolean SmartH264;//支持图像增强能力集
		public boolean SupportDNChangeByImage;//日夜切换灵敏度能力集
		public boolean SupportNotifyLight;//支持呼吸灯
		public boolean SupportPirAlarm = true;//支持PIR人体感应 为了规避双向门铃没有这个字段
		public boolean SupportIntervalWakeUp = false;//间隔录像能力集
		public boolean SupportKeySwitchManager = false;//按鍵管理能力集
		public boolean SupportDetectTrack;//人形跟随
		public boolean SupportDevRingControl;//外机按铃声音控制
		public boolean SupportForceShutDownControl;//永久不关机
		public boolean SupportPirTimeSection;//PIR徘徊检测时间段
		public boolean Support433Ring;//433响铃
		public boolean SupportSetVolume;//可控制设备的喇叭和mic的音量
		public boolean SupportAppBindFlag;
		public boolean SupportGetMcuVersion;//获取单片机版本号
		public boolean SupportBallTelescopic;//是否支持电子放大
		public boolean SupportCorridorMode;//是否支持走廊模式，就是90度旋转
		public boolean SupportSoftPhotosensitive;//软光敏功能
	}

	public class PreviewFunction {
		public boolean Talk;
		public boolean Tour;
	}
}
