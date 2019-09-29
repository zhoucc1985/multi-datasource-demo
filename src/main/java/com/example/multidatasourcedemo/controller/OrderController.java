package com.example.multidatasourcedemo.controller;

import com.example.multidatasourcedemo.services.order.IOderService;
import com.example.multidatasourcedemo.utils.ResultGenerator;
import com.example.multidatasourcedemo.vo.sys.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单服务。测试用策略模式和注解替换if-else
 * @author zhoucc
 * @date 2019/9/27 09:45
 */

@Api(tags = "订单服务", description = "订单服务")
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    @Qualifier("orderServiceV2Impl")
    IOderService OrderServiceV2Impl;

    @GetMapping
    @ApiOperation(value = "根据类型获取订单")
    public Result<String> getOrderByType(@ApiParam(name = "type", value = "订单类型", example = "1", required = true) @RequestParam String type) {
        return ResultGenerator.genSuccessResult(OrderServiceV2Impl.handle(type));
    }
}
