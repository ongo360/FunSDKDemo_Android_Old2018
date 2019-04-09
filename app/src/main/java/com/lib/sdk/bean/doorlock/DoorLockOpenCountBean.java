package com.lib.sdk.bean.doorlock;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hws on 2018-05-14.
 * 门铃消息统计数据
 */

public class DoorLockOpenCountBean implements Serializable{
    @JSONField(name = "FingerPrint")
    private List<MsgUserInfo> fingerPrint;
    @JSONField(name = "Card")
    private List<MsgUserInfo> card;
    @JSONField(name = "Passwd")
    private List<MsgUserInfo> passwd;

    public List<MsgUserInfo> getFingerPrint() {
        if (fingerPrint != null && !fingerPrint.isEmpty()) {
            fingerPrint.get(0).setOpenDoorType("FingerPrint");
        }
        return fingerPrint;
    }

    public void setFingerPrint(List<MsgUserInfo> fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public List<MsgUserInfo> getCard() {
        return card;
    }

    public void setCard(List<MsgUserInfo> card) {
        this.card = card;
    }

    public List<MsgUserInfo> getPasswd() {
        return passwd;
    }

    public void setPasswd(List<MsgUserInfo> passwd) {
        this.passwd = passwd;
    }

    public class MsgUserInfo implements Serializable{
        @JSONField(name = "UserNickName")
        private String userNickName;
        @JSONField(name = "OpenCount")
        private int openCount;
        private String openDoorType = "";

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public int getOpenCount() {
            return openCount;
        }

        public void setOpenCount(int openCount) {
            this.openCount = openCount;
        }

        public String getOpenDoorType() {
            return openDoorType;
        }

        public void setOpenDoorType(String openDoorType) {
            this.openDoorType = openDoorType;
        }
    }

    public List<MsgUserInfo> getAllMsgUserInfo() {
        List<MsgUserInfo> allMsgUserInfos = new ArrayList();
        if (fingerPrint != null && !fingerPrint.isEmpty()) {
            fingerPrint.get(0).setOpenDoorType("FingerPrint");
            allMsgUserInfos.addAll(allMsgUserInfos.size(),fingerPrint);
        }
        if (card != null && !card.isEmpty()) {
            card.get(0).setOpenDoorType("Card");
            allMsgUserInfos.addAll(allMsgUserInfos.size(),card);
        }
        if (passwd != null && !passwd.isEmpty()) {
            passwd.get(0).setOpenDoorType("Passwd");
            allMsgUserInfos.addAll(allMsgUserInfos.size(),passwd);
        }
        return allMsgUserInfos;
    }
}
