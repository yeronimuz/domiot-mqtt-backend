package org.domiot.backend.mapper;

import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.entities.DeviceEntity;
import org.mapstruct.Mapper;

/**
 * DeviceDtoMapper interface for mapping between DeviceEntity and DeviceDto objects.
 *
 * Utilizes MapStruct for the mapping process and includes the SensorDtoMapper for nested mappings.
 */
@Mapper(componentModel = "spring", uses = {SensorDtoMapper.class, DomiotParameterDtoMapper.class})
public interface DeviceDtoMapper {

    DeviceDto map(DeviceEntity device);

    DeviceEntity map(DeviceDto deviceDto);
}
