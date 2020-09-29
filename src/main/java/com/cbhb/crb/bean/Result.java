package com.cbhb.crb.bean;

import com.cbhb.crb.util.Converter;

public class Result
{
	private String code;
	
	private Object data;

	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}

	public Object getData()
	{
		return data;
	}
	public void setData(Object data)
	{
		this.data = data;
	}
	
	@Override
	public String toString()
	{
		String strJsonData = "";
		
		try
		{
			strJsonData = Converter.toJson(data);
		} 
		catch (Exception e)
		{
			strJsonData = data.toString();
		}
		
		return "Result [code=" + code + ", data=" + strJsonData + "]";
	}	
}
