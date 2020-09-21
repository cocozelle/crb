package com.cbhb.crb.controller.customer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbhb.crb.bean.LoginUser;
import com.cbhb.crb.controller.BaseController;
import com.cbhb.crb.service.customer.ExampleService;

@RestController
public class ExampleController extends BaseController
{
	@Resource(name="exampleService")
	private ExampleService exampleService;
	
	@RequestMapping(value="/my-customer", method=RequestMethod.POST)
	public Map<String, Object> getMyCustomer(HttpSession session,
			@RequestBody Map<String, String> mapParam)throws Exception
	{		
		Map<String, Object> MapResult = new HashMap<String, Object>();
		
		try
		{
			LoginUser loginUser = getUser(session);
			
			if(loginUser == null)
			{
				logger.info("未存入用户信息而试图访问。");
				MapResult.put("error", "请先把用户信息存入Session。");
			}
			else
			{
				MapResult = exampleService.getMyCustomer(
						mapParam, loginUser);
			}
		}
		catch(Exception e)
		{
			String strMessage = "POST方式/my-customer，"
					+ "获取我的客户报错：" + e.toString();
			
			logger.error(strMessage);
			throw new Exception(strMessage);
		}
		
		return MapResult;
	}
	
	@RequestMapping(value="/session", method=RequestMethod.POST)
	public LoginUser saveSession(HttpSession session, 
			@RequestBody LoginUser loginUser) throws Exception
	{	
		try
		{
			session.setAttribute("loginUser", loginUser);
		}
		catch(Exception e)
		{
			String strMessage = "POST方式/session，"
					+ "保存用户报错：" + e.toString();
			
			logger.error(strMessage);
			throw new Exception(strMessage);
		}
		
		return loginUser;
	}
	
	@RequestMapping(value="/session", method=RequestMethod.GET)
	public String showSession(HttpSession session) throws Exception
	{
		LoginUser loginUser = null;
		
		try
		{
			loginUser = getUser(session);
		}
		catch(Exception e)
		{
			String strMessage = "GET方式/session，"
					+ "获取用户报错：" + e.toString();
			
			logger.error(strMessage);
			throw new Exception(strMessage);
		}
		
		return "Session.loginUser: " + loginUser;
	}
}
