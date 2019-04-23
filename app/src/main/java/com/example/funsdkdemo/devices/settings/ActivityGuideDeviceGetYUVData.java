package com.example.funsdkdemo.devices.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.lib.EFUN_ATTR.EOA_MEDIA_YUV_USER;

/**
 * @author hws
 * @name FunSDKDemo_Android_Old2018
 * @class name：com.example.funsdkdemo.devices
 * @class 获取YUV数据
 * @time 2019-04-22 19:20
 */
public class ActivityGuideDeviceGetYUVData extends ActivityDemo
        implements IFunSDKResult {
    private int userId;//回调ID
    private int playHandle;//播放句柄
    private FunDevice funDevice;
    private TextView tvState;
    private boolean isSaveYUV;
    private String saveYUVFileName;
    private  FileOutputStream fos;
    private long saveYUVDataTime;
    private Button btnSaveYUV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_get_yuv_data);
        initView();
        initData();
    }

    private void initView() {
        tvState = findViewById(R.id.tv_state);
        btnSaveYUV = findViewById(R.id.save_yuv_data);
        findViewById(R.id.backBtnInTopLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        int devPos = intent.getIntExtra("FUN_DEVICE_ID", 0);
        funDevice = FunSupport.getInstance().findDeviceById(devPos);
        //注册与设备交互的监听事件
        userId = FunSDK.GetId(userId,this);
    }

    public void onStartGetYUV(View view) {
        //如果只要获取YUV数据 不需要显示的话 第五个参数 传null就可以了 其他参数和实时播放调用一样
        playHandle = FunSDK.MediaRealPlay(userId, funDevice.getDevSn(),
                0, SDKCONST.StreamType.Main, null,0);
        FunSDK.SetIntAttr(playHandle,EOA_MEDIA_YUV_USER,userId);
    }

    public void onStopGetYUV(View view) {
        if (playHandle != 0) {
            FunSDK.MediaStop(playHandle);
            playHandle = 0;
        }
    }

    public void onSaveYUV(View view) {
        this.isSaveYUV = !isSaveYUV;
        if (isSaveYUV) {
            ((Button)view).setText(R.string.stop_save_yuv_data_to_file);
            saveYUVFileName = FunPath.getMediaPath(this) + File.separator  + "funsdkDemoTest.yuv";
            try {
                File file = new File(saveYUVFileName);
                if (file.exists()) {
                    file.delete();
                }
                fos = new FileOutputStream(file);
                saveYUVDataTime = System.currentTimeMillis();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
            ((Button)view).setText(R.string.start_save_yuv_data_to_file);
        }
    }

    public void onYUVToImage(View view) {

    }

    @Override
    public int OnFunSDKResult(Message message, MsgContent msgContent) {
        switch (message.what) {
            case EUIMSG.START_PLAY:
                if (message.arg1 >= 0) {
                    tvState.setText(R.string.start_play_success);
                }else {
                    tvState.setText(getString(R.string.start_play_failed) + message.arg1);
                }
                break;
            case EUIMSG.ON_PLAY_BUFFER_END:
                if (message.arg1 >= 0) {
                    tvState.setText(R.string.get_buffer_success);
                }else {
                    tvState.setText(getString(R.string.get_buffer_failed) + message.arg1);
                }
                break;
            case EUIMSG.ON_YUV_DATA:
                if (message.arg1 >= 0) {
                    tvState.setText(R.string.get_yuv_success);
                }else {
                    tvState.setText(getString(R.string.get_yuv_failed) + message.arg1);
                }

                if (isSaveYUV && msgContent.pData != null && fos != null) {
					try {
					    long times = System.currentTimeMillis() - saveYUVDataTime;
					    if (times <= 5 * 1000) {
                            tvState.setText(getString(R.string.save_yuv) + saveYUVFileName + ":" + times / 1000);
                            fos.write(msgContent.pData);
                            fos.flush();
                        }else {
					        fos.close();
					        fos = null;
					        isSaveYUV = false;
                            btnSaveYUV.setText(R.string.start_save_yuv_data_to_file);
                        }
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
                break;
        }
        return 0;
    }
}
