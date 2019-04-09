package com.lib.funsdk.support.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;
import com.lib.FunSDK;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceTalkListener;
import com.lib.funsdk.support.models.FunDevice;

/**
 * Created by Jeff on 16/4/21.
 *
 */
public class TalkManager implements OnFunDeviceTalkListener {

    private final String TAG = "TalkManager";

    public int hTalkHandle = 0;
    private FunDevice mFunDevice;
    private AudioRecordThread mRecordThread;
    private byte[] mLock = new byte[1];

    public TalkManager(FunDevice funDevice) {
        FunSupport.getInstance().registerOnFunDeviceTalkListener(this);
        mFunDevice = funDevice;
    }

    @Override
    protected void finalize() throws Throwable {
        FunSupport.getInstance().removeOnFunDeviceTalkListener(this);
        super.finalize();
    }

    public void onStartTalk() {

        if (hTalkHandle == 0) {
            hTalkHandle = FunSupport.getInstance().requestDeviceStartTalk(mFunDevice);
        }
        onPauseThread(false);
    }

    public void onStopTalk() {
        if (hTalkHandle != 0) {
            FunSupport.getInstance().requestDeviceStopTalk(hTalkHandle);
            hTalkHandle = 0;
        }
    }

    public void onStartThread() {
        synchronized (mLock) {
            if (null == mRecordThread) {
                mRecordThread = new AudioRecordThread();
                mRecordThread.Start();
                mRecordThread.Pause(false);
            }
        }
    }

    public void onStopThread() {
        synchronized (mLock) {
            if (null != mRecordThread) {
                mRecordThread.Stop();
                mRecordThread.Pause(true);
                mRecordThread = null;
            }
        }
    }

    public void onPauseThread(boolean bPause) {
        synchronized (mLock) {
            if (null != mRecordThread) {
                mRecordThread.Pause(bPause);
            }
        }
    }

    public void setTalkSound(Boolean sound){
        if (hTalkHandle != 0) {
            FunSDK.MediaSetSound(hTalkHandle, sound ? 100 : 0, 0);
        }
    }

    @Override
    public void onDeviceStartTalkSuccess() {
        FunLog.d(TAG, "onDeviceStartTalkSuccess");
    }

    @Override
    public void onDeviceStartTalkFailed(int errCode) {
        onStopThread();
        hTalkHandle = 0;
    }

    @Override
    public void onDeviceStopTalkSuccess() {
        FunLog.d(TAG, "onDeviceStopTalkSuccess");
    }

    @Override
    public void onDeviceStopTalkFailed(int errCode) {
        FunLog.d(TAG, "onDeviceStopTalkFailed");
    }

    class AudioRecordThread extends Thread {
        private boolean mThreadExitFlag = false;
        private boolean mThreadPauseFlag = false;
        private AudioRecord mAudioRecord = null;

        public boolean Start() {
            mThreadExitFlag = false;
            int minBufSize = AudioRecord.getMinBufferSize(8000,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, minBufSize);
            if (null == mAudioRecord || mAudioRecord
                    .getState() == AudioRecord.STATE_UNINITIALIZED)
                return false;
            else {
                super.start();
                return true;
            }
        }

        public void Stop() {
            mThreadExitFlag = true;
        }

        public void Pause(boolean bPause) {
            mThreadPauseFlag = bPause;
            com.lib.funsdk.support.config.OPTalk mOPTalk = new com.lib.funsdk.support.config.OPTalk();
            if (mThreadPauseFlag) {
				mOPTalk.Action = "ResumeUpload";
			} else {
				mOPTalk.Action = "PauseUpload";
			}
            
			FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, mOPTalk);
        }

        @Override
        public void run() {
            if (null == mAudioRecord) {
                return;
            }
            mAudioRecord.startRecording();
            int bufferSizeInBytes = 640;
            byte[] audiodata = new byte[bufferSizeInBytes];
            int readsize = 0;

            //发送麦克风数据
            while (!mThreadExitFlag) {
                readsize = mAudioRecord.read(audiodata, 0, bufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != readsize
                        && readsize > 0 && !mThreadPauseFlag) {
                    FunSupport.getInstance().requestDeviceSendTalkData(
                            mFunDevice, audiodata, readsize);
                } else {
                    SystemClock.sleep(5);
                }
            }

            if (mAudioRecord != null) {
                if (mAudioRecord
                        .getState() == AudioRecord.RECORDSTATE_RECORDING){
                    mAudioRecord.stop();
                }
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }
    }



}
