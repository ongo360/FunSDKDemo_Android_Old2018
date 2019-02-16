package com.example.funsdkdemo;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ActivityGuide extends ActivityDemo implements OnItemClickListener, OnClickListener{

	private ListView mListView = null;
	private ListAdapterGuideModule mAdapter = null;
	
	protected TextView mTextTitle = null;
	protected ImageButton mBtnBack = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_demo);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mListView = (ListView)findViewById(R.id.listViewGuide);
		mAdapter = new ListAdapterGuideModule(this, getGuideModules());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
		String title = getIntent().getStringExtra("TITLE");
		if ( null != title ) {
			mTextTitle.setText(title);
		}
		
		boolean canBack = getIntent().getBooleanExtra("CAN_BACK", false);
		if ( canBack ) {
			mBtnBack.setVisibility(View.VISIBLE);
		} else {
			mBtnBack.setVisibility(View.GONE);
		}
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	protected abstract List<DemoModule> getGuideModules();
	
	
	protected DemoModule getGuideModule(int position) {
		if ( position >= 0 && position < getGuideModules().size() ) {
			return getGuideModules().get(position);
		}
		return null;
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		try {
			if ( getGuideModule(position).startModule(this) ) {
				// 点击事件已经处理
				return;
			}
			
			// 还没有处理，子类可以重载onDemoItemClick()函数来处理
			onDemoItemClick(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(View v) {
		if ( v.getId() == mBtnBack.getId() ) {
			finish();
		}
	}
	
	protected void onDemoItemClick(int position) {
		
	}
	
}
