package com.cbhb.crb.config;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.cbhb.crb.bean.LoginUser;
import com.cbhb.crb.util.Constant;
import com.cbhb.crb.util.Converter;

@Aspect
@Component
public class LogAspect
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Pointcut("execution(* com.cbhb.crb.controller.*.*.*(..))")
    public void pcController()
	{
		
	}
	
	@Pointcut("execution(* com.cbhb.crb.service.*.*.*(..))")
    public void pcService()
	{
		
	}
	
	@Pointcut("execution(* com.cbhb.crb.mapper.*.*.*(..))")
    public void pcMapper()
	{
		
	}
	
	@Around("pcController() || pcService() || pcMapper()")
	public Object writeLog(ProceedingJoinPoint pjp) throws Throwable
	{
		String strClass = null;
		String strMethod = null;
		StringBuilder sbArgs = null;
		Transactional annotationTransaction = null;
		String strReturnValue = null;		
		String strUserName = null;
		
		/* 获取类名 */
		try
		{
			Object target = pjp.getTarget();		
			if(target != null)
			{
				strClass = target.toString();
				
				if(strClass != null && strClass.length() > 0)
				{
					strClass = strClass.substring(0, strClass.indexOf("@"));
					strClass = strClass.substring(strClass.lastIndexOf(".") + 1);
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			strClass = "";
		}
		
		/* 获取方法名 */
		try
		{
			Signature signature = pjp.getSignature();
			if(signature != null)
			{
				strMethod = signature.getName();
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			strMethod = "";
		}
		
		/* 获取参数 */
		try
		{
			sbArgs = new StringBuilder();
			Object[] arrArgs = pjp.getArgs();	

			if(arrArgs != null)
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
			}
			
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
		
		/* 获取@Transaction注解 */
		try
		{
			Object target = pjp.getTarget();
			if(target != null)
			{
				Class<?> classTarget = target.getClass();
				if(classTarget != null)
				{
					Signature signature = pjp.getSignature();
					if(signature != null)
					{ 
						Class<?>[] arrArgs = ((MethodSignature)signature).getParameterTypes();	
						Method method = classTarget.getMethod(strMethod, arrArgs);
						
						annotationTransaction = method.getAnnotation(Transactional.class);
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			annotationTransaction = null;
		}
		
		try
		{
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();			
			LoginUser loginUser = (LoginUser)request.getSession().getAttribute("loginUser");
			
			strUserName = loginUser.getUserName() + "[" + loginUser.getUserId() + "]";
		}
		catch(Exception e)
		{
			strUserName = null;
		}
		
		String strMessage = strClass + "." + 
				strMethod + "(" + sbArgs + ")开始执行。";

        if(annotationTransaction != null)
		{
			strMessage += "该方法执行于事务中。";
		}
		
		if(strUserName != null)
		{
			strMessage += "对应用户：" + strUserName;
		}
		
		logger.debug(strMessage);
		
		strReturnValue = "";
		Object oReturnValue = pjp.proceed();

		strReturnValue = "，返回值：" +
			Converter.toJson(oReturnValue);
		
		if(strReturnValue.length() > Constant.DEF_RESULT_LENGTH_IN_LOG)
		{
			strReturnValue = strReturnValue.substring(
					0, Constant.DEF_RESULT_LENGTH_IN_LOG) + "....";
		}

		
		strMessage = strClass + "." + strMethod + 
				"(" + sbArgs + ")执行完毕" + strReturnValue + "。";
		
		if(annotationTransaction != null)
		{
			strMessage += "事务结束。";
		}
		
		if(strUserName != null)
		{
			strMessage += "对应用户：" + strUserName;
		}
		
		logger.debug(strMessage);
		
		return oReturnValue;
	}
}
