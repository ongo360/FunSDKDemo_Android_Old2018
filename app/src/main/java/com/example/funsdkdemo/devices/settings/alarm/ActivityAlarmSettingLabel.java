package com.example.funsdkdemo.devices.settings.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funsdkdemo.R;
import com.lib.FunSDK;

public class ActivityAlarmSettingLabel extends Activity {

	private EditText mLabel;
	private TextView mSaveLabel;
	private String label;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_label);
		Animation a = AnimationUtils.loadAnimation(this, R.anim.popshow_anim);
		// mButtomLayout.setAnimation(a);
		mSaveLabel = (TextView) this.findViewById(R.id.tv_sure);
		mLabel = (EditText) this.findViewById(R.id.set_label);
		mSaveLabel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				label = mLabel.getText().toString();
				if (null != label && label != "") {
					Intent intent = new Intent();
					intent.putExtra("Label", label);
					ActivityAlarmSettingLabel.this.setResult(3, intent);
					finish();
				} else {
					Toast.makeText(getApplication(), FunSDK.TS("label_tip"), Toast.LENGTH_SHORT).show();
				}
			}

		});
	}

}
