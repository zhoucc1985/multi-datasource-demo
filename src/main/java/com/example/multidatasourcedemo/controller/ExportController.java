package com.example.multidatasourcedemo.controller;

import com.example.multidatasourcedemo.services.TtlProductInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商品导出Controller
 * @author zhoucc
 * @date 2019/10/9 16:36
 */

@Api(tags = "Excel表格导出", description = "Excel表格导出")
@RestController
@RequestMapping("/excelUtils")
public class ExportController {

    @Autowired
    private TtlProductInfoService productInfoService;

    @ApiOperation(value = "Excel表格导出")
    @GetMapping("/export")
    public void export(HttpServletResponse response, HttpServletRequest request) {
        this.productInfoService.export(response, request, "商品信息");
    }

}
