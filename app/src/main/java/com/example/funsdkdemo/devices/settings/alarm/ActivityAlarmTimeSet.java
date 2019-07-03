package com.example.funsdkdemo.devices.settings.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.devices.settings.ActivityGuideDeviceSetup;
import com.example.funsdkdemo.entity.TimeItem;
import com.lib.SDKCONST.Switch;
import com.lib.funsdk.support.FunSupport;
import com.lib.sdk.bean.AlarmInfoBean;
import com.lib.sdk.bean.StringUtils;
import com.xm.ui.widget.ButtonCheck;
import com.xm.ui.widget.NumberPicker;
import com.xm.ui.widget.ScrollForeverTextView;
import com.xm.ui.widget.XTitleBar;

public class ActivityAlarmTimeSet extends ActivityDemo
		implements ButtonCheck.OnButtonClickListener,OnClickListener {

	XTitleBar mTitle;
	// 开关
	ButtonCheck mAlarmSettingOpen;
	// 编辑
	RelativeLayout mAlarmSettingEditRL;
	ButtonCheck mAlarmSettingEdit;
	LinearLayout mEditContent;
	// 设置星期
	RelativeLayout mAlarmSettingDateRL;
	ScrollForeverTextView mAlarmSettingDate;
	// 开始时间
	RelativeLayout mStartTimeRL;
	TextView mStartTime;
	// 停止时间
	RelativeLayout mStoptTimeRL;
	TextView mStopTime;
	// 设置重复
	ButtonCheck mRepeat;
	// 标签
	RelativeLayout mLabelShowRL;
	TextView mLabelShow;
	RelativeLayout mButtomLayout;
	RelativeLayout mButtomTimePickerLayout;
	ButtomTimepicker mButtomTimepicker;
	RelativeLayout mButtomTimepick;
	private int timeSelect;// 1:开始时间2：关闭时间
	private int startTime;
	private int stopTime;
	private AlarmInfoBean mAlarmInfo = new AlarmInfoBean();
	private int hours;
	private int minute;
	private int mPosition;
	private static final int REQUEST_CODE_SETTING_SELECT_DATE = 2;
	private static final int REQUEST_CODE_SETTING_SELECT_LABEL = 3;
	private String[] JudgeDate;
	private boolean isSave = false;// 用来标记数据是否被保存。
	private TimeItem mTimeInfo;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm_setting);
		InitView();
		initListener();
		initButtomView();
		init();
	}

	private void InitView() {
		// TODO Auto-generated method stub
		mTitle = (XTitleBar) this.findViewById(R.id.alarm_setting_title);
		mAlarmSettingOpen = (ButtonCheck) this.findViewById(R.id.alarm_setting_open);
		mAlarmSettingOpen.setOnButtonClick(this);
		mAlarmSettingEditRL = (RelativeLayout) this.findViewById(R.id.edit_rl);
		mAlarmSettingEditRL.setOnClickListener(this);
		mAlarmSettingEdit = (ButtonCheck) this.findViewById(R.id.alarm_setting_edit);
		mAlarmSettingEdit.setClickable(false);
		mEditContent = (LinearLayout) this.findViewById(R.id.edit_content);
		mEditContent.setOnClickListener(this);
		mAlarmSettingDateRL = (RelativeLayout) this.findViewById(R.id.alarm_setting_date_rl);
		mAlarmSettingDateRL.setOnClickListener(this);
		mAlarmSettingDate = (ScrollForeverTextView) this.findViewById(R.id.alarm_setting_date);
		mStartTimeRL = (RelativeLayout) this.findViewById(R.id.alarm_setting_start_time_rl);
		mStartTimeRL.setOnClickListener(this);
		mStoptTimeRL = (RelativeLayout) this.findViewById(R.id.alarm_setting_stop_time_rl);
		mStoptTimeRL.setOnClickListener(this);
		mStopTime = (TextView) this.findViewById(R.id.alarm_setting_stop_time);
		mStartTime = (TextView) this.findViewById(R.id.alarm_setting_start_time);
		mRepeat = (ButtonCheck) this.findViewById(R.id.alarm_setting_Repeat);
		mRepeat.setOnButtonClick(this);
		mLabelShowRL = (RelativeLayout) this.findViewById(R.id.alarm_setting_Label_rl);
		// mLabelShowRL.setOnClickListener(this);
		mLabelShow = (TextView) this.findViewById(R.id.tv_label_show);

		mButtomLayout = (RelativeLayout) this.findViewById(R.id.alarm_setting_buttom_timepick);
		mButtomLayout.setOnClickListener(this);

		mButtomTimePickerLayout = (RelativeLayout) findViewById(R.id.timepicker_rl);
		mButtomTimePickerLayout.setOnClickListener(this);
		JudgeDate = new String[] { getString(R.string.sunday),getString(R.string.monday),getString(R.string.tuesday),
		getString(R.string.wednesday),getString(R.string.thursday),getString(R.string.friday),getString(R.string.saturday)};
	}

	private void initListener() {
		mTitle.setLeftClick(new XTitleBar.OnLeftClickListener() {

			@Override
			public void onLeftclick() {
				finish();
			}
		});
		mTitle.setRightIvClick(new XTitleBar.OnRightClickListener() {

			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub
				saveConfig();
				isSave = true;
			}
		});
	}

	@Override
	public boolean onButtonClick(ButtonCheck bc, boolean bChecked) {
		// TODO Auto-generated method stub
		switch (bc.getId()) {
		case R.id.alarm_setting_open:
			mEditContent.setVisibility(mAlarmSettingOpen.getBtnValue() == 0 ? View.VISIBLE : View.GONE);
			mAlarmSettingEdit.setBtnValue(mAlarmSettingOpen.getBtnValue() == 0 ? 1 : 0);
			return true;
		case R.id.alarm_setting_Repeat:
			return true;
		case R.id.alarm_setting_edit:
			mEditContent.setVisibility(mEditContent.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
			mAlarmSettingOpen.setBtnValue(Switch.Open);
			return true;
		default:
			return false;
		}

	}

	public void init() {
		mAlarmInfo = FunSupport.getInstance().mAlarmInfoBean;
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", 0);
		mPosition = position;
		mTimeInfo = (TimeItem) intent.getSerializableExtra("timeInfo");
		if (null == mTimeInfo || null == mTimeInfo.getTime()) {
			Toast.makeText(this, R.string.get_config_f, Toast.LENGTH_LONG).show();
			finish();
		}
		String[] times = mTimeInfo.getTime().split("-");
		mStartTime.setText(times[0]);
		mStopTime.setText(times[1]);
		mLabelShow.setText(getString(R.string.time_slot) + (position + 1));
		mAlarmSettingOpen.setBtnValue(mTimeInfo.isOpen() ? 1 : 0);
		mAlarmSettingDate.setText(mTimeInfo.getWeeks(this));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("requestCode-->" + requestCode + "resultCode-->" + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_SETTING_SELECT_LABEL) {
				String label = data.getExtras().get("Label").toString();
				mLabelShow.setText(label);
				mEditContent.setVisibility(View.VISIBLE);
			} else if (requestCode == REQUEST_CODE_SETTING_SELECT_DATE) {
				int date = data.getIntExtra("WeekMask", 0);
				mTimeInfo.setWeekMask(date);
				mAlarmSettingDate.setText(mTimeInfo.getWeeks(this));
			}

		} else {

		}

	}

	private void initButtomView() {
		mButtomTimepicker = new ButtomTimepicker();
		String[] hour = new String[24];
		for (int i = 0; i < hour.length; i++) {
			if (i < 10) {
				hour[i] = "0" + i;
			} else {
				hour[i] = "" + i;
			}
		}
		String[] min = new String[60];
		for (int i = 0; i < min.length; i++) {
			if (i < 10) {
				min[i] = "0" + i;
			} else {
				min[i] = "" + i;
			}
		}
		mButtomTimepicker.mHourNumPicker = (NumberPicker) findViewById(R.id.numpicker_hour);
		mButtomTimepicker.mMinNumPicker = (NumberPicker) findViewById(R.id.numpicker_min);
		mButtomTimepicker.mHourNumPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mButtomTimepicker.mHourNumPicker.setMaxValue(hour.length - 1);
		mButtomTimepicker.mHourNumPicker.setMinValue(0);
		mButtomTimepicker.mHourNumPicker.setDisplayedValues(hour);
		mButtomTimepicker.mHourNumPicker.setValue(hours);
		mButtomTimepicker.mMinNumPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mButtomTimepicker.mMinNumPicker.setMaxValue(min.length - 1);
		mButtomTimepicker.mMinNumPicker.setMinValue(0);
		mButtomTimepicker.mMinNumPicker.setDisplayedValues(min);
		mButtomTimepicker.mMinNumPicker.setValue(minute);
		mButtomTimepicker.mSureTV = (TextView) findViewById(R.id.tv_sure);
		mButtomTimepicker.mCancelTV = (TextView) findViewById(R.id.tv_cancel);
		mButtomTimepicker.mSureTV.setOnClickListener(this);
		mButtomTimepicker.mCancelTV.setOnClickListener(this);
		startTime = Integer.parseInt(mStartTime.getText().toString().replace(":", ""));
		stopTime = Integer.parseInt(mStopTime.getText().toString().replace(":", ""));
	}

	private void showButtomLayout() {

		if (mButtomLayout.getVisibility() == View.INVISIBLE) {
			mButtomLayout.setVisibility(View.VISIBLE);
			Animation a = AnimationUtils.loadAnimation(this, R.anim.popshow_anim);
			mButtomLayout.setAnimation(a);
		} else {
			mButtomLayout.setVisibility(View.INVISIBLE);
		}

	}

	private void hideButtomLayout() {
		if (mButtomLayout.getVisibility() == View.VISIBLE) {
			mButtomLayout.setVisibility(View.INVISIBLE);
			Animation a = AnimationUtils.loadAnimation(this, R.anim.pophidden_anim);
			mButtomLayout.setAnimation(a);
		}

	}

	/**
	 * 设置开始时间和关闭时间，并在页面上显示
	 */
	private void setTime() {
		String hourStr = "";
		String minStr = "";
		int hour = mButtomTimepicker.mHourNumPicker.getValue();
		int min = mButtomTimepicker.mMinNumPicker.getValue();
		if (String.valueOf(hour).length() < 2) {
			hourStr = "0" + String.valueOf(hour);
		} else {
			hourStr = String.valueOf(hour);
		}
		if (String.valueOf(min).length() < 2) {
			minStr = "0" + String.valueOf(min);
		} else {
			minStr = String.valueOf(min);
		}
		if (timeSelect == 1) {

			int time = Integer.parseInt(hourStr + minStr);
			if (time == stopTime) {
				Toast.makeText(this, R.string.config_failure_opentime_same, Toast.LENGTH_SHORT).show();
				return;
			}
			mStartTime.setText(hourStr + ":" + minStr);
			startTime = time;
		}
		if (timeSelect == 2) {
			int time = Integer.parseInt(hourStr + minStr);
			if (time == startTime) {
				Toast.makeText(this, R.string.config_failure_closetime_same, Toast.LENGTH_SHORT).show();
				return;
			}
			if(time < startTime){
				Toast.makeText(this, R.string.config_failure_closetime_too_early, Toast.LENGTH_SHORT).show();
				return;
			}
			mStopTime.setText(hourStr + ":" + minStr);
			stopTime = time;
		}
		hideButtomLayout();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.alarm_setting_date_rl:
				ActivityWeekSelect.actionStart(this,mTimeInfo.getWeekMask(),REQUEST_CODE_SETTING_SELECT_DATE);
				break;

			case R.id.alarm_setting_start_time_rl:
				minute = Integer.parseInt(mStartTime.getText().toString().trim().substring(3));
				hours = Integer.parseInt(mStartTime.getText().toString().trim().substring(0, 2));
				UpdateTimeUI(minute, hours);
				timeSelect = 1;
				break;

			case R.id.alarm_setting_stop_time_rl:
				minute = Integer.parseInt(mStopTime.getText().toString().trim().substring(3));
				hours = Integer.parseInt(mStopTime.getText().toString().trim().substring(0, 2));
				UpdateTimeUI(minute, hours);
				timeSelect = 2;
				break;
			case R.id.alarm_setting_Label_rl:
				Intent intent1 = new Intent(this, ActivityAlarmSettingLabel.class);
				startActivityForResult(intent1, REQUEST_CODE_SETTING_SELECT_LABEL);
				break;
			case R.id.buttom_timepick:// 点击布局消失
				hideButtomLayout();
				break;
			case R.id.tv_sure:
				setTime();
				break;
			case R.id.tv_cancel:
				hideButtomLayout();
				break;
			case R.id.edit_rl:
				mEditContent.setVisibility(mEditContent.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
				mAlarmSettingEdit.setBtnValue(mAlarmSettingEdit.getBtnValue() == 1 ? 0 : 1);
				mAlarmSettingOpen.setBtnValue(Switch.Open);
				break;
			default:
				break;
		}
	}

	class ButtomTimepicker {
		NumberPicker mHourNumPicker;
		NumberPicker mMinNumPicker;
		TextView mSureTV;
		TextView mCancelTV;
	}



	private void saveConfig() {
		// TODO Auto-generated method stub
		String startTime = mStartTime.getText().toString();
		String stopTime = mStopTime.getText().toString();
		if (StringUtils.contrast(startTime, stopTime)) {
			Toast.makeText(this, R.string.config_failure_closetime_same, Toast.LENGTH_SHORT).show();
			return;
		}
		if (mAlarmSettingDate.getText().toString().length() == 0 && mAlarmSettingOpen.getBtnValue() == Switch.Open) {
			Toast.makeText(this, R.string.please_select_week, Toast.LENGTH_SHORT).show();
			return;
		}
		mTimeInfo.setTime(startTime + "-" + stopTime);
		mTimeInfo.setOpen(mAlarmSettingOpen.getBtnValue() == 1);
		Intent intent = new Intent(this, ActivityGuideDeviceSetup.class);
		intent.putExtra("timeInfo", mTimeInfo);
		intent.putExtra("mPosition", mPosition);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	// 更新时间控件的显示
	public void UpdateTimeUI(int min, int hours) {
		mButtomTimepicker.mMinNumPicker.setValue(min);
		mButtomTimepicker.mHourNumPicker.setValue(hours);
		showButtomLayout();
	}

}
