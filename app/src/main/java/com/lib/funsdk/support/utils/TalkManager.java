package com.lib.funsdk.support.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.lib.EDEV_JSON_ID;
import com.lib.EFUN_ERROR;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.bean.JsonConfig;
import com.lib.sdk.bean.OPTalkBean;

import java.util.concurrent.ThreadPoolExecutor;

/*********************************************************************************
 * 对讲流程：双向对讲
 * 1.创建对讲句柄以及和设备建立连接 {@link #createTalkHandle()}
 * 2.创建向设备发送数据的线程   {@link #createThreadAndStart()}
 * 3.向设备发送可以上传语音数据有给手机的命令    {@link #makeDeviceUploadData()}
 * 4.收到③成功的回调后设置声音为100
 * 5.收到建立连接的成功回调后开始发送语音数据给设备 {@link #canSendTalkDataToDevice}
 *
 *
 * *******************************************************************************
 */
public class TalkManager implements IFunSDKResult {
    private static final String TAG = "zyy---------";
    private static final int HALF_DUPLEX = 0X01;
    private static final int FULL_DUPLEX = 0X02;
    public int hTalkHandle = 0;
    private int mMsgId = 0xff00ff;
    private AudioRecordThread mRecordThread;
    private byte[] mLock = new byte[1];
    private String mDevId;
    private OnTalkButtonListener mTalkBtnLs;
    private boolean canSendTalkDataToDevice = false;
    private int mTalkBackMode = HALF_DUPLEX;
    private Handler mHandler;
    public static final int DELAY_SEND = 106;
    public static final int RESULT_OK = 0x11;
    public static final int RESULT_FAILED = 0x12;
    public static final int OPEN_UPLOAD_VOICE_DATA = 1;
    public static final int CLOSE_UPLOAD_VOICE_DATA = -1;
    private ThreadPoolExecutor mThreadPoolExecutor;

