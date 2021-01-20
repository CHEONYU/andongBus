package co.kr.smartplusteam.luna.study.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Slf4j
@Configuration
public class MongoDBConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongodb_uri;

    @Bean(name = "mongoTemplate")
    public MongoTemplate createMongoTemplate()  {
        log.info("MongoDB Template Connection URL = {}", mongodb_uri);
        MongoClient mongoClient = MongoClients.create(mongodb_uri);
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient, "andongBus");
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return new MongoTemplate(factory, converter);
    }
}
