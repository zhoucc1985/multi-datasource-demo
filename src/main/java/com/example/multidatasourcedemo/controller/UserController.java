package com.example.multidatasourcedemo.controller;

import com.example.multidatasourcedemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: UserController
 * @Auther: zhoucc
 * @Date: 2019/6/3 11:28
 * @Description:
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("getUser/{id}")
    public String GetUser(@PathVariable int id){
        return userService.findOne(id).toString();
    }

    @RequestMapping("/insert")
    public String insertUser() {
        try {
            userService.insertTest2();
        } catch (Exception e) {
            e.printStackTrace();
            return "插入失败";
        }
        return "插入成功";
    }
}
