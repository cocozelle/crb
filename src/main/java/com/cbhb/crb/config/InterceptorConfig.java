package com.cbhb.crb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cbhb.crb.interceptor.LoginInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer
{
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		InterceptorRegistration ir = registry.addInterceptor(new LoginInterceptor());
		ir.addPathPatterns("/**");
		ir.excludePathPatterns(
                "/login",
                "/logout"
                ); 
	}
}
