package com.dnliu.pdms.model;

import java.io.Serializable;

/**
 * @author dnliu
 * @date 2021-09-11 19:13
 */
public class Login implements Serializable {
    private static final long serialVersionUID = -7354914844462649572L;

    private String userName;
    private String userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
