package com.example.funsdkdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunChangePasswListener;

/**
 * Created by jijianfeng on 2016-04-09.
 */
public class ActivityGuideUserChangePassw extends ActivityDemo implements View.OnClickListener, OnFunChangePasswListener{

    private TextView mTextTitle = null;
    private ImageButton mBtnBack = null;

    private EditText mEditUserName = null;
    private EditText mEditOldPassw = null;
    private EditText mEditNewPassw = null;
    private TextView mTextPasswGrade = null;
    private EditText mEditNewPasswConfirm = null;
    private Button mBtnChangePassw = null;

    PasswordChecker passwordChecker = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);

        mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);

        mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);

        mEditUserName = (EditText) findViewById(R.id.userName);
        mEditOldPassw = (EditText) findViewById(R.id.oldPasswd);
        mTextPasswGrade = (TextView) findViewById(R.id.textGarde);
        mEditNewPassw = (EditText) findViewById(R.id.newPasswd);
        mEditNewPasswConfirm = (EditText) findViewById(R.id.newPasswdConfirm);
        mBtnChangePassw = (Button) findViewById(R.id.userChangePasswBtn);

        mTextTitle.setText(R.string.guide_module_title_user_change_passwd);
        mBtnChangePassw.setOnClickListener(this);

        //实时检测输入的密码强度
        passwordChecker = new PasswordChecker(this, mEditNewPassw, mTextPasswGrade);
        FunSupport.getInstance().registerOnFunCheckPasswListener(passwordChecker);
        passwordChecker.check();


        // 注册监听(用户注册相关)
        FunSupport.getInstance().registerOnFunChangepasswListener(this);
    }

    @Override
    protected void onDestroy() {

        // 注销监听(用户登录相关)
        FunSupport.getInstance().removeOnFunChangePasswListener(this);
        FunSupport.getInstance().removeOnFunCheckPasswListener(passwordChecker);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtnInTopLayout:
                finish();
                break;
            case R.id.userChangePasswBtn:
                tryToChangePassw();
                break;
            default:
                break;
        }
    }



    private void tryToChangePassw() {

        String userName = mEditUserName.getText().toString();
        String oldPassw = mEditOldPassw.getText().toString();
        String newPassw = mEditNewPassw.getText().toString();
        String newPasswConfirm = mEditNewPasswConfirm.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            showToast(R.string.user_change_password_error_emptyusername);
            return;
        }

        if (TextUtils.isEmpty(oldPassw) || TextUtils.isEmpty(newPassw) || TextUtils.isEmpty(newPasswConfirm)) {
            showToast(R.string.user_change_password_error_emptypassw);
            return;
        } else if (newPassw.length() < 8) {
            showToast(R.string.user_change_password_error_passwtooshort);
            return;
        } else if (!newPassw.equals(newPasswConfirm)) {
            showToast(R.string.user_change_password_error_passwnotequal);
            return;
        }

        showWaitDialog();

        if ( !FunSupport.getInstance().changePassw(userName, oldPassw, newPassw)) {
			showToast(R.string.guide_message_error_call);
		}

    }

    @Override
    public void onChangePasswSuccess() {
        hideWaitDialog();
        showToast(R.string.user_change_password_sucess);
    }

    @Override
    public void onChangePasswFailed(Integer errCode) {
        hideWaitDialog();
        showToast(FunError.getErrorStr(errCode));
    }
}
