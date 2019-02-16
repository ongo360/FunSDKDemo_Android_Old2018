package com.example.funsdkdemo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.funsdkdemo.R;

import java.util.ArrayList;

/**
 * Created by Jeff on 4/19/16.
 */
public class SocketCaptureAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> imgList;
    GridView mGridView;

    public SocketCaptureAdapter(Context context, ArrayList<String> imgList, GridView gv) {
        this.context = context;
        this.imgList = imgList;
        mGridView = gv;
    }

    public void addImg(String imgStr){
        imgList.add(imgStr);
        notifyDataSetChanged();
    }

    public void removeImg(String imgStr) {
        imgList.remove(imgStr);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return imgList == null ? 0 : imgList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_device_socket_capture_img, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.img_iv = (ImageView) convertView.findViewById(R.id.img_iv);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.img_iv.setImageBitmap(BitmapFactory.decodeFile((String)getItem(position)));


        return convertView;
    }

    class ViewHolder {
        ImageView img_iv;
    }
}
