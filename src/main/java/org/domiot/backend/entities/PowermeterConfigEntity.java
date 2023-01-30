package org.domiot.backend.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

public class PowermeterConfigEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private Integer repeatValuesAfter;
  private Integer internalQueueSize;

  @OneToOne
  private SerialConfigEntity serialConfigEntity;

}
