package com.example.funsdkdemo.devices.tour.model;

import android.content.Context;
import android.os.Message;

import com.basic.G;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.devices.tour.listener.TourDataSource;
import com.example.funsdkdemo.devices.tour.model.bean.ConfigGetPreset;
import com.example.funsdkdemo.devices.tour.model.bean.OPTourControlBean;
import com.example.funsdkdemo.devices.tour.model.bean.PTZTourBean;
import com.lib.EFUN_ERROR;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.bean.OPPTZControlBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ccy on 2017-09-29.
 * M具体实现
 */

public class TourRepository implements TourDataSource, IFunSDKResult {

    public static final String TOUR_END_RESPONSE_STR = "TOUR_END_RESPONSE_STR";//巡航停止回调key name
    /**
     * key:cmd or jsonName 对应ex.str <br/>
     * value:TourCallback
     */
    private Map<String, TourCallback> listeners;
    private int userId;
    private HandleConfigData handleConfigData;
    private FunDevice funDevice;
    private Context context;
    public TourRepository(FunDevice funDevice,Context context) {
        this.funDevice = funDevice;
        this.context = context;
        listeners = new HashMap<>();
        userId = FunSDK.RegUser(this);
        handleConfigData = new HandleConfigData();
    }

    @Override
    public void getTour(final TourCallback callback) {

        listeners.put(PTZTourBean.JSON_NAME, callback);

        /**
         * 测试发现：DevGetConfigByJson其实就是以1042作为jsonId发送DevCmdGeneral
         * 下面这两个接口的区别是前者能指定通道号（jsonName = Uart.PTZTour.[0])
         * 后者则返回全部通道(jsonName = Uart.PTZTour)对应数据
         * 此处因设备端逻辑，如果数据为空（即没有巡航路线），前者报错（-11406），后者不报错且json实体为null（除去Name/SessionID/Ret)
         * 故如果使用前者，注意处理报错情况，使用后者注意json实体多一层数组
         */

        FunSDK.DevGetConfigByJson(userId
                , funDevice.getDevSn()
                , PTZTourBean.JSON_NAME
                , 2048
                , 0
                , 5000
                , 0);

//        FunSDK.DevCmdGeneral(userId
//                ,DataCenter.Instance().strOptDevID
//                ,1042
//                ,PTZTourBean.JSON_NAME
//                ,-1
//                ,5000
//                ,null
//                ,-1
//                ,0);

    }

    @Override
    public void getPreset(TourCallback callback) {

        listeners.put(ConfigGetPreset.JSON_NAME, callback);

        FunSDK.DevGetConfigByJson(userId
                , funDevice.getDevSn()
                , ConfigGetPreset.JSON_NAME
                , 2048
                , 0
                , 5000
                , 0);
    }

    @Override
    public void controlTour(String cmd, int presetId, int tourId, TourCallback callback) {

        listeners.put(cmd, callback);

        OPTourControlBean bean = new OPTourControlBean();
        bean.Command = cmd;
        bean.Parameter.Preset = presetId;
        bean.Parameter.Tour = tourId;
        bean.Parameter.Step = 3;  //默认3秒
        bean.Parameter.TourTimes = 1; //默认1次

        FunSDK.DevCmdGeneral(userId
                , funDevice.getDevSn()
                , OPTourControlBean.OPPTZCONTROL_ID
                , cmd
                , -1
                , 5000
                , HandleConfigData.getSendData(OPTourControlBean.OPPTZCONTROL_JSONNAME, "0x08", bean).getBytes()
                , -1
                , 0);
    }

    @Override
    public void controlTour(String cmd, int presetId, int tourId, int presetIndex, TourCallback callback) {
        listeners.put(cmd, callback);

        OPTourControlBean bean = new OPTourControlBean();
        bean.Command = cmd;
        bean.Parameter.Preset = presetId;
        bean.Parameter.Tour = tourId;
        bean.Parameter.PresetIndex = presetIndex;
        bean.Parameter.Step = 3;  //默认3秒
        bean.Parameter.TourTimes = 1; //默认1次

        FunSDK.DevCmdGeneral(userId
                , funDevice.getDevSn()
                , OPTourControlBean.OPPTZCONTROL_ID
                , cmd
                , -1
                , 5000
                , HandleConfigData.getSendData(OPTourControlBean.OPPTZCONTROL_JSONNAME, "0x08", bean).getBytes()
                , -1
                , 0);
    }

    @Override
    public void controlPreset(String cmd, int chn, int presetId, TourCallback callback) {

        listeners.put(cmd, callback);

        //注意导包
        OPPTZControlBean bean = new OPPTZControlBean();
        bean.Command = cmd;
        bean.Parameter.Channel = chn;
        if(cmd == com.lib.sdk.bean.OPPTZControlBean.SET_PRESET){
            bean.Parameter.PresetName = context.getString(R.string.preset) + presetId;
        }
        bean.Parameter.Preset = presetId;

        FunSDK.DevCmdGeneral(userId
                , funDevice.getDevSn()
                , com.lib.sdk.bean.OPPTZControlBean.OPPTZCONTROL_ID
                , cmd
                , -1
                , 5000
                , HandleConfigData.getSendData(com.lib.sdk.bean.OPPTZControlBean.OPPTZCONTROL_JSONNAME, "0x08", bean).getBytes()
                , -1
                , 0);

    }

