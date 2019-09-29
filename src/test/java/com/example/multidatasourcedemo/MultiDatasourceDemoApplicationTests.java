package com.example.multidatasourcedemo;

import com.example.multidatasourcedemo.component.AsyncTask;
import com.example.multidatasourcedemo.dao.JdbcTemplateDao;
import com.example.multidatasourcedemo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MultiDatasourceDemoApplicationTests {
	@Autowired
	private StringEncryptor encryptor;

	@Autowired
	private SpringUtils springUtils;

	@Autowired
	private JdbcTemplateDao jdbcTemplateDao;

	@Autowired
	private AsyncTask asyncTask;

//	private Logger logger = Logger.getLogger(getClass());

	@Before
 	public void before() {

	}

	@Test
	public void contextLoads() {
	}

	/**
	 *  通过SqlSessionTemplate操作Mapper对象
	 */
	@Test
	public void testSelect(){
		SqlSessionTemplate barSqlSessionTemplate = (SqlSessionTemplate) springUtils.getBeanByName("barSqlSessionTemplate");
		String sql = "com.example.multidatasourcedemo.Dao.primary.UserDao.find";
		List<User>  user = barSqlSessionTemplate.selectList(sql, 1);
		System.out.println(user.toString());
	}

	/**
	 * 给HakariCP配置密码加密
 	 */
	@Test
	public void getEncPasswd() {
		String password = encryptor.encrypt("root");
		System.out.println("加密密码: " + password);
	}

	/**
	 * 功能描述: 通过jdbcTemplate 对数据库进行增删改查
	 * @auther: zhoucc
	 * @date: 2019/6/4 15:16
	 */
	@Test
	public void listData() {
		jdbcTemplateDao.listData();
	}

/**
 * 功能描述: jdbcTemplate WHERE条件查询
 *
 * @auther: zhoucc
 * @date: 2019/6/4 16:20
 */
	@Test
	public void selectById() {
		User user = jdbcTemplateDao.selectById(1);
		log.info(user.toString());
	}

	/**
	 * 功能描述: JdbcTemplate 数据单条插入并主键自增
	 *
	 * @auther: zhoucc
	 * @date: 2019/6/4 16:21
	 */
	@Test
	public void insertData() {
		User user = User.builder().id(2).userName("zhouql").build();
		int num = jdbcTemplateDao.insertData(user);
		log.info(user.toString());
	}

	/**
	 * 功能描述: JdbcTemplate 数据单条插入并主键自增，另一条方法.
	 *
	 * @auther: zhoucc
	 * @date: 2019/6/4 16:53
	 */
	@Test
	public void insertDataNew() {
		User user = User.builder().id(2).userName("zhouxc").build();
		jdbcTemplateDao.insertDataNew(user);
	}

	/**
	 * 功能描述: 更新数据
	 *
	 * @param: 更新对象
	 * @auther: zhoucc
	 * @date: 2019/6/4 17:11
	 */
	@Test
	public void updateData() {
		User user = User.builder().id(2).userName("zhouxc").build();
		jdbcTemplateDao.update(user);
	}


	/**
	 * 功能描述: 根据id删除记录
	 *
	 * @auther: zhoucc
	 * @date: 2019/6/4 17:11
	 */
	@Test
	public void deleteById() {
		jdbcTemplateDao.deleteById(7);
	}

	/**
	 * 功能描述: jdbcTemplate 批量插入
	 *
	 * @param:
	 * @auther: zhoucc
	 * @date: 2019/6/4 17:36
	 */
	@Test
	public void batchInsert() {
		jdbcTemplateDao.batchInsert();
	}


	@Test
	public void batchNewInsert() {
		List<User> list = new ArrayList<>();
		list.add(User.builder().id(12).userName("zhouyt").build());
		list.add(User.builder().id(13).userName("liuxr").build());
		list.add(User.builder().id(14).userName("zhouyd").build());
		list.add(User.builder().id(15).userName("zhouxd").build());
		jdbcTemplateDao.batchNewInsert(list);
	}

	@Test
	public void AsyncTaskTest(){
		try {
			asyncTask.doTaskOne();
			asyncTask.doTaskTwo();
			asyncTask.doTaskThree();
			Thread.sleep(15000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void AsyncTaskNewTest() throws InterruptedException {
		long start = System.currentTimeMillis();
		log.error("任务开始");
		Future<String> task1 = asyncTask.doTaskNewOne();
		Future<String> task2 = asyncTask.doTaskNewTwo();
		Future<String> task3 = asyncTask.doTaskNewThree();
		while (true) {
			if (task1.isDone() && task2.isDone() && task3.isDone()) {
				break;
			}
			Thread.sleep(1000);
		}
		long end = System.currentTimeMillis();
		System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
		log.info("已完成");
	}

}
