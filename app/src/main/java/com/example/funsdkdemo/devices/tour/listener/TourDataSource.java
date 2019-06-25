package com.example.funsdkdemo.devices.tour.listener;

import android.os.Message;
import android.support.annotation.Nullable;

import com.lib.MsgContent;

/**
 * Created by ccy on 2017-09-28.
 * 巡航点M接口
 */

public interface TourDataSource {

    interface TourCallback<T> {
        /**
         * 成功回调
         *
         * @param t 一般为json bean，亦可null
         */
        void onSuccess(@Nullable T t);

        /**
         * 失败回调
         *
         * @param msg      {@link com.lib.IFunSDKResult#OnFunSDKResult(Message, MsgContent)}
         * @param ex       {@link com.lib.IFunSDKResult#OnFunSDKResult(Message, MsgContent)}
         * @param extraStr 额外补充
         */
        void onError(Message msg, MsgContent ex, String extraStr);
    }

    /**
     * 获取巡航路线信息
     *
     * @param callback
     * @see com.xworld.devset.tour.model.bean.PTZTourBean
     */
    void getTour(TourCallback callback);

    /**
     * 获取已添加的预置点
     *
     * @param callback
     * @see com.xworld.devset.preset.model.bean.ConfigGetPreset
     */
    void getPreset(TourCallback callback);


    /**
     * 操作巡航路线
     *
     * @param cmd      操作命令，见{@link com.xworld.devset.tour.model.bean.OPPTZControlBean}
     * @param presetId 巡航点id，如果需要传该值的话
     * @param tourId   巡航路线id，如果需要传该值的话
     * @param callback 回调
     */
    void controlTour(String cmd, int presetId, int tourId, TourCallback callback);
    void controlTour(String cmd, int presetId, int tourId, int presetIndex, TourCallback callback);

    /**
     * 操作预置点
     *
     * @param cmd      操作命令，见{@link com.lib.sdk.bean.OPPTZControlBean}
     * @param chn      通道号，取0
     * @param presetId 预置点ID
     * @param callback 回调
     */
    void controlPreset(String cmd, int chn, int presetId, TourCallback callback);


    /**
     * 接受巡航结束的回调
     * @param rspCallback  接收回调
     */
    void registerTourEnd(TourCallback rspCallback);

    void removeAllCallback();


}
