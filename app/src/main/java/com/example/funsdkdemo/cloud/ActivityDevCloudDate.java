package com.example.funsdkdemo.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.cloud.CloudDirectory;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.bean.cloudmedia.CloudMediaDatesBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @name FunSDKDemo_Android_Old2018
 * @class name：com.example.funsdkdemo.cloud
 * @class describe
 * @time 2019-06-25 8:59
 * @change
 * @chang time
 * @class describe
 */
public class ActivityDevCloudDate extends ActivityDemo implements View.OnClickListener,IFunSDKResult{
    private RecyclerView rcCloudDate;
    private ImageView ivPreDate;
    private ImageView ivNextDate;
    private TextView tvDate;
    private Calendar calendar;
    private SimpleDateFormat format;
    private int userId;
    private FunDevice funDevice;
    private List<CloudMediaDatesBean.CloudDate> cloudDates;
    private CloudDateAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_cloud);
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

        rcCloudDate = findViewById(R.id.rc_cloud_date);
        ivPreDate = findViewById(R.id.pre_date_iv);
        ivNextDate = findViewById(R.id.next_date_iv);
        tvDate = findViewById(R.id.date_select_tv);

        ivPreDate.setOnClickListener(this);
        ivNextDate.setOnClickListener(this);

        rcCloudDate.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        int devId = intent.getIntExtra("FUN_DEVICE_ID",0);
        funDevice = FunSupport.getInstance().findDeviceById(devId);

        userId = FunSDK.GetId(userId,this);

        calendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-MM");
        tvDate.setText(format.format(calendar.getTime()));

        adapter = new CloudDateAdapter();
        rcCloudDate.setAdapter(adapter);

        searchCloudByMonth();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pre_date_iv:
                calendar.add(Calendar.MONTH,-1);
                format = new SimpleDateFormat("yyyy-MM");
                tvDate.setText(format.format(calendar.getTime()));
                searchCloudByMonth();
                break;
            case R.id.next_date_iv:
                calendar.add(Calendar.MONTH,+1);
                format = new SimpleDateFormat("yyyy-MM");
                tvDate.setText(format.format(calendar.getTime()));
                searchCloudByMonth();
                break;
        }
    }

    private void searchCloudByMonth() {
        synchronized (calendar) {
            showWaitDialog();
            int times = FunSDK.ToTimeType(new int[]{calendar.get(Calendar.YEAR),
                    (calendar.get(Calendar.MONTH) + 1),
                    (calendar.get(Calendar.DAY_OF_MONTH)), 0, 0, 0});
            CloudDirectory.SearchMediaByMoth(userId,funDevice.getDevSn() , 0, "Main", times, 0);
        }
    }


    @Override
    public int OnFunSDKResult(Message message, MsgContent msgContent) {
        hideWaitDialog();

        if (message.what == EUIMSG.MC_SearchMediaByMoth) {
            CloudMediaDatesBean cloudMediaDatesBean = new CloudMediaDatesBean();
            cloudMediaDatesBean.parseJson(msgContent.str);

            cloudDates = cloudMediaDatesBean.getDateTimes();
            adapter.notifyDataSetChanged();

        }
        return 0;
    }

    class CloudDateAdapter extends RecyclerView.Adapter<CloudDateAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ActivityDevCloudDate.this).inflate(R.layout.item_simple_list,null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final CloudMediaDatesBean.CloudDate cloudDate = cloudDates.get(position);
            if (cloudDate != null) {
                holder.tvDate.setText(cloudDate.getTime());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        format = new SimpleDateFormat("yyyy-MM-dd");
                        Date data = format.parse(cloudDate.getTime());
                        Intent it = new Intent(ActivityDevCloudDate.this, ActivityDevCloudPlayBack.class);
                        it.putExtra("year", data.getYear() + 1900);
                        it.putExtra("month", data.getMonth());
                        it.putExtra("day", data.getDate());
                        it.putExtra("FUN_DEVICE_ID",funDevice.getId());
                        startActivity(it);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return cloudDates != null ? cloudDates.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDate;
            public ViewHolder(View itemView) {
                super(itemView);
                tvDate = itemView.findViewById(R.id.tv_item_name);
            }
        }
    }
}
