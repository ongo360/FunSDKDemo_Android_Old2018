package com.example.funsdkdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.download.PictureDownload;
import com.example.download.PictureDownload.OnPictureDownloadListener;
import com.lib.funsdk.support.config.AlarmInfo;
import com.lib.funsdk.support.utils.StringUtils;

public class ListAdapterDeviceAlarmInfo extends BaseAdapter implements OnPictureDownloadListener {
	
	private Context mContext = null;
	private LayoutInflater mInflater;
	private List<AlarmInfo> mListAlarmInfo = new ArrayList<AlarmInfo>();
	
	private PictureDownload mPicDownload = null;
	
	private List<ViewHolder> mViewHolders = new ArrayList<ViewHolder>();
	
	private final int MESSAGE_PICTURE_DOWNLOADED = 0x100;
	

	public ListAdapterDeviceAlarmInfo(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		
		// 同时3个线程下载
		mPicDownload = new PictureDownload(3);
		mPicDownload.setOnPictureDownloadListener(this);
	}
	
	public void release() {
		if ( null != mPicDownload ) {
			mPicDownload.release();
			mPicDownload = null;
		}
		mViewHolders.clear();
	}
	
	public AlarmInfo getAlarmInfo(int position) {
		return (AlarmInfo)getItem(position);
	}
	
	public void updateAlarmInfos(List<AlarmInfo> devList) {
		mListAlarmInfo.clear();
		mListAlarmInfo.addAll(devList);
		this.notifyDataSetInvalidated();
	}
	
	@Override
	public Object getItem(int position) {
		if ( position >= 0 && position < mListAlarmInfo.size() ) {
			return mListAlarmInfo.get(position);
		}
		return null;
	}

	@Override
	public int getCount() {
		return mListAlarmInfo.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position,
			View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_device_alarm_list_item,
					null);
			
			holder = new ViewHolder();
			holder.imgAlarmPicture = (ImageView) convertView
					.findViewById(R.id.imgAlarmPicture);
			holder.txtAlarmTime = (TextView) convertView
					.findViewById(R.id.txtAlarmTime);
			holder.txtAlarmEvent = (TextView) convertView
					.findViewById(R.id.txtAlarmEvent);
			holder.txtAlarmStatus = (TextView) convertView
					.findViewById(R.id.txtAlarmStatus);
			convertView.setTag(holder);
			
			mViewHolders.add(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if ( holder.position != position ) {
			
			// 老的图片可以不用下载了
			if ( holder.position >= 0 ) {
				mPicDownload.drop(holder.position);
			}
			
			// 设置当前新的索引
			holder.position = position;
		}
		
		AlarmInfo alarmInfo = mListAlarmInfo.get(position);
		
		holder.txtAlarmTime.setText(alarmInfo.getTime());
		holder.txtAlarmEvent.setText(alarmInfo.getEvent());
		holder.txtAlarmStatus.setText(alarmInfo.getStatus());
		
		String picUrl = alarmInfo.getPic();
		
		Log.d("test", "[" + position + "] picUrl = " + picUrl);
		
		if ( StringUtils.isStringNULL(picUrl) ) {
			// 图片地址是有效的
			setImageViewPicture(holder.imgAlarmPicture,
					position, picUrl, null);
		} else {
			// 无效的图片地址,显示图片不可用标志
			holder.imgAlarmPicture.setImageResource(R.drawable.icon_device);
		}
		
		return convertView;
	}
	
	private void setImageViewPicture(ImageView iv, int position, String picUrl, Bitmap bmp) {
		if ( null == bmp && null != picUrl ) {
			bmp = mPicDownload.get(position, picUrl);
		}
		
		if ( null != bmp ) {
			iv.setImageBitmap(bmp);
		} else {
			// 显示默认图片
			iv.setImageResource(R.drawable.icon_device);
			
			// 添加图片下载
			mPicDownload.add(position, picUrl);
		}
	}
	
	private ViewHolder findViewHolder(int position) {
		for ( int i = 0; i < mViewHolders.size(); i ++ ) {
			if ( mViewHolders.get(i).position == position ) {
				return mViewHolders.get(i);
			}
		}
		return null;
	}
	
	private class ViewHolder {
		ImageView imgAlarmPicture;
		TextView txtAlarmTime;
		TextView txtAlarmEvent;
		TextView txtAlarmStatus;
		
		int position = -1;
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_PICTURE_DOWNLOADED:
				{
					int position = msg.arg1;
					ViewHolder vh = findViewHolder(position);
					if ( null != vh ) {
						setImageViewPicture(vh.imgAlarmPicture, 
								position, null, (Bitmap)msg.obj);
					}
				}
				break;
			}
		}
		
	};

	@Override
	public void onPictureDownload(Integer position, Bitmap bmp) {
		if ( null != mHandler ) {
			Message msg = new Message();
			msg.what = MESSAGE_PICTURE_DOWNLOADED;
			msg.arg1 = position;
			msg.obj = bmp;
			mHandler.sendMessage(msg);
		}
	}

}
