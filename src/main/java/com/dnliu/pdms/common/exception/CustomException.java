package com.dnliu.pdms.common.exception;

/**
 * 类名称：CustomException
 * 类描述：系统自定义异常类
 * 创建人：
 * 创建时间：2016年3月7日下午8:08:03
 * 修改人：
 */
public class CustomException extends Exception{
	private static final long serialVersionUID = 1L;
	
	//异常信息
	public String message;
	public CustomException(String message){
		super(message);
		this.message=message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
