package com.dnliu.pdms.dao;

import com.dnliu.pdms.entity.LoginLog;

import java.util.List;
import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-17 21:49
 */
public interface LoginLogMapper {
    int insert(LoginLog record);

    List<LoginLog> loginLogBatch(Map<String, Object> map);

    List<LoginLog> loginLogBatchAll();
}
