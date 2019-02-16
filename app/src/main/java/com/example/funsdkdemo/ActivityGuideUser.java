package com.example.funsdkdemo;

import java.util.ArrayList;
import java.util.List;

import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunLoginType;

import android.os.Bundle;

public class ActivityGuideUser extends ActivityGuide {

	private static List<DemoModule> mGuideModules = new ArrayList<DemoModule>();
	
	static {
		
		// 1.1用户注册
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_user_register, 
				-1, 
				ActivityGuideUserRegister.class));
		
		// 1.2.用户登录
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_user_login, 
				-1, 
				ActivityGuideUserLogin.class));
		
		// 1.3.修改密码
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_user_change_passwd, 
				-1, 
				ActivityGuideUserChangePassw.class));
			
		// 1.4.忘记密码
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_user_forgot_passwd, 
				-1, 
				ActivityGuideUserForgetPassw.class));
		
		// 1.5.用户信息
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_user_info,
				-1, 
				ActivityGuideUserInfo.class));
//		 1.6.设备密码本地保存
		mGuideModules.add(new DemoModule(-1,
				R.string.guide_module_title_user_debug, 
				-1, 
				ActivityGuideUserDebug.class));
		
	}
	
	@Override
	protected List<DemoModule> getGuideModules() {
		return mGuideModules;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 用户相关的操作,必须切换网络访问方式
		FunSupport.getInstance().setLoginType(FunLoginType.LOGIN_BY_INTENTT);
	}

	
}
