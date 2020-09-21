package com.cbhb.crb.config;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchSqlConfig
{
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Bean(name = "batchSqlSession")
	public SqlSessionTemplate batchSqlSession()
	{
		return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
	}
}
