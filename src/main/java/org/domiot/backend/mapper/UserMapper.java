package org.domiot.backend.mapper;

import org.domiot.backend.entities.UserEntity;
import org.lankheet.domiot.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  User mapUserEntiryToUser(UserEntity userEntity);

  UserEntity mapUserToUserEntity(User user);
}
