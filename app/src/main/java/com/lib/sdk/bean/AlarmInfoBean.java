package com.lib.sdk.bean;

/**
 * 
 * @ClassName: AlarmInfoBean
 * @Description: TODO(报警配置)
 * @author xxy
 * @date 2016年3月19日 下午4:38:42
 * 
 */

public class AlarmInfoBean {

	public AlarmInfoBean() {

	}

	public int Level;
	public boolean Enable;
	public EventHandler EventHandler = new EventHandler();

	public String[] Region;
}
