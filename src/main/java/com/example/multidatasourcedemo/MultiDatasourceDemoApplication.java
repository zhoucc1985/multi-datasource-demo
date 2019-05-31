package com.example.multidatasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class})
@Slf4j
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
	@Resource
	public PlatformTransactionManager barTxManager(DataSource barDataSource) {
		return new DataSourceTransactionManager(barDataSource);
	}

	@Bean
	public JdbcTemplate fooJdbcTemplate(){
		DataSource fooDataSource = fooDataSource();
		return new JdbcTemplate(fooDataSource);
	}

	@Bean
	public JdbcTemplate barJdbcTemplate(){
		DataSource barDataSource = barDataSource();
		return new JdbcTemplate(barDataSource);
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
}
