/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.login.model 
 * @author: liufuhua
 * @date: 2019年3月16日 上午10:43:09 
 */
package com.dnliu.pdms.model;

import java.io.Serializable;

/**
 * @desc: 微信登录接口报文字段
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月16日 上午10:43:09 
 *
 */
public class WxLogin implements Serializable {
	private static final long serialVersionUID = -9221055588643582527L;
	private String code;
	private String userPassword;
	//登录是否需要输入登录密码
	private String checkPwd;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}
	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	/**
	 * @return the checkPwd
	 */
	public String getCheckPwd() {
		return checkPwd;
	}
	/**
	 * @param checkPwd the checkPwd to set
	 */
	public void setCheckPwd(String checkPwd) {
		this.checkPwd = checkPwd;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WxLogin [code=" + code + ", userPassword=" + userPassword + ", checkPwd=" + checkPwd + "]";
	}

}
