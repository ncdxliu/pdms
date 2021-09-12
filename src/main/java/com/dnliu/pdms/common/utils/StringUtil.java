/**   
 * Copyright © 2018 Liufh. All rights reserved.
 * 
 * @Package: main.java.blockchain.common.util 
 * @author: liufuhua
 * @date: 2018年10月24日 下午4:03:07 
 */
package com.dnliu.pdms.common.utils;

/**
 * 字符串处理工具类
 *
 * @author 刘福华
 * @date 2019年9月21日 下午4:16:10 
 *
 */
public class StringUtil {
    
    private StringUtil () {
        
    }
	
	/**
	 * 判断字符串是否为空
	 * @author liufuhua
	 * @param 
	 * @return 
	 * @throws
	 */
	public static boolean isEmpty(String str) {
		return (null == str || str.length() == 0);
	}
	
	/**
	 * 判断字符串是否为非空
	 * 
	 * @author liufuhua
	 * @param str
	 * @return 
	 * @throws
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	/**
	 * 是否空字符串
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * 是否空字符串
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
	
	/**
     * 截取给定字符串的后N为
     * 
     * @param str
     * @return
     */
    public static String getLastNwords(String str, int length) {
        if (StringUtil.isEmpty(str) || str.length() <= length) {
            return str;
        }

        return str.substring(str.length() - length);
    }
    
    /**
     * 获取用*隐藏的字符串
     * @param oldStr 旧的字符串
     *
     * @return newStr
     * */
    public static String getNewStrByStr(String oldStr){
        if (StringUtil.isBlank(oldStr)){
            return oldStr;
        }

        int len = oldStr.length();
        int temLen = len - 4;

        //保留后4位
        oldStr =  oldStr.substring(temLen);

        //前面的用*填充
        StringBuilder buffer = new StringBuilder();
        for(int i =0; i < temLen; i++){
            buffer.append("*");
        }
        buffer.append(oldStr);

        return buffer.toString();
    }

    /***
     * 根据长度生成字符串 不够的用空格填充
     *@param param
     *@param len 定长
     *
     *@return  newString
     */
    public static String fomatStr(String param, int len){
        String space = " ";
        StringBuilder outParam = new StringBuilder();
        
        //param 无值
        if(StringUtil.isEmpty(param)){
            for(int i = 0; i < len; i++){
                outParam.append(space);
            }
            
            return outParam.toString();
        } else {
            //param 有值
            int newLen = param.length();
            //字符串长度超过规定长度则截取定长
            if(newLen >= len){
                param = param.substring(0, len);
                
                return param;
            } else {
                //计算不够长度
                int e = len - newLen;
                outParam.append(param);
                for(int i = 0; i < e; i++){
                    outParam.append(space);
                }
            }
            
            return outParam.toString();
        }
    }
}
