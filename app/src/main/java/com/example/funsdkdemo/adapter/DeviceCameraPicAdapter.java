package com.example.funsdkdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.basic.G;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceFileListener;
import com.lib.funsdk.support.config.OPCompressPic;
import com.lib.funsdk.support.models.FunDevType;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.funsdk.support.utils.FileDataUtils;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeff on 16/5/16.
 *
 */
public class DeviceCameraPicAdapter extends BaseAdapter implements OnFunDeviceFileListener {

    private FunDevice mFunDevice;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<FunFileData> mDatas = null;
    private LruCache<String, Bitmap> mLruCache;
    private OPCompressPic opCompressPic;
    private ListView mImagList;
    private int mPlayingIndex = -1;
    private List<Integer> mDispPosition = new ArrayList<Integer>();

    //尝试重新获取没加载的缩略图的计数器和最大次数
    private int retryCounter = 0;
    private final int RETRY_MAX_NUM = 10;

    private final int MESSAGE_SEARCH_FILE_INFO = 0x100;
    private final int MESSAGE_SEARCH_FILE_PICTURE = 0x101;
    private final int MESSAGE_SEARCH_FILE_SUCCESS = 0x102;

    public DeviceCameraPicAdapter(Context context, ListView imgList,FunDevice funDevice, List<FunFileData> datas) {
        mContext = context;
        mImagList = imgList;
        mInflater = LayoutInflater.from(mContext);
        mFunDevice = funDevice;
        mDatas = datas;
        initDtata();
        FunSupport.getInstance().registerOnFunDeviceFileListener(this);
    }

