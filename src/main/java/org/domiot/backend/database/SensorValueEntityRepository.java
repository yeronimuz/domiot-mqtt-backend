package org.domiot.backend.database;

import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorValueEntityRepository extends CrudRepository<SensorValueEntity,Long> {
}
