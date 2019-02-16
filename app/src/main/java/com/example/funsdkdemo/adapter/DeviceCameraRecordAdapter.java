package com.example.funsdkdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.models.FunDevRecordFile;

import java.util.List;

/**
 * Created by Jeff on 16/5/20.
 */
public class DeviceCameraRecordAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<FunDevRecordFile> recordFiles;
    private int mPlayingIndex = -1;

    public DeviceCameraRecordAdapter(Context context, List<FunDevRecordFile> files) {
        this.mContext = context;
        this.recordFiles = files;
        mInflater = LayoutInflater.from(mContext);
    }
    
    public FunDevRecordFile getRecordFile(int position) {
    	return (FunDevRecordFile)getItem(position);
    }

    public void setPlayingIndex(int index) {
    	mPlayingIndex = index;
    	notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return recordFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return recordFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (null == convertView) {
            convertView = mInflater.inflate(
                    R.layout.item_device_camera_record, parent, false);
            viewHolder = new DeviceCameraRecordAdapter.ViewHolder();
            viewHolder.ivRecordShot = (ImageView) convertView.findViewById(R.id.iv_record_shot);
            viewHolder.tvRecordTime = (TextView) convertView.findViewById(R.id.tv_record_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FunDevRecordFile recordFile = recordFiles.get(position);

        viewHolder.tvRecordTime.setText(recordFile.getRecStartTime() +
                " - " + recordFile.getRecEndTime());
        
        // 当前正在播放的高亮显示
        if ( mPlayingIndex == position ) {
        	viewHolder.tvRecordTime.setTextColor(0xffe97425);
        } else {
        	viewHolder.tvRecordTime.setTextColor(0xff636363);
        }

        return convertView;
    }


    class ViewHolder {

        ImageView ivRecordShot;
        TextView tvRecordTime;

    }
}
