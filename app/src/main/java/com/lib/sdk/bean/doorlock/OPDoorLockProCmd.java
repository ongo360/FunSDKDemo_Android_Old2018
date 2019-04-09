package com.lib.sdk.bean.doorlock;

import com.lib.sdk.bean.AlarmInfoBean;
import com.lib.sdk.bean.JsonConfig;

import java.util.List;

/**
 * Created by ccy on 2018-03-03.
 * 门铃配置接口请求主json
 * 因门锁视为433的一种，接口设计同普通433的接口：{@link OPConsumerProCmdBean},
 */

public class OPDoorLockProCmd {
    public static String JSON_NAME = JsonConfig.DOOR_LOCK_CMD;
    public static int JSON_ID = 2046;

    public String Cmd;
    public String Arg1;
    public String Arg2;

    //修改门锁权限用户昵称
    public List<DoorLockAuthManageBean> DoorLockAuthManage;

    //设置临时密码时用到
    public List<DoorLockBean.TempPasswdBean> TempPasswd;

    //报警配置时用到（仅含Enable、EventHandler
    public AlarmInfoBean ConsSensorAlarm;

    //消息统计
    public MessageStatisticsBean MessageStatistics;

    //消息推送权限信息
    public MessagePushAuthBean MessagePushAuth;
}
