package org.domiot.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.security.Principal;
import java.util.List;

/**
 * User in the domotics service.
 */
@Entity(name = "users")
public class UserEntity implements Principal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String userName;

  @ManyToOne
  @JoinColumn(name = "ID")
  private SiteEntity siteEntity;

  @OneToMany
  @JoinColumn(name = "ID")
  private List<Permission> permissions;


  public UserEntity() {
    // For JPA
  }


  /**
   * Constructor.
   *
   * @param userName user name
   */
  public UserEntity(String userName, SiteEntity siteEntity) {
    this.userName = userName;
    this.siteEntity = siteEntity;
  }


  /**
   * Get id.
   *
   * @return the id
   */
  public int getId() {
    return this.id;
  }


  /**
   * Get userName.
   *
   * @return the userName
   */
  public String getUserName() {
    return userName;
  }


  /**
   * Set userName.
   *
   * @param userName the userName to set
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }


  /**
   * Get siteId.
   *
   * @return the location
   */
  public SiteEntity getSiteId() {
    return siteEntity;
  }


  /**
   * Set siteId.
   *
   * @param siteEntity the site to set
   */
  public void setSiteId(SiteEntity siteEntity) {
    this.siteEntity = siteEntity;
  }


  /**
   * Get permissions.
   *
   * @return the roles
   */
  public List<Permission> getPermissions() {
    return this.permissions;
  }


  @Override
  public String getName() {
    return getUserName();
  }
}
