package com.cbhb.crb.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cbhb.crb.bean.LoginUser;

public class BaseController
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected LoginUser getUser()
	{
		LoginUser loginUser = null;
		
		try
		{
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();			
			loginUser = (LoginUser)request.getSession().getAttribute("loginUser");
		}
		catch(Exception e)
		{
			logger.error("获取当前用户出错：" + e.toString());
			loginUser = new LoginUser();
		}
		
		return loginUser;
	}
}
