package com.cbhb.crb.service.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cbhb.crb.bean.LoginUser;
import com.cbhb.crb.mapper.user.UserMapper;
import com.cbhb.crb.service.BaseService;

@Service("userService")
public class UserService extends BaseService
{
	@Resource(name="userMapper")
	private UserMapper userMapper;
	
	public LoginUser authenticateUser(LoginUser loginUser) throws Exception
	{
		loginUser = userMapper.getUserByIdAndPassword(loginUser);
		return loginUser;
	}
}
