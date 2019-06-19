package com.example.multidatasourcedemo.controller;

import com.example.multidatasourcedemo.Component.CurrentUser;
import com.example.multidatasourcedemo.Component.WebSocket;
import com.example.multidatasourcedemo.pojo.User;
import com.example.multidatasourcedemo.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: UserController
 * @Auther: zhoucc
 * @Date: 2019/6/3 11:28
 * @Description:
 */


@Slf4j
@Api(value="/test", tags="测试接口模块")
@Controller
public class UserController{

    @Autowired
    private UserService userService;


    @Autowired
    private WebSocket webSocket;

    @ResponseBody
    @RequestMapping("getUser/{id}")
    public String GetUser(@PathVariable int id){
        return userService.findOne(id).toString();
    }

    @ResponseBody
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

    @ResponseBody
    @RequestMapping(value = "/hello", method= RequestMethod.GET)
    public String hello(@RequestParam String name, @CurrentUser String curUser) {
        log.info("user : {}", curUser);
        return "Hello " + name;
    }

    @ApiOperation(value="异常测试", notes="")
    @ResponseBody
    @RequestMapping(value = "/exception", method= RequestMethod.GET)
    public String hello() throws InterruptedException {
        Thread.sleep(10000);
        int i = 1/0;
        return "exception";
    }

    @ApiOperation(value="获取所有的用户", notes="配合Thymeleaf页面展示")
    @RequestMapping("/list")
    public String  listUser(Model model) {
        List<User> userList = userService.list();
        model.addAttribute("users", userList);
        webSocket.sendMessage("有订单需要处理!");
        return "/user/list.html";
    }

    @ApiOperation(value="websocket测试", notes="返回websocket测试首页")
    @RequestMapping("/websocket")
    public String websocket() {
        return "/websocket/websocket.html";
    }
}
