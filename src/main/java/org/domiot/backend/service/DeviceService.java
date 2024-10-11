package org.domiot.backend.service;

import org.domiot.backend.database.DeviceEntityRepository;
import org.lankheet.domiot.mapper.DeviceMapper;
import org.lankheet.domiot.model.Device;
import org.springframework.stereotype.Service;

/**
 * Service layer for Device objects
 */
@Service
public class DeviceService {
    private DeviceEntityRepository repository;
    private DeviceMapper deviceMapper;

    /**
     * Save a device in the database and return the updated object.
     *
     * @param device The device to store
     * @return The updated device
     */
    public Device saveDevice(Device device) {
        return deviceMapper.map(repository.save(deviceMapper.map(device)));
    }
}
