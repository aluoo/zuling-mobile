package com.zxtx.hummer.common.exception;

import com.zxtx.hummer.common.service.LogService;
import com.zxtx.hummer.common.utils.HttpServletUtils;
import com.zxtx.hummer.common.utils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class BDExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    LogService logService;
//
//    /**
//     * 自定义异常
//     */
//    @ExceptionHandler(BDException.class)
//    public R handleBDException(BDException e) {
//        logger.error(e.getMessage(), e);
//        R r = new R();
//        r.put("code", e.getCode());
//        r.put("msg", e.getMessage());
//        return r;
//    }
//
//    @ExceptionHandler(DuplicateKeyException.class)
//    public R handleDuplicateKeyException(DuplicateKeyException e) {
//        logger.error(e.getMessage(), e);
//        return R.error("数据库中已存在该记录");
//    }
//
//    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
//    public R noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
//        logger.error(e.getMessage(), e);
//        return R.error(404, "没找找到页面");
//    }

    @ExceptionHandler(BaseException.class)
    public R handleBaseException(BaseException e) {
        R r = new R();
        r.put("code", e.getError().getCode());
        r.put("msg", e.getError().getMessage());
        r.put("message", e.getError().getMessage());
        return r;
    }

    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        if (HttpServletUtils.jsAjax(request)) {
            return R.error(403, "未授权");
        }
        return new ModelAndView("error/403");
    }


    @ExceptionHandler({Exception.class})
    public Object handelException(Exception e, HttpServletRequest request) {
        logger.error("ajax请求异常", e);
        if (request.getRequestURI().startsWith("/nvwa/api/express/")) {
            R r = new R();
            if (e instanceof BindException) {
                r.put("code", BizError.INVALID_PARAMETER.getCode());
                r.put("msg", e.getLocalizedMessage());
                r.put("message", e.getLocalizedMessage());
            } else {
                r.put("code", BizError.SYSTEM_INTERNAL_ERROR.getCode());
                r.put("msg", BizError.SYSTEM_INTERNAL_ERROR.getMessage());
                r.put("message", BizError.SYSTEM_INTERNAL_ERROR.getMessage());
            }
            return r;
        }
        if (HttpServletUtils.jsAjax(request)) {
            R r = new R();
            r.put("code", BizError.SYSTEM_INTERNAL_ERROR.getCode());
            r.put("msg", BizError.SYSTEM_INTERNAL_ERROR.getMessage());
            r.put("message", BizError.SYSTEM_INTERNAL_ERROR.getMessage());
            return r;
        }
        return new ModelAndView("error/500");
    }

//
//    @ExceptionHandler({Exception.class})
//    public Object handleExceptionh(Exception e, HttpServletRequest request) {
//        LogDO logDO = new LogDO();
//        logDO.setGmtCreate(new Date());
//        logDO.setOperation(Constant.LOG_ERROR);
//        logDO.setMethod(request.getRequestURL().toString());
//        logDO.setParams(e.toString());
//        UserDO current = ShiroUtils.getUser();
//        if(null!=current){
//            logDO.setUserId(current.getUserId());
//            logDO.setUsername(current.getUsername());
//        }
//        logService.save(logDO);
//        logger.error(e.getMessage(), e);
//        if (HttpServletUtils.jsAjax(request)) {
//            return R.error(500, "服务器错误，请联系管理员");
//        }
//        return new ModelAndView("error/500");
//    }
}
