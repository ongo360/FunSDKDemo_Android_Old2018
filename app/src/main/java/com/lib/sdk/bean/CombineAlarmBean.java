package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @ClassName: CombineAlarmBean
 * @Description: TODO(一种新的报警类型，无通道)
 * @author xxy
 * @date 2016年8月17日 上午10:21:09
 * Json 数据 { "Alarm.CombineAlarm" : { "AlarmGrade" : 0 }, "Name" : "Alarm.CombineAlarm", "Ret" : 100, 
 * "SessionID" : "0x0000006F" }
 */

public class CombineAlarmBean {

	@JSONField(name = "AlarmGrade")
	public int AlarmGrade;  //调节灵敏度参数
}
