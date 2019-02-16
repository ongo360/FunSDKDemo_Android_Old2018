package com.example.funsdkdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapterGuideModule extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<DemoModule> mListModules;
	
	public ListAdapterGuideModule(Context context, List<DemoModule> modules) {
		mInflater = LayoutInflater.from(context);
		mListModules = modules;
	}
	
	@Override
	public int getCount() {
		return mListModules.size();
	}

	@Override
	public Object getItem(int position) {
		return mListModules.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_guide_list_item, null);

            holder = new ViewHolder();
            holder.imgViewIcon = (ImageView) convertView.findViewById(R.id.imgItemIcon);
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtItemTitle);
            holder.txtViewDesc = (TextView) convertView.findViewById(R.id.txtItemDesc);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        DemoModule module = mListModules.get(position);
        
        if ( module.iconResId > 0 ) {
        	holder.imgViewIcon.setImageResource(module.iconResId);
        } else {
        	holder.imgViewIcon.setImageResource(R.drawable.icon_funsdk);
        }
        
        if ( module.titleStrId > 0 ) {
        	holder.txtViewTitle.setText(module.titleStrId);
        } else {
        	holder.txtViewTitle.setText("");
        }
        
        if ( module.descStrId > 0 ) {
        	holder.txtViewDesc.setText(module.descStrId);
        	holder.txtViewDesc.setVisibility(View.VISIBLE);
        } else {
        	holder.txtViewDesc.setText("");
        	holder.txtViewDesc.setVisibility(View.GONE);
        }
        
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imgViewIcon;
		TextView txtViewTitle;
		TextView txtViewDesc;
	}
}
