package com.lib.funsdk.support.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.View;

import com.example.funsdkdemo.MyApplication;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.utils.Define.EncrypType;
import com.lib.sdk.struct.H264_DVR_FINDINFO;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class MyUtils {
	/**
	 * 
	 * @param capabilities
	 * @return
	 */
	public static int getEncrypPasswordType(String capabilities) {
		if (capabilities.contains("WPA2") && capabilities.contains("CCMP")) {
			// sEncrypType = "AES";
			// sAuth = "WPA2";
			return 1;
		} else if (capabilities.contains("WPA2")
				&& capabilities.contains("TKIP")) {
			// sEncrypType = "TKIP";
			// sAuth = "WPA2";
			return 2;
		} else if (capabilities.contains("WPA")
				&& capabilities.contains("TKIP")) {
			// EncrypType = "TKIP";
			// sAuth = "WPA";
			return 2;
		} else if (capabilities.contains("WPA")
				&& capabilities.contains("CCMP")) {
			// sEncrypType = "AES";
			// sAuth = "WPA";
			return 1;
		} else if (capabilities.contains("WEP")) {
			return 3;
		} else {
			// sEncrypType = "NONE";
			// sAuth = "OPEN";
			return 0;
		}
	}

	public static EncrypType getEncrypType(String capabilities) {
		if (capabilities.contains("WPA2") && capabilities.contains("CCMP")) {
			return EncrypType.WPA2_CCMP;
		} else if (capabilities.contains("WPA2")
				&& capabilities.contains("TKIP")) {
			return EncrypType.WPA2_TKIP;
		} else if (capabilities.contains("WPA")
				&& capabilities.contains("TKIP")) {
			return EncrypType.WPA_TKIP;
		} else if (capabilities.contains("WPA")
				&& capabilities.contains("CCMP")) {
			return EncrypType.WPA_CCMP;
		} else if (capabilities.contains("WEP")) {
			return EncrypType.WEP;
		} else {
			return EncrypType.NONE;
		}
	}
	
	public static String formatIpAddress(int ip ) {
		byte[] ipAddress = new byte[4];
		InetAddress myaddr;
		try {
			ipAddress[3] = (byte)((ip >> 24) & 0xff);
			ipAddress[2] = (byte)((ip >> 16) & 0xff);
			ipAddress[1] = (byte)((ip >> 8) & 0xff);
			ipAddress[0] = (byte)(ip & 0xff);
			myaddr = InetAddress.getByAddress(ipAddress);
			String hostaddr = myaddr.getHostAddress();
			return hostaddr;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getMpsPushToken(Context context) {
		try {
			// 获取手机IMEI
			String imei = ((TelephonyManager) context.getSystemService(
					Context.TELEPHONY_SERVICE)).getDeviceId();
			
			if ( null == imei ) {
				return null;
			}
			
			return imei + "funsdkdemo";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// 返回值为是否开启了wifi
	public static boolean detectWifiNetwork(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		// WIFI正常
		return networkinfo != null && networkinfo.isAvailable() == true
				&& networkinfo.getType() == ConnectivityManager.TYPE_WIFI;

	}
	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isStringNULL(String str) {
		if (str == null || str.equals("") || str.equals("null"))
			return true;
		else
			return false;
	}

    /**
     * 判断字符串是否为数字
     *
     * @param s
     * @return
     */
    public static boolean isInteger(String s) {
        try {
            Long.parseLong(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public static int getIntFromHex(String s) {
		int value = 0;
		if (s != null && s.length() > 2 && s.startsWith("0x")) {
			value = Integer.parseInt((s.substring(2, s.length())), 16);
		} else if (s != null && s.isEmpty()) {

		} else if (s != null) {
			value = Integer.parseInt(s);
		}
		return value;
	}
    
	public static String getHexFromInt(int i) {
		return "0x" + Integer.toHexString(i);
	}
    /**
     * 判断是否是一个IP 
     * @param IP
     * @return
     */
    public static boolean isIp(String IP){
        boolean b = false; 
        IP = IP.trim();
        if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){ 
            String s[] = IP.split("\\."); 
            if(Integer.parseInt(s[0])<255) 
                if(Integer.parseInt(s[1])<255) 
                    if(Integer.parseInt(s[2])<255) 
                        if(Integer.parseInt(s[3])<255) 
                            b = true; 
        } 
        return b;
    }
    
    public static int[] getButtonPixels(View view) {
		if (null == view)
			return null;
		int[] pixels = new int[view.getWidth() * view.getHeight()];
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();
		if (null == bitmap)
			return null;
		bitmap.getPixels(pixels, 0, view.getWidth(), 0, 0, view.getWidth(), view.getHeight());
		view.setDrawingCacheEnabled(false);
		return pixels;
	}
    
    public static byte[] getPixelsToDevice(View view) {
		int n = 0, m = 0;
		int[] pixels = getButtonPixels(view);
		int w = view.getWidth();
		int h = view.getHeight();
		int bytesPerPixel = 1;
		int bytesPerRow = bytesPerPixel * w;
		int bufSize = w * h / 8;
		byte[] pRet = new byte[bufSize];
		if (null == pixels)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w && m < bufSize; j++) {
				int offset = bytesPerRow * i + bytesPerPixel * j;
				if (pixels[offset] != -1) {
					pRet[m] |= (0x1 << (7 - n));
					sb.append("*");
				} else {
					sb.append(" ");
				}
				n++;
				if (n == 8) {
					m++;
					n = 0;
				}
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
		return pRet;
	}

	public static String getDownloadFileNameByInfo(String mac,
												   H264_DVR_FINDINFO info) {
		StringBuffer sb = new StringBuffer();
		if (null != info) {
			if (mac == null) {
				sb.append(info.st_2_startTime.st_0_dwYear)
						.append(MyUtils
								.addZero(info.st_2_startTime.st_1_dwMonth))
						.append(MyUtils.addZero(info.st_2_startTime.st_2_dwDay))
						.append(MyUtils
								.addZero(info.st_2_startTime.st_3_dwHour))
						.append(MyUtils
								.addZero(info.st_2_startTime.st_4_dwMinute))
						.append(MyUtils
								.addZero(info.st_2_startTime.st_5_dwSecond))
						.append("-")
						.append(MyUtils.addZero(info.st_3_endTime.st_3_dwHour))
						.append(MyUtils
								.addZero(info.st_3_endTime.st_4_dwMinute))
						.append(MyUtils
								.addZero(info.st_3_endTime.st_5_dwSecond))
						.append(".mp4");
			} else if (mac != null) {
				sb.append(mac).append("_s-")
						.append(info.st_2_startTime.st_0_dwYear)
						.append(MyUtils
								.addZero(info.st_2_startTime.st_1_dwMonth))
						.append(MyUtils.addZero(info.st_2_startTime.st_2_dwDay))
						.append(MyUtils
								.addZero(info.st_2_startTime.st_3_dwHour))
						.append(MyUtils
								.addZero(info.st_2_startTime.st_4_dwMinute))
						.append(MyUtils
								.addZero(info.st_2_startTime.st_5_dwSecond))
						.append("_e-")
						.append(MyUtils.addZero(info.st_3_endTime.st_3_dwHour))
						.append(MyUtils
								.addZero(info.st_3_endTime.st_4_dwMinute))
						.append(MyUtils
								.addZero(info.st_3_endTime.st_5_dwSecond))
						.append(".mp4");
			}

		}
		return FunPath.PATH_VIDEO + File.separator + sb.toString();
	}

	/**
	 * 为一位的数字前补0
	 *
	 * @param from
	 * @return
	 */
	public static String addZero(int from) {
		String str = String.valueOf(from);
		return str.length() > 1 ? str : "0" + str;
	}
}
