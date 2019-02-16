package com.example.funsdkdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunFileData;

import java.util.List;

/**
 * Created by Administrator on 2017-03-21.
 * Discription:
 */

public class FileDownloadListAdapter extends BaseAdapter{

    private Context mContext;
    private FunDevice mFunDevice;
    private List<?> mLists;

    public FileDownloadListAdapter(Context context, FunDevice mFunDevice, List<FunFileData> files) {
    }

    public void FileDownloadListAdapter(Context context, FunDevice funDevice, List<FunFileData> list){
        this.mContext = context;
        this.mFunDevice = funDevice;
        this.mLists = list;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
