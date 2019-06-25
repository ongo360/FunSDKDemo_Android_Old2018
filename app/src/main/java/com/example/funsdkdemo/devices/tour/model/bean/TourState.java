package com.example.funsdkdemo.devices.tour.model.bean;

/**
 * Created by ccy on 2017-10-11.
 * 巡航状态枚举，暂时没使用，因为360巡航还未提供“停止巡航”命令
 * TODO:等提供后，让P层持有之，取消V层的isTouring和is360Touring，之后实现自动切换巡航状态功能，而不是提示用户“请先停止巡航”
 * @see com.xworld.devset.tour.view.TourFragment#isTouring
 * @see com.xworld.devset.tour.view.TourFragment#is360Touring
 */

public enum TourState {
    /**
     * 巡航中
     */
    TOURING,

    /**
     * 360巡航中
     */
    TOURING360,

    /**
     * 空闲
     */
    IDLE;
}
