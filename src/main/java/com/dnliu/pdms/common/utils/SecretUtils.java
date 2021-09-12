/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: main.java.ssm.common.util 
 * @author: liufuhua
 * @date: 2019年1月18日 上午10:41:48 
 */
package com.dnliu.pdms.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * Description: 加密解密的工具类 1、3DESC加解密 2、Base64加解密 Company:
 * 
 * @version 1.0
 * @author: liufuhua
 * @since: 2019年1月18日 上午10:41:48
 *
 */
public class SecretUtils {
	// 定义加密算法，有DES、DESede(即3DES)、Blowfish
	private static final String ALGORITHM = "DESede";
	// 加密密钥
	private static final String PASSWORD_CRYPT_KEY = "lfh-20170223-zaiguangfa*";

	/**
	 * 加密方法
	 * 
	 * @param src
	 *            源数据的字节数组
	 * @return
	 */
	public static byte[] encryptMode(byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), ALGORITHM); // 生成密钥
			Cipher c1 = Cipher.getInstance(ALGORITHM); // 实例化负责加密/解密的Cipher工具类
			c1.init(Cipher.ENCRYPT_MODE, deskey); // 初始化为加密模式
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}

		return null;
	}

	/**
	 * 解密函数
	 * 
	 * @param src
	 *            密文的字节数组
	 * @return
	 */
	public static byte[] decryptMode(byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), ALGORITHM);
			Cipher c1 = Cipher.getInstance(ALGORITHM);
			c1.init(Cipher.DECRYPT_MODE, deskey); // 初始化为解密模式
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据字符串生成密钥字节数组
	 * 
	 * @param keyStr
	 *            密钥字符串
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
		byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
		byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组

		// 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		if (key.length > temp.length) {
			// 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, temp.length);
		} else {
			// 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	}

	/**
	 * 将二进制数据编码为BASE64字符串
	 * 
	 * @param binaryData
	 * @return
	 */
	public static String encode(byte[] binaryData) {
		try {
			return new String(Base64.encodeBase64(binaryData), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将BASE64字符串恢复为二进制数据
	 * 
	 * @param base64String
	 * @return
	 */
	public static byte[] decode(String base64String) {
		try {
			return Base64.decodeBase64(base64String.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Description: 数据加密(3des+base64) 
	 * 
	 * @author: liufuhua 
	 * @since: 2019年1月18日 上午11:00:56 
	 * @param 
	 * @return 
	 * @throws
	 */
	public static String encryption(String src) throws Exception {
		// 3des加密
		byte[] secretArr = encryptMode(src.getBytes());
		if (null == secretArr) {
			throw new Exception("encryptMode加密失败!");
		}
		// base64加密
		String secretStr = encode(secretArr);
		if (null == secretStr) {
			throw new Exception("base64加密失败!");
		}

		return secretStr;
	}

	/**
	 * Description: 数据解密(base64+3des) 
	 * @author: liufuhua 
	 * @since: 2019年1月18日 上午11:04:09 
	 * @param 
	 * @return 
	 * @throws
	 * 
	 */
	public static String decrypt(String src) throws Exception {
		byte[] keyArr = decryptMode(decode(src));
		if (null == keyArr) {
			throw new Exception("解密失败");
		}

		return new String(keyArr);
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		// String msg = "cbs@123456";
		String msg = "ECOChain@123";
		System.out.println("加密前: " + msg);

		// 加密
		String secretStr = null;
		try {
			secretStr = encryption(msg);
		} catch (Exception e) {
			System.out.println("加密失败, error: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("加密后: " + secretStr);

		// 解密
		String keyStr = null;
		try {
			keyStr = decrypt(secretStr);
		} catch (Exception e) {
			System.out.println("解密失败, error: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("解密后: " + keyStr);
		
	}
}
