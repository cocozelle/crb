package com.cbhb.crb.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoginUser implements Serializable
{
	private String userId;
	private String orgId;
	
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public String getOrgId()
	{
		return orgId;
	}
	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}
	
	@Override
	public String toString()
	{
		return "LoginUser [userId=" + userId + ", orgId=" + orgId + "]";
	}
}
