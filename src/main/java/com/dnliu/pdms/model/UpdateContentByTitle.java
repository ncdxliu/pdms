/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.manage.model 
 * @author: liufuhua
 * @date: 2019年3月16日 上午10:51:52 
 */
package com.dnliu.pdms.model;

 /** 
 * @desc: 根据标题更新内容接口字段
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月16日 上午10:51:52 
 *
 */
public class UpdateContentByTitle {
	private String title;
	private String content;

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
		return "UpdateContentByTitle [title=" + title + ", content=" + content + "]";
	}
}
