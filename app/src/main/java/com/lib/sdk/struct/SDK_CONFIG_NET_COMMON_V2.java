package com.lib.sdk.struct;

import java.io.Serializable;

import com.basic.G;
import com.lib.SDKCONST;

public class SDK_CONFIG_NET_COMMON_V2 implements Serializable {
	// !主机名
	public byte[] st_00_HostName = new byte[SDKCONST.NET_NAME_PASSWORD_LEN];
	// !主机IP
	public CONFIG_IPAddress st_01_HostIP = new CONFIG_IPAddress();
	// !子网掩码
	public CONFIG_IPAddress st_02_Submask = new CONFIG_IPAddress();
	// !网关IP
	public CONFIG_IPAddress st_03_Gateway = new CONFIG_IPAddress();
	// !HTTP服务端口
	public int st_04_HttpPort;
	// !TCP侦听端口
	public int st_05_TCPPort;
	// !SSL侦听端口
	public int st_06_SSLPort;
	// !UDP侦听端口
	public int st_07_UDPPort;
	// !最大连接数
	public int st_08_MaxConn;
	// !监视协议 {"TCP","UDP","MCAST",…}
	public int st_09_MonMode;
	// !限定码流值
	public int st_10_MaxBps;
	// !传输策略
	// char TransferPlan[NET_NAME_PASSWORD_LEN];
	public int st_11_TransferPlan;

	// !是否启用高速录像下载测率
	public byte st_12_bUseHSDownLoad;
	// !MAC地址
	public byte[] st_13_sMac = new byte[SDKCONST.NET_MAX_MAC_LEN];

	public byte[] st_14_sSn = new byte[SDKCONST.NET_MAX_MAC_LEN]; // /序列号

	public int st_15_DeviceType; // 设备类型

	public byte[] st_151_arg0 = new byte[3];
	public byte[] st_99_Resume = new byte[64]; // /保留

	@Override
	public boolean equals(Object o) {
		if (o instanceof SDK_CONFIG_NET_COMMON_V2) {
			return G.ToString(this.st_14_sSn).equals(
					G.ToString(((SDK_CONFIG_NET_COMMON_V2) o).st_14_sSn));
		}
		return false;
	}

	@Override
	public String toString() {
		return "SDK_CONFIG_NET_COMMON_V2 [st_00_HostName="
				+ G.ToString(st_00_HostName) + ", st_01_HostIP=" + st_01_HostIP
				+ ", st_02_Submask=" + st_02_Submask + ", st_03_Gateway="
				+ st_03_Gateway + ", st_04_HttpPort=" + st_04_HttpPort
				+ ", st_05_TCPPort=" + st_05_TCPPort + ", st_06_SSLPort="
				+ st_06_SSLPort + ", st_07_UDPPort=" + st_07_UDPPort
				+ ", st_08_MaxConn=" + st_08_MaxConn + ", st_09_MonMode="
				+ st_09_MonMode + ", st_10_MaxBps=" + st_10_MaxBps
				+ ", st_11_TransferPlan=" + st_11_TransferPlan
				+ ", st_12_bUseHSDownLoad=" + st_12_bUseHSDownLoad
				+ ", st_13_sMac=" + G.ToString(st_13_sMac) + ", st_14_sSn="
				+ G.ToString(st_14_sSn) + ", st_15_Resume="
				+ G.ToString(st_99_Resume) + "]";
	}

}
