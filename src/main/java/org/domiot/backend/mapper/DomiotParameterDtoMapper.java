package org.domiot.backend.mapper;

import org.lankheet.domiot.domotics.dto.DomiotParameterDto;
import org.lankheet.domiot.entities.DomiotParameterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between DomiotParameterDto and DomiotParameterEntity objects.
 * Utilizes MapStruct for automatic mapping between data transfer objects and entities.
 */
@Mapper(componentModel = "spring")
public interface DomiotParameterDtoMapper {

    /**
     * Maps a DomiotParameterEntity to a DomiotParameterDto.
     *
     * @param domiotParameterEntity the entity to be mapped to a DTO
     * @return the mapped DomiotParameterDto
     */
    DomiotParameterDto map(DomiotParameterEntity domiotParameterEntity);

    /**
     * Maps a DomiotParameterDto to a DomiotParameterEntity.
     *
     * @param domiotParameterDto the DTO to be mapped to an entity
     * @return the mapped DomiotParameterEntity
     */
    @Mapping(target = "id", ignore = true)
    DomiotParameterEntity map(DomiotParameterDto domiotParameterDto);
}
