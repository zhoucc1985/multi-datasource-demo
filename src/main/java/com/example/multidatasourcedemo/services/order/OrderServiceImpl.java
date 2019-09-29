package com.example.multidatasourcedemo.services.order;

import org.springframework.stereotype.Service;

/**
 * 订单接口实现
 * @author zhoucc
 * @date 2019/9/27 11:31
 */
@Service
public class OrderServiceImpl implements IOderService {
    @Override
    public String handle(String type) {
        if ("1".equals(type)) {
            return "处理普通订单";
        } else if ("2".equals(type)) {
            return "处理团购订单";
        } else if ("3".equals(type)){
            return "处理团购订单";
        } else {
            return null;
        }
    }
}
