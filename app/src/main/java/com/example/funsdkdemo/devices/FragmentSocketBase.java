package com.example.funsdkdemo.devices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.common.UIFactory;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.OPPowerSocketGet;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunDeviceSocket;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

public abstract class FragmentSocketBase extends Fragment implements OnCheckedChangeListener, OnFunDeviceOptListener {

	protected View mLayout = null;
    protected View mViewControl = null;
	protected ImageView mImageControl = null;
	
	private int mControllerBgColor = 0xffe97425;
	private int mRoundImageResId = -1;
	
	protected FunDeviceSocket mFunDevice = null;
	
	protected RadioGroup mRadioGroup = null;
	protected RadioButton mRadioBtnOn = null;
	protected RadioButton mRadioBtnOff = null;
	protected RadioButton mRadioBtnTiming = null;
	protected RadioButton mRadioBtnSensor = null;
	
	protected ImageButton mBtnToAdd = null;
	
	private OPPowerSocketGet mTempOPPowerSocketGet = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = MyOnCreate(inflater, container, savedInstanceState);
		
		mViewControl = mLayout.findViewById(R.id.socket_control_background_rl);
		mImageControl = (ImageView)mViewControl.findViewById(R.id.socket_control_image);
		
		setControllerBackground(mControllerBgColor);
		if ( mRoundImageResId > 0 ) {
			setRoundImage(mRoundImageResId);
		}
		
		mRadioGroup = (RadioGroup)mLayout.findViewById(R.id.socket_control_switch_rg);
		mRadioBtnOn = (RadioButton)mLayout.findViewById(R.id.socket_control_switch_on_rb);
		mRadioBtnOff = (RadioButton)mLayout.findViewById(R.id.socket_control_switch_off_rb);
		mRadioBtnTiming = (RadioButton)mLayout.findViewById(R.id.socket_control_switch_timing_rb);
		mRadioBtnSensor = (RadioButton)mLayout.findViewById(R.id.socket_control_switch_sensor_rb);
		
		UIFactory.setTopDrawable(getActivity(), mRadioBtnOn, R.drawable.socket_switch_sel, 40, 40);
		UIFactory.setTopDrawable(getActivity(), mRadioBtnOff, R.drawable.socket_switch_sel, 40, 40);
		UIFactory.setTopDrawable(getActivity(), mRadioBtnTiming, R.drawable.socket_timing_sel, 40, 40);
		UIFactory.setTopDrawable(getActivity(), mRadioBtnSensor, R.drawable.socket_auto_sel, 40, 40);
		
		
		mRadioGroup.setOnCheckedChangeListener(this);
		
		mBtnToAdd = (ImageButton)mLayout.findViewById(R.id.add_iv);
		
		initLayout();
		
		refreshLayout();
		
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		return mLayout;
	}

	@Override
	public void onDestroyView() {
		FunSupport.getInstance().removeOnFunDeviceOptListener(this);
		
		super.onDestroyView();
	}
	
	protected abstract View MyOnCreate(LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState);
	
	protected void setControllerBackground(int bgColor) {
		mControllerBgColor = bgColor;
		if ( null != mViewControl ) {
			mViewControl.setBackgroundColor(mControllerBgColor);
		}
	}

	protected void setRoundImage(int resId) {
		mRoundImageResId = resId;
		if ( null != mImageControl ) {
			mImageControl.setImageResource(resId);
		}
	}
	
	protected void setRoundImageSelected(boolean isSelected) {
		if ( null != mImageControl ) {
			mImageControl.setSelected(isSelected);
		}
	}
	
	protected OPPowerSocketGet getOPPowerSocketGet() {
		if ( null != mFunDevice ) {
			return mFunDevice.getOPPowerSocketGet();
		}
		return null;
	}
	
	protected OPPowerSocketGet copyOPPowerSocketGet() {
		OPPowerSocketGet opps = getOPPowerSocketGet();
		if ( null == opps ) {
			return null;
		}
		return new OPPowerSocketGet(opps);
	}
	
	protected boolean setOPPowerSocketGet(OPPowerSocketGet opps) {
		if ( null != mFunDevice ) {
			showWaitDialog();
			mTempOPPowerSocketGet = opps;
			return FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, opps);
		}
		return false;
	}
	
	/**
	 * 把修改之后的参数保存下来,在设置成功之后调用,否则不调用
	 * 
	 */
	public void mergeChangedConfig() {
		if ( null != mTempOPPowerSocketGet ) {
			OPPowerSocketGet opps = getOPPowerSocketGet();
			if ( null != opps ) {
				opps.merge(mTempOPPowerSocketGet);
			}
			mTempOPPowerSocketGet = null;
		}
	}
	
	public void cleanChangedConfig() {
		mTempOPPowerSocketGet = null;
	}
	
	protected abstract void initLayout();
	
	protected abstract void refreshLayout();
	
	
	public void updateDevice(FunDeviceSocket funDevice) {
		mFunDevice = funDevice;
		refreshLayout();
	}
	
	protected void showWaitDialog() {
		try {
			ActivityDemo activity = (ActivityDemo)this.getActivity();
			activity.showWaitDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    protected void hideWaitDialog() {
        try {
            ActivityDemo activity = (ActivityDemo)this.getActivity();
            activity.hideWaitDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showToast(String text){
    	try {
			ActivityDemo activity = (ActivityDemo)this.getActivity();
			activity.showToast(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showToast(int resid){
		try {
			ActivityDemo activity = (ActivityDemo)this.getActivity();
			activity.showToast(resid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDeviceLoginSuccess(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceGetConfigSuccess(FunDevice funDevice,
			String configName, int nSeq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceSetConfigFailed(FunDevice funDevice, String configName,
			Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceChangeInfoSuccess(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceChangeInfoFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceOptionSuccess(FunDevice funDevice, String option) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceOptionFailed(FunDevice funDevice, String option,
			Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice,
			H264_DVR_FILE_DATA[] datas) {
		// TODO Auto-generated method stub
		
	}
	
    
}
