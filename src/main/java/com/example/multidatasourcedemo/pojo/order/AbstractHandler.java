package com.example.multidatasourcedemo.pojo.order;

/**
 * 订单策略处理接口
 * @author zhoucc
 * @date 2019/9/27 14:09
 */
public interface AbstractHandler {
    String handler(String type);
}
