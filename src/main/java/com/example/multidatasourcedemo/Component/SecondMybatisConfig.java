package com.example.multidatasourcedemo.Component;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 从数据源
 * @author zhoucc
 * @date 2019/8/30 11:28
 */

@Configuration
@Slf4j
@MapperScan(basePackages="com.example.multidatasourcedemo.Dao.second",sqlSessionFactoryRef="fooSqlSessionFactory")
public class SecondMybatisConfig {

    @Bean(name="fooDataSourceProperties")
    @ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name="fooDataSource")
    public DataSource fooDataSource(@Qualifier("fooDataSourceProperties") DataSourceProperties dataSourceProperties) {
        log.info("foo datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
	public JdbcTemplate fooJdbcTemplate(@Qualifier("fooDataSource") DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}

    @Bean(name = "fooSqlSessionFactory")
    public SqlSessionFactory fooSqlSessionFactory(@Qualifier("fooDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/second/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "fooSqlSessionTemplate")
    public SqlSessionTemplate fooSqlSessionTemplate(@Qualifier("fooSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name="fooTxManager")
    public PlatformTransactionManager fooTxManager(@Qualifier("fooDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
