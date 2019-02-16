package com.lib.sdk.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class ModeEnableBean {
	@JSONField(name = "AVEnc.VideoWidget")
	private List<VideoWidgetEnableBean> videoWidget;
	@JSONField(name = "Camera.ClearFog")
	private List<ClearFogEnableBean> clearFog;
	@JSONField(name = "Camera.Param")
	private List<CameraParamEnableBean> cameraParam;
	@JSONField(name = "Camera.ParamEx")
	private List<CameraParamExEnableBean> cameraParamEx;
	@JSONField(name = "Consumer.CommDevCfg")
	private CommDevCfgEnableBean commDevCfg;
	@JSONField(name = "FbExtraStateCtrl")
	private FbExtraStateCtrlEnableBean stateCtrl;
	@JSONField(name = "General.General")
	private GeneralEnableBean general;
	@JSONField(name = "NetWork.SetEnableVideo")
	private NetWorkVideoEnableBean netWorkVideo;
	@JSONField(name = "Simplify.Encode")
	private List<SimplifyEncodeEnableBean> encode;
	@JSONField(name = "System.FindDevice")
	private FindDeviceEnableBean findDev;
	@JSONField(name = "System.GSensorConfig")
	private GSensorConfigEnableBean gSensor;
	@JSONField(name = "System.ManageRemote")
	private ManageRemoteEnableBean remote;
	@JSONField(name = "System.ManageShutDown")
	private ManageShutDownEnableBean shutDown;
	@JSONField(name = "System.MangageTips")
	private MangageTipsEnableBean tips;
	@JSONField(name = "fVideo.OsdLogo")
	private OsdLogoEnableBean osdLogo;

	public List<VideoWidgetEnableBean> getVideoWidget() {
		return videoWidget;
	}

	public void setVideoWidget(List<VideoWidgetEnableBean> videoWidget) {
		this.videoWidget = videoWidget;
	}

	public List<ClearFogEnableBean> getClearFog() {
		return clearFog;
	}

	public void setClearFog(List<ClearFogEnableBean> clearFog) {
		this.clearFog = clearFog;
	}

	public List<CameraParamEnableBean> getCameraParam() {
		return cameraParam;
	}

	public void setCameraParam(List<CameraParamEnableBean> cameraParam) {
		this.cameraParam = cameraParam;
	}

	public List<CameraParamExEnableBean> getCameraParamEx() {
		return cameraParamEx;
	}

	public void setCameraParamEx(List<CameraParamExEnableBean> cameraParamEx) {
		this.cameraParamEx = cameraParamEx;
	}

	public CommDevCfgEnableBean getCommDevCfg() {
		return commDevCfg;
	}

	public void setCommDevCfg(CommDevCfgEnableBean commDevCfg) {
		this.commDevCfg = commDevCfg;
	}

	public FbExtraStateCtrlEnableBean getStateCtrl() {
		return stateCtrl;
	}

	public void setStateCtrl(FbExtraStateCtrlEnableBean stateCtrl) {
		this.stateCtrl = stateCtrl;
	}

	public GeneralEnableBean getGeneral() {
		return general;
	}

	public void setGeneral(GeneralEnableBean general) {
		this.general = general;
	}

	public NetWorkVideoEnableBean getNetWorkVideo() {
		return netWorkVideo;
	}

	public void setNetWorkVideo(NetWorkVideoEnableBean netWorkVideo) {
		this.netWorkVideo = netWorkVideo;
	}

	public List<SimplifyEncodeEnableBean> getEncode() {
		return encode;
	}

	public void setEncode(List<SimplifyEncodeEnableBean> encode) {
		this.encode = encode;
	}

	public FindDeviceEnableBean getFindDev() {
		return findDev;
	}

	public void setFindDev(FindDeviceEnableBean findDev) {
		this.findDev = findDev;
	}

	public GSensorConfigEnableBean getgSensor() {
		return gSensor;
	}

	public void setgSensor(GSensorConfigEnableBean gSensor) {
		this.gSensor = gSensor;
	}

	public ManageRemoteEnableBean getRemote() {
		return remote;
	}

	public void setRemote(ManageRemoteEnableBean remote) {
		this.remote = remote;
	}

	public ManageShutDownEnableBean getShutDown() {
		return shutDown;
	}

	public void setShutDown(ManageShutDownEnableBean shutDown) {
		this.shutDown = shutDown;
	}

	public MangageTipsEnableBean getTips() {
		return tips;
	}

	public void setTips(MangageTipsEnableBean tips) {
		this.tips = tips;
	}

	public OsdLogoEnableBean getOsdLogo() {
		return osdLogo;
	}

	public void setOsdLogo(OsdLogoEnableBean osdLogo) {
		this.osdLogo = osdLogo;
	}
}
