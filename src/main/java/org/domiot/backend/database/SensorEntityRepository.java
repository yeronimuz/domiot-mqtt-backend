package org.domiot.backend.database;

import org.domiot.entities.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link SensorEntity}
 */
@Repository
public interface SensorEntityRepository extends JpaRepository<SensorEntity, Long> {
}
