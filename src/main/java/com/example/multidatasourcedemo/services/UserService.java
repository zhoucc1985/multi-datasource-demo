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

    @Transactional
    public User findOne(int id) {
        return userDao.find();
    }

    @Transactional
    public void insert() {
        User user1 = User.builder().userName("0001").build();
        userDao.insert(user1);
        User user2 = User.builder().userName("0002").build();
        // 模拟事务中原子操作失败，观察事务是否回滚
        int i = 1/0;
        userDao.insert(user2);
    }
}