    private void initDtata() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 设置图片缓存大小为maxMemory的1/3
        int cacheSize = maxMemory / 3;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
        opCompressPic = new OPCompressPic();
        opCompressPic.setWidth(160);
        opCompressPic.setHeight(100);
        opCompressPic.setIsGeo(1);
    }

    public void release() {
        FunSupport.getInstance().removeOnFunDeviceFileListener(this);
        
        if ( null != mHandler ) {
        	mHandler.removeCallbacksAndMessages(null);
        	mHandler = null;
        }
        
        if ( null != mLruCache ) {
            mLruCache.evictAll();
            mLruCache = null;
        }
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public FunFileData getRecordFile(int position) {
        return (FunFileData)getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        FunFileData info = mDatas.get(position);
        int oldPosition = -1;
        if (null == convertView) {
            convertView = mInflater.inflate(
                    R.layout.item_device_camera_pic, parent, false);
            holder = new ViewHolder();
            holder.baseLayout = (RelativeLayout) convertView
                    .findViewById(R.id.rl_push_result_layout);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.iv_push_result_checked);
            holder.id = (TextView) convertView
                    .findViewById(R.id.tv_push_result_id);
            holder.time = (TextView) convertView
                    .findViewById(R.id.tv_push_result_time);
            holder.event = (TextView) convertView
                    .findViewById(R.id.tv_push_result_event);
            holder.status = (TextView) convertView
                    .findViewById(R.id.tv_push_result_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            oldPosition = holder.position;
        }

        if (oldPosition != position) {
            holder.position = position;
            retryCounter = 0;
            if (oldPosition >= 0) {
                if ( mDispPosition.contains(oldPosition) ) {
                    mDispPosition.remove((Integer)oldPosition);
                }
            }
            if ( !mDispPosition.contains(position) ) {
                mDispPosition.add(position);
            }

            if ( !info.hasSeachedFile() ) {
                // 文件信息还没搜索到,是需要搜索的.
                resetSearchFileInfo();
            }
        }

        if (info != null) {
            holder.baseLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(info.getBeginTimeStr())) {
                String timeStr;
                if (info.getFileName().endsWith(".h264")) {
                    // 当前正在播放的高亮显示
                    if ( mPlayingIndex == position ) {
                        holder.event.setTextColor(0xffe97425);
                        holder.time.setTextColor(0xffe97425);
                    } else {
                        holder.event.setTextColor(0xff636363);
                        holder.time.setTextColor(0xff636363);
                    }

                    timeStr = info.getBeginTimeStr() + " - " + info.getEndTimeStr();
                } else {
                    timeStr = info.getBeginTimeStr();
                }
                holder.time.setText(timeStr);
            } else {
                holder.time.setText("");
            }
            holder.id.setText(info.getFileName());
            String type = FileDataUtils.getStrFileType(mContext, info.getFileName());
            holder.event.setText(type);
            if (!TextUtils.isEmpty(type) && type.equals(mContext.getString(R.string.device_pic_type_manual))) {
                holder.event.setCompoundDrawables(null, null,
                        setTopDrawable(R.drawable.icon_devpicture_hand),
                        null);
            } else {
                holder.event.setCompoundDrawables(null, null, null, null);
            }
        }else {
            holder.baseLayout.setVisibility(View.INVISIBLE);
        }

        if (info.getFileName().endsWith(".jpg") && mFunDevice.getDevType() == FunDevType.EE_DEV_SPORTCAMERA){
            synchronized (mLruCache) {
                setImageForImageView(info.getFileName(), holder.image);  // for device of xmjp_spt_...
            }
        } else {
        	if (info.getCapTempPath() != null) {
        		Bitmap bitmap = dealBitmap(info.getCapTempPath());
        		holder.image.setImageBitmap(bitmap);
			}else {
				holder.image.setImageResource(R.color.thumbnail_bg_color);
			}
        }

        return convertView;
    }

    public void resetDispItem() {
        mDispPosition.clear();
    }

    private void resetSearchFileInfo() {
        if ( null != mHandler ) {
            mHandler.removeMessages(MESSAGE_SEARCH_FILE_INFO);
            mHandler.sendEmptyMessageDelayed(MESSAGE_SEARCH_FILE_INFO, 1000);
        }
    }

    private void resetSearchFileBmp() {
        if ( null != mHandler ) {
            mHandler.removeMessages(MESSAGE_SEARCH_FILE_PICTURE);
            mHandler.sendEmptyMessageDelayed(MESSAGE_SEARCH_FILE_PICTURE, 1000);
        }
    }



    class ViewHolder {
        RelativeLayout baseLayout;
        ImageView image;
        TextView id;
        TextView time;
        TextView event;
        TextView status;
        int position;
    }

    public void setPlayingIndex(int index) {
        mPlayingIndex = index;
        notifyDataSetChanged();
    }
    
    public void setBitmapTempPath(String path){
    	mDatas.get(mPlayingIndex).setCapTempPath(path);
    	notifyDataSetChanged();
    }

    private Drawable setTopDrawable(int src) {
        Drawable topDrawable = mContext.getResources().getDrawable(src);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
                topDrawable.getMinimumHeight());
        return topDrawable;
    }

    private Bitmap loadBitmap(int position, boolean toDownload) {

    	if ( position >= 0 && position < mDatas.size() ) {
	        FunFileData funFileData = mDatas.get(position);
	        H264_DVR_FILE_DATA info = funFileData.getFileData();
	        if (null == info || info.st_3_beginTime.st_0_year == 0) {
	            return null;
	        }

	        String fileName_thumb = FunPath.getSptTempPath()
	                + File.separator
	                + FunPath.getDownloadFileNameByData(info, 1, true);
	        String fileName_or = FunPath.getSptTempPath()
	                + File.separator
	                + FunPath.getDownloadFileNameByData(info, 1, false);
	        final long fileSize_thumb = FunPath.isFileExists(fileName_thumb);
	        final long fileSize_or = FunPath.isFileExists(fileName_or);
            String fileName = fileSize_thumb > 0 ? fileName_thumb
	                : (fileSize_or > 0 ? fileName_or : fileName_thumb);
	        if (fileSize_thumb > 0 || fileSize_or > 0) {
	            Bitmap bitmap = getBitmapFromLruCache(funFileData.getFileName());
	            if (null == bitmap) {
	                bitmap = dealBitmap(fileName);
	            }
	
	            if (null != bitmap) {
	                addBitmapToLruCache(funFileData.getFileName(), bitmap);
                    return bitmap;
	
	            } else {
	                FunPath.deleteFile(fileName);
	            }
	        }else if (toDownload) {
	            if (null == opCompressPic) {
	                return null;
	            }
	
	            opCompressPic
	                    .setPicName(G.ToString(info.st_2_fileName));
	            FunSupport.getInstance().requestDeviceSearchPicture(mFunDevice,
	                    opCompressPic, fileName_thumb, position);
	        }
    	}
        return null;
    }


    private Bitmap dealBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; // 设置了此属性一定要记得将值设置为false
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap == null)
            return null;
        Bitmap newBtimap = Bitmap.createScaledBitmap(bitmap, 160, 90, true);
        bitmap.recycle();
        return newBtimap;
    }

    private void checkAndLoadBmps() {
        new Thread() {

            @Override
            public void run() {
                FunLog.d("test", "-------------checkAndLoadBmps Begin");
                for ( int i = 0; i < mDispPosition.size(); i ++ ) {
                    //checkItemBitmap(mDispPosition.get(i), true);
                	if (mDispPosition.size() <= 0) {
						return;
					}
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    FunLog.d("test", "-------------Position:" + mDispPosition.get(i));
                    if ( checkItemBitmap(mDispPosition.get(i)) ) {
                        if ( null != mHandler ) {
                            Message msg = new Message();
                            msg.what = MESSAGE_SEARCH_FILE_SUCCESS;
                            msg.arg1 = mDispPosition.get(i);
                            mHandler.sendMessage(msg);
                        }
                    }
                }
                if (!checkDispItemBitmap() && retryCounter < RETRY_MAX_NUM) {
                    retryCounter++;
                    if (mHandler != null) {
                    	mHandler.removeMessages(MESSAGE_SEARCH_FILE_PICTURE);
                    	mHandler.sendEmptyMessageDelayed(MESSAGE_SEARCH_FILE_PICTURE, 5000);
					}
                }
            }

        }.start();

    }

    private boolean checkItemBitmap(int position) {
        FunFileData fileData = mDatas.get(position);
        if ( null == fileData ) {
            return false;
        }

        if ( FunPath.isValidPath(fileData.getFileName()) ) {
            Bitmap bmp = loadBitmap(position, true);
            if ( null != bmp ) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDispItemBitmap() {
        for ( int i = 0; i < mDispPosition.size(); i ++ ) {
            FunFileData fileData = mDatas.get(mDispPosition.get(i));
            if (null == fileData || loadBitmap(mDispPosition.get(i), false) == null) {
                return false;
            }
        }
        return true;
    }

    private void setItemBitmap(int position) {
        FunFileData fileData = mDatas.get(position);
        if ( null == fileData ) {
            return;
        }

        if ( FunPath.isValidPath(fileData.getFileName()) ) {
            ImageView iv = (ImageView)mImagList.findViewWithTag(fileData.getFileName());
            if ( null != iv ) {
                Bitmap bmp = loadBitmap(position, false);
                if ( null != bmp ) {
                    iv.setImageBitmap(bmp);
                }
            }
        }
    }

    /**
     * 将图片存储到LruCache
     */
    public void addBitmapToLruCache(String key, Bitmap bitmap) {
        if (mLruCache != null) {
            synchronized (mLruCache) {
                if (getBitmapFromLruCache(key) == null && bitmap != null) {
                    mLruCache.put(key, bitmap);
                }
            }
        }
    }

    /**
     * 从LruCache缓存获取图片
     */
    public Bitmap getBitmapFromLruCache(String key) {
        if ( null == mLruCache ) {
            return null;
        }

        return mLruCache.get(key);
    }

    /**
     * 为ImageView设置图片(Image) 1 从缓存中获取图片 2 若图片不在缓存中则为其设置默认图片
     */
    private void setImageForImageView(String imagePath, ImageView imageView) {
        if ( null != imagePath && imagePath.length() > 0 ) {
            imageView.setTag(imagePath);
            Bitmap bitmap = getBitmapFromLruCache(imagePath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.color.thumbnail_bg_color);
                resetSearchFileBmp();
            }
        } else {
            imageView.setTag(null);
            imageView.setImageResource(R.color.thumbnail_bg_color);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MESSAGE_SEARCH_FILE_INFO:
                {
                    // check and search file
                    //TODO 写一个回调接口
                }
                break;
                case MESSAGE_SEARCH_FILE_PICTURE:
                {
                    // load Bitmap
                    checkAndLoadBmps();
                }
                break;
                case MESSAGE_SEARCH_FILE_SUCCESS:
                {
                    setItemBitmap(msg.arg1);
                }
                break;
            }
        }

    };


    @Override
    public void onDeviceFileDownCompleted(FunDevice funDevice, String path, int nSeq) {
        if ( null != mHandler ) {
            Message msg = new Message();
            msg.what = MESSAGE_SEARCH_FILE_SUCCESS;
            msg.arg1 = nSeq;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onDeviceFileDownProgress(int totalSize, int progress, int nSeq) {

    }

    @Override
    public void onDeviceFileDownStart(boolean isStartSuccess, int nSeq) {

    }

}
