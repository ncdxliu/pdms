package com.dnliu.pdms.service;

import com.dnliu.pdms.model.GetCheckPwd;
import com.dnliu.pdms.model.Register;
import com.dnliu.pdms.model.ResetCheckPwd;
import com.dnliu.pdms.model.UpdatePassword;

import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-12 13:31
 */
public interface RegisterService {
    Map register(Register register);

    Map updatePassword(UpdatePassword updatePassword);

    Map resetCheckPwd(ResetCheckPwd resetCheckPwd);

    Map getCheckPwd(GetCheckPwd getCheckPwd);

}
