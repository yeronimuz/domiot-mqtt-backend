package org.domiot.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * A location represents a domotics site.
 */
@Entity(name = "sites")
public class SiteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  String name;
  /**
   * User defined free text
   */
  String description;

  @Temporal(value = TemporalType.TIMESTAMP)
  Date dtCreated;
  /**
   * All users that have access to a location's data, at least one needs to have admin rights
   */
  @OneToMany
  @JoinColumn(name = "ID")
  List<UserEntity> users;

  /**
   * All sensors in this location
   */
  @OneToMany
  @JoinColumn(name = "ID")
  List<DeviceEntity> deviceEntities;

  /**
   * All actuators in this location
   */
  @OneToMany
  @JoinColumn(name = "ID")
  List<ActuatorEntity> actuatorEntities;

  /**
   * MqttConfigEntity is site specific. Devices should inherit this config from Site.
   */
  @OneToOne
  @JoinColumn(name = "ID")
  private MqttConfigEntity mqttConfigEntity;

  // TODO: SerialConfig, PowermeterConfig should not be in Site

  public SiteEntity() {
    // required for JPA
  }

  public SiteEntity(String description) {
    this.description = description;
  }


  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getDtCreated() {
    return dtCreated;
  }

  public void setDtCreated(Date dtCreated) {
    this.dtCreated = dtCreated;
  }

  public List<UserEntity> getUserList() {
    return users;
  }

  public void setUserList(List<UserEntity> userList) {
    this.users = userList;
  }

  public List<DeviceEntity> getDeviceEntities() {
    return deviceEntities;
  }

  public void setDeviceEntities(List<DeviceEntity> deviceEntities) {
    this.deviceEntities = deviceEntities;
  }

  public List<ActuatorEntity> getActuatorList() {
    return actuatorEntities;
  }

  public void setActuatorList(List<ActuatorEntity> actuatorEntityList) {
    this.actuatorEntities = actuatorEntityList;
  }
}
