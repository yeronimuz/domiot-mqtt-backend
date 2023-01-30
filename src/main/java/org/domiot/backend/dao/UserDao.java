package org.domiot.backend.dao;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.domiot.backend.entities.UserEntity;
import org.lankheet.iot.model.User;

/**
 * DAO for {@link org.domiot.backend.entities.UserEntity}
 */
@Transactional
public class UserDao implements Dao<UserEntity> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<UserEntity> get(long id) {
    return Optional.ofNullable(entityManager.find(UserEntity.class, id));
  }

  @Override
  public List<UserEntity> getAll() {
    Query query = entityManager.createQuery("SELECT e FROM UserEntity e");
    List resultList = query.getResultList();
    return resultList;
  }

  @Override
  public UserEntity save(UserEntity userEntity) {
    entityManager.persist(userEntity);
    return userEntity;
  }

  @Override
  public UserEntity update(UserEntity userEntity) {
    entityManager.refresh(userEntity);
    return userEntity;
  }

  @Override
  public void delete(UserEntity userEntity) {
    entityManager.remove(userEntity);
  }
}
