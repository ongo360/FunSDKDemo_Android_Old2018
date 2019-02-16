package com.example.funsdkdemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityAbout extends Activity implements OnClickListener{

	
	private TextView mTextTitle = null;
	private TextView mTextVersion = null;
	private ImageButton mBtnBack = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		mTextVersion = (TextView)findViewById(R.id.txtVersion);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.guide_module_title_about);
		mTextVersion.setText("Version: " + getVersionName(this));
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if ( v.getId() == mBtnBack.getId() ) {
			finish();
		}
	}
	
	public String getVersionName(Context context) {
	    return getPackageInfo(context).versionName;
	}
	 
	public int getVersionCode(Context context) {
	    return getPackageInfo(context).versionCode;
	}
	 
	private PackageInfo getPackageInfo(Context context) {
	    PackageInfo pi = null;
	 
	    try {
	        PackageManager pm = context.getPackageManager();
	        pi = pm.getPackageInfo(context.getPackageName(),
	                PackageManager.GET_CONFIGURATIONS);
	 
	        return pi;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	 
	    return pi;
	}
}
