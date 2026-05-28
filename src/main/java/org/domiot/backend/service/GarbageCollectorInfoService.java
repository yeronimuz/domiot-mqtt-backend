package org.domiot.backend.service;

import java.lang.management.ManagementFactory;

import javax.management.JMException;
import javax.management.ObjectName;

import org.springframework.stereotype.Service;

@Service
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
