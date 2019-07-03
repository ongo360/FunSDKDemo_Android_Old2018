package com.example.funsdkdemo.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 用户信息实体类
 * Created by hanzhenbo on 2017-08-05.
 */

public class UserInfoEntity {
    private String mID;
    private String mUserName;
    private String mMail;
    private String mPhone; //开放平台
    private String mCompany; //开放平台
    private AuthorizesEntity mAuthorizesEntity;
    private String mMobilePhone; //普通库
    private String mEmail; //普通库

    @JSONField(name = "id")
    public String getID() {
        return mID;
    }

    @JSONField(name = "id")
    public void setID(String ID) {
        mID = ID;
    }

    @JSONField(name = "userName")
    public String getUserName() {
        return mUserName;
    }

    @JSONField(name = "userName")
    public void setUserName(String userName) {
        mUserName = userName;
    }

    @JSONField(name = "mail")
    public String getMail() {
        return mMail;
    }

    @JSONField(name = "mail")
    public void setMail(String mail) {
        mMail = mail;
    }

    @JSONField(name = "phone")
    public String getPhone() {
        return mPhone;
    }

    @JSONField(name = "phone")
    public void setPhone(String phone) {
        mPhone = phone;
    }

    @JSONField(name = "company")
    public String getCompany() {
        return mCompany;
    }

    @JSONField(name = "company")
    public void setCompany(String company) {
        mCompany = company;
    }

    @JSONField(name = "authorizes")
    public AuthorizesEntity getAuthorizesEntity() {
        return mAuthorizesEntity;
    }

    @JSONField(name = "authorizes")
    public void setAuthorizesEntity(AuthorizesEntity authorizesEntity) {
        mAuthorizesEntity = authorizesEntity;
    }

    @JSONField(name = "mobile_phone")
    public String getMobilePhone() {
        return mMobilePhone;
    }

    @JSONField(name = "mobile_phone")
    public void setMobilePhone(String mobilePhone) {
        mMobilePhone = mobilePhone;
    }

    @JSONField(name = "email")
    public String getEmail() {
        return mEmail;
    }

    @JSONField(name = "email")
    public void setEmail(String email) {
        mEmail = email;
    }
}
