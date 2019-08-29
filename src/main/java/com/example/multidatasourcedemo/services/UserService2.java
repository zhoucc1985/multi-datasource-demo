package com.example.multidatasourcedemo.services;

import com.example.multidatasourcedemo.Dao.UserDao;
import com.example.multidatasourcedemo.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zhoucc
 * @description
 * @date 2019/8/29 11:00
 */

@Service
public class UserService2 {
    @Resource
    private UserDao userDao;

    /**
     * 默认插入事务
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addRequired(User user) {
        userDao.insert(user);
    }

    /**
     * 默认异常插入事务
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addRequiredException(User user){
        userDao.insert(user);
        throw new RuntimeException();
    }

    /**
     * 插入事务
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(User user){
        userDao.insert(user);
    }

    /**
     * 插入事务异常
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNewException(User user){
        userDao.insert(user);
        throw new RuntimeException();
    }

    /**
     * 插入异常事务
     */
    @Transactional(propagation = Propagation.NESTED)
    public void addNested(User user){
        userDao.insert(user);
    }

    /**
     * 插入异常NESTED事务
     */
    @Transactional(propagation = Propagation.NESTED)
    public void addNestedException(User user){
        userDao.insert(user);
        throw new RuntimeException();
    }

}
