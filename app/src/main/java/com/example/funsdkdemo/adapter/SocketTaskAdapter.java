package com.example.funsdkdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.config.AutoTime;


public class SocketTaskAdapter extends BaseAdapter {
	private List<AutoTime> mDataList = new ArrayList<AutoTime>();
	private Context context;
	public SocketTaskAdapter(Context context, List<AutoTime> list) {
		this.context = context;
		this.DayStr = context.getResources().getStringArray(R.array.week_day);
		
		if ( null != list ) {
			mDataList.clear();
			mDataList.addAll(list);
		}
	}
	
	public List<AutoTime> getmDataList() {
		return mDataList;
	}

	public void setmDataList(List<AutoTime> mDataList) {
		this.mDataList = mDataList;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public AutoTime getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public boolean removeItem(int position){
		if(mDataList.get(position) != null){
			mDataList.remove(position);
			notifyDataSetChanged();
			return true;
		} else {
			return false;
		}
	}

	private String DayStr[] = null;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.activity_socket_task_item, parent, false);
			mViewHolder.task_open_time = (TextView) convertView
					.findViewById(R.id.task_open_time);
			mViewHolder.task_close_time = (TextView) convertView
					.findViewById(R.id.task_close_time);
			mViewHolder.task_switch = (ImageView) convertView
					.findViewById(R.id.task_switch);
			mViewHolder.task_open_day = (TextView) convertView
					.findViewById(R.id.task_open_day);
			mViewHolder.task_close_day = (TextView) convertView
					.findViewById(R.id.task_close_day);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		AutoTime autoTime = getItem(position);
		String setTimeDayStart=  String.valueOf(autoTime.DayStart);
		String setTimeDayStop =  String.valueOf(autoTime.DayStop);
		String setTimeStart   =  String.valueOf(autoTime.TimeStart);
		String setTimeStop    =  String.valueOf(autoTime.TimeStop);
		if (setTimeStart.length() == 1)
			setTimeStart="00:0" + setTimeStart;
		else if (setTimeStart.length() == 2)
			setTimeStart="00:" + setTimeStart;
		else
			setTimeStart=setTimeStart.substring(0,setTimeStart.length() - 2)+":"+setTimeStart.substring(setTimeStart.length()-2);
		if (setTimeStop.length() == 1)
			setTimeStop=setTimeStop;
		else if (setTimeStop.length() == 2)
			setTimeStop="00:" + setTimeStop;
		else
			setTimeStop=setTimeStop.substring(0,setTimeStop.length() - 2)+":"+setTimeStop.substring(setTimeStop.length()-2);
		
		setTimeDayStart = "";
		
		for ( int i = 0; i < DayStr.length; i ++ ) {
			if ( ((autoTime.DayStart >> i) & 0x01) == 0x01) {
				if ( setTimeDayStart.length() > 0 ) {
					setTimeDayStart += ", ";
				}
				setTimeDayStart += DayStr[i];
			}
		}
		
		setTimeDayStop = "";
		for ( int i = 0; i < DayStr.length; i ++ ) {
			if ( ((autoTime.DayStop >> i) & 0x01) == 0x01) {
				if ( setTimeDayStop.length() > 0 ) {
					setTimeDayStop += ", ";
				}
				setTimeDayStop += DayStr[i];
			}
		}
		
		mViewHolder.task_close_time.setText(convertView.getResources().getString(R.string.dev_socket_data_closetime)+ setTimeStop);
		mViewHolder.task_open_time.setText(convertView.getResources().getString(R.string.dev_socket_data_begin)+ setTimeStart);
		mViewHolder.task_open_day.setText(setTimeDayStart);
		mViewHolder.task_close_day.setText(setTimeDayStop);
		if (autoTime.Enable)
			mViewHolder.setColor(context.getResources().getColor(
					R.color.color_gray_open),context.getResources().getColor(
							R.color.color_gray_light_open));
		else
			mViewHolder.setColor(context.getResources().getColor(
					R.color.color_gray_close),context.getResources().getColor(
							R.color.color_gray_close));
		return convertView;
		
	}

	String dayString(String str) {
		StringBuffer temp = new StringBuffer(str);
		if (temp.length() > 9) {
			if (temp.length() == 20) {
				temp.replace(0, temp.length(), "Everyday");
			} else {
				temp.delete(8, 9);
				temp.insert(8, "\n");
			}
		}
		return temp.toString();
	}

	public void setDataList(List<AutoTime> list) {
		mDataList.clear();
		if ( null != list ) {
			mDataList.addAll(list);
		}
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}

	class ViewHolder {
		TextView task_open_time, task_close_time, task_open_day,
				task_close_day;
		ImageView task_switch;

		void setColor(int color,int lightColor) {
			task_open_time.setTextColor(color);
			task_close_time.setTextColor(color);
			task_open_day.setTextColor(lightColor);
			task_close_day.setTextColor(lightColor);
		}
	}

}
