package com.zxtx.hummer.common.utils;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.zxtx.hummer.system.shiro.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ShiroUtils {

    @Autowired
    private static SessionDAO sessionDAO;

    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }

    public static Long getUserIdByCatch() {
        try {
            return getUserId();
        } catch (Exception e) {
            log.debug("ShiroUtils.getUserIdByCatch.error: get user failed - {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }

    public static LoginUser getUserByCatch() {
        try {
            return getUser();
        } catch (Exception e) {
            log.debug("ShiroUtils.getUserByCatch.error: get user failed - {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }

    public static LoginUser getUser() {
        Object object = getSubjct().getPrincipal();
        return (LoginUser) object;
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }

    public static void logout() {
        getSubjct().logout();
    }

    public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }
}