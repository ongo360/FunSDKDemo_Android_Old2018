package com.lib.funsdk.support;

/**
 * Created by Jeff on 16/4/12.
 */
public interface OnFunGetUserInfoListener extends OnFunListener {

    /**
     * 获取用户信息成功时回调该方法
     * @param strUserInfo 包含服务器返回的用户信息的字符串
     */
    void onGetUserInfoSuccess(String strUserInfo);

    /**
     * 获取用户信息失败时回调该方法
     * @param errCode 服务器返回的失败代码
     */
    void onGetUserInfoFailed(int errCode);

    /**
     * 登出成功
     */
    void onLogoutSuccess();

    /**
     * 登出失败
     * @param errCode 服务器返回的错误代码
     */
    void onLogoutFailed(int errCode);

}
