package com.example.funsdkdemo.devices.lowpower;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.G;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDevBatteryLevelListener;
import com.lib.funsdk.support.OnFunDeviceWakeUpListener;
import com.lib.funsdk.support.models.FunDevStatus;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.bean.JsonConfig;
import com.lib.sdk.bean.StringUtils;
import com.lib.sdk.bean.idr.DevRingControlBean;

/**
 * @author hws
 * @name OldFunSdkDemo
 * @class name：com.example.funsdkdemo.devices.wakeup
 * @class 低功耗设备操作界面：唤醒设备、设备状态、提示音开关、电量显示
 * @time 2019-02-15 19:05
 */
public class LowPowerDevActivity extends ActivityDemo
        implements View.OnClickListener,OnFunDeviceWakeUpListener,OnFunDevBatteryLevelListener,IFunSDKResult{
    private ImageButton btnBack;
    private Button btnGetDevState;
    private Button btnWakeUpDev;
    private Button btnSleepDev;
    private TextView tvDevState;
    private FunDevice funDevice;
    private Button btnGetBatteryLevel;
    private TextView tvShowBatteryLevel;
    private boolean isReceiving;
    private ImageView ivPromptToneSwitch;
    private int userId;
    private DevRingControlBean devRingControl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_wake_up);
        initView();
        initData();
    }

    private void initView() {
        btnGetDevState = findViewById(R.id.btnGetDeviceState);
        btnWakeUpDev = findViewById(R.id.btnDevWakeUp);
        btnSleepDev = findViewById(R.id.btnDevSleep);
        tvDevState = findViewById(R.id.textDeviceState);
        btnBack = findViewById(R.id.backBtnInTopLayout);
        btnGetBatteryLevel = findViewById(R.id.btnGetBatteryLevel);
        tvShowBatteryLevel = findViewById(R.id.tvShowBatteryLevel);
        ivPromptToneSwitch = findViewById(R.id.iv_prompt_tone_switch);
        btnGetDevState.setOnClickListener(this);
        btnWakeUpDev.setOnClickListener(this);
        btnSleepDev.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnGetBatteryLevel.setOnClickListener(this);
        ivPromptToneSwitch.setOnClickListener(this);
    }

    private void initData() {
        userId = FunSDK.GetId(userId,this);
        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        funDevice = FunSupport.getInstance().findDeviceById(devId);
        if (funDevice != null) {
            FunSupport.getInstance().registerOnFunDeviceWakeUpListener(this);
            FunSupport.getInstance().registerOnFunDevBatteryLevelListener(this);
            FunSDK.DevGetConfigByJson(userId,
                    funDevice.getDevSn(),
                    JsonConfig.CFG_DEV_RING_CTRL,
                    1024,
                    -1,
                    5000,
                    0);
        }
        if (funDevice.devStatus != null) {
            tvDevState.setText(String.format(getString(R.string.device_state),
                    getString(funDevice.devStatus.getStatusResId())));
            tvDevState.setVisibility(View.VISIBLE);
        }else {
            tvDevState.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtnInTopLayout:
                finish();
                break;
            case R.id.btnGetDeviceState:
                showWaitDialog();
                FunSupport.getInstance().requestDeviceStatus(funDevice);
                break;
            case R.id.btnDevWakeUp:
                if (funDevice.devStatus == FunDevStatus.STATUS_CAN_NOT_WAKE_UP
                        || funDevice.devStatus == FunDevStatus.STATUS_OFFLINE) {
                    Toast.makeText(this, R.string.the_device_is_not_wakeup, Toast.LENGTH_SHORT).show();
                }else {
                    showWaitDialog();
                    FunSupport.getInstance().requestDevWakeUp(funDevice);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FunSupport.getInstance().requestDeviceStatus(funDevice);
                        }
                    }, 20000);
                }
                break;
            case R.id.btnDevSleep:
                showWaitDialog();
                FunSupport.getInstance().requestDevSleep(funDevice);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FunSupport.getInstance().requestDeviceStatus(funDevice);
                    }
                },3000);
                break;
            case R.id.btnGetBatteryLevel:
                if (!isReceiving) {
                    FunSupport.getInstance().requestRegisterDevBatteryLevel(funDevice);
                }else {
                    FunSupport.getInstance().requestCancelDevBatteryLevel(funDevice);
                    btnGetBatteryLevel.setText(R.string.start_receive_battery_level);
                    isReceiving = false;
                }
                break;
            case R.id.iv_prompt_tone_switch:
                if (devRingControl != null) {
                    devRingControl.setEnable(!devRingControl.isEnable());
                    ivPromptToneSwitch.setImageResource(devRingControl.isEnable() ? R.drawable.icon_checked_yes : R.drawable.icon_checked_no);
                    HandleConfigData handleConfigData = new HandleConfigData();
                    FunSDK.DevSetConfigByJson(userId,
                            funDevice.getDevSn(),
                            JsonConfig.CFG_DEV_RING_CTRL,
                            handleConfigData.getSendData(HandleConfigData.getFullName(JsonConfig.CFG_DEV_RING_CTRL, -1), devRingControl),
                            -1,
                            5000,
                            0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDeviceState(FunDevStatus state) {
        hideWaitDialog();
        tvDevState.setText(String.format(getString(R.string.device_state),
                getString(state.getStatusResId())));
    }

    @Override
    public void onWakeUpResult(boolean isSuccess, FunDevStatus state) {
        hideWaitDialog();
        Toast.makeText(this, isSuccess ? R.string.dev_wake_up_success
                : R.string.dev_wake_up_failed, Toast.LENGTH_LONG).show();
        tvDevState.setText(String.format(getString(R.string.device_state),
                getString(state.getStatusResId())));
    }

    @Override
    public void onSleepResult(boolean isSuccess, FunDevStatus status) {
        hideWaitDialog();
        Toast.makeText(this,isSuccess ? R.string.dev_sleep_success
                : R.string.dev_sleep_failed,Toast.LENGTH_LONG).show();
        tvDevState.setText(String.format(getString(R.string.device_state),
                getString(status.getStatusResId())));
    }

    @Override
    public void onRegister(boolean isSuccess) {
        if (isSuccess) {
            isReceiving = true;
            btnGetBatteryLevel.setText(R.string.stop_receive_battery_level);
        }
    }

    @Override
    public void onBatteryLevel(int devStorageStatus, int electable, int level) {
        tvShowBatteryLevel.setText(getString(R.string.battery_level) + ":" + level);
    }

    @Override
    public int OnFunSDKResult(Message message, MsgContent msgContent) {
        switch (message.what) {
            case EUIMSG.DEV_GET_JSON:
                if (StringUtils.contrast(JsonConfig.CFG_DEV_RING_CTRL,msgContent.str)) {
                    if (message.arg1 >= 0) {
                        HandleConfigData handleConfigData = new HandleConfigData();
                        if(handleConfigData.getDataObj(G.ToString(msgContent.pData), DevRingControlBean.class)) {
                            devRingControl = (DevRingControlBean) handleConfigData.getObj();
                            if (devRingControl != null) {
                                ivPromptToneSwitch.setImageResource(devRingControl.isEnable() ? R.drawable.icon_checked_yes : R.drawable.icon_checked_no);
                            }
                        }
                    }
                }
                break;
            case EUIMSG.DEV_SET_JSON:
                if (StringUtils.contrast(JsonConfig.CFG_DEV_RING_CTRL,msgContent.str)) {
                    Toast.makeText(this, message.arg1 >= 0 ? R.string.set_config_s : R.string.set_config_f, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return 0;
    }
}
