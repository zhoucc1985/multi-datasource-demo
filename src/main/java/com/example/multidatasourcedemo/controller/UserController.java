package com.example.multidatasourcedemo.controller;

import com.example.multidatasourcedemo.Component.CurrentUser;
import com.example.multidatasourcedemo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: UserController
 * @Auther: zhoucc
 * @Date: 2019/6/3 11:28
 * @Description:
 */

@RestController
@Slf4j
public class UserController{

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

    @RequestMapping(value = "/hello", method= RequestMethod.GET)
    public String hello(@RequestParam String name, @CurrentUser String curUser) {
        log.info("user : {}", curUser);
        return "Hello " + name;
    }

    @RequestMapping(value = "/exception", method= RequestMethod.GET)
    public String hello() throws InterruptedException {
        Thread.sleep(10000);
        int i = 1/0;
        return "exception";
    }
}
