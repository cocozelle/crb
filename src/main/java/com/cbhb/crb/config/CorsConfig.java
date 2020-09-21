package com.cbhb.crb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
@Configuration
public class CorsConfig
{	
	@Value("${config.cors.allowed-origin}")
	private String[] arrAllowedOrigin;
	
	@Bean
	public CorsFilter corsFilter() 
	{
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration corsConfiguration = new CorsConfiguration();	   
	    
	    for(String strAllowedOrigin: arrAllowedOrigin)
	    {
	    	corsConfiguration.addAllowedOrigin(strAllowedOrigin);
	    }

	    corsConfiguration.setAllowCredentials(true);
	    corsConfiguration.setMaxAge(3600L);
	    corsConfiguration.addAllowedHeader("*");
	    corsConfiguration.addAllowedMethod("*");	    
	    
	    source.registerCorsConfiguration("/**", corsConfiguration);
	    return new CorsFilter(source);
	  }
}
