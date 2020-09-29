package com.cbhb.crb.mapper.user;

import org.springframework.stereotype.Component;

import com.cbhb.crb.bean.LoginUser;

@Component("userMapper")
public interface UserMapper
{
	public LoginUser getUserByIdAndPassword(LoginUser loginUser);
}
