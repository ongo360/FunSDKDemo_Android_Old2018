package com.lib.sdk.struct;

import com.lib.SDKCONST;

public class SDK_ChannelNameConfigAll {
	public byte[][] st_channelTitle = new byte[SDKCONST.NET_MAX_CHANNUM][SDKCONST.NET_NAME_PASSWORD_LEN];

	public int nChnCount = 0;
	public boolean isComOpen=false;
	public SDK_ChannelNameConfigAll() {
		for (int i = 0; i < SDKCONST.NET_MAX_CHANNUM; ++i) {
			st_channelTitle[i] = new byte[SDKCONST.NET_NAME_PASSWORD_LEN];
			for(int j = 0 ; j < SDKCONST.NET_NAME_PASSWORD_LEN;++j)
				st_channelTitle[i][j] = 0;
		}
	}

	public int getCanUsedChannelSize() {
		return this.nChnCount;
	}

}
