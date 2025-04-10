package org.deliverygo.delivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.deliverygo.delivery")
public class MongoConfig {

}
