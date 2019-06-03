package com.example.multidatasourcedemo.services;

import com.example.multidatasourcedemo.Dao.UserDao;
import com.example.multidatasourcedemo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

    @Transient
    public User findOne(int id) {
        return userDao.find();
    }
}
