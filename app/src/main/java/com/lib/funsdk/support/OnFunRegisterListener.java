package com.lib.funsdk.support;

public interface OnFunRegisterListener extends OnFunListener {

	// 请求发送验证码成功
	void onRequestSendCodeSuccess();
	
	// 请求发送验证码失败
	void onRequestSendCodeFailed(final Integer errCode);
	
	// 注册用户成功
	void onRegisterNewUserSuccess();
	
	// 注册用户失败
	void onRegisterNewUserFailed(final Integer errCode);
	
	//用户名可用
	void onUserNameFine();
	//用户名不可用
	void onUserNameUnfine(final Integer errCode);
}
