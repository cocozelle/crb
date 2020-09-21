package com.cbhb.crb.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cbhb.crb.bean.LoginUser;

public class BaseController
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected LoginUser getUser(HttpSession session)
	{
		LoginUser loginUser = (LoginUser)session.getAttribute("loginUser");
		
		return loginUser;
	}
}
