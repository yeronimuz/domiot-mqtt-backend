package org.domiot.backend.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import org.domiot.backend.service.DeviceService;
import org.lankheet.domiot.api.DeviceApi;
import org.lankheet.domiot.model.Device;
import org.lankheet.domiot.model.Site;

/**
 * REST resource for managing Domiot {@link Device}s
 */

public class DeviceResource implements DeviceApi {

  @Inject
  private DeviceService deviceService;

  @Override
  public List<Device> addDevices(Long siteId, @Valid List<Device> deviceList) {
    return deviceService.addDevices(siteId, deviceList);
  }

  @Override
  public List<Device> getDevice(Long siteId, Long deviceId) {
    return deviceService.getDevice(siteId, deviceId);
  }

  @Override
  public List<Device> getSiteDevices(BigDecimal siteId) {
    return deviceService.getSiteDevices(siteId);
  }

  @Override
  public List<Device> updateDevice(Long aLong, Object o, @Valid Device device) {
    return null;
  }
}
