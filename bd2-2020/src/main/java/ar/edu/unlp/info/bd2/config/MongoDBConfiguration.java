package ar.edu.unlp.info.bd2.config;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MongoDBConfiguration {

    @Bean
    public MongoDatabase mongoDatabase() {
        try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
            return client.getDatabase("bd2_grupo" + this.getGroupNumber());
        }
    }

    @Bean
    @Qualifier("mongoPojoDatabase")
    public MongoClient mongoPojoDatabase() {
        CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClient client =
                MongoClients.create(MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).build());
        return client;
    }

    private Integer getGroupNumber() {
        return 21;
    }
}