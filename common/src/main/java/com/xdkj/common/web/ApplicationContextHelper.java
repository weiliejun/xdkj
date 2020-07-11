package com.xdkj.common.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 工具类
 */
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext appCtx;

    public static ApplicationContext getApplicationContext() {
        return appCtx;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }

    /**
     * 获取bean
     */
    public static Object getBean(String beanName) {
        return appCtx.getBean(beanName);
    }

}
