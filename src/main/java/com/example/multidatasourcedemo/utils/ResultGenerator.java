package com.example.multidatasourcedemo.utils;

import com.example.multidatasourcedemo.vo.sys.Result;

/**
 * 响应结果生成工具
 *
 * @author zhoucc
 */
public class ResultGenerator {

    private static final String DEFAULT_SUCCESS_MESSAGE = "执行成功";
    private static final String DEFAULT_FAIL_MESSAGE = "执行失败";
    private static final String RETURN_STATE_SUC = "suc";
    private static final String RETURN_STATE_FAIL = "fail";

    /**
     * 产生调用成功消息
     *
     * @return
     */
    public static <T> Result<T> genSuccessResult() {
        return new Result<T>().setStatus(RETURN_STATE_SUC).setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 产生调用成功消息
     *
     * @param data 数据内容
     * @return
     */
    public static <T> Result<T> genSuccessResult(T data) {
        return new Result<T>().setStatus(RETURN_STATE_SUC).setMessage(DEFAULT_SUCCESS_MESSAGE).setDatas(data);
    }



    /**
     * 调用失败结果
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> genFailResult() {
        return new Result<T>().setStatus(RETURN_STATE_FAIL).setMessage(DEFAULT_FAIL_MESSAGE);
    }

    /**
     * 产生调用失败结果
     *
     * @param message 失败原因
     * @return
     */
    public static <T> Result<T> genFailResult(String message) {
        return new Result<T>().setStatus(RETURN_STATE_FAIL).setMessage(message);
    }
}
