package com.dnliu.pdms.common.utils;

import com.dnliu.pdms.entity.User;

public class AppUtil {
	/**
	 * 用户信息本线程的副本
	 */
	private static ThreadLocal<User> CURRENT__LOGIN_USER = new ThreadLocal<>();
	private static ThreadLocal<String> LOGIN_IP = new ThreadLocal<>();

	/**
	 * 取得当前登录者的userId，未登录鉴权请求无法取到
	 * 
	 * @return
	 */
	public static User getUser() {
		return CURRENT__LOGIN_USER.get();
	}

	/**
	 * 设置当前登录者的userId，在登录成功的时候或者每次访问登录鉴权的时候设置这个值，其他情况不能设置
	 * 
	 * @param user
	 */
	public static void setUser(User user) {
		CURRENT__LOGIN_USER.set(user);
	}

	/**
	 * 删除当前登录用户的userId，在请求处理完成之后删除，一般不要随便调用这个方法
	 */
	public static void removeUser() {
		CURRENT__LOGIN_USER.remove();
	}

	/**
	 * 获取登录时的IP
	 * @return
	 */
	public static String getLoginIp() {
		return LOGIN_IP.get();
	}

	/**
	 * 设置登录时的IP
	 * @param loginIp
	 */
	public static void setLoginIp(String loginIp) {
		LOGIN_IP.set(loginIp);
	}
}
