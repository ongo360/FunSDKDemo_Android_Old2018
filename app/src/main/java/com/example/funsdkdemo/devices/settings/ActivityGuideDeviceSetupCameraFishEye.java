package com.example.funsdkdemo.devices.settings;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basic.G;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.config.BaseConfig;
import com.lib.funsdk.support.config.CameraFishEye;
import com.lib.funsdk.support.config.CameraWhiteLight;
import com.lib.funsdk.support.config.FishEyePlatform;
import com.lib.funsdk.support.config.SystemFunction;
import com.lib.funsdk.support.models.FunDevice;

import java.util.List;

/**
 *
 * @author niutong
 * @date 2017-10-19
 * Discription: 我司鱼眼灯泡分两种，所以这部分代码分两种情况处理灯泡操作  |  We have two kinds of fish eye bulbs, so this part of the code is divided into two cases to deal with the operation of the bulb
 */

public class ActivityGuideDeviceSetupCameraFishEye extends ActivityDemo implements IFunSDKResult,View.OnClickListener{

    private static final String TAG = "ActivityGuideDeviceSetupCameraFishEye";

    private TimePicker startTime;
    private TimePicker endTime;
    private Spinner spinnerMode;
    private Spinner spinnerAppType;
    private SeekBar seekbarDuty;
    private ImageView buttonEnable;
    private Button button_ok;
    private TextView mTypeOrLevel;
    private TextView mTextDuty;
    private LinearLayout mLayoutIntelligent;

    private TextView mTextTitle;
    private ImageButton mBtnBack;

    private int mHander;
    private FunDevice mFunDevice;

    private boolean supportBulb = false;    //  the device support bulb ?
    private boolean ifSupportInteLLigent = false; // When moving objects are detected, auto light
    private CameraBulbType DeviceBulbType;

