package com.example.funsdkdemo.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.EFUN_ATTR;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.cloud.CloudDirectory;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.bean.cloudmedia.CloudMediaFileInfoBean;
import com.lib.sdk.bean.cloudmedia.CloudMediaFilesBean;
import com.video.opengl.GLSurfaceView20;

import java.util.Calendar;

/**
 * @author Administrator
 * @name FunSDKDemo_Android_Old2018
 * @class name：com.example.funsdkdemo.cloud
 * @class describe
 * @time 2019-06-25 10:09
 * @change
 * @chang time
 * @class describe
 */
public class ActivityDevCloudPlayBack extends ActivityDemo implements IFunSDKResult{
    private FrameLayout funVideoView;
    private Calendar calendar;
    private FunDevice funDevice;
    private int userId;
    private CloudMediaFilesBean cloudMediaFiles;
    private int playHandle;
    private RecyclerView rcFileTime;
    private FileTimeAdapter adapter;
    private SurfaceView playSurfaceView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_playback);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.backBtnInTopLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        funVideoView = findViewById(R.id.funVideoView);
        rcFileTime = findViewById(R.id.rc_file_time);

        rcFileTime.setLayoutManager(new LinearLayoutManager(this));

        createSurfaceView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        userId = FunSDK.GetId(userId,this);


        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,intent.getIntExtra("year",0));
        calendar.set(Calendar.MONTH,intent.getIntExtra("month",0));
        calendar.set(Calendar.DATE,intent.getIntExtra("day",0));

        cloudMediaFiles = new CloudMediaFilesBean(calendar);

        int devId = intent.getIntExtra("FUN_DEVICE_ID",0);

        funDevice = FunSupport.getInstance().findDeviceById(devId);

        adapter = new FileTimeAdapter();
        rcFileTime.setAdapter(adapter);

        searchFileByTime();
    }

    private void createSurfaceView() {
        if (playSurfaceView == null) {
            playSurfaceView = new GLSurfaceView20(this);
        }

        funVideoView.removeAllViews();
        funVideoView.addView(playSurfaceView);
        return;
    }

    private void searchFileByTime() {
        showWaitDialog();

        int begin[] = { calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE), 0, 0, 0 };
        int end[] = { calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE), 23, 59, 59 };
        CloudDirectory.SearchMediaByTime(userId, funDevice.getDevSn(),0, "", FunSDK.ToTimeType(begin),
                FunSDK.ToTimeType(end),0);
    }

    private int seekToTime(int times) {

        int[] time = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0};
        int absTime = FunSDK.ToTimeType(time) + times;

        if (playHandle == 0) {
            Calendar calendar = cloudMediaFiles.getSearchCalendar();
            int[] endTime = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59};
            int endTimes = FunSDK.ToTimeType(endTime);
           playHandle = FunSDK
                    .MediaCloudRecordPlay(userId,
                            funDevice.getDevSn(),0,"Main",absTime,endTimes,
                            playSurfaceView, 0);//主码流 Main 副码流 Sub

            FunSDK.SetIntAttr(playHandle,EFUN_ATTR.EOA_PCM_SET_SOUND,100);

            FunSDK.MediaSetSound(playHandle,100, 0);//开启声音 100 关闭声音0
        } else {
            FunSDK.MediaSeekToTime(playHandle,
                    0, absTime, 0);
        }

        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playHandle != 0) {
            FunSDK.MediaStop(playHandle);
        }
    }

    @Override
    public int OnFunSDKResult(Message message, MsgContent msgContent) {
        hideWaitDialog();

        if (message.what == EUIMSG.MC_SearchMediaByTime) {
            if (message.arg1 < 0) {
                Toast.makeText(ActivityDevCloudPlayBack.this, "查询失败", Toast.LENGTH_SHORT).show();
            } else {
                cloudMediaFiles.parseJson(msgContent.str);
                seekToTime(0);
                adapter.notifyDataSetChanged();
            }
        }
        return 0;
    }

    class FileTimeAdapter extends RecyclerView.Adapter<FileTimeAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ActivityDevCloudPlayBack.this).inflate(R.layout.item_simple_list,null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
           final CloudMediaFileInfoBean info = cloudMediaFiles.getFileList().get(position);
            if (info != null) {
                holder.tvFileTime.setText(info.startTime + "-" + info.endTime);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showWaitDialog();
                    seekToTime((int) info.getStartTimes());
                }
            });
        }

        @Override
        public int getItemCount() {
            return cloudMediaFiles.getFileNum();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvFileTime;
            public ViewHolder(View itemView) {
                super(itemView);
                tvFileTime = itemView.findViewById(R.id.tv_item_name);
            }
        }
    }
}
