package com.example.multidatasourcedemo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装json结果集
 * @author junli
 */
@Getter
@Setter
@ApiModel(value = "返回结果")
public class Result<T> {

    @ApiModelProperty(value = "返回是否成功")
    private String state ;

    @ApiModelProperty(value = "返回信息")
    private String msg ;

    @ApiModelProperty(value = "返回其他对象信息")
    private T datas = null;


    public Result<T> setDatas(T datas) {
        this.datas = datas;
        return this;
    }

    public Result<T> setStatus(String state) {
        this.state=state;
        return this;
    }

    public Result<T> setMessage(String message) {
        this.msg = message;
        return this;
    }



}
