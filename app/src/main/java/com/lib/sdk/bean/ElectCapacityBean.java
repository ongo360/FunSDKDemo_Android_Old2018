package com.lib.sdk.bean;

/**
 * Created by hws on 2017-10-25.
 */

public class ElectCapacityBean {
    public static final String CLASSNAME = "Dev.ElectCapacity";
    public static final int PULL_OUT_STORAGE = -1;//存储设备被拔出
    public static  final int NO_STORAGE = 0;//没有存储设备
    public static final int HAVE_STORAGE = 1;//有存储设备
    public static final int INSET_STORAGE = 2;//存储设备插入
    public static final int UNKNOWN_STORAGE = -2;//存储设备未知
    public int devStorageStatus = HAVE_STORAGE;//存储设备状态
    public int percent;
    public int electable;// // 显示是否在充电 0:未充电; 1:正在充电;2:充电满；3:未知

    public ElectCapacityBean() {
        percent = 80;
    }
}
