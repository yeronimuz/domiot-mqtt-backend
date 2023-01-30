package org.domiot.backend.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity that is able to control peripherals at a Location.<BR> Actuator is always regarded separately from a Sensor, even when
 * combined in one case.
 */
@Entity
@Table(name = "actuators")
public class ActuatorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "ID")
  private SiteEntity siteEntity;

  // TODO: Accompanied sensor (0 or more)
  // TODO: Status
  // TODO: params, values

  public ActuatorEntity() {
    // TODO Auto-generated constructor stub
  }

  /**
   * Get id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Set id.
   *
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get the location.
   *
   * @return location
   */
  public SiteEntity getLocation() {
    return siteEntity;
  }
}
