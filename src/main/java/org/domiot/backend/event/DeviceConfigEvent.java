package org.domiot.backend.event;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;

import org.domiot.dto.DeviceDto;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeviceConfigEvent extends ApplicationEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final transient DeviceDto device;

    public DeviceConfigEvent(Object source, DeviceDto device) {
        super(source);
        this.device = device;
    }

}
