/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.user.model 
 * @author: liufuhua
 * @date: 2019年3月28日 下午1:37:44 
 */
package com.dnliu.pdms.model;

 /** 
 * @desc: TODO
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月28日 下午1:37:44 
 *
 */
public class UpdatePassword {
	private String newPassword;
	private String oldPassword;

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}
	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}
	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UpdatePassword [newPassword=" + newPassword + ", oldPassword=" + oldPassword
				+ "]";
	}
	
}
