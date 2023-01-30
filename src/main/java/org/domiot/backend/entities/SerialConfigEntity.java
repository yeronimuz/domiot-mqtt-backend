package org.domiot.backend.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class SerialConfigEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String p1Key;
  private String serialPort;
  private Integer baudRate;

}
