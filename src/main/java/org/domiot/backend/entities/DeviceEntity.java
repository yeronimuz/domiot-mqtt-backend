package org.domiot.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import org.lankheet.domiot.model.Actuator;
import org.lankheet.domiot.model.Sensor;

@Entity
    @Table(name = "devices")
public class DeviceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;
  private String macAddress;
  @OneToMany
  @JoinColumn(name = "ID")
  private List<Sensor> sensors = null;
  @OneToMany
  @JoinColumn(name = "ID")
  private List<Actuator> actuators = null;

  public DeviceEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public List<Sensor> getSensors() {
    return sensors;
  }

  public void setSensors(List<Sensor> sensors) {
    this.sensors = sensors;
  }

  public List<Actuator> getActuators() {
    return actuators;
  }

  public void setActuators(List<Actuator> actuators) {
    this.actuators = actuators;
  }
}
