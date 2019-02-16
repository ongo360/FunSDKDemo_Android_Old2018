package com.example.funsdkdemo;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunGetUserInfoListener;
import com.lib.funsdk.support.models.FunUserInfo;

/**
 * Created by Jeff on 16/4/11.
 */
public class ActivityGuideUserInfo extends ActivityDemo implements View.OnClickListener, OnFunGetUserInfoListener {

    private static final String TAG = "ActivityGuideUserInfo";

    private TextView mTextTitle = null;
    private ImageButton mBtnBack = null;

    private TextView mTextUserId = null;
    private TextView mTextUserName = null;
    private TextView mTextUserEmail = null;
    private TextView mTextUserPhone = null;
    private TextView mTextUserSex = null;
    private TextView mTextRegTime = null;

    private Button mBtnLogout = null;

    private String responseCode;
    private String responseMsg;
    private FunUserInfo mUserInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();

        tryToGetUserInfo();

        // 注册监听(用户登录相关)
        FunSupport.getInstance().registerOnFunGetUserInfoListener(this);
    }

    @Override
    protected void onDestroy() {
    	hideWaitDialog();
        // 注销监听(用户登录相关)
        FunSupport.getInstance().removeOnFunGetUserInfoListener(this);

        super.onDestroy();
    }

    /**
     * 初始化 Activity 的各个控件
     */
    public void initView() {
        mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);

        mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);

        mTextTitle.setText(R.string.guide_module_title_user_info);

        mTextUserId = (TextView) findViewById(R.id.tvUserId);
        mTextUserName = (TextView) findViewById(R.id.tvUserName);
        mTextUserEmail = (TextView) findViewById(R.id.tvUserEmail);
        mTextUserPhone = (TextView) findViewById(R.id.tvUserPhone);
        mTextUserSex = (TextView) findViewById(R.id.tvUserSex);
        mTextRegTime = (TextView) findViewById(R.id.tvUserRegTime);

        mBtnLogout = (Button) findViewById(R.id.userLogoutBtn);
        mBtnLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backBtnInTopLayout:
                finish();
                break;
            case R.id.userLogoutBtn:
                tryToLogout();
                break;

        }
    }

    private void tryToGetUserInfo() {
        showWaitDialog();
        if (!FunSupport.getInstance().getUserInfo()) {
            showToast(R.string.user_info_not_login);
        }
    }

    private void tryToLogout() {
        showWaitDialog();
        if (!FunSupport.getInstance().logout()) {
            showToast(R.string.guide_message_error_call);
        }
        finish();
    }

    public String getSexStr(int sex) {
        if (sex == 0) {
            return getResources().getString(R.string.user_info_sex_man);
        } else if (sex == 1) {
            return getResources().getString(R.string.user_info_sex_female);
        } else {
            return "";
        }
    }
    
    @Override
    public void onGetUserInfoSuccess(String strUserInfo) {
        hideWaitDialog();
        mUserInfo = parseJSONTOUserInfo(strUserInfo);
        if (mUserInfo != null) {
            mTextUserId.setText(mUserInfo.getUserId());
            mTextUserName.setText(mUserInfo.getUserName());
            mTextUserEmail.setText(mUserInfo.getEmail());
            mTextUserPhone.setText(mUserInfo.getMobile_phone());
            mTextUserSex.setText(getSexStr(mUserInfo.getSex()));
            mTextRegTime.setText(mUserInfo.getReg_time());
        } else {
            showToast(responseMsg);
        }
    }

    @Override
    public void onGetUserInfoFailed(int errCode) {
        hideWaitDialog();
        showToast(FunError.getErrorStr(errCode));
    }

    @Override
    public void onLogoutSuccess() {
        finish();
    }

    @Override
    public void onLogoutFailed(int errCode) {
        showToast(FunError.getErrorStr(errCode));
    }

    /*
        {
            "code":10001,
            "msg":"获取成功",
            "data":{
                "user_id":"169363",
                "user_name":"jijianfeng",
                "email":"xm@hatcloud.me",
                "mobile_phone":"",
                "sex":"0",
                "reg_time":"0"
            }
        }
        */
    private FunUserInfo parseJSONTOUserInfo(String data) {
        FunUserInfo userInfoTemp = null;

        try {
            JSONObject jsonContent = new JSONObject(data);
            responseCode = jsonContent.getString("code");
            responseMsg = jsonContent.getString("msg");
            if (responseMsg.equalsIgnoreCase("success")) {
                JSONObject jsonData = jsonContent.getJSONObject("data");
                String user_id = jsonData.optString("id");
                String user_name = jsonData.optString("username");
                String email = jsonData.optString("mail");
                String mobile_phone = jsonData.optString("phone");
                int sex = jsonData.optInt("sex");
                String reg_time = jsonData.optString("reg_time");
                userInfoTemp = new FunUserInfo(user_id, user_name, email, mobile_phone, sex, reg_time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfoTemp;

    }

}