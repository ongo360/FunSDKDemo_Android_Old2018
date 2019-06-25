package com.example.funsdkdemo;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.basic.G;
import com.example.funsdkdemo.ListAdapterFunDevice.OnFunDeviceItemClickListener;
import com.example.funsdkdemo.alarm.ActivityGuideDeviceAlarmResult;
import com.example.funsdkdemo.devices.ActivityGuideDeviceTransCom;
import com.example.funsdkdemo.devices.lowpower.LowPowerDevActivity;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnAddSubDeviceResultListener;
import com.lib.funsdk.support.OnFunDeviceListener;
import com.lib.funsdk.support.config.JsonConfig;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunLoginType;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo: 搜索显示同一个局域网内的设备列表
 * 1. 切换访问模式为本地访问  - FunSDK.SysInitLocal()
 * 2. 注册设备列表更新监听  - FunSupport.registerOnFunDeviceListener()
 * 3. 搜索局域网内的设备 - FunSDK.DevSearchDevice()
 * 4. 搜索结果通过监听返回  - onLanDeviceListChanged()/标识设备列表变化了,界面可以刷新了
 * 5. 退出并注销监听
 */
public class ActivityGuideDeviceListLan extends ActivityDemo implements OnClickListener, OnFunDeviceListener, OnFunDeviceItemClickListener, OnAddSubDeviceResultListener {


    private TextView mTextTitle = null;
    private ImageButton mBtnBack = null;

    private ImageButton mBtnRefresh = null;

    private ExpandableListView mListView = null;
    private ListAdapterFunDevice mAdapter = null;
    private List<FunDevice> mLanDeviceList = new ArrayList<FunDevice>();


