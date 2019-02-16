package com.lib.sdk.struct;

import com.lib.SDKCONST;

/**
 * 
 * @ClassName: SDK_CameraClearFogAll
 * @Description: TODO(去雾配置)
 * @author hws
 * @date 2015年12月1日 下午2:41:55
 * 
 */
public class SDK_CameraClearFogAll {
	public SDK_CameraClearFog st_0_vCameraClearFogAll[] = new SDK_CameraClearFog[SDKCONST.NET_MAX_CHANNUM];

	public SDK_CameraClearFogAll() {
		for (int i = 0; i < SDKCONST.NET_MAX_CHANNUM; ++i) {
			st_0_vCameraClearFogAll[i] = new SDK_CameraClearFog();
		}
	}
}
