package com.example.multidatasourcedemo.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Excel中的商品信息对象
 * @author zhoucc
 * @date 2019/10/9 16:36
 */
@Data
@Accessors(chain = true)
public class TtlProductInfoPo {

    private Long id;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Long branchId;
    private String branchName;
    private Long shopId;
    private String shopName;
    private Double price;
    private Integer stock;
    private Integer salesNum;
    private String createTime;
    private String updateTime;
    private Byte isDel;

}
