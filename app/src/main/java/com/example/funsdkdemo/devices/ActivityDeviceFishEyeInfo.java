package com.example.funsdkdemo.devices;

import org.json.JSONObject;

import com.example.funsdkdemo.R;
import com.example.funsdkdemo.R.id;
import com.example.funsdkdemo.R.layout;
import com.example.funsdkdemo.R.string;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityDeviceFishEyeInfo extends Activity implements OnClickListener{

	
	private TextView mTextInfo = null;
	private TextView mTextVersion = null;
	private ImageButton mBtnBack = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_fisheye_info);
		
		mTextInfo = (TextView)findViewById(R.id.txtInfo);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		String fishInfo = getIntent().getStringExtra("FISH_EYE_INFO");
		if ( null != fishInfo ) {
			try {
				JSONObject json = new JSONObject(fishInfo);
				String infoTxt = String.format(" SN : %s\n\n center x : %d\n cneter y : %d\n radius : %d\n\n image w : %d\n image h : %d\n", 
						getIntent().getStringExtra("DEVICE_SN"),
						json.getInt("x"), json.get("y"), json.getInt("r"), json.getInt("w"), json.getInt("h"));
				mTextInfo.setText(infoTxt);
			} catch (Exception e) {
				mTextInfo.setText("Error FishEye Infomation.");
			}
		} else {
			mTextInfo.setText("No FishEye Infomation.");
		}
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
