package com.example.download;

import java.io.File;

import android.os.SystemClock;
import android.util.Log;

public class XFileCacheManager extends Thread{
	private static final String TAG = "XFileCacheManager";
	
	private File mDestDir = null;
	private long mMaxSize = 1000000;
	
	public XFileCacheManager(String dir, long maxSize)
	{
		File destDir = new File(dir);
		if (destDir.exists())
		{
			mDestDir = destDir;
		}
		
		mMaxSize = maxSize;
		
		this.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long sleepTime = 10000;
		long percent = 0;
		
		Log.d(TAG, "XFileCacheManager.run() [mMaxSize : " + mMaxSize + "]");
		
		while ( true )
		{
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				percent = checkCacheSize();
				
				Log.d(TAG, "    FileCache percent : " + percent + "%");
				
				if ( percent < 80 )
				{
					sleepTime = 3600000;	// 一小时
				}
				else if ( percent < 95 )
				{
					sleepTime = 60000;		// 一分钟
				}
				else
				{
					// 清空
					reBuildDir();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Log.d("test", "-------------------> exit XFileCacheManager.run()");
	}
	
	
	private long getFileSize(File f)throws Exception//取得文件夹大小
    {
		if ( null == f )
		{
			return 0;
		}
		
		if ( !f.exists() )
		{
			return 0;
		}
		
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size = size + getFileSize(flist[i]);
            } else
            {
                size = size + flist[i].length();
            }
        }
        
        return size;
    }
	
	private long checkCacheSize() throws Exception
	{
		long cacheSize = 0;
		long time_s = SystemClock.uptimeMillis();
		
		cacheSize = getFileSize(mDestDir);
		
		long time_e = SystemClock.uptimeMillis();
		
		Log.d(TAG, "    Get FileCache Size, cacheSize = " + cacheSize + "  used time : " + (time_e - time_s));
		
		return (cacheSize*100/mMaxSize);
	}
	
	private void DeleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            DeleteRecursive(child);

	    fileOrDirectory.delete();
	}

	
	private boolean reBuildDir()
	{
		long time_s = SystemClock.uptimeMillis();
		
		if ( mDestDir != null )
		{
			DeleteRecursive(mDestDir);
			
			boolean result = mDestDir.mkdir();
			
			Log.d(TAG, "    mkdir : " + result);
		}
		
		long time_e = SystemClock.uptimeMillis();
		
		Log.d(TAG, "    reBuild Cache,  used time : " + (time_e - time_s));
		
		return true;
	}
}