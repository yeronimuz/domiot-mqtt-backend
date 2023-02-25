package org.domiot.backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.lankheet.domiot.entities.UserEntity;

/**
 * DAO for {@link org.lankheet.domiot.entities.UserEntity}
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
    return query.getResultList();
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
