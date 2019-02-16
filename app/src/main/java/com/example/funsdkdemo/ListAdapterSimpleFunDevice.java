package com.example.funsdkdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.funsdk.support.models.FunDevStatus;
import com.lib.funsdk.support.models.FunDevice;

public class ListAdapterSimpleFunDevice extends BaseAdapter {
	
	private Context mContext = null;
	private LayoutInflater mInflater;
	private List<FunDevice> mListDevs = new ArrayList<FunDevice>();

	public ListAdapterSimpleFunDevice(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public void updateDevice(List<FunDevice> devList) {
		mListDevs.clear();
		mListDevs.addAll(devList);
		this.notifyDataSetInvalidated();
	}
	
	public FunDevice getFunDevice(int position) {
		return (FunDevice)getItem(position);
	}
	
	@Override
	public Object getItem(int position) {
		if ( position >= 0 && position < mListDevs.size() ) {
			return mListDevs.get(position);
		}
		return null;
	}

	@Override
	public int getCount() {
		return mListDevs.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int groupPosition,
			View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_device_list_item,
					null);

			holder = new ViewHolder();
			holder.imgDevIcon = (ImageView) convertView
					.findViewById(R.id.imgDevIcon);
			holder.txtDevName = (TextView) convertView
					.findViewById(R.id.txtDevName);
			holder.txtDevStatus = (TextView) convertView
					.findViewById(R.id.txtDevStatus);
			holder.imgArrowIcon = (ImageView) convertView
					.findViewById(R.id.imgArrowIcon);
			holder.imgArrowIcon.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		FunDevice funDevice = mListDevs.get(groupPosition);
		
		holder.imgDevIcon.setImageResource(funDevice.devType.getDrawableResId());
		holder.txtDevName.setText(funDevice.getDevName());
		
		holder.txtDevName.setTextColor(mContext.getResources().getColorStateList(R.drawable.common_title_color));
		
		holder.txtDevStatus.setText(funDevice.devStatus.getStatusResId());
		if ( funDevice.devStatus == FunDevStatus.STATUS_ONLINE ) {
			holder.txtDevStatus.setTextColor(0xff177fca);
		} else if ( funDevice.devStatus == FunDevStatus.STATUS_OFFLINE ) {
			holder.txtDevStatus.setTextColor(0xffda202e);
		} else {
			holder.txtDevStatus.setTextColor(mContext.getResources().getColor(R.color.demo_desc));
		}
		
		return convertView;
	}

	private class ViewHolder {
		ImageView imgDevIcon;
		TextView txtDevName;
		TextView txtDevStatus;
		ImageView imgArrowIcon;
	}

}
