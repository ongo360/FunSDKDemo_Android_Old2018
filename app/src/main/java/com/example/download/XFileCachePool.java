/*
 * 文件Cache管理
 */
package com.example.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.CRC32;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class XFileCachePool {
	String mDir = null;
	
	public XFileCachePool(String dir)
	{
		mDir = dir;
		
		if ( mDir != null )
		{
			// 如果Cache目录不存在，创建之
			File destDir = new File(mDir);
			if (!destDir.exists())
			{
				destDir.mkdirs();
			}
		}
	}
	
	public Bitmap getPictureFromCache(String urlPath)
	{
		String filePath;
		
		try {
			filePath = getFilePathByURL(urlPath);
			
			if ( null == filePath )
			{
				return null;
			}
			
			File destFile = new File(filePath);
			if (!destFile.exists())
			{
				destFile = null;
				return null;
			}
			
			BitmapFactory.Options option = BitmapOptions.getDefaultBitmapOptions();
			
//			if ( destFile.length() > 1024*1024 )
//			{
//				Log.i("test", "Too Large File : " + urlPath);
//				option.inSampleSize = 8;
//			}
			
			Bitmap bm = BitmapFactory.decodeFile(filePath, option);
			
			destFile = null;
			return bm;
			
		} catch (Exception e) {
			Log.e("test", "getPictureFromCache() Error : " + e.getMessage());
			return null;
		}
	}
	
	public boolean savePictureToCache(String urlPath, InputStream in)
	{
		try {
			String filePath = getFilePathByURL(urlPath);
			
			if ( null == filePath )
			{
				return false;
			}
			
			File destFile = new File(filePath);
			
			FileOutputStream fout = new FileOutputStream(destFile);
			
			byte[] buffer = new byte[8096];
            int len1 = 0;
            int totsize = 0;
            in.skip(0);
            while ((len1 = in.read(buffer)) > 0) {
            	fout.write(buffer, 0, len1);
            	
            	totsize += len1;
            }
            fout.flush();
            fout.close();
            fout = null;
            //Log.d("XFileCachePool", "----> save a file to cache : " + totsize);
            //Log.d("XFileCachePool", "    urlPath = " + urlPath);
            //Log.d("XFileCachePool", "    filePath = " + filePath);
		 } catch (Exception e) {
			 Log.e("XFileCachePool", e.getMessage());
		 }
		 
		return true;
	}
	
	public boolean removeFileCache(String urlPath) {
		try {
			String filePath = getFilePathByURL(urlPath);
			
			if ( null == filePath )
			{
				return false;
			}
			
			File destFile = new File(filePath);
			if ( destFile.exists() ) {
				destFile.delete();
			}
		} catch (Exception e) {
			
		}
		return true;
	}
	
	
	public String getFilePathByURL(String url)
	{
		if ( mDir == null || url == null)
		{
			return null;
		}
		
		CRC32 mCrc = new CRC32();
		long crcVal;
		String fileName;
		
		mCrc.update(url.getBytes());
		crcVal = mCrc.getValue();
		
		fileName = Long.toHexString(crcVal);
		
		return (mDir + "/U" + fileName + ".tmp");
	}
	
}