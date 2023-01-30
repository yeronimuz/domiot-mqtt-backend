package org.domiot.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * User role; what may a user do.<BR> A user may have more roles. Each role can be configured for CRUD and control actions.
 */
@Entity
@Table(name = "permissions")
public class Permission {

  /**
   * Permission to read location data
   */
  public static final int ROLE_PERMISSION_READ_FLAG = 0x1;

  /**
   * Permission to write location data; new measurements, create users, sensors, actuators
   */
  public static final int ROLE_PERMISSION_WRITE_FLAG = 0x2;

  /**
   * Permission to delete configuration items
   */
  public static final int ROLE_PERMISSION_DELETE_FLAG = 0x4;

  /**
   * Permission to modify Location's settings
   */
  public static final int ROLE_PERMISSION_MODIFY_FLAG = 0x8;

  /**
   * Permission to control actuators
   */
  public static final int ROLE_PERMISSION_CONTROL_FLAG = 0x10;

  public static final int ROLE_PERMISSION_RESERVED1_FLAG = 0x20;

  public static final int ROLE_PERMISSION_RESERVED2_FLAG = 0x40;

  /**
   * This role has admin permissions
   */
  public static final int ROLE_PERMISSION_ADMIN_FLAG = 0x80;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * Name of the role
   */
  private String name;

  /**
   * All user permissions OR-ed together
   */
  // TODO: Shift 1 or 0 into flags, or use masks (also for clearing a flag)
  private int permissionFlags;

  public Permission() {
    // For JPA
  }

  /**
   * Constructor.
   *
   * @param name The name of the role
   */
  public Permission(String name) {
    super();
    this.name = name;
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
   * Get name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Set name.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get read permission.
   *
   * @return the read
   */
  public boolean isReadPermissionSet() {
    return (permissionFlags & ROLE_PERMISSION_READ_FLAG) == ROLE_PERMISSION_READ_FLAG;
  }

  /**
   * Set read permission.
   *
   * @param read the read to set
   */
  public void setReadPermission(boolean read) {
    this.permissionFlags =
        (read) ? (permissionFlags | ROLE_PERMISSION_READ_FLAG)
            : (permissionFlags & ~ROLE_PERMISSION_READ_FLAG);
  }

  /**
   * Get write permission status.
   *
   * @return the write
   */
  public boolean isWritePermissionSet() {
    return (permissionFlags & ROLE_PERMISSION_WRITE_FLAG) == ROLE_PERMISSION_WRITE_FLAG;
  }

  /**
   * Set write permission.
   *
   * @param write the write to set
   */
  public void setWritePermission(boolean write) {
    this.permissionFlags = (write) ? (permissionFlags | ROLE_PERMISSION_WRITE_FLAG)
        : (permissionFlags & ~ROLE_PERMISSION_WRITE_FLAG);
  }

  /**
   * Get delete permission flag value.
   *
   * @return the delete
   */
  public boolean isDeletePermissionSet() {
    return (permissionFlags & ROLE_PERMISSION_DELETE_FLAG) == ROLE_PERMISSION_DELETE_FLAG;
  }

  /**
   * Set delete.
   *
   * @param delete the delete to set
   */
  public void setDeletePermission(boolean delete) {
    this.permissionFlags = (delete) ? (permissionFlags | ROLE_PERMISSION_DELETE_FLAG)
        : (permissionFlags & ~ROLE_PERMISSION_DELETE_FLAG);
  }

  /**
   * Get modify.
   *
   * @return the modify
   */
  public boolean isModifyPermissionSet() {
    return (permissionFlags & ROLE_PERMISSION_MODIFY_FLAG) == ROLE_PERMISSION_MODIFY_FLAG;
  }

  /**
   * Set modify.
   *
   * @param modify the modify to set
   */
  public void setModifyPermission(boolean modify) {
    this.permissionFlags = (modify) ? (permissionFlags | ROLE_PERMISSION_MODIFY_FLAG)
        : (permissionFlags & ~ROLE_PERMISSION_MODIFY_FLAG);
  }

  /**
   * Get control.
   *
   * @return the control
   */
  public boolean isControlPermissionSet() {
    return (permissionFlags & ROLE_PERMISSION_CONTROL_FLAG) == ROLE_PERMISSION_CONTROL_FLAG;
  }

  /**
   * Set control.
   *
   * @param control the control to set
   */
  public void setControlPermission(boolean control) {
    this.permissionFlags = (control) ? (permissionFlags | ROLE_PERMISSION_CONTROL_FLAG)
        : (permissionFlags & ~ROLE_PERMISSION_CONTROL_FLAG);
  }

  /**
   * Get all the flags.
   *
   * @return The permission flags
   */
  public int getPermissionFlags() {
    return permissionFlags;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Role [id=" + id + ", name=" + name + ", permissionFlags=" + permissionFlags + "]";
  }
}
