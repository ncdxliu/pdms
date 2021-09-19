package com.dnliu.pdms.dao;

import com.dnliu.pdms.entity.LoginLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dnliu
 * @date 2021-09-17 21:49
 */
public interface LoginLogMapper {
    int insert(LoginLog record);

    List<LoginLog> selectLoginLogByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
}
