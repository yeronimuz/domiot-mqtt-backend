package org.domiot.backend.dao;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;
import org.domiot.backend.entities.SiteEntity;
import org.lankheet.iot.model.Site;

public class SiteDao implements Dao<SiteEntity> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<SiteEntity> get(long id) {
    return Optional.ofNullable(entityManager.find(SiteEntity.class, id));
  }

  @Override
  public List getAll() {
    Query query = entityManager.createQuery("SELECT e FROM SiteEntity e");
    return query.getResultList();
  }

  @Override
  public SiteEntity save(SiteEntity siteEntity) {
    entityManager.persist(siteEntity);
    return siteEntity;
  }

  @Override
  public SiteEntity update(SiteEntity siteEntity) {
    entityManager.refresh(siteEntity);
    return siteEntity;
  }

  @Override
  public void delete(SiteEntity siteEntity) {
    entityManager.remove(siteEntity);
  }
}