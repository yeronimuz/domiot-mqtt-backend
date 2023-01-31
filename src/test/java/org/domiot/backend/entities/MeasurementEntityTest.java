package org.domiot.backend.entities;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.domiot.backend.entities.MeasurementEntity;
import org.domiot.backend.entities.MeasurementType;
import org.domiot.backend.entities.SensorEntity;
import org.domiot.backend.entities.SensorType;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class MeasurementEntityTest
{
    private static final Logger               LOG = LoggerFactory.getLogger(MeasurementEntityTest.class);
    private              EntityManagerFactory entityManagerFactory;

    private              EntityManager entityManager;
    private static final String        PERSISTENCE_UNIT = "measurements-pu";
    
    @Before
    public void testSetup() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        entityManager = entityManagerFactory.createEntityManager();
    }
    
    @Test
    public void testMeasurementWithSensor() throws JsonParseException, JsonMappingException, IOException {
        Date now = new Date();
        SensorEntity sensorEntity = new SensorEntity(SensorType.POWER_METER, "01:02:03:04", "mainPowerSensor", "meterkast");
        MeasurementEntity meas = new MeasurementEntity(sensorEntity, now, MeasurementType.CONSUMED_GAS, 2.0);

        entityManager.getTransaction().begin();
        entityManager.persist(sensorEntity);
        entityManager.persist(meas);
        entityManager.getTransaction().commit();

        TypedQuery<MeasurementEntity> query = entityManager.createQuery("SELECT m FROM measurements m", MeasurementEntity.class);
        List<MeasurementEntity> measurements = query.getResultList();
        
        assertThat(measurements.size(), is(1));
        assertThat(measurements.get(0).getMeasurementType(), CoreMatchers.is(MeasurementType.CONSUMED_GAS));
        SensorEntity sensorEntityDb = measurements.get(0).getSensor();
        assertThat(sensorEntityDb, is(notNullValue()));
        assertThat(sensorEntityDb.getMacAddress(), is("01:02:03:04"));
    }
    
    @After
    public void doCleanup() {
        entityManager.close();
        entityManagerFactory.close();
    }
}
