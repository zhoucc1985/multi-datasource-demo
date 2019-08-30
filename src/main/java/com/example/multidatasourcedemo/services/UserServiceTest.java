package com.example.multidatasourcedemo.services;

import com.example.multidatasourcedemo.Dao.primary.UserDao;
import com.example.multidatasourcedemo.Dao.second.UserSecondDao;
import com.example.multidatasourcedemo.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zhoucc
 * @description
 * @date 2019/8/29 11:31
 */

@Service
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private UserService2 userService2;

    @Resource
    private UserDao userDao;

    @Resource
    private UserSecondDao userSecondDao;


    /**
     * 两个数据源事务一个回滚是否影响另一个。
     */
    @Transactional
    public void insertTwoTest() {
        User user = User.builder().userName("zhoucc").build();
        userDao.insert(user);
        userSecondDao.insert(user);
        throw new RuntimeException();
    }

    /**
     * 此场景外围方法没有开启事务且抛出异常.
     * 外围方法未开启事务，插入“张三”、“李四”方法在自己的事务中独立运行，外围方法异常不影响内部插入“张三”、“李四”方法独立的事务。
     */
    public void notransaction_exception_required_required() {
        User user = new User();
        user.setUserName("张三");
        userService.addRequired(user);

        User user2 = new User();
        user2.setUserName("李四");
        userService2.addRequired(user2);

        throw new RuntimeException();
    }

    /**
     * 此场景外围方法没有开启事务且其中一个事务抛出异常.
     * 外围方法没有事务，插入“张三”、“李四”方法都在自己的事务中独立运行,所以插入“李四”方法抛出异常只会回滚插入“李四”方法，插入“张三”方法不受影响
     */
    public void notransaction_required_required_exception(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addRequired(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addRequiredException(user2);
    }

    /**
     * 外围方法开启事务，这个是使用率比较高的场景。
     *  “张三”、“李四”均未插入。外围方法开启事务，内部方法加入外围方法事务，外围方法回滚，内部方法也要回滚。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_exception_required_required() {
        User user = new User();
        user.setUserName("张三");
        userService.addRequired(user);

        User user2 = new User();
        user2.setUserName("李四");
        userService2.addRequired(user2);

        throw new RuntimeException();
    }

    /**
     * 外围方法开启事务，这个是使用率比较高的场景.
     * “张三”、“李四”均未插入。	外围方法开启事务，内部方法加入外围方法事务，内部方法抛出异常回滚，外围方法感知异常致使整体事务回滚。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_required_required_exception(){
        User user1 = new User();
        user1.setUserName("张三");
        userService.addRequired(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addRequiredException(user2);
    }

    /**
     * 外围方法开启事务，这个是使用率比较高的场景.
     * “张三”、“李四”均未插入。	外围方法开启事务，内部方法加入外围方法事务，内部方法抛出异常回滚，即使方法被catch不被外围方法感知，整个事务依然回滚。
     */
    @Transactional
    public void transaction_required_required_exception_try(){
        User user1 = new User();
        user1.setUserName("张三");
        userService.addRequired(user1);

        User user2=new User();
        user2.setUserName("李四");
        try {
            userService2.addRequiredException(user2);
        } catch (Exception e) {
            System.out.println("方法回滚");
        }
    }


    /**
     * 外围方法没有开启事务。
     * “张三”插入，“李四”插入。	外围方法没有事务，插入“张三”、“李四”方法都在自己的事务中独立运行,外围方法抛出异常回滚不会影响内部方法。
     */
    public void notransaction_exception_requiresNew_requiresNew(){
        User user1 = new User();
        user1.setUserName("张三");
        userService.addRequiresNew(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addRequiresNew(user2);
        throw new RuntimeException();
    }

    /**
     * 外围方法没有开启事务。
     * 张三”插入，“李四”未插入 外围方法没有开启事务，插入“张三”方法和插入“李四”方法分别开启自己的事务，插入“李四”方法抛出异常回滚，其他事务不受影响。
     */
    public void notransaction_requiresNew_requiresNew_exception(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addRequiresNew(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addRequiresNewException(user2);
    }


    /**
     * 外围方法开启事务。
     * 张三”未插入，“李四”插入，“王五”插入。	外围方法开启事务，插入“张三”方法和外围方法一个事务，插入“李四”方法、插入“王五”方法分别在独立的新建事务中，外围方法抛出异常只回滚和外围方法同一事务的方法，故插入“张三”的方法回滚。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_exception_required_requiresNew_requiresNew(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addRequired(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addRequiresNew(user2);

        User user3=new User();
        user3.setUserName("王五");
        userService2.addRequiresNew(user3);
        throw new RuntimeException();
    }

    /**
     * 外围方法开启事务。
     * “张三”未插入，“李四”插入，“王五”未插入。	外围方法开启事务，插入“张三”方法和外围方法一个事务，插入“李四”方法、插入“王五”方法分别在独立的新建事务中。插入“王五”方法抛出异常，首先插入 “王五”方法的事务被回滚，异常继续抛出被外围方法感知，外围方法事务亦被回滚，故插入“张三”方法也被回滚。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_required_requiresNew_requiresNew_exception(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addRequired(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addRequiresNew(user2);

        User user3=new User();
        user3.setUserName("王五");
        userService2.addRequiresNewException(user3);
    }

    /**
     * 外围方法开启事务。
     * “张三”插入，“李四”插入，“王五”未插入。	外围方法开启事务，插入“张三”方法和外围方法一个事务，插入“李四”方法、插入“王五”方法分别在独立的新建事务中。插入“王五”方法抛出异常，首先插入“王五”方法的事务被回滚，异常被catch不会被外围方法感知，外围方法事务不回滚，故插入“张三”方法插入成功。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_required_requiresNew_requiresNew_exception_try(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addRequired(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addRequiresNew(user2);

        User user3=new User();
        user3.setUserName("王五");
        try {
            userService2.addRequiresNewException(user3);
        } catch (Exception e) {
            System.out.println("回滚");
        }
    }

    /**
     * 此场景外围方法没有开启事务
     * “张三”、“李四”均插入。	外围方法未开启事务，插入“张三”、“李四”方法在自己的事务中独立运行，外围方法异常不影响内部插入“张三”、“李四”方法独立的事务。
     */
    public void notransaction_exception_nested_nested(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addNested(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addNested(user2);
        throw new RuntimeException();
    }

    /**
     * 此场景外围方法没有开启事务
     * 张三”插入，“李四”未插入。	外围方法没有事务，插入“张三”、“李四”方法都在自己的事务中独立运行,所以插入“李四”方法抛出异常只会回滚插入“李四”方法，插入“张三”方法不受影响
     */
    public void notransaction_nested_nested_exception(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addNested(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addNestedException(user2);
    }

    /**
     * 此场景外围方法没有开启事务
     * “张三”、“李四”均未插入。	外围方法开启事务，内部事务为外围事务的子事务，外围方法回滚，内部方法也要回滚。
     */
    @Transactional
    public void transaction_exception_nested_nested(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addNested(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addNested(user2);
        throw new RuntimeException();
    }

    /**
     * 此场景外围方法没有开启事务
     * 张三”、“李四”均未插入。	外围方法开启事务，内部事务为外围事务的子事务，内部方法抛出异常回滚，且外围方法感知异常致使整体事务回滚。
     */
    @Transactional
    public void transaction_nested_nested_exception(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addNested(user1);

        User user2=new User();
        user2.setUserName("李四");
        userService2.addNestedException(user2);
    }

    /**
     * 此场景外围方法没有开启事务
     * “张三”插入、“李四”未插入。	外围方法开启事务，内部事务为外围事务的子事务，插入“张三”内部方法抛出异常，可以单独对子事务回滚。
     */
    @Transactional
    public void transaction_nested_nested_exception_try(){
        User user1=new User();
        user1.setUserName("张三");
        userService.addNested(user1);

        User user2=new User();
        user2.setUserName("李四");
        try {
            userService2.addNestedException(user2);
        } catch (Exception e) {
            System.out.println("方法回滚");
        }
    }
}
