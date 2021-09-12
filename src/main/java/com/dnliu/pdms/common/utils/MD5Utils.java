/**   
 * Copyright © 2018 Liufh. All rights reserved.
 * 
 * @Package: main.java.blockchain.common.util 
 * @author: liufuhua
 * @date: 2018年10月18日 下午3:44:14 
 */
package com.dnliu.pdms.common.utils;

import java.security.MessageDigest;

/**
 * @desc: md5加解密工具
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2018年10月18日 上午10:40:08 
 *
 */
public class MD5Utils {
	// 转换字符的集合 这里可以根据自己喜好改一下顺序 达到改变生成的MD5的结果的作用 慢慢试 估计可能可以清楚顺序的
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	// 通过获取参数并且生成新的MD5校验码 然后和 获取到的MD5校验码 对比 以此验证传过来的数据是否被修改
	public static boolean validatePassword(String origin, String inputString, String charsetname) {
		if (origin.equals(MD5Encode(inputString, charsetname))) {
			return true;
		} else {
			System.out.println("MD5Encode: " + MD5Encode(inputString, charsetname) + ", origin: " + origin);
			return false;
		}
	}

	// 通过获取数据来生成MD5验证码 charsetname为"UTF-8"编码比较好
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digestString = md.digest(resultString.getBytes(charsetname));
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(digestString);
			else
				resultString = byteArrayToHexString(digestString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultString;
	}

	// 生成MD5序列的逻辑
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		String byteToHex = null;
		
		for (int i = 0; i < b.length; i++) {
			byteToHex = byteToHexString(b[i]);
			resultSb.append(byteToHex);
		}
		
		return resultSb.toString();
	}

	// 逻辑中每一位数据 在这里对应转换成两个字符
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	
	public static void main(String args[]) {
		String txnNo = "2018093014250100000000000000009";
		String reqTime = "2018093014261";
		String oriTxnNo = "20180930142501000000000000000091";
		String key = "ecochain";
		String src = txnNo + reqTime + oriTxnNo + key;
		
		String mac = MD5Encode(src, "UTF-8");
		System.out.println(mac);
	}
}
