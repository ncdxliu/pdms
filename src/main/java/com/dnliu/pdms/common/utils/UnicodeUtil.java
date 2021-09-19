package com.dnliu.pdms.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * unicode编码工具类
 * @author dnliu
 * @date 2021-09-18 22:51
 */
public class UnicodeUtil {
    /**
     * 将unicode字符串转为正常字符串
     *
     * @param str unicode字符串（比如"\u67e5\u8be2\u6210\u529f"）
     * @return 转换后的字符串（比如"查询成功"）
     */
    public static String convertUnicodeToCh(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\w{4}))");
        Matcher matcher = pattern.matcher(str);

        // 迭代，将str中的所有unicode转换为正常字符
        while (matcher.find()) {
            String unicodeFull = matcher.group(1); // 匹配出的每个字的unicode，比如\u67e5
            String unicodeNum = matcher.group(2); // 匹配出每个字的数字，比如\u67e5，会匹配出67e5

            // 将匹配出的数字按照16进制转换为10进制，转换为char类型，就是对应的正常字符了
            char singleChar = (char) Integer.parseInt(unicodeNum, 16);

            // 替换原始字符串中的unicode码
            str = str.replace(unicodeFull, singleChar + "");
        }
        return str;
    }

    public static void main(String[] args) {
        String unicodeStr = "\\u5e7f\\u4e1c\\u7701\\u5e7f\\u5dde\\u5e02\\u9ec4\\u57d4\\u533a \\u7535\\u4fe1";

        System.out.println(convertUnicodeToCh(unicodeStr));
    }
}
