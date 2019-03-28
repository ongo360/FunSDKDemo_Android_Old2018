package com.example.funsdkdemo.devices.settings;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.adapter.FileDownloadListAdapter;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.OnFunDeviceRecordListener;
import com.lib.funsdk.support.config.OPCompressPic;
import com.lib.funsdk.support.models.FunDevRecordFile;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.sdk.struct.H264_DVR_FINDINFO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017-03-21.
 * Discription: Download record file from device
 */

public class ActivityGuideDeviceSetupDownload extends ActivityDemo implements OnFunDeviceRecordListener,OnFunDeviceOptListener {

    private Calendar calendar = null;
    private FunDevice mFunDevice = null;
    private FileDownloadListAdapter mFileDownloadListAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_device_setup_download);
        calendar = Calendar.getInstance();

    }

    private void searchFile(){
        int[] time = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)};

        H264_DVR_FINDINFO info = new H264_DVR_FINDINFO();
        info.st_1_nFileType = SDKCONST.FileType.SDK_RECORD_ALL;
        info.st_2_startTime.st_0_dwYear = time[0];
        info.st_2_startTime.st_1_dwMonth = time[1];
        info.st_2_startTime.st_2_dwDay = time[2];
        info.st_2_startTime.st_3_dwHour = 0;
        info.st_2_startTime.st_4_dwMinute = 0;
        info.st_2_startTime.st_5_dwSecond = 0;
        info.st_3_endTime.st_0_dwYear = time[0];
        info.st_3_endTime.st_1_dwMonth = time[1];
        info.st_3_endTime.st_2_dwDay = time[2];
        info.st_3_endTime.st_3_dwHour = 23;
        info.st_3_endTime.st_4_dwMinute = 59;
        info.st_3_endTime.st_5_dwSecond = 59;
        info.st_0_nChannelN0 = mFunDevice.CurrChannel;
        FunSupport.getInstance().requestDeviceFileList(mFunDevice, info);
    }


    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {
        List<FunFileData> files = new ArrayList<>();
        if (funDevice != null && mFunDevice != null && funDevice.getId() == mFunDevice.getId()) {
            for (H264_DVR_FILE_DATA data : datas) {
                FunFileData funFileData = new FunFileData(data, new OPCompressPic());
                files.add(funFileData);
            }

            if (files.size() == 0) {
                return;
            }else {
                mFileDownloadListAdapter = new FileDownloadListAdapter(this, mFunDevice, files);

            }

        }

    }

    @Override
    public void onRequestRecordListSuccess(List<FunDevRecordFile> files) {

    }

    @Override
    public void onRequestRecordListFailed(Integer errCode) {

    }

    @Override
    public void onDeviceLoginSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceGetConfigSuccess(FunDevice funDevice, String configName, int nSeq) {

    }

    @Override
    public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {

    }

    @Override
    public void onDeviceSetConfigFailed(FunDevice funDevice, String configName, Integer errCode) {

    }

    @Override
    public void onDeviceChangeInfoSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceChangeInfoFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceOptionSuccess(FunDevice funDevice, String option) {

    }

    @Override
    public void onDeviceOptionFailed(FunDevice funDevice, String option, Integer errCode) {

    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice) {

    }

    @Override
    public void onDeviceFileListGetFailed(FunDevice funDevice) {

    }
}
