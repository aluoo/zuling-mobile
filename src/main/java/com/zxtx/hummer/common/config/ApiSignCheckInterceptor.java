package com.zxtx.hummer.common.config;

import com.alibaba.fastjson.JSONObject;
import com.zxtx.hummer.common.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiSignCheckInterceptor extends HandlerInterceptorAdapter {


    @Value("${api.user.seckey}")
    private String seckey = "jwt45609";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authInf = request.getHeader("etcAuth");
        if (StringUtils.isEmpty(authInf)) {
            throw new Exception("签名验证不通过");
        }
        JSONObject joAuthInf = JSONObject.parseObject(authInf);
        Long time = joAuthInf.getLong("time");
        Long curTime = System.currentTimeMillis();
        if (((curTime - time) / 10000) > 60 * 30) //30分钟过期
        {
            throw new Exception("签名过期");
        }
        String orgSignStr = joAuthInf.getString("sign");
        String newSignStr = MD5Utils.getMD5(time + seckey);
        if (!orgSignStr.equals(newSignStr)) {
            throw new Exception("签名验证不通过");
        }
        return true;
    }

}
