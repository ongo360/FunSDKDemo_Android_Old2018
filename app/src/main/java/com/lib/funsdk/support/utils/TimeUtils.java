package com.lib.funsdk.support.utils;


public class TimeUtils {

	public static String formatTimes(int minute) {
		int HH = minute / 60;
		int DD = HH / 24;
		int MM = minute % 60;
		if (DD == 0 && HH == 0) {
			return "00H" + String.format("%02dM", MM);
		}
		if (DD == 0) {
			return String.format("%02dH%02dM", HH, MM);
		} else {
			HH -= DD * 24;
			return String.format("%02dD%02dH%02dM", DD, HH, MM);
		}
	}

	public static String formatTimes(int hour, int minute, int second) {
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	public static String formatTimes(String hour, String minute, String second) {
		return String.format("%02d:%02d:%02d", Integer.parseInt(hour),
				Integer.parseInt(minute), Integer.parseInt(second));
	}

	public static long getLongTimes(String hour, String minute, String second) {
		int nHour = (int) (MyUtils.isInteger(hour) ? Long.parseLong(hour) : 0);
		int nMinute = (int) (MyUtils.isInteger(minute) ? Long.parseLong(minute)
				: 0);
		int nSecond = (int) (MyUtils.isInteger(second) ? Long.parseLong(second)
				: 0);
		return getLongTimes(nHour, nMinute, nSecond);
	}

	public static long getLongTimes(int hour, int minute, int second) {
		long times = hour * 3600 + minute * 60 + second;
		return times;
	}
}
