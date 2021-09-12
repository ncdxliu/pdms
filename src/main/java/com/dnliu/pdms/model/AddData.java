/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.manage.model 
 * @author: liufuhua
 * @date: 2019年3月16日 上午10:49:15 
 */
package com.dnliu.pdms.model;

 /** 
 * @desc: 数据新增接口字段
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月16日 上午10:49:15 
 *
 */
public class AddData {
	private String dataType;
	private String title;
	private String content;

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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AddData [dataType=" + dataType + ", title=" + title + ", content=" + content + "]";
	}
	
}
