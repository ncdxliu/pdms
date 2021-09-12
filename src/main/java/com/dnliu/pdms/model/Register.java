/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.user.model 
 * @author: liufuhua
 * @date: 2019年5月29日 上午9:08:41 
 */
package com.dnliu.pdms.model;

 /** 
 * @desc: TODO
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年5月29日 上午9:08:41 
 *
 */
public class Register {
	private String userName;
	private String userPassword;
	private String userEmail;
	private String userPhone;
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	/**
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}
	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Register [userName=" + userName + ", userPassword=" + userPassword + ", userEmail=" + userEmail
				+ ", userPhone=" + userPhone + "]";
	}
	
}
