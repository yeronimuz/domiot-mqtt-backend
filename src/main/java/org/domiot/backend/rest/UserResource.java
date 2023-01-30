package org.domiot.backend.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;
import org.domiot.backend.service.UserService;
import org.lankheet.domiot.api.UserApi;
import org.lankheet.domiot.model.User;

public class UserResource implements UserApi {

  @Inject
  private UserService userService;

  @Override
  public User addUser(@Valid User user) {
    return userService.addUser(user);
  }


  @Override
  public User getUser(Long aLong) {
    return null;
  }


  @Override
  public List<User> updateUser(Long aLong, @Valid User user) {
    return null;
  }
}
