package com.example.funsdkdemo.entity;

import android.content.Context;

import com.example.funsdkdemo.R;
import com.lib.SDKCONST;

import java.io.Serializable;

public class TimeItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String ALL_DAY_TIME = "00:00-24:00";
	private String mTime = ALL_DAY_TIME;
	private int mWeekMask;
	private boolean mIsOpen;
	private static int[] mWeeks = new int[] {R.string.sunday,R.string.monday,R.string.tuesday,
			R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday};

	public boolean isOpen() {
		return mIsOpen;
	}

	public void setOpen(boolean isOpen) {
		this.mIsOpen = isOpen;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		this.mTime = time;
	}

	public void setWeekMask(int mask) {
		this.mWeekMask = mask;
	}

	public int getWeekMask() {
		return mWeekMask;
	}

	public String getWeeks(Context context) {
		int i = 0;
		int mask = mWeekMask;
		boolean isEveryDay = true;
		StringBuffer sb = new StringBuffer();
		do {
			if ((mask & 0x01) == 1) {
				sb.append(context.getString(mWeeks[i]));
				sb.append(" ");
			} else {
				isEveryDay = false;
			}
			i++;
		} while ((mask = mask >> 1) != 0);
		if (isEveryDay && sb.length() > 0 && i == SDKCONST.NET_N_WEEKS) {
			return context.getString(R.string.every_day);
		}
		return sb.toString();
	}

	public void setTimeSection(int position, String[][] weeks) {
		int i = 0;
		int mask = mWeekMask;
		String[] times = mTime.split("-");
		do {
			if ((mask & 0x01) == 1) {
				if (mIsOpen) {
					weeks[i][position] = "1 " + times[0] + ":00" + "-" + times[1] + ":00";
				} else {
					weeks[i][position] = "0 " + times[0] + ":00" + "-" + times[1] + ":00";
				}
			} else {
				weeks[i][position] = "0 " + times[0] + ":00" + "-" + times[1] + ":00";
			}
			i++;
			mask = mask >> 1;
		} while (i < 7);		

	}
}
