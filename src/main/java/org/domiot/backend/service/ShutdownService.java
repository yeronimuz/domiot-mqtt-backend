package org.domiot.backend.service;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.util.JvmMemoryUtil;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShutdownService {

    @PreDestroy
    public void destroy() throws Exception {
        log.info("Shutting down...");
        JvmMemoryUtil.logMemoryStatistics();
        log.info("Waiting for other threads...");
        Thread.sleep(5000);
    }
}
