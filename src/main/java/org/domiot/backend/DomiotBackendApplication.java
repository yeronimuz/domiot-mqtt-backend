package org.domiot.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DomiotBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(DomiotBackendApplication.class, args);
    }

}
