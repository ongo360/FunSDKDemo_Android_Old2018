package com.example.funsdkdemo.devices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.*;

import com.basic.G;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceFileListener;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.*;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Demo: 打开图片原图
 */
public class ActivityGuideDevicePicBrowser extends ActivityDemo implements OnFunDeviceOptListener, View.OnClickListener, View.OnLongClickListener, OnFunDeviceFileListener {

    private final int PLAY_PICTURE = 1;

    private FunDevice mFunDevice = null;
    private int mPosition = 0;
    private boolean isFromSportCamera = false;
    private int mPicType = SDKCONST.PicFileType.PIC_BURST_SHOOT;
    private FunFileData mInfo = null;
    private List<FunFileData> mDatas = null;
    private int mIndex = 0;
    private boolean isPlay = false;


    private TextView mTextTitle = null;
    private ImageButton mBtnBack = null;
    private ImageView mImageBrowser = null;
    private ImageView mBtnPlayStop = null;
    private ImageView mBtnReplay = null;
    private ProgressBar mProgressBar = null;
    private TextView mProgressText = null;
    private Button mBtnJPEGToMp4 = null;

    private LruCache<String, Bitmap> mLruCache;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_pic_browser);


        FunSupport.getInstance().registerOnFunDeviceOptListener(this);

        initLayout();
        initData();
    }

    @Override
    protected void onDestroy() {
        FunSupport.getInstance().removeOnFunDeviceOptListener(this);
        super.onDestroy();
    }

    private void initLayout() {
        mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);
        mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);
        mImageBrowser = (ImageView) findViewById(R.id.img_pic_browser);
        mImageBrowser.setOnClickListener(this);
        mImageBrowser.setOnLongClickListener(this);
        mBtnPlayStop = (ImageView) findViewById(R.id.iv_play_stop);
        mBtnPlayStop.setOnClickListener(this);
        mBtnReplay = (ImageView) findViewById(R.id.iv_play_replay);
        mBtnReplay.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressText = (TextView) findViewById(R.id.progress_text);
        mBtnJPEGToMp4 = (Button) findViewById(R.id.btn_jpeg_to_mp4);
        mBtnJPEGToMp4.setOnClickListener(this);
    }

    private void initData() {
        //showWaitDialog();
        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        mFunDevice = FunSupport.getInstance().findDeviceById(devId);
        mPosition = getIntent().getIntExtra("position", 0);
        isFromSportCamera = getIntent().getBooleanExtra("fromSportCamera", false);
        mDatas = new ArrayList<FunFileData>();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 设置图片缓存大小为maxMemory的1/3
        int cacheSize = maxMemory / 3;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };

        if (isFromSportCamera) {
            mInfo = getFileData(mPosition);
        } else {
            mInfo = mFunDevice.mDatas.get(mPosition);
        }


        mPicType = mInfo.getFileType();
        if (mPicType == SDKCONST.PicFileType.PIC_BURST_SHOOT) {
            mTextTitle.setText(R.string.device_sport_camera_pic_burst_shoot);
        } else if (mPicType == SDKCONST.PicFileType.PIC_TIME_LAPSE) {
            mTextTitle.setText(R.string.device_sport_camera_pic_time_lapse);
        }

        //onSearchFileNum(mPosition);
        onSearchPicture(mInfo.getFileName(),
                mInfo.getBeginTimeStr(0, 0, 0),
                mInfo.getEndTimeStr(23, 59, 59), 0);
        showWaitDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtnInTopLayout:
                finish();
                break;
            case R.id.img_pic_browser:
                if (mIndex >= mDatas.size() - 1) {
                    break;
                }
            case R.id.iv_play_stop:
                isPlay = !isPlay;
                playStopPic();
                break;
            case R.id.iv_play_replay:
                preparePlayPic();
                break;
            case R.id.btn_jpeg_to_mp4:
                //TODO 实现图片转成 mp4 并下载
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        //TODO 实现长按打开高清图
        isPlay = false;
        playStopPic();
        Intent it = new Intent(ActivityGuideDevicePicBrowser.this,
                ActivityGuideDeviceNormalPic.class);
        it.putExtra("preview", true);
        int position = mIndex >= mDatas.size() - 1 ? mDatas.size() - 1: mIndex;
        it.putExtra("position", position);
        it.putExtra("FUN_DEVICE_ID", mFunDevice.getId());
        startActivity(it);
        return true;
    }

    /*private void onSearchFileNum(int position) {

        DevCmdSearchFileNumJP cmdSearchFileNumJP = (DevCmdSearchFileNumJP)
                mFunDevice.checkConfig(DevCmdSearchFileNumJP.CONFIG_NAME);

        cmdSearchFileNumJP.setSearchPath(mInfo.getFileName());
        cmdSearchFileNumJP.setBeginTime(mInfo.getStartTimeOfYear());
        cmdSearchFileNumJP.setEndTime(mInfo.getEndTimeOfYear());
        cmdSearchFileNumJP.setEvent("*");
        cmdSearchFileNumJP.setFileType("jpg");
        cmdSearchFileNumJP.setmStreamType("0x00000002");
        FunSupport.getInstance().requestDeviceFileNum(mFunDevice, cmdSearchFileNumJP, position);
    }*/


    private void onSearchPicture(String fileName, String beginTime,
                                 String endTime, int fileFlag) {

        DevCmdOPFileQueryJP cmdOPFileQueryJP = (DevCmdOPFileQueryJP)
                mFunDevice.checkConfig(DevCmdOPFileQueryJP.CONFIG_NAME);

        cmdOPFileQueryJP.setSearchPath(fileName);
        cmdOPFileQueryJP.setBeginTime(beginTime);
        cmdOPFileQueryJP.setEndTime(endTime);
        cmdOPFileQueryJP.setFileNum(64);
        cmdOPFileQueryJP.setStreamType("0x00000002");
        cmdOPFileQueryJP.setType("jpg");
        cmdOPFileQueryJP.setEvent("*");

        FunSupport.getInstance().requestDeviceSearchPicByPath(mFunDevice, cmdOPFileQueryJP, fileFlag);
    }



    private void prepareAllBitmap(boolean toDownload) {
        for (FunFileData funFileData : mDatas) {
            H264_DVR_FILE_DATA info = funFileData.getFileData();
            if (null == info || info.st_3_beginTime.st_0_year == 0) {
                return;
            }

            String fileName = "";
            String fileName_thumb = FunPath.getSptTempPath()
                    + File.separator
                    + FunPath.getDownloadFileNameByData(info, 1, true);
            String fileName_or = FunPath.getSptTempPath()
                    + File.separator
                    + FunPath.getDownloadFileNameByData(info, 1, false);
            final long fileSize_thumb = FunPath.isFileExists(fileName_thumb);
            final long fileSize_or = FunPath.isFileExists(fileName_or);
            fileName = fileSize_thumb > 0 ? fileName_thumb
                    : (fileSize_or > 0 ? fileName_or : fileName_thumb);
            if (fileSize_thumb > 0 || fileSize_or > 0) {
                Bitmap bitmap = getBitmapFromLruCache(funFileData.getFileName());
                if (null == bitmap) {
                    bitmap = dealBitmap(fileName);
                }

                if (null != bitmap) {
                    addBitmapToLruCache(funFileData.getFileName(), bitmap);
                } else {
                    FunPath.deleteFile(fileName);
                }
            } else if (toDownload) {
                OPCompressPic opCompressPic = funFileData.getOPCompressPic();
                if (null == opCompressPic) {
                    return;
                }

                opCompressPic
                        .setPicName(G.ToString(info.st_2_fileName));
                FunSupport.getInstance().requestDeviceSearchPicture(mFunDevice,
                        opCompressPic, fileName_thumb, 0);
            }
        }
        if (toDownload) {
            prepareAllBitmap(false);
        } else {
            preparePlayPic();
            hideWaitDialog();
        }
    }

    private Bitmap dealBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; // 设置了此属性一定要记得将值设置为false
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap == null)
            return null;
        Bitmap newBtimap = Bitmap.createScaledBitmap(bitmap, 160, 90, true);
        bitmap.recycle();
        bitmap = null;
        return newBtimap;
    }

    /**
     * 为ImageView设置图片(Image) 1 从缓存中获取图片 2 若图片不在缓存中则为其设置默认图片
     */
    private void setImage(String imagePath) {
        if ( null != imagePath && imagePath.length() > 0 ) {
            mImageBrowser.setTag(imagePath);
            Bitmap bitmap = getBitmapFromLruCache(imagePath);
            if (bitmap != null) {
                mImageBrowser.setImageBitmap(bitmap);
            } else {
                mImageBrowser.setImageResource(R.drawable.icon_default_image);
            }
        } else {
            mImageBrowser.setTag(null);
            mImageBrowser.setImageResource(R.drawable.icon_default_image);
        }
    }

    /**
     * 将图片存储到LruCache
     */
    public void addBitmapToLruCache(String key, Bitmap bitmap) {
        synchronized (mLruCache) {
            if (getBitmapFromLruCache(key) == null && bitmap != null) {
                mLruCache.put(key, bitmap);
            }
        }
    }

    /**
     * 从LruCache缓存获取图片
     */
    public Bitmap getBitmapFromLruCache(String key) {
        if ( null == mLruCache ) {
            return null;
        }

        return mLruCache.get(key);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case PLAY_PICTURE:
                {
                    if (isPlay) {
                        mIndex++;
                        if (mIndex >= mDatas.size()) {
                            isPlay = false;
                            mHandler.removeMessages(PLAY_PICTURE);
                            mBtnPlayStop.setVisibility(View.GONE);
                            mBtnReplay.setVisibility(View.VISIBLE);
                        } else {
                            mProgressBar.setProgress(mIndex + 1);
                            mProgressText.setText(mProgressBar.getProgress() + "/" + mProgressBar.getMax());
                            setImage(mDatas.get(mIndex).getFileName());
                            mHandler.sendEmptyMessageDelayed(PLAY_PICTURE, 1000);
                        }
                    }
                }
                break;
            }
        }

    };

    public void preparePlayPic() {
        mIndex = 0;
        isPlay = false;
        mProgressBar.setMax(mDatas.size());
        mProgressBar.setProgress(mIndex + 1);
        mProgressText.setText(mProgressBar.getProgress() + "/" + mProgressBar.getMax());
        setImage(mDatas.get(mIndex).getFileName());
        mBtnPlayStop.setVisibility(View.VISIBLE);
        mBtnReplay.setVisibility(View.GONE);
    }

    public void playStopPic() {
        if (mDatas == null) {
            return;
        } else {
            if (mIndex < mDatas.size()) {
                if (isPlay) {
                    mBtnPlayStop.setVisibility(View.GONE);
                    mHandler.sendEmptyMessageDelayed(PLAY_PICTURE, 500);
                } else {
                    mHandler.removeMessages(PLAY_PICTURE);
                    mBtnPlayStop.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private FunFileData getFileData(int position) {
        int pos = position;
        DevCmdOPSCalendar opsCalendar = (DevCmdOPSCalendar) mFunDevice.checkConfig(DevCmdOPSCalendar.CONFIG_NAME);
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
    public void onDeviceLoginSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceGetConfigSuccess(FunDevice funDevice, String configName, int nSeq) {
        if (DevCmdSearchFileNumJP.CONFIG_NAME.equals(configName)) {

           /* for (int i = 0; i < mFileNumJP.getFileNum(); i++) {
                mDatas.add(i, new FunFileData(new H264_DVR_FILE_DATA(), new OPCompressPic()));
            }
            onSearchPicture(mInfo.getFileName(),
                    mInfo.getBeginTimeStr(0, 0, 0),
                    mInfo.getEndTimeStr(23, 59, 59), 0);
            mFileQueryJP.setFileData(mDatas);*/

        }else if (DevCmdOPFileQueryJP.CONFIG_NAME.equals(configName)) {
            DevCmdOPFileQueryJP cmdOPFileQueryJP = (DevCmdOPFileQueryJP)
                    mFunDevice.getConfig(DevCmdOPFileQueryJP.CONFIG_NAME);

            int tempNum = 64;
            if (cmdOPFileQueryJP.getFileNum() > tempNum) {
                FunFileData funData = mDatas.get(tempNum - 1);
                Date date = new Date(funData.getBeginDate().getTime() + 1000);
                String beginTime = String.format(
                        "%04d-%02d-%02d %02d:%02d:%02d",
                        date.getYear() + 1900, date.getMonth() + 1,
                        date.getDate(), date.getHours(),
                        date.getMinutes(), date.getSeconds());
                String endTime = funData.getFileData().st_4_endTime.getTime();
                onSearchPicture(funData.getFileName(), beginTime, endTime,
                        tempNum);
            } else {
                mDatas = cmdOPFileQueryJP.getFileData();
                prepareAllBitmap(true);
            }
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

    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {

    }

    @Override
    public void onDeviceFileDownCompleted(FunDevice funDevice, String path, int nSeq) {

    }

    @Override
    public void onDeviceFileDownProgress(int totalSize, int progress, int nSeq) {

    }

    @Override
    public void onDeviceFileDownStart(boolean isStartSuccess, int nSeq) {

    }

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}
}