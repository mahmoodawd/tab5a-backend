package dev.awd.tab5abackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Tab5aBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tab5aBackendApplication.class, args);
    }

}