    public TalkManager(String devId, OnTalkButtonListener ls) {
        this.mDevId = devId;
        this.mTalkBtnLs = ls;
        mMsgId = FunSDK.GetId(mMsgId, this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DELAY_SEND: {
                        deviceStopUploadData();
                        break;
                    }

                }
            }
        };
    }

    @Override
    public int OnFunSDKResult(Message msg, MsgContent ex) {
        // TODO Auto-generated method stub
        Log.d(TAG, "arg1:  " + String.valueOf(msg.arg1) + "    ex.str   :" + ex.str);
        switch (msg.what) {
            case EUIMSG.DEV_START_TALK: {
                Log.d(TAG, "DEV_START_TALK arg1:  " + String.valueOf(msg.arg1));
                if (msg.arg1 < 0) {
                    canSendTalkDataToDevice = false;
                    if (mTalkBackMode == FULL_DUPLEX) {
                        mTalkBtnLs.OnCreateLinkResult(RESULT_FAILED);
                    }
                    if (mRecordThread != null) {
                        mRecordThread.Stop();
                        mRecordThread = null;
                    }
                    if (msg.arg1 == EFUN_ERROR.EE_MNETSDK_TALK_ALAREADY_START) {
                        System.out.println("msg.what:" + msg.what + "msg.arg1:" + msg.arg1 + "ex.str:" + ex.str);
                    }
                    hTalkHandle = 0;
                    return 0;
                } else {
                    canSendTalkDataToDevice = true;
                    if (mTalkBackMode == FULL_DUPLEX) {
                        makeDeviceUploadData();
                        canSendTalkDataToDevice = true;
                        mTalkBtnLs.OnCreateLinkResult(RESULT_OK);
                        canSendTalkDataToDevice = true;
                    } else if (mTalkBackMode == HALF_DUPLEX) {
                        deviceStopUploadData();
                    }

                }
                break;
            }
            case EUIMSG.DEV_STOP_TALK: {
                if (msg.arg1 >= 0) {
                    mTalkBtnLs.OnCloseTalkResult(RESULT_OK);
                    Log.d(TAG, "关闭对讲成功");
                } else {
                    mTalkBtnLs.OnCloseTalkResult(RESULT_FAILED);
                    Log.d(TAG, "关闭对讲失败");
                }
            }
            break;
            case EUIMSG.DEV_CMD_EN: {
                if (msg.arg1 >= 0) {
                    if (ex.seq == 0x100) {
                        Log.d(TAG, "设备语音数据打开上传成功");
                        mTalkBtnLs.OnVoiceOperateResult(OPEN_UPLOAD_VOICE_DATA, RESULT_OK);
                        FunSDK.MediaSetSound(hTalkHandle, 100, 0);//打开语音
                    } else if (ex.seq == 0x1001) {
                        Log.d(TAG, "设备语音数据关闭上传成功");
                        mTalkBtnLs.OnVoiceOperateResult(CLOSE_UPLOAD_VOICE_DATA, RESULT_OK);
                        if (mTalkBackMode == FULL_DUPLEX) {
                            sendStopTalkCommand();
                        }
                    }
                } else {
                    if (ex.seq == 0x100) {
                        Log.d(TAG, "设备语音数据打开上传失败");
                        mTalkBtnLs.OnVoiceOperateResult(OPEN_UPLOAD_VOICE_DATA, RESULT_FAILED);
                        FunSDK.MediaSetSound(hTalkHandle, 100, 0);//打开语音
                    } else if (ex.seq == 0x1001) {
                        Log.d(TAG, "设备语音数据关闭上传失败");
                        mTalkBtnLs.OnVoiceOperateResult(CLOSE_UPLOAD_VOICE_DATA, RESULT_FAILED);
                        if (mTalkBackMode == FULL_DUPLEX) {
                            sendStopTalkCommand();
                        }

                    }
                }
            }
            break;

            default:
                break;
        }
        return 0;
    }

    /****
     * 半双工对讲开始
     */
    public void startTalkByHalfDuplex() {
        mTalkBackMode = this.HALF_DUPLEX;
        createTalkHandle();
        createThreadAndStart();
        FunSDK.MediaSetSound(hTalkHandle, 0, 0);
        if (canSendTalkDataToDevice) {
            deviceStopUploadData();
        }
    }

    /****
     * 半双工对讲停止
     */
    public void stopTalkByHalfDuplex() {
        stopTalkThread();
        makeDeviceUploadData();
        if (hTalkHandle != 0) {
            FunSDK.MediaSetSound(hTalkHandle, 100, 0);
        }
    }

    /****
     * 开启对讲双向形式
     */
    public void startTalkByDoubleDirection() {
        mTalkBackMode = this.FULL_DUPLEX;
        createTalkHandle();
        createThreadAndStart();
    }

    /****
     * 关闭对讲双向形式
     */
    public void stopTalkByDoubleDirection() {
        this.canSendTalkDataToDevice = false;
        stopTalkThread();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessage(DELAY_SEND);
    }

    /****
     * 开启对讲双向形式
     */
    public void startTalkByDoubleDirection(boolean uploadTalk) {
        mTalkBackMode = this.FULL_DUPLEX;
        createTalkHandle();
        createThreadAndStart(uploadTalk);
    }

    public void doubleDirectionSound(int voice) {
        if (hTalkHandle != 0) {
            FunSDK.MediaSetSound(hTalkHandle, voice, 0);//关闭语音
        }
    }


    /****
     * 创建并打开线程
     */
    private void createThreadAndStart() {
        synchronized (mLock) {
            if (null == mRecordThread) {
                mRecordThread = new AudioRecordThread();
                mRecordThread.Start();
                Log.d(TAG, "创建并打开线程");
            } else {
                Log.d(TAG, "线程已创建");
            }
        }
    }

    public void uploadTalk(boolean uploadTalk) {
        if (mRecordThread != null)
            mRecordThread.setUploadTalk(uploadTalk);
    }

    /****
     * 创建并打开线程
     */
    private void createThreadAndStart(boolean uploadTalk) {
        synchronized (mLock) {
            if (null == mRecordThread) {
                mRecordThread = new AudioRecordThread();
                mRecordThread.Start();
                uploadTalk(uploadTalk);
                Log.d(TAG, "创建并打开线程");
            } else {
                uploadTalk(uploadTalk);
                Log.d(TAG, "线程已创建");
            }
        }
    }

    /***
     * 创建对讲句柄并启用设备对讲功能
     */
    private void createTalkHandle() {
        if (hTalkHandle == 0) {
            hTalkHandle = FunSDK.DevStarTalk(mMsgId, mDevId,0,0, 0);
            Log.d(TAG, "创建hTalkHandle");
        } else {
            Log.d(TAG, "hTalkHandle 已经创建过");
        }
    }

    /***
     * 让设备上传对讲语音数据
     */
    private void makeDeviceUploadData() {
        if (hTalkHandle != 0) {
            OPTalkBean mOPTalk = new OPTalkBean();
            mOPTalk.Action = "ResumeUpload";
            FunSDK.DevCmdGeneral(mMsgId, mDevId, EDEV_JSON_ID.OPTALK_REQ, JsonConfig.OPTALK, -1, 0,
                    HandleConfigData.getSendData(JsonConfig.OPTALK, "0x1", mOPTalk).getBytes(), -1, 0x100);
            Log.d(TAG, "ResumeUpload");
        }
    }


    /***
     * 设备停止上传对讲语音数据
     */
    private void deviceStopUploadData() {
        if (hTalkHandle != 0) {
            OPTalkBean mOPTalk = new OPTalkBean();
            mOPTalk.Action = "PauseUpload";
            FunSDK.DevCmdGeneral(mMsgId, mDevId, EDEV_JSON_ID.OPTALK_REQ, JsonConfig.OPTALK, -1, 0,
                    HandleConfigData.getSendData(JsonConfig.OPTALK, "0x1", mOPTalk).getBytes(), -1, 0x1001);
            Log.d(TAG, "PauseUpload");
        }
    }

    /****
     *停止对讲线程
     */
    public void stopTalkThread() {
        synchronized (mLock) {
            if (null != mRecordThread) {
                mRecordThread.Stop();
                mRecordThread = null;
            }
        }
    }

    public void sendStopTalkCommand() {
        if (hTalkHandle != 0) {
            Log.d(TAG, "sendStopTalkCommand");
            FunSDK.DevStopTalk(hTalkHandle);
            FunSDK.MediaSetSound(hTalkHandle, 0, 0);//关闭语音
            hTalkHandle = 0;
        }
    }

    public interface OnTalkButtonListener {
        boolean isPressed();

        void onUpdateUI();

        void OnCreateLinkResult(int Result);

        void OnCloseTalkResult(int Result);

        void OnVoiceOperateResult(int Type, final int result);

    }

    class AudioRecordThread extends Thread {
        private boolean mThreadExitFlag = false;
        private AudioRecord mAudioRecord = null;
        private boolean mUploadTalk = true;

        public void setUploadTalk(boolean uploadTalk) {
            mUploadTalk = uploadTalk;
        }

        public boolean Start() {
            mThreadExitFlag = false;
            FunSDK.WebRtcAudioInit();
            int minBufSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, minBufSize);
            if (null == mAudioRecord || mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED)
                return false;
            else {
                super.start();
                return true;
            }
        }

        public void Stop() {
            mThreadExitFlag = true;
        }


        @Override
        public void run() {
            if (null == mAudioRecord) {
                return;
            }
            mAudioRecord.startRecording();
            int bufferSizeInBytes = 640;
            byte[] AudioData = new byte[bufferSizeInBytes];
            int readSize = 0;
            while (!mThreadExitFlag) {
                if (!mUploadTalk) {
                    SystemClock.sleep(5);
                    continue;
                }
                readSize = mAudioRecord.read(AudioData, 0, bufferSizeInBytes);
                Log.d(TAG, "Thread  running ");
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize && readSize > 0 && canSendTalkDataToDevice) {
                    AudioData = FunSDK.AgcProcess(AudioData, readSize);
                    FunSDK.DevSendTalkData(mDevId, AudioData, readSize);
                    Log.d(TAG, "sendData");
                } else {
                    SystemClock.sleep(5);
                }
            }
            Log.d(TAG, "Thread  dead");
            if (mAudioRecord != null) {
                if (mAudioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING) {
                    mAudioRecord.stop();
                    Log.d(TAG, "停止录音");
                }
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }
        }
    }
