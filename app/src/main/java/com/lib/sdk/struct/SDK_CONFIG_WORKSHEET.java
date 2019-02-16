/**
 * Android_NetSdk
 * SDK_CONFIG_WORKSHEET.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;

import com.lib.SDKCONST;

public class SDK_CONFIG_WORKSHEET {
	public SDK_TIMESECTION st_0_tsSchedule[][]; /* !< ʱ��� */

	public SDK_CONFIG_WORKSHEET() {
		st_0_tsSchedule = new SDK_TIMESECTION[SDKCONST.NET_N_WEEKS][SDKCONST.NET_N_TSECT];
		for (int i = 0; i < SDKCONST.NET_N_WEEKS; ++i) {
			for (int j = 0; j < SDKCONST.NET_N_TSECT; ++j)
				st_0_tsSchedule[i][j] = new SDK_TIMESECTION();
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("SDK_CONFIG_WORKSHEET {st_0_tsSchedule=[");
		for (int i = 0; i < st_0_tsSchedule.length; i++) {
			sb.append(i).append(":[");
			for (SDK_TIMESECTION sdk_TIMESECTIONs : st_0_tsSchedule[i]) {
				sb.append("[").append(sdk_TIMESECTIONs.toString()).append("],");
			}
			sb.append("],");
		}
		sb.append("]}");
		return sb.toString();
	}

	public static class Builder {
		private SDK_TIMESECTION[] monday;
		private SDK_TIMESECTION[] tuesday;
		private SDK_TIMESECTION[] wednesday;
		private SDK_TIMESECTION[] thursday;
		private SDK_TIMESECTION[] friday;
		private SDK_TIMESECTION[] saturday;
		private SDK_TIMESECTION[] sunday;

		public Builder() {
		}

		public Builder monday(SDK_TIMESECTION[] monday) {
			this.monday = monday;
			return this;
		}

		public Builder tuesday(SDK_TIMESECTION[] tuesday) {
			this.tuesday = tuesday;
			return this;
		}

		public Builder wednesday(SDK_TIMESECTION[] wednesday) {
			this.wednesday = wednesday;
			return this;
		}

		public Builder thursday(SDK_TIMESECTION[] thursday) {
			this.thursday = thursday;
			return this;
		}

		public Builder friday(SDK_TIMESECTION[] friday) {
			this.friday = friday;
			return this;
		}

		public Builder saturday(SDK_TIMESECTION[] saturday) {
			this.saturday = saturday;
			return this;
		}

		public Builder sunday(SDK_TIMESECTION[] sunday) {
			this.sunday = sunday;
			return this;
		}

		public SDK_CONFIG_WORKSHEET build() {
			SDK_CONFIG_WORKSHEET sheet = new SDK_CONFIG_WORKSHEET();
			if (null != monday && monday.length > 0) {
				sheet.st_0_tsSchedule[0] = monday;
			}
			if (null != tuesday && tuesday.length > 0) {
				sheet.st_0_tsSchedule[1] = tuesday;
			}
			if (null != wednesday && wednesday.length > 0) {
				sheet.st_0_tsSchedule[2] = wednesday;
			}
			if (null != thursday && thursday.length > 0) {
				sheet.st_0_tsSchedule[3] = thursday;
			}
			if (null != friday && friday.length > 0) {
				sheet.st_0_tsSchedule[4] = friday;
			}
			if (null != saturday && saturday.length > 0) {
				sheet.st_0_tsSchedule[5] = saturday;
			}
			if (null != sunday && sunday.length > 0) {
				sheet.st_0_tsSchedule[6] = sunday;
			}
			return sheet;
		}
	}
}