    enum CameraBulbType{
        NORMALFISHEYEBULB, SMARTFISHEYEBULB
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup_camerafisheye);
        mHander = FunSDK.RegUser(this);
        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        mFunDevice = FunSupport.getInstance().findDeviceById(devId);
        initView();
        initData();
        showWaitDialog();
    }

    private void initData(){
        getConfigDataByCmd(FishEyePlatform.CONFIG_NAME, 1360);

        SystemFunction systemFunction = (SystemFunction) mFunDevice.getConfig(SystemFunction.CONFIG_NAME);
        //get if the device support bulb whitelight   // 是否支持whitelight灯泡配置
        if (systemFunction == null) {
            getConfigData(SystemFunction.CONFIG_NAME);
        }else {
            supportBulb = getSupportOfWhiteLiht(systemFunction, "SupportCameraWhiteLight");
            ifSupportInteLLigent = getSupportOfWhiteLiht(systemFunction, "SupportDoubleLightBulb");
        }

        if (supportBulb){
            getConfigData(CameraWhiteLight.CONFIGNAME);
        }
    }


    private void getConfigData(String configName){
        FunSDK.DevGetConfigByJson(mHander, mFunDevice.getDevSn(), configName, 4096, -1, 10000, mFunDevice.getId());
    }

    private void getConfigDataByCmd(String StrConfigName, int mDevCmdID){
        FunSDK.DevCmdGeneral(mHander, mFunDevice.getDevSn(), mDevCmdID, StrConfigName, 1024, 10000, null, -1, mFunDevice.getId());
    }

    private void initView() {

        mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);
        mTypeOrLevel = (TextView) findViewById(R.id.TypeOrLevel);
        mTextDuty = (TextView) findViewById(R.id.textDuty);
        mLayoutIntelligent = (LinearLayout) findViewById(R.id.layout_intelligent);
        // 返回
        mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);

        mTextTitle.setText(R.string.device_setup_camerafisheye);

        startTime = (TimePicker) findViewById(R.id.timepic_start);
        endTime = (TimePicker) findViewById(R.id.timepic_end);

        startTime.setIs24HourView(true);
        endTime.setIs24HourView(true);

        spinnerMode = (Spinner) findViewById(R.id.spinner_mode);
        spinnerAppType = (Spinner) findViewById(R.id.spinner_apptype);
        seekbarDuty = (SeekBar) findViewById(R.id.duty_seekbar);
        buttonEnable = (ImageView) findViewById(R.id.image_enable);
        buttonEnable.setOnClickListener(this);
        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(this);

    }

    private void refreshView(){
        if (!supportBulb) {
            return;
        }
        if (DeviceBulbType == CameraBulbType.NORMALFISHEYEBULB) {
            CameraFishEye mCameraFishEye = (CameraFishEye) mFunDevice.getConfig(CameraFishEye.CONFIG_NAME);
            if (mCameraFishEye != null) {
                spinnerMode.setSelection(mCameraFishEye.WorkMode, true);
                spinnerAppType.setSelection(mCameraFishEye.AppType, true);
                seekbarDuty.setProgress(mCameraFishEye.Duty);

                startTime.setCurrentHour(mCameraFishEye.mLightOnSec.SHour);
                startTime.setCurrentMinute(mCameraFishEye.mLightOnSec.SMinute);
                endTime.setCurrentHour(mCameraFishEye.mLightOnSec.EHour);
                endTime.setCurrentMinute(mCameraFishEye.mLightOnSec.EMinute);

                buttonEnable.setSelected(mCameraFishEye.mLightOnSec.Enable == 1 ? true : false);
            }
        }

        if (DeviceBulbType == CameraBulbType.SMARTFISHEYEBULB) {
            CameraWhiteLight cameraWhiteLight = (CameraWhiteLight) mFunDevice.getConfig(CameraWhiteLight.CONFIGNAME);
            if (cameraWhiteLight == null) {
                return;
            }
            String[] mItems;
            if (ifSupportInteLLigent) {
                mItems = getResources().getStringArray(R.array.bulb_white_mode_intelligent);

                mTypeOrLevel.setText("Sensitivity:");
                String[] levels = getResources().getStringArray(R.array.bulb_white_level_inelligent);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, levels);
                spinnerAppType.setAdapter(adapter);
                spinnerAppType.setSelection(cameraWhiteLight.moveTrigLight.Level - 1, true);

                mTextDuty.setText("Duration:");
                // 0 ~ 3600 s
                seekbarDuty.setMax(3600);
                seekbarDuty.setProgress(cameraWhiteLight.moveTrigLight.Duration);
            }else {
                mItems = getResources().getStringArray(R.array.bulb_white_mode);
                mLayoutIntelligent.setVisibility(View.GONE);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
            spinnerMode.setAdapter(arrayAdapter);
            spinnerMode.setSelection(getIntFromString(cameraWhiteLight.WorkMode), true);

            startTime.setCurrentHour(cameraWhiteLight.workPeriod.SHour);
            startTime.setCurrentMinute(cameraWhiteLight.workPeriod.SMinute);
            endTime.setCurrentHour(cameraWhiteLight.workPeriod.EHour);
            endTime.setCurrentMinute(cameraWhiteLight.workPeriod.EMinute);
        }
    }

    private int getIntFromString(String mode){
        if (mode.equals("Auto")) {
            return 0;
        }else if (mode.equals("Timming")){
            return 1;
        }else if (mode.equals("KeepOpen")){
            return 2;
        }else if (mode.equals("Close")){
            return 3;
        }else if (mode.equals("Intelligent")){
            return 4;
        }
        return 0;
    }

    private String getStringFromInt(int mode){
        switch (mode) {
            case 0:
                return "Auto";
            case 1:
                return "Timing";
            case 2:
                return "KeepOpen";
            case 3:
                return "Close";
            case 4:
                return "Intelligent";
            default:
                return "Auto";
        }
    }

    private void setClickListener(){
        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CameraFishEye cameraFishEye = (CameraFishEye) mFunDevice.getConfig(CameraFishEye.CONFIG_NAME);
                CameraWhiteLight cameraWhiteLight = (CameraWhiteLight) mFunDevice.getConfig(CameraWhiteLight.CONFIGNAME);
                if (cameraFishEye != null) {
                    cameraFishEye.WorkMode = position;
                    sendConfigJson();
                }

                if (cameraWhiteLight != null) {
                    cameraWhiteLight.WorkMode = getStringFromInt(position);
                    sendConfigJson();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAppType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CameraFishEye cameraFishEye = (CameraFishEye) mFunDevice.getConfig(CameraFishEye.CONFIG_NAME);
                CameraWhiteLight cameraWhiteLight = (CameraWhiteLight) mFunDevice.getConfig(CameraWhiteLight.CONFIGNAME);
                if (cameraFishEye != null) {
                    cameraFishEye.AppType = position;
                    sendConfigJson();
                }
                // 该项配置只有 1  3  5  生效
                if (cameraWhiteLight != null) {
                    cameraWhiteLight.moveTrigLight.Level = position + 1;
                    sendConfigJson();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekbarDuty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    CameraFishEye cameraFishEye = (CameraFishEye) mFunDevice.getConfig(CameraFishEye.CONFIG_NAME);
                    CameraWhiteLight cameraWhiteLight = (CameraWhiteLight) mFunDevice.getConfig(CameraWhiteLight.CONFIGNAME);
                    if (cameraFishEye != null) {
                        cameraFishEye.Duty = progress;
                        sendConfigJson();
                    }

                    if (cameraWhiteLight != null) {
                        cameraWhiteLight.moveTrigLight.Duration = progress;
                        sendConfigJson();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void sendConfigJson(){
        CameraFishEye mCameraFishEye = (CameraFishEye) mFunDevice.getConfig(CameraFishEye.CONFIG_NAME);
        CameraWhiteLight cameraWhiteLight = (CameraWhiteLight) mFunDevice.getConfig(CameraWhiteLight.CONFIGNAME);
        if (mCameraFishEye != null) {
            FunSDK.DevSetConfigByJson(mHander, mFunDevice.getDevSn(), CameraFishEye.CONFIG_NAME, mCameraFishEye.getSendMsg(), -1, 10000, mFunDevice.getId());
        }

        if (cameraWhiteLight != null) {
            FunSDK.DevSetConfigByJson(mHander, mFunDevice.getDevSn(), CameraWhiteLight.CONFIGNAME, cameraWhiteLight.getSendMsg(), -1, 10000, mFunDevice.getId());
        }
    }

    @Override
    public int OnFunSDKResult(Message message, MsgContent msgContent) {

        hideWaitDialog();

        if (message.arg1 <= 0) {
            Toast.makeText(ActivityGuideDeviceSetupCameraFishEye.this, msgContent.str + " " + message.arg1, Toast.LENGTH_SHORT).show();
            return 0;
        }

        switch (message.what) {
            case EUIMSG.DEV_GET_JSON:
            case EUIMSG.DEV_CMD_EN:
                FunDevice funDevice = FunSupport.getInstance().findDeviceById(msgContent.seq);
                if (mFunDevice != funDevice) {
                    return 0;
                }

                if (msgContent.pData != null){
                    String json = G.ToString(msgContent.pData);
                    FunLog.i(TAG, "EUIMSG.DEV_GET_JSON --> json: " + json);
                    FunLog.i(TAG, "configName = " + msgContent.str);

                    if (json == null) {
                        Toast.makeText(this, "Can not get json !!", Toast.LENGTH_SHORT).show();
                        return 0;
                    }

                    try {
                        BaseConfig devJson = funDevice.checkConfig(msgContent.str);   // get json 实例
                        // 解析配置
                        if ( !devJson.onParse(json) ) {
                            Toast.makeText(this, "Can not get json !!", Toast.LENGTH_SHORT).show();
                            return 0;
                        }
                        // 保存信息到设备信息实例里面
                        mFunDevice.setConfig(devJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (msgContent.str.equals(FishEyePlatform.CONFIG_NAME)) {
                    FishEyePlatform fishEyePlatform = (FishEyePlatform) mFunDevice.getConfig(FishEyePlatform.CONFIG_NAME);
                    //get if the device support bulb normal   //  是否支持通用鱼眼灯泡配置
                    if (fishEyePlatform != null) {
                        if (fishEyePlatform.LedAbility == 1) {
                            supportBulb = true;
                        }
                    }

                    if (supportBulb) {
                        // get CameraFishEye , do not care others
                        getConfigData(CameraFishEye.CONFIG_NAME);
                    }
                }else if (msgContent.str.equals(CameraFishEye.CONFIG_NAME))
                {
                    DeviceBulbType = CameraBulbType.NORMALFISHEYEBULB;
                    refreshView();
                    setClickListener();
                }else if (msgContent.str.equals(SystemFunction.CONFIG_NAME)){
                    SystemFunction systemFunction = (SystemFunction) mFunDevice.getConfig(SystemFunction.CONFIG_NAME);
                    if (systemFunction != null) {
                        supportBulb = getSupportOfWhiteLiht(systemFunction, "SupportCameraWhiteLight");
                        ifSupportInteLLigent = getSupportOfWhiteLiht(systemFunction, "SupportDoubleLightBulb");
                    }
                    // get if the device support bulb whitelight   //是否支持whitelight 灯泡配置
                    if (supportBulb) {
                        getConfigData(CameraWhiteLight.CONFIGNAME);
                    }
                }else if (msgContent.str.equals(CameraWhiteLight.CONFIGNAME)){
                    DeviceBulbType = CameraBulbType.SMARTFISHEYEBULB;
                    refreshView();
                    setClickListener();
                }
                break;
            case EUIMSG.DEV_SET_JSON:
                Toast.makeText(this, "Setting Success !!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
       
        return 0;
    }

    private boolean getSupportOfWhiteLiht(SystemFunction systemFunction, String function){
        List<SystemFunction.FunctionAttr> list = systemFunction.getFunctionAttrs();
        for (SystemFunction.FunctionAttr f : list) {
            if (f.name.equals("OtherFunction")) {
                for (SystemFunction.FunctionItem func : f.funcs) {
                    if (func.attrName.equals(function)) {
                        return func.isSupport;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        CameraFishEye cameraFishEye = (CameraFishEye) mFunDevice.getConfig(CameraFishEye.CONFIG_NAME);
        CameraWhiteLight cameraWhiteLight = (CameraWhiteLight) mFunDevice.getConfig(CameraWhiteLight.CONFIGNAME);
        switch (v.getId()) {
            case R.id.backBtnInTopLayout:
                finish();
                break;
            case R.id.image_enable:
                buttonEnable.setSelected(!buttonEnable.isSelected());
                cameraFishEye.mLightOnSec.Enable = buttonEnable.isSelected() == true ? 1 : 0;
                sendConfigJson();
                break;
            case R.id.button_ok:
                if (DeviceBulbType == CameraBulbType.NORMALFISHEYEBULB) {

                    cameraFishEye.mLightOnSec.SHour = startTime.getCurrentHour();
                    cameraFishEye.mLightOnSec.SMinute = startTime.getCurrentMinute();
                    cameraFishEye.mLightOnSec.EHour = endTime.getCurrentHour();
                    cameraFishEye.mLightOnSec.EMinute = endTime.getCurrentMinute();
                }

                if (DeviceBulbType == CameraBulbType.SMARTFISHEYEBULB)
                {
                    cameraWhiteLight.workPeriod.SHour = startTime.getCurrentHour();
                    cameraWhiteLight.workPeriod.SMinute = startTime.getCurrentMinute();
                    cameraWhiteLight.workPeriod.EHour = endTime.getCurrentHour();
                    cameraWhiteLight.workPeriod.EMinute = endTime.getCurrentMinute();
                }
                sendConfigJson();
                break;
            default:
                break;
        }
    }
}
