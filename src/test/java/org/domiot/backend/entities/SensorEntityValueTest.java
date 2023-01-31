package org.domiot.backend.entities;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import java.util.Date;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SensorEntityValueTest
{

    @Test
    public void testEquals() {
        SensorNode sensorNode1 = new SensorNode("AA:BB:CC:DD", 1);
        SensorNode sensorNode2 = new SensorNode("AA:BB:CC:DD", 1);
        SensorValue sensorValueOne = new SensorValue(sensorNode1, new Date(), 2, 1.0);
        SensorValue sensorValueTwo = new SensorValue(sensorNode1, new Date(), 2, 1.0);
        
        assertThat(sensorValueOne.equals(sensorValueTwo), is(true));
        assertThat(sensorValueOne.equalsInType(sensorValueTwo), is(true));
        
        SensorNode sensorNode3 = new SensorNode("BB:CC:DD:EE", 1);
        SensorValue sensorValueThree = new SensorValue(sensorNode3, new Date(), 2, 1.0);
        assertThat(sensorValueOne.equalsInType(sensorValueThree), is(false));
        
        sensorValueThree = new SensorValue(sensorNode2, new Date(), 2, 1.1);
        assertThat(sensorValueThree.equals(sensorValueTwo), is(false));
        assertThat(sensorValueOne.equalsInType(sensorValueThree), is(true));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void nullValueSensorTest() {
        SensorNode sensorNode = null;
        SensorValue sensorValue = new SensorValue(sensorNode, new Date(), 2, 1.0);
    }
}
