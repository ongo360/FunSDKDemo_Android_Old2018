/**
 * Android_NetSdk
 * SDK_EncodeConfigAll_SIMPLIIFY.java
 * Administrator
 * TODO
 * 2014-12-30
 */
package com.lib.sdk.struct;
import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_EncodeConfigAll_SIMPLIIFY.java
 * @author huangwanshui
 * TODO
 * 2014-12-30
 */
public class SDK_EncodeConfigAll_SIMPLIIFY {
	public SDK_CONFIG_ENCODE_SIMPLIIFY st_0_vEncodeConfigAll[] = new SDK_CONFIG_ENCODE_SIMPLIIFY[SDKCONST.NET_MAX_CHANNUM];
	public SDK_EncodeConfigAll_SIMPLIIFY() {
		for(int i = 0 ; i < SDKCONST.NET_MAX_CHANNUM; ++i)
			st_0_vEncodeConfigAll[i] = new SDK_CONFIG_ENCODE_SIMPLIIFY();
	}
}
