package com.example.funsdkdemo.devices.tour.model.bean;

/**
 * Created by ccy on 2017-09-28.<br/>
 * <p>
 * 巡航操作命令Json<br/>
 * 跟预置点的命令json一样：{@link com.lib.sdk.bean.OPPTZControlBean}（巡航点预置点一个概念）<br/>
 * 为了简洁，新建一个，但注意勿混用
 *
 * @see com.lib.sdk.bean.OPPTZControlBean
 */

public class OPTourControlBean {
    public static final int PTZ_TOUR_END_RSP_ID = 2141; //巡航停止返回ID

    public static final int OPPTZCONTROL_ID = 1400;
    public static final String OPPTZCONTROL_JSONNAME = "OPPTZControl";
    public static final String ADD_TOUR = "AddTour"; //增加巡航点
    public static final String DELETE_TOUR = "DeleteTour";//删除巡航点
    public static final String START_TOUR = "StartTour";//开始巡航
    public static final String STOP_TOUR = "StopTour";//结束巡航
    public static final String CLEAR_TOUR = "ClearTour";//清除巡航线上的所有点

    public String Command;//巡航点操作指令
    public Parameter Parameter = new Parameter();

    public class Parameter {
        public int Preset;  //单个巡航点（预置点）id [1,255）
        public int Tour;   //巡航点路线id  [0,255]
        public int Step;   //巡航点时长（秒）
        public int TourTimes; //巡航次数
        public int PresetIndex; //添加的位置
    }
}
/**
 * 巡航线接口文档：
 */
/*
云台控制 消息ID 1400
命令字
AddTour  增加巡航点
DeleteTour 删除巡航点
StartTour 开始巡航
StopTour 结束巡航
ClearTour 清除巡航线上的所有点
{
    "OPPTZControl": {
        "Command": "AddTour",
        "Parameter": {
            "Preset": 1,          //1~255
            "Tour": 1,           //0~255
			"Step": 1 		//step >= 3 两个预置点间移动的时长（秒）
			"PresetIndex":-1  //插入到队列中的位置
        }
    }
}

{
    "OPPTZControl": {
        "Command": "DeleteTour",
        "Parameter": {
            "Preset": 1,
            "Tour": 1
        }
    }
}
{
    "OPPTZControl": {
        "Command": "StartTour ",
"Parameter": {
            "Tour": 1
            "TourTimes":1
        }
a    }
}
{
    "OPPTZControl": {
        "Command": "StopTour"
    }
}
{
    "OPPTZControl": {
        "Command": "ClearTour",
        "Parameter": {
            "Tour": 1
        }
    }

}

 */
