package com.example.multidatasourcedemo.pojo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单对象
 * @author zhoucc
 * @date 2019/9/27 10:02
 */

@ApiModel(value = "订单信息")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class orderDTO {

    @ApiModelProperty(value = "订单编码")
    private String code;

    @ApiModelProperty(value = "订单价格")
    private BigDecimal price;

    @ApiModelProperty(value = "订单类型。1、普通类型；2、团购类型；3、促销类型")
    private String type;
}
