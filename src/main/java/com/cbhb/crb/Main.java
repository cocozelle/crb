package com.cbhb.crb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@MapperScan(value = {"com.cbhb.crb.mapper"})
public class Main
{
	public static void main(String[] args)
	{
		SpringApplication.run(Main.class, args);
	}
}
