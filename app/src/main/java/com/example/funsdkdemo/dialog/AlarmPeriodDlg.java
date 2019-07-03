package com.example.funsdkdemo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basic.G;
import com.example.common.DialogWaitting;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.adapter.TimePeriodsAdapter;
import com.example.funsdkdemo.devices.settings.alarm.ActivityAlarmTimeSet;
import com.example.funsdkdemo.entity.TimeItem;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.MsgContent;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.bean.AlarmInfoBean;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.bean.JsonConfig;
import com.lib.sdk.bean.StringUtils;
import com.xm.ui.widget.XTitleBar;

import java.util.ArrayList;

public class AlarmPeriodDlg extends BaseDlg implements OnCheckedChangeListener {
	public static final String ALL_DAY_TIME = "00:00-24:00";
	public static final String ALL_DAY_OPEN = "1 00:00:00-24:00:00";
	public static final int REQUEST_CODE_SETTING_ALARM_TIME = 5;
	private Activity mActivity;
	private Dialog mDlg;
	private RadioGroup mRadioTime;
	private int mUserId;

	private TextView add_time;
	private TextView mDeleteItem;
	private ListView mListView;
	private String weekday_str[];
	private XTitleBar mTitle;
	private HandleConfigData<Object> mConfigData;
	public AlarmInfoBean mAlarmInfo;
	public TimePeriodsAdapter mTimeAdapter;
	public boolean isSave = false;// 用来标记数据是否被保存。
	private FunDevice mFunDevice;
	private DialogWaitting mWaitDialog = null;
	public AlarmPeriodDlg(Activity mActivity) {
		this.mActivity = mActivity;
		initView();
		initData();
	}

	private void initView() {
		mDlg = new Dialog(mActivity, R.style.MyDialog);
		View v = LayoutInflater.from(mActivity).inflate(R.layout.dlg_alarm_period, null);
		mTitle = (XTitleBar) v.findViewById(R.id.alarm_period_title);
		mRadioTime = (RadioGroup) v.findViewById(R.id.time);
		mListView = (ListView) v.findViewById(R.id.show_task_listview);
		mDeleteItem = (TextView) v.findViewById(R.id.delete_alarm_item);
		mDeleteItem.setOnClickListener(this);
		add_time = (TextView) v.findViewById(R.id.add_time);
		weekday_str = new String[] { FunSDK.TS("Monday"), FunSDK.TS("Tuesday"), FunSDK.TS("Wednesday"),
				FunSDK.TS("Thursday"), FunSDK.TS("Friday"), FunSDK.TS("Saturday"), FunSDK.TS("Sunday") };
		mDlg.setContentView(v);
		initListener();

		mWaitDialog = new DialogWaitting(mActivity);
	}

	private void initListener() {
		mTitle.setLeftClick(new XTitleBar.OnLeftClickListener() {

			@Override
			public void onLeftclick() {
				mDlg.dismiss();
				mTitle.setLeftBtnValue(0);
			}
		});
		mTitle.setRightIvClick(new XTitleBar.OnRightClickListener() {

			@Override
			public void onRightClick() {
				saveConfig();
				mTitle.setLeftBtnValue(0);
			}
		});
		add_time.setOnClickListener(this);
		mRadioTime.setOnCheckedChangeListener(this);
	}

	private void initData() {
		mWaitDialog.show();
		mUserId = FunSDK.RegUser(this);
		mConfigData = new HandleConfigData<Object>();
	}

	public void setFunDevice(FunDevice funDevice) {
		this.mFunDevice = funDevice;
	}

	public void dismiss() {
		mDlg.dismiss();
	}

