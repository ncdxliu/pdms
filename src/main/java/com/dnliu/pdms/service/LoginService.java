package com.dnliu.pdms.service;

import com.dnliu.pdms.model.Login;
import com.dnliu.pdms.model.WxLogin;

import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-11 20:14
 */
public interface LoginService {
    Map<String, Object> login(Login login);

    Map wxLogin(WxLogin wxLogin);
}
