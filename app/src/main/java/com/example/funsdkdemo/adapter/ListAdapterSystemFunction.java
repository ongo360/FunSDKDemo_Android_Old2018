package com.example.funsdkdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funsdkdemo.R;
import com.lib.funsdk.support.config.SystemFunction;

public class ListAdapterSystemFunction extends BaseExpandableListAdapter {
	
	
	private Context mContext = null;
	private LayoutInflater mInflater;
	
	private List<SystemFunction.FunctionAttr> mSystemFuncs = new ArrayList<SystemFunction.FunctionAttr>();
	
	
	public ListAdapterSystemFunction(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public void setSystemFunctions(List<SystemFunction.FunctionAttr> funcAttrs) {
		mSystemFuncs.clear();
		mSystemFuncs.addAll(funcAttrs);
		notifyDataSetInvalidated();
	}
	
	public Object getChild(int groupPosition, int childPosition) {
		return mSystemFuncs.get(groupPosition).funcs.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return mSystemFuncs.get(groupPosition).funcs.size();
	}
	
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(
					R.layout.layout_device_system_function_list_item_more, null);

			holder = new ChildViewHolder();
			
			holder.txtAttrName = (TextView) convertView
					.findViewById(R.id.txtAttrName);
			holder.txtAttrValue = (TextView) convertView
					.findViewById(R.id.txtAttrValue);
			
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}

		SystemFunction.FunctionAttr func = mSystemFuncs.get(groupPosition);
		SystemFunction.FunctionItem funcAttr = func.funcs.get(childPosition);
		
		holder.txtAttrName.setText(funcAttr.attrName + ": ");
		holder.txtAttrValue.setText(Boolean.toString(funcAttr.isSupport));
		
		return convertView;
	}

	// group method stub
	public Object getGroup(int groupPosition) {
		return mSystemFuncs.get(groupPosition);
	}

	public int getGroupCount() {
		return mSystemFuncs.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_device_system_function_list_item,
					null);

			holder = new GroupViewHolder();
			holder.txtFuncName = (TextView) convertView
					.findViewById(R.id.txtFuncName);
			holder.imgArrowIcon = (ImageView) convertView
					.findViewById(R.id.imgArrowIcon);

			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}

		SystemFunction.FunctionAttr func = mSystemFuncs.get(groupPosition);

		holder.txtFuncName.setText(func.name);
		
		if ( isExpanded ) {
			holder.imgArrowIcon.setImageResource(R.drawable.icon_arrow_up);
		} else {
			holder.imgArrowIcon.setImageResource(R.drawable.icon_arrow_down);
		}
		
		return convertView;
	}

	private class GroupViewHolder {
		TextView txtFuncName;
		ImageView imgArrowIcon;
	}

	private class ChildViewHolder {
		TextView txtAttrName;
		TextView txtAttrValue;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}
