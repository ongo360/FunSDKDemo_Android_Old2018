/**
 * Android_NetSdk
 * SDK_NetDDNSConfigALL.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;

import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_NetDDNSConfigALL.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_NetDDNSConfigALL {
	public SDK_NetDDNSConfig st_0_ddnsConfig[] = new SDK_NetDDNSConfig[SDKCONST.NET_MAX_DDNS_TYPE];
	public SDK_NetDDNSConfigALL() {
		for(int i = 0; i < SDKCONST.NET_MAX_DDNS_TYPE; ++i)
			st_0_ddnsConfig[i] = new SDK_NetDDNSConfig();
	}
}
