package com.example.multidatasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class})
@Slf4j
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy=true)
@EnableScheduling
@EnableAsync
public class MultiDatasourceDemoApplication implements CommandLineRunner  {

	@Autowired
	private SpringUtils springUtils;

	@Resource
	private JdbcTemplate barJdbcTemplate;

	@Resource
	private JdbcTemplate fooJdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(MultiDatasourceDemoApplication.class, args);
	}

	/**
	 * 功能描述: springboot jdbcTemplate自带处理并返回主键
	 *
	 * @param:
	 * @auther: zhoucc
	 * @date: 2019/6/4 17:21
	 */
	@Bean
	public SimpleJdbcInsert simpleJdbcInsert(@Qualifier("barJdbcTemplate") JdbcTemplate jdbcTemplate) {
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
	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource barDataSource) {
		return new NamedParameterJdbcTemplate(barDataSource);
	}

	@Override
	public void run(String... args) throws Exception {
		DataSource fooDataSource = (DataSource) springUtils.getBeanByName("fooDataSource");
		DataSource barDataSource = (DataSource) springUtils.getBeanByName("barDataSource");
		log.info("fooDataSource: " + fooDataSource.getConnection().toString());
		log.info("barDataSource: " + barDataSource.getConnection().toString());
		showConnection();
		showData();
	}

	private void showConnection() throws SQLException {
		DataSource fooDataSource = (DataSource) springUtils.getBeanByName("fooDataSource");
		DataSource barDataSource = (DataSource) springUtils.getBeanByName("barDataSource");
		log.info("barDataSource: {}", barDataSource.toString());
		Connection conn = barDataSource.getConnection();
		log.info(conn.toString());
		conn.close();

	}

	private void showData() {
		fooJdbcTemplate.queryForList("SELECT * FROM users").forEach(row -> log.info(row.toString()));
		barJdbcTemplate.queryForList("SELECT * FROM users").forEach(row -> log.info(row.toString()));
	}

}
