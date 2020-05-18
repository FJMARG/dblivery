package ar.edu.unlp.info.bd2.config;

import ar.edu.unlp.info.bd2.repositories.DBliveryMongoRepository;
import ar.edu.unlp.info.bd2.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public DBliveryService createService() {
        DBliveryMongoRepository repository = this.createRepository();
        return new DBliveryServiceImpl(repository);
    }

    @Bean
    public DBliveryMongoRepository createRepository() {
        return new DBliveryMongoRepository();
    }
}
