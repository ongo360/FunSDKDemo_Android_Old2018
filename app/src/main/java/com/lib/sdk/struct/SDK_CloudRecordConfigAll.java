package com.lib.sdk.struct;

import com.lib.SDKCONST;

public class SDK_CloudRecordConfigAll {
	public SDK_CloudRecordConfig st_0_vRecordConfigAll[] = new SDK_CloudRecordConfig[SDKCONST.NET_MAX_CHANNUM];

	public SDK_CloudRecordConfigAll() {
		for (int i = 0; i < SDKCONST.NET_MAX_CHANNUM; ++i) {
			st_0_vRecordConfigAll[i] = new SDK_CloudRecordConfig();
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("SDK_CloudRecordConfigAll [st_0_vRecordConfigAll=");
		for (SDK_CloudRecordConfig sdk_CloudRecordConfig : st_0_vRecordConfigAll) {
			sb.append("[").append(sdk_CloudRecordConfig.toString()).append("],");
		}
		return sb + "]";
	}

}
