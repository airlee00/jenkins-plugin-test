package org.jvnet.hudson.test.mybatis.mapper;

import java.util.List;

import org.jvnet.hudson.test.mybatis.User;

public interface UserMapper {

	public void insertUser(User user);

	public User getUserById(Integer userId);

	public List<User> getAllUsers();

	public void updateUser(User user);

	public void deleteUser(Integer userId);

}
