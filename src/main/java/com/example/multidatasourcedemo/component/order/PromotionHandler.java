package com.example.multidatasourcedemo.component.order;

import com.example.multidatasourcedemo.annotation.HandlerType;
import com.example.multidatasourcedemo.pojo.order.AbstractHandler;
import org.springframework.stereotype.Component;

/**
 * 促销订单处理器
 * @author zhoucc
 * @date 2019/9/27 14:11
 */

@Component
@HandlerType("3")
public class PromotionHandler implements AbstractHandler {
    @Override
    public String handler(String type) {
        return "处理促销订单";
    }
}
