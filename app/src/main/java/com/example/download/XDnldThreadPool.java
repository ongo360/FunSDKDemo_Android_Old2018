
/*
 * 下载池，目前暂时只实现一个现在线程，后续扩展为多个线程同时下载
 */
package com.example.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class XDnldThreadPool extends Thread{

	// 消息定义
	public static final int XDNLD_SERVICE_MESSAGE_START = 0x8001;
//	public static final int XDNLD_SERVICE_MESSAGE_STOP	= 0x8002;
//	public static final int XDNLD_SERVICE_MESSAGE_TIMER	= 0x8003;
	public static final int XDNLD_SERVICE_MESSAGE_DOWNLOAD	= 0x8004;

	// 对外消息定义
	public static final int XDNLD_MESSAGE_DOWNLOAD_BEGIN	= 0x9000;
	public static final int XDNLD_MESSAGE_DOWNLOAD_SUCCESS	= 0x9001;	// 数据下载成功
	public static final int XDNLD_MESSAGE_DOWNLOAD_FAILED	= 0x9002;	// 数据下载失败
	public static final int XDNLD_MESSAGE_DOWNLOAD_PROGRESS	= 0x9003;	// 数据下载进度

	// 各Activity自定义消息
	public static final int XDNLD_MESSAGE_ACTIVITY_DEFINED	= 0xA000;
	public static final int XDNLD_MESSAGE_XMLPARSER_FINISH = 0xA001;
	public static final int XDNLD_MESSAGE_PROGRESS_SHOW = 0xA002;

	// 数据类型定义
	public static final int XDNLD_DATATYPE_STRING 	= 0x0001;
	public static final int XDNLD_DATATYPE_PICTURE 	= 0x0002;
	public static final int XDNLD_DATATYPE_DATA 	= 0x0003;
	public static final int XDNLD_DATATYPE_POST		= 0x0004;
	public static final int XDNLD_DATATYPE_APPLICATION = 0x0005;

	// 超时时间定义
	public static final int XDNLD_DOWNLOAD_TIMEOUT = 20000;
	public static final int XDNLD_DOWNLOAD_DATA_TIMEOUT = 20000;

	static final String TAG = "XDnldThread";

	private XDnldThreadHandler mDnldThreadHandler = null;

	private XFileCachePool mFileCache = null;
	private boolean mExitStream = false;

	private List<XDnldTask> mTaskList = new ArrayList<XDnldTask>();

	public XDnldThreadPool()
	{
		this.start();
	}

	public void startTask()
	{
//		do {
//			if (mDnldThreadHandler != null)
//				break;
//
//			SystemClock.sleep(100);
//
//		} while (true);

		synchronized (this){
			mExitStream = false;
		}

//		sendMessage(XDNLD_SERVICE_MESSAGE_START);
	}

	public void stopTask()
	{
		try {
			synchronized (this){
				mExitStream = true;
				//Log.d(TAG,"===>mExitStream="+mExitStream);
			}

			mDnldThreadHandler.removeCallbacksAndMessages(null);
			mDnldThreadHandler.getLooper().quit();

			// 等待退出
			int tryTimes = 20;
			while(mDnldThreadHandler != null && tryTimes-->0) {
				SystemClock.sleep(10);
			}
			mDnldThreadHandler = null;

			synchronized (mTaskList) {
				if( null != mTaskList && 0 < mTaskList.size() )
				{
					for (int j = 0; j < mTaskList.size(); j++)
					{
						mTaskList.get(j).mHandler = null;
					}
				}
				mTaskList.clear();
				mTaskList = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cleanTasks() {
		if ( null != mDnldThreadHandler ) {
			mDnldThreadHandler.removeCallbacksAndMessages(null);
		}

		synchronized (mTaskList) {
			for ( XDnldTask task : mTaskList ) {
				task.cancel();
				task.mHandler = null;
			}
			mTaskList.clear();
		}
	}

	public void run() {
//		Log.i(TAG, "XDnldThreadPool run......");

		// 设置下载任务的优先级
		Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);

		if ( !mExitStream ) {
			// 初始化消息循环队列，需要在Handler创建之前
			Looper.prepare();

			mDnldThreadHandler = new XDnldThreadHandler();
//			Log.d(TAG,"dnld handler start....");
			// 启动子线程消息循环队列
			Looper.loop();
		}

		if ( null != mDnldThreadHandler ) {
			mDnldThreadHandler.removeCallbacksAndMessages(null);
			mDnldThreadHandler = null;
		}

//		Log.e(TAG, "XDnldThreadPool over......");

		return;
	}

	// 事件处理
	public class XDnldThreadHandler extends Handler {

		// 处理服务器的命令
		public String translationResponse(String line) {
			Log.d(TAG, "Receive:" + line);
			return line;
		}

		// 处理主线程的消息
		@Override
		public void handleMessage(Message message) {

			switch (message.what) {

			case XDNLD_SERVICE_MESSAGE_START:
//				Log.d(TAG, "XDnldThreadHandler->" + "message start");

				//monitorStart();

				// 触发一个超时消息
//				TimerEvent();

				break;

//			case XDNLD_SERVICE_MESSAGE_TIMER:
//				// 发送一个自处理的超时消息
//				TimerEvent();
//				break;

			case XDNLD_SERVICE_MESSAGE_DOWNLOAD:
				XDnldTask dnTask = (XDnldTask)message.obj;

				if ( dnTask != null )
				{
					if( null != mTaskList )
					{
						synchronized (mTaskList) {
							mTaskList.add(dnTask);
						}
					}
					dnTask.done();

					dnTask.mHandler = null;

					if( null != mTaskList )
					{
						synchronized (mTaskList) {
							mTaskList.remove(dnTask);
						}
					}
				}
				else
				{
					Log.e(TAG, " serious error, XDnldTask is empty.");
				}
				break;
			default:
				break;
			}

			super.handleMessage(message);
		}
	}

	private boolean checkThreadHandler() {

		if ( null != mDnldThreadHandler ) {
			return true;
		}

		int tryTimes = 0;
		while ( null == mDnldThreadHandler && tryTimes++<10 ) {
			SystemClock.sleep(10);
		}

		return null != mDnldThreadHandler;

	}

	public boolean sendMessage(int what)
	{
		if ( !checkThreadHandler() )
		{
			Log.e(TAG, "sendMessage() Download Thread Handler is null");
			return false;
		}

		Message cmdmsg = mDnldThreadHandler.obtainMessage();

		cmdmsg.what = what;

		return mDnldThreadHandler.sendMessage(cmdmsg);
	}

	public boolean sendMessage(int what, Object obj)
	{
		if ( !checkThreadHandler() )
		{
			Log.e(TAG, "sendMessage2()Download Thread Handler is null");
			return false;
		}

		Message cmdmsg = mDnldThreadHandler.obtainMessage();

		cmdmsg.what = what;
		cmdmsg.obj = obj;

		return mDnldThreadHandler.sendMessage(cmdmsg);
	}

	public boolean sendMessageDelayed(int what, Object obj, int delayMs)
	{
		if ( !checkThreadHandler() )
		{
			Log.e(TAG, "sendMessage2()Download Thread Handler is null");
			return false;
		}

		Message cmdmsg = mDnldThreadHandler.obtainMessage();

		cmdmsg.what = what;
		cmdmsg.obj = obj;

		return mDnldThreadHandler.sendMessageDelayed(cmdmsg, delayMs);
	}

	public boolean sendOutputMessage(Handler hl, int dataType, int msg, int out_arg2, Object obj)
	{
		if ( hl == null )
		{
			Log.e(TAG, "sendOutputMessage() Download Thread Handler is null");
			return false;
		}

		Message cmdmsg = hl.obtainMessage();

		cmdmsg.what = dataType;
		cmdmsg.arg1 = msg;
		cmdmsg.arg2 = out_arg2;
		cmdmsg.obj = obj;

		return hl.sendMessage(cmdmsg);
	}

	public boolean sendOutputMessage(Handler hl, int dataType, int msg, int out_arg2)
	{
		if ( hl ==  null )
		{
			Log.e(TAG, "sendOutputMessage2() Download Thread Handler is null");
			return false;
		}

		Message cmdmsg = hl.obtainMessage();

		cmdmsg.what = dataType;
		cmdmsg.arg1 = msg;
		cmdmsg.arg2 = out_arg2;

		return hl.sendMessage(cmdmsg);
	}

	// 添加一个下载任务
	// outputHandler : 下载完成后消息发送句柄
	// dnldUrl : 下载URL
	public boolean addDownloadTask(Handler outputHandler, String dnldUrl, int out_arg2, int dataType)
	{
		if ( null == dnldUrl )
		{
			Log.e(TAG, "dnldUrl is null");
			if ( null != outputHandler ) {
				Message errMsg = new Message();
				errMsg.what = dataType;
				errMsg.arg1 = XDNLD_MESSAGE_DOWNLOAD_FAILED;
				errMsg.arg2 = out_arg2;
				outputHandler.sendMessage(errMsg);
			}
			return false;
		}

		// 特殊处理，认为是非法的URL
		if ( dnldUrl.length() < 2 )
		{
			Log.e(TAG, "dnldUrl is error : " + dnldUrl);
			return false;
		}

		XDnldTask dnldTask = new XDnldTask();

		dnldTask.mHandler = outputHandler;
		dnldTask.mUrl = dnldUrl;
		dnldTask.mOutArg2 = out_arg2;
		dnldTask.mDataType = dataType;
		dnldTask.mPicWidth = 0;
		dnldTask.mPicHeight = 0;

		return sendMessage(XDNLD_SERVICE_MESSAGE_DOWNLOAD, dnldTask);
	}

	public boolean addDownloadTask(Handler outputHandler, String dnldUrl, int out_arg2, int dataType, String encode)
	{
		if ( null == dnldUrl )
		{
			Log.e(TAG, "dnldUrl is null");
			return false;
		}

		// 特殊处理，认为是非法的URL
		if ( dnldUrl.length() < 2 )
		{
			Log.e(TAG, "dnldUrl is error : " + dnldUrl);
			return false;
		}

		XDnldTask dnldTask = new XDnldTask();

		dnldTask.mHandler = outputHandler;
		dnldTask.mUrl = dnldUrl;
		dnldTask.mEncode = encode;
		dnldTask.mOutArg2 = out_arg2;
		dnldTask.mDataType = dataType;
		dnldTask.mPicWidth = 0;
		dnldTask.mPicHeight = 0;

		return sendMessage(XDNLD_SERVICE_MESSAGE_DOWNLOAD, dnldTask);
	}

	public boolean addDownloadTaskDelayed(Handler outputHandler, String dnldUrl, int out_arg2, int dataType, int delayMs)
	{
		if ( null == dnldUrl )
		{
			Log.e(TAG, "dnldUrl is null");
			return false;
		}

		// 特殊处理，认为是非法的URL
		if ( dnldUrl.length() < 2 )
		{
			Log.e(TAG, "dnldUrl is error : " + dnldUrl);
			return false;
		}

		XDnldTask dnldTask = new XDnldTask();

		dnldTask.mHandler = outputHandler;
		dnldTask.mUrl = dnldUrl;
		dnldTask.mOutArg2 = out_arg2;
		dnldTask.mDataType = dataType;
		dnldTask.mPicWidth = 0;
		dnldTask.mPicHeight = 0;

		return sendMessageDelayed(XDNLD_SERVICE_MESSAGE_DOWNLOAD, dnldTask, delayMs);
	}

	public boolean addDownloadTask(Handler outputHandler, String dnldUrl, int out_arg2, int dataType, int picScalWidth, int picScalHeight)
	{
		if ( null == dnldUrl )
		{
			Log.e(TAG, "dnldUrl is null");
			return false;
		}

		// 特殊处理，认为是非法的URL
		if ( dnldUrl.length() < 2 )
		{
			Log.e(TAG, "dnldUrl is error : " + dnldUrl);
			return false;
		}

		XDnldTask dnldTask = new XDnldTask();

		dnldTask.mHandler = outputHandler;
		dnldTask.mUrl = dnldUrl;
		dnldTask.mOutArg2 = out_arg2;
		dnldTask.mDataType = dataType;
		dnldTask.mPicWidth = picScalWidth;
		dnldTask.mPicHeight = picScalHeight;

		return sendMessage(XDNLD_SERVICE_MESSAGE_DOWNLOAD, dnldTask);
	}

	/*
	 * 增加播放记录：auctionId，resourceId，playTime，boxSn
	 * thboxdomain/addCollection.xml
	 * post提交表单参数：1.boxSn 淘花盒序列号，2.auctionId，收藏的商品Id
	 * 返回标准的xml，成功时is-success为true，否则为false，并且带上错误信息
	 */
	public boolean addPostTask(Handler outputHandler,
			int out_arg2,
			String postUrl,
			List<BasicNameValuePair> nameValueList)
	{
		XDnldTask dnldTask = new XDnldTask();

		dnldTask.mHandler = outputHandler;
		dnldTask.mUrl = postUrl;
		dnldTask.mOutArg2 = out_arg2;
		dnldTask.mDataType = XDNLD_DATATYPE_POST;
		dnldTask.mPicWidth = 0;
		dnldTask.mPicHeight = 0;
		dnldTask.mNameValueList = nameValueList;
		return sendMessage(XDNLD_SERVICE_MESSAGE_DOWNLOAD, dnldTask);
	}

	public boolean addPostTask(Handler outputHandler,
			int out_arg2,
			String postUrl,
			List<BasicNameValuePair> nameValueList,
			int delayMs)
	{
		XDnldTask dnldTask = new XDnldTask();

		dnldTask.mHandler = outputHandler;
		dnldTask.mUrl = postUrl;
		dnldTask.mOutArg2 = out_arg2;
		dnldTask.mDataType = XDNLD_DATATYPE_POST;
		dnldTask.mPicWidth = 0;
		dnldTask.mPicHeight = 0;
		dnldTask.mNameValueList = nameValueList;
		return sendMessageDelayed(XDNLD_SERVICE_MESSAGE_DOWNLOAD, dnldTask, delayMs);
	}

	public boolean addDownloadAppTask(Handler outputHandler,
			String dnldUrl,
			int out_arg2,
			String cacheFile)
	{
		XDnldTask dnldTask = new XDnldTask();

		dnldTask.mHandler = outputHandler;
		dnldTask.mUrl = dnldUrl;
		dnldTask.mOutArg2 = out_arg2;
		dnldTask.mDataType = XDNLD_DATATYPE_APPLICATION;
		dnldTask.mPicWidth = 0;
		dnldTask.mPicHeight = 0;
		dnldTask.mCacheFilePath = cacheFile;
		return sendMessage(XDNLD_SERVICE_MESSAGE_DOWNLOAD, dnldTask);
	}


	public boolean removeAllTask()
	{
		if ( null != mDnldThreadHandler ) {
			mDnldThreadHandler.removeMessages(XDNLD_SERVICE_MESSAGE_DOWNLOAD);
		}
		return true;
	}

//	private void TimerEvent()
//	{
//		// 初始化消息体,发送消息给自己
//		mDnldThreadHandler.sendEmptyMessageDelayed(XDNLD_SERVICE_MESSAGE_TIMER, 2000);
//	}

	// 数据下载任务节点定义
	private class XDnldTask {
		public String mUrl;
		public String mEncode = null;
		public Handler mHandler;
		public int mDataType;
		public int mOutArg2;

		// 图片下载时使用
		public int mPicWidth;
		public int mPicHeight;

		//下载备份文件(下载应用程序时使用)
		public String mCacheFilePath;

		public List<BasicNameValuePair> mNameValueList = null;

		public boolean mToCancel = false;

		public XDnldTask()
		{
			mPicWidth = 0;
			mPicHeight = 0;
			mCacheFilePath = null;
		}

		public void cancel() {
			mToCancel = true;
		}

		public void done()
		{
			sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_BEGIN, mOutArg2);

			switch(mDataType)
			{
			case XDNLD_DATATYPE_STRING:
				String resultStr = downLoadLink(mUrl, XDNLD_DOWNLOAD_TIMEOUT, mEncode);
				if ( !mToCancel && !mExitStream ) {
					if ( resultStr == null ) {
						sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_FAILED, mOutArg2);
						Log.e(TAG, "Download String File Failed!["+mUrl+"]");
					} else {
						sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_SUCCESS, mOutArg2, resultStr);
						resultStr = null;//add by wxl, out of memory
						//Log.i(TAG, "Download String File Success!");
					}
				}
				break;
			case XDNLD_DATATYPE_PICTURE:
				Bitmap resultBmp = null;

				if ( mUrl.startsWith("file://") ) {
					// 打开本地图片
					try {
						String localPath = mUrl.substring("file:/".length());
						resultBmp = BitmapFactory.decodeFile(localPath);
					} catch (Exception e) {
						resultBmp = null;
					}
				} else {
					if ( getFileCache() != null )
					{
						resultBmp = getFileCache().getPictureFromCache(mUrl);
					}

					if ( resultBmp == null )
					{
						resultBmp = downLoadPicture(mUrl, XDNLD_DOWNLOAD_TIMEOUT);
					}
				}

				// 如果已经退出的状态，立即返回
				if ( mExitStream || mToCancel ) {
					if ( null != resultBmp ) {
						resultBmp.recycle();
						resultBmp = null;
					}
					return;
				}

				if( null != resultBmp )
				{
					if ( mPicWidth > 0 && mPicHeight > 0)
					{
						Matrix matrix = new Matrix();
						float wScal = (float)mPicWidth/resultBmp.getWidth();
						float hScal = (float)mPicHeight/resultBmp.getHeight();

						matrix.postScale(wScal, hScal);

						Bitmap scalBmp = Bitmap.createBitmap(resultBmp, 0, 0, resultBmp.getWidth(), resultBmp.getHeight(), matrix,true);

						resultBmp.recycle();
						resultBmp = null;

						resultBmp = scalBmp;
					}
				}

				if ( null == resultBmp )
				{
					sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_FAILED, mOutArg2);
					Log.e(TAG, "Download Picture File Failed!["+mOutArg2+"][" + mUrl +"]");
				}
				else
				{
					sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_SUCCESS, mOutArg2, resultBmp);
					resultBmp = null;
				}
				break;
			case XDNLD_DATATYPE_POST:
				String postResult = postLink(mUrl, XDNLD_DOWNLOAD_TIMEOUT, mNameValueList);
				if ( !mToCancel && !mExitStream ) {
					if ( postResult == null ) {
						sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_FAILED, mOutArg2);
						Log.e(TAG, "Download String File Failed!["+mUrl+"]");
					} else {
						sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_SUCCESS, mOutArg2, postResult);
						//Log.i(TAG, "Download String File Success!");
					}
				}
				break;
			case XDNLD_DATATYPE_APPLICATION://下载应用程序
				boolean result = downLoadApplication(this,mUrl,mCacheFilePath, XDNLD_DOWNLOAD_TIMEOUT);
				if ( !mToCancel && !mExitStream ) {
					if ( result == false ) {
						sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_FAILED, mOutArg2);
						Log.e(TAG, "Download application File Failed!["+mUrl+"]");
					} else {
						sendOutputMessage(mHandler, mDataType, XDNLD_MESSAGE_DOWNLOAD_SUCCESS, mOutArg2);
						//Log.i(TAG, "Download String File Success!");
					}
				}
				break;
			default:
				break;
			}
		}

		private String downLoadLink(String link, int timeoutConnection, String encode)
	    {
			try {
				Log.d("test", "link : " + link);

				URL url = new URL(link);
				HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setConnectTimeout(timeoutConnection);
				httpConnection.setReadTimeout(timeoutConnection);

				if ( HttpURLConnection.HTTP_OK != httpConnection.getResponseCode() ) {
					Log.e(TAG,"downLoadLink---> http not ok");
					return null;
				}

				String askEncode = "UTF-8";
				if ( encode != null && encode.length() > 0 ) {
					askEncode = encode;
				}
				StringBuilder Builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), askEncode));
				for (String s = reader.readLine(); s != null && !mToCancel && !mExitStream; s = reader.readLine())
				{
					Builder.append(s);
					s = null;//add by wxl, out of memory
	            }
				reader.close();
				httpConnection.disconnect();
				httpConnection = null;
				return Builder.toString();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Download Link File Error : " + e.getMessage());
			}

	    	return null;
	    }

		private Bitmap downLoadPicture(String picUrl, int timeout)
		{
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
			HttpConnectionParams.setSoTimeout(httpParameters, XDNLD_DOWNLOAD_DATA_TIMEOUT); //读数据超时
			HttpClient client = new DefaultHttpClient(httpParameters);

			// 消息/进度通知
			// ......
			try {
				picUrl = picUrl.replace(" ", "%20");
				HttpGet getcfg = new HttpGet( picUrl );
				HttpResponse response = client.execute(getcfg);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					HttpEntity entity = response.getEntity();
					if ( entity != null )
					{
						InputStream inputStream = entity.getContent();
						byte[] buff = new byte[8000];
						int bytesRead = 0;
						ByteArrayOutputStream bao = new ByteArrayOutputStream();
						while((bytesRead = inputStream.read(buff)) != -1) {
				             bao.write(buff, 0, bytesRead);
				          }

						byte[] input_data = bao.toByteArray();
						BitmapFactory.Options option = BitmapOptions.getDefaultBitmapOptions();

//						if ( input_data.length > 1024*1024 )
//						{
//							Log.i(TAG, "Too Large File : " + picUrl);
//							option.inSampleSize = 8;
//						}
						ByteArrayInputStream bin = new ByteArrayInputStream(input_data);

						Bitmap srcImage = BitmapFactory.decodeStream(bin,
								BitmapOptions.getDefaultBitmapOutPadding(),
								option);
						//Bitmap srcImage = BitmapFactory.decodeStream(bin);

						if ( srcImage == null )
						{
							Log.e(TAG, "..... BitmapFactory.decodeStream failed ");
							try {
								if( null != bao )
								{
									bao.close();
									bao = null;
								}
								if( null != bin )
								{
									bin.close();
									bin = null;
								}
								client.getConnectionManager().shutdown();
								client = null;
							} catch (Exception e) {

							}
							return null;
						}

						//Log.d("test", "-----------srcImage.hasAlpha() = " + srcImage.hasAlpha());
						//Log.d("test", "------downLoadPicture()-----srcImage.getConfig() = " + srcImage.getConfig());
						if ( getFileCache() != null )
						{
							ByteArrayInputStream binsv = new ByteArrayInputStream(input_data);
							getFileCache().savePictureToCache(picUrl, binsv);
						}

//						if ( mPicWidth > 0 && mPicHeight > 0)
//						{
//							Matrix matrix = new Matrix();
//							float wScal = (float)mPicWidth/srcImage.getWidth();
//							float hScal = (float)mPicHeight/srcImage.getHeight();
//							float mScal = Math.max(wScal, hScal);
//
//							matrix.postScale(mScal, mScal);
//
//							Bitmap scalImage = Bitmap.createBitmap(srcImage, 0, 0, srcImage.getWidth(), srcImage.getHeight(), matrix,true);
//
//							srcImage.recycle();
//							srcImage = null;
//
//							try {
//								client.getConnectionManager().shutdown();
//								client = null;
//							} catch (Exception e) {
//
//							}
//							return scalImage;
//						}
//						else
//						{
						try {
							if( null != bao )
							{
								bao.close();
								bao = null;
							}
							if( null != bin )
							{
								bin.close();
								bin = null;
							}
							client.getConnectionManager().shutdown();
							client = null;
						} catch (Exception e) {

						}
						return srcImage;
//						}
					}
				}
				else
				{
					try {
						client.getConnectionManager().shutdown();
						client = null;
					} catch (Exception e) {

					}
					return null;
				}

	        } catch (Exception e) {
	        	Log.e(TAG, "downloadLink() Error : " + e.getMessage());
	        }

			if ( null != client ) {
				try {
					client.getConnectionManager().shutdown();
					client = null;
				} catch (Exception e) {

				}
			}
			return null;
		}

		//下载应用程序
		private boolean downLoadApplication(XDnldTask dnld_task,String appUrl,String cacheFile, int timeout)
		{
//			Log.i(TAG, "..... appUrl = " + appUrl);
			if(cacheFile == null)
			{
				return false;
			}

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout); //链接超时
			HttpConnectionParams.setSoTimeout(httpParameters, XDNLD_DOWNLOAD_DATA_TIMEOUT); //读数据超时
			HttpClient client = new DefaultHttpClient(httpParameters);

			// 消息/进度通知
			HttpGet getcfg = new HttpGet(appUrl);

			try {
				HttpResponse response = client.execute(getcfg);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					HttpEntity entity = response.getEntity();
					if ( entity != null )
					{
						long app_len = entity.getContentLength();
						long download_len = 0;
						long percent = 0;
						if(app_len == 0)//
						{
//							Log.d(TAG,"error,app_len = "+app_len);
							try {
								client.getConnectionManager().shutdown();
								client = null;
							} catch (Exception e) {

							}
							return false;
						}

						InputStream inputStream = entity.getContent();

						//打开缓冲文件
						File cache_file = new File(cacheFile);
						Log.d(TAG,"download app,cacheFile = "+cacheFile);
						cache_file.createNewFile();
						FileOutputStream fos = new  FileOutputStream(cache_file);

						//读写文件
						byte[] buff = new byte[8000];
						int bytesRead = 0;
						while((bytesRead = inputStream.read(buff)) > 0 && mExitStream == false && mToCancel == false) {
							fos.write(buff, 0, bytesRead);
							download_len += bytesRead;

							//Log.d(TAG,"download_len = "+download_len+",app_len="+app_len+",mExitStream="+mExitStream);

							if(percent != download_len*100/app_len)
							{
								percent = download_len*100/app_len;
								sendOutputMessage(dnld_task.mHandler, dnld_task.mDataType, XDNLD_MESSAGE_DOWNLOAD_PROGRESS, dnld_task.mOutArg2,percent);
							}
							sleep(10);
				          }
						fos.close();
						//inputStream.close();

						try {
							client.getConnectionManager().shutdown();
							client = null;
						} catch (Exception e) {

						}
						return download_len == app_len;
					}
				}
				else
				{
					try {
						client.getConnectionManager().shutdown();
						client = null;
					} catch (Exception e) {

					}
					return false;
				}

	        } catch (Exception e) {
	        	Log.e(TAG, "downLoadApplication() Error : " + e.getMessage());
	        	e.printStackTrace();

	        }

			if ( client != null ) {
				try {
					client.getConnectionManager().shutdown();
					client = null;
				} catch (Exception e) {

				}
			}
			return false;
		}
	}




	public static String downLoadLink_bck(String link, int timeoutConnection)
    {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, XDNLD_DOWNLOAD_DATA_TIMEOUT); //读数据超时
		HttpClient client = new DefaultHttpClient(httpParameters);
		StringBuilder Builder = new StringBuilder();

