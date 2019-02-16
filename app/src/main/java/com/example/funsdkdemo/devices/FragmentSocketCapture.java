package com.example.funsdkdemo.devices;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.example.funsdkdemo.adapter.SocketCaptureAdapter;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceCaptureListener;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class FragmentSocketCapture extends FragmentSocketBase implements View.OnClickListener
        , OnFunDeviceCaptureListener, AdapterView.OnItemClickListener {

    private String mCapturePath;
    private Bitmap mCaptureBmp;
    private ArrayList<String> mImgList = new ArrayList<String>();
    private GridView mGridView;
    private SocketCaptureAdapter mSocketCaptureAdapter;


    @Override
    protected View MyOnCreate(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        FunSupport.getInstance().registerOnFunDeviceCaptureListener(this);
        return inflater.inflate(R.layout.fragment_socket_capture,
                container, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setControllerBackground(getResources().getColor(R.color.bg_socket_capture));
        setRoundImage(R.drawable.socket_capture_round);

        return view;
    }

    @Override
    public void onDestroyView() {
        FunSupport.getInstance().removeOnFunDeviceCaptureListener(this);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.socket_control_image:
                tryToCapture();
                break;
        }
    }

    @Override
    protected void initLayout() {
        mRadioGroup.setVisibility(View.GONE);
        mImageControl.setOnClickListener(this);

        mGridView = (GridView) mLayout.findViewById(R.id.img_gv);
        mSocketCaptureAdapter = new SocketCaptureAdapter(getActivity(), mImgList, mGridView);
        mGridView.setAdapter(mSocketCaptureAdapter);
        mGridView.setOnItemClickListener(this);

    }

    private void tryToCapture() {
        showWaitDialog();
        FunSupport.getInstance().requestDeviceCapture(mFunDevice);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView iv = new ImageView(getActivity());
        mCapturePath = mImgList.get(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        mCaptureBmp = BitmapFactory.decodeFile(mCapturePath, options);
        iv.setImageBitmap(mCaptureBmp);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.device_socket_capture_preview)
                .setView(iv)
                .setPositiveButton(R.string.device_socket_capture_save,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(mCapturePath);
                        File imgPath = new File(FunPath.PATH_PHOTO + File.separator
                                + file.getName());
                        if (imgPath.exists()) {
                            ((ActivityDemo) getActivity()).showToast(R.string.device_socket_capture_exist);
                        } else {
                            FileUtils.copyFile(mCapturePath, FunPath.PATH_PHOTO + File.separator
                                    + file.getName());
                            ((ActivityDemo) getActivity()).showToast(R.string.device_socket_capture_save_success);
                        }
                    }
                })
                .setNegativeButton(R.string.device_socket_capture_delete,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSocketCaptureAdapter.removeImg(mCapturePath);
                        ((ActivityDemo) getActivity()).showToast(R.string.device_socket_capture_delete_success);
                    }
                })
                .show();
    }

    @Override
    protected void refreshLayout() {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

    }

    @Override
    public void onCaptureSuccess(String picStr) {
        hideWaitDialog();
        mSocketCaptureAdapter.addImg(picStr);
    }

    @Override
    public void onCaptureFailed(int errCode) {
        hideWaitDialog();
        ((ActivityDemo)getActivity()).showToast(FunError.getErrorStr(errCode));
    }

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}



}
