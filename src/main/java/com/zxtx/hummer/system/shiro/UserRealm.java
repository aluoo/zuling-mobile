package com.zxtx.hummer.system.shiro;

import com.zxtx.hummer.common.config.ApplicationContextRegister;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.system.dao.exmapper.SysUserExMapper;
import com.zxtx.hummer.system.dao.mapper.SysUserMapper;
import com.zxtx.hummer.system.domain.UserDO;
import com.zxtx.hummer.system.service.MenuService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {
/*	@Autowired
	UserMapper userMapper;
	@Autowired
	MenuService menuService;*/


    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserExMapper sysUserExMapper;
    @Autowired
    private EmService emService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {


        Long userId = ShiroUtils.getUserId();
        MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
        Set<String> perms = menuService.listPerms(userId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(perms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = (String) token.getPrincipal();
        Map<String, Object> map = new HashMap<>(16);
        map.put("username", username);
        String password = new String((char[]) token.getCredentials());

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
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(loginUser, password, getName());
        return info;
    }

}