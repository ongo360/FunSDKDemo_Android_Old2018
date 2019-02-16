/**
 * Android_NetSdk
 * SDK_TIMESECTION.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;


/**
 * Android_NetSdk SDK_TIMESECTION.java
 * 
 * @author huangwanshui TODO 2014-12-10
 */
public class SDK_TIMESECTION {
	// !ʹ��
	public int st_0_enable;
	// !��ʼʱ��:Сʱ
	public int st_1_startHour;
	// !��ʼʱ��:����
	public int st_2_startMinute;
	// !��ʼʱ��:����
	public int st_3_startSecond;
	// !����ʱ��:Сʱ
	public int st_4_endHour;
	// !����ʱ��:����
	public int st_5_endMinute;
	// !����ʱ��:����
	public int st_6_endSecond;

	public SDK_TIMESECTION() {
		this.st_0_enable = 1;
		this.st_1_startHour = 0;
		this.st_2_startMinute = 0;
		this.st_3_startSecond = 0;
		this.st_4_endHour = 23;
		this.st_5_endMinute = 59;
		this.st_6_endSecond = 59;
	}

	public Object clone() {  
		SDK_TIMESECTION o = null;  
        try {  
            o = (SDK_TIMESECTION) super.clone();  
        } catch (CloneNotSupportedException ex) {  
            ex.printStackTrace();  
        }  
  
        return o;  
    } 
	
	@Override
	public String toString() {
		return "SDK_TIMESECTION [st_0_enable=" + st_0_enable + ", st_1_startHour=" + st_1_startHour
				+ ", st_2_startMinute=" + st_2_startMinute + ", st_3_startSecond=" + st_3_startSecond
				+ ", st_4_endHour=" + st_4_endHour + ", st_5_endMinute=" + st_5_endMinute
				+ ", st_6_endSecond=" + st_6_endSecond + "]";
	}

}
