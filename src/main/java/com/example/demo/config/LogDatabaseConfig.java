package com.example.demo.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = "com.example.demo.repository.log", sqlSessionFactoryRef = "logSqlSessionFactory")
public class LogDatabaseConfig {

	@Bean(name = "logDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.log")
	public DataSource dataSourceLog() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "logSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("logDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "logTransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("logDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}