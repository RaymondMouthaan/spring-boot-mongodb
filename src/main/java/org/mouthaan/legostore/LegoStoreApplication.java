package org.mouthaan.legostore;

import com.github.mongobee.Mongobee;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class LegoStoreApplication {

    private final MongoTemplate mongoTemplate;

    public LegoStoreApplication(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(LegoStoreApplication.class, args);
    }

    @Bean
    public Mongobee mongobee() {
        Mongobee runner = new Mongobee(
                "mongodb://localhost:27017/legostore"
        );
        runner.setMongoTemplate(mongoTemplate);
        runner.setChangeLogsScanPackage("org.mouthaan.legostore.persistence");
        return runner;
    }
}
