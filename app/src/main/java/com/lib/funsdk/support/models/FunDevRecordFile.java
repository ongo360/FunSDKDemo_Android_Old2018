package com.lib.funsdk.support.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.basic.G;
import com.lib.SDKCONST;
import com.lib.sdk.struct.SDK_SearchByTimeInfo;

/**
 * Created by Jeff on 16/5/23.
 */
public class FunDevRecordFile {

    public static int TYPE_COMMON = 1;
    public static int F_ALERT = 2;
    public static int F_DYNAMIC = 3;
    public static int F_CARD = 4;

    public int recStartTime; //开始时间,当天总秒数
    public int recEndTime;
    public int recData;

    public String getRecStartTime() {
        return  String.format("%02d:%02d:%02d", recStartTime / 3600,
                (recStartTime % 3600) / 60, (recStartTime % 3600) % 60);
    }

    public String getRecEndTime() {
        return  String.format("%02d:%02d:%02d", recEndTime / 3600,
                (recEndTime % 3600) / 60, (recEndTime % 3600) % 60);
    }

    public static List<FunDevRecordFile> parseDevFilesByResult(byte[] pData) {			
        List<FunDevRecordFile> files = new ArrayList<FunDevRecordFile>();

        SDK_SearchByTimeResult info = new SDK_SearchByTimeResult(1);
        G.BytesToObj(info, pData);

        byte[] pRet = info.st_1_ByTimeInfo[0].st_1_cRecordBitMap;
        pRet[719] = 16;
        if (null != pRet) {
            byte lastData = 0;
            int curTmSec = 0;   // min
            FunDevRecordFile file = null;
            for (int i = 0; i < pRet.length; i ++) {
                byte curData = pRet[i];
                byte[] subDatas = new byte[2];
                subDatas[0] = (byte)(curData & 0xf);
                subDatas[1] = (byte)((curData >> 4) & 0x0f);
                for ( int j = 0; j < subDatas.length; j ++ ) {
                	byte subData = subDatas[j];
                	if ( subData != 0 ) {
                		if ( subData == lastData) {
                			//
                		} else {
                			if ( lastData != 0 ) {			//这里的demo应该不会执行
                				// end last file
                				if ( null != file ) {
                					file.recEndTime = curTmSec;
                					files.add(file);
                				}
                			}
                			// new file start
                			lastData = subData;
                			file = new FunDevRecordFile();
                			file.recStartTime = curTmSec;
                			file.recData = subData;
                		}
                	} else {
                		if ( null != file ) {
                			// end of a record file
                			file.recEndTime = curTmSec;
                			
                			files.add(file);
                			file = null;
                			lastData = 0x00;
                		}
                	}
                	
                	curTmSec += 60; // 1min
                }

            }
        }
        // 按录像时间 倒序
        Collections.sort(files, new ComparatorRecordFile());
        
        //if you want to get only one file , can do this
        FunDevRecordFile rfile = new FunDevRecordFile();
        if (files.size() > 0) {
        	int size = files.size();
        	rfile.recStartTime = files.get(0).recStartTime;
        	rfile.recEndTime = files.get(size-1).recEndTime;
        	files.clear();
        	files.add(rfile);
		}
        
        return files;
    }

    static class ComparatorRecordFile implements Comparator {

		@Override
		public int compare(Object arg0, Object arg1) {
			FunDevRecordFile rf0 = (FunDevRecordFile)arg0;
			FunDevRecordFile rf1 = (FunDevRecordFile)arg1;
			if ( rf0.recStartTime > rf1.recStartTime ) {
				return 1;
			} else if ( rf0.recStartTime < rf1.recStartTime ) {
				return -1;
			}
			
			return 0;
		}
    	
    }

    static class SDK_SearchByTimeResult {
        public int st_0_nInfoNum; // /< 通道的录像记录信息个数
        public SDK_SearchByTimeInfo st_1_ByTimeInfo[] = null; // /< 通道的录像记录信息

        public SDK_SearchByTimeResult(int chnNum) {
            if (chnNum > 0) {
                st_1_ByTimeInfo = new SDK_SearchByTimeInfo[chnNum];
            } else {
                st_1_ByTimeInfo = new SDK_SearchByTimeInfo[SDKCONST.NET_MAX_CHANNUM];
            }
            for (int i = 0; i < st_1_ByTimeInfo.length; ++i) {
                st_1_ByTimeInfo[i] = new SDK_SearchByTimeInfo();
            }
        }

    }
}
