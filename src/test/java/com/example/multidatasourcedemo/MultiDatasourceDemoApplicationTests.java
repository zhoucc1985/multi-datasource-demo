package com.example.multidatasourcedemo;

		import com.example.multidatasourcedemo.pojo.User;
		import org.junit.Before;
		import org.junit.Test;
		import org.junit.runner.RunWith;
		import org.mybatis.spring.SqlSessionTemplate;
		import org.springframework.boot.test.context.SpringBootTest;
		import org.springframework.context.ApplicationContext;
		import org.springframework.context.annotation.AnnotationConfigApplicationContext;
		import org.springframework.test.context.junit4.SpringRunner;

		import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiDatasourceDemoApplicationTests {

	private SqlSessionTemplate template = null;
	private ApplicationContext context = null;

	@Before
 	public void before() {
		context = new AnnotationConfigApplicationContext("com.example.multidatasourcedemo");
		template = context.getBean(SqlSessionTemplate.class);
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testSelect(){
		String sql = "com.example.multidatasourcedemo.Dao.find";
		User user = (User) template.selectList(sql, 1);
		System.out.println(user.toString());
	}
}
