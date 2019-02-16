/**
 * FutureFamily
 * DSTPoint.java
 * Administrator
 * TODO
 * 2015-6-29
 */
package com.lib.sdk.struct;

/**
 * FutureFamily DSTPoint.java
 * 
 * @author huangwanshui TODO 2015-6-29
 */
public class DSTPoint {
	public int st_0_iYear;
	public int st_1_iMonth;
	public int st_2_iWeek; // /<周1:first to2 3 4 -1:last one 0:表示使用按日计算的方法[-1,4]
	public int st_3_iWeekDay; // /<weekday from sunday=0 [0, 6]
	public int st_4_Hour;
	public int st_5_Minute;
}
