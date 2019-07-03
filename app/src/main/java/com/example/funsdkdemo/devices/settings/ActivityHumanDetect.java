package com.example.funsdkdemo.devices.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.basic.G;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.bean.HumanDetectionBean;
import com.lib.sdk.bean.JsonConfig;
import com.lib.sdk.bean.StringUtils;
import com.xm.ui.widget.ListSelectItem;
import com.xm.ui.widget.XTitleBar;

/**
 * @author Administrator
 * @name FunSDKDemo_Android_Old2018
 * @class name：com.example.funsdkdemo.devices.settings
 * @class describe
 * @time 2019-07-03 17:03
 * @change
 * @chang time
 * @class describe
 */
public class ActivityHumanDetect extends ActivityDemo implements IFunSDKResult{
    private ListSelectItem lsiHumanDetect;
    private FunDevice funDevice;
    private int userId;
    private HumanDetectionBean humanDetectionBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_detect);
        initView();
        initData();
    }

    private void initView() {
        ((XTitleBar)findViewById(R.id.human_detect_title)).setLeftClick(new XTitleBar.OnLeftClickListener() {
            @Override
            public void onLeftclick() {
                finish();
            }
        });

        ((XTitleBar)findViewById(R.id.human_detect_title)).setRightIvClick(new XTitleBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                if (humanDetectionBean == null) {
                    return;
                }
                FunSDK.DevSetConfigByJson(userId,funDevice.getDevSn(),JsonConfig.DETECT_HUMAN_DETECTION,
                        HandleConfigData.getSendData(HandleConfigData.getFullName(JsonConfig.DETECT_HUMAN_DETECTION,0),"0x08",humanDetectionBean),
                        0,5000,0);
            }
        });
        lsiHumanDetect = findViewById(R.id.lsi_human_detect);
        lsiHumanDetect.setOnRightClick(new ListSelectItem.OnRightImageClickListener() {
            @Override
            public void onClick(ListSelectItem listSelectItem, View view) {
                if (humanDetectionBean != null) {
                    humanDetectionBean.setEnable(listSelectItem.getRightValue() == SDKCONST.Switch.Open);
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        userId = FunSDK.GetId(userId,this);

        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        funDevice = FunSupport.getInstance().findDeviceById(devId);

        FunSDK.DevGetConfigByJson(userId, funDevice.getDevSn(), JsonConfig.DETECT_HUMAN_DETECTION,
                4096,0,5000,0);
        showWaitDialog();
    }

    @Override
    public int OnFunSDKResult(Message message, MsgContent msgContent) {
        hideWaitDialog();
        switch (message.what) {
            case EUIMSG.DEV_GET_JSON:
                if (message.arg1 < 0) {
                    Toast.makeText(this,R.string.not_support,Toast.LENGTH_LONG).show();
                    finish();
                    return 0;
                }
                if (StringUtils.contrast(msgContent.str, JsonConfig.DETECT_HUMAN_DETECTION)) {
                    if (msgContent.pData != null) {
                        HandleConfigData handleConfigData = new HandleConfigData();
                        if (handleConfigData.getDataObj(G.ToString(msgContent.pData), HumanDetectionBean.class)) {
                            humanDetectionBean = (HumanDetectionBean) handleConfigData.getObj();
                            if (humanDetectionBean != null) {
                                lsiHumanDetect.setRightImage(humanDetectionBean.isEnable() ? SDKCONST.Switch.Open : SDKCONST.Switch.Close);
                                break;
                            }
                        }
                    }
                }
                break;
            case EUIMSG.DEV_SET_JSON:
                if (message.arg1 < 0) {
                    Toast.makeText(this,R.string.set_config_f,Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, R.string.set_config_s, Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            default:
                break;
        }
        return 0;
    }
}
