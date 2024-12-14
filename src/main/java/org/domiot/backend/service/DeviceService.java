package org.domiot.backend.service;

import org.domiot.backend.database.DeviceEntityRepository;
import org.domiot.backend.mapper.DeviceDtoMapper;
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

    public DeviceService(DeviceEntityRepository repository, DeviceDtoMapper deviceMapper, SensorDtoMapper sensorMapper) {
        this.repository = repository;
        this.deviceMapper = deviceMapper;
        this.sensorMapper = sensorMapper;
    }

    /**
     * Save or update a device in the database and return the updated object.
     *
     * @param deviceDto The device to store
     * @return The updated device
     */
    public DeviceDto saveDevice(DeviceDto deviceDto) {
        // TODO: Find Device and update and save
        DeviceEntity deviceEntityStored = repository.findByMacAddress(deviceDto.getMacAddress());
        if (deviceEntityStored != null) {
            updateDevice(deviceDto, deviceEntityStored);
        }
        return deviceMapper.map(repository.save(deviceMapper.map(deviceDto)));
    }

    private void updateDevice(DeviceDto deviceDto, DeviceEntity deviceEntityStored) {
        deviceEntityStored.setMacAddress(deviceDto.getMacAddress());
        deviceEntityStored.setFirmwareVersion(deviceDto.getFirmwareVersion());
        deviceEntityStored.setHardwareVersion(deviceDto.getHardwareVersion());
        deviceEntityStored.setSensors(sensorMapper.map(deviceDto.getSensors()));
    }
}
