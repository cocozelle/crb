package com.cbhb.crb.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoginUser implements Serializable
{
	private String userId;
	private String password;
	private String orgId;
	private String userName;
	private String roleId;
	
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getOrgId()
	{
		return orgId;
	}
	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}
	
	public String getRoleId()
	{
		return roleId;
	}
	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	public String toString()
	{
		return "LoginUser [userId=" + userId + ", password=" + password + 
				", orgId=" + orgId + ", userName=" + userName + ", roleId=" + roleId + "]";
	}
}
