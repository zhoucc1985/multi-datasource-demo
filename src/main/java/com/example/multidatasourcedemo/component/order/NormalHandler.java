package com.example.multidatasourcedemo.component.order;

import com.example.multidatasourcedemo.annotation.HandlerType;
import com.example.multidatasourcedemo.pojo.order.AbstractHandler;
import org.springframework.stereotype.Component;

/**
 * 普通订单处理器
 * @author zhoucc
 * @date 2019/9/27 14:11
 */

@Component
@HandlerType("2")
public class NormalHandler implements AbstractHandler {
    @Override
    public String handler(String type) {
        return "处理普通订单";
    }
}
