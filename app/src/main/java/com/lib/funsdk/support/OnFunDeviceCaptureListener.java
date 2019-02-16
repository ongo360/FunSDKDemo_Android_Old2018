package com.lib.funsdk.support;

/**
 * Created by Jeff on 4/19/16.
 */
public interface OnFunDeviceCaptureListener extends OnFunListener {

    /**
     * 抓拍成功
     * @param picStr 抓拍的照片会传回到本地, picStr 是这个照片的本地路径
     */
    void onCaptureSuccess(String picStr);

    /**
     * 抓拍失败
     * @param ErrorCode 失败 说明代码
     */
    void onCaptureFailed(int ErrorCode);

}
