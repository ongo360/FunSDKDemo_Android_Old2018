package com.example.funsdkdemo.manager;

import android.content.Context;
import android.os.Message;

import com.basic.G;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.sdk.bean.SysDevAbilityInfoBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hws on 2018-07-17.
 * 服务器端获取能力集
 */

public class SysAbilityManager implements IFunSDKResult {
    public static final int CLOUD_GET_STATE = 0;//获取状态中
    public static final int CLOUD_NOT_SUPPORT = 1;//不支持
    public static final int CLOUD_NOT_OPEND = 2;//未开通
    public static final int CLOUD_NORMAL = 3;//已经开通,正常使用
    public static final int CLOUD_EXPIRED = 4;//已经过期

    private int userId;
    private static SysAbilityManager instance;
    private Map<String,SysDevAbilityInfoBean> sysDevAbilityInfoBeanMap;
    private Map<String,OnSysAbilityResultLisener> lisenerMap;
    private SysAbilityManager() {
        sysDevAbilityInfoBeanMap = new HashMap<>();
        lisenerMap = new HashMap<>();
        userId = FunSDK.GetId(userId,this);
    }

    public static SysAbilityManager getInstance() {
        synchronized (SysAbilityManager.class) {
            if (instance == null) {
                instance = new SysAbilityManager();
            }else {
                instance.init();
            }
            return instance;
        }
    }

    public void init() {
        userId = FunSDK.GetId(userId,this);
    }

    public boolean isSupport(Context context,String devId, String key, boolean isRefreshDataFromService, OnSysAbilityResultLisener<Boolean> lisener) {
           if (sysDevAbilityInfoBeanMap != null) {
               synchronized (sysDevAbilityInfoBeanMap) {
                   if (!sysDevAbilityInfoBeanMap.containsKey(devId)) {
                       SysDevAbilityInfoBean bean = new SysDevAbilityInfoBean(devId);
                       sysDevAbilityInfoBeanMap.put(devId,bean);
                       lisenerMap.put(devId,lisener);
                       FunSDK.SysGetDevAbilitySetFromServer(userId,
                               bean.getSendJson(context,key),0);
                   }else {
                       SysDevAbilityInfoBean bean = sysDevAbilityInfoBeanMap.get(devId);
                       if (bean != null) {
                           boolean isSupport = bean.isConfigSupport(key);
                           if (lisener != null) {
                               lisener.onSupportResult(isSupport);
                           }
                           if (isRefreshDataFromService){
                               lisenerMap.put(devId,lisener);
                               FunSDK.SysGetDevAbilitySetFromServer(userId,
                                       bean.getSendJson(context,key),0);
                           }
                           return isSupport;
                       }
                   }
               }
           }
        return false;
    }

    public void isSupports(Context context,String devId,boolean isRefreshDataFromService,
                           OnSysAbilityResultLisener<Map<String,Object>> lisener,String... keys) {
        if (sysDevAbilityInfoBeanMap != null) {
            synchronized (sysDevAbilityInfoBeanMap) {
                if (!sysDevAbilityInfoBeanMap.containsKey(devId)) {
                    SysDevAbilityInfoBean bean = new SysDevAbilityInfoBean(devId);
                    sysDevAbilityInfoBeanMap.put(devId,bean);
                    lisenerMap.put(devId,lisener);
                    FunSDK.SysGetDevAbilitySetFromServer(userId,
                            bean.getSendJson(context,keys),0);
                }else {
                    SysDevAbilityInfoBean bean = sysDevAbilityInfoBeanMap.get(devId);
                    if (bean != null) {
                        if (lisener != null) {
                            lisener.onSupportResult(bean.isConfigSupports());
                        }
                    }
                    if (isRefreshDataFromService) {
                        lisenerMap.put(devId,lisener);
                        FunSDK.SysGetDevAbilitySetFromServer(userId,
                                bean.getSendJson(context,keys),0);
                    }
                }
            }
        }
    }

    public void resetData(String devId) {
        if (sysDevAbilityInfoBeanMap != null) {
            synchronized (sysDevAbilityInfoBeanMap) {
                sysDevAbilityInfoBeanMap.remove(devId);
            }
        }
    }

    public void resetAllData() {
        if (sysDevAbilityInfoBeanMap != null) {
            synchronized (sysDevAbilityInfoBeanMap) {
                sysDevAbilityInfoBeanMap.clear();
            }
        }
    }

    public void release() {
        if (sysDevAbilityInfoBeanMap != null) {
            sysDevAbilityInfoBeanMap.clear();
            sysDevAbilityInfoBeanMap = null;
        }
        instance = null;
        FunSDK.UnRegUser(userId);
        userId = -1;
    }

    @Override
    public int OnFunSDKResult(Message message, MsgContent msgContent) {
        switch (message.what) {
            case EUIMSG.SYS_GET_ABILITY_SET: {
                if (message.arg1 < 0) {
                    return 0;
                }
                String devId = G.ToString(msgContent.pData);
                if (sysDevAbilityInfoBeanMap != null && sysDevAbilityInfoBeanMap.containsKey(devId)) {
                    SysDevAbilityInfoBean bean = sysDevAbilityInfoBeanMap.get(devId);
                    if (bean != null) {
                        bean.parseJson(msgContent.str);
                        OnSysAbilityResultLisener lisener = lisenerMap.get(devId);
                        if (lisener != null) {
                            lisener.onSupportResult(bean.isConfigSupports());
                        }
                    }
                }
                break;
            }
        }
        return 0;
    }

    public interface OnSysAbilityResultLisener<T> {
        void onSupportResult(T isSupport);
    }
}
