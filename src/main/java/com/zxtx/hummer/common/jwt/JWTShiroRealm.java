package com.zxtx.hummer.common.jwt;

import com.zxtx.hummer.common.config.ApplicationContextRegister;
import com.zxtx.hummer.common.utils.JwtUtil;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.system.dao.exmapper.SysUserExMapper;
import com.zxtx.hummer.system.domain.UserDO;
import com.zxtx.hummer.system.service.MenuService;
import com.zxtx.hummer.system.shiro.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class JWTShiroRealm extends AuthorizingRealm {
    @Autowired
    private EmService emService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Long userId = ShiroUtils.getUserId();
        MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
        Set<String> perms = menuService.listPerms(userId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(perms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        System.out.println("认证-----------》》》》");
        System.out.println("1.toString ------>>> " + authenticationToken.toString());
        System.out.println("2.getCredentials ------>>> " + authenticationToken.getCredentials().toString());
        System.out.println("3. -------------》》 " + authenticationToken.getPrincipal().toString());
        String jwt = (String) authenticationToken.getCredentials();
        if (!JwtUtil.verify(jwt)) {
            throw new AuthenticationException("Token认证失败");
        }
        String username = JwtUtil.getClaim(jwt, "username");
        String password = JwtUtil.getClaim(jwt, "password");
        Map<String, Object> map = new HashMap<>(16);
        map.put("username", username);
        SysUserExMapper userMapper = ApplicationContextRegister.getBean(SysUserExMapper.class);
        // 查询用户信息
        UserDO user = userMapper.list(map).get(0);
        // 账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        // 密码错误
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }
        // 账号注销
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被注销,请联系管理员");
        }
        LoginUser loginUser = userMapper.getLoginUser(user.getUserId());
        Integer employeeRoleType = emService.getEmployeeRoleType(loginUser.getEmployeeId());
        loginUser.setEmployeeRoleType(employeeRoleType);
        return new SimpleAuthenticationInfo(loginUser, jwt, "JWTShiroRealm");

    }

}