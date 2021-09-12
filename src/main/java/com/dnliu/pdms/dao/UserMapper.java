package com.dnliu.pdms.dao;

import com.dnliu.pdms.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper {
    User selectByNamePwd(@Param("userName") String userName, @Param("password") String password);

    User selectUserByOpenid(@Param("openid") String openid);

    Long addWxUser(Map map);

    Long addUser(Map map);

    User selectUserById(@Param("id") Long id);

    void updatePassword(Map map);

    void updateCheckPwd(Map map);

    int countUserName(@Param("userName") String userName);

    int countUserPhone(@Param("phone") String phone);

    int countUserEmail(@Param("userEmail") String userEmail);
}