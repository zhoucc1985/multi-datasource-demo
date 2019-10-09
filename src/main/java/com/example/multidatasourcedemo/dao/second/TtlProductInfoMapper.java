package com.example.multidatasourcedemo.dao.second;


import com.example.multidatasourcedemo.pojo.TtlProductInfoPo;

import java.util.List;
import java.util.Map;

/**
 * 商品mapper
 * @author zhoucc
 * @date 2019/10/9 16:36
 */

public interface TtlProductInfoMapper {

    List<TtlProductInfoPo> listProduct(Map<String, Object> map);

}
