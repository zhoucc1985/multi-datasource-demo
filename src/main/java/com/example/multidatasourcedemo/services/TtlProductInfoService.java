package com.example.multidatasourcedemo.services;

import com.example.multidatasourcedemo.pojo.TtlProductInfoPo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author kundy
 * @create 2019/2/16 11:22 AM
 */
public interface TtlProductInfoService {

    List<TtlProductInfoPo> listProduct(Map<String, Object> map);

    void export(HttpServletResponse response, HttpServletRequest request, String fileName);

}
