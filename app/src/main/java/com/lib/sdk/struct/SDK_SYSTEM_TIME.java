package com.lib.sdk.struct;

import java.io.Serializable;
import java.util.Date;

public class SDK_SYSTEM_TIME implements Serializable {
	public int st_0_year;// /< �ꡣ
	public int st_1_month;// /< �£�January = 1, February = 2, and so on.
	public int st_2_day;// /< �ա�
	public int st_3_wday;// /< ���ڣ�Sunday = 0, Monday = 1, and so on
	public int st_4_hour;// /< ʱ��
	public int st_5_minute;// /< �֡�
	public int st_6_second;// /< �롣
	public int st_7_isdst;// /< ����ʱ��ʶ��

	@Override
	public String toString() {
		return "SDK_SYSTEM_TIME [st_0_year=" + st_0_year + ", st_1_month="
				+ st_1_month + ", st_2_day=" + st_2_day + ", st_3_wday="
				+ st_3_wday + ", st_4_hour=" + st_4_hour + ", st_5_minute="
				+ st_5_minute + ", st_6_second=" + st_6_second
				+ ", st_7_isdst=" + st_7_isdst + "]";
	}

	/**
	 * @param type
	 *            0: yyyyMMddhhmmss 1: yyyy-MM-dd hh:mm:ss
	 * @Title: getTime
	 * @Description: TODO(������һ�仰�����������������)
	 */
	public String getTime(int type) {
		if (type == 0) {
			return String.format("%04d%02d%02d%02d%02d%02d", st_0_year,
					st_1_month, st_2_day, st_4_hour, st_5_minute, st_6_second);
		} else if (type == 1) {
			return String.format("%04d-%02d-%02d %02d:%02d:%02d", st_0_year,
					st_1_month, st_2_day, st_4_hour, st_5_minute, st_6_second);
		} else {
			return "";
		}
	}
	public String getTime() {
		return getTime(1);
	}

	public String getStrDate() {
		return String.format("%04d-%02d-%02d", st_0_year, st_1_month, st_2_day);
	}

	public String getTime(int hour, int minute, int second) {
		return String.format("%04d-%02d-%02d %02d:%02d:%02d", st_0_year,
				st_1_month, st_2_day, hour, minute, second);
	}

	@SuppressWarnings("deprecation")
	public Date getDate() {
		return new Date(st_0_year - 1900, st_1_month - 1, st_2_day, st_4_hour,
				st_5_minute, st_6_second);
	}

	public String getStrTime() {
		return String.format("%02d.%02d.%02d", st_4_hour, st_5_minute,
				st_6_second);
	}

	public void setDate(Date date) {
		this.st_0_year = date.getYear() + 1900;
		this.st_1_month = date.getMonth() + 1;
		this.st_2_day = date.getDate();
		this.st_4_hour = date.getHours();
		this.st_5_minute = date.getMinutes();
		this.st_6_second = date.getSeconds();
	}
}