    private final int MESSAGE_REFRESH_DEVICE_STATUS = 0x100;
    private String mSubDeviceSN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_list);

        mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);
        mTextTitle.setText(R.string.device_list_lan);

        mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);

        View layoutRefresh = setNavagateRightButton(R.layout.imagebutton_refresh);
        mBtnRefresh = (ImageButton) layoutRefresh.findViewById(R.id.btnRefresh);
        mBtnRefresh.setOnClickListener(this);

        mListView = (ExpandableListView) findViewById(R.id.listViewDevice);
        mAdapter = new ListAdapterFunDevice(this, mLanDeviceList);
        // 局域网内设备不允许删除和重命名,不需要再连接AP
        mAdapter.setCanRemoved(false);
        mAdapter.setCanRenamed(false);
        mAdapter.setNeedConnectAP(false);
        mAdapter.setOnFunDeviceItemClickListener(this);
        mListView.setAdapter(mAdapter);

        // 刷新设备列表
        refreshLanDeviceList();

        // Demo，如果是进入设备列表就切换到本地模式,退出时切换回NET模式
        FunSupport.getInstance().setLoginType(FunLoginType.LOGIN_BY_LOCAL);

        // 监听设备类事件
        FunSupport.getInstance().registerOnFunDeviceListener(this);

        FunSupport.getInstance().registerOnAddSubDeviceResultListener(this);

        // 打开之后进行一次搜索
        requestToGetLanDeviceList();
    }


    @Override
    protected void onDestroy() {

        // 注销设备事件监听
        FunSupport.getInstance().removeOnFunDeviceListener(this);

        // 切换回网络访问
        FunSupport.getInstance().setLoginType(FunLoginType.LOGIN_BY_INTENTT);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtnInTopLayout: {
                // 返回/退出
                finish();
            }
            break;
            case R.id.btnRefresh: {
                // 刷新设备列表
                requestToGetLanDeviceList();
            }
            break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private void requestToGetLanDeviceList() {
        if (!FunSupport.getInstance().requestLanDeviceList()) {
            showToast(R.string.guide_message_error_call);
        } else {
            showWaitDialog();
        }
    }

    private void refreshLanDeviceList() {
        hideWaitDialog();

        mLanDeviceList.clear();

        mLanDeviceList.addAll(FunSupport.getInstance().getLanDeviceList());

        mAdapter.notifyDataSetChanged();

        // 延时100毫秒更新设备消息
        mHandler.removeMessages(MESSAGE_REFRESH_DEVICE_STATUS);
        if (mLanDeviceList.size() > 0) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_DEVICE_STATUS, 100);
        }
    }


    /**
     * 以下函数实现来自OnFunDeviceListener()，监听设备列表变化
     */
    @Override
    public void onDeviceListChanged() {
    }

    @Override
    public void onDeviceStatusChanged(final FunDevice funDevice) {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeviceAddedSuccess() {

    }


    @Override
    public void onDeviceAddedFailed(Integer errCode) {

    }


    @Override
    public void onDeviceRemovedSuccess() {

    }


    @Override
    public void onDeviceRemovedFailed(Integer errCode) {

    }

    @Override
    public void onAPDeviceListChanged() {

    }

    @Override
    public void onLanDeviceListChanged() {
        refreshLanDeviceList();
    }


    /**
     * 以下4个函数来自OnFunDeviceItemClickListener()，监听设备中的操作按钮选择
     */
    @Override
    public void onFunDeviceRenameClicked(FunDevice funDevice) {
        // 设备重命名

    }


    @Override
    public void onFunDeviceConnectClicked(FunDevice funDevice) {
        // 打开设备/连接设备
    }


    @Override
    public void onFunDeviceControlClicked(FunDevice funDevice) {
        // 打开设备控制
        DeviceActivitys.startDeviceActivity(this, funDevice);
    }

    @Override
    public void onFunDeviceAlarmClicked(FunDevice funDevice) {
        // 设备报警
        if (null != funDevice) {
            if (!FunSupport.getInstance().hasLogin()) {
                // 报警功能需要用户登录后才能使用
                showToast(R.string.user_logout_not_login);
                return;
            }

            Intent intent = new Intent();
            intent.setClass(this, ActivityGuideDeviceAlarmResult.class);
            intent.putExtra("FUN_DEVICE_ID", funDevice.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onFunDeviceTransComClicked(FunDevice funDevice) {
        if (null != funDevice) {
            Intent intent = new Intent();
            intent.setClass(this, ActivityGuideDeviceTransCom.class);
            intent.putExtra("FUN_DEVICE_ID", funDevice.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onFunDeviceRemoveClicked(FunDevice funDevice) {
        // 删除设备

    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REFRESH_DEVICE_STATUS: {
                    FunSupport.getInstance().requestAllLanDeviceStatus();
                }
                break;
            }
        }

    };


    /* (non-Javadoc)
     * @see com.example.funsdkdemo.ListAdapterFunDevice.OnFunDeviceItemClickListener#onFunDevice433Control(com.lib.funsdk.support.models.FunDevice)
     */
    @Override
    public void onFunDevice433Control(FunDevice funDevice) {
        // TODO Auto-generated method stub
//        if (null != mSubDeviceSN && mSubDeviceSN != "") {
//            showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("RFDeviceSN", mSubDeviceSN);
        jsonObject.put("DeviceType", "Relay");
        jsonObject.put("Operate", "Close");
        jsonObject.put("Freq", "433");
        jsonObject.put("BaudRate", "100");
        FunSupport.getInstance().requestControlSubDevice(funDevice, JsonConfig.ControlSubDevice, jsonObject.toJSONString());
//        }else{
//            Toast.makeText(this, "子设备序列号为空", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onFunDeviceWakeUp(FunDevice funDevice) {
        Intent intent = new Intent();
        intent.setClass(this, LowPowerDevActivity.class);
        intent.putExtra("FUN_DEVICE_ID", funDevice.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFunDeviceCloud(FunDevice funDevice) {

    }


    /* (non-Javadoc)
     * @see com.example.funsdkdemo.ListAdapterFunDevice.OnFunDeviceItemClickListener#onFunDevice433AddSub(com.lib.funsdk.support.models.FunDevice)
     */
    @Override
    public void onFunDevice433AddSub(FunDevice funDevice) {
        // TODO Auto-generated method stub
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("DeviceType", "Relay");
        jsonObject.put("DeviceMold", "Control");
        jsonObject.put("Operate", "Close");
        jsonObject.put("Freq", "433");
        jsonObject.put("BaudRate", "100");
        FunSupport.getInstance().requestDeviceAddSubDev(funDevice, JsonConfig.AddSubDevice, jsonObject.toJSONString());
    }

    @Override
    public void onAddSubDeviceFailed(FunDevice funDevice, MsgContent msgContent) {
        if (msgContent.str.equals(JsonConfig.AddSubDevice)) {
            Toast.makeText(this, "添加子设备失败", Toast.LENGTH_SHORT).show();

        }
        hideWaitDialog();
    }

    @Override
    public void onAddSubDeviceSuccess(FunDevice funDevice, MsgContent msgContent) {
        if (msgContent.str.equals(JsonConfig.AddSubDevice)) {
            JSONObject jsonObject;
            jsonObject = (JSONObject) JSONObject.parse(G.ToString(msgContent.pData));
            mSubDeviceSN = (String) jsonObject.get("RFDeviceSN");
            System.out.println("zyy------------mSubDeviceSN    " + mSubDeviceSN);
            Toast.makeText(this, "添加子设备成功", Toast.LENGTH_SHORT).show();
        }
        hideWaitDialog();
    }
}
