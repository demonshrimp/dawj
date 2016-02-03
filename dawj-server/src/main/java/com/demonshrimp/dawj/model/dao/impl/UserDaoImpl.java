package com.demonshrimp.dawj.model.dao.impl;

import org.springframework.stereotype.Repository;

import com.demonshrimp.dawj.model.dao.UserDao;
import com.demonshrimp.dawj.model.entity.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User, String> implements UserDao {

}