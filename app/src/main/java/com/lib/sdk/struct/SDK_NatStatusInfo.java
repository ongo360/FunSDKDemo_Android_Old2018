package com.lib.sdk.struct;

public class SDK_NatStatusInfo {
	public int st_0_iNatStatus;
	public byte[] st_1_NatInfoCode = new byte[256];// 增加内容，加大

	public interface SDK_NatStatusInfo_Status {
		// 未开启
		int SDK_NAT_STATUS_DISENABLE = 0;

		// 正在探测DNS
		int SDK_NAT_STATUS_PROBING = 1;

		// 正在连接服务器
		int SDK_NAT_STATUS_CONNECTING = 2;

		// 连接成功
		int SDK_NAT_STATUS_CONNECTED = 3;
	}

}
