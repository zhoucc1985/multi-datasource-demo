package com.example.multidatasourcedemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装json结果集
 */
@Getter
@Setter
@ApiModel(value = "返回json结果集")
public class JsonResult<T> {

    @ApiModelProperty(value = "返回是否成功")
    private String state ;

    @ApiModelProperty(value = "返回信息")
    private String msg ;

    @ApiModelProperty(value = "返回其他对象信息")
    private T datas = null;


    public JsonResult<T> setDatas(T datas) {
        this.datas = datas;
        return this;
    }

    public JsonResult<T> setStatus(String state) {
        this.state=state;
        return this;
    }

    public JsonResult<T> setMessage(String message) {
        this.msg = message;
        return this;
    }



}