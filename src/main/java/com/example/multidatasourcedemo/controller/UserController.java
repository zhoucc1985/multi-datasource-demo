package com.example.multidatasourcedemo.controller;

import com.example.multidatasourcedemo.Component.CurrentUser;
import com.example.multidatasourcedemo.Component.WebSocket;
import com.example.multidatasourcedemo.pojo.User;
import com.example.multidatasourcedemo.services.UserService;
import com.example.multidatasourcedemo.services.UserServiceTest;
import com.example.multidatasourcedemo.utils.ResultGenerator;
import com.example.multidatasourcedemo.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
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

    @Resource
    private UserServiceTest userServiceTest;


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
            userService.insertTest1();
        } catch (Exception e) {
            e.printStackTrace();
            return "插入失败";
        }
        return "插入成功";
    }

    @ResponseBody
    @PostMapping("/insertRollBackOK")
    public String insertRollBackOK() {
        try {
            userService.insertRollBackOK();
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
        List<User> userList = userService.listPrimary();
        model.addAttribute("users", userList);
        webSocket.sendMessage("有订单需要处理!");
        return "/user/list.html";
    }

    @ApiOperation(value="websocket测试", notes="返回websocket测试首页")
    @RequestMapping("/websocket")
    public String websocket() {
        return "/websocket/websocket.html";
    }

    /**
     * add.html用户添加
     */
    @RequestMapping("/add")
    public String add() {
        return "/user/add";
    }

    /**
     * update.html用户更新
     */
    @RequestMapping("/update")
    public String update() {
        return "/user/update";
    }

    /**
     * login.html自定义的登录页面
     */
    @RequestMapping(value = "/login", method=RequestMethod.GET)
    public String login() {
        return "/user/login";
    }

    /**
     * login.html自定义的登录页面
     */
    @RequestMapping("/testThymeleaf")
    public String testThymeleaf() {
        return "/user/test.html";
    }

    /**
     * 登录逻辑处理
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String name,String password,boolean rememberMe, Model model) {
        /*
         * 使用Shiro编写认证操作
         * 1.获取Subject
         * 2.封装用户数据
         * 3、执行登录方法
         */
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name,password, rememberMe);
        //3.执行登录方法
        try {
            subject.login(token);
            /*
             * 无任何异常，则登录成功
             * 跳转到test.html页面
             */
            Session session = subject.getSession();
            Serializable sessionId = session.getId();
            System.out.println("登录成功 -> " + sessionId);
            return "redirect:/testThymeleaf";		//redirect：重定向（不带数据），而非转发请求（带数据）
        } catch (UnknownAccountException e) {
            //UnKnownAccountException登录失败：用户名不存在
            model.addAttribute("msg", "用户名不存在");
            //带着msg数据，转发请求
            return "/user/login";
        } catch (IncorrectCredentialsException e) {
            //IncorrectCredentialsException登录失败：密码错误
            model.addAttribute("msg", "密码错误");
            return "/user/login";
        }
    }

    /**
     * 事务传递性测试
     */
    @RequestMapping(value = "/insert3", method = RequestMethod.POST)
    public void insert() {
//        userServiceTest.notransaction_exception_required_required();
//        userServiceTest.notransaction_required_required_exception();
//        userServiceTest.transaction_exception_required_required();
//        userServiceTest.transaction_required_required_exception();
//        userServiceTest.transaction_required_required_exception_try();
//        userServiceTest.notransaction_exception_requiresNew_requiresNew();
//        userServiceTest.notransaction_requiresNew_requiresNew_exception();
//        userServiceTest.transaction_exception_required_requiresNew_requiresNew();
//        userServiceTest.transaction_required_requiresNew_requiresNew_exception();
//        userServiceTest.transaction_required_requiresNew_requiresNew_exception_try();
//        userServiceTest.notransaction_exception_nested_nested();
//        userServiceTest.notransaction_nested_nested_exception();
//        userServiceTest.transaction_exception_nested_nested();
//        userServiceTest.transaction_nested_nested_exception();
        userServiceTest.transaction_nested_nested_exception_try();
    }

    /**
     * 获取主数据源中user表对象
     */
    @ResponseBody
    @RequestMapping("/listPrimary")
    public void getPrimaryUsers() {
        List<User> users = userService.listPrimary();
        users.stream().forEach(user -> System.out.println(user));
    }

    /**
     * 获取从数据源中user表对象
     */
    @ResponseBody
    @RequestMapping("/listSecond")
    public void getSecondUsers() {
        List<User> users = userService.listSecond();
        users.stream().forEach(user -> System.out.println(user));
    }

    /**
     * 获取从数据源中user表对象
     */
    @ResponseBody
    @RequestMapping("/all")
    public JsonResult<List<User>> getUsersFromAlDataSources() {
        List<User> users = userService.list();
        return ResultGenerator.genSuccessResult(users);
    }


    /**
     * 同时向两个数据源中插入数据。
     */
    @ResponseBody
    @PostMapping("/two")
    public JsonResult<String> insertTwo() {
        userServiceTest.insertTwoTest();
        return ResultGenerator.genSuccessResult();
    }
}
