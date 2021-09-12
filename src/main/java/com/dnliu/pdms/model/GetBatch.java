/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.manage.model 
 * @author: liufuhua
 * @date: 2019年3月16日 上午11:03:02 
 */
package com.dnliu.pdms.model;

 /** 
 * @desc: 批量查询接口字段
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月16日 上午11:03:02 
 *
 */
public class GetBatch {
	private String dataType;
	private int count;
	private int pageNum;

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}
	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GetBatch [dataType=" + dataType + ", count=" + count + ", pageNum=" + pageNum + "]";
	}
}
