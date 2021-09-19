package com.dnliu.pdms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.*;
import com.dnliu.pdms.dao.LoginLogMapper;
import com.dnliu.pdms.dao.UserMapper;
import com.dnliu.pdms.entity.LoginLog;
import com.dnliu.pdms.entity.User;
import com.dnliu.pdms.model.Login;
import com.dnliu.pdms.model.WxLogin;
import com.dnliu.pdms.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-11 20:14
 */
@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private UserMapper userMapper;

    private LoginLogMapper loginLogMapper;

    @Value("${weixing.appid}")
    private String appid;

    @Value("${weixing.code2SessionUrl}")
    private String code2SessionUrl;

    @Value("${weixing.secret}")
    private String secret;

    @Autowired
    LoginServiceImpl(UserMapper userMapper, LoginLogMapper loginLogMapper) {
        this.userMapper = userMapper;
        this.loginLogMapper = loginLogMapper;
    }

    /**
     * 登录
     * @param login
     * @return
     */
    @Override
    public Map<String, Object> login(Login login) {
        // 获取前端传入的参数
        String userName = login.getUserName();
        String password = login.getUserPassword();

        // 健壮性校验
        if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
            return ResponseUtil.getCommonFailResponse("用户密码或密码不正确");
        }

        // 查询用户信息
        User user = userMapper.selectByNamePwd(userName, password);
        if (user == null) {
            return ResponseUtil.getCommonFailResponse("用户不存在或者密码错误");
        }

        Map<String, Object> rspMap = ResponseUtil.getCommonSuccessResponse("登录成功");

        // 获取token
        String token = JwtUtil.createToken(user);
        rspMap.put("token", token);

        // 记录登录日志
        recordLoginLog(user, "网页版登录");

        return rspMap;
    }

    /**
     * 微信登录接口
     * @param wxLogin
     * @return
     */
    @Override
    public Map<String, Object> wxLogin(WxLogin wxLogin) {
        Map<String, Object> rspMap = new HashMap<>();

        String userName = "";

        String code = wxLogin.getCode();
        String userPassword = wxLogin.getUserPassword();
        String url = code2SessionUrl + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        String response = "";
        try {
            response = HttpClientUtil.get(url, "UTF-8");
        } catch (Exception e) {
            logger.error("获取微信openid错, code: {}, e: ", code, e);
            return ResponseUtil.getCommonFailResponse("获取微信openid失败");
        }

        JSONObject object = JSONObject.parseObject(response);
        String openid = (String) object.get("openid");

        User user = userMapper.selectUserByOpenid(openid);
        //新用户
        if (null == user) {
            user = addWxUser(openid);
            logger.info("--------微信用户[{}]注册完成--------", user.getUserName());
            rspMap.put("isNew", "1");
        } else {
            userName = user.getUserName();
            //如果需要校验登录密码
            if (null == user.getCheckPwd() || "0".equals(user.getCheckPwd())) {
                //密码校验
                if (!userPassword.equals(user.getUserPassword())) {
                    logger.info("密码不正确, userName: {}, userPassword: {}", userName, userPassword);
                    return ResponseUtil.getCommonFailResponse("密码不正确");
                }

                //判断是否是默认密码
                if (userPassword.equals(MD5Utils.MD5Encode("123456", "UTF-8"))) {
                    rspMap.put("isNew", "2");
                } else {
                    rspMap.put("isNew", "0");
                }
            } else {
                rspMap.put("isNew", "0");
            }
        }

        // 获取token
        String token = JwtUtil.createToken(user);

        rspMap.put("userId", user.getId());
        rspMap.put("checkPwd", user.getCheckPwd());
        rspMap.put("token", token);
        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "登录成功");

        //是否需要登录密码设置
        if ("1".equals(wxLogin.getCheckPwd())) {
            Map map = new HashMap<>();
            map.put("userId", user.getId());
            map.put("checkPwd", "1");

            userMapper.updateCheckPwd(map);
        }

        // 记录登录日志
        recordLoginLog(user, "小程序版登录");

        return rspMap;
    }

    /**
     * 新增微信用户注册
     * @param openid
     * @return
     */
    private User addWxUser(String openid) {
        Map<String, Object> map = new HashMap<>();

        String userName = "wx" + DateUtils.getNowTime();
        //用户名
        map.put("userName", userName);
        String userPassword = MD5Utils.MD5Encode("123456", "UTF-8");
        //用户登录密码
        map.put("userPassword", userPassword);
        //openid
        map.put("openid", openid);
        //注册时间
        map.put("registeredTime", DateUtils.getNowTime());
        //密码最后修改日期
        map.put("passwordLastChangeDay", DateUtils.getNowDate());
        //状态 0-正常 1-注销
        map.put("status", "0");
        //备注
        map.put("remark", "微信注册");

        Long id = userMapper.addWxUser(map);

        User user = new User();
        user.setId(id);
        user.setUserName(userName);

        return user;
    }

    /**
     * 记录登录日志
     * @param remark
     */
    private void recordLoginLog(User user, String remark) {
        String loginIp = AppUtil.getLoginIp();

        new Thread(() -> {
            Date nowDate = new Date();
            LoginLog loginLog = new LoginLog();
            loginLog.setUserId(user.getId());
            loginLog.setUserName(user.getUserName());
            loginLog.setLoginTime(DateUtils.getNowFormatTime());
            loginLog.setIpAddress(loginIp);
            String location = WebUtil.getIpLocation(loginIp);
            loginLog.setIpAddressParse(location);
            loginLog.setRemark(remark);
            loginLog.setCreateTime(nowDate);
            loginLog.setUpdateTime(nowDate);

            loginLogMapper.insert(loginLog);
        }).start();
    }
}
