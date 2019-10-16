package com.example.multidatasourcedemo.services;

import com.example.multidatasourcedemo.dao.primary.UserDao;
import com.example.multidatasourcedemo.dao.second.UserSecondDao;
import com.example.multidatasourcedemo.exception.RollbackException;
import com.example.multidatasourcedemo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName: UserService
 * @Auther: zhoucc
 * @Date: 2019/6/3 11:29
 * @Description:
 */

@Service
@Slf4j
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private UserSecondDao userSecondDao;

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

    public void insertUserOK() {
        User user1 = User.builder().userName("0001").build();
        userDao.insert(user1);
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
    public void insertException() {
        User user1 = User.builder().userName("0001").build();
        userDao.insert(user1);
        User user2 = User.builder().userName("0002").build();
        userDao.insert(user2);
        throw new RuntimeException();
    }

    /**
     * 自调用问题。前提条件：主事务方法自调用捕获异常不抛出异常事务方法，这样主事务方法无法感知到异常，也就无法回滚。如果自调用方法抛出异常，则主方法感知会回滚。自调用方法的对象不是代理对象，所以不会进行AOP处理回滚。
     * @throws RollbackException
     */
    @Transactional
    public void insertTest1(){
        //没有回滚
        try {
            insertException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 没有开启事务，自调用insert1，利用userService代理类异常触发它的事务
     * @throws RollbackException
     */
    @Transactional
    public void insertRollBackOK(){
        //自调用，要想回滚
        log.info("请求开始");
        UserService userService = ((UserService) AopContext.currentProxy());
        try {
            userService.insertException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取主数据源中的所有的用户
     */
    @Transactional
    public List<User> listPrimary(){
        return userDao.list();
    }

    /**
     * 获取从数据源中的所有的用户
     */
    public List<User> listSecond() {
        return userSecondDao.list();
    }

    @Transactional
    public List<User> list() {
        List<User> primaryUsers =  userDao.list();
        primaryUsers.addAll(userSecondDao.list());
        return primaryUsers;
    }

    /**
     * 默认插入事务
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User user) {
        userDao.insert(user);
    }

    /**
     * 新建新的事务
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(User user){
        userDao.insert(user);
    }

    /**
     * 插入NESTED事务
     */
    @Transactional(propagation = Propagation.NESTED)
    public void addNested(User user){
        userDao.insert(user);
    }

    /**
     * 通过ids字符串批量查询用户
     * @param ids 以逗号连接的id集合字符串。1,2,3,4
     * @return 用户集合
     */
    public List<User> getUsersByIds(String ids) {
        List<Long> idList = Arrays.asList(ids.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
        return userDao.findSelectiveByIds(idList);
    }

    /**
     * 批量插入用户
     * @param users 用户对象
     * @return 成功个数
     */
    public int insertBatch(List users) {
        return userDao.insertSelective(users);
    }

    /**
     * 批量删除用户
     * @param ids 用户id集合
     * @return 成功个数
     */
    public int deleteBatch(String ids) {
        List<Long> idList = Arrays.asList(ids.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
        return userDao.deleteSelectiveByIds(idList);
    }

    /**
     * 批量更新用户
     * @return
     */
    public int updateBatch() {
        List<User> users = Arrays.asList(new User((long) 138, "张三"), new User((long) 139, "李四"));
        List ids = users.stream().map(User::getId).collect(Collectors.toList());
        return userDao.updateSelective(users, ids);
    }
}
