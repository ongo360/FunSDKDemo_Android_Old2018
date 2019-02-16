package com.example.funsdkdemo.devices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.adapter.GridAdapterDevicePicture;
import com.example.funsdkdemo.adapter.GridAdapterDevicePicture.OnGridDevicePictureListener;
import com.lib.SDKCONST;
import com.lib.SDKCONST.SDK_File_Type;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.DevCmdOPRemoveFileJP;
import com.lib.funsdk.support.config.DevCmdOPSCalendar;
import com.lib.funsdk.support.config.DevCmdSearchFileNumJP;
import com.lib.funsdk.support.config.SameDayPicInfo;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.sdk.struct.H264_DVR_FINDINFO;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivityGuideDeviceSportPicList extends ActivityDemo implements OnFunDeviceOptListener,
        OnClickListener, OnGridDevicePictureListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private FunDevice mFunDevice = null;
    private GridAdapterDevicePicture mGridAdapter = null;
    private StickyGridHeadersGridView mGridView = null;
    private TextView mTextTitle = null;
    private ImageButton mBtnBack = null;
    private LinearLayout mOptLayout = null;
    private Button mOptDelete = null;
    private Button mOptCancel = null;

    private String mFileType = null;

    // 在文件个数达到足够多的时候再显示出来,提高用户体验效果和尽量避免不必要的数据请求
    private boolean mFileNumInited = false;
    private final int MIN_FILE_NUM = 30;	// 至少获取到30个文件,显示满一屏幕(这个值是可以动态计算的,Demo中暂定30个)

    private List<Integer> mNeedSearchFileDays = new ArrayList<Integer>();
    private boolean mIsSearchingFile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_sport_pic_list);

        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
        if ( null == funDevice ) {
            finish();
            return;
        } else {
            mFunDevice = funDevice;
        }

        FunPath.onCreateSptTempPath(mFunDevice.getSerialNo());

        mFileType = getIntent().getStringExtra("FILE_TYPE");
        if ( null == mFileType ) {
            mFileType = "jpg";
        }

        mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
        mTextTitle.setText(mFunDevice.devName);
        mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);
        mOptLayout = (LinearLayout) findViewById(R.id.optionLayout);
        mOptDelete = (Button) findViewById(R.id.optionDelete);
        mOptDelete.setOnClickListener(this);
        mOptCancel = (Button) findViewById(R.id.optionCancel);
        mOptCancel.setOnClickListener(this);
        mGridView = (StickyGridHeadersGridView)findViewById(R.id.gridViewDevicePicture);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        mGridAdapter = new GridAdapterDevicePicture(this, mGridView, mFunDevice);
        mGridAdapter.setOnGridDevicePictureListener(this);

        //mGridView.setAdapter(mGridAdapter);

        FunSupport.getInstance().registerOnFunDeviceOptListener(this);

        tryGetCalendar();
    }

    @Override
    protected void onDestroy() {

        // 注销监听
        FunSupport.getInstance().removeOnFunDeviceOptListener(this);

        // 是否Adapter中使用的资源
        if ( null != mGridAdapter ) {
            mGridAdapter.release();
        }

        mFunDevice.invalidConfig(DevCmdOPSCalendar.CONFIG_NAME);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtnInTopLayout:
            {
                // 返回/退出
                finish();
            }
            break;
            case R.id.optionDelete:
                tryRemoveFiles();
                break;
            case R.id.optionCancel:
                cancelChoiceMode();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mGridAdapter.isSelMode) {
            mGridAdapter.select(position);
            mGridAdapter.notifyDataSetChanged();
        } else {
            int type;
            FunFileData imageInfo = getFileData(position);

            if (imageInfo != null) {
                type = imageInfo.getFileType();
            } else {
                type = 0;
            }

            Intent it = null;
            if (type == SDKCONST.PicFileType.PIC_BURST_SHOOT
                    || type == SDKCONST.PicFileType.PIC_TIME_LAPSE) {
                it = new Intent(this, ActivityGuideDevicePicBrowser.class);
            } else {
                it = new Intent(this, ActivityGuideDeviceNormalPic.class);
            }
            it.putExtra("fromSportCamera", true);
            it.putExtra("position", position);
            it.putExtra("FUN_DEVICE_ID", mFunDevice.getId());
            startActivity(it);
        }

    }

    private FunFileData getFileData(int position) {
        int pos = position;
        DevCmdOPSCalendar opsCalendar = (DevCmdOPSCalendar)mFunDevice.checkConfig(DevCmdOPSCalendar.CONFIG_NAME);
        for ( SameDayPicInfo picInfo : opsCalendar.getData() ) {
            if ( pos >= 0 && pos < picInfo.getPicNum() ) {
                return picInfo.getPicData(pos);
            } else if ( pos >= picInfo.getPicNum() ) {
                pos -= picInfo.getPicNum();
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mGridAdapter.select(position);
        enterChoiceMode();
        return true;
    }

    private void enterChoiceMode() {
        mGridAdapter.isSelMode = true;
        mTextTitle.setVisibility(View.GONE);
        mOptLayout.setVisibility(View.VISIBLE);
        mGridAdapter.notifyDataSetChanged();
    }

    private void cancelChoiceMode(){
        mGridAdapter.isSelMode = false;
        mTextTitle.setVisibility(View.VISIBLE);
        mOptLayout.setVisibility(View.GONE);
        mGridAdapter.notifyDataSetChanged();
    }

    private void tryGetCalendar() {
        showWaitDialog();
        Calendar time = Calendar.getInstance();
        DevCmdOPSCalendar cmdCalendar = (DevCmdOPSCalendar)mFunDevice.checkConfig(DevCmdOPSCalendar.CONFIG_NAME);

        cmdCalendar.setEvent("*");
        cmdCalendar.setFileType(mFileType);
        cmdCalendar.setYear(time.get(Calendar.YEAR));
        FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, cmdCalendar);
    }

    private void tryGetFileNum(int position) {
        DevCmdOPSCalendar cmdCalendar = (DevCmdOPSCalendar)mFunDevice.checkConfig(DevCmdOPSCalendar.CONFIG_NAME);
        if ( position >= 0 && position < cmdCalendar.getData().size() ) {
            SameDayPicInfo picInfo = cmdCalendar.getData().get(position);

            if ( picInfo.hasRequestFileNum() ) {
                return;
            }

            picInfo.setRequestFileNum(true);

            DevCmdSearchFileNumJP cmdFileNum = (DevCmdSearchFileNumJP)mFunDevice.checkConfig(DevCmdSearchFileNumJP.CONFIG_NAME);

            cmdFileNum.setEvent("*");
            picInfo.setDayTime(0, 0, 0);
            cmdFileNum.setBeginTime(picInfo.getTime().getTime());
            picInfo.setDayTime(23, 59, 59);
            cmdFileNum.setEndTime(picInfo.getTime().getTime());
            cmdFileNum.setFileType(mFileType);

            FunSupport.getInstance().requestDeviceFileNum(mFunDevice, cmdFileNum, position);
        }

    }

    private void trySearchFile() {
        SameDayPicInfo toSearchPicInfo = null;
        FunFileData lastSearchedFile = null;
        DevCmdOPSCalendar cmdCalendar = (DevCmdOPSCalendar)mFunDevice.checkConfig(DevCmdOPSCalendar.CONFIG_NAME);
        synchronized (mNeedSearchFileDays) {
            if ( !mIsSearchingFile ) {
                for ( int i = 0; i < mNeedSearchFileDays.size();i ++ ) {
                    Integer dayIndex = mNeedSearchFileDays.get(i);
                    boolean allSearched = true;
                    lastSearchedFile = null;
                    if ( dayIndex >= 0 && dayIndex < cmdCalendar.getData().size() ) {
                        SameDayPicInfo picInfo = cmdCalendar.getData().get(dayIndex);
                        // 判断是否所有的图片都已经搜索了
                        if ( picInfo.getPicNum() > 0 ) {
                            for ( FunFileData fileData : picInfo.getAllPicData() ) {
                                if ( !fileData.hasSeachedFile() ) {
                                    allSearched = false;
                                    mIsSearchingFile = true;
                                    toSearchPicInfo = picInfo;
                                    break;
                                } else {
                                    lastSearchedFile = fileData;
                                }
                            }
                        }
                    }

                    if ( !allSearched ) {
                        break;
                    }
                }
            }

        }


        if ( null != toSearchPicInfo ) {
            Date searchDate = null;
            boolean searchNewest = false;
            if ( null != lastSearchedFile ) {
                searchDate = new Date(lastSearchedFile.getBeginDate().getTime()-1000);
                searchNewest = false;
            } else {
                searchDate = toSearchPicInfo.getDate();
                searchNewest = true;
            }
            onSearchPicture(searchDate, 0, searchNewest);
        }
    }

    private void onSearchPicture(Date date, int channel, boolean searchAll) {
        H264_DVR_FINDINFO info = new H264_DVR_FINDINFO();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        info.st_1_nFileType = SDK_File_Type.SDK_PIC_ALL;
        info.st_2_startTime.st_0_dwYear = c.get(Calendar.YEAR);
        info.st_2_startTime.st_1_dwMonth = c.get(Calendar.MONTH) + 1;
        info.st_2_startTime.st_2_dwDay = c.get(Calendar.DATE);
        info.st_2_startTime.st_3_dwHour = 0;
        info.st_2_startTime.st_4_dwMinute = 0;
        info.st_2_startTime.st_5_dwSecond = 0;
        info.st_3_endTime.st_0_dwYear = c.get(Calendar.YEAR);
        info.st_3_endTime.st_1_dwMonth = c.get(Calendar.MONTH) + 1;
        info.st_3_endTime.st_2_dwDay = c.get(Calendar.DATE);
        if (searchAll) {
            info.st_3_endTime.st_3_dwHour = 23;
            info.st_3_endTime.st_4_dwMinute = 59;
            info.st_3_endTime.st_5_dwSecond = 59;
        } else {
            info.st_3_endTime.st_3_dwHour = c.get(Calendar.HOUR_OF_DAY);
            info.st_3_endTime.st_4_dwMinute = c.get(Calendar.MINUTE);
            info.st_3_endTime.st_5_dwSecond = c.get(Calendar.SECOND);
        }
        info.st_0_nChannelN0 = channel;

        //FunLog.i("test", "search file : searchAll = " + searchAll + " info = " + info.toString());
        //此处不可跨天获取
        FunSupport.getInstance().requestDeviceFileList(mFunDevice, info);
    }

    private void tryRemoveFiles() {
        showWaitDialog();
        DevCmdOPRemoveFileJP cmdOPRemoveFileJP = mGridAdapter.getRemovingFiles();
        FunSupport.getInstance().requestDeviceRemovePicture(mFunDevice, cmdOPRemoveFileJP);
    }

    private void notifyDataSetChanged() {
        if ( null != mGridAdapter ) {
            mGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeviceLoginSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceGetConfigSuccess(final FunDevice funDevice, final String configName, final int nSeq) {
        if ( configName.equals(DevCmdOPSCalendar.CONFIG_NAME) ) {
            DevCmdOPSCalendar cmdCalendar = (DevCmdOPSCalendar)mFunDevice.checkConfig(DevCmdOPSCalendar.CONFIG_NAME);
            if (cmdCalendar.getData().size() == 0) {
                hideWaitDialog();
            }
            // 立刻获取第一(天)的文件个数
            mFileNumInited = false;
            tryGetFileNum(0);
        } else if ( configName.equals(DevCmdSearchFileNumJP.CONFIG_NAME) ) {
            DevCmdOPSCalendar cmdCalendar = (DevCmdOPSCalendar)mFunDevice.getConfig(DevCmdOPSCalendar.CONFIG_NAME);
            DevCmdSearchFileNumJP cmdFileNum = (DevCmdSearchFileNumJP)mFunDevice.getConfig(DevCmdSearchFileNumJP.CONFIG_NAME);
            if ( null != cmdCalendar
                    && null != cmdFileNum
                    && nSeq >= 0 && nSeq < cmdCalendar.getData().size()) {
                cmdCalendar.getData().get(nSeq).setPicNum(cmdFileNum.getFileNum());

                int totFileCount = 0;
                for ( SameDayPicInfo picInfo : cmdCalendar.getData() ) {
                    totFileCount += picInfo.getPicNum();
                }

                if ( !mFileNumInited ) {
                    if ( totFileCount >= MIN_FILE_NUM
                            || nSeq >= cmdCalendar.getData().size()-1 ) {
                        if ( null != mGridView && null != mGridAdapter ) {
                            mGridView.setAdapter(mGridAdapter);
                        }
                        mFileNumInited = true;
                        hideWaitDialog();
                    } else {
                        // 继续获取更多的文件个数
                        tryGetFileNum(nSeq+1);
                        return;
                    }
                }

            }

            notifyDataSetChanged();
        } else if (configName.equals(DevCmdOPRemoveFileJP.CONFIG_NAME)) {
            hideWaitDialog();
            Toast.makeText(this, R.string.device_sport_camera_remove_pic_sucess, Toast.LENGTH_LONG)
                    .show();
            cancelChoiceMode();
        }
    }

    @Override
    public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceSetConfigSuccess(final FunDevice funDevice, 
    		final String configName) {

    }

    @Override
    public void onDeviceSetConfigFailed(final FunDevice funDevice, 
    		final String configName, final Integer errCode) {

    }

    @Override
	public void onDeviceChangeInfoSuccess(final FunDevice funDevice) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDeviceChangeInfoFailed(final FunDevice funDevice, final Integer errCode) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDeviceOptionSuccess(final FunDevice funDevice, final String option) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDeviceOptionFailed(final FunDevice funDevice, final String option, final Integer errCode) {
		// TODO Auto-generated method stub
	}
	
    @Override
    public void onDeviceFileListChanged(FunDevice funDevice) {
        if ( null != funDevice
                && null != mFunDevice
                && funDevice.getId() == mFunDevice.getId() ) {

            synchronized (mNeedSearchFileDays) {
                mIsSearchingFile = false;
            }

            trySearchFile();

            notifyDataSetChanged();
        }
    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {

    }

    @Override
    public void onGridDevicePictureDayDisplay(int position,
                                              SameDayPicInfo picInfo) {
        tryGetFileNum(position);
    }

    @Override
    public void onGridDevicePictureItemDisplay(List<Integer> dayIds) {
        synchronized (mNeedSearchFileDays) {
            mNeedSearchFileDays.clear();
            mNeedSearchFileDays.addAll(dayIds);
        }

        trySearchFile();
    }

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}



}
