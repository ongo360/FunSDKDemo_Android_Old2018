package com.lib.sdk.struct;

import java.io.Serializable;

import com.basic.G;

public class SDBDeviceInfo implements Cloneable, Serializable {
	public byte[] st_0_Devmac = new byte[64]; // 设备mac地址
	public byte[] st_1_Devname = new byte[128]; // 设备名
	public byte[] st_2_Devip = new byte[64]; // ip
	public byte[] st_4_loginName = new byte[16]; // 登录名 默认admin
	public byte[] st_5_loginPsw = new byte[16]; // 登录密码

	// 閸忚泛鐣犳穱鈩冧紖
	public int st_6_nDMZTcpPort;

	public int st_7_nType; // 0 监控 1 插座
	public int st_8_nID; //
	public boolean isOnline = false;
	public int arg1 = 0;
	public boolean isModeOn = false;// 是否开启了延时助眠等模式
	public int index = 0;// 设备在列表中的位置 供排序使用
	private SDK_ChannelNameConfigAll channel;

	public int connectType = 2; // 0:p2p连接，1转发模式 2:IP地址直连

	public SDK_ChannelNameConfigAll getChannel() {
		return channel;
	}

	public void setChannel(SDK_ChannelNameConfigAll channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "SDBDeviceInfo [st_0_Devmac=" + G.ToString(st_0_Devmac)
				+ ", st_1_Devname=" + G.ToString(st_1_Devname)
				+ ", st_4_loginName=" + G.ToString(st_4_loginName)
				+ ", st_5_loginPsw=" + G.ToString(st_5_loginPsw)
				+ ", st_6_nDMZTcpPort=" + st_6_nDMZTcpPort + ", st_7_nType="
				+ st_7_nType + ", st_8_nID=" + st_8_nID + ", channel="
				+ channel + "]";
	}

	@Override
	public Object clone() {
		SDBDeviceInfo info = new SDBDeviceInfo();
		try {
			info = (SDBDeviceInfo) super.clone();
			info.st_0_Devmac = this.st_0_Devmac.clone();
			info.st_1_Devname = this.st_1_Devname.clone();
			// info.st_2_Devip = this.st_2_Devip.clone();
			// info.st_3_devPort = this.st_3_devPort.clone();
			info.st_4_loginName = this.st_4_loginName.clone();
			info.st_5_loginPsw = this.st_5_loginPsw.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return info;
	}

}
