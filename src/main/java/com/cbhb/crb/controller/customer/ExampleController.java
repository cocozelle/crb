package com.cbhb.crb.controller.customer;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbhb.crb.bean.LoginUser;
import com.cbhb.crb.bean.Result;
import com.cbhb.crb.controller.BaseController;
import com.cbhb.crb.service.customer.ExampleService;
import com.cbhb.crb.util.Constant;

@RestController
public class ExampleController extends BaseController
{
	@Resource(name="exampleService")
	private ExampleService exampleService;
	
	@RequestMapping(value="/my-customer", method=RequestMethod.POST)
	public Result getMyCustomer(@RequestBody Map<String, String> mapParam)throws Exception
	{		
		Result result = new Result();
		
		try
		{
			LoginUser loginUser = getUser();
			
			if(loginUser == null)
			{
				result.setCode(Constant.ResultCode.NOT_LOGIN);
				result.setData("用户未登录，而试图访问。");
			}
			else
			{
				Map<String, Object> MapResult = exampleService.getMyCustomer(
						mapParam, loginUser);
				
				result.setCode(Constant.ResultCode.SUCCESS);
				result.setData(MapResult);
			}
		}
		catch(Exception e)
		{
			String strMessage = "POST方式/my-customer，"
					+ "获取我的客户报错：" + e.toString();
			
			logger.error(strMessage);
			throw new Exception(strMessage);
		}
		
		return result;
	}
}
