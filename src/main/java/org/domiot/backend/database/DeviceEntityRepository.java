package org.domiot.backend.database;

import org.lankheet.domiot.entities.DeviceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link DeviceEntity}
 */
@Repository
public interface DeviceEntityRepository extends CrudRepository<DeviceEntity, Long> {
    /**
     * Retrieves a DeviceEntity instance based on the given MAC address.
     *
     * @param macAddress the MAC address of the device to be located.
     * @return the DeviceEntity associated with the provided MAC address, or null if no such entity exists.
     */
    DeviceEntity findByMacAddress(String macAddress);
}
