package com.dnliu.pdms.service.impl;

import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.AppUtil;
import com.dnliu.pdms.common.utils.DateUtils;
import com.dnliu.pdms.common.utils.StringUtil;
import com.dnliu.pdms.dao.UserMapper;
import com.dnliu.pdms.entity.User;
import com.dnliu.pdms.model.GetCheckPwd;
import com.dnliu.pdms.model.Register;
import com.dnliu.pdms.model.ResetCheckPwd;
import com.dnliu.pdms.model.UpdatePassword;
import com.dnliu.pdms.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-12 13:42
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    private UserMapper userMapper;

    @Autowired
    RegisterServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 注册
     * @param register
     * @return
     */
    @Override
    public Map<String, Object> register(Register register) {
        Map<String, Object> rspMap = new HashMap<>();

        String userName = register.getUserName();
        String userPhone = register.getUserPhone();
        String userEmail = register.getUserEmail();
        String userPassword = register.getUserPassword();

        //判断用户名是否已注册
        int count = userMapper.countUserName(userName);
        if (count > 0) {
            return ResponseUtil.getCommonFailResponse("该用户名已注册");
        }

        //判断邮箱是否已注册
        count = userMapper.countUserEmail(userEmail);
        if (count > 0) {
            return ResponseUtil.getCommonFailResponse("该邮箱已注册");
        }

        //判断手机号码是否已注册
        if (!StringUtil.isEmpty(userPhone)) {
            count = userMapper.countUserPhone(userPhone);
            if (count > 0) {
                return ResponseUtil.getCommonFailResponse("该手机号码已注册");
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        map.put("userPassword", userPassword);
        map.put("userEmail", userEmail);
        map.put("userPhone", userPhone);
        map.put("openid", "");
        map.put("registeredTime", DateUtils.getNowTime());
        map.put("remark", "网页注册");
        map.put("passwordLastChangeDay", DateUtils.getNowDate());
        map.put("status", "0");

        userMapper.addUser(map);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "注册成功");

        return rspMap;
    }

    /**
     * 修改密码
     * @param updatePassword
     * @return
     */
    @Override
    public Map<String, Object> updatePassword(UpdatePassword updatePassword) {
        Map<String, Object> rspMap = new HashMap<>();

        String userName = AppUtil.getUser().getUserName();

        User user = userMapper.selectByNamePwd(userName, updatePassword.getOldPassword());
        if (user == null) {
            return ResponseUtil.getCommonFailResponse("用户不存在或者密码错误");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("userPassword", updatePassword.getNewPassword());
        map.put("passwordLastChangeDay", DateUtils.getNowDate());
        userMapper.updatePassword(map);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "修改密码成功");

        return rspMap;
    }

    /**
     * 重置密码
     * @param resetCheckPwd
     * @return
     */
    @Override
    public Map<String, Object> resetCheckPwd(ResetCheckPwd resetCheckPwd) {
        Map<String, Object> rspMap = new HashMap<>();

        long userId = AppUtil.getUser().getId();

        String checkPwd = resetCheckPwd.getCheckPwd();

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("checkPwd", checkPwd);
        userMapper.updateCheckPwd(map);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "设置成功");

        return rspMap;
    };

    @Override
    public Map<String, Object> getCheckPwd(GetCheckPwd getCheckPwd) {
        Map<String, Object> rspMap = new HashMap<>();

        Long userId = AppUtil.getUser().getId();

        User user = userMapper.selectUserById(userId);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "设置成功");
        rspMap.put("checkPwd", user.getCheckPwd());

        return rspMap;
    };

    /**
     * 销户
     * @return
     */
    @Override
    public Map<String, Object> destoryUser() {
        Long id = AppUtil.getUser().getId();

        userMapper.destoryUser(id);

        return ResponseUtil.getCommonSuccessResponse("销户成功!");
    }

}
