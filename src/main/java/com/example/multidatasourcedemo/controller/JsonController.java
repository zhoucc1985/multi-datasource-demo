package com.example.multidatasourcedemo.controller;

import com.example.multidatasourcedemo.services.JsonService;
import com.example.multidatasourcedemo.utils.ResultGenerator;
import com.example.multidatasourcedemo.vo.HistoryInfo;
import com.example.multidatasourcedemo.vo.sys.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * 读取json文件到对象
 * @author zhoucc
 * @date 2019/9/29 11:34
 */

@Api(tags = "Json操作", description = "Json操作服务")
@RestController
@RequestMapping("/json")
@Slf4j
public class JsonController {

    @Resource
    JsonService jsonService;

    @GetMapping("/list")
    @ApiOperation(value = "根据json文件获取列表对象")
    public Result<List<HistoryInfo>> getListData() {
        return ResultGenerator.genSuccessResult(jsonService.getDataList());
    }

    @GetMapping
    @ApiOperation(value = "根据json文件获取对象")
    public Result<HistoryInfo> getData() {
        return ResultGenerator.genSuccessResult(jsonService.getData());
    }
}
