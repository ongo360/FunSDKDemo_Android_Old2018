package com.example.funsdkdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunForgetPasswListener;

import java.util.regex.Pattern;

/**
 * Created by Jeff on 4/15/16.
 */
public class ActivityGuideUserForgetPassw extends ActivityDemo implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, OnFunForgetPasswListener {

    private TextView mTextTitle = null;
    private ImageButton mBtnBack = null;

    private RadioGroup mRadioResetPasswMode = null;

    private EditText mEditEmail = null;
    private EditText mEditPhone = null;
    private EditText mEditVerifyCode = null;
    private Button mBtnSendVerifyCode = null;
    private EditText mEditNewPassw = null;
    private TextView mTextPasswGrade = null;
    private EditText mEditNewPassConfirm = null;
    private Button mBtnVerifyCode = null;
    private Button mBtnSubmit = null;

    private PasswordChecker passwordChecker = null;

    private boolean isVerifyCodeConfirmed = false;

    private boolean byEmail = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forget_passw);

        mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
        mTextTitle.setText(R.string.guide_module_title_user_forgot_passwd);
        
        mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);

        mRadioResetPasswMode = (RadioGroup) findViewById(R.id.radioForgetPasswMode);
        mRadioResetPasswMode.setOnCheckedChangeListener(this);

        mEditEmail = (EditText) findViewById(R.id.userEmail);
        mEditPhone = (EditText) findViewById(R.id.userPhone);
        mEditVerifyCode = (EditText) findViewById(R.id.userVerifyCode);
        mEditNewPassw = (EditText) findViewById(R.id.userNewPasswd);
        mTextPasswGrade = (TextView) findViewById(R.id.passwGarde);
        mEditNewPassConfirm = (EditText) findViewById(R.id.userNewPasswdConfirm);

        mBtnSendVerifyCode = (Button) findViewById(R.id.btnSendVerifyCode);
        mBtnSendVerifyCode.setOnClickListener(this);
        mBtnVerifyCode = (Button) findViewById(R.id.verifyBtn);
        mBtnVerifyCode.setOnClickListener(this);
        mBtnSubmit = (Button) findViewById(R.id.submitBtn);
        mBtnSubmit.setOnClickListener(this);

        mRadioResetPasswMode.check(R.id.radioBtnResetPwdByEmail);
        showForgetPasswLayout(byEmail);

        //实时检测输入的密码强度
        passwordChecker = new PasswordChecker(this, mEditNewPassw, mTextPasswGrade);
        FunSupport.getInstance().registerOnFunCheckPasswListener(passwordChecker);
        passwordChecker.check();

        FunSupport.getInstance().registerOnFunForgetPasswListener(this);
    }

    @Override
    protected void onDestroy() {
        FunSupport.getInstance().removeOnFunCheckPasswListener(passwordChecker);
        FunSupport.getInstance().removeOnFunForgetPasswListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtnInTopLayout:
                finish();
                break;
            case R.id.btnSendVerifyCode:
                tryToSendVerifyCode();
                break;
            case R.id.verifyBtn:
                tryToVerifyCode();
                break;
            case R.id.submitBtn:
                tryToSubmit();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioBtnResetPwdByEmail:
                byEmail = true;
                showForgetPasswLayout(byEmail);
                break;
            case  R.id.radioBtnResetPwdByCellphone:
                byEmail = false;
                showForgetPasswLayout(byEmail);
                break;
        }
    }

    /**
     * 请求发送验证码(区分邮箱还是手机)
     */
    private void tryToSendVerifyCode() {
        if (byEmail) {
            String email = mEditEmail.getText().toString().trim();
            if (!isEmailValid(email)) {
                // 邮箱不正确
                showToast(R.string.user_login_error_email);
                return;
            }

            showWaitDialog();

            if ( !FunSupport.getInstance().requestSendEmailCodeForResetPW(email) ) {
                showToast(R.string.guide_message_error_call);
            }

        } else {
            String phoneNum = mEditPhone.getText().toString().trim();
            if ( phoneNum.length() != 11 ) {
                // 手机号不正确
                showToast(R.string.user_login_error_phone_number);
                return;
            }

            showWaitDialog();

            if ( !FunSupport.getInstance().requestSendPhoneMsgForResetPW(phoneNum) ) {
                showToast(R.string.guide_message_error_call);
            }
        }

    }

    /**
     * 校验验证码(区分邮箱还是手机)
     */
    private void tryToVerifyCode() {
        if (byEmail) {
            String email = mEditEmail.getText().toString().trim();
            String verifyCode = mEditVerifyCode.getText().toString().trim();
            showWaitDialog();
            if (!FunSupport.getInstance().requestVerifyEmailCode(email, verifyCode)) {
                showToast(R.string.guide_message_error_call);
            }
        } else {
            String phone = mEditPhone.getText().toString().trim();
            String verifyCode = mEditVerifyCode.getText().toString().trim();
            Pattern pattern = Pattern.compile("[0-9]{4}");
            if (!pattern.matcher(verifyCode).matches()) {
                showToast(R.string.user_forget_pwd_verify_code_format_erroe);
            } else {
                showWaitDialog();
                if (!FunSupport.getInstance().requestVerifyPhoneCode(phone, verifyCode)) {
                    showToast(R.string.guide_message_error_call);
                }
            }
        }
    }

    /**
     * 发送密码重置请求
     */
    private void tryToSubmit(){
        String newPassw = mEditNewPassw.getText().toString();
        String newPasswConfirm = mEditNewPassConfirm.getText().toString();
        if (!isVerifyCodeConfirmed) {
            showToast(R.string.user_forget_pwd_verify_code_first);
            return;
        }

        if (TextUtils.isEmpty(newPassw) || newPassw.length() < 8) {
            showToast(R.string.user_forget_pwd_new_password_error);
            return;
        }

        if (passwordChecker.getPasswGrade() == 1) {
            showToast(R.string.password_checker_weak_error);
        }

        if (!newPassw.equals(newPasswConfirm)) {
            showToast(R.string.user_forget_pwd_new_password_confirm_error);
            return;
        }

        showWaitDialog();

        if (byEmail) {
            String email = mEditEmail.getText().toString().trim();
            if (!FunSupport.getInstance().requestResetPasswByEmail(email, newPassw)) {
                showToast(R.string.guide_message_error_call);
            }
        } else {
            String phone = mEditPhone.getText().toString().trim();
            if (!FunSupport.getInstance().requestResetPasswByPhone(phone, newPassw)) {
                showToast(R.string.guide_message_error_call);
            }
        }

    }

    /**
     * 验证邮箱格式是否正确
     */
    public boolean isEmailValid(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

    /**
     * 以邮箱的方式还是手机方式重置密码
     * @param byEmail
     */
    private void showForgetPasswLayout(boolean byEmail) {
        if ( byEmail ) {
            findViewById(R.id.layoutPhone).setVisibility(View.GONE);
            findViewById(R.id.layoutEmail).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layoutEmail).setVisibility(View.GONE);
            findViewById(R.id.layoutPhone).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestCodeSuccess() {
        hideWaitDialog();
        showToast(R.string.guide_message_request_phone_msg_success);
    }

    @Override
    public void onRequestCodeFailed(Integer errCode) {
        hideWaitDialog();
        showToast(FunError.getErrorStr(errCode));
    }

    @Override
    public void onVerifyCodeSuccess() {
        isVerifyCodeConfirmed = true;
        hideWaitDialog();
        showToast(R.string.user_forget_pwd_verify_success);
    }

    @Override
    public void onVerifyFailed(Integer errCode) {
        isVerifyCodeConfirmed = false;
        hideWaitDialog();
        showToast(FunError.getErrorStr(errCode));
    }

    @Override
    public void onResetPasswSucess() {
        hideWaitDialog();
        showToast(R.string.user_forget_pwd_reset_passw_success);
    }

    @Override
    public void onResetPasswFailed(Integer errCode) {
        hideWaitDialog();
        showToast(FunError.getErrorStr(errCode));
    }
}