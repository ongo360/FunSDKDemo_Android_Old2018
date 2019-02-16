package com.example.common;



import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.method.DialerKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.funsdkdemo.R;

public abstract class DialogInputPasswd {
	Context context;
	AlertDialog tip;
	public TextView mTextTitle;
	TextView mTextConfirm;
	TextView mTextCancel;
	RelativeLayout mLayoutTipShow;
	LinearLayout mLayoutConfirm;
	LinearLayout mLayoutCancel;
	EditText mEditText;
	
	private final int MESSAGE_SHOW_IM = 0x102;

	public DialogInputPasswd(Context context, String titleRes, String editText, int confirmRes,
			int cancelRes) {
		this.context = context;
		
		tip = new android.app.AlertDialog.Builder(context).create();
		tip.requestWindowFeature(Window.FEATURE_NO_TITLE);
		tip.setCanceledOnTouchOutside(true);
		tip.show();

		View view = LayoutInflater.from(context).inflate(R.layout.tip_input_passwd, null);

		mTextTitle = (TextView) view.findViewById(R.id.title);
		mTextConfirm = (TextView) view.findViewById(R.id.confirm);
		mTextCancel = (TextView) view.findViewById(R.id.cancel);
		mLayoutTipShow = (RelativeLayout) view.findViewById(R.id.tipShow);
		mLayoutConfirm = (LinearLayout) view.findViewById(R.id.confirmLayout);
		mLayoutCancel = (LinearLayout) view.findViewById(R.id.cancelLayout);
		mEditText = (EditText) view.findViewById(R.id.editPasswd);
//		mEditText.setKeyListener(DialerKeyListener.getInstance());
		
		mTextTitle.setText(titleRes);
		mTextConfirm.setText(confirmRes);
		mTextCancel.setText(cancelRes);

		RelativeLayout.LayoutParams pointLp = (RelativeLayout.LayoutParams) mLayoutTipShow
				.getLayoutParams();
		pointLp.width = UIFactory.dip2px(context, 270);
		pointLp.height = UIFactory.dip2px(context, 200);
		mLayoutTipShow.setLayoutParams(pointLp);

		mLayoutConfirm.setOnClickListener(confirmListener);
		mLayoutCancel.setOnClickListener(cancelListener);

		Window window = tip.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.setContentView(view);
		
		tip.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); 
		tip.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}
	


	OnClickListener confirmListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			tryConfirm();
		}
	};
	
	private void tryConfirm() {
		String passwdStr = mEditText.getText().toString();
		
		if ( !confirm(passwdStr) ) {
			mEditText.setText("");
		}
	}

	OnClickListener cancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			cancel();
		}
	};

	public void show() {
		tip.show();
		
		mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_IM, 100);
	}

	public void hide() {
		tip.dismiss();
	}

	public boolean confirm(String editText) {
		hide();
		return true;
	}

	public void cancel() {
		hide();
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_SHOW_IM:
				{
					mEditText.requestFocus();
					InputMethodManager imm = (InputMethodManager) DialogInputPasswd.this.context.getSystemService(Context.INPUT_METHOD_SERVICE);  
					imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
				}
				break;
			}
		}
		
	};
}
