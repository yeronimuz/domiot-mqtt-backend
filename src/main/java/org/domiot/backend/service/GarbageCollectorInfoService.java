package org.domiot.backend.service;

import javax.management.JMException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class GarbageCollectorInfoService {

    private GarbageCollectorInfoService() {
    }

    public static String getGCInfo() throws JMException {
        String histogram = (String) ManagementFactory.getPlatformMBeanServer().invoke(
                new ObjectName("com.sun.management:type=DiagnosticCommand"),
                "gcClassHistogram",
                new Object[]{null},
                new String[]{"[Ljava.lang.String;"});
        return histogram;
    }
}
