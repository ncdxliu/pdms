package com.dnliu.pdms.controller;

import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.AppUtil;
import com.dnliu.pdms.common.utils.StringUtil;
import com.dnliu.pdms.model.Login;
import com.dnliu.pdms.model.WxLogin;
import com.dnliu.pdms.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private LoginService loginService;

    @Autowired
    LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 登录
     * @param login
     * @return
     */
    @RequestMapping("/login")
    public Map<String, Object> login(@RequestBody Login login) {
        if (login == null || StringUtil.isBlank(login.getUserName())
        || StringUtil.isBlank(login.getUserPassword())) {
            return ResponseUtil.getCommonFailResponse("请输入用户名和密码!");
        }

        return loginService.login(login);
    }

    /**
     * 微信登录
     * @param wxLogin
     * @return
     */
    @RequestMapping("/wxLogin")
    public Map<String, Object> wxLogin(@RequestBody WxLogin wxLogin) {
        if (wxLogin == null || StringUtil.isBlank(wxLogin.getCode())
                || StringUtil.isBlank(wxLogin.getUserPassword())) {
            return ResponseUtil.getCommonFailResponse("请输入用户名和密码!");
        }

        return loginService.wxLogin(wxLogin);
    }

    @RequestMapping("/loginOut")
    public Map<String, Object> loginOut() {
        AppUtil.setUser(null);

        return ResponseUtil.getCommonSuccessResponse("退出成功");
    }
}
