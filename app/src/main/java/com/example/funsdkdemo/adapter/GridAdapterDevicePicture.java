package com.example.funsdkdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.G;
import com.example.funsdkdemo.R;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceFileListener;
import com.lib.funsdk.support.config.DevCmdOPRemoveFileJP;
import com.lib.funsdk.support.config.DevCmdOPSCalendar;
import com.lib.funsdk.support.config.OPCompressPic;
import com.lib.funsdk.support.config.SameDayPicInfo;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridAdapterDevicePicture extends BaseAdapter implements StickyGridHeadersBaseAdapter, OnFunDeviceFileListener {


	public interface OnGridDevicePictureListener {
		void onGridDevicePictureDayDisplay(int position, SameDayPicInfo picInfo);

		void onGridDevicePictureItemDisplay(List<Integer> dayIds);
	}

	private Context mContext = null;
	private LayoutInflater mInflater;
	private FunDevice mFunDevice;
	private DevCmdOPSCalendar opsCalendar = null;
	private StickyGridHeadersGridView mGridView = null;
	private OnGridDevicePictureListener mListener = null;

	private LruCache<String, Bitmap> mLruCache;

	// 当前显示的文件索引
	private List<Integer> mDispPosition = new ArrayList<Integer>();

	// 记录图片的选中情况
	private HashMap<Integer, Boolean> mIsSelected = new HashMap<Integer, Boolean>();
	public boolean isSelMode = false;

	private final int MESSAGE_SEARCH_FILE_INFO = 0x100;
	private final int MESSAGE_SEARCH_FILE_PICTURE = 0x101;
	private final int MESSAGE_SEARCH_FILE_SUCCESS = 0x102;

	public GridAdapterDevicePicture(Context context,
									StickyGridHeadersGridView gridView,
									FunDevice funDevice) {
		mContext = context;
		mGridView = gridView;
		mInflater = LayoutInflater.from(mContext);
		mFunDevice = funDevice;
		opsCalendar = (DevCmdOPSCalendar) mFunDevice.checkConfig(DevCmdOPSCalendar.CONFIG_NAME);

		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// 设置图片缓存大小为maxMemory的1/3
		int cacheSize = maxMemory / 3;
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};

		FunSupport.getInstance().registerOnFunDeviceFileListener(this);
	}


	private boolean isSelected(int position) {
		Boolean is = mIsSelected.get(position);
		return is == null ? false : is;
	}

	public void select(int position) {
		mIsSelected.put(position, !isSelected(position));
	}


	public void clearSelected() {
		mIsSelected.clear();
	}


	public DevCmdOPRemoveFileJP getRemovingFiles() {
		DevCmdOPRemoveFileJP cmdOPRemoveFileJP = (DevCmdOPRemoveFileJP)
				mFunDevice.checkConfig(DevCmdOPRemoveFileJP.CONFIG_NAME);
		List<Integer> position = new ArrayList<>();
		int num = 0;
		for (Map.Entry<Integer, Boolean> entry : mIsSelected.entrySet()) {
			if (entry.getValue()) {
				int pos = entry.getKey();
				position.add(num,pos);
				num++;
			}
		}
		int posOne,posTwo;
		for (int i =0 ; i < position.size();i++){
			for (int j = 0; j<position.size()-1-i;j++){
				posOne = position.get(j);
				posTwo = position.get(j+1);
				if (posOne > posTwo){
					position.set(j,posTwo);
					position.set(j+1,posOne);
				}
			}
		}

			for (int i = position.size() - 1; i>= 0; i--) {
				FunFileData data = getFileData(position.get(i));
				String fileName = null;
				if (data.hasSeachedFile()) {
					fileName = data.getFileName();
				}
				if (null == fileName) {
					Toast.makeText(mContext, "error fileName:" + position.get(i),
							Toast.LENGTH_SHORT).show();
					continue;
				}
				int isDir = (data.getFileType() == SDKCONST.PicFileType.PIC_BURST_SHOOT || data
						.getFileType() == SDKCONST.PicFileType.PIC_TIME_LAPSE) ? 1
						: 0;
				cmdOPRemoveFileJP.addFileNameInfo(position.get(i), SDKCONST.StreamType.Main, isDir, fileName);
				mLruCache.remove(fileName);
				removeFileData(position.get(i));
			}


		return cmdOPRemoveFileJP;
	}

	public void release() {
		FunSupport.getInstance().removeOnFunDeviceFileListener(this);
		if (null != mLruCache) {
			mLruCache.evictAll();
			mLruCache = null;
		}

		if (null != mHandler) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
	}

	public void setOnGridDevicePictureListener(OnGridDevicePictureListener l) {
		mListener = l;
	}

	@Override
	public int getCount() {
		int count = 0;
		for (SameDayPicInfo picInfo : opsCalendar.getData()) {
			count += picInfo.getPicNum();
		}
		return count;
	}

	private FilePosition translateFilePosition(int position) {
		int pos = position;
		for (int i = 0; i < opsCalendar.getData().size(); i++) {
			SameDayPicInfo picInfo = opsCalendar.getData().get(i);
			if (pos >= 0 && pos < picInfo.getPicNum()) {
				return new FilePosition(i, pos);
			} else if (pos >= picInfo.getPicNum()) {
				pos -= picInfo.getPicNum();
			} else {
				return null;
			}
		}
		return null;
	}

	private FunFileData getFileData(int position) {
		int pos = position;
		for (SameDayPicInfo picInfo : opsCalendar.getData()) {
			if (pos >= 0 && pos < picInfo.getPicNum()) {
				return picInfo.getPicData(pos);
			} else if (pos >= picInfo.getPicNum()) {
				pos -= picInfo.getPicNum();
			} else {
				return null;
			}
		}
		return null;
	}

	private void removeFileData(int position) {
		int pos = position;
		for (SameDayPicInfo picInfo : opsCalendar.getData()) {
			if (pos >= 0 && pos < picInfo.getPicNum()) {
				picInfo.removePicData(pos);
				if (picInfo.getPicNum() <= 0) {
					opsCalendar.getData().remove(picInfo);
				}
				return;
			} else if (pos >= picInfo.getPicNum()) {
				pos -= picInfo.getPicNum();
			}
		}
	}


	private void resetSearchFileInfo() {
		if (null != mHandler) {
			mHandler.removeMessages(MESSAGE_SEARCH_FILE_INFO);
			mHandler.sendEmptyMessageDelayed(MESSAGE_SEARCH_FILE_INFO, 220);
		}
	}

	private void resetSearchFileBmp() {
		if (null != mHandler) {
			mHandler.removeMessages(MESSAGE_SEARCH_FILE_PICTURE);
			mHandler.sendEmptyMessageDelayed(MESSAGE_SEARCH_FILE_PICTURE, 220);
		}
	}

	private void checkAndSearchFile() {
		try {
			List<Integer> needDays = new ArrayList<Integer>();

			for (int i = 0; i < mDispPosition.size(); i++) {
				FilePosition fpos = translateFilePosition(mDispPosition.get(i));
				if (!needDays.contains(fpos.day_index)) {
					needDays.add(fpos.day_index);
				}
			}

			FunLog.d("test", "---> to search days : " + needDays.toString());

			if (null != mListener) {
				mListener.onGridDevicePictureItemDisplay(needDays);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	private void checkAndLoadBmps() {
		new Thread() {

			@Override
			public void run() {
				for (int i = 0; i < mDispPosition.size(); i++) {
					//checkItemBitmap(mDispPosition.get(i), true);
					if (checkItemBitmap(mDispPosition.get(i))) {
						if (null != mHandler) {
							Message msg = new Message();
							msg.what = MESSAGE_SEARCH_FILE_SUCCESS;
							msg.arg1 = mDispPosition.get(i);
							mHandler.sendMessage(msg);
						}
					}
				}
			}

		}.start();

	}

	private boolean checkItemBitmap(int position) {
		FunFileData fileData = getFileData(position);
		if (null == fileData) {
			return false;
		}

		if (FunPath.isValidPath(fileData.getFileName())) {
			Bitmap bmp = loadBitmap(position, true);
			if (null != bmp) {
				return true;
			}
		}
		return false;
	}

	private void setItemBitmap(int position) {
		FunFileData fileData = getFileData(position);
		if (null == fileData) {
			return;
		}

		if (FunPath.isValidPath(fileData.getFileName())) {
			ImageView iv = (ImageView) mGridView.findViewWithTag(fileData.getFileName());
			if (null != iv) {
				Bitmap bmp = loadBitmap(position, false);
				if (null != bmp) {
					iv.setImageBitmap(bmp);
				}
			}
		}
	}

	@Override
	public Object getItem(int position) {
		return getFileData(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder vh = null;
		int oldPosition = -1;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.layout_device_picture_grid_item,
					null);
			vh = new ItemViewHolder();
			vh.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
			vh.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
			vh.itemType = (ImageView) convertView.findViewById(R.id.itemType);
			vh.itemCheck = (CheckBox) convertView.findViewById(R.id.itemCheck);
			convertView.setTag(vh);
		} else {
			vh = (ItemViewHolder) convertView.getTag();
			oldPosition = vh.position;
		}


		FunFileData fileData = getFileData(position);

		if (oldPosition != position) {
			vh.position = position;

			if (oldPosition >= 0) {
				if (mDispPosition.contains(oldPosition)) {
					mDispPosition.remove((Integer) oldPosition);
				}
			}

			if (!mDispPosition.contains(position)) {
				mDispPosition.add(position);
			}

			if (!fileData.hasSeachedFile()) {
				// 文件信息还没搜索到,是需要搜索的.
				resetSearchFileInfo();
			}
		}

		//更新 Checkbox 状态
		if (isSelMode) {
			vh.itemCheck.setVisibility(View.VISIBLE);
			vh.itemCheck.setChecked(isSelected(position));
		} else {
			clearSelected();
			vh.itemCheck.setVisibility(View.GONE);
		}


		vh.itemTitle.setText(fileData.getBeginTimeStr());
		if (fileData.getFileType() == SDKCONST.PicFileType.PIC_BURST_SHOOT) {
			vh.itemType.setVisibility(View.VISIBLE);
			vh.itemType
					.setImageResource(R.drawable.pic_burst_shoot);
		} else if (fileData.getFileType() == SDKCONST.PicFileType.PIC_TIME_LAPSE) {
			vh.itemType.setVisibility(View.VISIBLE);
			vh.itemType
					.setImageResource(R.drawable.pic_time_lapse);
		} else {
			vh.itemType.setVisibility(View.GONE);
		}
		synchronized (mLruCache) {
			setImageForImageView(fileData.getFileName(), vh.itemImage);
		}


		return convertView;
	}

	@Override
	public int getCountForHeader(int header) {
		return opsCalendar.getData().get(header).getPicNum();
	}

	@Override
	public int getNumHeaders() {
		return opsCalendar.getData().size();
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_device_picture_grid_header,
					null);

			holder = new HeaderViewHolder();
			holder.txtDate = (TextView) convertView
					.findViewById(R.id.txtDate);
			holder.txtFileNum = (TextView) convertView
					.findViewById(R.id.txtFileNum);
			holder.imgArrowIcon = (ImageView) convertView
					.findViewById(R.id.imgArrowIcon);

			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		SameDayPicInfo picInfo = opsCalendar.getData().get(position);

		holder.txtDate.setText(picInfo.getDispDate());
		if (picInfo.hasGotFileNum()) {
			// 已经发送了获取文件列表个数的请求
			holder.txtFileNum.setText(Integer.toString(picInfo.getPicNum()));
		} else {
			// 等待获取文件列表个数
			holder.txtFileNum.setText("...");
		}

		if (null != mListener) {
			mListener.onGridDevicePictureDayDisplay(position, picInfo);
		}

		return convertView;
	}

	private class HeaderViewHolder {
		TextView txtDate;
		TextView txtFileNum;
		ImageView imgArrowIcon;
	}

	private class ItemViewHolder {
		TextView itemTitle;
		ImageView itemType;
		CheckBox itemCheck;
		ImageView itemImage;
		int position;
	}

	private class FilePosition {
		Integer day_index;
		Integer file_index;

		FilePosition(Integer day, Integer pos) {
			day_index = day;
			file_index = pos;
		}

		@Override
		public boolean equals(Object o) {
			FilePosition fp = (FilePosition) o;
			return fp.day_index == this.day_index
					&& fp.file_index == this.file_index;
		}
	}

	public String getStartTimeOfDay(H264_DVR_FILE_DATA fileData) {
		return String.format("%02d:%02d:%02d", fileData.st_3_beginTime.st_4_hour,
				fileData.st_3_beginTime.st_5_minute, fileData.st_3_beginTime.st_6_second);
	}

	/**
	 * 为ImageView设置图片(Image) 1 从缓存中获取图片 2 若图片不在缓存中则为其设置默认图片
	 */
	private void setImageForImageView(String imagePath, ImageView imageView) {
		if (null != imagePath && imagePath.length() > 0) {
			imageView.setTag(imagePath);
			Bitmap bitmap = getBitmapFromLruCache(imagePath);
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
			} else {
				imageView.setImageResource(R.drawable.icon_default_image);
				resetSearchFileBmp();
			}
		} else {
			imageView.setTag(null);
			imageView.setImageResource(R.drawable.icon_default_image);
		}
	}

	/**
	 * 将图片存储到LruCache
	 */
	public void addBitmapToLruCache(String key, Bitmap bitmap) {
		synchronized (mLruCache) {
			if (getBitmapFromLruCache(key) == null && bitmap != null) {
				mLruCache.put(key, bitmap);
			}
		}
	}

	/**
	 * 从LruCache缓存获取图片
	 */
	public Bitmap getBitmapFromLruCache(String key) {
		if (null == mLruCache) {
			return null;
		}

		return mLruCache.get(key);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_SEARCH_FILE_INFO: {
					// check and search file
					checkAndSearchFile();
				}
				break;
				case MESSAGE_SEARCH_FILE_PICTURE: {
					// load Bitmap
					checkAndLoadBmps();
				}
				break;
				case MESSAGE_SEARCH_FILE_SUCCESS: {
					setItemBitmap(msg.arg1);
				}
				break;
			}
		}

	};

	private Bitmap loadBitmap(int position, boolean toDownload) {
		FunFileData fileData = getFileData(position);
		H264_DVR_FILE_DATA info = fileData.getFileData();
		if (null == info || info.st_3_beginTime.st_0_year == 0) {
			return null;
		}

		String fileName = "";
		String fileName_thumb = FunPath.getSptTempPath()
				+ File.separator
				+ FunPath.getDownloadFileNameByData(info, 1, true);
		String fileName_or = FunPath.getSptTempPath()
				+ File.separator
				+ FunPath.getDownloadFileNameByData(info, 1, false);
		final long fileSize_thumb = FunPath.isFileExists(fileName_thumb);
		final long fileSize_or = FunPath.isFileExists(fileName_or);
		fileName = fileSize_thumb > 0 ? fileName_thumb
				: (fileSize_or > 0 ? fileName_or : fileName_thumb);
		if (fileSize_thumb > 0 || fileSize_or > 0) {
			Bitmap bitmap = getBitmapFromLruCache(fileData.getFileName());
			if (null == bitmap) {
				bitmap = dealBitmap(fileName);
			}

			if (null != bitmap) {
				addBitmapToLruCache(fileData.getFileName(), bitmap);
				return bitmap;
			} else {
				FunPath.deleteFile(fileName);
			}
		} else if (toDownload) {
			OPCompressPic opCompressPic = fileData.getOPCompressPic();
			if (null == opCompressPic) {
				return null;
			}

			opCompressPic
					.setPicName(G.ToString(info.st_2_fileName));
			FunSupport.getInstance().requestDeviceSearchPicture(mFunDevice,
					opCompressPic, fileName_thumb, position);
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
		bitmap = null;
		return newBtimap;
	}

	@Override
	public void onDeviceFileDownCompleted(FunDevice funDevice, String path, int nSeq) {
		if (null != mHandler) {
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
