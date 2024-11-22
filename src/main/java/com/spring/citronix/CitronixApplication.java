package com.spring.citronix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(excludeName = {
        "jdbcConnectionDetailsForCitronixPostgres1",
        "jdbcConnectionDetailsForCitronixPostgresql1"
})
public class CitronixApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitronixApplication.class, args);
    }

}
