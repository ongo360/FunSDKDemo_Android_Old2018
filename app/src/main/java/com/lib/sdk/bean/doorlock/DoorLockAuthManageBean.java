package com.lib.sdk.bean.doorlock;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccy on 2018-03-01.
 * 门锁-权限管理信息（密码、指纹、门卡）
 * {@link com.lib.sdk.bean.JsonConfig#DOOR_LOCK_USER_INFO}
 */

public class DoorLockAuthManageBean{

    public String DoorLockName;
    public String DoorLockID;
    public UserListBean FingerManger; //指纹
    public UserListBean CardManger;//门卡
    public UserListBean PassWdManger;//密码

    @JSONField(serialize = false)
    public List<UserListBean.UserBean> getAllUserInfo() {
        List<UserListBean.UserBean> allUserInfo = new ArrayList<>();
        if (FingerManger != null) {
            List<UserListBean.UserBean> infos = FingerManger.getAllUserInfo();
            if (!infos.isEmpty()) {
                infos.get(0).setDoorTpyeShow(true);
                for (UserListBean.UserBean bean : infos) {
                    bean.setOpenDoorType("FingerManger");
                }
                allUserInfo.addAll(allUserInfo.size(), infos);
            }
        }
        if (CardManger != null) {
            List<UserListBean.UserBean> infos = CardManger.getAllUserInfo();
            if (!infos.isEmpty()) {
                infos.get(0).setDoorTpyeShow(true);
                for (UserListBean.UserBean bean : infos) {
                    bean.setOpenDoorType("CardManger");
                }
                allUserInfo.addAll(allUserInfo.size(), infos);
            }
        }
        if (PassWdManger != null) {
            List<UserListBean.UserBean> infos = PassWdManger.getAllUserInfo();
            if (!infos.isEmpty()) {
                infos.get(0).setDoorTpyeShow(true);
                for (UserListBean.UserBean bean : infos) {
                    bean.setOpenDoorType("PassWdManger");
                }
                allUserInfo.addAll(allUserInfo.size(), infos);
            }
        }
        return allUserInfo;
    }

    /**
     * 用户列表
     */
    public static class UserListBean {
        // nullable
        public List<UserBean> Admin;//管理员用户
        public List<UserBean> General;//普通用户
        public List<UserBean> Guest;//宾客
        public List<UserBean> Force;//胁迫用户
        public List<UserBean> Temporary;//临时用户

        @JSONField(serialize = false)
        public List<UserBean> getAllUserInfo() {
            List<UserBean> allUserInfo = new ArrayList<>();
            if (Admin != null && !Admin.isEmpty()) {
                for (UserBean bean : Admin) {
                    bean.setDoorLockUserType("Admin");
                }
                allUserInfo.addAll(Admin);
            }
            if (General != null && !General.isEmpty()) {
                for (UserBean bean : General) {
                    bean.setDoorLockUserType("General");
                }
                allUserInfo.addAll(allUserInfo.size(),General);
            }
            if (Guest != null && !Guest.isEmpty()) {
                for (UserBean bean : Guest) {
                    bean.setDoorLockUserType("Guest");
                }
                allUserInfo.addAll(allUserInfo.size(),Guest);
            }
            if (Force != null && !Force.isEmpty()) {
                for (UserBean bean : Force) {
                    bean.setDoorLockUserType("Force");
                }
                allUserInfo.addAll(allUserInfo.size(),Force);
            }
            if (Temporary != null && Temporary.isEmpty()) {
                for (UserBean bean : Temporary) {
                    bean.setDoorLockUserType("Temporary");
                }
                allUserInfo.addAll(allUserInfo.size(),Temporary);
            }
            return allUserInfo;
        }
        /**
         * 用户
         */
        public static class UserBean implements Cloneable,Serializable {
            public String Serial;//门锁传上来的用户ID，唯一、不可修改
            public String NickName; //可修改昵称，最大十个中文 20个英文
            public boolean MessagePushEnable;//
            private String openDoorType = "";//开门类型：指纹、密码、刷卡
            private String doorLockUserType = "";//用户类型:管理员用户、普通用户
            private boolean isDoorTpyeShow = false;//用于列表 是否需要显示 开门类型
            @Override
            public UserBean clone() {
                try {
                    return (UserBean) super.clone();
                } catch (CloneNotSupportedException e) {

                }
                return null;
            }


            /**
             * 以Serial作为唯一身份
             */
            @Override
            public boolean equals(Object o) {
                if (!(o instanceof UserBean)) {
                    return false;
                }
                if(this.Serial == null){
                    return false;
                }
                if (this == o) {
                    return true;
                }
                return this.Serial.equals(((UserBean) o).Serial);
            }

            @JSONField(serialize = false)
            public String getOpenDoorType() {
                return openDoorType;
            }

            @JSONField(serialize = false)
            public void setOpenDoorType(String openDoorType) {
                this.openDoorType = openDoorType;
            }

            @JSONField(serialize = false)
            public String getDoorLockUserType() {
                return doorLockUserType;
            }

            @JSONField(serialize = false)
            public void setDoorLockUserType(String doorLockUserType) {
                this.doorLockUserType = doorLockUserType;
            }

            @JSONField(serialize = false)
            public boolean isDoorTpyeShow() {
                return isDoorTpyeShow;
            }

            @JSONField(serialize = false)
            public void setDoorTpyeShow(boolean doorTpyeShow) {
                isDoorTpyeShow = doorTpyeShow;
            }
        }

    }
}
