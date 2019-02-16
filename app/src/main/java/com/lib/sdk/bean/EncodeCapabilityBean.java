package com.lib.sdk.bean;

import java.util.List;

/**
* 
* @ClassName: EncodeCapabilityBean 
* @Description: TODO(编码能力配置) 
* @author xxy 
* @date 2016年3月19日 下午4:45:01 
*
*/

public class EncodeCapabilityBean {

	public int MaxEncodePower;
	public int MaxBitrate;
	public int ChannelMaxSetSync;
	public String Compression;
	public String[] ImageSizePerChannel;
	public String[] ExImageSizePerChannel;
	public String[] MaxEncodePowerPerChannel;
	public String[][] ExImageSizePerChannelEx;
	
	public List<EncodeInfo> EncodeInfo;

	public class EncodeInfo {
		public String ResolutionMask;
		public String StreamType;
		public String CompressionMask;
		public boolean Enable;
		public boolean HaveAudio;
	}

	public List<CombEncodeInfo> CombEncodeInfo;

	public class CombEncodeInfo {
		public String ResolutionMask;
		public String StreamType;
		public String CompressionMask;
		public boolean Enable;
		public boolean HaveAudio;
	}

}
