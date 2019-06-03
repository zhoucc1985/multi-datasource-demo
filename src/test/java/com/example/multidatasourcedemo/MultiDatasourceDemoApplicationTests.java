package com.example.multidatasourcedemo;

		import com.example.multidatasourcedemo.pojo.User;
		import org.jasypt.encryption.StringEncryptor;
		import org.junit.Before;
		import org.junit.Test;
		import org.junit.runner.RunWith;
		import org.mybatis.spring.SqlSessionTemplate;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.boot.test.context.SpringBootTest;
		import org.springframework.context.ApplicationContext;
		import org.springframework.context.annotation.AnnotationConfigApplicationContext;
		import org.springframework.test.context.junit4.SpringRunner;

		import javax.sql.DataSource;
		import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiDatasourceDemoApplicationTests {
	@Autowired
	private StringEncryptor encryptor;

	@Autowired
	private SpringUtils springUtils;

	@Before
 	public void before() {

	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testSelect(){
		SqlSessionTemplate barSqlSessionTemplate = (SqlSessionTemplate) springUtils.getBeanByName("barSqlSessionTemplate");
		String sql = "com.example.multidatasourcedemo.Dao.UserDao.find";
		List<User>  user = barSqlSessionTemplate.selectList(sql, 1);
		System.out.println(user.toString());
	}

	// 给HakariCP配置密码加密
	@Test
	public void getEncPasswd() {

		String password = encryptor.encrypt("root");
		System.out.println("加密密码: " + password);
	}
}
