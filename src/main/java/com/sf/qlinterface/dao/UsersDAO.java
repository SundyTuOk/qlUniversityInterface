package com.sf.qlinterface.dao;

import com.sf.db.domain.User;

public interface UsersDAO {
	public User getUserById(Integer id);
	public User getUserByUsername(String username);
	public String getPasswordByUsername(String username);
}
