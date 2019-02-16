package com.example.funsdkdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.funsdkdemo.R;
import com.lib.funsdk.support.widget.FunVideoView;

public class GridCameraChannelsPreviewsAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private int mchannels;
	
	public GridCameraChannelsPreviewsAdapter(Context context, int channels) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		mchannels = channels;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("TTT---->>> count");
		return mchannels;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		//return mList.get(position);
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout view = null;
		ChannelsItem channelsItem = new ChannelsItem();
		if ( null == convertView ) {
			if (inflater != null) {
				System.out.println("TTT---->>> new");
				view = (LinearLayout) inflater.inflate(R.layout.layout_channelspreview_list_item, null);
				
				channelsItem.textView = (TextView) view.findViewById(R.id.textVideoStat1);
				channelsItem.funVideoView = (FunVideoView) view.findViewById(R.id.funVideoView1);
			}
		} else {
			view = (LinearLayout) convertView;
		}
		
		view.setTag(position);
		
		return view;
	}
	
	public final class ChannelsItem{
		
		public TextView textView;
		public FunVideoView funVideoView;
		
	}
	
}
