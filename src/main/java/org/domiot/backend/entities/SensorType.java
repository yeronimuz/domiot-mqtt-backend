package org.domiot.backend.entities;

import java.util.stream.Stream;

/**
 * Sensor types.
 */
public enum SensorType {
  TEMPERATURE(1, "temperature"),
  HUMIDITY(2, "humidity"),
  POWER_METER(3, "power_meter"),
  GAS_METER(4, "gas_meter"),
  GAS_SENSOR(5, "gas_sensor");

  private int id;

  private String description;

  private SensorType(int id, String name) {
    this.id = id;
    this.description = name;
  }

  public Integer getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  /**
   * Get type by Id.
   *
   * @param type The type.id to lookup.
   * @return The SensorType that matches the type.id or null if not found
   */
  public static SensorType getType(int type) {
    return Stream.of(SensorType.values())
        .filter(value -> value.id == type)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
