package com.naonao.grab12306ticket.version.springboot.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot
 * @description: get spring boot bean
 * @author: Wen lyuzhao
 * @create: 2019-05-22 21:51
 **/
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    /**
     * context instance
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    /**
     * getApplicationContext
     * @return  ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     *get bean by name
     * @param name  bean name
     * @return      bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }


    /**
     * get bean by class
     * @param clazz     class
     * @param <T>       generic
     * @return          bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * get bean by name and class
     * @param name      bean name
     * @param clazz     class
     * @param <T>       generic
     * @return          bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
