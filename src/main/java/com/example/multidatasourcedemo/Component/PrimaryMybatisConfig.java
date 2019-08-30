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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 主数据源
 * @author zhoucc
 * @date 2019/8/30 11:26
 */

@Slf4j
@Configuration
@MapperScan(basePackages="com.example.multidatasourcedemo.Dao.primary",sqlSessionFactoryRef="barSqlSessionFactory")
public class PrimaryMybatisConfig {

    @Primary
    @Bean(name="barDataSourceProperties")
    @ConfigurationProperties("bar.datasource")
    public DataSourceProperties barDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name="barDataSource")
    public DataSource barDataSource(@Qualifier("barDataSourceProperties") DataSourceProperties dataSourceProperties) {
        log.info("bar datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name="barJdbcTemplate")
    public JdbcTemplate barJdbcTemplate(@Qualifier("barDataSource") DataSource barDataSource){
        return new JdbcTemplate(barDataSource);
    }

    @Primary
    @Bean(name = "barSqlSessionFactory")
    public SqlSessionFactory barSqlSessionFactory(@Qualifier("barDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/primary/*.xml"));
        return bean.getObject();
    }

    @Primary
    @Bean(name = "barSqlSessionTemplate")
    public SqlSessionTemplate barSqlSessionTemplate(@Qualifier("barSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean(name="barTxManager")
    public PlatformTransactionManager barTxManager(@Qualifier("barDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
