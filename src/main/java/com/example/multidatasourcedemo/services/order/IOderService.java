package com.example.multidatasourcedemo.services.order;

/**
 * 订单接口
 * @author zhoucc
 * @date 2019/9/27 11:29
 */
public interface IOderService {

    /**
     * 根据类型选择处理
     * @param type 订单类型
     * @return 订单
     */
    String handle(String type);
}
