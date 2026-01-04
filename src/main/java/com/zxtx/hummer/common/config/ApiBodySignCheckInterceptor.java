package com.zxtx.hummer.common.config;

import com.alibaba.fastjson.JSONObject;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.utils.DigestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiBodySignCheckInterceptor extends HandlerInterceptorAdapter {


    @Value("${api.express.huge.zto.seckey}")
    private String seckey = "jwt45609";

    private String signKey = "apiBSign";

    private Logger logger = LoggerFactory.getLogger(ApiBodySignCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String apiSignInf = request.getHeader(signKey);
        if (StringUtils.isEmpty(apiSignInf)) {
            throw new BaseException(BizError.SIGN_ERROR);
        }
        JSONObject joSignInf = JSONObject.parseObject(apiSignInf);
        String apiSignStr = joSignInf.getString("signStr");
        logger.debug("apiBodySignStr:{}", apiSignInf);

        String cp = joSignInf.getString("cp"); //暂时没用
        apiSignStr = apiSignStr.replaceAll("[\\s*\t\n\r]", "");
        if (!(HttpMethod.POST.name().equals(request.getMethod()))) {
            return true;
        }
        BodyReaderRequestWrapper servletRequestWrapper = new BodyReaderRequestWrapper(request);
        String params = servletRequestWrapper.getBodyString(request);
        logger.debug("apiBodySign paras:{}", params);
        if (StringUtils.isEmpty(params)) {
            throw new BaseException(BizError.SIGN_ERROR);
        }
        String realSignStr = DigestUtil.digest(params, seckey, "utf-8").
                replaceAll("[\\s*\t\n\r]", "");
        logger.debug("apiBodyRealSignStr:{}", realSignStr);
        if (!apiSignStr.equals(realSignStr)) {
            throw new BaseException(BizError.SIGN_ERROR);
        }
        return true;
    }


}
