package com.zxtx.hummer.common.controller;

import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.system.shiro.LoginUser;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {


    public LoginUser getUser() {
        return ShiroUtils.getUser();
    }

    public Long getUserId() {
        return getUser().getUserId();
    }

    //用户名
    public String getUsername() {
        return getUser().getUserName();
    }


}