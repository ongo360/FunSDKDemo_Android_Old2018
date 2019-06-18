package com.example.funsdkdemo.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funsdkdemo.R;
import com.example.funsdkdemo.utils.XUtils;

import java.util.List;

/**
 * Created by zhangyongyong on 2017-09-19-15:45.
 */

public class WifiListAdapter extends BaseAdapter {


    private List<ScanResult> mScanResults;
    private Context mContext;

    public WifiListAdapter(List<ScanResult> scanResults, Context context) {
        mScanResults = scanResults;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mScanResults != null ? mScanResults.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mScanResults != null ? mScanResults.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.config_network_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.wifi_SSID);
            holder.single = (ImageView) convertView.findViewById(R.id.wifi_single);
            holder.lock = (ImageView) convertView.findViewById(R.id.wifi_lock);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult scanResult = mScanResults.get(position);
        holder.name.setText(scanResult.SSID);
        if (XUtils.getCapabilities(scanResult.capabilities) == 0) {
            holder.lock.setBackground(null);
        } else {
            holder.lock.setBackgroundResource(R.drawable.wifi_lock);
        }
        if (Math.abs(scanResult.level) > 100) {
            //holder.single.setImageDrawable(mContext.getResources().getDrawable(R.drawable.wifi_ss_0));
            holder.single.setImageResource(R.drawable.wifi_ss_0);
        } else if (Math.abs(scanResult.level) > 80) {
            //holder.single.setImageDrawable(mContext.getResources().getDrawable(R.drawable.wifi_ss_1));
            holder.single.setImageResource(R.drawable.wifi_ss_1);
        } else if (Math.abs(scanResult.level) > 70) {
            //holder.single.setImageDrawable(mContext.getResources().getDrawable(R.drawable.wifi_ss_1));
            holder.single.setImageResource(R.drawable.wifi_ss_1);
        } else if (Math.abs(scanResult.level) > 60) {
            //holder.single.setImageDrawable(mContext.getResources().getDrawable(R.drawable.wifi_ss_2));
            holder.single.setImageResource(R.drawable.wifi_ss_2);
        } else if (Math.abs(scanResult.level) > 50) {
            //holder.single.setImageDrawable(mContext.getResources().getDrawable(R.drawable.wifi_ss_3));
            holder.single.setImageResource(R.drawable.wifi_ss_3);
        } else {
            //holder.single.setImageDrawable(mContext.getResources().getDrawable(R.drawable.wifi_ss_4));
            holder.single.setImageResource(R.drawable.wifi_ss_4);
        }
        return convertView;
    }

    public void updateData(List<ScanResult> scanResults) {
        this.mScanResults = scanResults;
        notifyDataSetChanged();

    }

    class ViewHolder {
        TextView name;
        ImageView single, lock;
    }


}
