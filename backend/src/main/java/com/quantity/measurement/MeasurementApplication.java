package com.quantity.measurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "QuantityMeasurementApp",
        "com.quantity.measurement"
})
@EntityScan("com.quantity.measurement.entity")
@EnableJpaRepositories("com.quantity.measurement.repository")
public class MeasurementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeasurementApplication.class, args);
    }
}