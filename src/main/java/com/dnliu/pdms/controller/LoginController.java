package com.dnliu.pdms.controller;

import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.StringUtil;
import com.dnliu.pdms.model.Login;
import com.dnliu.pdms.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录处理类
 * @author dnliu
 * @date 2021-09-11 19:06
 */
@RestController
@RequestMapping("/pdms/loginApi")
@CrossOrigin
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     * @param login
     * @return
     */
    @RequestMapping("/login")
    public Map login(@RequestBody Login login) {
        if (login == null || StringUtil.isBlank(login.getUserName())
        || StringUtil.isBlank(login.getUserPassword())) {
            return ResponseUtil.getCommonFailResponse("请输入用户名和密码!");
        }

        return loginService.login(login);
    }

    @RequestMapping("/test")
    public Map test() {
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "token校验成功");

        return rspMap;
    }

}
