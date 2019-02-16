package com.example.funsdkdemo;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunCheckPasswListener;

/**
 * Created by Jeff on 4/14/16.
 *
 * 该类是用来通过服务器来检测用户输入密码,并在一个 TextView 里面显示的工具类
 */
public class PasswordChecker implements OnFunCheckPasswListener {

	
    Resources resource = null;

    EditText editPassw;
    TextView textGrade;

    int passwordGrade;
    String loading = ".";

    private final int MESSAGE_CHECK_PASSWORD = 0x100;
    private final int MESSAGE_LOADING = 0x101;

    /**
     *
     * @param editPassw 输入密码的 EditText
     * @param textGrade 显示检测结果对的 TextView
     */
    public PasswordChecker(Context context, EditText editPassw, TextView textGrade) {
    	this.resource = context.getResources();
        this.editPassw = editPassw;
        this.textGrade = textGrade;
        passwordGrade = 0;
    }

    public int getPasswGrade() {
        return passwordGrade;
    }

    public void check(){
        editPassw.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            mHandler.removeMessages(MESSAGE_LOADING);
            mHandler.sendEmptyMessageDelayed(MESSAGE_LOADING, 700);

            mHandler.removeMessages(MESSAGE_CHECK_PASSWORD);
            Message message = new Message();
            message.what = MESSAGE_CHECK_PASSWORD;
            message.obj = s.toString();
            mHandler.sendMessageDelayed(message, 1000);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CHECK_PASSWORD:
                    String password = (String)msg.obj;
                    if (TextUtils.isEmpty(password)) {
                        textGrade.setText(R.string.password_checker_empty);
                        textGrade.setTextColor(resource.getColorStateList(R.color.passw_weak));
                        break;
                    }

                    if (password.length() < 6) {
                        textGrade.setText(R.string.password_checker_short);
                        textGrade.setTextColor(resource.getColorStateList(R.color.passw_weak));
                        break;
                    }
                    FunSupport.getInstance().checkPassw(password);
                    break;
                case MESSAGE_LOADING:
                    loading += ".";
                    if(loading.equals("....")){
                        loading = ".";
                    }
                    String loadingDiaplay = loading;
                    for(int i = loadingDiaplay.length(); i < 3; i++) {
                        loadingDiaplay += " ";
                    }
                    textGrade.setText(loadingDiaplay);
                    textGrade.setTextColor(resource.getColorStateList(R.color.passw_changing));
                    mHandler.sendEmptyMessageDelayed(MESSAGE_LOADING, 700);
                    break;
            }
        }
    };

    @Override
    public void onCheckPasswSuccess(String returnData) {

        mHandler.removeMessages(MESSAGE_LOADING);
        Log.d("test", "密码强度正常" + returnData);
        try {
            JSONObject jsonObject = new JSONObject(returnData);
            int code = jsonObject.getInt("code");
            FunLog.d("test", "code: " + code);
            String msg = jsonObject.getString("msg");
            String grade = jsonObject.getString("grade");
            if (code == 10001) {
                passwordGrade = Integer.valueOf(grade);
                switch (Integer.valueOf(grade)) {
                    case 1:
                        textGrade.setText(R.string.password_checker_weak);
                        textGrade.setTextColor(resource.getColorStateList(R.color.passw_weak));
                        break;
                    case 2:
                        textGrade.setText(R.string.password_checker_medium);
                        textGrade.setTextColor(resource.getColorStateList(R.color.passw_medium));
                        break;
                    case 3:
                        textGrade.setText(R.string.password_checker_strong);
                        textGrade.setTextColor(resource.getColorStateList(R.color.passw_strong));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FunLog.d("test", "JSONObject ERROR");
        }

    }

    @Override
    public void onCheckPasswFailed(int errCode, String returnData) {
        Log.d("test", "获取失败" + returnData);
        if ( null != mHandler ) {
        	mHandler.removeMessages(MESSAGE_LOADING);
        }
        if ( null != textGrade ) {
        	textGrade.setText(R.string.password_checker_weak);
        }
//        FunSupport.getInstance().checkPassw(editPassw.getText().toString());
    }
}
