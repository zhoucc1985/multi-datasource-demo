package com.example.multidatasourcedemo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SpringUtils
 * @Auther: zhoucc
 * @Date: 2019/5/31 11:37
 * @Description:
 */
@Component
public class SpringUtils implements ApplicationContextAware {
    private  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext(){
        return this.applicationContext;
    }

    public Object getBeanByName(String beanName) {
        return applicationContext.getBean(beanName);
    }
}