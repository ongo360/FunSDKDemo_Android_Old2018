package com.example.funsdkdemo.dialog;

import android.support.v4.app.FragmentActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import com.example.funsdkdemo.R;
import com.lib.IFunSDKResult;

public abstract class BaseDlg implements OnClickListener, IFunSDKResult {

	protected FragmentActivity mActivity;

	protected View mView;

	protected LayoutInflater mInflater;
	protected static String sEncrypType = "";
	protected static String sAuth = "";

	public BaseDlg() {
	}

	public BaseDlg(FragmentActivity activity, View mView) {
		super();
		this.mActivity = activity;
		this.mView = mView;
		this.mInflater = LayoutInflater.from(activity);
	}

	protected ViewGroup GetRootLayout(View Baseview) {
		ViewGroup layout = (ViewGroup) Baseview.findViewById(R.id.layoutRoot);
		if (layout == null && null != mActivity) {
			View v = mActivity.getCurrentFocus();
			if (v != null) {
				ViewParent vp = v.getParent();
				if (vp == null) {
					layout = (ViewGroup) v;
				} else {
					layout = (ViewGroup) vp;
					while ((vp = vp.getParent()) != null) {
						layout = (ViewGroup) vp;
					}
				}
			}
		}
		return layout;
	}

	// 显示密码
	public void SetShowPsd(View v, int id) {
		EditText pwd_et = (EditText) v.findViewById(id);
		if (pwd_et.getTransformationMethod() == null) {
			pwd_et.setTransformationMethod(new PasswordTransformationMethod());
		} else {
			pwd_et.setTransformationMethod(null);
		}
		pwd_et.setSelection(pwd_et.getText().toString().length());

	}

	public static void setEncrype(String capabilities) {
		if (capabilities.contains("WPA2") && capabilities.contains("CCMP")) {
			sEncrypType = "AES";
			sAuth = "WPA2";
		} else if (capabilities.contains("WPA2") && capabilities.contains("TKIP")) {
			sEncrypType = "TKIP";
			sAuth = "WPA2";
		} else if (capabilities.contains("WPA") && capabilities.contains("TKIP")) {
			sEncrypType = "TKIP";
			sAuth = "WPA";
		} else if (capabilities.contains("WPA") && capabilities.contains("CCMP")) {
			sEncrypType = "AES";
			sAuth = "WPA";
		} else if (capabilities.contains("WEP")) {
		} else {
			sEncrypType = "NONE";
			sAuth = "OPEN";
		}
	}
}
