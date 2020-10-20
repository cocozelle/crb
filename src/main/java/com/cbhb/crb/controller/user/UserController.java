package com.cbhb.crb.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbhb.crb.bean.LoginUser;
import com.cbhb.crb.bean.Result;
import com.cbhb.crb.controller.BaseController;
import com.cbhb.crb.service.user.UserService;
import com.cbhb.crb.util.Constant;

@RestController
public class UserController extends BaseController
{
	@Resource(name="userService")
	private UserService userService;
	
	@PostMapping("/login")
	public Result Login(@RequestBody LoginUser loginUser, 
			HttpSession session) throws Exception
	{
		Result result = new Result();
		
		try
		{
			loginUser = userService.authenticateUser(loginUser); 
			
			if(loginUser == null)
			{
				result.setCode(Constant.ResultCode.ERROR_PASSWORD);
				result.setData("错误的用户名或密码");
			}
			else 
			{
				session.setAttribute("loginUser", loginUser);
				
				result.setCode(Constant.ResultCode.SUCCESS);
				result.setData(loginUser);
			}
		} 
		catch(Exception e)
		{
			String strMessage = "POST方式/login，"
					+ "验证用户登录报错：" + e.toString();
			
			logger.error(strMessage);
			throw new Exception(strMessage);
		}
		
		return result;
	}
	
	@PostMapping("/logout")
	public Result Logout(HttpSession session) throws Exception
	{
		Result result = new Result();
		
		try
		{
			session.invalidate();
			
			result.setCode("0");
			result.setData("注销用户成功");
		}
		catch(Exception e)
		{
			String strMessage = "POST方式/logout，"
					+ "注销用户报错：" + e.toString();
			
			logger.error(strMessage);
			throw new Exception(strMessage);
		}
		
		return result;
	}
}
