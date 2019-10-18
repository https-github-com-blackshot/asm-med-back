package kz.beeset.med.dmp;

import kz.beeset.med.dmp.repository.ResourceUtilRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableMongoRepositories(repositoryBaseClass = ResourceUtilRepositoryImpl.class)
@EnableMongoAuditing
@ComponentScan(basePackages = {"kz.beeset.med"})
public class DMPApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DMPApplication.class, args);
    }

}
