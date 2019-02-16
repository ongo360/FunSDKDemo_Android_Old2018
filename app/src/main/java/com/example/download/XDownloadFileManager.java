package com.example.download;


public class XDownloadFileManager {

	private static String mFileCachePath = null;
	private static XFileCacheManager mFileCacheManager = null;
	
	/**
	 * 设置图片下载等的本地缓存目录,缓存大小(可以加速图片显示,并节省用户流量)
	 * @param cachePath
	 * @param cacheMaxSize
	 */
	public static void setFileManager(String cachePath, long cacheMaxSize) {
		if ( null != cachePath && cacheMaxSize > 0 ) {
			mFileCachePath = cachePath;
			
			if ( null == mFileCacheManager )
			{
				mFileCacheManager = new XFileCacheManager(mFileCachePath, cacheMaxSize);
			}
		}
	}
	
	public static String getFileCachePath() {
		return mFileCachePath;
	}
}
