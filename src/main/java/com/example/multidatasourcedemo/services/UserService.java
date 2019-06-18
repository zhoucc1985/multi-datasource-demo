package com.example.multidatasourcedemo.services;

import com.example.multidatasourcedemo.Dao.UserDao;
import com.example.multidatasourcedemo.exception.RollbackException;
import com.example.multidatasourcedemo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.List;


/**
 * @ClassName: UserService
 * @Auther: zhoucc
 * @Date: 2019/6/3 11:29
 * @Description:
 */

@Service
@Slf4j
public class UserService {

    @Autowired
    @Resource
    private UserDao userDao;

//    protected Logger logger = Logger.getLogger(getClass());

    //默认回滚类为RuntimeException或Error.所以回滚所有的非编译性异常
    @Transactional(rollbackFor = { Exception.class })
    public User findOne(int id) {
        return userDao.find();
    }

    /**
     * 回滚。因为添加rollbackFor = { Exception.class }。
     * 虽然调用该方法的UserController代理类捕捉到该方法异常。但是主要是UserService捕获到异常。
     */
    @Transactional(rollbackFor = { Exception.class })
    public void insert() {
        User user1 = User.builder().userName("0001").build();
        userDao.insert(user1);
        User user2 = User.builder().userName("0002").build();
        // 模拟事务中原子操作失败，观察事务是否回滚
        int i = 1/0;
        userDao.insert(user2);
    }

    /**
     * 不会回滚。因为该方法被自己捕获了。调用该方法的代理类无法捕捉到异常，所以就根据数据源中设置自动回滚。
     */
    @Transactional
    public void insertTest() {
        try {
            insert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void insert1() {
        User user1 = User.builder().userName("0001").build();
        userDao.insert(user1);
        User user2 = User.builder().userName("0002").build();
        // 模拟事务中原子操作失败，观察事务是否回滚
        int i = 1/0;
        userDao.insert(user2);
    }

    // 自调用问题。insert1是UserService类异常，代理类异常但是没有事务，直接提交，所以不会滚。如果要回滚，需要在调用方法上添加事务。这样才会滚
//    @Transactional
    public void insertTest1() throws RollbackException{
        //没有回滚
        insert1();
    }

    /**
     * 没有开启事务，自调用insert1，利用userService代理类异常触发它的事务
     * @throws RollbackException
     */
    public void insertTest2() throws RollbackException {
        //自调用，要想回滚
        log.info("请求开始");
        UserService userService = ((UserService) AopContext.currentProxy());
        userService.insert1();
    }

    /**
     * 获取所有的用户
     */
    @Transactional
    public List<User> list(){
        return userDao.list();
    }

}
