package com.zxtx.hummer.system.service;

import com.zxtx.hummer.system.domain.UserDO;
import com.zxtx.hummer.system.domain.UserOnline;
import com.zxtx.hummer.system.shiro.LoginUser;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 待完善
 *
 * @author bootdo
 */
@Service
public class SessionService {
    private final SessionDAO sessionDAO;

    @Autowired
    public SessionService(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public List<UserOnline> list() {
        List<UserOnline> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            UserOnline userOnline = new UserOnline();
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);/* UserDO userDO = (UserDO) principalCollection.getPrimaryPrincipal();
                userOnline.setUsername(userDO.getUsername());*/

                LoginUser loginUser = (LoginUser) principalCollection.getPrimaryPrincipal();
                userOnline.setUsername(loginUser.getUserName());
            }
            userOnline.setId((String) session.getId());
            userOnline.setHost(session.getHost());
            userOnline.setStartTimestamp(session.getStartTimestamp());
            userOnline.setLastAccessTime(session.getLastAccessTime());
            userOnline.setTimeout(session.getTimeout());
            list.add(userOnline);
        }
        return list;
    }

    public List<UserDO> listOnlineUser() {
        List<UserDO> list = new ArrayList<>();
        UserDO userDO;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                userDO = (UserDO) principalCollection.getPrimaryPrincipal();
                list.add(userDO);
            }
        }
        return list;
    }

    public Collection<Session> sessionList() {
        return sessionDAO.getActiveSessions();
    }

    public boolean forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        sessionDAO.delete(session);
//        session.setTimeout(0);
        return true;
    }
}