    @Override
    public void registerTourEnd(TourCallback rspCallback) {
        listeners.put(TOUR_END_RESPONSE_STR,rspCallback);
    }


    @Override
    public void removeAllCallback() {
        listeners.clear();
    }

    @Override
    public int OnFunSDKResult(Message msg, MsgContent ex) {

        switch (msg.what) {
            case EUIMSG.DEV_GET_JSON:
                switch (ex.str) {
                    case PTZTourBean.JSON_NAME:  //处理“获取巡航线”
                        if (msg.arg1 < 0) {
                            if (msg.arg1 == EFUN_ERROR.EE_DVR_OPT_CONFIG_PARSE_ERROR) {  //第一次获取时因不存在任何巡航线会报错
                                if (listeners.get(PTZTourBean.JSON_NAME) != null) {
                                    listeners.get(PTZTourBean.JSON_NAME).onSuccess(null);
                                }
                            } else {
                                handleCommonError(msg, ex);
                            }
                            return 0;
                        }

                        try {
                            if (handleConfigData.getDataObj(G.ToString(ex.pData), PTZTourBean.class)) {
                                List<PTZTourBean> tours = (List<PTZTourBean>) handleConfigData.getObj();
                                if (listeners.get(PTZTourBean.JSON_NAME) != null) {
                                    listeners.get(PTZTourBean.JSON_NAME).onSuccess(tours);
                                }
                            }else{
                                if (listeners.get(PTZTourBean.JSON_NAME) != null) {
                                    listeners.get(PTZTourBean.JSON_NAME).onSuccess(null);
                                }
                            }
                        } catch (Exception e) {
                            //可能是ex.pData 为空，也可能强转出错
                            if (listeners.get(PTZTourBean.JSON_NAME) != null) {
                                listeners.get(PTZTourBean.JSON_NAME).onError(null, null, context.getString(R.string.request_data_error));
                            }
                        }

                        break;
                    case ConfigGetPreset.JSON_NAME:  //处理“获取预置点”
                        if(msg.arg1 < 0){
                            handleCommonError(msg,ex);
                            return 0;
                        }
                        try {
                            if (handleConfigData.getDataObj(G.ToString(ex.pData), ConfigGetPreset.class)) {
                                List<ConfigGetPreset> presets = (List<ConfigGetPreset>) handleConfigData.getObj();
                                if (listeners.get(ConfigGetPreset.JSON_NAME) != null) {
                                    listeners.get(ConfigGetPreset.JSON_NAME).onSuccess(presets);
                                }
                            }
                        } catch (Exception e) {
                            //可能是ex.pData 为空，也可能强转出错
                            if (listeners.get(PTZTourBean.JSON_NAME) != null) {
                                listeners.get(PTZTourBean.JSON_NAME).onError(null, null, context.getString(R.string.request_data_error));
                            }
                        }
                        break;
                }
                break;
            case EUIMSG.DEV_CMD_EN: //处理“操作巡航线”、“操作预置点”
                if (msg.arg1 < 0) {
                    handleCommonError(msg, ex);
                    return 0;
                }
                switch (ex.str) {
                    case OPTourControlBean.ADD_TOUR:
                    case OPTourControlBean.DELETE_TOUR:
                    case OPTourControlBean.START_TOUR:
                    case OPTourControlBean.STOP_TOUR:
                    case OPTourControlBean.CLEAR_TOUR:
                    case com.lib.sdk.bean.OPPTZControlBean.SET_PRESET:
                    case com.lib.sdk.bean.OPPTZControlBean.TURN_PRESET:
                    case com.lib.sdk.bean.OPPTZControlBean.REMOVE_PRESET:
                    //以上回调目前均无需额外操作，统一处理
                    handleCommonSuccess(msg, ex);
                        break;
                }
                break;
            case EUIMSG.DEV_PTZ_CONTROL:
                if(msg.arg1 == OPTourControlBean.PTZ_TOUR_END_RSP_ID){
                    if(listeners.get(TOUR_END_RESPONSE_STR) != null){
                        listeners.get(TOUR_END_RESPONSE_STR).onSuccess(null);
                    }
                }
                break;
        }
        return 0;
    }


    private void handleCommonSuccess(Message msg, MsgContent ex) {
        if (listeners.get(ex.str) != null) {  //不要用containsKey判断，因为value值允许为null
            listeners.get(ex.str).onSuccess(null);
        }
    }

    private void handleCommonError(Message msg, MsgContent ex) {
        if (listeners.get(ex.str) != null) { //不要用containsKey判断，因为value值允许为null
            listeners.get(ex.str).onError(msg, ex, "");
        }
    }
}
