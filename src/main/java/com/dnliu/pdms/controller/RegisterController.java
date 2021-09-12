package com.dnliu.pdms.controller;

import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.StringUtil;
import com.dnliu.pdms.model.GetCheckPwd;
import com.dnliu.pdms.model.Register;
import com.dnliu.pdms.model.ResetCheckPwd;
import com.dnliu.pdms.model.UpdatePassword;
import com.dnliu.pdms.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-12 13:46
 */
@RestController
@RequestMapping("/pdms/registerApi")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    /**
     * @desc: 用户注册
     * @author: liufuhua
     * @since: 2019年3月15日 下午5:33:01
     * @param
     * @return
     * @throws
     */
    @RequestMapping("/register")
    public Map register(@RequestBody Register register) {
        if (register == null || StringUtil.isBlank(register.getUserName())
        || StringUtil.isBlank(register.getUserEmail()) || StringUtil.isBlank(register.getUserPassword())) {
            return ResponseUtil.getCommonFailResponse("请填写全部必填项");
        }

        return registerService.register(register);
    }

    /**
     * @desc: 用户修改密码
     * @author: liufuhua
     * @since: 2019年3月21日 下午3:09:47
     * @param
     * @return
     * @throws
     */
    @RequestMapping("/updatePassword")
    public Map updatePassword(@RequestBody UpdatePassword updatePassword) {
        if (updatePassword == null) {
            return ResponseUtil.getCommonFailResponse("请填写全部必填项");
        }

        return registerService.updatePassword(updatePassword);
    }

    /**
     * @desc: 登录是否需要输入密码设置
     * @author: liufuhua
     * @since: 2019年3月21日 下午3:09:47
     * @param
     * @return
     * @throws
     */
    @RequestMapping("/resetCheckPwd")
    public Map resetCheckPwd(@RequestBody ResetCheckPwd resetCheckPwd) {
        if (resetCheckPwd == null) {
            return ResponseUtil.getCommonFailResponse("请填写全部必填项");
        }

        return registerService.resetCheckPwd(resetCheckPwd);
    }

    /**
     * @desc: 是否需要输入密码登录查询
     * @author: liufuhua
     * @since: 2019年3月21日 下午3:09:47
     * @param
     * @return
     * @throws
     */
    @RequestMapping("/getCheckPwd")
    public Map getCheckPwd(@RequestBody GetCheckPwd getCheckPwd) {
        if (getCheckPwd == null) {
            return ResponseUtil.getCommonFailResponse("请填写全部必填项");
        }

        return registerService.getCheckPwd(getCheckPwd);
    }
}