	public void show() {
		FunSDK.DevGetConfigByJson(mUserId, mFunDevice.getDevSn(), JsonConfig.DETECT_MOTIONDETECT, 1024, 0,
				5000, 0);
		mDlg.show();
		mWaitDialog.show();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.time_diy) {
			mListView.setVisibility(View.VISIBLE);
		} else if (checkedId == R.id.time_day) {
			mListView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.delete_alarm_item:
			mDeleteItem.setVisibility(View.GONE);
			break;
		case R.id.add_time:
			mActivity.startActivity(new Intent(mActivity, ActivityAlarmTimeSet.class));
			break;
		}
	}

	public boolean parseAllDay() {
		for (int i = 0; i < SDKCONST.NET_N_WEEKS; ++i) {
			String time = mAlarmInfo.EventHandler.TimeSection[i][0];
			if (!StringUtils.contrast(time, ALL_DAY_OPEN)) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<TimeItem> parseSomeDay() {
		// 第一个时间段被用于判断是否为每天报警了
		ArrayList<TimeItem> alarmTimeInfo = new ArrayList<TimeItem>();
		for (int i = 1; i < SDKCONST.NET_N_TSECT; ++i) {
			TimeItem info = new TimeItem();
			int weekMask = 0;
			String[] eachTime = null;
			for (int j = 0; j < SDKCONST.NET_N_WEEKS; ++j) {
				String time = mAlarmInfo.EventHandler.TimeSection[j][i];
				eachTime = time.split("\\W+");
				if (StringUtils.contrast(eachTime[0], "1")) {
					info.setOpen(true);
					info.setTime(eachTime[1] + ":" + eachTime[2] + "-" + eachTime[4] + ":" + eachTime[5]);
					weekMask |= 1 << j;
				}
			}
			info.setWeekMask(weekMask);
			if (!info.isOpen() && null != eachTime) {
				info.setTime(eachTime[1] + ":" + eachTime[2] + "-" + eachTime[4] + ":" + eachTime[5]);
			}
			alarmTimeInfo.add(info);
		}
		return alarmTimeInfo;
	}

	public void parsData() {
		if (parseAllDay()) {
			mRadioTime.check(R.id.time_day);
		} else {
			mRadioTime.check(R.id.time_diy);
		}
		mTimeAdapter = new TimePeriodsAdapter(mActivity, parseSomeDay());
		mListView.setAdapter(mTimeAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, ActivityAlarmTimeSet.class);
				intent.putExtra("position", position);
				intent.putExtra("timeInfo", (TimeItem) mTimeAdapter.getItem(position));
				mActivity.startActivityForResult(intent, REQUEST_CODE_SETTING_ALARM_TIME);
			}
		});
	}

	private void saveConfig() {
		if (null != mAlarmInfo) {
			for (int i = 0; i < SDKCONST.NET_N_WEEKS; i++) {
				mAlarmInfo.EventHandler.TimeSection[i][0] = String.format("%s %s:%s:%s-%s:%s:%s",
						mRadioTime.getCheckedRadioButtonId() == R.id.time_day ? 1 : 0, "00", "00", "00", "24", "00",
						"00");
			}
			FunSupport.getInstance().mAlarmInfoBean = mAlarmInfo;
			FunSDK.DevSetConfigByJson(mUserId, mFunDevice.getDevSn(),
					JsonConfig.DETECT_MOTIONDETECT, JSON.toJSONString(mAlarmInfo), 0, 5000, 0);
			isSave = true;
			mDlg.dismiss();
		}
	}

	public void update(int position, TimeItem data) {
		mTimeAdapter.update(position, data);
	}

	@Override
	public int OnFunSDKResult(Message msg, MsgContent ex) {
		mWaitDialog.dismiss();
		if (msg.arg1 < 0) {
			Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
			return 0;
		}
		switch (msg.what) {
		case EUIMSG.DEV_GET_JSON:
			if (JsonConfig.DETECT_MOTIONDETECT.equals(ex.str) || JsonConfig.DETECT_LOCAL_ALARM.equals(ex.str)) {
				mConfigData.getDataObj(G.ToString(ex.pData), AlarmInfoBean.class);
				FunSupport.getInstance().mAlarmInfoBean = (AlarmInfoBean) mConfigData.getObj();
				mAlarmInfo = FunSupport.getInstance().mAlarmInfoBean;
				parsData();
			}
			break;
		case EUIMSG.DEV_SET_JSON:
			mWaitDialog.dismiss();
			if (JsonConfig.DETECT_MOTIONDETECT.equals(ex.str) || JsonConfig.DETECT_LOCAL_ALARM.equals(ex.str)) {
				Toast.makeText(mActivity,R.string.Save_Success, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		return 0;
	}
}
