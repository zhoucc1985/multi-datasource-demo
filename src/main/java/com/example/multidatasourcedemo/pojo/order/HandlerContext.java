package com.example.multidatasourcedemo.pojo.order;

import com.example.multidatasourcedemo.component.BeanTool;

import java.util.Map;

/**
 * 获取对应的订单处理器
 * @author zhoucc
 * @date 2019/9/29 10:15
 */
public class HandlerContext {

    private Map<String, Class> handlerMap;

    public HandlerContext(Map<String, Class> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public AbstractHandler getInstance(String type) {
        Class clazz = handlerMap.get(type);
        if (clazz == null) {
            throw new IllegalArgumentException("not found handler for type: " + type);
        }
        return (AbstractHandler) BeanTool.getBean(clazz);
    }
}
