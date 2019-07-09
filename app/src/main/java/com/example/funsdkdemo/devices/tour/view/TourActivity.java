package com.example.funsdkdemo.devices.tour.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.common.DialogWaitting;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.devices.tour.listener.TourContract;
import com.example.funsdkdemo.devices.tour.model.bean.PTZTourBean;
import com.example.funsdkdemo.devices.tour.model.bean.TourBean;
import com.example.funsdkdemo.devices.tour.presenter.TourPresenter;
import com.lib.FunSDK;
import com.lib.MsgContent;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.utils.TimeUtils;
import com.lib.funsdk.support.widget.CornerPopupWindow;
import com.xm.ui.widget.ListSelectItem;


/**
 * Created by ccy on 2017-09-22.
 * 巡航界面<br/>
 * 1、巡航点就是预置点<br/>
 * 2、在一条巡航线上添加巡航点，首先要新建这个巡航点，再添加到当前巡航线上<br/>
 * 3、为了新建的巡航点与原有的预置点相互独立，新建巡航点ID取值252、253、254，并且在预置点界面不显示这3个点<br/>
 * 4、所以在“添加一个巡航点到一条巡航线上”这个操作需要走两步：<br/>
 * （1）、创建一个ID为252/253/254的预置点（对应巡航界面上的1/2/3）<br/>
 * （2）、将新建的预置点添加到一条巡航线上<br/>
 * 5、所以在“删除一个巡航线上的某个巡航点”这个操作需要走两步：<br/>
 * （1）、删除这个预置点(ID是252/253/254之一）<br/>
 * （2）、移除当前巡航线上绑定的这个巡航点<br/>
 *
 * @see com.lib.sdk.bean.OPPTZControlBean
 * @see com.xworld.devset.tour.model.bean.OPPTZControlBean
 */

