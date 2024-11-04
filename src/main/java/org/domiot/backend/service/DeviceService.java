package org.domiot.backend.service;

import org.domiot.backend.database.DeviceEntityRepository;
import org.domiot.backend.mapper.DeviceDtoMapper;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.springframework.stereotype.Service;

/**
 * Service layer for Device objects
 */
@Service
public class DeviceService {
    private final DeviceEntityRepository repository;
    private final DeviceDtoMapper deviceMapper;

    public DeviceService(DeviceEntityRepository repository, DeviceDtoMapper deviceMapper) {
        this.repository = repository;
        this.deviceMapper = deviceMapper;
    }

    /**
     * Save a device in the database and return the updated object.
     *
     * @param deviceDto The device to store
     * @return The updated device
     */
    public DeviceDto saveDevice(DeviceDto deviceDto) {
        return deviceMapper.map(repository.save(deviceMapper.map(deviceDto)));
    }
}
