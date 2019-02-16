package com.lib.sdk.struct;

public class SDK_TitleDot {
	// public static final int NET_MAX_TITLE_DOT_BUF_LEN = 64 * 24 * 24;
	public short st_0_width;
	public short st_1_height;
	public byte st_2_revese[];
	public byte st_3_pDotBuf[];

	public SDK_TitleDot(int width, int height) {
		st_2_revese = new byte[12];
		st_3_pDotBuf = new byte[width * height / 8];
	}
}
