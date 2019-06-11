package com.example.multidatasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class})
@MapperScan("com.example.multidatasourcedemo.Dao")
@Slf4j
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy=true)
@EnableScheduling
public class MultiDatasourceDemoApplication implements CommandLineRunner  {

	@Autowired
	private SpringUtils springUtils;

	@Autowired
	@Resource
	private JdbcTemplate fooJdbcTemplate;

	@Autowired
	@Resource
	private JdbcTemplate barJdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(MultiDatasourceDemoApplication.class, args);
	}

	@Bean
	@ConfigurationProperties("foo.datasource")
	public DataSourceProperties fooDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties("bar.datasource")
	public DataSourceProperties barDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource fooDataSource() {
		DataSourceProperties dataSourceProperties = fooDataSourceProperties();
		log.info("foo datasource: {}", dataSourceProperties.getUrl());
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Primary
	public DataSource barDataSource() {
		DataSourceProperties dataSourceProperties = barDataSourceProperties();
		log.info("bar datasource: {}", dataSourceProperties.getUrl());
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Resource
	public PlatformTransactionManager fooTxManager(DataSource fooDataSource) {
		return new DataSourceTransactionManager(fooDataSource);
	}

	@Bean
	public JdbcTemplate fooJdbcTemplate(){
		DataSource fooDataSource = fooDataSource();
		return new JdbcTemplate(fooDataSource);
	}

	@Bean
	@Primary
	public JdbcTemplate barJdbcTemplate(){
		DataSource barDataSource = barDataSource();
		return new JdbcTemplate(barDataSource);
	}

	/**
	 * 功能描述: springboot jdbcTemplate自带处理并返回主键
	 *
	 * @param:
	 * @auther: zhoucc
	 * @date: 2019/6/4 17:21
	 */
	@Bean
	@Autowired
	public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate) {
		return new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("users").usingGeneratedKeyColumns("id");
	}

	/**
	 * 功能描述: springboot jdbcTemplate自带批量处理
	 *
	 * @auther: zhoucc
	 * @date: 2019/6/4 17:22
	 */
	@Bean
	@Resource
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource barDataSource) {
		return new NamedParameterJdbcTemplate(barDataSource);
	}

	@Override
	public void run(String... args) throws Exception {
//		ApplicationContext applicationContext = springUtils.getApplicationContext();
		DataSource fooDataSource = (DataSource) springUtils.getBeanByName("fooDataSource");
		DataSource barDataSource = (DataSource) springUtils.getBeanByName("barDataSource");
		log.info("fooDataSource: " + fooDataSource.getConnection().toString());
		log.info("barDataSource: " + barDataSource.getConnection().toString());
		showConnection();
		showData();
	}

	private void showConnection() throws SQLException {
		DataSource fooDataSource = (DataSource) springUtils.getBeanByName("fooDataSource");
		//DataSource barDataSource = (DataSource) springUtils.getBeanByName("barDataSource");
		log.info(fooDataSource.toString());
		Connection conn = fooDataSource.getConnection();
		log.info(conn.toString());
		conn.close();

	}

	private void showData() {
		fooJdbcTemplate.queryForList("SELECT * FROM sequence_table").forEach(row -> log.info(row.toString()));
		barJdbcTemplate.queryForList("SELECT * FROM VERSION").forEach(row -> log.info(row.toString()));
	}

	@Bean(name = "fooSqlSessionFactory")
//	@Primary
	public SqlSessionFactory fooSqlSessionFactory() throws Exception {
		DataSource dataSource = fooDataSource();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "fooSqlSessionTemplate")
//	@Primary
	public SqlSessionTemplate fooSqlSessionTemplate(@Qualifier("fooSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name = "barSqlSessionFactory")
	@Primary
	public SqlSessionFactory barSqlSessionFactory() throws Exception {
		DataSource dataSource = barDataSource();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*.xml"));
		return bean.getObject();
	}


	@Bean(name = "barSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate barSqlSessionTemplate(@Qualifier("barSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	@Resource
	@Primary
	public PlatformTransactionManager barTxManager(DataSource barDataSource) {
		return new DataSourceTransactionManager(barDataSource);
	}

}
