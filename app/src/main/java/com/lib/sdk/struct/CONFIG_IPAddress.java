/**
 * Android_NetSdk
 * CONFIG_IPAddress.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Android_NetSdk
 * CONFIG_IPAddress.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class CONFIG_IPAddress implements Serializable{
	public byte[] st_0_ip = new byte[4];
	
	public String getIp() {
		return GetIPValue(st_0_ip[0]) + "." + GetIPValue(st_0_ip[1]) + "." + GetIPValue(st_0_ip[2]) + "." + GetIPValue(st_0_ip[3]);
	}
	public void setIp(int ip1,int ip2,int ip3,int ip4) {
		st_0_ip[0] = (byte)ip1;
		st_0_ip[1] = (byte)ip2;
		st_0_ip[2] = (byte)ip3;
		st_0_ip[3] = (byte)ip4;
	}
	private int GetIPValue(byte b) {
		if(b < 0)
			return b+256;
		else
			return b;
	}
	@Override
	public String toString() {
		return "CONFIG_IPAddress [st_0_ip=" + Arrays.toString(st_0_ip) + "]";
	}
	
	
}
