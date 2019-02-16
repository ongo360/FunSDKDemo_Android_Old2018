package com.lib.funsdk.support;

public interface OnFunLoginListener extends OnFunListener {

	// 用户登录成功
	void onLoginSuccess();
	
	// 用户登录失败
	void onLoginFailed(final Integer errCode);
	
	// 用户登出
	void onLogout();
}
