/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.user.model 
 * @author: liufuhua
 * @date: 2019年5月27日 上午10:35:38 
 */
package com.dnliu.pdms.model;

 /** 
 * @desc: 登录是否需要输入密码设置接口
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年5月27日 上午10:35:38 
 *
 */
public class ResetCheckPwd {
	private String checkPwd;

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
		return "ResetCheckPwd [checkPwd=" + checkPwd + "]";
	}
}
