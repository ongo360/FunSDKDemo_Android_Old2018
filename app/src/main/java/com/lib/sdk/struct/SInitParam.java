package com.lib.sdk.struct;

import com.basic.G;

public class SInitParam {
	public int st_0_nAppType;
	public byte[] st_1_nSource = new byte[64];
	public byte[] st_2_language = new byte[32]; //中文（zh）、英文（en）
	
	public SInitParam()
	{
		G.SetValue(st_1_nSource, "mobile");
		G.SetValue(st_2_language, "en");
	}
	
    public final static int LOGIN_TYPE_WEB = 0;		///< WEB登陆(默认)
    public final static int LOGIN_TYPE_LOCALUPGRADE = 1;	///< 升级工具(局域网升级)登陆
    public final static int LOGIN_TYPE_CLOUDUPGRADE = 2;	///< 云升级登陆
    public final static int LOGIN_TYPE_PCCLIENT = 3;	///< PC客户端登陆
    public final static int LOGIN_TYPE_MOBILE = 4;		///< 移动终端（IPhone）登陆
    public final static int LOGIN_TYPE_FUTRUE_HOME = 5;	//未来家庭客户端登陆
    public final static int LOGIN_TYPE_XM030 = 6;//xmeye客户端登陆
}
