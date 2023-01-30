package org.domiot.backend.service;

import jakarta.inject.Inject;
import org.domiot.backend.dao.UserDao;
import org.domiot.backend.mapper.UserMapper;
import org.lankheet.domiot.model.User;


public class UserService {

  @Inject
  private UserDao userDao;
  @Inject
  private UserMapper userMapper;

  public User addUser(User user) {
    return userMapper.mapUserEntiryToUser(userDao.save(userMapper.mapUserToUserEntity(user)));
  }
}
