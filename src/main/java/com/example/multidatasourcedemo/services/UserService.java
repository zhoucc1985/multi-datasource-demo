package com.example.multidatasourcedemo.services;

import com.example.multidatasourcedemo.Dao.UserDao;
import com.example.multidatasourcedemo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;


/**
 * @ClassName: UserService
 * @Auther: zhoucc
 * @Date: 2019/6/3 11:29
 * @Description:
 */

@Service
public class UserService {

    @Autowired
    @Resource
    private UserDao userDao;

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
}
