package com.example.funsdkdemo.devices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.AutoTime;
import com.lib.funsdk.support.config.BaseConfig;
import com.lib.funsdk.support.config.PowerSocketAutoLight;
import com.lib.funsdk.support.config.PowerSocketAutoSwitch;
import com.lib.funsdk.support.config.PowerSocketAutoUsb;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunDeviceSocket;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

public class ActivityGuideDevicesSocketSetTask extends
		ActivityDemo implements OnFunDeviceOptListener ,OnClickListener{
	public static final int SOCKET_POWER = 0;
	public static final int SOCKET_LIGHT = 1;
	public static final int SOCKET_USB = 2;
	private TextView open_time;
	private TextView close_time;
	private TimePickerDialog mTimeDialog;
	private CheckBox timeset_en;
	private String isCheck = "y#";
	private long open = 830;
	private long close = 1730;
	private int mPosition = -1;
	private RadioGroup repeat_select;
	private RadioButton repeat_repet, repeat_only;
	private Button[] mDayStartBtns = new Button[7];
	private Button[] mDayStopBtns = new Button[7];
	private boolean Multi[] = { false, false, false, false, false, false,
			false, false };
	private boolean Single[] = { false, false, false, false, false, false,
			false, false };
	private boolean Multi_s[] = { false, false, false, false, false, false,
			false, false };
	private boolean Single_s[] = { false, false, false, false, false, false,
			false, false };
	private ArrayList<Integer> initdays, initdays_stop;
	private boolean multi = true;
	private FunDevice mFunDevice = null;
	private int mFunType = SOCKET_POWER;
	private int TitleSet = 1;
	private AutoTime mInputAutoTime = null;
	private int i;
	private int startDay;
	private int stopDay;
	private int one_startDay;
	private int one_stopDay;
	private int k = 0;
	ArrayList<String> mDataList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socket_task);
		findViewById(R.id.open_time).setOnClickListener(this);
		findViewById(R.id.close_time).setOnClickListener(this);
		findViewById(R.id.day1).setOnClickListener(this);
		findViewById(R.id.day2).setOnClickListener(this);
		findViewById(R.id.day3).setOnClickListener(this);
		findViewById(R.id.day4).setOnClickListener(this);
		findViewById(R.id.day5).setOnClickListener(this);
		findViewById(R.id.day6).setOnClickListener(this);
		findViewById(R.id.day7).setOnClickListener(this);
		findViewById(R.id.day1_s).setOnClickListener(this);
		findViewById(R.id.day2_s).setOnClickListener(this);
		findViewById(R.id.day3_s).setOnClickListener(this);
		findViewById(R.id.day4_s).setOnClickListener(this);
		findViewById(R.id.day5_s).setOnClickListener(this);
		findViewById(R.id.day6_s).setOnClickListener(this);
		findViewById(R.id.day7_s).setOnClickListener(this);
		findViewById(R.id.timerset_ok).setOnClickListener(this);
		findViewById(R.id.backBtnInTopLayout).setOnClickListener(this);
		TitleSet = getIntent().getIntExtra("TITLE", 1);
		mPosition = getIntent().getIntExtra("FUNCTION_POSITION", -1);
		mFunType = getIntent().getIntExtra("FUNCTION_TYPE", SOCKET_POWER);

		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if (null == funDevice) {
			mFunDevice = null;
		} else {
			try {
				if (funDevice instanceof FunDeviceSocket) {
					mFunDevice = funDevice;
				} else {
					// 传入的参数不是插座类的,不适合用这个Activity打开
					mFunDevice = null;
				}
			} catch (Exception e) {
				mFunDevice = null;
				e.printStackTrace();
			}
		}

		if (null == mFunDevice) {
			finish();
			return;
		}
		// 是否新建
		try {
			if (mPosition >= 0 && mPosition < getAutoTimes().size()) {
				mInputAutoTime = getAutoTimes().get(mPosition);
			}
		} catch (Exception e) {
			mInputAutoTime = null;
		}
		initLayout();
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
	}

	@Override
	public void onDestroy() {
		FunSupport.getInstance().removeOnFunDeviceOptListener(this);
		super.onDestroy();
	}

	private void setDayStartSelected(int day) {
		for (int i = 0; i < mDayStartBtns.length; i++) {
			mDayStartBtns[i].setSelected(i == day);
		}
	}

	private void setDayStopSelected(int day) {
		for (int i = 0; i < mDayStopBtns.length; i++) {
			mDayStopBtns[i].setSelected(i == day);
		}
	}

	private void setDayMulStartSelected(int day) {
		mDayStartBtns[day].setSelected(true);
	}

	private void setDayMulStopSelected(int day) {
		mDayStopBtns[day].setSelected(true);
	}

	private void setDayStartUnSelected(int day) {
		mDayStartBtns[day].setSelected(false);
	}

	private void setDayStopUnSelected(int day) {
		mDayStopBtns[day].setSelected(false);
	}

	OnCheckedChangeListener myOnCheckedChangeLs = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			if (arg1 == R.id.repeat_repet) {

				multi = true;
			} else if (arg1 == R.id.repeat_only) {
				multi = false;
				setDayStartSelected(-1);
				setDayStopSelected(-1);
			}
		}

	};

	private void initLayout() {
		((TextView) findViewById(R.id.textViewInTopLayout)).setTextColor(getResources()
				.getColor(R.color.white));
		SetTextView(R.id.textViewInTopLayout, this.getResources().getString(R.string.device_socket_new_task));
		if (TitleSet == 0)
			SetTextView(R.id.textViewInTopLayout, this.getResources().getString(R.string.device_socket_modify_task));
		repeat_select = (RadioGroup) findViewById(R.id.repeat_picke);
		repeat_repet = (RadioButton) findViewById(R.id.repeat_repet);
		repeat_only = (RadioButton) findViewById(R.id.repeat_only);
		repeat_select.setOnCheckedChangeListener(myOnCheckedChangeLs);
		repeat_repet.setChecked(true);
		timeset_en = (CheckBox) findViewById(R.id.timeset_en);
		timeset_en
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg1)
							isCheck = "y#";
						else
							isCheck = "n#";
					}
				});
		mDayStartBtns[0] = (Button) this.findViewById(R.id.day1);
		mDayStartBtns[1] = (Button) this.findViewById(R.id.day2);
		mDayStartBtns[2] = (Button) this.findViewById(R.id.day3);
		mDayStartBtns[3] = (Button) this.findViewById(R.id.day4);
		mDayStartBtns[4] = (Button) this.findViewById(R.id.day5);
		mDayStartBtns[5] = (Button) this.findViewById(R.id.day6);
		mDayStartBtns[6] = (Button) this.findViewById(R.id.day7);
		mDayStopBtns[0] = (Button) this.findViewById(R.id.day1_s);
		mDayStopBtns[1] = (Button) this.findViewById(R.id.day2_s);
		mDayStopBtns[2] = (Button) this.findViewById(R.id.day3_s);
		mDayStopBtns[3] = (Button) this.findViewById(R.id.day4_s);
		mDayStopBtns[4] = (Button) this.findViewById(R.id.day5_s);
		mDayStopBtns[5] = (Button) this.findViewById(R.id.day6_s);
		mDayStopBtns[6] = (Button) this.findViewById(R.id.day7_s);
		open_time = (TextView) findViewById(R.id.open_time);
		close_time = (TextView) findViewById(R.id.close_time);
		// 初始化布局
		if (mInputAutoTime != null) {
			initdays = new ArrayList<Integer>();
			initdays_stop = new ArrayList<Integer>();
			if (mInputAutoTime.Enable) {
				timeset_en.setChecked(true);
				isCheck = "y#";
			} else {
				timeset_en.setChecked(false);
				isCheck = "n#";
			}
			startDay = mInputAutoTime.DayStart;
			int init_startday = startDay;
			int day_pos = 0;
			do {
				if ((init_startday & 1) == 1) {
					initdays.add(day_pos);
				}
				day_pos++;
			} while ((init_startday = (init_startday >> 1)) > 0);

			for (int idx = initdays.size() - 1; idx >= 0; idx--) {
				Integer j = initdays.get(idx);

				Multi[j] = true;
				if (j >= 0 && j < 7) {
					// Multi[j] = true;
					mDayStartBtns[j].setSelected(true);
				} else if (j == 7) {
					repeat_only.setChecked(true);
					multi = false;
				}
			}
			stopDay = mInputAutoTime.DayStop;
			int init_stopday = stopDay;
			int day_pos2 = 0;
			do {
				if ((init_stopday & 1) == 1) {
					initdays_stop.add(day_pos2);
				}
				day_pos2++;
			} while ((init_stopday = (init_stopday >> 1)) > 0);
			for (int idx = initdays_stop.size() - 1; idx >= 0; idx--) {
				Integer j = initdays_stop.get(idx);
				Multi_s[j] = true;
				if (j >= 0 && j < 7) {
					// Multi_s[j] = true;
					mDayStopBtns[j].setSelected(true);
				} else if (j == 7) {
					repeat_only.setChecked(true);
					multi = false;
				}
			}
			open_time.setText(getTimeStr(mInputAutoTime.TimeStart));
			close_time.setText(getTimeStr(mInputAutoTime.TimeStop));
			open = mInputAutoTime.TimeStart;
			close = mInputAutoTime.TimeStop;
		}
	}

	private String getTimeStr(int tm) {
		String tmpStr = String.format("%04d", tm);
		return tmpStr.substring(0, 2) + ":" + tmpStr.substring(2);
	}
	public boolean SetTextView(int id, String text) {
		TextView et = (TextView) this.findViewById(id);
		if (et == null) {
			return false;
		}
		et.setText(text);
		return true;
	}
	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.open_time:
			Calendar calendar = Calendar.getInstance();
			TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker timerPicker, int hourOfDay,
						int minute) {
					long tempTime = Long.parseLong(String.format("%02d",
							hourOfDay) + String.format("%02d", minute));
					if (close == tempTime) {
						Toast.makeText(ActivityGuideDevicesSocketSetTask.this,
								v.getContext().getResources().getString(R.string.devices_socket_dialog),
								Toast.LENGTH_SHORT).show();
						return;
					}

					open = tempTime;
					open_time.setText(String.format("%02d", hourOfDay) + ":"
							+ String.format("%02d", minute));
					System.out.println("open_time:" + open);
				}
			};
			mTimeDialog = new TimePickerDialog(this, timeListener,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), true);
			mTimeDialog.show();
			break;
		case R.id.close_time:
			Calendar calendar2 = Calendar.getInstance();
			TimePickerDialog.OnTimeSetListener timeListener2 = new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker timerPicker, int hourOfDay,
						int minute) {
					long tempTime = Long.parseLong(String.format("%02d",
							hourOfDay) + String.format("%02d", minute));
					if (tempTime == open) {
						Toast.makeText(ActivityGuideDevicesSocketSetTask.this,
								v.getContext().getResources().getString(R.string.devices_socket_dialog),
								Toast.LENGTH_SHORT).show();
						return;
					}
					close = tempTime;
					close_time.setText(String.format("%02d", hourOfDay) + ":"
							+ String.format("%02d", minute));
					System.out.println("close_time:" + close);
				}
			};
			mTimeDialog = new TimePickerDialog(this, timeListener2,
					calendar2.get(Calendar.HOUR_OF_DAY),
					calendar2.get(Calendar.MINUTE), true);
			mTimeDialog.show();
			break;
		case R.id.day1_s:
			if (multi) {
				if (Multi_s[0] == false) {
					Multi_s[0] = !Multi_s[0];
					stopDay |= (1 << 0);
					setDayMulStopSelected(0);
				} else if (Multi_s[0] == true) {
					Multi_s[0] = !Multi_s[0];
					stopDay &= (~(1 << 0));
					setDayStopUnSelected(0);
				}
			} else {
				if (Single_s[0] == false) {
					Single_s[0] = !Single_s[0];
					one_stopDay = (1 << 0);
					setDayStopSelected(0);
				} else if (Single_s[0] == true) {
					Single_s[0] = !Single_s[0];
					one_stopDay = 0;
					setDayStopUnSelected(0);
				}
			}
			break;
		case R.id.day2_s:
			if (multi) {
				if (Multi_s[1] == false) {
					Multi_s[1] = !Multi_s[1];
					stopDay |= (1 << 1);
					setDayMulStopSelected(1);
				} else if (Multi_s[1] == true) {
					Multi_s[1] = !Multi_s[1];
					stopDay &= (~(1 << 1));
					setDayStopUnSelected(1);
				}
			} else {
				if (Single_s[1] == false) {
					Single_s[1] = !Single_s[1];
					one_stopDay = (1 << 1);
					setDayStopSelected(1);
				} else if (Single_s[1] == true) {
					Single_s[1] = !Single_s[1];
					one_stopDay = 0;
					setDayStopUnSelected(1);
				}
			}
			break;
		case R.id.day3_s:
			if (multi) {
				if (Multi_s[2] == false) {
					Multi_s[2] = !Multi_s[2];
					stopDay |= (1 << 2);
					setDayMulStopSelected(2);
				} else if (Multi_s[2] == true) {
					Multi_s[2] = !Multi_s[2];
					stopDay &= (~(1 << 2));
					setDayStopUnSelected(2);
				}
			} else {
				if (Single_s[2] == false) {
					Single_s[2] = !Single_s[2];
					one_stopDay = (1 << 2);
					setDayStopSelected(2);
				} else if (Single_s[2] == true) {
					Single_s[2] = !Single_s[2];
					one_stopDay = 0;
					setDayStopUnSelected(2);
				}
			}
			break;
		case R.id.day4_s:
			if (multi) {
				if (Multi_s[3] == false) {
					Multi_s[3] = !Multi_s[3];
					stopDay |= (1 << 3);
					setDayMulStopSelected(3);
				} else if (Multi_s[3] == true) {
					Multi_s[3] = !Multi_s[3];
					stopDay &= (~(1 << 3));
					setDayStopUnSelected(3);
				}
			} else {
				if (Single_s[3] == false) {
					Single_s[3] = !Single_s[3];
					one_stopDay = (1 << 3);
					setDayStopSelected(3);
				} else if (Single_s[3] == true) {
					Single_s[3] = !Single_s[3];
					one_stopDay = 0;
					setDayStopUnSelected(3);
				}
			}
			break;
		case R.id.day5_s:
			if (multi) {
				if (Multi_s[4] == false) {
					Multi_s[4] = !Multi_s[4];
					stopDay |= (1 << 4);
					setDayMulStopSelected(4);
				} else if (Multi_s[4] == true) {
					Multi_s[4] = !Multi_s[4];
					stopDay &= (~(1 << 4));
					setDayStopUnSelected(4);
				}
			} else {
				if (Single_s[4] == false) {
					Single_s[4] = !Single_s[4];
					one_stopDay = (1 << 4);
					setDayStopSelected(4);
				} else if (Single_s[4] == true) {
					Single_s[4] = !Single_s[4];
					one_stopDay = 0;
					setDayStopUnSelected(4);
				}
			}
			break;
		case R.id.day6_s:
			if (multi) {
				if (Multi_s[5] == false) {
					Multi_s[5] = !Multi_s[5];
					stopDay |= (1 << 5);
					setDayMulStopSelected(5);
				} else if (Multi_s[5] == true) {
					Multi_s[5] = !Multi_s[5];
					stopDay &= (~(1 << 5));
					setDayStopUnSelected(5);
				}
			} else {
				if (Single_s[5] == false) {
					Single_s[5] = !Single_s[5];
					one_stopDay = (1 << 5);
					setDayStopSelected(5);
				} else if (Single_s[5] == true) {
					Single_s[5] = !Single_s[5];
					one_stopDay = 0;
					setDayStopUnSelected(5);
				}
			}
			break;
		case R.id.day7_s:
			if (multi) {
				if (Multi_s[6] == false) {
					Multi_s[6] = !Multi_s[6];
					stopDay |= (1 << 6);
					setDayMulStopSelected(6);
				} else if (Multi_s[6] == true) {
					Multi_s[6] = !Multi_s[6];
					stopDay &= (~(1 << 6));
					setDayStopUnSelected(6);
				}
			} else {
				if (Single_s[6] == false) {
					Single_s[6] = !Single_s[6];
					one_stopDay = (1 << 6);
					setDayStopSelected(6);
				} else if (Single_s[6] == true) {
					Single_s[6] = !Single_s[6];
					one_stopDay = 0;
					setDayStopUnSelected(6);
				}
			}
			break;
		case R.id.day1:
			if (multi) {
				if (Multi[0] == false) {
					Multi[0] = !Multi[0];
					startDay |= (1 << 0);
					setDayMulStartSelected(0);
				} else if (Multi[0] == true) {
					Multi[0] = !Multi[0];
					startDay &= (~(1 << 0));
					setDayStartUnSelected(0);
				}
			} else {
				if (Single[0] == false) {
					Single[0] = !Single[0];
					one_startDay = (1 << 0);
					setDayStartSelected(0);
				} else if (Single[0] == true) {
					Single[0] = !Single[0];
					one_startDay = 0;
					setDayStartUnSelected(0);
				}
			}
			break;
		case R.id.day2:
			if (multi) {
				if (Multi[1] == false) {
					Multi[1] = !Multi[1];
					startDay |= (1 << 1);
					setDayMulStartSelected(1);
				} else if (Multi[1] == true) {
					Multi[1] = !Multi[1];
					startDay &= (~(1 << 1));
					setDayStartUnSelected(1);
				}
			} else {
				if (Single[1] == false) {
					Single[1] = !Single[1];
					one_startDay = (1 << 1);
					setDayStartSelected(1);
				} else if (Single[1] == true) {
					Single[1] = !Single[1];
					one_startDay = 0;
					setDayStartUnSelected(1);
				}
			}
			break;
		case R.id.day3:
			if (multi) {
				if (Multi[2] == false) {
					Multi[2] = !Multi[2];
					startDay |= (1 << 2);
					setDayMulStartSelected(2);
				} else if (Multi[2] == true) {
					Multi[2] = !Multi[2];
					startDay &= (~(1 << 2));
					setDayStartUnSelected(2);
				}
			} else {
				if (Single[2] == false) {
					Single[2] = !Single[2];
					one_startDay = (1 << 2);
					setDayStartSelected(2);
				} else if (Single[2] == true) {
					Single[2] = !Single[2];
					one_startDay = 0;
					setDayStartUnSelected(2);
				}
			}
			break;
		case R.id.day4:
			if (multi) {
				if (Multi[3] == false) {
					Multi[3] = !Multi[3];
					startDay |= (1 << 3);
					setDayMulStartSelected(3);
				} else if (Multi[3] == true) {
					Multi[3] = !Multi[3];
					startDay &= (~(1 << 3));
					setDayStartUnSelected(3);
				}
			} else {
				if (Single[3] == false) {
					Single[3] = !Single[3];
					one_startDay = (1 << 3);
					setDayStartSelected(3);
				} else if (Single[3] == true) {
					Single[3] = !Single[3];
					one_startDay = 0;
					setDayStartUnSelected(3);
				}
			}
			break;
		case R.id.day5:
			if (multi) {
				if (Multi[4] == false) {
					Multi[4] = !Multi[4];
					startDay |= (1 << 4);
					setDayMulStartSelected(4);
				} else if (Multi[4] == true) {
					Multi[4] = !Multi[4];
					startDay &= (~(1 << 4));
					setDayStartUnSelected(4);
				}
			} else {
				if (Single[4] == false) {
					Single[4] = !Single[4];
					one_startDay = (1 << 4);
					setDayStartSelected(4);
				} else if (Single[4] == true) {
					Single[4] = !Single[4];
					one_startDay = 0;
					setDayStartUnSelected(4);
				}
			}
			break;
		case R.id.day6:
			if (multi) {
				if (Multi[5] == false) {
					Multi[5] = !Multi[5];
					startDay |= (1 << 5);
					setDayMulStartSelected(5);
				} else if (Multi[5] == true) {
					Multi[5] = !Multi[5];
					startDay &= (~(1 << 5));
					setDayStartUnSelected(5);
				}
			} else {
				if (Single[5] == false) {
					Single[5] = !Single[5];
					one_startDay = (1 << 5);
					setDayStartSelected(5);
				} else if (Single[5] == true) {
					Single[5] = !Single[5];
					one_startDay = 0;
					setDayStartSelected(5);
				}
			}
			break;
		case R.id.day7:
			if (multi) {
				if (Multi[6] == false) {
					Multi[6] = !Multi[6];
					startDay |= (1 << 6);
					setDayMulStartSelected(6);
				} else if (Multi[6] == true) {
					Multi[6] = !Multi[6];
					startDay &= (~(1 << 6));
					setDayStartUnSelected(6);
				}
			} else {
				if (Single[6] == false) {
					Single[6] = !Single[6];
					one_startDay = (1 << 6);
					setDayStartSelected(6);
				} else if (Single[6] == true) {
					Single[6] = !Single[6];
					one_startDay = 0;
					setDayStartUnSelected(6);
				}
			}
			break;
		case R.id.timerset_ok:
			onSave();
			break;
		case R.id.backBtnInTopLayout:
			finish();
			break;

		default:
			break;
		}

	}

	private void onSave() {
		// 保存数据

		for (i = 0; i <= 6; i++) {
			if (Multi[i] == false && Single[i] == false && Multi_s[i] == false
					&& Single_s[i] == false) {
				k++;
				break;
			}
		}
		if (k == 6) {
			Toast.makeText(ActivityGuideDevicesSocketSetTask.this, this.getResources().getText(R.string.devices_socket_choose_time),
					Toast.LENGTH_SHORT).show();
		}

		AutoTime autoTime = null;
		if (null != mInputAutoTime) {
			autoTime = mInputAutoTime;
		} else {
			autoTime = new AutoTime();
		}

		ArrayList<String> mDataStrList = new ArrayList<String>();
		String result_modify;
		if (multi) {
			startDay &= (~(1 << 7));
			stopDay &= (~(1 << 7));
			result_modify = isCheck + startDay + "#" + stopDay + "#" + open
					+ "#" + close;
		} else {
			one_startDay |= (1 << 7);
			one_stopDay |= (1 << 7);
			result_modify = isCheck + one_startDay + "#" + one_stopDay + "#"
					+ open + "#" + close;
		}
		if (mPosition >= 0 && mDataStrList != null && mDataStrList.size() > 0)
			mDataStrList.add(mPosition++, result_modify);
		else
			mDataStrList.add(result_modify);
		for (int i = 0; i < mDataStrList.size(); i++) {
			String[] taskparam = mDataStrList.get(i).split("#");
			autoTime.Enable = taskparam[0].equals("y");
			autoTime.DayStart = Integer.parseInt(taskparam[1]);
			autoTime.DayStop = Integer.parseInt(taskparam[2]);
			autoTime.TimeStart = Integer.parseInt(taskparam[3]);
			autoTime.TimeStop = Integer.parseInt(taskparam[4]);
		}
		BaseConfig configObj = null;
		if (mFunType == SOCKET_POWER) {
			PowerSocketAutoSwitch autoSwitch = (PowerSocketAutoSwitch) mFunDevice
					.getConfig(PowerSocketAutoSwitch.CONFIG_NAME);
			if (null == mInputAutoTime) {
				autoSwitch.getAutoTimes().add(autoTime);
			}
			configObj = autoSwitch;
		} else if (mFunType == SOCKET_LIGHT) {
			PowerSocketAutoLight autoLight = (PowerSocketAutoLight) mFunDevice
					.getConfig(PowerSocketAutoLight.CONFIG_NAME);
			if (null == mInputAutoTime) {
				autoLight.getAutoTimes().add(autoTime);
			}
			configObj = autoLight;
		} else if (mFunType == SOCKET_USB) {
			PowerSocketAutoUsb autoUsb = (PowerSocketAutoUsb) mFunDevice
					.getConfig(PowerSocketAutoUsb.CONFIG_NAME);
			if (null == mInputAutoTime) {
				autoUsb.getAutoTimes().add(autoTime);
			}
			configObj = autoUsb;
		}
		if (null != configObj) {
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
					configObj);
		}

	}

	private List<AutoTime> getAutoTimes() {
		if (mFunType == SOCKET_POWER) {
			PowerSocketAutoSwitch autoSwitch = (PowerSocketAutoSwitch) mFunDevice
					.getConfig(PowerSocketAutoSwitch.CONFIG_NAME);
			return autoSwitch.getAutoTimes();
		} else if (mFunType == SOCKET_LIGHT) {
			PowerSocketAutoLight autoLight = (PowerSocketAutoLight) mFunDevice
					.getConfig(PowerSocketAutoLight.CONFIG_NAME);
			return autoLight.getAutoTimes();
		} else if (mFunType == SOCKET_USB) {
			PowerSocketAutoUsb autoUsb = (PowerSocketAutoUsb) mFunDevice
					.getConfig(PowerSocketAutoUsb.CONFIG_NAME);
			return autoUsb.getAutoTimes();
		}
		return null;
	}

	@Override
	public void onDeviceLoginSuccess(FunDevice funDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceGetConfigSuccess(FunDevice funDevice,
			String configName, int nSeq) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		// TODO Auto-generated method stub
		if (PowerSocketAutoUsb.CONFIG_NAME.equals(configName)) {
			showWaitDialog();
			finish();
		}
		if (PowerSocketAutoLight.CONFIG_NAME.equals(configName)) {
			showWaitDialog();
			finish();

		}
		if (PowerSocketAutoSwitch.CONFIG_NAME.equals(configName)) {
			showWaitDialog();
			finish();
		}
	}

	@Override
	public void onDeviceSetConfigFailed(FunDevice funDevice, String configName,
			Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceChangeInfoSuccess(FunDevice funDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceChangeInfoFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceOptionSuccess(FunDevice funDevice, String option) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceOptionFailed(FunDevice funDevice, String option,
			Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice,
			H264_DVR_FILE_DATA[] datas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

}
