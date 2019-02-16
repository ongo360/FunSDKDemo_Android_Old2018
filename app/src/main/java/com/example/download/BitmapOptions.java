package com.example.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class BitmapOptions {

	static BitmapFactory.Options mBitmapDecodeOptions = null;
	public static BitmapFactory.Options getDefaultBitmapOptions()
	{
		if ( mBitmapDecodeOptions == null )
		{
			mBitmapDecodeOptions = new BitmapFactory.Options();
			mBitmapDecodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		}
		
		mBitmapDecodeOptions.inSampleSize = 0;
		
		return mBitmapDecodeOptions;
	}
	
	static Rect mBitmapDecodeOutPadding = null;
	public static Rect getDefaultBitmapOutPadding()
	{
		if ( null == mBitmapDecodeOutPadding )
		{
			mBitmapDecodeOutPadding = new Rect(-1, -1, -1, -1);
		}
		return mBitmapDecodeOutPadding;
	}
	
}
