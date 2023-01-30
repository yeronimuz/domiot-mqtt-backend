package org.domiot.backend.dao;

import java.util.List;
import java.util.Optional;

/**
 * DAO pattern interface for all entities
 * @param <T> Entity to manage
 */
public interface Dao<T> {

  Optional<T> get(long id);

  List<T> getAll();

  T save(T t);

  T update(T t);

  void delete(T t);
}
