package com.example.common;



import java.util.ArrayList;
import java.util.List;

import com.example.funsdkdemo.R;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class DialogSavedUsers implements OnItemClickListener {
	
	public interface OnSavedUserSelectListener {
		void onSavedUserSelected(String userName);
	}

	private Dialog mDialog = null;
	
	private ListView mListView = null;
	private List<String> mUserNames = new ArrayList<String>();
	private OnSavedUserSelectListener mListener = null;
	
	public DialogSavedUsers(Context context, 
			List<String> userNames,
			OnSavedUserSelectListener l) {
		mDialog = new Dialog(context, R.style.dialog_translucent);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mDialog.setContentView(R.layout.dialog_saved_users);
		
		mListener = l;
		
		mUserNames.clear();
		mUserNames.addAll(userNames);
		
		mListView = (ListView)mDialog.findViewById(R.id.listSavedUsers);
		mListView.setAdapter(new SavedUserAdapter(context));
		mListView.setOnItemClickListener(this);
		
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		Window mWindow = mDialog.getWindow();
		WindowManager.LayoutParams lpWin = mWindow.getAttributes();
		lpWin.width = displayMetrics.widthPixels/2;
		lpWin.height = displayMetrics.heightPixels/2;
		mWindow.setAttributes(lpWin);
		
		mDialog.setCanceledOnTouchOutside(true);
	}
	
	
	public void show() {
		mDialog.show();
	}
	
	public void dismiss() {
		mDialog.dismiss();
	}

	private class SavedUserAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
        
        public SavedUserAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }
        
		@Override
		public int getCount() {
			return mUserNames.size();
		}

		@Override
		public Object getItem(int position) {
			return mUserNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
          	
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.dialog_saved_users_list_item, null);
                holder = new ViewHolder();
                holder.txtUserName = (TextView) convertView.findViewById(R.id.textUserName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            holder.txtUserName.setText(mUserNames.get(position));
            
            return convertView;
        }

        public class ViewHolder {
            TextView txtUserName;
        }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if ( position >= 0 && position < mUserNames.size() ) {
			if ( null != mListener ) {
				mListener.onSavedUserSelected(mUserNames.get(position));
				dismiss();
			}
		}
	}
}
