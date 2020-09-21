package com.cbhb.crb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cbhb.crb.bean.LoginUser;
import com.cbhb.crb.util.Constant;
import com.cbhb.crb.util.Converter;

public class BaseService
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "batchSqlSession")
	protected SqlSessionTemplate batchSqlSession;
	
	@Resource(name="redisTemplate")
	protected RedisTemplate<String, Object> redisTemplate;
	
	protected LoginUser getUser(HttpSession session)
	{
		LoginUser loginUser = (LoginUser)session.getAttribute("loginUser");
		
		return loginUser;
	}
	
	protected List<Map<String, Object>> translateByRedis(List<Map<String, Object>> listObject, 
			List<Map<String, Object>> listParam) throws Exception
	{
		logStart(this.getClass().getName(), 
				Thread.currentThread().getStackTrace()[1].getMethodName(), 
				new Object[] {listObject, listParam});
		
		/* redis访问虽然快，但毕竟需要通过网络，
		   *   用一个局部变量map来缓存此次调用中redis返回的结果。
		 */
		Map<String, String> mapRedisCache = new HashMap<>();
		
		for(Map<String, Object> mapObject : listObject)
		{
			for(Map<String, Object> mapParam : listParam)
			{
				/* 存在redis中hash结构的key值 */
				String strRedisKey = (String)mapParam.get("key");
				
				/* 指明在listObject里，哪个字段的内容为redis中hash结构的field值 */
				String strFieldKey = (String)mapParam.get("fieldKey");
				
				/* 通过redis翻译后，返回给前台的name */
				String strResultName = (String)mapParam.get("resultName");
				
				/* 可能为空，对redis翻译后的字符串进行处理的类 */
				Object converter = mapParam.get("converter");
				
				/* 取出redis中hash结构的field值 */
				String strRedisField = (String)mapObject.get(strFieldKey);
				
				/* 先从map里取，要是没有，再从redis里取，并放入map */
				String strValue = mapRedisCache.get(strRedisKey + strRedisField);								
				if(strValue == null || strValue.trim().equals(""))
				{
					strValue = (String)redisTemplate.opsForHash().get(
							strRedisKey, strRedisField);
					
					/* 如果redis里没有对应内容，则不翻译 */
					if(strValue == null || strValue.trim().equals(""))
					{
						strValue = strRedisField;
					}
					if(converter != null && converter instanceof Converter)
					{
						strValue = ((Converter)converter).dealString(strValue);
					}
					
					mapRedisCache.put(strRedisKey + strRedisField, strValue);
				}
				
				/* 加入翻译后的内容 */
				mapObject.put(strResultName, strValue);
			}
		}
		
		logEnd(this.getClass().getName(), 
				Thread.currentThread().getStackTrace()[1].getMethodName(), 
				new Object[] {listObject, listParam}, listObject);
		
		return listObject;
	}

	@SuppressWarnings("unused")
	private void logStart(String strClass, String strMethod)
	{
		logStart(strClass, strMethod, null);
	}
	
	private void logStart(String strClass, 
			String strMethod, Object[] arrArgs) 
	{
		String strArg = joinArg(arrArgs);
		
		String strMessage = strClass + "." + 
				strMethod + "(" + strArg + ")开始执行。";

		logger.debug(strMessage);
	}
	
	@SuppressWarnings("unused")
	private void logEnd(String strClass, 
			String strMethod, Object[] arrArgs)
	{
		logEnd(strClass, strMethod, arrArgs, null);
	}
	
	@SuppressWarnings("unused")
	private void logEnd(String strClass, 
			String strMethod, Object returnValue)
	{
		logEnd(strClass, strMethod, null, returnValue);
	}
	
	@SuppressWarnings("unused")
	private void logEnd(String strClass, String strMethod)
	{
		logEnd(strClass, strMethod, null, null);
	}
	
	private void logEnd(String strClass, String strMethod, 
			Object[] arrArgs, Object returnValue)
	{
		String strArg = joinArg(arrArgs);
		
		String strReturnValue = aa(returnValue);
		
		String strMessage = strClass + "." + strMethod + 
				"(" + strArg + ")执行完毕" + strReturnValue + "。";
		
		logger.debug(strMessage);
	}
	
	private String joinArg(Object[] arrArgs)
	{
		StringBuilder sbArgs = new StringBuilder();
		
		if(arrArgs != null)
		{
			try
			{
				for(int i = 0; i < arrArgs.length; i++)
				{
					if(!"{}".equals(arrArgs[i]))
					{
						if(arrArgs[i] instanceof MultipartFile)
						{
							sbArgs.append("file, ");
						}
						else if(arrArgs[i] instanceof HttpServletResponse)
						{
							sbArgs.append("response, ");
						}
						else if(arrArgs[i] instanceof HttpServletRequest)
						{
							sbArgs.append("request, ");
						}
						else if(arrArgs[i] instanceof HttpSession)
						{
							sbArgs.append("session, ");
						}
						else
						{
							sbArgs.append(Converter.toJson(arrArgs[i])).append(", ");					
						}
					}
				}
				
				sbArgs.setLength(sbArgs.length() - 2);			
			
				if(sbArgs.length() > Constant.DEF_ARGS_LENGTH_IN_LOG)
				{
					sbArgs.setLength(Constant.DEF_ARGS_LENGTH_IN_LOG);
					sbArgs.append("....");
				}
			}
			catch(Exception e)
			{
				logger.error("拼接参数出错：" + e.toString());
			}
		}		
		
		return sbArgs.toString();
	}
	
	private String aa(Object returnValue)
	{
		StringBuilder sbReturnValue = new StringBuilder();
		
		if(returnValue != null)
		{
			try
			{
				sbReturnValue.append("，返回值：" + Converter.toJson(returnValue));
					
				if(sbReturnValue.length() > Constant.DEF_RESULT_LENGTH_IN_LOG)
				{
					sbReturnValue.setLength(Constant.DEF_RESULT_LENGTH_IN_LOG);
					sbReturnValue.append("....");
				}
			}
			catch(Exception e)
			{
				logger.error("转换返回值出错：" + e.toString());;
			}
		}
			
		return sbReturnValue.toString();
	}
}
