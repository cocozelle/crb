package com.cbhb.crb.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Converter
{
	private static ObjectMapper om = new ObjectMapper();
	
	public static String toJson(Object object) throws Exception
	{
		String strJson = null;
		
		try
		{
			om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
				
			strJson = om.writeValueAsString(object);
		}
		catch(Exception e)
		{
			throw new Exception(object + "，Json化字符串出错：" + e.toString());
		}
		
		return strJson;
	}
	
	public String dealString(String strOriginal) 
	{
		return strOriginal;
	}
}
