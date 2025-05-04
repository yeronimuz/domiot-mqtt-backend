package org.domiot.backend.service;

import org.domiot.backend.database.DeviceEntityRepository;
import org.domiot.backend.mapper.DeviceDtoMapper;
import org.domiot.backend.mapper.DomiotParameterDtoMapper;
import org.domiot.backend.mapper.SensorDtoMapper;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.entities.DeviceEntity;
import org.springframework.stereotype.Service;

/**
 * Service layer for Device objects
 */
@Service
public class DeviceService {
    private final DeviceEntityRepository repository;
    private final DeviceDtoMapper deviceMapper;
    private final SensorDtoMapper sensorMapper;
    private final DomiotParameterDtoMapper domiotParameterDtoMapper;

    public DeviceService(DeviceEntityRepository repository, DeviceDtoMapper deviceMapper, SensorDtoMapper sensorMapper, DomiotParameterDtoMapper domiotParameterDtoMapper) {
        this.repository = repository;
        this.deviceMapper = deviceMapper;
        this.sensorMapper = sensorMapper;
        this.domiotParameterDtoMapper = domiotParameterDtoMapper;
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
            DeviceEntity newDeviceEntity = deviceMapper.map(deviceDto);
            newDeviceEntity.getSensors().forEach(sensorDto -> {
                sensorDto.setId(null);
                sensorDto.setDeviceEntity(newDeviceEntity);
            });
            deviceEntityStored = repository.save(newDeviceEntity);
        }

        return deviceMapper.map(deviceEntityStored);
    }
}
