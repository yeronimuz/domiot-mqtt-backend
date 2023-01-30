package org.domiot.backend.entities;

/**
 * The types of measurements in P1 datagram
 */
public enum MeasurementType {
  // From SmartMeter
  PRODUCED_POWER_T1(1, "Produced power T1", "kW"),
  PRODUCED_POWER_T2(2, "Produced power T2", "kW"),
  CONSUMED_POWER_T1(3, "Consumed power T1", "kW"),
  CONSUMED_POWER_T2(4, "Consumed power T2", "kW"),
  ACTUAL_CONSUMED_POWER(5, "Actual consumed power", "kW"),
  ACTUAL_PRODUCED_POWER(6, "Actual produced power", "kW"),
  CONSUMED_GAS(7, "Consumed gas", "m3"),

  // From iot network
  TEMPERATURE(8, "Temperature", "Deg.C"),
  HUMIDITY(9, "Humidity", "%"),
  CONSUMED_WATER(10, "water consumed", "m3"),
  UNKNOWN(99, "unknown", "none"),
  ;

  int id;

  String name;

  String unit;

  MeasurementType(int id, String name, String unit) {
    this.id = id;
    this.name = name;
    this.unit = unit;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getUnit() {
    return unit;
  }

  public static MeasurementType getType(Integer type) {
    MeasurementType returnType = null;
    for (MeasurementType measType : MeasurementType.values()) {
      if (type == measType.getId()) {
        returnType = measType;
        break;
      }
    }
    return returnType;
  }
}
