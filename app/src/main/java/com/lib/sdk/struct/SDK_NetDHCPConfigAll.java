/**
 * Android_NetSdk
 * SDK_NetDHCPConfigAll.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;

/**
 * Android_NetSdk
 * SDK_NetDHCPConfigAll.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_NetDHCPConfigAll {
	public SDK_NetDHCPConfig st_0_vNetDHCPConfig[];
	public SDK_NetDHCPConfigAll() {
		st_0_vNetDHCPConfig = new SDK_NetDHCPConfig[4];
		for(int i = 0 ; i < 4; i++)
			st_0_vNetDHCPConfig[i] = new SDK_NetDHCPConfig();
	}
}
