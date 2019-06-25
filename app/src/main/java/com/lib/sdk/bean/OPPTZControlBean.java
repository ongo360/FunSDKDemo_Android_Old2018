package com.lib.sdk.bean;

/**预置点功能<br/>
 * 为了简洁将巡航线功能额外新建了一个:{@link com.xworld.devset.tour.model.bean.OPPTZControlBean}
 */

public class OPPTZControlBean {
    public static final int OPPTZCONTROL_ID = 1400;
    public static final String OPPTZCONTROL_JSONNAME = "OPPTZControl";
    public static final String SET_PRESET = "SetPreset";// 设置预置点
    public static final String REMOVE_PRESET = "ClearPreset";//删除预置点
    public static final String TURN_PRESET = "GotoPreset"; //跳转到预置点
    public static final String EDIT_NAME = "SetPresetName"; //修改预置点名字
    public String Command;//预置点操作类别
    public Parameter Parameter = new Parameter();
    public class Parameter {
        public int Channel;//要控制哪个通道
        public int Preset;//预置点编号 1 ~ 255  （200开始的预置点可能会作为特殊用途，建议不要使用）
        public String PresetName; //预置点名称
    }
}
