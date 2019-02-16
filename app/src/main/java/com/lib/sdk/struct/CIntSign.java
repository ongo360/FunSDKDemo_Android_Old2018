package com.lib.sdk.struct;

public class CIntSign {
	private int _id;
	public CIntSign()
	{
		_id = CreateId();
	}
	
	public int GetSignId()
	{
		return _id;
	}
	public static int s_id = 0;
	public static synchronized int CreateId(){
		s_id++;
		return s_id;
	}
}
