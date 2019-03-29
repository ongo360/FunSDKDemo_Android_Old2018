package com.example.funsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunLoginListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityGuideMain extends ActivityGuide implements OnFunLoginListener {

	private static List<DemoModule> mGuideModules = new ArrayList<DemoModule>();
	
	private Button mBtnUserStat = null;
	
	static {
		
		// 1.用户相关
		mGuideModules.add(new DemoModule(R.drawable.icon_user, 
				R.string.guide_module_title_user, 
				R.string.guide_module_desc_user, 
				ActivityGuideUser.class));
		
		// 2.设备相关
		mGuideModules.add(new DemoModule(R.drawable.icon_device, 
				R.string.guide_module_title_device, 
				R.string.guide_module_desc_device, 
				ActivityGuideDevice.class));
		
		// 3.媒体功能
		mGuideModules.add(new DemoModule(R.drawable.icon_media, 
				R.string.guide_module_title_media, 
				R.string.guide_module_desc_media, 
				ActivityGuideMedia.class));
		
		// 4.编码转码
		mGuideModules.add(new DemoModule(R.drawable.icon_transcode, 
				R.string.guide_module_title_trancode, 
				R.string.guide_module_desc_trancode, 
				ActivityGuideTranscode.class));
		
		// 5.关于
		mGuideModules.add(new DemoModule(R.drawable.icon_funsdk, 
				R.string.guide_module_title_about, 
				R.string.guide_module_desc_about, 
				ActivityAbout.class));
	}
	
	@Override
	protected List<DemoModule> getGuideModules() {
		return mGuideModules;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View navRight = setNavagateRightButton(R.layout.button_user_stat, 
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		mBtnUserStat = (Button)navRight.findViewById(R.id.btnUserStat);
		mBtnUserStat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( FunSupport.getInstance().hasLogin() ) {
					// 如果已经登录,显示用户详情界面
					enterUserInfo();
				} else {
					// 如果未登录,进入登录界面
					enterLoginActivity();
				}
			}
		});
		
		FunSupport.getInstance().registerOnFunLoginListener(this);
		
		refreshLoginStat(FunSupport.getInstance().hasLogin());
	}

	@Override
	protected void onDestroy() {
		
		FunSupport.getInstance().removeOnFunLoginListener(this);
		((MyApplication)getApplication()).exit();
		super.onDestroy();
	}

	@Override
	public void onLoginSuccess() {
		refreshLoginStat(true);
	}

	@Override
	public void onLoginFailed(Integer errCode) {
		refreshLoginStat(false);
	}

	@Override
	public void onLogout() {
		refreshLoginStat(false);
	}

	private void refreshLoginStat(boolean hasLogin) {
		if ( null != mBtnUserStat ) {
			if ( hasLogin ) {
				mBtnUserStat.setText(FunSupport.getInstance().getUserName());
			} else {
				mBtnUserStat.setText(R.string.user_not_login);
			}
		}
	}
	
	private void enterLoginActivity() {
		Intent intent = new Intent();
		intent.setClass(this, ActivityGuideUserLogin.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	private void enterUserInfo() {
		Intent intent = new Intent();
		intent.setClass(this, ActivityGuideUserInfo.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
