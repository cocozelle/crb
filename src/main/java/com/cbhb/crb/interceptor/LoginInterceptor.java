package com.cbhb.crb.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.cbhb.crb.bean.LoginUser;
import com.cbhb.crb.bean.Result;
import com.cbhb.crb.util.Converter;

public class LoginInterceptor implements HandlerInterceptor
{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		HttpSession session = request.getSession();
		LoginUser loginUser = (LoginUser)session.getAttribute("loginUser");
		
		if(loginUser == null)
		{
	        //UTF-8编码
			response.setCharacterEncoding("UTF-8");
	        response.setContentType("application/json;charset=utf-8");
	        
			Result result = new Result();
			result.setCode("-1");
			result.setData("还未登录，请先登录");
			response.getWriter().print(Converter.toJson(result));
			
			return false;
		}
		
		return true;
	}
}
