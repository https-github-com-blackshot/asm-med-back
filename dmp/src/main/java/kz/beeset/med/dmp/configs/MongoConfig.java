package kz.beeset.med.dmp.configs;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.defaultbucket}")
    private String defaultbucket;

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(host, port);
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    public String getDefaultBucket() {
        return defaultbucket;
    }

    @Bean
    public GridFsTemplate gridFsTemplate() {
        try {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(), defaultbucket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
