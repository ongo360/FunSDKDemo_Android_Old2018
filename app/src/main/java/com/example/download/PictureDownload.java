package com.example.download;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;


public class PictureDownload extends Thread {
	
	public interface OnPictureDownloadListener {
		void onPictureDownload(Integer position, Bitmap bmp);
	}

	private int __test_add = 0;
	private int __test_release = 0;
	
	class PictureItem {
		Integer vPosition;					// 与之关联控件
		String purl;			// 图片下载地址
		Bitmap bmp;				// 图片
		boolean isDownloading;	// 是否正在下载
		int tryTimes;			// 重试次数
		int flag;
		
		void release() {
			if ( null != bmp ) {
				bmp.recycle();
				bmp = null;
				__test_release ++;
			}
			isDownloading = false;
			tryTimes = 0;
		}
	}
	
	class ThreadItem {
		boolean used = false;
		XDnldThreadPool dnldThread = null;
		
		ThreadItem() {
			dnldThread = new XDnldThreadPool();
			dnldThread.startTask();
		}
		
		void stopTask() {
			if ( null != dnldThread ) {
				dnldThread.stopTask();
			}
		}
	}
	
	private int mThreadCount = 0;
	private ThreadItem mThreads[];
	
	private Integer mFlag = 0;	// 下载标识计数,一直累加,不重复
	
	private PicDownHandler mHandler = null;
	private List<PictureItem> mItems = new ArrayList<PictureItem>();
	private List<Bitmap> mWaitReleaseBmps = new ArrayList<Bitmap>(); 
	
	
	private final int MESSAGE_GOTO_DOWNLOAD = 0x100;
	private final int MESSAGE_RELEASE_UNUSED_BMPS = 0x101;
	
	private OnPictureDownloadListener mListener = null;
	
	private long mDelayStart = 0;
	private long mDelayMs = 0;
	
	
	public PictureDownload(int threads) {
		if ( threads <= 0 || threads > 10 ) {
			mThreadCount = 1;
		} else {
			mThreadCount = threads;
		}
		
		mThreads = new ThreadItem[mThreadCount];
		for ( int i = 0; i < mThreadCount; i ++ ) {
			mThreads[i] = new ThreadItem();
		}
		
		this.start();
	}
	
	public void setOnPictureDownloadListener(OnPictureDownloadListener l) {
		mListener = l;
	}
	
	/**
	 * 延迟ms毫秒执行下载
	 * @param ms
	 */
	public void delayExec(int ms) {
		mDelayStart = SystemClock.uptimeMillis();
		mDelayMs = ms;
	}
	
