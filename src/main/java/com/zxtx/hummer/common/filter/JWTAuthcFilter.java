package com.zxtx.hummer.common.filter;

import com.google.common.base.Strings;
import com.zxtx.hummer.common.jwt.JwtToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JWTAuthcFilter extends AccessControlFilter {

    private final String headerKeyOfToken;

    private final boolean isDisabled;


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isDisabled) {
            log.info("Shiro Authentication is disabled, hence  can access api directly.");
            return true;
        } else {
            log.info("Shiro Authentication is enabled, to continue to execute onAccessDenied method");
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        // 登录状态判断
        log.info("onAccessDenied......");
        //从header或URL参数中查找token
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(headerKeyOfToken);
        if (Strings.isNullOrEmpty(authorization)) {
            authorization = req.getParameter(headerKeyOfToken);
        }
        JwtToken token = new JwtToken(authorization);
        try {
            getSubject(request, response).login(token);
        } catch (Exception e) {
            log.error("认证失败:" + e.getMessage());
            responseError(response);
            return false;
        }
        return true;

    }

    /**
     * 将非法请求跳转到 /filterError/**中
     */
    private void responseError(ServletResponse response) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendRedirect("/phone-admin/api/filterError");
        } catch (IOException e1) {
            log.error(e1.getMessage());
        }
    }

}
