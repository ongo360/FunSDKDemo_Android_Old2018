/**
 * Android_NetSdk
 * SDK_StorageDeviceInformationAll.java
 * Administrator
 * TODO
 * 2014-12-24
 */
package com.lib.sdk.struct;

import java.util.Arrays;

import com.lib.SDKCONST;

public class SDK_StorageDeviceInformationAll {
	public int st_0_iDiskNumber;
	public SDK_STORAGEDISK st_1_vStorageDeviceInfoAll[];

	public SDK_StorageDeviceInformationAll() {
		st_1_vStorageDeviceInfoAll = new SDK_STORAGEDISK[SDKCONST.SDK_MAX_DISK_PER_MACHINE];
		for (int i = 0; i < SDKCONST.SDK_MAX_DISK_PER_MACHINE; ++i)
			st_1_vStorageDeviceInfoAll[i] = new SDK_STORAGEDISK();
	}

	@Override
	public String toString() {
		return "SDK_StorageDeviceInformationAll [st_0_iDiskNumber=" + st_0_iDiskNumber + ", st_1_vStorageDeviceInfoAll="
				+ Arrays.toString(st_1_vStorageDeviceInfoAll) + "]";
	}

}
