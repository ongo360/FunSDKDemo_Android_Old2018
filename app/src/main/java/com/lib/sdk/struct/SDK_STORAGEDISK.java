/**
 * Android_NetSdk
 * SDK_STORAGEDISK.java
 * Administrator
 * TODO
 * 2014-12-24
 */
package com.lib.sdk.struct;

import java.util.Arrays;

import com.lib.SDKCONST;

/**
 * Android_NetSdk SDK_STORAGEDISK.java
 * 
 * @author huangwanshui TODO 2014-12-24
 */
public class SDK_STORAGEDISK {
	public int st_0_iPhysicalNo;
	public int st_1_iPartNumber; // ������
	public SDK_DriverInformation st_2_diPartitions[];

	public SDK_STORAGEDISK() {
		st_2_diPartitions = new SDK_DriverInformation[SDKCONST.SDK_MAX_DRIVER_PER_DISK];
		for (int i = 0; i < SDKCONST.SDK_MAX_DRIVER_PER_DISK; ++i)
			st_2_diPartitions[i] = new SDK_DriverInformation();
	}

	@Override
	public String toString() {
		return "SDK_STORAGEDISK [st_0_iPhysicalNo=" + st_0_iPhysicalNo + ", st_1_iPartNumber=" + st_1_iPartNumber
				+ ", st_2_diPartitions=" + Arrays.toString(st_2_diPartitions) + "]";
	}

}
