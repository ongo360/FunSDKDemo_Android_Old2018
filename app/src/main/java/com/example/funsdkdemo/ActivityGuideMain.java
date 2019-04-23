package com.example.funsdkdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.example.funsdkdemo.dialog.PermissionDialog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunLoginListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

public class ActivityGuideMain extends ActivityGuide implements OnFunLoginListener {

	private static List<DemoModule> mGuideModules = new ArrayList<DemoModule>();
	
	private Button mBtnUserStat = null;
	private PermissionDialog mPermissionDialog;
	private RxPermissions rxPermissions;

	static {
		
		// 1.用户相关
		mGuideModules.add(new DemoModule(R.drawable.icon_user, 
				R.string.guide_module_title_user, 
				R.string.guide_module_desc_user, 
				ActivityGuideUser.class));
		
		// 2.设备相关
		mGuideModules.add(new DemoModule(R.drawable.icon_device, 
				R.string.guide_module_title_device, 
				R.string.guide_module_desc_device, 
				ActivityGuideDevice.class));
		
		// 3.媒体功能
		mGuideModules.add(new DemoModule(R.drawable.icon_media, 
				R.string.guide_module_title_media, 
				R.string.guide_module_desc_media, 
				ActivityGuideMedia.class));
		
		// 4.编码转码
		mGuideModules.add(new DemoModule(R.drawable.icon_transcode, 
				R.string.guide_module_title_trancode, 
				R.string.guide_module_desc_trancode, 
				ActivityGuideTranscode.class));
		
		// 5.关于
		mGuideModules.add(new DemoModule(R.drawable.icon_funsdk, 
				R.string.guide_module_title_about, 
				R.string.guide_module_desc_about, 
				ActivityAbout.class));
	}
	
	@Override
	protected List<DemoModule> getGuideModules() {
		return mGuideModules;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View navRight = setNavagateRightButton(R.layout.button_user_stat, 
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		mBtnUserStat = (Button)navRight.findViewById(R.id.btnUserStat);
		mBtnUserStat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( FunSupport.getInstance().hasLogin() ) {
					// 如果已经登录,显示用户详情界面
					enterUserInfo();
				} else {
					// 如果未登录,进入登录界面
					enterLoginActivity();
				}
			}
		});
		
		FunSupport.getInstance().registerOnFunLoginListener(this);
		
		refreshLoginStat(FunSupport.getInstance().hasLogin());

		rxPermissions = new RxPermissions(this);

		checkPermission(getString(R.string.all_permission), Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO);
	}

	@Override
	protected void onDestroy() {
		
		FunSupport.getInstance().removeOnFunLoginListener(this);
		((MyApplication)getApplication()).exit();
		super.onDestroy();
	}

	@Override
	public void onLoginSuccess() {
		refreshLoginStat(true);
	}

	@Override
	public void onLoginFailed(Integer errCode) {
		refreshLoginStat(false);
	}

	@Override
	public void onLogout() {
		refreshLoginStat(false);
	}

	private void refreshLoginStat(boolean hasLogin) {
		if ( null != mBtnUserStat ) {
			if ( hasLogin ) {
				mBtnUserStat.setText(FunSupport.getInstance().getUserName());
			} else {
				mBtnUserStat.setText(R.string.user_not_login);
			}
		}
	}
	
	private void enterLoginActivity() {
		Intent intent = new Intent();
		intent.setClass(this, ActivityGuideUserLogin.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	private void enterUserInfo() {
		Intent intent = new Intent();
		intent.setClass(this, ActivityGuideUserInfo.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private boolean checkPermission(final String permissionTitle, final String... permission) {
		try{
			final boolean[] result = new boolean[1];
			rxPermissions.requestEach(permission).subscribe(new io.reactivex.functions.Consumer<Permission>() {
				@Override
				public void accept(final Permission permission) throws Exception {
					if (permission.granted) {
						result[0] = true;
						permissionGranted(permission);
					} else if (permission.shouldShowRequestPermissionRationale) {
						result[0] = false;
						if (mPermissionDialog == null) {
							mPermissionDialog = new PermissionDialog();
						}
						if (!mPermissionDialog.isAdded()) {
							mPermissionDialog.setTitle(permissionTitle);
							mPermissionDialog.setOperateListener(new PermissionDialog.OperateListener() {
								@Override
								public void onCancel() {
									result[0] = false;
									permissionResult(false,permission);
								}

								@Override
								public void onConfirm() {
									permissionResult(true,permission);
									result[0] = true;
								}
							});
							mPermissionDialog.show(getSupportFragmentManager(), "mPermissionDialog");
						}
					} else {
						// Need to go to the settings
						if (mPermissionDialog == null) {
							mPermissionDialog = new PermissionDialog();
						}
						if (!mPermissionDialog.isAdded()) {
							mPermissionDialog.setTitle(permissionTitle);
							mPermissionDialog.setOperateListener(new PermissionDialog.OperateListener() {
								@Override
								public void onCancel() {
									result[0] = false;
									permissionResult(false,permission);
								}

								@Override
								public void onConfirm() {
									permissionResult(true,permission);
									result[0] = true;
								}
							});
							mPermissionDialog.show(getSupportFragmentManager(), "mPermissionDialog");
						}

					}
				}
			});
			return result[0];
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private void permissionGranted(Permission permission) {

	}

	private void permissionResult(boolean isSuccess,Permission permission){

	}
}
