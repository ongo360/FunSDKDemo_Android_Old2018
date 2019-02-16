package com.example.funsdkdemo.devices.wakeup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceWakeUpListener;
import com.lib.funsdk.support.models.FunDevStatus;
import com.lib.funsdk.support.models.FunDevice;

/**
 * @author hws
 * @name OldFunSdkDemo
 * @class name：com.example.funsdkdemo.devices.wakeup
 * @class 唤醒设备
 * @time 2019-02-15 19:05
 */
public class DevWakeUpActivity extends ActivityDemo
        implements View.OnClickListener,OnFunDeviceWakeUpListener{
    private ImageButton btnBack;
    private Button btnGetDevState;
    private Button btnWakeUpDev;
    private Button btnSleepDev;
    private TextView tvDevState;
    private FunDevice funDevice;
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
        btnGetDevState.setOnClickListener(this);
        btnWakeUpDev.setOnClickListener(this);
        btnSleepDev.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void initData() {
        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        funDevice = FunSupport.getInstance().findDeviceById(devId);
        if (funDevice != null) {
            FunSupport.getInstance().registerOnFunDeviceWakeUpListener(this);
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
}
