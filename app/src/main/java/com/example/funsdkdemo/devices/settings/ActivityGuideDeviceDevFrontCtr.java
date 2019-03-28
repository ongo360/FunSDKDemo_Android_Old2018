package com.example.funsdkdemo.devices.settings;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by niutong on 2017-05-31.
 * Discription:
 */

public class ActivityGuideDeviceDevFrontCtr extends ActivityDemo implements View.OnClickListener, IFunSDKResult{

    private Button mBtnOk;
    private Button mBtnEsc;
    private Button mBtnMenu;
    private Button mBtnLeft;
    private Button mBtnRight;
    private Button mBtnUp;
    private Button mBtnDown;
    private TextView mTitle;
    private ImageButton mBack;
    private FunDevice mFunDevice;

    private final String TAG = "ActivityGuideDeviceDevFrontCtr";

    private int mHandler;

    enum NetKeyBoardValue{

        NET_KEY_UP("Up"), NET_DOWN("Down"), NET_LEFT("Left"), NET_RIGHT("Right"), NET_KEY_PGUP("PageUp"), NET_KEY_PGDN("PageDown"), NET_KEY_RET("Enter"), NET_KEY_ESC("Esc"),
        NET_KEY_MENU("Menu");

        String value;

        private NetKeyBoardValue(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup_devfrontctr);
        initView();
        int Sn = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        mFunDevice = FunSupport.getInstance().findDeviceById(Sn);
        mHandler = FunSDK.RegUser(this);
    }

    private void initView(){

        mTitle = (TextView) findViewById(R.id.textViewInTopLayout);
        mTitle.setText(R.string.device_setup_frontctr);
        mBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
        mBack.setOnClickListener(this);

        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);
        mBtnEsc = (Button) findViewById(R.id.btn_esc);
        mBtnEsc.setOnClickListener(this);
        mBtnMenu = (Button) findViewById(R.id.btn_menu);
        mBtnMenu.setOnClickListener(this);
        mBtnLeft = (Button) findViewById(R.id.btn_left);
        mBtnLeft.setOnClickListener(this);
        mBtnRight = (Button) findViewById(R.id.btn_right);
        mBtnRight.setOnClickListener(this);
        mBtnUp = (Button) findViewById(R.id.btn_up);
        mBtnUp.setOnClickListener(this);
        mBtnDown = (Button) findViewById(R.id.btn_down);
        mBtnDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                String jsonokdown = getJson(NetKeyBoardValue.NET_KEY_RET.getValue(), "KeyDown");
                doOperate(jsonokdown);
                String jsonokup = getJson(NetKeyBoardValue.NET_KEY_RET.getValue(), "KeyUp");
                doOperate(jsonokup);
                break;
            case R.id.btn_esc:
                String jsonescdown = getJson(NetKeyBoardValue.NET_KEY_ESC.getValue(), "KeyDown");
                doOperate(jsonescdown);
                String jsonescup = getJson(NetKeyBoardValue.NET_KEY_ESC.getValue(), "KeyUp");
                doOperate(jsonescup);
                break;
            case R.id.btn_menu:
                String jsonmenudown = getJson(NetKeyBoardValue.NET_KEY_MENU.getValue(), "KeyDown");
                doOperate(jsonmenudown);
                String jsonmenuup = getJson(NetKeyBoardValue.NET_KEY_MENU.getValue(), "KeyUp");
                doOperate(jsonmenuup);
                break;
            case R.id.btn_left:
                String jsonleftdown = getJson(NetKeyBoardValue.NET_LEFT.getValue(), "KeyDown");
                doOperate(jsonleftdown);
                String jsonleftup = getJson(NetKeyBoardValue.NET_LEFT.getValue(), "KeyUp");
                doOperate(jsonleftup);
                break;
            case R.id.btn_right:
                String jsonrightdown = getJson(NetKeyBoardValue.NET_RIGHT.getValue(), "KeyDown");
                doOperate(jsonrightdown);
                String jsonrightup = getJson(NetKeyBoardValue.NET_RIGHT.getValue(), "KeyUp");
                doOperate(jsonrightup);
                break;
            case R.id.btn_up:
                String jsonupdown = getJson(NetKeyBoardValue.NET_KEY_UP.getValue(), "KeyDown");
                doOperate(jsonupdown);
                String jsonupup = getJson(NetKeyBoardValue.NET_KEY_UP.getValue(), "KeyUp");
                doOperate(jsonupup);
                break;
            case R.id.btn_down:
                String jsondowndown = getJson(NetKeyBoardValue.NET_DOWN.getValue(), "KeyDown");
                doOperate(jsondowndown);
                String jsondownup = getJson(NetKeyBoardValue.NET_DOWN.getValue(), "KeyUp");
                doOperate(jsondownup);
                break;
            case R.id.backBtnInTopLayout:
                finish();
                break;
        }
    }

    private void doOperate(String json4){
        FunSDK.DevCmdGeneral(mHandler, mFunDevice.devSn, 1550, "OPNetKeyboard", 0, 10000, json4.getBytes(), -1, 0);
    }

    private String getJson(String value, String statu){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonVS = new JSONObject();
        try {
            jsonVS.put("Value", value);
            jsonVS.put("Status", statu);
            jsonObject.put("Name", "OPNetKeyboard");
            jsonObject.put("OPNetKeyboard", jsonVS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("TTTT--->>> = " + jsonObject.toString());
        return  jsonObject.toString();
    }

    @Override
    public int OnFunSDKResult(Message msg, MsgContent msgContent) {

        FunLog.d(TAG, "msg.what : " + msg.what);
        FunLog.d(TAG, "msg.arg1 : " + msg.arg1 + " [" + FunError.getErrorStr(msg.arg1) + "]");
        FunLog.d(TAG, "msg.arg2 : " + msg.arg2);
        if (null != msgContent) {
            FunLog.d(TAG, "msgContent.sender : " + msgContent.sender);
            FunLog.d(TAG, "msgContent.seq : " + msgContent.seq);
            FunLog.d(TAG, "msgContent.str : " + msgContent.str);
            FunLog.d(TAG, "msgContent.arg3 : " + msgContent.arg3);
            FunLog.d(TAG, "msgContent.pData : " + msgContent.pData);
        }
        return 0;
    }
}
