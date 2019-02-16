/**
 * FutureFamily
 * SDK_VideoWidgetConfigAll.java
 * Administrator
 * TODO
 * 2015-7-4
 */
package com.lib.sdk.struct;

/**
 * FutureFamily SDK_VideoWidgetConfigAll.java
 * 
 * @author huangwanshui TODO 2015-7-4
 */
public class SDK_VideoWidgetConfigAll {
	public SDK_CONFIG_VIDEOWIDGET st_0_vVideoWidegetConfigAll[] = new SDK_CONFIG_VIDEOWIDGET[32];

	public SDK_VideoWidgetConfigAll() {
		for (int i = 0; i < 32; ++i)
			st_0_vVideoWidegetConfigAll[i] = new SDK_CONFIG_VIDEOWIDGET();
	}
}