	public void release() {
		if ( mThreadCount > 0 && null != mThreads ) {
			for ( int i = 0; i < mThreadCount; i ++ ) {
				if ( null != mThreads[i] ) {
					mThreads[i].stopTask();
					mThreads[i] = null;
				}
			}
			
			mThreadCount = 0;
			mThreads = null;
		}
		
		if ( null != mHandler ) {
			mHandler.getLooper().quit();
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		
		releaseUnUsedBmps();
		
		for ( PictureItem item : mItems ) {
			item.release();
		}
		mItems.clear();
		
		Log.e("test", "----------> __test_add = " + __test_add);
		Log.e("test", "----------> __test_release = " + __test_release);
	}
	
	public void reset() {
		for ( int i = 0; i < mThreadCount; i ++ ) {
			mThreads[i].dnldThread.cleanTasks();
			mThreads[i].used = false;
		}
		
		mHandler.removeCallbacksAndMessages(null);
		
		releaseUnUsedBmps();
		
		for ( PictureItem item : mItems ) {
			item.release();
		}
		mItems.clear();
	}
	
	public void drop(Integer position) {
		synchronized (mItems) {
			PictureItem findItem = null;
			for ( PictureItem item : mItems ) {
				if ( item.vPosition == position ) {
					findItem = item;
					break;
				}
			}
			
			if ( null != findItem ) {
//				synchronized (mWaitReleaseBmps) {
//					mWaitReleaseBmps.add(findItem.bmp);
//					Log.i("test", "---> need release : " + mWaitReleaseBmps.size());
//					mHandler.removeMessages(MESSAGE_RELEASE_UNUSED_BMPS);
//					mHandler.sendEmptyMessageDelayed(MESSAGE_RELEASE_UNUSED_BMPS, 200);
//				}
				findItem.release();
				mItems.remove(findItem);
			}
		}
	}
	public void add(Integer position, String purl) {
		if ( position < 0 || null == purl || purl.length() == 0) {
			return;
		}
		
		synchronized (mItems) {
			PictureItem findItem = null;
			for ( PictureItem item : mItems ) {
				if ( item.vPosition == position ) {
					findItem = item;
					break;
				}
			}
			
			if ( null == findItem ) {
				findItem = new PictureItem();
				findItem.vPosition = position;
				findItem.purl = purl;
				findItem.flag = requestFlag();
				findItem.bmp = null;
				findItem.isDownloading = false;
				findItem.tryTimes = 0;
				
				mItems.add(findItem);
			} else {
				if ( findItem.purl.equals(purl) ) {
					// 没有变化,重复了
				} else {
					// 把BMP添加到释放队列
					if ( null != findItem.bmp ) {
						synchronized (mWaitReleaseBmps) {
							mWaitReleaseBmps.add(findItem.bmp);
							mHandler.removeMessages(MESSAGE_RELEASE_UNUSED_BMPS);
							mHandler.sendEmptyMessageDelayed(MESSAGE_RELEASE_UNUSED_BMPS, 200);
						}
					}
					
					findItem.purl = purl;
					findItem.flag = requestFlag();
					findItem.bmp = null;
					findItem.isDownloading = false;
					findItem.tryTimes = 0;
				}
			}
		}
		
		if ( null != mHandler ) {
			mHandler.removeMessages(MESSAGE_GOTO_DOWNLOAD);
			mHandler.sendEmptyMessageDelayed(MESSAGE_GOTO_DOWNLOAD, 200);
		}
	}
	
	public Bitmap get(Integer position, String purl) {
		Bitmap bmp = null;
		synchronized (mItems) {
			for ( PictureItem item : mItems ) {
				if ( item.vPosition == position && item.purl.equals(purl) ) {
					bmp = item.bmp;
					break;
				}
			}
		}
		return bmp;
	}
	
	private int requestFlag() {
		int flag = 0;
		synchronized (mFlag) {
			flag = mFlag++;
			if ( mFlag > 0xffff ) {
				mFlag = 0;
			}
		}
		return flag;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		
		mHandler = new PicDownHandler();
		
		Looper.loop();
	}
	
	private void releaseUnUsedBmps() {
		synchronized (mWaitReleaseBmps) {
			Log.d("test", "-------------> relese bmps : " + mWaitReleaseBmps.size());
			for ( Bitmap bmp : mWaitReleaseBmps ) {
				bmp.recycle();
				__test_release++;
			}
			mWaitReleaseBmps.clear();
		}
	}
	
	private PictureItem getCanDownloadItem() {
//		if ( !WifiStatCheck.getInstance().canDownloadPicture() ) {
//			return null;
//		}
		
		for ( PictureItem item : mItems ) {
			if ( null == item.bmp 
					&& !item.isDownloading
					&& item.tryTimes < 3 ) {
				return item;
			}
		}
		return null;
	}
	
	private PictureItem getItemByFlag(int flag) {
		for ( PictureItem item : mItems ) {
			if ( flag == item.flag ) {
				return item;
			}
		}
		return null;
	}
	
	private void gotoDownload() {
		
		mHandler.removeMessages(MESSAGE_GOTO_DOWNLOAD);
		
		if ( mDelayStart > 0 && mDelayMs > 0 ) {
			long ctime = SystemClock.uptimeMillis();
			if ( ctime < mDelayStart + mDelayMs ) {
				// 延迟执行时间还没到
				mHandler.sendEmptyMessageDelayed(MESSAGE_GOTO_DOWNLOAD, 100);
				return;
			}
		}
		
		for ( int i = 0; i < mThreadCount; i ++ ) {
			if ( !mThreads[i].used ) {
				// 有空闲的线程
				boolean needNext = true;
				synchronized (mItems) {
					PictureItem item = getCanDownloadItem();
					if ( null != item ) {
						item.isDownloading = true;
						mThreads[i].used = true;
						int flag = (i<<24) | item.flag;
						mThreads[i].dnldThread.addDownloadTask(mHandler, 
								item.purl, flag, XDnldThreadPool.XDNLD_DATATYPE_PICTURE);
						needNext = false;
					}
				}
				if ( !needNext ) {
					// 不用再继续了,没有可下载的了
					return;
				}
			}
		}
	}
	
	private class PicDownHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what) {
			case MESSAGE_GOTO_DOWNLOAD:
				gotoDownload();
				break;
			case MESSAGE_RELEASE_UNUSED_BMPS:
				releaseUnUsedBmps();
				break;
			case XDnldThreadPool.XDNLD_DATATYPE_PICTURE:
				switch(msg.arg1) {
				case XDnldThreadPool.XDNLD_MESSAGE_DOWNLOAD_SUCCESS:
					{
						int threadId = (msg.arg2 >> 24) & 0xff;
						int flag = (msg.arg2 & 0xffff);
						Bitmap bmp = (Bitmap)msg.obj;
						if ( mThreads[threadId].used ) {
							synchronized (mItems ) {
								PictureItem item = getItemByFlag(flag);
								if ( null == item ) {
									// 过期了
									bmp.recycle();
									bmp = null;
								} else {
									item.bmp = bmp;
									item.isDownloading = false;
									__test_add ++;
									// 通知已经图片已经下载
									if ( null != mListener ) {
										mListener.onPictureDownload(item.vPosition, item.bmp);
									}
								}
							}
							
							mThreads[threadId].used = false;
							
							gotoDownload();
						} else {
							bmp.recycle();
							bmp = null;
						}
					}
					break;
				case XDnldThreadPool.XDNLD_MESSAGE_DOWNLOAD_FAILED:
					{
						int threadId = (msg.arg2 >> 24) & 0xff;
						int flag = (msg.arg2 & 0xffff);
						synchronized (mItems ) {
							PictureItem item = getItemByFlag(flag);
							if ( null != item ) {
								item.tryTimes ++;
								item.isDownloading = false;
							}
						}
						
						mThreads[threadId].used = false;
						
						gotoDownload();
					}
					break;
				}
				break;
			}
		}
	}
}
