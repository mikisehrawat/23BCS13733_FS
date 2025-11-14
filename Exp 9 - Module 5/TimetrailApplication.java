
package org.example.timetrail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class TimetrailApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimetrailApplication.class, args);
    }

}