/**
 * Android_NetSdk
 * MsgManager.java
 * Administrator
 * TODO
 * 2014-7-10
 */
package com.lib.sdk.struct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Android_NetSdk
 * MsgManager.java
 * @author huangwanshui
 * TODO
 * 2014-7-10
 */
public class MsgManager {
	private String mVerificationCode;//��֤��
	public static String executeHttpPost(MsgInfo msginfo) {
		StringBuffer requststr = new StringBuffer();
		requststr.append("CORPID=");
		requststr.append(msginfo.uid);
		requststr.append("&CPPW=");
		requststr.append(msginfo.pwd);
		requststr.append("&PHONE=");
		requststr.append(msginfo.mobile);
		requststr.append("&CONTENT=");
		requststr.append(msginfo.content);
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL("http://60.209.7.15:8080/smsServer/submit");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Charset", "utf-8");
			connection.setRequestProperty("Connection", "Close");
			PrintWriter pw = new PrintWriter(connection.getOutputStream());
			pw.print(requststr.toString());
			pw.flush();
			pw.close();

			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result = "FAILED";
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}
}
