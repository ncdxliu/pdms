package com.dnliu.pdms.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-11 19:54
 */
public class ResponseUtil {
    /**
     * 通用失败响应
     * @param rspMsg
     * @return
     */
    public static Map<String, Object> getCommonFailResponse(String rspMsg) {
        Map<String, Object> resMap = new HashMap<>();

        resMap.put("rspCode", ResponseCode.ERROR_CODE);
        resMap.put("rspMsg", rspMsg);

        return resMap;
    }

    /**
     * 通用成功响应
     * @param rspMsg
     * @return
     */
    public static Map<String, Object> getCommonSuccessResponse(String rspMsg) {
        Map<String, Object> resMap = new HashMap<>();

        resMap.put("rspCode", ResponseCode.SUCCESS);
        resMap.put("rspMsg", rspMsg);

        return resMap;
    }
}
