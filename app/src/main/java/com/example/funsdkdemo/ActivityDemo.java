package com.example.funsdkdemo;

import java.util.Arrays;
import java.util.List;

import com.example.common.DialogWaitting;
import com.example.common.UIFactory;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ActivityDemo extends FragmentActivity {
	private DialogWaitting mWaitDialog = null;
	private Toast mToast = null;
	
	private View mNavRightView = null;
	
	public void showWaitDialog() {
		if ( null == mWaitDialog ) {
			mWaitDialog = new DialogWaitting(this);
		}
		mWaitDialog.show();
	}
	
	public void showWaitDialog(int resid) {
		if ( null == mWaitDialog ) {
			mWaitDialog = new DialogWaitting(this);
		}
		mWaitDialog.show(resid);
	}
	
	public void showWaitDialog(String text) {
		if ( null == mWaitDialog ) {
			mWaitDialog = new DialogWaitting(this);
		}
		mWaitDialog.show(text);
	}
	
	public void hideWaitDialog() {
		if ( null != mWaitDialog ) {
			mWaitDialog.dismiss();
		}
	}
	
	public void showToast(String text){
		if ( null != text ) {
			if ( null != mToast ) {
				mToast.cancel();
			}
			mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
			mToast.show();
		}
	}
	
	public void showToast(int resid){
		if ( resid > 0 ) {
			if ( null != mToast ) {
				mToast.cancel();
			}
			mToast = Toast.makeText(this, resid, Toast.LENGTH_SHORT);
			mToast.show();
		}
	}
	
	/**
	  *  判断某个字符串是否存在于数组中
	  *  用来判断该配置是否通道相关
	  *  @param stringArray 原数组
	  *  @param source 查找的字符串
	  *  @return 是否找到
	  */
	 public static boolean contains(String[] stringArray, String source) {
	  // 转换为list
	  List<String> tempList = Arrays.asList(stringArray);
	  
	  // 利用list的包含方法,进行判断
		 return tempList.contains(source);
	 }
	
	// 只有布局中有指定的标题栏的Activity才允许设置
	protected View setNavagateRightButton(int resource) {
		return setNavagateRightButton(resource, 48, LayoutParams.MATCH_PARENT);
	}
	
	protected View setNavagateRightButton(int resource, int witdhOfDP, int heightOfDP) {
		if ( null != findViewById(R.id.layoutTop) ) {
			RelativeLayout navLayout = (RelativeLayout)findViewById(R.id.layoutTop);
			if ( null != mNavRightView ) {
				navLayout.removeView(mNavRightView);
			}
			
			mNavRightView = LayoutInflater.from(this).inflate(resource, null);
			RelativeLayout.LayoutParams lp = null;
			
			int lpWidth = 0;
			int lpHeight = 0;
			if ( witdhOfDP > 0 ) {
				lpWidth = UIFactory.dip2px(this, witdhOfDP);
			} else {
				lpWidth = witdhOfDP;
			}
			
			if ( heightOfDP > 0 ) {
				lpHeight = UIFactory.dip2px(this, heightOfDP);
			} else {
				lpHeight = heightOfDP;
			}
			
			lp = new RelativeLayout.LayoutParams(
					lpWidth, lpHeight);
			
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			
			lp.rightMargin = UIFactory.dip2px(this, 5);
			
			navLayout.addView(mNavRightView, lp);
			
			return mNavRightView;
		}
		return null;
	}
}