public class TourActivity extends ActivityDemo implements TourContract.ITourView,
        View.OnClickListener,View.OnLongClickListener {

    private TextView tour1;
    private TextView tour2;
    private TextView tour3;
    private TextView startTour;
    private TextView start360;

    public static final int[] PRESETS = {252, 253, 254}; //预置点列表
    private CornerPopupWindow tourPopup; //长按popupwindow
    private TourContract.ITourPresenter tourPresenter;  // P
    private PTZTourBean tourData = new PTZTourBean(); //需实例化，等于初始一个空的ID=0的巡航路线
    private boolean isTouring = false; //巡航中
    private boolean is360Touring = false; //360巡航中
    private DialogWaitting waitDialog = null;
    private FunDevice funDevice;

    private ListSelectItem lsiTimimgPtzTourEnable;
    private SeekBar sbTimimgPtzTourInterval;
    private ListSelectItem lsiTimimgPtzTourInterval;
    private TextView tvTimimgPtzTourSupport;
    public static TourActivity newInstance() {
        return new TourActivity();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tour);
        initView();
        initData();
        updateViewState();
    }

    private void initView(){
        findViewById(R.id.backBtnInTopLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tour1 = (TextView) findViewById(R.id.tour_point_1);
        tour2 = (TextView) findViewById(R.id.tour_point_2);
        tour3 = (TextView) findViewById(R.id.tour_point_3);
        startTour = (TextView) findViewById(R.id.tour_start);
        start360 = (TextView) findViewById(R.id.tour_start_360);
        start360.setVisibility(View.GONE);
        tour1.setOnClickListener(this);
        tour2.setOnClickListener(this);
        tour3.setOnClickListener(this);
        startTour.setOnClickListener(this);
        start360.setOnClickListener(this);

        tour1.setOnLongClickListener(this);
        tour2.setOnLongClickListener(this);
        tour3.setOnLongClickListener(this);

        lsiTimimgPtzTourInterval = findViewById(R.id.lsi_timimg_ptz_tour_interval);
        sbTimimgPtzTourInterval = lsiTimimgPtzTourInterval.getExtraSeekbar();
        sbTimimgPtzTourInterval.setMax(60);
        sbTimimgPtzTourInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int prgress, boolean bUser) {
                if (bUser) {
                    lsiTimimgPtzTourInterval.setRightText(TimeUtils.formatTimes(prgress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tourPresenter.setTimingPtzTour(lsiTimimgPtzTourEnable.getRightValue() == SDKCONST.Switch.Open,
                        sbTimimgPtzTourInterval.getProgress() * 60);
            }
        });

        lsiTimimgPtzTourEnable = findViewById(R.id.lsi_timimg_ptz_tour_enable);
        lsiTimimgPtzTourEnable.setOnRightClick(new ListSelectItem.OnRightImageClickListener() {
            @Override
            public void onClick(ListSelectItem listSelectItem, View view) {
                tourPresenter.setTimingPtzTour(listSelectItem.getRightValue() == SDKCONST.Switch.Open,
                        sbTimimgPtzTourInterval.getProgress() * 60);
            }
        });

        lsiTimimgPtzTourInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lsiTimimgPtzTourInterval.toggleExtraView();
            }
        });

        tvTimimgPtzTourSupport = findViewById(R.id.tv_timimg_ptz_tour_support);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tour_point_1:
            case R.id.tour_point_2:
            case R.id.tour_point_3:
                addTours((TextView) v);
                break;
            case R.id.tour_start:
                startTour((TextView) v);
                break;
            case R.id.tour_start_360:
                start360((TextView) v);
                break;
        }
    }
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.tour_point_1:
            case R.id.tour_point_2:
            case R.id.tour_point_3:
                setupTours((TextView) v);
                break;
        }
        return true;
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        int devId = intent.getIntExtra("FUN_DEVICE_ID",0);
        funDevice = FunSupport.getInstance().findDeviceById(devId);

        tourPresenter = new TourPresenter(this, this,funDevice);
        tourPresenter.getTour();
        tourPresenter.getTimimgPtzTour();

        if (FunSDK.GetDevAbility(funDevice.getDevSn(),"OtherFunction/SupportTimingPtzTour") > 0) {
            tvTimimgPtzTourSupport.setText("定时巡航功能是支持的，可以正常操作");
        }else {
            tvTimimgPtzTourSupport.setText("定时巡航功能是不支持，不可以操作");
        }
    }
    public void addTours(TextView v) {

        if (isTouring) {
            Toast.makeText(this, R.string.stop_tour_first, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.tour_point_1:
                if (!tour1.isSelected()) {
                    tourPresenter.addTour(PRESETS[0], tourData.Id,calculatePresetIndex(PRESETS[0]));
                } else {
                    tourPresenter.turnPreset(PRESETS[0]);
                }
                break;
            case R.id.tour_point_2:
                if (!tour2.isSelected()) {
                    tourPresenter.addTour(PRESETS[1], tourData.Id,calculatePresetIndex(PRESETS[1]));
                } else {
                    tourPresenter.turnPreset(PRESETS[1]);
                }
                break;
            case R.id.tour_point_3:
                if (!tour3.isSelected()) {
                    tourPresenter.addTour(PRESETS[2], tourData.Id,calculatePresetIndex(PRESETS[2]));
                } else {
                    tourPresenter.turnPreset(PRESETS[2]);
                }
                break;
        }

    }


    /**
     * 计算新增的巡航点要插入在巡航路线中的位置(无论用户用何种顺序添加的巡航点，最终顺序要PRESETS[0] ->PRESETS[1] ->PRESETS[2]
     * @param presetId  要添加的预置点ID
     * @return
     */
    private int calculatePresetIndex(int presetId){

        int index = 0;  //要插入的位置
        for (int i = 0; i < tourData.Tour.size(); i++) {
            if(presetId > tourData.Tour.get(i).Id){   //如果要添加的巡航点id大于当前巡航点的id，加1
                index = i+1;
            }
        }
        return index;

    }

    //    @OnLongClick({R.id.tour_point_1, R.id.tour_point_2, R.id.tour_point_3})
    public boolean setupTours(TextView v) {
        if (v.isSelected()) {
            setupPopup();
            switch (v.getId()) {
                case R.id.tour_point_1:
                    tourPopup.setTag(PRESETS[0]);
                    break;
                case R.id.tour_point_2:
                    tourPopup.setTag(PRESETS[1]);
                    break;
                case R.id.tour_point_3:
                    tourPopup.setTag(PRESETS[2]);
                    break;
            }
            tourPopup.show();
        }
        return true;
    }

    /**
     * 初始化巡航点弹窗
     */
    private void setupPopup() {
        if (tourPopup == null) {
            tourPopup = new CornerPopupWindow(this);
            tourPopup.setItem1(getString(R.string.reset_tour), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTouring) {
                        Toast.makeText(TourActivity.this,R.string.stop_tour_first, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int which = (int) tourPopup.getTag();
                    tourPresenter.resetTour(which, tourData.Id);
                }
            });
            tourPopup.setItem2(getString(R.string.delete_tour), this.getResources().getColor(R.color.theme_color), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTouring) {
                        Toast.makeText(TourActivity.this,R.string.stop_tour_first, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int which = (int) tourPopup.getTag();
                    tourPresenter.deleteTour(which, tourData.Id);
                }
            });
            tourPopup.setItem3(getString(R.string.common_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tourPopup != null) {
                        tourPopup.dissmiss();
                    }
                }
            });
        }
    }

    //    @OnClick(R.id.tour_start)
    public void startTour(TextView v) {
        if(is360Touring){
            Toast.makeText(this,R.string.stop_360tour_first, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isTouring) {
            tourPresenter.stopTour();
        } else {
            tourPresenter.startTour(tourData.Id);
        }

    }

    //    @OnClick(R.id.tour_start_360)
    public void start360(TextView v) {
        Toast.makeText(this,"暂不支持", Toast.LENGTH_SHORT).show();
    }

    /**
     * 更新控件状态
     */
    private void updateViewState() {
        tour1.setSelected(false);
        tour2.setSelected(false);
        tour3.setSelected(false);
        startTour.setEnabled(false);
        if (tourData != null && tourData.Tour != null) {
            for (int i = 0; i < tourData.Tour.size(); i++) {
                int presetId = tourData.Tour.get(i).Id;
                if (presetId == PRESETS[0]) {
                    tour1.setSelected(true);
                    continue;
                }
                if (presetId == PRESETS[1]) {
                    tour2.setSelected(true);
                    continue;
                }
                if (presetId == PRESETS[2]) {
                    tour3.setSelected(true);
                    continue;
                }
            }
        }
        startTour.setEnabled(tour1.isSelected() && tour2.isSelected() && tour3.isSelected());
        startTour.setSelected(isTouring);
        start360.setSelected(is360Touring);

    }


    @Override
    public void onDestroy() {
        hideWaitDialog();
        tourPresenter.removeAllCallback();
        super.onDestroy();
    }


    //以下V接口实现


    @Override
    public void onLoadTours(@Nullable PTZTourBean tourBean) {
        if (tourBean != null) {  //为null时不要赋值,tourData要有默认实例
            this.tourData = tourBean;
        }
        updateViewState();
    }

    @Override
    public void onTourAdded(int presetId) {

        TourBean newTour = new TourBean();
        newTour.Id = presetId;
        tourData.Tour.add(calculatePresetIndex(presetId),newTour);
        updateViewState();
        Toast.makeText(this, R.string.tour_added, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTourDeleted(int presetId) {

        int deleteId = -1;
        for (int i = 0; i < tourData.Tour.size(); i++) {
            if (tourData.Tour.get(i).Id == presetId) {
                deleteId = i;
                break;
            }
        }
        if (deleteId != -1) {
            tourData.Tour.remove(deleteId);
            updateViewState();
            if (tourPopup != null && tourPopup.isShowing()) {
                tourPopup.dissmiss();
            }
            Toast.makeText(this, R.string.delete_s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTourReseted(int presetId) {
        updateViewState();
        if (tourPopup != null && tourPopup.isShowing()) {
            tourPopup.dissmiss();
        }
        Toast.makeText(this, R.string.reset_s, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onTourStarted() {

        isTouring = true;
        updateViewState();
    }

    @Override
    public void onTourStoped() {
        isTouring = false;
        updateViewState();
    }

    @Override
    public void onTour360Started() {
        is360Touring = true;
        updateViewState();
    }

    @Override
    public void onTour360Stoped() {
        is360Touring = false;
        updateViewState();
    }

    @Override
    public void onTourCleared() {
        tourData = new PTZTourBean();
        updateViewState();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void showLoading(boolean cancelable, String info) {
        showWaitDialog(info);
    }

    @Override
    public void dismissLoading() {
        hideWaitDialog();
    }


    @Override
    public void onFailed(Message msg, MsgContent ex, String extraStr) {
        hideWaitDialog();
        if (msg != null && ex != null) {
            Toast.makeText(this, "操作错误" + msg.arg1, Toast.LENGTH_LONG).show();
        } else if (extraStr != null) {
            Toast.makeText(this, extraStr, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTmimgPtzTourResult(boolean isEnable, int timeInterval) {
        lsiTimimgPtzTourEnable.setRightImage(isEnable ? SDKCONST.Switch.Open : SDKCONST.Switch.Close);
        sbTimimgPtzTourInterval.setProgress(timeInterval / 60);
        lsiTimimgPtzTourInterval.setRightText(TimeUtils.formatTimes(timeInterval / 60));
    }

    @Override
    public void onSaveTimimgPtzTourResult(boolean isSuccess) {
        if (isSuccess) {
            showToast(R.string.set_config_s);
        }
    }
}
