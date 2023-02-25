package org.domiot.backend.service;

import java.math.BigDecimal;
import java.util.List;
import org.lankheet.domiot.model.Device;

/**
 * Service for handling devices
 */
public class DeviceService {

  public List<Device> addDevices(Long siteId, List<Device> deviceList) {
    return null;
  }

  public List<Device> getDevice(Long siteId, Long deviceId) {
    return null;
  }

  public List<Device> getSiteDevices(BigDecimal siteId) {
    // FIXME: Why is this a BigDecimal?
    return null;
  }
}
