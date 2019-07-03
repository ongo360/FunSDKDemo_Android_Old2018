package com.example.funsdkdemo.devices;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.G;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.adapter.DeviceCameraPicAdapter;
import com.example.funsdkdemo.adapter.DeviceCameraRecordAdapter;
import com.example.funsdkdemo.entity.DownloadInfo;
import com.lib.FunSDK;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceFileListener;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.OnFunDeviceRecordListener;
import com.lib.funsdk.support.config.OPCompressPic;
import com.lib.funsdk.support.models.FunDevRecordFile;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.funsdk.support.utils.Define;
import com.lib.funsdk.support.utils.MyUtils;
import com.lib.funsdk.support.widget.FunVideoView;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.sdk.struct.H264_DVR_FINDINFO;
import com.lib.sdk.struct.OPRemoveFileJP;
import com.lib.sdk.struct.SDK_SearchByTime;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Demo: 设备远程访问录像列表,并播放录像
 * 前置: 在上一个Activity-ActivityGuideDeviceCamera中已经登入设备,此Demo不需要再重复登入设备
 * 1. 注册录像文件搜索结果监听 - 在搜索完成后以回调的方式返回
 * 2. 按照时间(日期)获取远程设备的录像列表  - onSearchFile()
 * 3. 在回调中处理录像列表结果 - onRequestRecordListSuccess()
 * 4. 播放录像文件  - playRecordVideoByTime()
 * 5. 退出注销监听  - onDestroy()
 */

