package com.zxtx.hummer.system.controller;

import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.config.BootdoConfig;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.service.DictService;
import com.zxtx.hummer.common.service.FileService;
import com.zxtx.hummer.common.utils.IPUtils;
import com.zxtx.hummer.common.utils.JwtUtil;
import com.zxtx.hummer.common.utils.MD5Utils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "登录")
@Controller
@Slf4j
public class LoginController extends BaseController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    MenuService menuService;
    @Autowired
    FileService fileService;
    @Autowired
    BootdoConfig bootdoConfig;
    @Autowired
    DictService dictService;

    @Log(value = "登录", argsPos = {0})
    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    Response ajaxLogin(@RequestParam String username, @RequestParam String password, String verify, HttpServletRequest request) {
        password = MD5Utils.encrypt(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        String val = redisTemplate.opsForValue().get(getLoginErrorKey(username, request));
        String loginErrorCount = dictService.getByNameWithCache("bg_login_erro_count");
        log.info("ip:{} 账号:{} 请求后台登录", IPUtils.getIpAddr(request), username);
        if (StringUtils.isNotEmpty(val) && Integer.parseInt(val) >= Integer.parseInt(loginErrorCount)) {
            throw new BusinessException(BizError.USER_IP_LOGIN_COUNT_ERROR, new Object[]{loginErrorCount});
        }
        try {
            subject.login(token);
            org.apache.shiro.session.Session curSession = ShiroUtils.getSubjct().getSession();
            redisTemplate.delete(getLoginErrorKey(username, request));
            String jwtTokenKey = String.format(Constants.LOGIN_USER_JWT_TOKEN, username, request.getSession().getId());
            String jwtToken = redisTemplate.opsForValue().get(jwtTokenKey);
            if (StrUtil.isBlank(jwtToken)) {
                jwtToken = JwtUtil.createToken(username, password);
                redisTemplate.opsForValue().set(jwtTokenKey, jwtToken, 24, TimeUnit.HOURS);
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("jwtToken", jwtToken);
            resultMap.put("username", username);
            resultMap.put("name", ShiroUtils.getUser().getName());
            resultMap.put("mobile", ShiroUtils.getUser().getMobile());
            resultMap.put("employeeId", ShiroUtils.getUser().getEmployeeId());
            resultMap.put("emRoleType", ShiroUtils.getUser().getEmployeeRoleType());
            resultMap.put("companyName", ShiroUtils.getUser().getCompanyName());
            return Response.ok(resultMap);
        } catch (AuthenticationException e) {
            redisTemplate.opsForValue().increment(getLoginErrorKey(username, request), 1);
            redisTemplate.expire(getLoginErrorKey(username, request), 1, TimeUnit.HOURS);
            return Response.failed(BizError.INVALID_USER);
        }
    }

    //    @NotNull
    private String getLoginErrorKey(@RequestParam String username, HttpServletRequest request) {
        String ip = IPUtils.getIpAddr(request);
        return Constants.LOGIN_EROR_COUNT_REDIS + ip + ":" + username;
    }

    @GetMapping("/logout")
    String logout(HttpServletRequest request) {
        //退出的时候删除JWT
        String jwtTokenKey = String.format(Constants.LOGIN_USER_JWT_TOKEN, ShiroUtils.getUser().getUserName(), request.getSession().getId());
        if (redisTemplate.hasKey(jwtTokenKey)) {
            redisTemplate.delete(jwtTokenKey);
        }
        ShiroUtils.logout();
        return "redirect:/login";
    }
}