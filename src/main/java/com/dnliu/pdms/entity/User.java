package com.dnliu.pdms.entity;

import java.util.Date;

public class User {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.user_name
     *
     * @mbggenerated
     */
    private String userName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.user_password
     *
     * @mbggenerated
     */
    private String userPassword;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.openid
     *
     * @mbggenerated
     */
    private String openid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.user_email
     *
     * @mbggenerated
     */
    private String userEmail;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.user_phone
     *
     * @mbggenerated
     */
    private String userPhone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.registered_time
     *
     * @mbggenerated
     */
    private String registeredTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.remark
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.password_last_change_day
     *
     * @mbggenerated
     */
    private String passwordLastChangeDay;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.online_status
     *
     * @mbggenerated
     */
    private String onlineStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.status
     *
     * @mbggenerated
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.verif_code
     *
     * @mbggenerated
     */
    private String verifCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.check_pwd
     *
     * @mbggenerated
     */
    private String checkPwd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.id
     *
     * @return the value of tb_user.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.id
     *
     * @param id the value for tb_user.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.user_name
     *
     * @return the value of tb_user.user_name
     *
     * @mbggenerated
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.user_name
     *
     * @param userName the value for tb_user.user_name
     *
     * @mbggenerated
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.user_password
     *
     * @return the value of tb_user.user_password
     *
     * @mbggenerated
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.user_password
     *
     * @param userPassword the value for tb_user.user_password
     *
     * @mbggenerated
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.openid
     *
     * @return the value of tb_user.openid
     *
     * @mbggenerated
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.openid
     *
     * @param openid the value for tb_user.openid
     *
     * @mbggenerated
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.user_email
     *
     * @return the value of tb_user.user_email
     *
     * @mbggenerated
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.user_email
     *
     * @param userEmail the value for tb_user.user_email
     *
     * @mbggenerated
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.user_phone
     *
     * @return the value of tb_user.user_phone
     *
     * @mbggenerated
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.user_phone
     *
     * @param userPhone the value for tb_user.user_phone
     *
     * @mbggenerated
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.registered_time
     *
     * @return the value of tb_user.registered_time
     *
     * @mbggenerated
     */
    public String getRegisteredTime() {
        return registeredTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.registered_time
     *
     * @param registeredTime the value for tb_user.registered_time
     *
     * @mbggenerated
     */
    public void setRegisteredTime(String registeredTime) {
        this.registeredTime = registeredTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.remark
     *
     * @return the value of tb_user.remark
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.remark
     *
     * @param remark the value for tb_user.remark
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.password_last_change_day
     *
     * @return the value of tb_user.password_last_change_day
     *
     * @mbggenerated
     */
    public String getPasswordLastChangeDay() {
        return passwordLastChangeDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.password_last_change_day
     *
     * @param passwordLastChangeDay the value for tb_user.password_last_change_day
     *
     * @mbggenerated
     */
    public void setPasswordLastChangeDay(String passwordLastChangeDay) {
        this.passwordLastChangeDay = passwordLastChangeDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.online_status
     *
     * @return the value of tb_user.online_status
     *
     * @mbggenerated
     */
    public String getOnlineStatus() {
        return onlineStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.online_status
     *
     * @param onlineStatus the value for tb_user.online_status
     *
     * @mbggenerated
     */
    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.status
     *
     * @return the value of tb_user.status
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.status
     *
     * @param status the value for tb_user.status
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.verif_code
     *
     * @return the value of tb_user.verif_code
     *
     * @mbggenerated
     */
    public String getVerifCode() {
        return verifCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.verif_code
     *
     * @param verifCode the value for tb_user.verif_code
     *
     * @mbggenerated
     */
    public void setVerifCode(String verifCode) {
        this.verifCode = verifCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.check_pwd
     *
     * @return the value of tb_user.check_pwd
     *
     * @mbggenerated
     */
    public String getCheckPwd() {
        return checkPwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.check_pwd
     *
     * @param checkPwd the value for tb_user.check_pwd
     *
     * @mbggenerated
     */
    public void setCheckPwd(String checkPwd) {
        this.checkPwd = checkPwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.create_time
     *
     * @return the value of tb_user.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.create_time
     *
     * @param createTime the value for tb_user.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user.update_time
     *
     * @return the value of tb_user.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user.update_time
     *
     * @param updateTime the value for tb_user.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}