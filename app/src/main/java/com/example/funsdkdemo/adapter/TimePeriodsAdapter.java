package com.example.funsdkdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.funsdkdemo.R;
import com.example.funsdkdemo.entity.TimeItem;

import java.util.ArrayList;
import java.util.List;

public class TimePeriodsAdapter extends BaseAdapter {
	private Context mContext;
	private List<TimeItem> timeList;
	private LayoutInflater inflater;
	public int mSelectedpos = -1;//

	public TimePeriodsAdapter(Context context, ArrayList<TimeItem> timeList) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.timeList = timeList;
	}

	@Override
	public int getCount() {
		return null == timeList ? 0 : timeList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return timeList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.show_timetask_item, parent,
					false);
			holder.tvLabel = (TextView) convertView.findViewById(R.id.tv_label);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvDate = (TextView) convertView
					.findViewById(R.id.date_state);
			holder.tvOpenState = (TextView) convertView
					.findViewById(R.id.tv_open);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final TimeItem info = timeList.get(position);
		holder.tvTime.setText(info.getTime());
		holder.tvDate.setText(info.getWeeks(mContext));
		holder.tvLabel.setText(mContext.getString(R.string.time_slot)
				+ String.valueOf(position + 1));
		if (info.isOpen()) {
			holder.tvOpenState.setText(R.string.already_open);
		} else {
			holder.tvOpenState.setText(R.string.not_open);
		}
		return convertView;
	}

	public void setOnTimeInfoChangedListener(
			OnTimeInfoChangedListener mOnPushInfoChangedListener) {
	}

	class ViewHolder {
		TextView tvLabel;
		TextView tvTime;
		TextView tvDate;
		TextView tvOpenState;
	}

	public interface OnTimeInfoChangedListener {
		public void TimeInfoChanged(int position, View v);
	}

	public void update(int position, TimeItem data) {
		timeList.set(position, data);
		notifyDataSetChanged();
	}
}
