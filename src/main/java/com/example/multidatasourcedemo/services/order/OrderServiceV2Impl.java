package com.example.multidatasourcedemo.services.order;

import com.example.multidatasourcedemo.pojo.order.AbstractHandler;
import com.example.multidatasourcedemo.pojo.order.HandlerContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 策略模式封装业务
 * @author zhoucc
 * @date 2019/9/27 14:00
 */

@Service
public class OrderServiceV2Impl implements IOderService {

    @Resource
    private HandlerContext handlerContext;

    @Override
    public String handle(String type) {
        AbstractHandler handler = handlerContext.getInstance(type);
        return handler.handler(type);
    }
}
