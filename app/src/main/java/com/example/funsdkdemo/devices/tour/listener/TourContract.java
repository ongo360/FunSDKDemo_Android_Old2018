package com.example.funsdkdemo.devices.tour.listener;

import android.os.Message;
import android.support.annotation.Nullable;

import com.example.funsdkdemo.devices.tour.model.bean.PTZTourBean;
import com.example.funsdkdemo.devices.tour.model.bean.TourState;
import com.lib.MsgContent;

/**
 * Created by ccy on 2017-09-28.
 * 巡航主界面V、P接口
 */

public class TourContract {

    // V
    public interface ITourView {

        void onLoadTours(@Nullable PTZTourBean tourBean);

        void onTourAdded(int presetId);

        void onTourDeleted(int presetId);

        void onTourReseted(int presetId);

        void onTourStarted();

        void onTourStoped();

        void onTour360Started();

        void onTour360Stoped();

        void onTourCleared();

        /**
         * V仍在前台（或不在前台但接受回调）,每次获取完数据后都应该判断
         * @return
         */
        boolean isActive();

        void showLoading(boolean cancelable, String info);

        void dismissLoading();

        void onFailed(Message msg, MsgContent ex, String extraStr);

        void onTmimgPtzTourResult(boolean isEnable,int timeInterval);

        void onSaveTimimgPtzTourResult(boolean isSuccess);

    }


    //P
    public interface ITourPresenter {
        /**
         * 获取巡航路线信息
         *
         * @return
         */
        void getTour();


        /**
         * 在指定巡航路线上添加一个巡航点
         *
         * @param presetId 巡航点的id, [1,255）。
         * @param tourId   巡航路线id，[0,255]。目前仅需一条，一般为0
         * @param presetIndex 巡航点添加的位置
         */
        void addTour(int presetId, int tourId, int presetIndex);

        /**
         * 重置巡航点
         *
         * @param presetId 巡航点的id, [1,255）。
         * @param tourId   巡航路线id，[0,255]。目前仅需一条，一般为0
         */
        void resetTour(int presetId, int tourId);

        /**
         * 将指定巡航路线上的某个巡航点删除
         *
         * @param presetId 巡航点的id, [1,255）。
         * @param tourId   巡航路线id，[0,255]。 目前仅需一条，一般为0
         */
        void deleteTour(int presetId, int tourId);

        /**
         * 开始巡航
         *
         * @param tourId 巡航线路id
         */
        void startTour(int tourId);

        void stopTour();

        void start360Tour();

        void stop360Tour();

        /**
         * 清除某条巡航路线上所有点
         *
         * @param tourId 巡航路线id
         */
        void clearTour(int tourId);

        /**
         * 转向预置点
         * @param presetId
         */
        void turnPreset(int presetId);


        /**
         * 注销回调
         */
        void removeAllCallback();

        void setTourState(TourState state);

        TourState getTourState();

        void getTimimgPtzTour();

        void setTimingPtzTour(boolean isEnable,int timeInterval);

    }
}
