package com.example.multidatasourcedemo.controller;

import com.example.multidatasourcedemo.utils.ResultGenerator;
import com.example.multidatasourcedemo.vo.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: GlobalExceptionHandler
 * @Auther: zhoucc
 * @Date: 2019/6/14 11:13
 * @Description:
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult<String> jsonErrorHandler(HttpServletRequest req, Exception e) {
        return ResultGenerator.genFailResult(e.getMessage());
    }
}