public class ActivityGuideDeviceRecordList extends ActivityDemo
        implements View.OnClickListener, OnFunDeviceRecordListener
        ,OnFunDeviceOptListener, OnItemClickListener, AdapterView.OnItemLongClickListener, OnPreparedListener
        , OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, OnErrorListener, OnFunDeviceFileListener{

    private FunDevice mFunDevice = null;
    private Calendar calendar;
    private boolean byFile = true;

    private DeviceCameraRecordAdapter mRecordByTimeAdapter;
    private DeviceCameraPicAdapter mRecordByFileAdapter;

    private TextView mTextTitle = null;
    private ImageButton mBtnBack = null;
    private ImageButton mBtnDateSelector = null;
    private ListView mRecordList = null;
    private RadioGroup rgWayToGetVideo = null;

    private FunVideoView mVideoView = null;

    private RelativeLayout mLayoutProgress = null;
    private TextView mTextCurrTime = null;
    private TextView mTextDuration = null;
    private SeekBar mSeekBar = null;

    private final int MESSAGE_REFRESH_PROGRESS = 0x100;
    private final int MESSAGE_SEEK_PROGRESS = 0x101;
    private final int MESSAGE_SET_IMAGE = 0x102;
    private int MaxProgress;
    private int mPosition;
    private OPRemoveFileJP mOPRemoveFileJP;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_record_list);

        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        boolean byFile = getIntent().getBooleanExtra("BY_FILE", true);
        FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
        if (devId==0) {
            funDevice = FunSupport.getInstance().mCurrDevice;
        }
        if (null == funDevice) {
            finish();
            return;
        } else {
            mFunDevice = funDevice;
        }

        calendar = Calendar.getInstance();

        mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mTextTitle.setText(sdf.format(calendar.getTime()));
        mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);
        mBtnDateSelector = (ImageButton) findViewById(R.id.ib_date_selector);
        mBtnDateSelector.setOnClickListener(this);
        mRecordList = (ListView) findViewById(R.id.lv_records);
        mRecordList.setOnItemClickListener(this);
        mRecordList.setOnItemLongClickListener(this);
        rgWayToGetVideo = (RadioGroup) findViewById(R.id.rg_way_to_get_video);
        rgWayToGetVideo.setOnCheckedChangeListener(this);

        mLayoutProgress = (RelativeLayout)findViewById(R.id.videoProgressArea);
        mTextCurrTime = (TextView)findViewById(R.id.videoProgressCurrentTime);
        mTextDuration = (TextView)findViewById(R.id.videoProgressDurationTime);
        mSeekBar = (SeekBar)findViewById(R.id.videoProgressSeekBar);
        mSeekBar.setOnSeekBarChangeListener(this);

        mVideoView = (FunVideoView)findViewById(R.id.funRecVideoView);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);

        // 1. 注册录像文件搜索结果监听 - 在搜索完成后以回调的方式返回
        FunSupport.getInstance().registerOnFunDeviceRecordListener(this);
        FunSupport.getInstance().registerOnFunDeviceOptListener(this);

        // 2. 按照时间(日期)获取远程设备的录像列表  - onSearchFile()
        //onSearchFile();
        ((RadioButton)findViewById(R.id.rb_by_file)).setChecked(true);
    }

    @Override
    protected void onDestroy() {

    	// 停止视频播放
    	if ( null != mVideoView ) {
    		mVideoView.stopPlayback();
    	}

    	// 5. 退出注销监听
    	FunSupport.getInstance().removeOnFunDeviceRecordListener(this);
        FunSupport.getInstance().removeOnFunDeviceOptListener(this);

    	if ( null != mHandler ) {
    		mHandler.removeCallbacksAndMessages(null);
    		mHandler = null;
    	}

        super.onDestroy();

    }

    private int MasktoInt(int channel){
    	int MaskofChannel = 0;
    	MaskofChannel = (1 << channel) | MaskofChannel;
    	System.out.println("TTTT-------maskofchannel = " + MaskofChannel);
    	return MaskofChannel;
    }

    private void onSearchFile() {
        showWaitDialog();

        int time[] = { calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE) };
        if (byFile) {
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
        } else {
            SDK_SearchByTime search_info = new SDK_SearchByTime();
            search_info.st_6_nHighStreamType = 0;
            search_info.st_7_nLowStreamType = 0;
            search_info.st_1_nLowChannel = MasktoInt(mFunDevice.CurrChannel);
            search_info.st_2_nFileType = 0;
            search_info.st_3_stBeginTime.st_0_year = time[0];
            search_info.st_3_stBeginTime.st_1_month = time[1];
            search_info.st_3_stBeginTime.st_2_day = time[2];
            search_info.st_3_stBeginTime.st_4_hour = 0;
            search_info.st_3_stBeginTime.st_5_minute = 0;
            search_info.st_3_stBeginTime.st_6_second = 0;
            search_info.st_4_stEndTime.st_0_year = time[0];
            search_info.st_4_stEndTime.st_1_month = time[1];
            search_info.st_4_stEndTime.st_2_day = time[2];
            search_info.st_4_stEndTime.st_4_hour = 23;
            search_info.st_4_stEndTime.st_5_minute = 59;
            search_info.st_4_stEndTime.st_6_second = 59;
            FunSupport.getInstance().requestDeviceFileListByTime(mFunDevice, search_info);


//        DevCmdOPTimeQueryJP cmdOPTimeQueryJP = new DevCmdOPTimeQueryJP();
//        cmdOPTimeQueryJP.setBeginTime(String.format(
//                "%04d-%02d-%02d %02d:%02d:%02d", time[0], time[1], time[2],
//                0, 0, 0));
//        cmdOPTimeQueryJP.setEndTime(String.format(
//                "%04d-%02d-%02d %02d:%02d:%02d", time[0], time[1], time[2],
//                23, 59, 59));
//        FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, cmdOPTimeQueryJP);
        }

    }

    private void playRecordVideoByTime(FunDevRecordFile recordFile) {

    	mVideoView.stopPlayback();

    	showWaitDialog();

		int[] time = { calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0 };
		// 只给定起始时间播放
//		int absTime = FunSDK.ToTimeType(time) + recordFile.recStartTime;
//    	mVideoView.playRecordByTime(mFunDevice.getDevSn(), absTime);

		// 给定起始时间和结束时间播放
		int fromTime = FunSDK.ToTimeType(time) + recordFile.recStartTime;
		int toTime = FunSDK.ToTimeType(time) + recordFile.recEndTime;
		mVideoView.playRecordByTime(mFunDevice.getDevSn(), fromTime, toTime, mFunDevice.CurrChannel);

        //打开声音
        mVideoView.setMediaSound(true);
    }

    private void playRecordVideoByFile(FunFileData recordFile) {
        mVideoView.stopPlayback();
        showWaitDialog();
        mVideoView.playRecordByFile(mFunDevice.getDevSn(), recordFile.getFileData(), mFunDevice.CurrChannel);
        mVideoView.setMediaSound(true);
    }

    private void seekRecordVideo(int progress) {
    	if ( null != mVideoView ) {
    		int seekPos = mVideoView.getStartTime()+progress;
    		if (byFile) {
				int seekposbyfile = (progress*100)/MaxProgress;
				mVideoView.seekbyfile(seekposbyfile);
			}else{
				mVideoView.seek(seekPos);
			}
    	}
    }

    private void refreshPlayInfo() {
//        if (byFile) {
//            mSeekBar.setEnabled(false);
//        } else {
//            mSeekBar.setEnabled(true);
//        }
        int startTm = mVideoView.getStartTime();
        int endTm = mVideoView.getEndTime();
        MaxProgress = endTm-startTm;
        Log.i("startTm","TTTT----" + startTm);
        Log.i("endTm","TTTT----" + endTm);
        Log.i("MaxProgress","TTT----" + MaxProgress);
        if (startTm > 0 && endTm > startTm) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            mTextCurrTime.setText(sdf.format(new Date((long) startTm * 1000)));
            mTextDuration.setText(sdf.format(new Date((long) endTm * 1000)));
            mSeekBar.setMax(endTm - startTm);
            mSeekBar.setProgress(0);
            mLayoutProgress.setVisibility(View.VISIBLE);

            resetProgressInterval();
        } else {
            mLayoutProgress.setVisibility(View.GONE);

            cleanProgressInterval();
        }
    }

    private void resetProgressInterval() {
    	if ( null != mHandler ) {
    		mHandler.removeMessages(MESSAGE_REFRESH_PROGRESS);
    		mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_PROGRESS, 500);
    	}
    }

    private void cleanProgressInterval() {
    	if ( null != mHandler ) {
    		mHandler.removeMessages(MESSAGE_REFRESH_PROGRESS);
    	}
    }

    private void refreshProgress() {
    	int posTm = mVideoView.getPosition();
    	if ( posTm > 0 ) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            mTextCurrTime.setText(sdf.format(new Date((long)posTm*1000)));
            Log.i("TTTT","TTTT----" + sdf.format(new Date((long)posTm*1000)));

    		mSeekBar.setProgress(posTm - mVideoView.getStartTime());
    	}
    }

    private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_REFRESH_PROGRESS:
				{
					refreshProgress();
					resetProgressInterval();
				}
				break;
			case MESSAGE_SEEK_PROGRESS:
				{
					seekRecordVideo(msg.arg1);
				}
				break;
			case MESSAGE_SET_IMAGE:
			{
				if (mRecordByFileAdapter != null) {
					mRecordByFileAdapter.setBitmapTempPath((String) msg.obj);
				}
			}
			break;
			}
		}

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtnInTopLayout: {
                // 返回/退出
                finish();
            }
            break;
            case R.id.ib_date_selector: {
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        mTextTitle.setText(sdf.format(calendar.getTime()));
                        onSearchFile();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
            break;
        }
    }

    @Override
    public void onRequestRecordListSuccess(List<FunDevRecordFile> files) {

        if (files == null || files.size() == 0) {
            showToast(R.string.device_camera_video_list_empty);
        }

        // 3. 在回调中处理录像列表结果 - onRequestRecordListSuccess()
        // 显示录像文件列表

        mRecordByTimeAdapter = new DeviceCameraRecordAdapter(this, files);
        mRecordList.setAdapter(mRecordByTimeAdapter);

        hideWaitDialog();

        // 如果录像存在,默认开始播放第一段录像
        if ( files.size() > 0 ) {
        	mRecordByTimeAdapter.setPlayingIndex(0);
        	playRecordVideoByTime(files.get(0));
        }
    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {

        List<FunFileData> files = new ArrayList<FunFileData>();

        if (null != funDevice
                && null != mFunDevice
                && funDevice.getId() == mFunDevice.getId()) {

            for (H264_DVR_FILE_DATA data : datas) {
                FunFileData funFileData = new FunFileData(data, new OPCompressPic());
                files.add(funFileData);
            }

            if (files.size() == 0) {
                showToast(R.string.device_camera_video_list_empty);
            } else {
                mRecordByFileAdapter = new DeviceCameraPicAdapter(this, mRecordList, mFunDevice, files);
                mRecordList.setAdapter(mRecordByFileAdapter);
                if (mRecordByFileAdapter != null) {
                    mRecordByFileAdapter.release();
                }
            }

            // 如果录像存在,默认开始播放第一段录像
            if ( files.size() > 0 ) {
                playRecordVideoByFile(files.get(0));
                mRecordByFileAdapter.setPlayingIndex(0);
            }
        }

        hideWaitDialog();
    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice) {
    }

    @Override
    public void onRequestRecordListFailed(Integer errCode) {
        hideWaitDialog();
        showToast(FunError.getErrorStr(errCode));
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        if (byFile) {
            if (null != mRecordByFileAdapter) {
                FunFileData recordFile = mRecordByFileAdapter.getRecordFile(position);
                if (null != recordFile) {
                    mRecordByFileAdapter.setPlayingIndex(position);
                    playRecordVideoByFile(recordFile);
                }
            }
        } else {
            if ( null != mRecordByTimeAdapter) {
                FunDevRecordFile recordFile = mRecordByTimeAdapter.getRecordFile(position);
                if (null != recordFile) {
                    mRecordByTimeAdapter.setPlayingIndex(position);
                    playRecordVideoByTime(recordFile);
                }
            }
        }

	}

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_by_file:
                byFile = true;
                break;
            case R.id.rb_by_time:
                byFile = false;
                break;
        }
        mVideoView.stopPlayback();
        onSearchFile();
    }

	@Override
	public void onPrepared(MediaPlayer mp) {
		hideWaitDialog();
		refreshPlayInfo();
		String path = mVideoView.captureImage(null);
		Message message = Message.obtain();
		message.what = MESSAGE_SET_IMAGE;
		message.obj = path;
		mHandler.sendMessageDelayed(message, 200);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
		if ( fromUser ) {
			if ( null != mHandler ) {
				mHandler.removeMessages(MESSAGE_SEEK_PROGRESS);
				Message msg = new Message();
				msg.what = MESSAGE_SEEK_PROGRESS;
				msg.arg1 = progress;
				mHandler.sendMessageDelayed(msg, 300);
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

    @Override
    public void onDeviceLoginSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceGetConfigSuccess(FunDevice funDevice, String configName, int nSeq) {
    	onSearchFile();
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
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		// 播放失败
        hideWaitDialog();
        showToast(getResources().getString(R.string.media_play_error)
						+ " : "
						+ FunError.getErrorStr(extra));
		return true;
	}

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (byFile) {
        	mPosition = position;
        	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        	final String[] items = {"Download By File","Delete","Download By Time"};
        	dialog.setItems(items, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (which == 0) {
						dialog.dismiss();
						new AlertDialog.Builder(ActivityGuideDeviceRecordList.this)
	                    .setTitle("Download ?")
	                    .setNeutralButton("Cancel", null)
	                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            FunFileData recordFile = mRecordByFileAdapter.getRecordFile(position);
	                            byte[] data = null;
	                            if (recordFile != null) {
	                                data = G.ObjToBytes(recordFile.getFileData());
	                            }
	                            String path = FunPath.PATH_VIDEO + File.separator +recordFile.getBeginTimeStr() + ".mp4";
	                            File file = new File(path);
	                            if (file.exists())
	                            {
	                                Toast.makeText(ActivityGuideDeviceRecordList.this, "文件已存在", Toast.LENGTH_SHORT).show();
	                                return;
	                            }
	                            FunSupport.getInstance().requestDeviceDownloadByFile(mFunDevice, data, path, position);
	                        }
	                    }).show();
					}else if (which == 1){
						if ( null != mVideoView ) {
				    		mVideoView.stopPlayback();
				    	}
						if (null == mOPRemoveFileJP) {
							mOPRemoveFileJP = new OPRemoveFileJP();
						}
						FunFileData recordFile = mRecordByFileAdapter.getRecordFile(position);
						H264_DVR_FILE_DATA data = null;
                        if (recordFile != null) {
                            data = recordFile.getFileData();
                        }
						mOPRemoveFileJP.setFileNameInfo(position, data.getStreamType(), 0,
								data.getFileName());
						// TODO Auto-generated method stub
						FunSupport.getInstance().requestDeviceRemoveFile(mFunDevice, mOPRemoveFileJP);
					}else if (which == 2){
						FunFileData recordFile = mRecordByFileAdapter.getRecordFile(position);

						Date timeBegin = recordFile.getBeginDate();
						Calendar timeDateBegin = Calendar.getInstance();
						timeDateBegin.setTime(timeBegin);

						Date timeEnd = recordFile.getEndDate();
						Calendar timeDateEnd = Calendar.getInstance();
						timeDateEnd.setTime(timeEnd);
						downLoadVideoByTime(timeDateBegin,timeDateEnd);
					}
				}
			});
            dialog.show();

        }
        return true;
    }

	//下载时间段只需要改开始时间和结束时间即可(下面只写了下载当前时间前一小时的一分钟录像)
	private void downLoadVideoByTime(Calendar timeBegin,Calendar timeEnd){
		Calendar time = Calendar.getInstance();
		H264_DVR_FINDINFO findInfo = new H264_DVR_FINDINFO();
		findInfo.st_0_nChannelN0 = mFunDevice.CurrChannel;
		findInfo.st_1_nFileType = 0;

		findInfo.st_2_startTime.st_0_dwYear = timeBegin.get(Calendar.YEAR);
		findInfo.st_2_startTime.st_1_dwMonth = timeBegin.get(Calendar.MONTH)+1;
		findInfo.st_2_startTime.st_2_dwDay =  timeBegin.get(Calendar.DAY_OF_MONTH);
		findInfo.st_2_startTime.st_3_dwHour = timeBegin.get(Calendar.HOUR);
		findInfo.st_2_startTime.st_4_dwMinute = timeBegin.get(Calendar.MINUTE);
		findInfo.st_2_startTime.st_5_dwSecond = timeBegin.get(Calendar.SECOND);

		findInfo.st_3_endTime.st_0_dwYear = timeEnd.get(Calendar.YEAR);
		findInfo.st_3_endTime.st_1_dwMonth = timeEnd.get(Calendar.MONTH)+1;
		findInfo.st_3_endTime.st_2_dwDay = timeEnd.get(Calendar.DAY_OF_MONTH);
		findInfo.st_3_endTime.st_3_dwHour = timeEnd.get(Calendar.HOUR);
		findInfo.st_3_endTime.st_4_dwMinute = timeEnd.get(Calendar.MINUTE);
		findInfo.st_3_endTime.st_5_dwSecond = timeEnd.get(Calendar.SECOND);

		DownloadInfo<H264_DVR_FINDINFO> info = new DownloadInfo<H264_DVR_FINDINFO>(
				-1, mFunDevice.devSn, findInfo);
		Object obj = info.getObj();
		//下载的录像文件保存路径
		String tempName = MyUtils.getDownloadFileNameByInfo(
				Define.TEMP_DOWNLOAD_FILE_PREFIX + "_n-" + info.getSn(),
				(H264_DVR_FINDINFO) obj);
		info.setFileName(tempName);
		FunSupport.getInstance().requestDeviceDownloadByTime(info);
	}
    @Override
    public void onDeviceFileDownCompleted(FunDevice funDevice, String path, int nSeq) {
		if (path != null){
        	Toast.makeText(this, "Download Complete!!!" + "Path:" + path, Toast.LENGTH_LONG).show();
    	}else{
			Toast.makeText(this, "Download Complete!!!" + "Path:" + FunPath.PATH_VIDEO, Toast.LENGTH_LONG).show();
		}
	}

    @Override
    public void onDeviceFileDownProgress(int totalSize, int progress, int nSeq) {

    }

    @Override
    public void onDeviceFileDownStart(boolean isStartSuccess, int nSeq) {
		if (isStartSuccess){
			Toast.makeText(this, "Download Start!!!" , Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "Download Faile!!!" , Toast.LENGTH_LONG).show();
    	}
	}
}