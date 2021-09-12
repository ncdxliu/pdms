/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.manage.model 
 * @author: liufuhua
 * @date: 2019年3月26日 下午3:36:13 
 */
package com.dnliu.pdms.model;

 /** 
 * @desc: TODO
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月26日 下午3:36:13 
 *
 */
public class Search {
	private String seachFlag;
	private String searchStr;

	/**
	 * @return the seachFlag
	 */
	public String getSeachFlag() {
		return seachFlag;
	}
	/**
	 * @param seachFlag the seachFlag to set
	 */
	public void setSeachFlag(String seachFlag) {
		this.seachFlag = seachFlag;
	}
	/**
	 * @return the searchStr
	 */
	public String getSearchStr() {
		return searchStr;
	}
	/**
	 * @param searchStr the searchStr to set
	 */
	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Search [seachFlag=" + seachFlag + ", searchStr=" + searchStr + "]";
	}

}
