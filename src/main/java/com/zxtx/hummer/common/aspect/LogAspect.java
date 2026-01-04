package com.zxtx.hummer.common.aspect;

import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.domain.LogDO;
import com.zxtx.hummer.common.service.LogService;
import com.zxtx.hummer.common.utils.HttpContextUtils;
import com.zxtx.hummer.common.utils.IPUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.system.shiro.LoginUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    LogService logService;


    @Pointcut("@annotation(com.zxtx.hummer.common.annotation.Log)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        // 执行方法
        try {
            result = point.proceed();
            // 执行时长(毫秒)
        } catch (Exception e) {
            long time = System.currentTimeMillis() - beginTime;
            //异步保存日志
            saveLog(point, time);
            throw e;
        }
        long time = System.currentTimeMillis() - beginTime;
        //异步保存日志
        saveLog(point, time);
        return result;
    }

    void saveLog(ProceedingJoinPoint joinPoint, long time) throws InterruptedException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogDO sysLog = new LogDO();
        Log syslog = method.getAnnotation(Log.class);
        if (syslog != null) {
            // 注解上的描述
            sysLog.setOperation(syslog.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        try {
            if (!syslog.noArgs()) {
                // 请求的参数
                Object[] args = joinPoint.getArgs();
                int[] argsPos = syslog.argsPos();
                if (argsPos.length > 0) {
                    Object[] argsNew = new Object[argsPos.length];
                    for (int i = 0; i < argsPos.length; i++) {
                        argsNew[i] = args[argsPos[i]];
                    }
                    sysLog.setParams(Arrays.toString(argsNew));
                } else {
                    sysLog.setParams(Arrays.toString(args));
                }
            }
        } catch (Exception e) {
        }
        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        // 用户名
        LoginUser currUser = ShiroUtils.getUser();
        if (null == currUser) {
            if (null != sysLog.getParams()) {
                sysLog.setUserId(-1L);
                sysLog.setUsername(sysLog.getParams());
            } else {
                sysLog.setUserId(-1L);
                sysLog.setUsername("获取用户信息为空");
            }
        } else {
            sysLog.setUserId(ShiroUtils.getUserId());
            sysLog.setUsername(ShiroUtils.getUser().getUserName());
        }
        sysLog.setTime((int) time);
        // 系统当前时间
        Date date = new Date();
        sysLog.setGmtCreate(date);
        // 保存系统日志
        logService.save(sysLog);
    }
}
