package com.cbhb.crb.config;

import org.springframework.context.annotation.Configuration;

import com.cbhb.crb.util.Constant;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

@Configuration
public class LogFilterConfig extends Filter<ILoggingEvent>
{	
	@Override
	public FilterReply decide(ILoggingEvent event)
	{
		FilterReply reply = FilterReply.DENY;
		
		String strLoggerName = event.getLoggerName();
		if(strLoggerName != null)
		{
			/* 只记录程序包下的日志记录 */
			if(strLoggerName.startsWith(Constant.DEF_BASE_PACKAGE))
			{
				/* 去除一些mapper的sql日志，如：IdGeneratorMapper、SecurityUserMapper等 */
				String strMessage = event.getMessage();
				if(strMessage != null && !strMessage.startsWith(
						Constant.DEF_INNER_MAPPER))
				{
					reply = FilterReply.ACCEPT;
				}	
			}
			else if(strLoggerName.startsWith(Constant.DEF_APACHE_CONTAINER_BASE) &&
					event.getLevel() == Level.ERROR)
			{
				reply = FilterReply.ACCEPT;
			}
			
			/* 根据loggerName过滤 */
			/* 去除IdGenerator(Mapper)日志 */
			if(strLoggerName.startsWith(Constant.DEF_MAPPER_ID_GENERATOR))
			{
				reply = FilterReply.DENY;
			}
			/* 去除security日志(Mapper) */
			else if(strLoggerName.startsWith(Constant.DEF_MAPPER_SECURITY))
			{
				reply = FilterReply.DENY;
			}
			
			/* 根据message内容过滤 */			
			String strMessage = event.getMessage();
			if(strMessage != null)
			{
				/* 去除一些mapper的sql日志，如：IdGeneratorMapper、SecurityUserMapper等 */
				if(strMessage.startsWith(Constant.DEF_INNER_MAPPER))
				{
					reply = FilterReply.DENY;
				}
			}			
		}

		return reply;
	}
}
