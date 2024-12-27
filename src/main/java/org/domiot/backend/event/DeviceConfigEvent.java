package org.domiot.backend.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;

import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeviceConfigEvent extends ApplicationEvent {
    private final DeviceDto device;

    public DeviceConfigEvent(Object source, DeviceDto device) {
        super(source);
        this.device = device;
    }

}
