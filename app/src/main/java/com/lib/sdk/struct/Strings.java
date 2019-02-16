package com.lib.sdk.struct;

public class Strings {
	public byte[][] st_str = new byte[6][512];

	public Strings() {
		for (int i = 0; i < 6; ++i) {
			st_str[i] = new byte[512];
		}
	}
}
