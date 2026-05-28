package org.domiot.backend.service;

import lombok.extern.slf4j.Slf4j;

import org.domiot.backend.database.DeviceEntityRepository;
import org.domiot.backend.mapper.DeviceDtoMapper;
import org.domiot.dto.DeviceDto;
import org.domiot.entities.DeviceEntity;
import org.springframework.stereotype.Service;

/**
 * Service layer for Device objects
 */
@Slf4j
@Service
public class DeviceService {
    private final DeviceEntityRepository repository;
    private final DeviceDtoMapper deviceMapper;

    public DeviceService(DeviceEntityRepository repository, DeviceDtoMapper deviceMapper) {
        this.repository = repository;
        this.deviceMapper = deviceMapper;
    }

    /**
     * Save or update a device in the database and return the updated object.
     *
     * @param deviceDto The device to store
     * @return The updated device
     */
    public DeviceDto saveDevice(DeviceDto deviceDto) {
        DeviceEntity deviceEntityStored = repository.findByMacAddress(deviceDto.getMacAddress());
        if (deviceEntityStored == null) {
            log.debug("Creating new device");
            DeviceEntity newDeviceEntity = deviceMapper.map(deviceDto);
            newDeviceEntity.getSensors().forEach(sensorDto -> {
                sensorDto.setId(null);
                sensorDto.setDeviceEntity(newDeviceEntity);
            });
            deviceEntityStored = repository.save(newDeviceEntity);
        } else {
            log.debug("Existing device");
        }

        return deviceMapper.map(deviceEntityStored);
    }
}
