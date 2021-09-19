package com.dnliu.pdms.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

public class WebUtil {

	private static Logger log = LoggerFactory.getLogger(WebUtil.class);

	/**
	 * 获得远程 ip
	 * 
	 * @param request
	 * @return String
	 */
	public static String getRemoteIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return transIP(request.getRemoteAddr());
		}
		// 多级反向代理,取X-Forwarded-For中第一个非unknown的有效IP字符串
		if (request.getHeader("x-forwarded-for").contains(",")) {
			String[] ips = request.getHeader("x-forwarded-for").split(",");
			for (String string : ips) {
				if (!"unknown".equalsIgnoreCase(string)) {
					return transIP(string);
				}
			}
			return "unknown";
		}
		return transIP(request.getHeader("x-forwarded-for"));
	}

	private static String transIP(String ipAddress) {
		if ("127.0.0.1".equals(ipAddress)) {
			// 根据网卡取本机配置的IP
			InetAddress inet = null;
			try {
				inet = InetAddress.getLocalHost();
				ipAddress = inet.getHostAddress();
			} catch (Exception e) {
				log.error("get ip address error", e);
			}
		}
		return ipAddress;
	}

	/**
	 * 查询IP地址归属地址
	 * @param ip
	 * @return
	 */
	public static String getIpLocation(String ip) {
		if (StringUtil.isBlank(ip)) {
			return "";
		}

		if (ip.indexOf("0:0:0:0") >= 0 || ip.indexOf("127.0.0.1") >= 0) {
			return "本地";
		}

		Long nowTime = System.currentTimeMillis();
		//String url = "http://ip.ws.126.net/ipquery?ip=" + ip;
		String url = "https://sp1.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=" + ip + "&co=&resource_id=5809&t=" + nowTime + "&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110208788642735551251_" + nowTime + "&_=1631975638871";

		String response = null;
		try {
			response = HttpClientUtil.get(url, "UTF-8");
		} catch (Exception e) {
			log.error("查询IP地址的归属地址错误, ip: {}, e: ", ip, e);
			return "查询IP地址的归属地址错误";
		}

		int index = response.indexOf("\"location\":\"");
		int endIndex = response.indexOf("\",\"userip\"");
		if (index < 0 || endIndex <= 0 || index >= endIndex) {
			return "获取IP地址的归属地址错误";
		}

		String locationUnicode = response.substring(index + 12, endIndex);
		String location = null;
		try {
			location = UnicodeUtil.convertUnicodeToCh(locationUnicode);
		} catch (Exception e) {
			log.error("获取IP地址的归属地址错误, ip: {}, e: ", ip, e);
			return "获取IP地址的归属地址错误";
		}

		return location;
	}
}
