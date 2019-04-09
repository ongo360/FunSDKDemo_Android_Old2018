package com.lib.sdk.bean.doorlock;

import com.lib.sdk.bean.EventHandler;

import java.util.List;

/**
 * Created by ccy on 2018-03-01.
 * 门锁配置
 * {@link com.lib.sdk.bean.JsonConfig#OPERATION_CMD_GET}
 */

public class DoorLockBean {
    public String DoorLockName;
    public String DoorLockID;
    public int LockStatus; //开关状态
    public int DevType; //门锁类型 = 7602176
    public int UnLock;//开锁关锁状态（暂未实现
    public List<TempPasswdBean> TempPasswd; //临时密码，理论仅一个
    public MessageStatisticsBean MessageStatistics;//消息统计数据
    /**
     * {@link com.lib.sdk.bean.AlarmInfoBean}
     */
    public boolean Enable;
    public EventHandler EventHandler;

    //临时密码
    public static class TempPasswdBean{
        public String Passwd;
        public String StartTime;//格式= "1970-01-01 00:00:00"
        public String EndTime;
        public int VaildNum;//有效次数
    }

}
