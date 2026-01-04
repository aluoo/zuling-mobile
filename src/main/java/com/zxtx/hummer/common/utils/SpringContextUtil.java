package com.zxtx.hummer.common.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/8/8
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static String getProfile() {
        return getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }

    public static boolean isProduction() {
        return getProfile().equals("production");
    }
}