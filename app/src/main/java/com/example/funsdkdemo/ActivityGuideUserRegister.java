package com.example.funsdkdemo;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunRegisterListener;


public class ActivityGuideUserRegister extends ActivityDemo implements OnClickListener, OnCheckedChangeListener, OnFunRegisterListener{

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private RadioGroup mRadioRegisterMode = null;
	
	
	private EditText mEditUserName = null;
	private EditText mEditPassWord = null;
	private EditText mEditPassWordConfirm = null;
	private EditText mEditEmail = null;
	private EditText mEditPhone = null;
	private EditText mEditVerifyCode = null;
	private Button mBtnGetVerifyCode = null;
	private Button mBtnRegister = null;
	private Button mBtnUsernameCheck = null;

    private boolean byEmail = true;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_user_register);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.guide_module_title_user_register);
		
		mRadioRegisterMode = (RadioGroup)findViewById(R.id.radioRegisterMode);
		mRadioRegisterMode.setOnCheckedChangeListener(this);
		
		mEditUserName = (EditText)findViewById(R.id.userRegisterUserName);
		mEditEmail = (EditText)findViewById(R.id.userRegisterEmail);
		mEditPhone = (EditText)findViewById(R.id.userRegisterPhone);
		mEditVerifyCode = (EditText)findViewById(R.id.userRegisterVerifyCode);
		mEditPassWord = (EditText)findViewById(R.id.userRegisterPasswd);
		mEditPassWordConfirm = (EditText)findViewById(R.id.userRegisterPasswdConfirm);
		
		mBtnGetVerifyCode = (Button)findViewById(R.id.btnGetVerifyCode);
		mBtnGetVerifyCode.setOnClickListener(this);
		mBtnRegister = (Button)findViewById(R.id.userRegisterBtn);
		mBtnRegister.setOnClickListener(this);
		mBtnUsernameCheck = (Button) findViewById(R.id.btnUserNameRepeat);
		mBtnUsernameCheck.setOnClickListener(this);
		
		mRadioRegisterMode.check(R.id.radioBtnRegisterByEmail);
		showRegisterLayout(byEmail);
		
		// 注册监听(用户注册相关)
		FunSupport.getInstance().registerOnFunRegisterListener(this);
	}
	

	@Override
	protected void onDestroy() {
		// 注销监听(用户注册相关)
		FunSupport.getInstance().removeOnFunRegisterListener(this);
		
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.backBtnInTopLayout:
			{
				// 返回/退出
				finish();
			}
			break;
		case R.id.userRegisterBtn:
			{
				// 快速注册
				tryToRegister();
			}
			break;
		case R.id.btnGetVerifyCode:
			{
				// 获取验证码
				tryGetVerifyCode();
			}
			break;
		case R.id.btnUserNameRepeat:
			//check username repeat
			checkUsername();
			break;
		}
	}
	
	/**
	 * 以邮箱的方式还是手机方式注册
	 * @param byEmail
	 */
	private void showRegisterLayout(boolean byEmail) {
		if ( byEmail ) {
			//findViewById(R.id.layoutRegisterVerifyCode).setVisibility(View.GONE);
			findViewById(R.id.layoutRegisterPhone).setVisibility(View.GONE);
			findViewById(R.id.layoutRegisterEmail).setVisibility(View.VISIBLE);
		} else {
			//findViewById(R.id.layoutRegisterVerifyCode).setVisibility(View.VISIBLE);
			findViewById(R.id.layoutRegisterPhone).setVisibility(View.VISIBLE);
			findViewById(R.id.layoutRegisterEmail).setVisibility(View.GONE);
			
			setVerifyCodeButton(true);
		}
	}
	
	// check username
	private void checkUsername(){
		String userName = mEditUserName.getText().toString();
		if (userName != null && userName.length() != 0) {
			FunSupport.getInstance().checkUserName(userName);
		}
	}
	
	// 设置获取验证码按钮是否可用
	private void setVerifyCodeButton(boolean enabled) {
		mBtnGetVerifyCode.setEnabled(enabled);
		if ( enabled ) {
			mBtnGetVerifyCode.setTextColor(getResources().getColor(R.color.white));
			mBtnGetVerifyCode.setBackgroundResource(R.drawable.common_button_selector);
		} else {
			mBtnGetVerifyCode.setTextColor(getResources().getColor(R.color.demo_desc));
			mBtnGetVerifyCode.setBackgroundColor(getResources().getColor(R.color.bg_gray));
		}
	}
	
	private void tryGetVerifyCode() {
		String userName = mEditUserName.getText().toString();
		String phoneNum = mEditPhone.getText().toString().trim();
        String emailStr = mEditEmail.getText().toString().trim();

        if ( null == userName || userName.length() == 0 ) {
			// 用户名为空
			showToast(R.string.user_login_error_emptyusername);
			return;
		}

        if (byEmail) {
            if (!isEmailValid(emailStr)) {
                // 邮箱不正确
                showToast(R.string.user_login_error_email);
                return;
            }
            showWaitDialog();
            if (!FunSupport.getInstance().requestEmailCode(emailStr)) {
                showToast(R.string.guide_message_error_call);
            }

        } else {
            if ( phoneNum.length() != 11 ) {
                // 手机号不正确
                showToast(R.string.user_login_error_phone_number);
                return;
            }
            showWaitDialog();

            if ( !FunSupport.getInstance().requestPhoneMsg(userName, phoneNum) ) {
                showToast(R.string.guide_message_error_call);
            }
        }
		

	}
	
	private void tryToRegister() {
		String userName = mEditUserName.getText().toString();
		String passWord = mEditPassWord.getText().toString();
		String passWordConfirm = mEditPassWordConfirm.getText().toString();
		
		if ( null == userName || userName.length() == 0 ) {
			// 用户名为空
			showToast(R.string.user_login_error_emptyusername);
			return;
		}
		
		if ( null == passWord || passWord.length() == 0 ) {
			// 密码为空
			showToast(R.string.user_login_error_emptypassword);
			return;
		}
		
		if ( !passWord.equals(passWordConfirm) ) {
			showToast(R.string.user_register_error_password_unmatched);
			return;
		}
		
		if ( userName.length() > 16 || userName.length() < 6 ) {
			showToast(R.string.user_register_error_username_length);
			return;
		}
		
		if ( passWord.length() < 8 ) {
			showToast(R.string.user_register_error_password_length);
			return;
		}
		
		if ( R.id.radioBtnRegisterByEmail 
				== mRadioRegisterMode.getCheckedRadioButtonId() ) {
			// 通过邮箱注册
			String email = mEditEmail.getText().toString().trim();
            String verifyCode = this.mEditVerifyCode.getText().toString().trim();
			
			if ( null == email || email.length() == 0 || !email.contains("@") || !isEmailValid(email)) {
				// 邮箱格式不正确
				showToast(R.string.user_login_error_email);
				return;
			}

            if ( null == verifyCode || verifyCode.length() == 0 ) {
                // 验证码为空
                showToast(R.string.user_login_error_emptyverifycode);
                return;
            }

            showWaitDialog();

            if ( !FunSupport.getInstance().registerByEmail(
                    userName, passWord, verifyCode, email) ) {
                showToast(R.string.guide_message_error_call);
            }
			
		} else {
			// 通过手机号注册
			String phoneNo = mEditPhone.getText().toString().trim();
			String verifyCode = this.mEditVerifyCode.getText().toString().trim();
			
			if ( phoneNo.length() != 11 ) {
				// 手机号不正确
				showToast(R.string.user_login_error_phone_number);
				return;
			}
			
			if ( null == verifyCode || verifyCode.length() == 0 ) {
				// 验证码为空
				showToast(R.string.user_login_error_emptyverifycode);
				return;
			}
			
			showWaitDialog();
			
			if ( !FunSupport.getInstance().registerByPhone(
					userName, passWord, verifyCode, phoneNo) ) {
				showToast(R.string.guide_message_error_call);
			}
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int id) {
		switch(id) {
		case R.id.radioBtnRegisterByEmail:
			{
                byEmail = true;
                showRegisterLayout(byEmail);
			}
			break;
		case R.id.radioBtnRegisterByCellphone:
			{
                byEmail = false;
                showRegisterLayout(byEmail);
			}
			break;
		}
	}

    /**
     * 验证邮箱格式是否正确
     */
    public boolean isEmailValid(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

	@Override
	public void onRequestSendCodeSuccess() {
		hideWaitDialog();
		showToast(R.string.guide_message_request_phone_msg_success);
	}


	@Override
	public void onRequestSendCodeFailed(final Integer errCode) {
		hideWaitDialog();
		showToast(FunError.getErrorStr(errCode));
	}


	@Override
	public void onRegisterNewUserSuccess() {
		hideWaitDialog();
		showToast(R.string.guide_message_register_user_success);
	}

	@Override
	public void onRegisterNewUserFailed(Integer errCode) {
		hideWaitDialog();
		showToast(FunError.getErrorStr(errCode));
	}

	@Override
	public void onUserNameFine() {
		// TODO Auto-generated method stub
		
		showToast(R.string.guide_message_username_fine);
	}

	@Override
	public void onUserNameUnfine(Integer errCode) {
		// TODO Auto-generated method stub
		showToast(FunError.getErrorStr(errCode));
	}
	
}