//		Log.d(TAG, "Getting Link : " + link);

		// Link下载统一加BoxSN
		//link = XUniviewCom.getUrlWithBoxSn(link);

		HttpGet getcfg = new HttpGet(link);


		try {
				HttpResponse response = client.execute(getcfg);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					BufferedReader reader = new BufferedReader(new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
					for (String s = reader.readLine(); s != null; s = reader.readLine())
					{
						Builder.append(s);
		            }

					try {
						client.getConnectionManager().shutdown();
						client = null;
					} catch (Exception e) {

					}
					return Builder.toString();
				}
				else
				{
					Log.e(TAG, "Download Failed, link=["+link+"]");

					try {
						client.getConnectionManager().shutdown();
						client = null;
					} catch (Exception e) {

					}
					return null;
				}

		} catch (Exception e) {
	        	Log.e(TAG, "Download Link File Error : " + e.getMessage());
		}

		if ( client != null ) {
			try {
				client.getConnectionManager().shutdown();
				client = null;
			} catch (Exception e) {

			}
		}
    	return null;
    }

	private String postLink(String link, int timeoutConnection, List<BasicNameValuePair> nameValue)
    {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, XDNLD_DOWNLOAD_DATA_TIMEOUT); //读数据超时
		HttpClient client = new DefaultHttpClient(httpParameters);
		StringBuilder Builder = new StringBuilder();

		HttpPost postcfg = new HttpPost(link);


		try {
				if ( null != nameValue )
				{
//					for ( int i = 0; i < nameValue.size(); i ++ )
//					{
//						Log.d(TAG, "Post Link : name = " + nameValue.get(i).getName() + "  value = " + nameValue.get(i).getValue());
//					}
					UrlEncodedFormEntity form = new UrlEncodedFormEntity(nameValue, "UTF-8");
					form.setContentEncoding(HTTP.UTF_8);
					postcfg.setEntity(form);

				}

				HttpResponse response = client.execute(postcfg);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					BufferedReader reader = new BufferedReader(new InputStreamReader(
								response.getEntity().getContent()));
					for (String s = reader.readLine(); s != null; s = reader.readLine())
					{
						Builder.append(s);
		            }

					try {
						client.getConnectionManager().shutdown();
						client = null;
					} catch (Exception e) {

					}
					return Builder.toString();
				}
				else
				{
					Log.e(TAG, "Download Failed, link=["+link+"]");

					try {
						client.getConnectionManager().shutdown();
						client = null;
					} catch (Exception e) {

					}
					return null;
				}

		} catch (Exception e) {
	        	Log.e(TAG, "Download Link File Error : " + e.getMessage());
		}

		if ( null != client ) {
			try {
				client.getConnectionManager().shutdown();
				client = null;
			} catch (Exception e) {

			}
		}
    	return null;
    }


	private XFileCachePool getFileCache()
	{
		if ( mFileCache == null )
		{
			if ( null != XDownloadFileManager.getFileCachePath() ) {
				mFileCache = new XFileCachePool(XDownloadFileManager.getFileCachePath());
			}
		}

		return mFileCache;
	}
}